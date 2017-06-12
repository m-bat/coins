/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * TagTreeBuilder.java
 *
 * Created on July 28, 2003, 4:07 PM
 */

package coins.alias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import coins.HirRoot;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirVisitorModel2;
import coins.ir.hir.PointedExp;
import coins.ir.hir.QualifiedExp;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubpNode;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.VarNode;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;

/**
 * Builds tag tree and assigns tags to HIR nodes.
 * @author  hasegawa
 */
class TagTreeBuilder
  extends HirVisitorModel2
{
  private SubpDefinition fSubpDef;

  private MyExpId fMyExpIds[];

  /**
   * Total number of Tags that have a bit position assigned
   * in the TagVector.
   */
  int fTagBitCount = 0;

  private Map fHIRToTag = new HashMap(50);

  /**
   * <code>Map</code> from malloc invocation HIR nodes to
   * corresponding Tags.
   */
  Map fHIRTomallocTag = new HashMap(5);

  /**
   * Tags corresponding to each named variable and
   * allocated area and unknown external area.
   * The whole area in memory that can be referred to by
   * the subprogram under consideraion is covered by
   * the tags that are in this <code>List</code>.
   */
  List fParentlessTags = new ArrayList();
  private AliasFactory fFactory;

  private final boolean fIsOptimistic;

  /** Creates a new instance of TagTreeBuilder
   *
   * @param pSubpDef the <code>SubpDefinition</code> object
   * this module is responsible for.
   * @param pMyExpIds the array of <code>MyExpId</code>
   * objects, with HIR node's index as its index.
   * This serves as the map from HIR nodes to
   * <code>MyExpId</code> objects.
   * @param pHirRoot the <code>HirRoot</code> object
   * shared by every module in the program.
   */
  TagTreeBuilder(SubpDefinition pSubpDef, MyExpId pMyExpIds[],
                 boolean pIsOptimistic, HirRoot pHirRoot)
  {
    super(pHirRoot);
    fSubpDef = pSubpDef;
    fMyExpIds = pMyExpIds;
    fFactory = new AliasFactory(hirRoot);
    fIsOptimistic = pIsOptimistic;
  }

  /**
   * Builds the Tag tree. It creates a Tag for each
   * MyExpId that is lvalue, builds tree relation between
   *  those Tags, and makes a <code>Map</code> from HIR
   * nodes to such Tags.
   *
   * @return the <code>Map</code> from HIR nodes to Tags.
   */
  Map process()
  {
    visit(fSubpDef.getHirBody());
    fTagBitCount++; // For "other" addresses.
    //		fParentlessTags.add(fFactory.tag(Tag.KIND_OTHER));
    fParentlessTags.add(fFactory.otherTag());
    return fHIRToTag;
  }

  /**
   * Creates a Tag for the <code>Var</code> attached to
   * the specified argument if it is not yet done,
   * and registers the node-Tag correspondence into
   * a global map.
   *
   * @param pVarNode the <code>VarNode</code> the
   * <code>Var</code> attached to which is going to
   * be assigned a Tag.
   */
  public void atVarNode(VarNode pVarNode)
  {
    int lBitPos;
    Tag lTag, lParentTag;
    int lKind, lStorageClass;

    if (fMyExpIds[pVarNode.getIndex()].fTag == null) {
      //			System.out.println(pVarNode + "'s tag
      //        not yet assigned. Assigning...");
      lKind = tagKind(pVarNode.getType());
      lStorageClass = storageClass(pVarNode.getVar());
      lTag = fMyExpIds[pVarNode.getIndex()].fTag = fFactory.tag(fMyExpIds[
        pVarNode.getIndex()], true, lKind, lStorageClass);
      lTag.fIsUnique = true;
      fTagBitCount++;
      fParentlessTags.add(lTag);
    }
    //		System.out.println("tag is " + fMyExpIds[pVarNode.getIndex()].fTag);
    fHIRToTag.put(pVarNode, fMyExpIds[pVarNode.getIndex()].fTag);
  }

  /**
   * Visits the children of the specified argument,
   * and if the specified argument is not lvalue, creates
   * a Tag for the <code>MyExpId</code> attached to
   * the specified argument if it is not yet done,
   * and registers the node-Tag correspondence into a global map.
   *
   * @param pExp the <code>SubscriptedExp</code>
   * the <code>MyExpId</code> attached to which is going
   * to be assigned a Tag.
   */
  public void atSubscriptedExp(SubscriptedExp pExp)
  {
    int lBitPos;
    Tag lTag, lParentTag;
    Exp lArrayExp = pExp.getArrayExp();
    VectorType lArrayType = (VectorType)lArrayExp.getType();
    Exp lSubscriptExp = pExp.getSubscriptExp();

    visitChildren(pExp);

    if (lArrayExp.getOperator() == HIR.OP_CONST) // String literal array.
      return;
    lParentTag = fMyExpIds[lArrayExp.getIndex()].fTag;
    if (lParentTag == null) // not lvalue. An array node that

      // stems from a function call (e.g. f().a[1]) is
      // not considered lvalue (although it is in C).
      return;
    if ((lTag = fMyExpIds[pExp.getIndex()].fTag) == null) {
      //			lParentTag = fMyExpIds[lArrayExp.getIndex()].fTag;
      //            if (lParentTag == null) // not lvalue.
      //                return;
      if (!fIsOptimistic &&
          (lSubscriptExp.getOperator() != HIR.OP_CONST ||
           !AliasUtil.
           subscriptCheck(lArrayType, ((ConstNode)lSubscriptExp).getIntValue())))
        lTag = fFactory.tag(fMyExpIds[pExp.getIndex()], false,
          tagKind(pExp.getType()), lParentTag.fStorageClass); // Storage class will be the same as that of the vector
      else {
        if (!lParentTag.fIsUnique ||
            lSubscriptExp.getOperator() != HIR.OP_CONST)
          ; //lBitPos = lParentTag.fBitPos;
        else if (lParentTag.getFlag(Tag.HAS_UNION_ANCESTOR)) // Handling of union.
          ;
        else
          fTagBitCount++;
        lTag = fFactory.tag(fMyExpIds[pExp.getIndex()], lParentTag.isAnchored(),
          tagKind(pExp.getType()), lParentTag.fStorageClass);
        lTag.fParent = lParentTag;
        lParentTag.fChildren.add(lTag);
        lTag.inheritAttributes();
        lTag.fIsUnique = !lParentTag.getFlag(Tag.HAS_UNION_ANCESTOR) &&
          lParentTag.fIsUnique && lSubscriptExp.getOperator() == HIR.OP_CONST;
      }
      fMyExpIds[pExp.getIndex()].fTag = lTag;
    }
    fHIRToTag.put(pExp, lTag);
  }

  /**
   * Visits the children of the specified argument, and
   * if the specified argument is lvalue, creates a Tag for
   * the <code>MyExpId</code> attached to the specified argument
   * if it is not yet done, and registers the node-Tag
   * correspondence into a global map.
   *
   * @param pExp the <code>QualifiedExp</code> the
   * <code>MyExpId</code> attached to which is going
   * to be assigned a Tag.
   */
  public void atQualifiedExp(QualifiedExp pExp)
  {
    int lBitPos;
    Tag lTag, lParentTag;

    visit((Exp)pExp.getChild1());
    lParentTag = fMyExpIds[pExp.getChild1().getIndex()].fTag;
    if (lParentTag == null) // not lvalue.
      return;
    if ((lTag = fMyExpIds[pExp.getIndex()].fTag) == null) {
      //            lParentTag = fMyExpIds[pExp.getChild1().getIndex()].fTag;
      if (!lParentTag.fIsUnique)
        ;
      else if (lParentTag.getFlag(Tag.HAS_UNION_ANCESTOR)) // Handling of union.
        ;
      else
        fTagBitCount++;
      lTag = fFactory.tag(fMyExpIds[pExp.getIndex()], lParentTag.isAnchored(),
        tagKind(pExp.getType()), lParentTag.fStorageClass);
      lTag.fParent = lParentTag;
      lParentTag.fChildren.add(lTag);
      lTag.inheritAttributes();
      lTag.fIsUnique = !lParentTag.getFlag(Tag.HAS_UNION_ANCESTOR) &&
        lParentTag.fIsUnique;
      fMyExpIds[pExp.getIndex()].fTag = lTag;
    }
    fHIRToTag.put(pExp, lTag);

  }

  /**
   * Visits the children of the specified argument, creates
   * a Tag for the <code>MyExpId</code> attached to the
   * specified argument if it is not yet done, and registers
   * the node-Tag correspondence into a global map.
   *
   * @param pExp the <code>PointedExp</code> the
   * <code>MyExpId</code> attached to which is going to be
   * assigned a Tag.
   */
  public void atPointedExp(PointedExp pExp)
  {
    int lBitPos;
    Tag lTag, lParentTag;

    visit((Exp)pExp.getChild1());

    if ((lTag = fMyExpIds[pExp.getIndex()].fTag) == null) {
      lTag = fFactory.tag(fMyExpIds[pExp.getIndex()], false,
        tagKind(pExp.getType()), Tag.STO_ROOT);
      fMyExpIds[pExp.getIndex()].fTag = lTag;
    }
    fHIRToTag.put(pExp, lTag);
  }

  /**
   * Visits the children of the specified argument, creates
   * a Tag for the <code>MyExpId</code> attached to
   * the specified argument if it is lvalue and not yet done,
   * and registers the node-Tag correspondence into a global map.
   *
   * @param pExp the <code>Exp</code> the <code>MyExpId</code>
   * attached to which is going to be assigned a Tag.
   */
  public void atExp(Exp pExp)
  {
    int lOpCode = pExp.getOperator();

    switch (lOpCode) {
      case HIR.OP_CONTENTS:
        if (pExp.getType().getTypeKind() != Type.KIND_SUBP) {
          if (fMyExpIds[pExp.getIndex()].fTag == null)
            fMyExpIds[pExp.getIndex()].fTag = fFactory.tag(fMyExpIds[pExp.
              getIndex()], false, tagKind(pExp.getType()), Tag.STO_ROOT);
          fHIRToTag.put(pExp, fMyExpIds[pExp.getIndex()].fTag);
        }
        visitChildren(pExp);
        break;
      case HIR.OP_UNDECAY:
        if (fMyExpIds[pExp.getIndex()].fTag == null) {
          Tag lTag = fMyExpIds[pExp.getIndex()].fTag = fFactory.tag(fMyExpIds[
            pExp.getIndex()], false, tagKind(pExp.getType()), Tag.STO_ROOT);
          lTag.fIsUnique = false;
          //              fTagBitCount++;
          //               fParentlessTags.add(lTag);
        }
        fHIRToTag.put(pExp, fMyExpIds[pExp.getIndex()].fTag);
//                System.out.println(fMyExpIds[pExp.getIndex()].fTag);
        visitChildren(pExp);
        break;
      default:
        visitChildren(pExp);
        break;

    }
  }

  /**
   * Visits the children of the specified argument, and, if
   * the specified argument is the C malloc library function
   * call node, creates a Tag for the area allocated by the
   * call and registers the node-Tag correspondence into a global map.
   *
   * @param pExp the <code>FunctionExp</code> the area
   * allocated by which is going to be assigned a Tag.
   */
  public void atFunctionExp(FunctionExp pExp)
  {
    HIR lFuncSpec = pExp.getFunctionSpec();
    HIR lSubpNode;
    //                System.out.println("subp " + pExp.get);
    if (lFuncSpec.getOperator() == HIR.OP_ADDR &&
        (lSubpNode = (HIR)lFuncSpec.getChild1()).getOperator() == HIR.OP_SUBP &&
        AliasUtil.ismalloc(((SubpNode)lSubpNode).getSubp())) {
      Tag lTag = fFactory.mallocTag(pExp);
      fHIRTomallocTag.put(pExp, lTag);
      fTagBitCount++;
      fParentlessTags.add(lTag);
    }
    visitChildren(pExp);
  }

  private static int tagKind(Type pType)
  {
    switch (pType.getTypeKind()) {
      case Type.KIND_VECTOR:
        return Tag.KIND_VECTOR;
      case Type.KIND_STRUCT:
        return Tag.KIND_STRUCT;
      case Type.KIND_UNION:
        return Tag.KIND_UNION;
      default:
        return Tag.KIND_ATOMIC;
    }
  }

  private static int storageClass(Var pVar)
  {
    switch (pVar.getStorageClass()) {
      case Var.VAR_AUTO:
      case Var.VAR_REGISTER:
        return Tag.STO_CUR_FRAME;
      case Var.VAR_STATIC:
        return Tag.STO_STATIC;
      default:
        throw new AliasError("Unexpected");
    }
  }
}
