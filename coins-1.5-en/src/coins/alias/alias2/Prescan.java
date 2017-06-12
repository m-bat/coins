/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * Prescan.java
 *
 * Created on July 28, 2003, 4:07 PM
 */

package coins.alias.alias2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import coins.alias.AliasUtil;
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
import coins.sym.VectorType;

/**
 * Scans HIR nodes and collects some info.
 *
 * @author  hasegawa
 */
class Prescan extends HirVisitorModel2
{
    private SubpDefinition fSubpDef;


    /**
     * Total number of Tags that have a bit position assigned
     * in the TagVector.
     */
    int fTagBitCount = 0;



    Set fAccessedVars = new HashSet();
    Set fAccessedElems = new HashSet();
    Set fAccessedConstSubscripts = new HashSet();

    Set fmallocs = new HashSet();

    private AliasFactory2 fFactory;


    private Exp fRecentLvalue;

    private AliasUtil fUtil;
    private AliasAnalHir2 fAliasAnal;

    /** Creates a new instance of TagTreeBuilder
     *
     * @param pSubpDef the <code>SubpDefinition</code> object
     * this module is responsible for.
     * @param pAlaisAnal the <code>AliasAnalHir2</code> object
     * that drives the analysis.
     */
    Prescan(SubpDefinition pSubpDef, AliasAnalHir2 pAliasAnal)
    {
        super(pAliasAnal.hirRoot);
        fSubpDef = pSubpDef;
        fAliasAnal = pAliasAnal;
        fFactory = new AliasFactory2(hirRoot);
        fUtil = pAliasAnal.fUtil;
    }

    void process()
    {
        visit(fSubpDef.getHirBody());
        fTagBitCount++; // For "other" addresses.
    }

    public void atVarNode(VarNode pVarNode)
    {
        int lBitPos;
        int lKind, lStorageClass;

        if (fAccessedVars.add(pVarNode.getVar()))
            fTagBitCount++;
        fRecentLvalue = pVarNode;
    }

    public void atSubscriptedExp(SubscriptedExp pExp)
    {
        Exp lArrayExp = pExp.getArrayExp();
        VectorType lArrayType = (VectorType)lArrayExp.getType();
        Exp lSubscriptExp = pExp.getSubscriptExp();
        Exp lRecentLvalue;
        boolean lUnionMemVisited;

        visit(lArrayExp);
        lRecentLvalue = fRecentLvalue;

        visit(lSubscriptExp);

        if (lArrayExp.getOperator() == HIR.OP_CONST) // String literal array.
            return;

        if (fRecentLvalue != lArrayExp) // not lvalue
            return;

        if (lSubscriptExp.getOperator() == HIR.OP_CONST)
        {
            List lPair = new ArrayList(2);
            lPair.add(new Long(((ConstNode)lSubscriptExp).getConstSym().longValue()));
            lPair.add(fUtil.toBareAndSigned(pExp.getType()));
            fAccessedConstSubscripts.add(lPair);
        }
        fRecentLvalue = pExp;
     //   fUnionMemVisited = lUnionMemVisited || lArrayExp.getType().getTypeKind() == Type.KIND_UNION;

    }

    public void atQualifiedExp(QualifiedExp pExp)
    {

        visit(pExp.getQualifierExp());

        if (fRecentLvalue != pExp.getQualifierExp()) // not lvalue.
            return;

        fAccessedElems.add(pExp.getQualifiedElem());
        fRecentLvalue = pExp;
       // fUnionMemVisited |= pExp.getType().getTypeKind() == Type.KIND_UNION;
    }

    /**
     * Visits the children of the specified argument, creates a Tag for
     * the <code>MyExpId</code> attached to the specified argument
     * if it is not yet done, and registers the node-Tag correspondence
     * into a global map.
     *
     * @param pExp the <code>PointedExp</code> the <code>MyExpId</code>
     * attached to which is going to be assigned a Tag.
     */
    public void atPointedExp(PointedExp pExp)
    {

        visit((Exp)pExp.getChild1());

        fAccessedElems.add(pExp.getQualifiedElem());
        fRecentLvalue = pExp;
    }


    public void atFunctionExp(FunctionExp pExp)
    {
        HIR lFuncSpec = pExp.getFunctionSpec();
        HIR lSubpNode;
        //                System.out.println("subp " + pExp.get);
        if (lFuncSpec.getOperator() == HIR.OP_ADDR &&
            (lSubpNode = (HIR)lFuncSpec.getChild1()).getOperator() == HIR.OP_SUBP &&
            AliasUtil.ismalloc(((SubpNode)lSubpNode).getSubp()))
        {
            fmallocs.add(pExp);
            fTagBitCount++;
        }
        visitChildren(pExp);
    }

}


