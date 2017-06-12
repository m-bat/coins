/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * BBlockStmtIterator.java
 *
 * Created on July 30, 2002, 4:05 PM
 */
package coins.aflow;

import coins.ir.IR;
import coins.ir.hir.HIR;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.StmtImpl;


/**
 * Unreliable
 * @author  hasegawa
 */
//##25 class BBlockStmtIterator implements BBlockSubtreeIterator {
  public class BBlockStmtIterator implements BBlockSubtreeIterator //##25
  {
    final static Stmt EOB = new StmtImpl();
    private Stmt fSubtreeNext = null; // Subtree that would be returned by the next call of next().

//    private List fStmtList = new ArrayList();

    //##25 BBlockStmtIterator(BBlockHir pBBlock)
    public BBlockStmtIterator(BBlockHir pBBlock) //##25
    {
        fSubtreeNext = (LabeledStmt) pBBlock.getIrLink();
//        fStmtList = new BBlockStmtVisitor().go();
    }
//
//    private class BBlockStmtVisitor extends HirVisitorModel3
//    {
//        private Stmt fSubtreeNext;
//        List fStmtList = new ArrayList();
//
//        private LabeledStmt fLabeledStmt;
//
//        private BBlockStmtVisitor(LabeledStmt pLabeledStmt)
//        {
//            super(null);
//            fLabeledStmt = pLabeledStmt;
//        }
//
//        private List go()
//        {
//            visit(fLabeledStmt);
//            return fStmtList;
//        }
//
//        public void atBlockStmt(BlockStmt pBlockStmt)
//        {
//            processStmt(pBlockStmt);
//        }
//
//        public void atInfStmt(InfStmt pInfStmt)
//        {
//            processStmt(pInfStmt);
//        }
//
//        public void atAssignStmt(AssignStmt pAssign)
//        {
//            processStmt(pAssign);
//        }
//
//        public void atIfStmt(IfStmt pIf)
//        {
//            processStmt(pIf);
//        }
//
//        public void atWhileStmt(WhileStmt pWhile)
//        {
//            process

    public boolean hasNext() {
        return (fSubtreeNext != EOB) && (fSubtreeNext != null);
    }

    public IR next() {
        if (fSubtreeNext == null) {
            return null;
        }
        Stmt lCurrent = fSubtreeNext;

        if (fSubtreeNext.getOperator() == HIR.OP_JUMP)
        {
            fSubtreeNext = EOB;
            return lCurrent;
        }


        HIR lHIR = fSubtreeNext;
        HIR lHIRNext;
        int lOpCode = 0;

        do {
            lHIRNext = tryNext(lHIR);

            if (lHIRNext == null) {
                lHIRNext = tryNeitherDescendantsNorAncestors(lHIR);
            }

            if (lHIRNext instanceof LabeledStmt) {
                lHIRNext = EOB;
            }

            lHIR = lHIRNext;
        } while ((lHIR != EOB) && !(lHIR instanceof Stmt));

        fSubtreeNext = (Stmt) lHIR;

        return lCurrent;
    }

    private static HIR tryNext(HIR pHIR) {
        HIR lNext;
        int i = 1;

        for (i = 1; i <= pHIR.getChildCount(); i++) {
            lNext = (HIR) pHIR.getChild(i);

            if (lNext != null) {
                return lNext;
            }
        }

        return tryNeitherDescendantsNorAncestors(pHIR);
    }

    private static HIR tryNeitherDescendantsNorAncestors(HIR pHIR) {
        HIR lNext;
        int lChildNumber;
        HIR lParent;
        int lChildCount;

        if ((lNext = pHIR.getNextStmt()) != null) {
            return lNext;
        }

        lChildNumber = pHIR.getChildNumber();
        lParent = (HIR) pHIR.getParent();

        if (lParent == null) {
            return EOB; // End of subprogram reached.
        }

        lChildCount = lParent.getChildCount();

        while ((0 < lChildNumber) && (lChildNumber < lChildCount))

            if ((lNext = (HIR) lParent.getChild(++lChildNumber)) != null) {
                return lNext;
            }

        return tryNeitherDescendantsNorAncestors(lParent);
    }
}
