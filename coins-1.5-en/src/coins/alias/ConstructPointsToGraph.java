/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * PointsToGraph.java
 *
 * Created on July 18, 2003, 2:13 PM
 */

package coins.alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import coins.HirRoot;
import coins.IoRoot;
import coins.alias.util.Scanner;
import coins.ir.IrList;
import coins.ir.hir.AssignStmt;
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
import coins.sym.VectorType;

/**
 * This class constructs the points-to graph, the bit matrix representing the points-to relation, implemented as the array of <code>TagVector</code>s. It analyzes the propagation of address values, and, since this is a flow-insensitive analysis, does it repeatedly until the points-to graph saturates (reaches the fixed state). There is no consideration for types; not only pointer types can hold the address values.
 * @author  hasegawa
 */
class ConstructPointsToGraph
{
    private boolean fIsOptimistic;

    private List fAssigns = new ArrayList();
    private List fExps = new ArrayList();
    //	private List fVolatiles = new ArrayList();

    private Map fHIRToTag;
    private Map fHIRTomallocTag;
    private int fTagBitCount;
    private Tag fBitAssignedTags[];

    private final AliasFactory fFactory;
    private final AliasUtil fUtil;

    private TagVector fPointsTo[];
    //	private final TagVector fAddressTakens;
    private final TagVector fRoot;
    private final TagVector fCurFrame;
    private final TagVector fStatic;
    private final TagVector fHeap;
    private final TagVector fOther;
    private final TagVector fGlobals;
    private final TagVector fExternPes;
    private final TagVector fExternOpt;
    private final AliasAnalHir1 fAliasAnal;

    /**
     * The <code>HirRoot</code> object shared by every module
     * in the program.
     */
    public final HirRoot hirRoot;

    /**
     * The <code>IoRoot</code> object shared by every module i
     * n the program.
     */
    public final IoRoot ioRootAlias;

    protected TagVector fAddressExternallyVisibles = null; //##58
    /** Creates a new instance of ConstructPointsToGraph.
     * Some information is passed from the <code>AliasAnalHir1</code>
     * object to this module.
     *
     * @param pAnal <code>AliasAnalHir1</code> object, the
     * object that drives the alias analysis.
     * @param pHirRoot <code>HirRoot</code> object shared
     * by every module in the program.
     */
    ConstructPointsToGraph(AliasAnalHir1 pAnal, HirRoot pHirRoot)
    {
        hirRoot = pHirRoot;
        ioRootAlias = hirRoot.ioRoot;
        fHIRToTag = pAnal.fHIRToTag;
        fHIRTomallocTag = pAnal.fHIRTomallocTag;
        fTagBitCount = pAnal.fTagBitCount;
        fBitAssignedTags = pAnal.fBitAssignedTags;
        fPointsTo = pAnal.fPointsTo;
        fFactory = pAnal.fFactory;
        fUtil = pAnal.fUtil;
        fAliasAnal = pAnal;
        if (ioRootAlias.dbgAlias.getLevel() > 0) //##58
          fAliasAnal.dbg(2, "ConstructPointsToGraph", "tagBitCount "+fTagBitCount); //##58

        fIsOptimistic = pAnal.fIsOptimistic;
        TagVector lTagVect;
        //	fAddressTakens = pAnal.fAddressTakens;
        lTagVect = fFactory.tagVector(fTagBitCount);
        fRoot = (TagVector)lTagVect.vectorNot(lTagVect);
        fCurFrame = pAnal.fCurFrame;
        fStatic = pAnal.fStatic;
        fHeap = pAnal.fHeap;
        fOther = pAnal.fOther;
        fGlobals = pAnal.fGlobals;
        fExternOpt = pAnal.fExternOpt;
        fExternPes = pAnal.fExternPes;
    }

    /**
     * Construct the points-to graph.
     *
     * @param pSubpDef <code>SubpDefinition</code> object nodes
     * contained in which the points-to graph is created for.
     * @return points-to graph as the array of <code>TagVector</code>s.
     */
    TagVector[] makePointsToGraph(SubpDefinition pSubpDef)
    {
        if (ioRootAlias.dbgAlias.getLevel() > 0) //##58
          fAliasAnal.dbg(3, "makePointsToGraph", pSubpDef.getSubpSym().getName()); //##58
        AssignAndExpScanner lScanner = new AssignAndExpScanner(pSubpDef);
        lScanner.visit(pSubpDef.getHirBody());
        if (ioRootAlias.dbgAlias.getLevel() > 3) //##58
          fAliasAnal.dbg(4, "fExps", fExps.toString()); //##58
        TagVector lReturnVal[] = process();
//        printPointsToGraph();
        return lReturnVal;
    }

