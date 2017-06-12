/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * AssignHashBasedFlowExpId.java
 *
 * Created on July 31, 2002, 2:33 PM
 *
 * Hash-code based implementation of <code>AssignFlowExpId</code>. The identity of an expression is based on its hash codes computed from the nodes that comprise the expression. This is the weakest possible identification, that is, there could be better ways to identify expressions.
 */
package coins.aflow;

import java.util.HashSet;
import java.util.Iterator;

import coins.FlowRoot;
import coins.IoRoot;
import coins.aflow.util.FAList;
import coins.aflow.util.FlowError;
import coins.ir.IR;
import coins.ir.hir.HIR;
import coins.ir.hir.AssignStmt;  //##53
//import coins.ir.lir.LIRNode; // 2004.05.31 S.Noishi
//import coins.ir.lir.LIRType; // 2004.05.31 S.Noishi


/**
 *
 * @author  hasegawa
 */
abstract public class AssignHashBasedFlowExpId implements AssignFlowExpId {
    //##55 public static final int EXP_ID_HASH_SIZE = 127; // Prime number
    public static final int EXP_ID_HASH_SIZE = 499; // Prime number //##55
    // Prime numbers: 599, 691, 797, 887, 997
    final HashBasedFlowExpId[] fFlowExpIdHashtable = new HashBasedFlowExpId[EXP_ID_HASH_SIZE];
    FAList fFlowExpIdTable = new FAList();
    SubpFlow fSubpFlow;
    public final FlowRoot flowRoot;
    public final IoRoot ioRoot;
    protected final FlowResults fResults;

    public AssignHashBasedFlowExpId(SubpFlow pSubpFlow) {
        fSubpFlow = pSubpFlow;
        flowRoot = ((SubpFlowImpl) fSubpFlow).flowRoot;
        ioRoot = flowRoot.ioRoot;
        fResults = fSubpFlow.results();
        fResults.put("AssignerForSubpFlow", fSubpFlow, this);
        fSubpFlow.allocateExpIdTable(); //##25
    }

    /**
     * Assigns <code>FlowExpId</code>s to nodes over <code>SubpFlow</code>.
     */
    public void assign() {
        flowRoot.aflow.dbg(2, " AssignHashBasedFlowExpId.","assign"); //##56
        flowRoot.aflow.dbg(4," fRefults=" + fResults); //##56
        fSubpFlow.setFlowExpIdTable(fFlowExpIdTable);  //##56
        for (Iterator lIt = fSubpFlow.getBBlocks().iterator();
             lIt.hasNext();) {
          Object lObject = lIt.next(); //##25
          //##25 assignForBBlock( (BBlock) lIt.next());
          flowRoot.aflow.dbg(3, " assign ",
             ((BBlock)lObject).toString());    //##25
          assignForBBlock( (BBlock) lObject);  //##25
        }

        //##56 fSubpFlow.setFlowExpIdTable(fFlowExpIdTable);
        ioRoot.dbgFlow.print(4, "fFlowExpIdHashtable: " + fFlowExpIdHashtable);
        if (flowRoot.ioRoot.dbgFlow.getLevel() >= 2)  //##25 //##62
          fSubpFlow.printExpIdTable();                //##25
        if (flowRoot.ioRoot.dbgFlow.getLevel() >= 3)  //##56
          fSubpFlow.getSubpDefinition().printHir("after ExpId assignment"); //##56
    }

    void assignForBBlock(BBlock pBBlock) {
        SetRefRepr lSetRefRepr;

        for (SetRefReprIterator lIt = pBBlock.getSetRefReprs()
                                             .setRefReprIterator();
                lIt.hasNext();) {
             Object lObject = lIt.next();                       //##25
            //##25 lSetRefRepr = (SetRefRepr) lIt.next();
            flowRoot.aflow.dbg(3," assignForBBlock ", lObject); //##25
            lSetRefRepr = (SetRefRepr) lObject;                 //##25
            assignForSetRefRepr(lSetRefRepr);
        }
    }

