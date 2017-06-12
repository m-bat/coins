/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * AliasAnalHir2.java
 *
 * Created on July 15, 2003, 1:39 PM
 */

package coins.alias.alias2;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import coins.HirRoot;
import coins.IoRoot;
import coins.alias.AliasAnalHir1;
import coins.alias.AliasError;
import coins.alias.AliasGroup;
import coins.alias.AliasUtil;
import coins.alias.util.BriggsSet;
import coins.alias.util.Scanner;
import coins.ir.hir.Exp;
import coins.ir.hir.ForStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.HirVisitorModel2;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.RepeatStmt; //##57
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.WhileStmt;
import coins.sym.Elem;
import coins.sym.Label;
import coins.sym.StructType;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;

/**
 * <p>Alias analysis level 2 implementation.</p>
 * <p>Alias analysis level 2 is a flow-sensitive intraprocedural
 * analysis.</p>
 * <p>This class drives the alias analysis and also implements
 * AliasAnal interface.</p>
 * <p>Flow sensitive analysis is performed only for structured
 * subprograms, which don't contain unstructed jumps
 * (jumps that do not correpond to C's break/continue).
 * If the subprogram is unstructured, the analysis is
 * reverted to that of level 1 (flow-insensitive analysis).
 * <p>Two modes (Optimistic and Pessimistic), or options,
 * for the analysis are available, each of which uses
 * different assumptions about aliasing.</p>
 * <p>Handling of source languages other than C is
 * not supported in this version.</p>
 *
 * @author  hasegawa
 */