    private TagVector[] process()
    {
        AssignStmt lAssignStmt;
        TagVector lLLocVect, lRLocVect;
        if (ioRootAlias.dbgAlias.getLevel() > 0) //##62
          fAliasAnal.dbg(2, "TagVector.process",
            "fPointsTo.length " +fPointsTo.length+
            "fAssigns.length " +fPointsTo.length+
            " fTagBitCount " +fTagBitCount); //##62
        TagVector lPointsTo[] = new TagVector[fPointsTo.length];
        for (int i = 0; i < fPointsTo.length; i++)
            lPointsTo[i] = fFactory.tagVector(fTagBitCount);
        if (ioRootAlias.dbgAlias.getLevel() > 3) //##58
          fAliasAnal.dbg(5, "lPointsTo(initial)", Arrays.asList(fPointsTo)); //##58
        int lRepetition = 0; //##58
        do
        {
            lRepetition++; //##58
            if (ioRootAlias.dbgAlias.getLevel() > 0) //##58
              //##62 fAliasAnal.dbg(6, "ConsructPointsToGraph", "repetition " + lRepetition); //##58
              fAliasAnal.dbg(2, "ConsructPointsToGraph", "repetition " + lRepetition); //##62
            for (int j = 0; j < fPointsTo.length; j++)
                fPointsTo[j].vectorCopy(lPointsTo[j]);
            for (Iterator lIt = fAssigns.iterator(); lIt.hasNext();)
            {
                lAssignStmt = (AssignStmt)lIt.next();
                lLLocVect = findLLocSet(lAssignStmt.getLeftSide());
                lRLocVect = findRLocSet(lAssignStmt.getRightSide());
                if (ioRootAlias.dbgAlias.getLevel() > 3) { //##58
                  fAliasAnal.dbg(6, "lAssignStmt ", lAssignStmt.toStringShort()); //##58
                  fAliasAnal.dbg(6, "lLLLLocVect ", lLLocVect); //##58
                  fAliasAnal.dbg(6, "lRLLLocVect ", lRLocVect); //##58
                }
                if (lRLocVect.isZero() || lLLocVect.isZero())  //##58
                  continue;                                    //##58
                for (Scanner lScanner = lLLocVect.toBriggsSet().scanner();
                     lScanner.hasNext();)
                {
                    int lNext = lScanner.next();
                    fPointsTo[lNext].vectorOr(lRLocVect, fPointsTo[lNext]);
                    //##58 fAliasAnal.dbg(6, "fPointsTo", Arrays.asList(fPointsTo));
                    if (ioRootAlias.dbgAlias.getLevel() > 3) //##58
                      fAliasAnal.dbg(6, "fPointsTo " + lNext, Arrays.asList(fPointsTo)); //##58
                }
            }

            for (Iterator lIt = fExps.iterator(); lIt.hasNext();)
            {
                findRLocSet((Exp)lIt.next());
            }
            fAliasAnal.dbg(5, "ConstructPointsToGraph", "Main loop");
            if (ioRootAlias.dbgAlias.getLevel() > 3) //##58
              fAliasAnal.dbg(5, "fPointsTo", Arrays.asList(fPointsTo));
        } while (!Arrays.equals(fPointsTo, lPointsTo));

        return fPointsTo;
    }

    private TagVector findLLocSet(Exp pLHS)
    {
        Tag lTag = (Tag)fHIRToTag.get(pLHS);
        if (lTag == null) //  String literal array, e.g. "aaa"[1] = ...
            return fFactory.tagVector(fTagBitCount);
        if (lTag.isAnchored())
        {
            return lTag.fTagVect;
        }
        else
        {
            return new LocVisitor(pLHS).process();
        }
    }

    private TagVector findRLocSet(Exp pRHS)
    {
        return new LocVisitor(pRHS).process();
    }


    private class AssignAndExpScanner extends HirVisitorModel2
    {
        SubpDefinition fSubpDef;

        AssignAndExpScanner(SubpDefinition pSubpDef)
        {
            super(ConstructPointsToGraph.this.hirRoot);
            fSubpDef = pSubpDef;
        }

        public void atAssignStmt(AssignStmt pAssignStmt)
        {
            fAssigns.add(pAssignStmt);
        }

        // Every lvalue should be taken care of since this module not only
        // creates points-to graph but also assigns TagVectors to
        // Tags that are needed later.

        public void atExp(Exp pExp)
        {
            fExps.add(pExp);
        }