    void assignForSetRefRepr(SetRefRepr pSetRefRepr) {
        flowRoot.aflow.dbg(3," assignForSetRefRepr", " "); //##56
        java.util.Set lFlowExpIds = new HashSet();
        java.util.Set lUseFlowExpIds = new HashSet();

        for (NodeIterator lIt = FlowUtil.nodeIterator(pSetRefRepr.getIR());
                lIt.hasNext();) {
            IR lIR = lIt.next();
            FlowExpId lFlowExpId;

            if (AssignHashBasedFlowExpId.shouldAssignFlowExpId(lIR)) {
                lFlowExpId = assignToNode(lIR);
                lFlowExpIds.add(lFlowExpId);

                if (!pSetRefRepr.sets() || ((pSetRefRepr).defNode() != lIR)) {
                    lUseFlowExpIds.add(lFlowExpId);
                }
            }
        }

        fResults.put("FlowExpIdsForSetRefRepr", pSetRefRepr, lFlowExpIds);
        fResults.put("UseFlowExpIdsForSetRefRepr", pSetRefRepr, lUseFlowExpIds);
        ioRoot.dbgFlow.print(4, " put UseFlowExpIdsForSetRefRepr",
          pSetRefRepr + " " + lUseFlowExpIds); //##25
    }

    public FlowExpId assignToNode(IR pIR) {
        //System.out.println("flowexpidtable " + fFlowExpIdTable);
        //   if (!(pIR instanceof HIR))
        //        System.out.println("node " + ((LIRTree)pIR).dumpTree());
        //##56 BEGIN
        int lIndex = pIR.getIndex();
        FlowExpId lFlowExpId0 = ((SubpFlowImpl)fSubpFlow).getExpId(pIR, lIndex);
        if (lFlowExpId0 != null) {
          ioRoot.dbgFlow.print(4, " allocated " +
              ((HIR)pIR).toStringShort() + " " + lFlowExpId0.toStringShort());
          return lFlowExpId0;
        }
        //##56 END
        int lHashCode = FlowUtil.computeHashCodeOfNode(pIR);
        ioRoot.dbgFlow.print(4, "AssignHashBasedFlowExpId",
            ((HIR)pIR).toStringShort() + ": hash= " + lHashCode); //##56

        FlowResults lResults = fSubpFlow.results();
        int lFlowExpIdTableSize = fFlowExpIdTable.size();
        HashBasedFlowExpId lFlowExpId;

        for (lFlowExpId = fFlowExpIdHashtable[lHashCode]; lFlowExpId != null;
                lFlowExpId = lFlowExpId.getNextInChain())
            if (FlowUtil.isSameTree(lFlowExpId.getTree(), pIR)) {
                break;
            }

        if (lFlowExpId == null) {
            lResults.setFlowExpIdForNode(pIR,
                lFlowExpId = newHashBasedFlowExpId(pIR, ++lFlowExpIdTableSize,
                        fSubpFlow));
            fFlowExpIdTable.add(lFlowExpId);
            lFlowExpId.setNextInChain(fFlowExpIdHashtable[lHashCode]);
            fFlowExpIdHashtable[lHashCode] = lFlowExpId;

            //   if (!(pIR instanceof HIR))
            //        System.out.println("node " + ((LIRTree)pIR).dumpTree() + "has been assigned a flowexpid");
        } else {
            lResults.setFlowExpIdForNode(pIR, lFlowExpId);
        }
        fSubpFlow.setExpId(pIR, lFlowExpId);          //##25
        if (pIR instanceof HIR) {                     //##25
          //##62 ( (HIR) pIR).setExpId(lFlowExpId.getExpId()); //##25
          if ((((HIR)pIR).getParent() instanceof AssignStmt)&& //##53
              (((HIR)pIR).getParent().getChild1() == pIR))     //##53
            lFlowExpId.setLHSFlag();                           //##53
        }
        ioRoot.dbgFlow.print(4, lFlowExpId.toStringShort()); //##56
        return lFlowExpId;
    }

    //        abstract HashBasedFlowExpId newHashBasedFlowExpId(IR pIR, int pIndex, FlowResults pResults);
    abstract HashBasedFlowExpId newHashBasedFlowExpId(IR pIR, int pIndex,
        SubpFlow pSubpFlow);

