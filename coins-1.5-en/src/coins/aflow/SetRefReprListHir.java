/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * SetRefReprListHir.java
 *
 * Created on August 29, 2002, 2:19 PM
 */
package coins.aflow;

import coins.aflow.util.FlowError;
import coins.ir.IR;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HIR_Impl;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;
import coins.sym.Label;

/**
 * List of SetRefReprHir objects.
 *
 * @author  hasegawa
 */
public class SetRefReprListHir extends SetRefReprList
{
    /** Creates a new instance of SetRefReprListHir. This SetRefReprListHir will be associated with a given BBlock.
     */
    public SetRefReprListHir(BBlockHir pBBlock)
    {
        super(pBBlock);
    }

    public SetRefReprIterator setRefReprIterator()
    {
        //		return new SetRefReprListHirItr();
        return setRefReprIterator(0);
    }

    public SetRefReprIterator setRefReprIterator(int pIndex)
    {
        return new SetRefReprListHirItr(pIndex);
    }

    private static boolean isStmtNotAttachable(LabeledStmt pLabeledStmt)
    {
        switch (pLabeledStmt.getLabel().getLabelKind())
        {
            case coins.sym.Label.END_IF_LABEL:
            case coins.sym.Label.LOOP_END_LABEL:
            case coins.sym.Label.SWITCH_END_LABEL:
                //			case coins.sym.Label.SWITCH_CASE_LABEL:
                //			case coins.sym.Label.SWITCH_DEFAULT_LABEL:
                return true;
            default:
                return false;
        }
    }

    protected class SetRefReprListHirItr extends Itr
    implements SetRefReprIterator
    {
        SetRefReprListHirItr(int pIndex)
        {
            super(pIndex);
        }

        public void remove()
        {
            SetRefReprHir lToBeRemoved = (SetRefReprHir) lastReturned();
            ((Stmt) lToBeRemoved.getStmt()).deleteThisStmt();
            super.remove();
        }

        public void add(IR pIR)
        {
            SetRefRepr lSetRefRepr;
            lSetRefRepr = getBBlock().setRefRepr(pIR);
            add(lSetRefRepr);

            Stmt lStmt = (Stmt) ((SetRefReprHir) next.element).getStmt();
            lStmt.insertPreviousStmt((Stmt) pIR, lStmt.getBlockStmt());
        }

        public void addAfter(IR pIR)
        {
            Stmt lStmt;
            BBlock lBBlock;
            LabeledStmt lLabeledStmt;
            Stmt lUpperStmt;
            int lOpCode;
            HIR hir = ((HIR_Impl)pIR).hirRoot.hir;
            flow.dbg(5, "addAfter", FlowUtil.toString((HIR)pIR));
            if (hasNext())
            {
                lStmt = ((SetRefReprHir) next.element).getStmt();
                lStmt.insertPreviousStmt((Stmt) pIR, lStmt.getBlockStmt());
            } else if (hasPrevious())
            {
                lStmt = (Stmt) ((SetRefReprHir) next.previous.element).getStmt();
                lStmt.addNextStmt((Stmt)pIR); //##53
            } else if ((lBBlock = getBBlock()) != null)
            {
                lLabeledStmt = ((LabeledStmt) lBBlock.getIrLink());
                switch(lLabeledStmt.getLabel().getLabelKind())
                {
                    case Label.SWITCH_DEFAULT_LABEL:
                        lLabeledStmt.addNextStmt((Stmt)pIR); //##53
                        break;
                    default:
                        // The following needs to be modified.
                        if (isStmtNotAttachable(lLabeledStmt)) // End label of IF, LOOP, or SWITCH
                        {
                            lUpperStmt = lLabeledStmt.getUpperStmt();
                            lUpperStmt.addNextStmt((Stmt)pIR); //##53
                            //					lLabeledStmt.addNextStmt((Stmt)pIR, lLabeledStmt.getBlockStmt());
                        } else
                        {
                            while ((lLabeledStmt.getStmt() != null) && lLabeledStmt.getStmt().getOperator() == HIR.OP_LABELED_STMT)
                                lLabeledStmt = (LabeledStmt)lLabeledStmt.getStmt();
                            if (lLabeledStmt.getStmt() == null)
                            {
                                lUpperStmt = lLabeledStmt.getUpperStmt();
                                lUpperStmt.addNextStmt((Stmt)pIR); //##53
                            } else
                            {
                                switch (lLabeledStmt.getStmt().getOperator())
                                {
                                    case HIR.OP_WHILE:
                                    case HIR.OP_FOR:
                                    case HIR.OP_UNTIL:
                                    case HIR.OP_JUMP:
                                        BlockStmt lBlock = hir.blockStmt((Stmt)pIR);
                                        lLabeledStmt.setStmt(lBlock);
                                        break;
                                    case HIR.OP_BLOCK:
                                        ((BlockStmt)lLabeledStmt.getStmt()).addFirstStmt((Stmt)pIR);
                                        break;
                                    case HIR.OP_ASSIGN:
                                    case HIR.OP_IF:
                                    case HIR.OP_INDEXED_LOOP:
                                    case HIR.OP_SWITCH:
                                    case HIR.OP_RETURN:
                                    case HIR.OP_EXP_STMT:
                                    default:
                                        throw new FlowError("Unexpected.");
                                        //							lLabeledStmt.setStmt((Stmt) pIR);

                                        //                      ((lUpperStmt = lLabeledStmt.getUpperStmt()) != null)) {
                                        //                lUpperStmt.getBlockStmt().addLastStmt((Stmt) pIR);
                                        //          } else {
                                        //        }
                                }
                            }
                        }
                }
            }
            else
            {
                throw new FlowError();
            }

            SetRefRepr lSetRefRepr;
            lSetRefRepr = getBBlock().setRefRepr(pIR);
            addAfter(lSetRefRepr);
        }
    }
}