        public void atVarNode(VarNode pVarNode)
        {
            fExps.add(pVarNode);
        }

        public void atSubscriptedExp(SubscriptedExp pExp)
        {
            fExps.add(pExp);
        }

        public void atQualifiedExp(QualifiedExp pExp)
        {
            fExps.add(pExp);
        }

        public void atPointedExp(PointedExp pExp)
        {
            fExps.add(pExp);
        }


        public void atFunctionExp(FunctionExp pExp)
        {
            fExps.add(pExp);
        }
    }



    private class LocVisitor extends HirVisitorModel2
    {
        TagVector fTagVect = fFactory.tagVector(fTagBitCount); // This vector
           // will be in one of two states: dereferenced and raw.
          // The current state is determined by the parent's operator code.
        Exp fRoot;

        LocVisitor(Exp pRoot)
        {
            super(ConstructPointsToGraph.this.hirRoot);
            fRoot = pRoot;
        }

        TagVector process()
        {
            visit(fRoot);
            return fTagVect;
        }

        public void atVarNode(VarNode pVarNode)
        {
            TagVector lTagVect = ((Tag)fHIRToTag.get(pVarNode)).fTagVect;
            dereferenceIfNeeded(lTagVect, pVarNode);
        }

        public void atExp(Exp pExp)
        {
            Tag lTag;
            switch (pExp.getOperator())
            {
                case HIR.OP_CONTENTS:
                    TagVector lTagVect = new LocVisitor((Exp)pExp.getChild1()).process();
                    if (pExp.getType().getTypeKind() != Type.KIND_SUBP)
                    {
                        ((Tag)fHIRToTag.get(pExp)).fTagVect = lTagVect;
                        dereferenceIfNeeded(lTagVect, pExp);
                    }
                    break;
                case HIR.OP_ADD:
                case HIR.OP_SUB:
                    visitChildren(pExp);
                    for (Scanner lScanner0 = fTagVect.toBriggsSet().scanner(); lScanner0.hasNext();)
                    {
                        if (ioRoot.dbgAlias.getLevel() > 3) //##58
                          fAliasAnal.dbg(6, "fTagVect = ", fTagVect);
                        int lNext = lScanner0.next();
                        if (fIsOptimistic)
                            fTagVect.vectorOr(fBitAssignedTags[lNext].promote().fTagVect, fTagVect);
                        else
                            fTagVect.vectorOr(storageClassVect(fBitAssignedTags[lNext].fStorageClass), fTagVect);
                    }
                    break;
                case HIR.OP_ADDR:
                    visitChildren(pExp);
                    break;
                case HIR.OP_CONV:
                    visitChildren(pExp);
                    break;
                case HIR.OP_DECAY:
                    visitChildren(pExp);
                    break;
                case HIR.OP_UNDECAY:
                    new LocVisitor((Exp)pExp.getChild1()).process();
                    new LocVisitor((Exp)pExp.getChild2()).process();
    //                TagVector lTagVect0 = ((Tag)fHIRToTag.get(pExp)).fTagVect;
                    TagVector lTagVect0 = ConstructPointsToGraph.this.fRoot;
     //               lTagVect0.vectorCopy(((Tag)fHIRToTag.get(pExp)).fTagVect));
                    ((Tag)fHIRToTag.get(pExp)).fTagVect = lTagVect0;
                    dereferenceIfNeeded(lTagVect0, pExp);
                    break;
                default:
                    visitChildren(pExp);
                    if (!fIsOptimistic)
                        for (Scanner lScanner1 = fTagVect.toBriggsSet().scanner(); lScanner1.hasNext();)
                        {
                            if (ioRoot.dbgAlias.getLevel() > 3) //##58
                              fAliasAnal.dbg(6, "fTagVect = ", fTagVect);
                            int lNext = lScanner1.next();
                            fTagVect.vectorOr(storageClassVect(fBitAssignedTags[lNext].fStorageClass), fTagVect);
                        }
            }
        }