    /**
     * Returns true if the given node should be assigned a FlowExpId.
     */
    public static boolean shouldAssignFlowExpId(IR pIR) {
        if (pIR instanceof HIR) {
            switch (((HIR) pIR).getOperator()) {
            case HIR.OP_PROG:
            case HIR.OP_SUBP_DEF:
            case HIR.OP_LABEL_DEF:
            case HIR.OP_INF:
            case HIR.OP_CONST:
            case HIR.OP_SYM:
            case HIR.OP_SUBP:
            case HIR.OP_TYPE:
            case HIR.OP_LABEL:
            case HIR.OP_LIST:
            case HIR.OP_SEQ:
            case HIR.OP_ENCLOSE:
            case HIR.OP_LABELED_STMT:
            case HIR.OP_ASSIGN:
            case HIR.OP_IF:
            case HIR.OP_WHILE:
            case HIR.OP_FOR:
            case HIR.OP_UNTIL:
            case HIR.OP_INDEXED_LOOP:
            case HIR.OP_JUMP:
            case HIR.OP_SWITCH:
            case HIR.OP_RETURN:
            case HIR.OP_BLOCK:
            case HIR.OP_EXP_STMT:
            case HIR.OP_STMT_UPPER:
            case HIR.OP_SETDATA:
            case HIR.OP_NULL:
            case HIR.OP_SELECT:
            case HIR.OP_COMMA:
                return false;

            case HIR.OP_VAR:
            case HIR.OP_PARAM:
            case HIR.OP_ELEM:
            case HIR.OP_SUBS:
            case HIR.OP_INDEX:
            case HIR.OP_QUAL:
            case HIR.OP_ARROW:
            case HIR.OP_CALL:
            case HIR.OP_ADD:
            case HIR.OP_SUB:
            case HIR.OP_MULT:
            case HIR.OP_DIV:
            case HIR.OP_MOD:
            case HIR.OP_AND:
            case HIR.OP_OR:
            case HIR.OP_XOR:
            case HIR.OP_CMP_EQ:
            case HIR.OP_CMP_NE:
            case HIR.OP_CMP_GT:
            case HIR.OP_CMP_GE:
            case HIR.OP_CMP_LT:
            case HIR.OP_CMP_LE:
            case HIR.OP_SHIFT_LL:
            case HIR.OP_SHIFT_R:
            case HIR.OP_SHIFT_RL:
            case HIR.OP_NOT:
            case HIR.OP_NEG:
            case HIR.OP_ADDR:
            case HIR.OP_CONV:
            case HIR.OP_DECAY:
            case HIR.OP_UNDECAY: //##10
            case HIR.OP_CONTENTS:
            case HIR.OP_SIZEOF:
            case HIR.OP_PHI:
            case HIR.OP_OFFSET:
            case HIR.OP_LG_AND:
            case HIR.OP_LG_OR:
            case HIR.OP_PRE_INCR:
            case HIR.OP_PRE_DECR:
            case HIR.OP_POST_INCR:
            case HIR.OP_POST_DECR:
            case HIR.OP_ADD_ASSIGN:
            case HIR.OP_SUB_ASSIGN:
            case HIR.OP_MULT_ASSIGN:
            case HIR.OP_DIV_ASSIGN:
            case HIR.OP_MOD_ASSIGN:
            case HIR.OP_SHIFT_L_ASSIGN:
            case HIR.OP_SHIFT_R_ASSIGN:
            case HIR.OP_AND_ASSIGN:
            case HIR.OP_OR_ASSIGN:
            case HIR.OP_XOR_ASSIGN:
                return true;

            default:
                throw new FlowError();
            }
        } else {
            throw new FlowError(); // 2004.05.31 S.Noishi
            /*
            LIRType lLIRType = ((LIRNode) pIR).getLIRType();

            if ((lLIRType == LIRType.OPERATOR) ||
                    (lLIRType == LIRType.CONDITION) ||
                    (lLIRType == LIRType.REGISTER) ||
                    (lLIRType == LIRType.VARIABLE) ||
                    (lLIRType == LIRType.MEMORY) || (lLIRType == LIRType.CALL) ||
                    (lLIRType == LIRType.SUBREG) ||
                    (lLIRType == LIRType.CLOBBER)) {
                return true;
            } else if ((lLIRType == LIRType.JUMP) ||
                    (lLIRType == LIRType.CONST) || (lLIRType == LIRType.LABEL) ||
                    (lLIRType == LIRType.LABELDEF) ||
                    (lLIRType == LIRType.LISTA) || (lLIRType == LIRType.LISTD) ||
                    (lLIRType == LIRType.PARALLEL) ||
                    (lLIRType == LIRType.USE) ||
                    (lLIRType == LIRType.ASMCONST) ||
                    (lLIRType == LIRType.PROLOGUE) ||
                    (lLIRType == LIRType.EPILOGUE) ||
                    (lLIRType == LIRType.PHI) ||
                    (lLIRType == LIRType.LINEINFO)) {
                return false;
            } else {
                throw new FlowError();
            }
            */
        }
    }
}