public class AliasAnalHir2
  extends AliasAnalHir1
{
  protected static final int AREA_INCLUDES = 1, AREA_OVERLAPS = 2,
    MUST_ALIAS = AREA_INCLUDES | AREA_OVERLAPS, NOT_ALIAS = 4,
    MAY_ALIAS = MUST_ALIAS | NOT_ALIAS;

  protected static final String PREDEFINED = "aliaspredefined";

//    private SubpDefinition fSubpDef;

  /**
   * Set of predefined functions.
   */
  protected Set fPredefined;

  /**
   * Map that maps HIR nodes to TagVectors.
   */
  Map fHIRToLoc;

  private Map fHIRToAliasGroup = new HashMap(); // map of HIR
  // nodes to the set of HIR nodes which may be aliased to
  // the given HIR node.

  private Set fAccessedVars; // Accessed named variables
  private Set fAccessedElems; // Accessed structure elements
  private Set fAccessedConstSubscripts; // Accessed const subscripts
  private Set fmallocs; // malloc invocations

  TagVector2 fLocalRootVects[]; // Array of TagVector2s where for
  // each bit the BitVector for the enclosing Var/malloc/Ext
  //object corresponds.

  /**
   * Number of bit-allocated memory objects. This determines
   * the length of the <code>TagVector2</code>.
   */
  int fTagBitCount;

  /**
   * The points-to graph represented as an array of
   * <code>TagVector2</code>s.
   */
  TagVector2 fPointsTo[];

  private TagVector2 fNonNullInitials;
  private TagVector2 fInitialAddressTakens;

  /**
   * <code>TagVector2</code> that corresponds to global variables.
   */
  TagVector2 fGlobals;

  /**
   * <code>TagVector2</code> that corresponds to the area
   * visible from the outside of the current subprogram.
   */
  TagVector2 fExternOpt;

  /**
   * The <code>HirRoot</code> object shared by every module in
   * the program.
   */
  public final HirRoot hirRoot;

  /**
   * The <code>IoRoot</code> object shared by every module in
   * the program.
   */
  public final IoRoot ioRoot;

  /**
   * Alias analysis option. This determines the set of assumptions
   * on which the alias analysis is based.
   *
   * @see #AliasAnalHir2(boolean, HirRoot)
   */
  final boolean fIsOptimistic;

  /**
   * Factory object used to create other objects.
   */
  final AliasFactory2 fFactory;

  /**
   * AliasUtil object used to access utility methods.
   */
  protected final AliasUtil fUtil;

  private TagVector2 fFullVect;

  Map fVarToLoc; // = new HashMap();
  Map fSubscriptToMask; // = new HashMap();
  Map fElemToMask; // = new HashMap();
  Map fmallocToLoc; // = new HashMap();

  private Map fVectToParam; // = new HashMap(); // used for optimistic

  private boolean fUseLevel1;

  /**
   * Creates a new instance of <code>AliasAnalHir2</code> that performs
   * alias analysis based on some set of assumptions about aliasing.
   * Two sets of assumptions (Pessimistic and Optimistic) are
   * available, and which one to use is determined from
   * the command line, and defaults to Pessimistic.
   *
   * @param pHirRoot the <code>HirRoot</code> object shared
   *   by every module in the program.
   * @see #AliasAnalHir2(boolean, HirRoot)
   */
  public AliasAnalHir2(HirRoot pHirRoot)
  {
    this(pHirRoot.ioRoot.getCompileSpecification().getCoinsOptions().getArg(
      "alias") != null ?
         pHirRoot.ioRoot.getCompileSpecification().
         getCoinsOptions().getArg("alias").equals("opt") : false, pHirRoot);
  }

  /**
   * Creats a new instance of <code>AliasAnalHir2</code>
   * that performs alias analysis with the specified
   * assumptions (<code>pIsOptimistic</code>) about aliasing.
   * The difference between optimistic and pessimistic
   * assumptions are:
   * <ol>
   * <li>Whether inconsistent object access (access by
   * an object whose type differs from that of the stored object)
   * may happen</li>.
   * <li>Whether parameters may initially pointing to objects
   * that are elements of the same array object.</li>
   * </ol>
   */
  public AliasAnalHir2(boolean pIsOptimistic, HirRoot pHirRoot)
  {
    super(pIsOptimistic, pHirRoot);
    hirRoot = pHirRoot;
    ioRoot = hirRoot.ioRoot;
    fFactory = new AliasFactory2(hirRoot);
    fUtil = new AliasUtil(hirRoot.symRoot);
    fIsOptimistic = pIsOptimistic;
    readPredefined(ioRoot);
    dbg(1, "AliasAnalHir2", "optimistic=" + pIsOptimistic); //##56
  }

  private Set readPredefined(IoRoot pIo)
  {
    Set lPredefineds = new HashSet();
    /* //##71
    File lLibDir = pIo.getCompileSpecification().getCoinsOptions().getLibDir();
    File lPredefined = null;
    try {
      lPredefined = new File(lLibDir.getCanonicalPath() + File.separator +
        PREDEFINED);

      FileReader lReader = new FileReader(lPredefined);

      char lBuff[] = new char[72];

      int i = 0;
      while ((lBuff[i++] = (char)lReader.read()) != (char) - 1)
        if (lBuff[i - 1] != '\r' && lBuff[i - 1] != '\n')
          ; //System.out.println("lBuff[" + (i - 1) + "] = " + (int)lBuff[i - 1]);
        else {
          String lEntry = new String(lBuff, 0, i - 1).trim();
          if (lEntry.length() != 0)
            lPredefineds.add(lEntry);
          i = 0;
        }
      String lEntry0 = new String(lBuff, 0, i - 1).trim();
      if (lEntry0.length() != 0)
        lPredefineds.add(lEntry0);

    }
    catch (Exception e) {
      dbg(4, "AliasAnalHir2",
          "Cannot read list of predefined functions (\"" + lPredefined
          + "\"). \n" + "No predefined functions are assumed."); //##57
    }
    */ //##71
    //##71 BEGIN
    lPredefineds.addAll(
      hirRoot.symRoot.sourceLanguage.functionsWithoutSideEffect);
    dbg(2, "AliasAnalHir2",
      "functionsWithoutSideEffect" + lPredefineds);
    //##71 END
    return fPredefined = lPredefineds;

  }

  /**
   * <p>Performs alias analysis for the given SubpDefinition
   * argument so that may/mustAlias methods for nodes
   * contained in the SubpDefinition are ready.</p>
   * <p>This method may be called only once in the
   * lifetime of this analysis object; for analyses
   * of different SubpDefinitions, the AliasAnalHir2
   * object has to be newly created.</p>
   *
   * @param pSubpDef the <code>SubpDefinition</code>
   * instance nodes contained in which are to be analyzed
   * for aliasing.
   */
  public void prepareForAliasAnalHir(SubpDefinition pSubpDef)
  {
    if (!jumpCheck(pSubpDef)) {
      fUseLevel1 = true;
      super.prepareForAliasAnalHir(pSubpDef);
      return;
    }
    fUseLevel1 = false;
    init();
    prepare(pSubpDef);
    constructPointsToGraph(pSubpDef);
    buildAliasGroups(pSubpDef);
  }

  boolean jumpCheck(SubpDefinition pSubpDef)
  {
    return new JumpChecker(pSubpDef).go();
  }

  private class JumpChecker
    extends HirVisitorModel2
  {
    private boolean fUnstructuredJumpDetected;

    List fLoopStack = new ArrayList();
    List fSwitchStack = new ArrayList();

    SubpDefinition fSubpDef;

    private JumpChecker(SubpDefinition pSubpDef)
    {
      super(AliasAnalHir2.this.hirRoot);
      fSubpDef = pSubpDef;
    }

    private boolean go()
    {
      visit(fSubpDef.getHirBody());
      return!fUnstructuredJumpDetected;
    }

    public void atForStmt(ForStmt pFor)
    {
      if (!pFor.isSimpleForLoop()) {
        fUnstructuredJumpDetected = true;
        return;
      }
      fLoopStack.add(pFor);
      visitChildren(pFor);
      fLoopStack.remove(fLoopStack.size() - 1);
    }

    public void atLoopStmt(LoopStmt pFor)
    {
      fLoopStack.add(pFor);
      visitChildren(pFor);
      fLoopStack.remove(fLoopStack.size() - 1);
    }

    public void atRepeatStmt(RepeatStmt pFor) //##57
    {
      if (!pFor.isSimpleRepeatLoop()) { //##57
        fUnstructuredJumpDetected = true;
        return;
      }
      fLoopStack.add(pFor);
      visitChildren(pFor);
      fLoopStack.remove(fLoopStack.size() - 1);
    }

    public void atWhileStmt(WhileStmt pFor)
    {
      if (!pFor.isSimpleWhileLoop()) {
        fUnstructuredJumpDetected = true;
        return;
      }
      fLoopStack.add(pFor);
      visitChildren(pFor);
      fLoopStack.remove(fLoopStack.size() - 1);
    }

    public void atSwitchStmt(SwitchStmt pSwitch)
    {
      fSwitchStack.add(pSwitch);
      visitChildren(pSwitch);
      fSwitchStack.remove(pSwitch);
    }

    public void atJumpStmt(JumpStmt pJumpStmt)
    {
      Label lLabel = pJumpStmt.getLabel();

      switch (lLabel.getLabelKind()) {
        case Label.LOOP_END_LABEL:
        case Label.LOOP_STEP_LABEL:
          if ((fLoopStack.size() > 0) && //##57
              //##57 if (((LoopStmt)fLoopStack.get(fLoopStack.size() - 1)).getLabel() == lLabel)
              (((LoopStmt)fLoopStack.get(fLoopStack.size() - 1))
               .getLabel() == lLabel)) //##57
            return;
        case Label.ELSE_LABEL:
        case Label.END_IF_LABEL:
        case Label.JUMP_LABEL:
        case Label.LOOP_BACK_LABEL:
        case Label.LOOP_BODY_LABEL:
        case Label.LOOP_COND_INIT_LABEL:
        case Label.SOURCE_LABEL:
        case Label.SWITCH_CASE_LABEL:
        case Label.SWITCH_DEFAULT_LABEL:
        case Label.THEN_LABEL:
        case Label.UNCLASSIFIED_LABEL:
          fUnstructuredJumpDetected = true;
          return;
        case Label.SWITCH_END_LABEL:
          if (((SwitchStmt)fSwitchStack.get(fSwitchStack.size() - 1)).
              getEndLabel() == lLabel)
            return;
          else {
            fUnstructuredJumpDetected = true;
            return;
          }

      }
    }
  }

  private void init()
  {
    fVarToLoc = new HashMap();
    fSubscriptToMask = new HashMap();
    fElemToMask = new HashMap();
    fmallocToLoc = new HashMap();

    fVectToParam = new HashMap();

  }

  /**
   * Instantiates TagVector2s and assigns TagVector2
   * objects to Var, malloced area, and external area.
   *
   * @param pSubpDef <code>SubpDefinition</code> instance
   * to analyze.
   */
  void prepare(SubpDefinition pSubpDef)
  {
//        fSubpDef = pSubpDef;

    Prescan lBuilder = new Prescan(pSubpDef, this);
    lBuilder.process();

    fAccessedVars = lBuilder.fAccessedVars;
    fAccessedElems = lBuilder.fAccessedElems;
    fAccessedConstSubscripts = lBuilder.fAccessedConstSubscripts;
    fmallocs = lBuilder.fmallocs;

    fTagBitCount = lBuilder.fTagBitCount;

    countBits();

    fFullVect = fFactory.tagVector(fTagBitCount);
    fFullVect.vectorNot(fFullVect);

    fNonNullInitials = fFactory.tagVector(fTagBitCount);
    fInitialAddressTakens = fFactory.tagVector(fTagBitCount);
    //  fCurFrame = fFactory.tagVector(fTagBitCount);
    //  fStatic = fFactory.tagVector(fTagBitCount);
//        fHeap = fFactory.tagVector(fTagBitCount);
//        fOther = fFactory.tagVector(fTagBitCount);
    fGlobals = fFactory.tagVector(fTagBitCount);
    //  fExternPes = fFactory.tagVector(fTagBitCount);
    fExternOpt = fFactory.tagVector(fTagBitCount);
    fLocalRootVects = new TagVector2[fTagBitCount];
    fPointsTo = new TagVector2[fTagBitCount];

//        fParamPoss = fFactory.tagVector(fTagBitCount);

    for (int i = 0; i < fTagBitCount; i++) {
      fLocalRootVects[i] = fFactory.tagVector(fTagBitCount);
      fPointsTo[i] = fFactory.tagVector(fTagBitCount);
    }
    assignBits();
    for (int i = 0; i < fTagBitCount; i++) {
      if (fNonNullInitials.isSet(i)) {
        fPointsTo[i].vectorOr(fInitialAddressTakens, fPointsTo[i]);
        for (Iterator lIt = fVectToParam.entrySet().iterator(); lIt.hasNext(); ) {
          Map.Entry lEntry = (Map.Entry)lIt.next();
          TagVector2 lVect = (TagVector2)lEntry.getKey();
          if (lVect.isSet(i)) {
            fPointsTo[i].fAssociatedParam = (Var)lEntry.getValue();
            break;
          }
        }
        if (fDbgLevel > 3) //##67
          dbg(5, "fPointsTo", Arrays.asList(fPointsTo));
      }
    }

    //        dbg(5, "Bit-assigned tags ", Arrays.asList(fBitAssignedTags));
  }

  /**
   * Named variables and malloc instances and the "external"
   * bits have already been counted.
   */
  private void countBits()
  {
    Var lVar;
    Elem lElem;

    for (Iterator lIt = fAccessedVars.iterator(); lIt.hasNext(); ) {
      lVar = (Var)lIt.next();
      if (fDbgLevel > 3) {//##67
        dbg(5, "counting bits for ", lVar);
        dbg(5, "current count = " + fTagBitCount, "");
      }
      countUnder(lVar.getSymType());
    }
    if (fDbgLevel > 3) //##67
      dbg(5, "fTagBitCount", new Integer(fTagBitCount));
  }

  private void countUnder(Type pType)
  {
    List lPair;
    Elem lElem;
    Type lSubtype;
    //        pTypeWorkList.add(pType);
    if (fDbgLevel > 3) { //##67
      dbg(5, "counting bits for ", pType);
      dbg(5, "current count = " + fTagBitCount, "");
    }

    switch (pType.getTypeKind()) {
      case Type.KIND_VECTOR:
        lSubtype = ((VectorType)pType).getElemType();
        for (Iterator lSubscriptIt = fAccessedConstSubscripts.iterator();
             lSubscriptIt.hasNext(); ) {
          lPair = (List)lSubscriptIt.next();
          if (lPair.get(1) == fUtil.toBareAndSigned(lSubtype)) {
            fTagBitCount++;
            countUnder(lSubtype);
          }
        }
        break;
      case Type.KIND_STRUCT:
        for (Iterator lElemIt = ((StructType)pType).getElemList().iterator();
             lElemIt.hasNext(); ) {
          lElem = (Elem)lElemIt.next();
          if (fAccessedElems.contains(lElem)) {
            fTagBitCount++;
            countUnder(lElem.getSymType());
          }
        }
        break;
      case Type.KIND_UNION:
      default:
        break;

    }
  }

  private void assignBits()
  {
    Var lVar;
    Elem lElem;
    TagVector2 lTagVect;
    BitAssigner lAssigner = new BitAssigner();

    for (Iterator lIt = fAccessedVars.iterator(); lIt.hasNext(); ) {
      lVar = (Var)lIt.next();
      lTagVect = fFactory.tagVector(fTagBitCount);
      lTagVect.fIsDefinite = true;
      //    lTagVect.setBit(lCurBitPos++);
      fVarToLoc.put(lVar, lTagVect);
      //            fVarToBitPos.put(lVar, new Integer(lCurBitPos++));
      lAssigner.assignUnder(lTagVect, lVar.getSymType());

      if (lVar.getSymKind() == Sym.KIND_PARAM) {
        fNonNullInitials.vectorOr(lTagVect, fNonNullInitials);
        if (fIsOptimistic)
          fVectToParam.put(lTagVect, lVar);
      }
      if (lVar.getStorageClass() == Var.VAR_STATIC) {
        if (lVar.isGlobal()) {
          fGlobals.vectorOr(lTagVect, fGlobals);
          fInitialAddressTakens.vectorOr(lTagVect, fInitialAddressTakens);
        }
        if (lVar.getFlag(Sym.FLAG_ADDRESS_TAKEN)) {
          fInitialAddressTakens.vectorOr(lTagVect, fInitialAddressTakens);
        }
        fNonNullInitials.vectorOr(lTagVect, fNonNullInitials);
      }
    }
    for (Iterator lIt = fmallocs.iterator(); lIt.hasNext(); ) {
      Exp lmalloc = (Exp)lIt.next();
      lTagVect = fFactory.tagVector(fTagBitCount);
      fmallocToLoc.put(lmalloc, lTagVect);
      lTagVect.setBit(lAssigner.fCurBitPos);
      lTagVect.vectorCopy(fLocalRootVects[lAssigner.fCurBitPos++]);
//            fLocalRootVects[lAssigner.fCurBitPos] = lTagVect;
    }

    if (lAssigner.fCurBitPos != fTagBitCount - 1)
      throw new AliasError();
    fGlobals.vectorCopy(fExternOpt);
    fExternOpt.setBit(fTagBitCount - 1);
    fInitialAddressTakens.setBit(fTagBitCount - 1);
    fNonNullInitials.setBit(fTagBitCount - 1);
    TagVector2 lExternVect = fFactory.tagVector(fTagBitCount);
    lExternVect.setBit(fTagBitCount - 1);
    fLocalRootVects[fTagBitCount - 1] = lExternVect;
  }

  private class BitAssigner
  {
    int fCurBitPos;

    private void assignUnder(TagVector2 pVarVect, Type pType)
    {
      List lPair;
      Elem lElem;
      Type lSubtype;
      TagVector2 lMaskVect;
      TagVector2 lLocalVectorMask, lLocalVectorElemMask;
      TagVector2 lLocalStructMask, lLocalStructMemMask;
      Map lSubscriptToLocalVectorElemMask = new HashMap(),
        lElemToLocalStructMemMask = new HashMap();
      int lAggregateBitPos = fCurBitPos;
      fLocalRootVects[fCurBitPos].setBit(fCurBitPos);
      pVarVect.setBit(fCurBitPos++);

      switch (pType.getTypeKind()) {
        case Type.KIND_VECTOR:
          lSubscriptToLocalVectorElemMask.clear();
          lSubtype = ((VectorType)pType).getElemType();
          lLocalVectorMask = fFactory.tagVector(fTagBitCount);
          for (Iterator lSubscriptIt = fAccessedConstSubscripts.iterator();
               lSubscriptIt.hasNext(); ) {
            lPair = (List)lSubscriptIt.next();
            if (lPair.get(1) != fUtil.toBareAndSigned(lSubtype))
              continue;
            if ((lMaskVect = (TagVector2)fSubscriptToMask.get(lPair)) == null) {
              lMaskVect = fFactory.tagVector(fTagBitCount);
              fFullVect.vectorCopy(lMaskVect);
              fSubscriptToMask.put(lPair, lMaskVect);
            }
            lLocalVectorMask.setBit(fCurBitPos);
            lLocalVectorElemMask = fFactory.tagVector(fTagBitCount);
            lLocalVectorElemMask.setBit(fCurBitPos);
            lSubscriptToLocalVectorElemMask.put(lPair, lLocalVectorElemMask);
            assignUnder(pVarVect, lSubtype);
          }
          for (Iterator lSubscriptIt = fAccessedConstSubscripts.iterator();
               lSubscriptIt.hasNext(); ) {
            lPair = (List)lSubscriptIt.next();
            if (lPair.get(1) != fUtil.toBareAndSigned(lSubtype))
              continue;
            lMaskVect = (TagVector2)fSubscriptToMask.get(lPair);
            lLocalVectorElemMask = (TagVector2)lSubscriptToLocalVectorElemMask.
              get(lPair);
            lLocalVectorElemMask.vectorXor(lLocalVectorMask,
              lLocalVectorElemMask);
            lMaskVect.vectorSub(lLocalVectorElemMask, lMaskVect);
            lMaskVect.resetBit(lAggregateBitPos);
            lMaskVect.fIsDefinite = true;
          }
          break;
        case Type.KIND_STRUCT:
          lElemToLocalStructMemMask.clear();
          lLocalStructMask = fFactory.tagVector(fTagBitCount);
          for (Iterator lElemIt = ((StructType)pType).getElemList().iterator();
               lElemIt.hasNext(); ) {
            lElem = (Elem)lElemIt.next();
            if (!fAccessedElems.contains(lElem))
              continue;
            if ((lMaskVect = (TagVector2)fElemToMask.get(lElem)) == null) {
              lMaskVect = fFactory.tagVector(fTagBitCount);
              fFullVect.vectorCopy(lMaskVect);
              fElemToMask.put(lElem, lMaskVect);
            }
            lLocalStructMask.setBit(fCurBitPos);
            lLocalStructMemMask = fFactory.tagVector(fTagBitCount);
            lLocalStructMemMask.setBit(fCurBitPos);
            lElemToLocalStructMemMask.put(lElem, lLocalStructMemMask);
            assignUnder(pVarVect, lElem.getSymType());
          }
          for (Iterator lElemIt = ((StructType)pType).getElemList().iterator();
               lElemIt.hasNext(); ) {
            lElem = (Elem)lElemIt.next();
            if (!fAccessedElems.contains(lElem))
              continue;
            lMaskVect = (TagVector2)fElemToMask.get(lElem);
            lLocalStructMemMask = (TagVector2)lElemToLocalStructMemMask.get(
              lElem);
            lLocalStructMemMask.vectorXor(lLocalStructMask, lLocalStructMemMask);
            lMaskVect.vectorSub(lLocalStructMemMask, lMaskVect);
            lMaskVect.resetBit(lAggregateBitPos);
            lMaskVect.fIsDefinite = true;
          }
          break;
        case Type.KIND_UNION:
          break;
        default:
          break;

      }
    }
  }

  /**
   * Constructs points-to graph.
   * The points-to graph is implemented as a bit matrix (array of bit vectors).
   * On the way, lvalue expressions are assigned TagVector2s.
   *
   * @return such a bit matrix as array of TagVector2s
   */
  private TagVector2[] constructPointsToGraph(SubpDefinition pSubpDef)
  {
    ConstructPointsToGraph2 lConstructPointsToGraph2 = new
      ConstructPointsToGraph2(pSubpDef, this);
    fPointsTo = lConstructPointsToGraph2.fPointsTo;
    fHIRToLoc = lConstructPointsToGraph2.fHIRToLoc;
    if (fDbgLevel > 2) //##67
      dbg(3, "fPointsTo after points-to graph creation ", Arrays.asList(fPointsTo));
    return fPointsTo;
  }

  private Map buildAliasGroups(SubpDefinition pSubpDef)
  {
    AliasGroup lAliasGroups[] = new AliasGroup[fTagBitCount];
    AliasGroup lAliasGroup;

    for (int i = 0; i < lAliasGroups.length; i++)
      lAliasGroups[i] = fFactory.aliasGroup();
    Exp lExp, lExp0, lExp1;
    Type lType, lType0;
    //        Tag lTag, lTag0;
    Map.Entry lEntry;
    Map lTagToAliasGroups = new HashMap();
    HIR lHIR, lUnion;
    TagVector2 lTagVect;

    for (Iterator lIt = fHIRToLoc.entrySet().iterator(); lIt.hasNext(); ) {
      lEntry = (Map.Entry)lIt.next();
      lExp = (Exp)lEntry.getKey();
      lTagVect = (TagVector2)lEntry.getValue();
      //        System.out.println("tag vect " + lTag.fTagVect);
      BriggsSet lBriggsSet = lTagVect.toBriggsSet();
      Scanner lScanner;
      int lBitPos;
      for (lScanner = lBriggsSet.scanner(); lScanner.hasNext(); ) {
        lBitPos = lScanner.next();
        lAliasGroups[lBitPos].add(lExp);
      }
      lTagToAliasGroups.put(lTagVect, lBriggsSet);
    }
    for (Iterator lIt = fHIRToLoc.entrySet().iterator(); lIt.hasNext(); ) {
      lAliasGroup = fFactory.aliasGroup();
      lEntry = (Map.Entry)lIt.next();
      lExp = (Exp)lEntry.getKey();
      if (fDbgLevel > 3) //##67
        dbg(5, "Building AliasGroup for " + lExp + " ...", "");
      lTagVect = (TagVector2)lEntry.getValue();
      BriggsSet lBriggsSet = (BriggsSet)lTagToAliasGroups.get(lTagVect);
      int lBitPos;
      for (Scanner lScanner = lBriggsSet.scanner(); lScanner.hasNext(); ) {
        lBitPos = lScanner.next();
        for (Iterator lAGIt = lAliasGroups[lBitPos].iterator(); lAGIt.hasNext(); ) {
          lExp0 = (Exp)lAGIt.next();

          for (lExp1 = lExp, lUnion = null;
               lExp1.getOperator() == HIR.OP_QUAL ||
               lExp1.getOperator() == HIR.OP_SUBS; lExp1 = lExp1.getExp1()) {
            if (lExp1.getType().getTypeKind() == Type.KIND_UNION)
              lUnion = lExp1;
          }
          if (lExp1.getType().getTypeKind() == Type.KIND_UNION)
            lUnion = lExp1;
          lType = lUnion == null ? lExp.getType() : lUnion.getType();

          for (lExp1 = lExp0, lUnion = null;
               lExp1.getOperator() == HIR.OP_QUAL ||
               lExp1.getOperator() == HIR.OP_SUBS; lExp1 = lExp1.getExp1()) {
            if (lExp1.getType().getTypeKind() == Type.KIND_UNION)
              lUnion = lExp1;
          }
          if (lExp1.getType().getTypeKind() == Type.KIND_UNION)
            lUnion = lExp1;
          lType0 = lUnion == null ? lExp0.getType() : lUnion.getType();

          TagVector2 lTagVect0 = (TagVector2)fHIRToLoc.get(lExp0);
          if (!fIsOptimistic ||
              fUtil.mayAlias(lType, lType0) &&
              (lTagVect.fAssociatedParam == null || lTagVect0.fAssociatedParam == null ||
               lTagVect.fAssociatedParam == lTagVect0.fAssociatedParam))
            lAliasGroup.add(lExp0);
        }
      }
      fHIRToAliasGroup.put(lExp, lAliasGroup);
      //		dbg(3, "AliasGroup for " + lExp + ": ", lAliasGroup.sort());
    }

    if (ioRoot.getCompileSpecification().getTrace().shouldTrace(CATEGORY_NAME,
      3)) {
      for (HirIterator lIt = hirRoot.hir.hirIterator(pSubpDef.getHirBody());
           lIt.hasNext(); ) {
        lHIR = lIt.next();
        if (fHIRToLoc.containsKey(lHIR)) {
          lAliasGroup = (AliasGroup)fHIRToAliasGroup.get(lHIR);
          ioRoot.printOut.println("AliasGroup for " + lHIR + ": " +
            lAliasGroup.sort());
        }
      }
    }

    return fHIRToAliasGroup;
  }

  /**
   * Returns one of the codes (MUST_ALIAS, MAY_ALIAS, NOT_ALIAS).
   */
  protected int areAliased(Exp pExp, Exp pExp0)
  {
    if (fUseLevel1)
      return super.areAliased(pExp, pExp0);
    if (!isLvalue(pExp))
      throw new IllegalArgumentException(pExp + " not lvalue.");
    if (!isLvalue(pExp0))
      throw new IllegalArgumentException(pExp0 + " not lvalue.");
    if (getAliasGroupFor(pExp).contains(pExp0))
      return areAliased0(pExp, pExp0);
    return NOT_ALIAS;
  }

  private int areAliased0(Exp pExp, Exp pExp0)
  {
    TagVector2 lTagVect1 = fFactory.tagVector(fTagBitCount);
//        dbg(5, "areAliased", "Alias relation of " + pExp + " and " + pExp0);
    TagVector2 lTag = null;
    lTag = (TagVector2)fHIRToLoc.get(pExp);
//        { System.out.println("nullpointerexception");}
    TagVector2 lTag0 = (TagVector2)fHIRToLoc.get(pExp0);

    TagVector2 lTagVect = lTag;
    TagVector2 lTagVect0 = lTag0;

    lTagVect.vectorAnd(lTagVect0, lTagVect1);
    if (!lTagVect1.isZero()) {
      if (lTagVect.vectorEqual(lTagVect1) && lTag0.fIsDefinite ||
          lTagVect0.vectorEqual(lTagVect1) && lTag.fIsDefinite)
        return MUST_ALIAS;
      else
        return MAY_ALIAS;
    }
    throw new AliasError("Unexpected.");
  }

  /**
   * Returns the set of lvalue nodes the specified argument may be
   *  aliased to. Each <code>AliasGroup</code> object for different
   * argument (<code>pExp</code>) is distinct from one another,
   * so it can be safely modified without affecting others.
   *
   * @param pExp the lvalue node to check for aliasing.
   * @return the set of lvalue nodes the specified argument may
   * be aliased to.
   */
  public coins.alias.AliasGroup getAliasGroupFor(Exp pExp)
  {
    if (fUseLevel1)
      return super.getAliasGroupFor(pExp);
    return (AliasGroup)fHIRToAliasGroup.get(pExp);
  }

  /**
     * Prints out alias pairs in <code>IoRoot.printOut</code> object.
     * For debugging.
   *
   * @param pSubpDef the <code>SubpDefinition</code> object
   * aliasing relation between nodes contained in which are to
   * be printed.
   */
  public void printAliasPairs(SubpDefinition pSubpDef)
  {
    if (fUseLevel1) {
      super.printAliasPairs(pSubpDef);
      return;
    }
    HIR lHIR;
    List lHIRList = new ArrayList();
    HIR lHIRs[];
    TagVector2 lTagVect1 = fFactory.tagVector(fTagBitCount);

    for (HirIterator lIt = hirRoot.hir.hirIterator(pSubpDef.getHirBody());
         lIt.hasNext(); ) {
      lHIR = (HIR)lIt.next();
      lHIRList.add(lHIR);
    }
    lHIRs = (HIR[])lHIRList.toArray(new HIR[0]);

    ioRoot.printOut.println("---Alias Pairs--- " +
      pSubpDef.getSubpSym().getName()); //##27
    ioRoot.printOut.println(
      "Legend (D: Definitely aliased, P: Possibly aliased)");
    ioRoot.printOut.println("");
    for (int i = 0; i < lHIRs.length; i++) {
      TagVector2 lTagVect = (TagVector2)fHIRToLoc.get(lHIRs[i]);
      if (lTagVect != null)
        for (int j = i + 1; j < lHIRs.length; j++) {
          TagVector2 lTagVect0 = (TagVector2)fHIRToLoc.get(lHIRs[j]);
          if (lTagVect0 != null) {

            //                        if (! lHIRs[i].isSameAs(lHIRs[j])) //##27
            switch (areAliased((Exp)lHIRs[i], (Exp)lHIRs[j])) {
              case MUST_ALIAS:
                ioRoot.printOut.println("(D: " + lHIRs[i] + ", " + lHIRs[j] +
                  ")");
                break;
              case MAY_ALIAS:
                ioRoot.printOut.println("(P: " + lHIRs[i] + ", " + lHIRs[j] +
                  ")");
                break;
            }
          }
        }
    }
    ioRoot.printOut.println();
  }

  /**
   * <p>Returns true if the two arguments may refer to the overlapping
   * area in memory. A CONTENTS node with uninitialized pointer
   * value operand (undefined behavior in C) does not represent
   * an object that inhabits the memory space, so it is not
   * aliased even to itself.</p>
   * <p>This method has to be called after the prepareForAliasAnalHir
   * method has been called.</p>
   *
     * @exception IllegalArgumentException if either of the
     * arguments is not lvalue.
   */
  public boolean mayAlias(Exp pExp, Exp pExp0)
  {
    if (fUseLevel1)
      return super.mayAlias(pExp, pExp0);
    return (areAliased(pExp, pExp0) & MUST_ALIAS) != 0;
  }

  /**
   * <p>Returns true if the two arguments definitely refer to the
   *  overlapping area in memory.</p>
   * <p>This method has to be called after the
   * prepareForAliasAnalHir method has been called.</p>
   *
     * @exception IllegalArgumentException if either of the
     * arguments is not lvalue.
   */
  public boolean mustAlias(Exp pExp, Exp pExp0)
  {
    if (fUseLevel1)
      return super.mustAlias(pExp, pExp0);
    return (areAliased(pExp, pExp0) & NOT_ALIAS) == 0;
  }

  /**
   * <p>Returns true if the specified argument is lvalue.
   * As is the case for the query methods (mayAlias etc.)
   * that is specified in the AliasAnal interface, this method
   * also has to be called after the prepareForAliasAnalHir
   * method for the corresponding SubpDefinition object
   * has been called.</p>
   * <p>This method has to be called after the
   * prepareForAliasAnalHir method has been called.</p>
   *
   * @return true if the specified argument is lvalue.
   */
  public boolean isLvalue(Exp pExp)
  {
    if (fUseLevel1)
      return super.isLvalue(pExp);
    return fHIRToLoc.containsKey(pExp);
    //        return fHIRToTag.containsKey(pExp);
  }

}