        // The constant index will not be honored if the Tag is not anchored.
        public void atSubscriptedExp(SubscriptedExp pExp)
        {
            if (pExp.getArrayExp().getOperator() == HIR.OP_CONST) // String literal array.
            {
                new LocVisitor(pExp.getSubscriptExp()).process();
                return;
            }

            Tag lTag = (Tag)fHIRToTag.get(pExp);
            if (lTag != null && lTag.isAnchored())
            {
                TagVector lTagVect = lTag.fTagVect;
                dereferenceIfNeeded(lTagVect, pExp);
                new LocVisitor(pExp.getSubscriptExp()).process();
            } else
            {
                visit(pExp.getArrayExp());
                new LocVisitor(((SubscriptedExp)pExp).getSubscriptExp()).process();
                TagVector lTagVect;
                //                System.out.println("ftagvect = " + fTagVect);
                if (lTag != null)
                {
                    if (fIsOptimistic || pExp.getSubscriptExp().getOperator() == HIR.OP_CONST && AliasUtil.subscriptCheck((VectorType)pExp.getArrayExp().getType(), ((ConstNode)pExp.getSubscriptExp()).getIntValue()))
                    {
                        lTagVect = ((Tag)fHIRToTag.get(((SubscriptedExp)pExp).getArrayExp())).fTagVect;
                        lTag.fTagVect = lTagVect; // Should copy?
                    } else // pessimistic and variable subscript
                    {
                        lTagVect = fFactory.tagVector(fTagBitCount);
                        storageClassVect(lTag.fStorageClass).vectorCopy(lTagVect);
                        lTag.fTagVect = lTagVect;
                    }
                    dereferenceIfNeeded(lTagVect, pExp);
                }
            }
        }

        // Struct element distinction will not be honored if the Tag is not anchored.
        public void atQualifiedExp(QualifiedExp pExp)
        {
            Tag lTag = (Tag)fHIRToTag.get(pExp);
            if (lTag != null && lTag.isAnchored())
            {
                TagVector lTagVect = lTag.fTagVect;
                dereferenceIfNeeded(lTagVect, pExp);
            } else
            {
                visit((Exp)pExp.getChild1());
                if (lTag != null)
                {
                    TagVector lTagVect = ((Tag)fHIRToTag.get(pExp.getChild1())).fTagVect;
                   lTag.fTagVect = lTagVect; // Should copy?
                    dereferenceIfNeeded(lTagVect, pExp);
                }
            }
        }

        // Struct element distinction will not be honored.
        public void atPointedExp(PointedExp pExp)
        {
            TagVector lTagVect = new LocVisitor((Exp)pExp.getChild1()).process();
            ((Tag)fHIRToTag.get(pExp)).fTagVect = lTagVect; // Should copy?
            dereferenceIfNeeded(lTagVect, pExp);
        }


        public void atFunctionExp(FunctionExp pExp)
        {
            IrList lActuals = pExp.getActualParamList();
            boolean lIsmalloc = false;
            boolean lIsPredefined = false;
            Exp lFuncSpec, lSubpNode;
            TagVector lAddressExternallyVisibles = fFactory.tagVector(fTagBitCount);

            if (fHIRTomallocTag.containsKey(pExp))
                lIsmalloc = true;
            else if ((lFuncSpec = pExp.getSubpSpec()).getOperator() == HIR.OP_ADDR && (lSubpNode = lFuncSpec.getExp1()).getOperator()  == HIR.OP_SUBP && fUtil.isPredefined(((SubpNode)lSubpNode).getSubp(), fAliasAnal.fPredefined))
                lIsPredefined = true;
            //			else if (pExp.getFunctionNode().getSubp() != null && fUtil.isPredefined(pExp.getFunctionNode().getSubp()))
            //				lIsPredefined = true;
            else
                new LocVisitor(pExp.getFunctionSpec()).process();
            for (Iterator lIt = lActuals.iterator(); lIt.hasNext();)
                lAddressExternallyVisibles.vectorOr(new LocVisitor((Exp)lIt.next()).process(), lAddressExternallyVisibles);
            if (lIsmalloc)
                fTagVect.vectorOr(((Tag)fHIRTomallocTag.get(pExp)).fTagVect, fTagVect);
            else if (lIsPredefined)
                ;
            else
            {
                if (fIsOptimistic)
                    lAddressExternallyVisibles.vectorOr(fExternOpt, lAddressExternallyVisibles);
                else
                    lAddressExternallyVisibles.vectorOr(fExternPes, lAddressExternallyVisibles);
                TagVector lTemp = fFactory.tagVector(fTagBitCount);
                if (fAddressExternallyVisibles == null) { //##58
                  int lIteration = 0; //##58
                  do {
                    lIteration++; //##58
                    if (ioRoot.dbgAlias.getLevel() > 3) //##58
                      fAliasAnal.dbg(6, "AddressExternallyVisible", //##58
                        "iteration " + lIteration);                 //##58
                    lAddressExternallyVisibles.vectorCopy(lTemp);
                    for (Scanner lScanner = lAddressExternallyVisibles.toBriggsSet().
                         scanner(); lScanner.hasNext(); ) {
                      int lNext = lScanner.next();
                      lAddressExternallyVisibles.vectorOr(fPointsTo[lNext],
                        lAddressExternallyVisibles);
                    }
                  }
                  while (!lAddressExternallyVisibles.vectorEqual(lTemp));
                  if (!fIsOptimistic)
                    if (!lAddressExternallyVisibles.vectorAnd(fCurFrame,
                      fFactory.tagVector(fTagBitCount)).isZero())
                      lAddressExternallyVisibles.vectorOr(fCurFrame,
                        lAddressExternallyVisibles);
                    if (ioRoot.dbgAlias.getLevel() > 3) //##58
                      fAliasAnal.dbg(6, "lAddressExternallyVisibles",
                        lAddressExternallyVisibles);
                  for (Scanner lScanner0 = lAddressExternallyVisibles.toBriggsSet().
                       scanner(); lScanner0.hasNext(); ) {
                    int lNext0 = lScanner0.next();
                    fPointsTo[lNext0].vectorOr(lAddressExternallyVisibles,
                      fPointsTo[lNext0]);
                    //##58 fAliasAnal.dbg(6, "fPointsTo", Arrays.asList(fPointsTo));
                    if (ioRoot.dbgAlias.getLevel() > 3) //##58
                      fAliasAnal.dbg(6, "fPointsTo(atFunctionExp)" + lNext0,
                        Arrays.asList(fPointsTo)); //##58
                  }
                  // fAddressExternalliVisibles == null
                fTagVect.vectorOr(lAddressExternallyVisibles, fTagVect);
                fAddressExternallyVisibles = lAddressExternallyVisibles; //##58
              //##58 BEGIN
              }else {
                fTagVect.vectorOr(fAddressExternallyVisibles, fTagVect);
              }
              //##58 END
            }

        }

        private void dereferenceIfNeeded(TagVector pTagVect, HIR pHIR)
        {
            if (ioRoot.dbgAlias.getLevel() > 3) //##58
              fAliasAnal.dbg(6, "HIR to be dereferenced", pHIR);
            HIR lParent = (HIR)pHIR.getParent();
            int lParentOpCode = lParent.getOperator();
            switch (lParentOpCode)
            {
                case HIR.OP_ADDR:
                case HIR.OP_SUBS:
                case HIR.OP_QUAL:
                case HIR.OP_DECAY:
                    fTagVect.vectorOr(pTagVect, fTagVect);
                    break;
                case HIR.OP_ASSIGN:
                    if (((AssignStmt)lParent).getLeftSide() == pHIR)
                    {
                        fTagVect.vectorOr(pTagVect, fTagVect);
                        break;
                    } else
                    {
                        fTagVect.vectorReset();
                        deref(pTagVect);
                        break;
                    }
                case HIR.OP_CONTENTS:
                case HIR.OP_ARROW:
                    fTagVect.vectorReset(); // <- What's this?
                    deref(pTagVect);
                    break;
                default:
                  if (ioRoot.dbgAlias.getLevel() > 3) //##58
                    fAliasAnal.dbg(6, "default. parent", lParent.toString()); //##58
                  //##58 deref(pTagVect);

            }
        }

        private TagVector deref(TagVector pTagVect)
        {
            for (Scanner lScanner = pTagVect.toBriggsSet().scanner(); lScanner.hasNext();)
            {
                int lnext = lScanner.next();
                //##58 fAliasAnal.dbg(6, "fPointsTo", Arrays.asList(fPointsTo));
                if (ioRoot.dbgAlias.getLevel() > 3) //##58
                  fAliasAnal.dbg(6, "fPointsTo(deref)"+ lnext, Arrays.asList(fPointsTo));  //##58
                fTagVect.vectorOr(fPointsTo[lnext], fTagVect);
            }
            return fTagVect;
        }

    }

    private void printPointsToGraph()
    {
        for (int i = 0; i < fPointsTo.length; i++)
        {
            ioRootAlias.printOut.println(fBitAssignedTags[i]  + " points to " );
            for (Scanner lScanner = fPointsTo[i].toBriggsSet().scanner(); lScanner.hasNext();)
                ioRootAlias.printOut.println(" " + fBitAssignedTags[lScanner.next()]);
        }
    }

    private TagVector storageClassVect(int pStorageClass)
    {

        switch (pStorageClass)
        {
            case Tag.STO_ROOT:
                return fRoot;
            case Tag.STO_CUR_FRAME:
                return fCurFrame;
            case Tag.STO_STATIC:
                return fStatic;
            case Tag.STO_HEAP:
                return fHeap;
            case Tag.STO_OTHER:
                return fOther;
        }
        throw new AliasError("Unexpected.");
    }

}

