/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * RecordSetRefReprHir.java
 *
 * Created on September 5, 2002, 4:37 PM
 */
package coins.aflow;

import coins.aflow.util.FAList;
import coins.aflow.util.FlowError;
import coins.ir.hir.HIR;
import coins.ir.hir.AssignStmt;  //##25
import coins.ir.hir.ExpStmt;     //##25
import coins.ir.hir.HirIterator; //##25
import coins.ir.hir.LabeledStmt; //##25
import coins.ir.hir.Stmt;        //##25
import coins.sym.ExpId;          //##25

/**
 *
 * @author  hasegawa
 */
public class RecordSetRefReprsHir extends RecordSetRefReprs {
    /** Creates a new instance of RecordSetRefReprHir */
    public RecordSetRefReprsHir(FlowResults pResults) {
        super(pResults);
    }

/* //##25 BEGIN --------------------

    protected void record(BBlock pBBlock, FAList pSubpFlowSetReprs) {
        SetRefReprList lBBlockSetRefReprs = new SetRefReprListHir((BBlockHir) pBBlock);
        HIR lStmt;
        SetRefRepr lSetRefRepr;
        int lOpCode;
        int lDefIndex;
        int lNodeIndex;
        BBlockStmtIterator lBBlockStmtIterator = new BBlockStmtIterator((BBlockHir) pBBlock);

        while ((lStmt = (HIR) lBBlockStmtIterator.next()) != BBlockStmtIterator.EOB) {
            flow.dbg(5, "RecordSetRefReprsHir", lStmt);

            //System.out.println(pBBlock + " " + lStmt);
            //if (lSubtree != null)
            //System.out.println("     " + HIR.OP_CODE_NAME[lSubtree.getOperator()]);
            lOpCode = lStmt.getOperator();

            //System.out.println(lOpCode);
            //			if (true)
            //				continue;
            //			if (lOpCode == HIR.OP_RETURN)
            //				System.out.println("RETURN");
            switch (lOpCode) {
            case HIR.OP_LABELED_STMT:
            case HIR.OP_WHILE:
            case HIR.OP_FOR:
            case HIR.OP_UNTIL:
            case HIR.OP_JUMP:
            case HIR.OP_CALL:
            case HIR.OP_BLOCK:
            case HIR.OP_SETDATA:  //##42
                break;

            case HIR.OP_ASSIGN:
            case HIR.OP_IF:
            case HIR.OP_INDEXED_LOOP:
            case HIR.OP_SWITCH:
            case HIR.OP_RETURN:
            case HIR.OP_EXP_STMT:
            case HIR.OP_STMT_UPPER:
                lSetRefRepr = pBBlock.setRefRepr(lStmt);
                lBBlockSetRefReprs.add(lSetRefRepr);
                pSubpFlowSetReprs.add(lSetRefRepr);

                break;

            //					lSetRefRepr = pBBlock.setRefRepr(lStmt);
            //					lBBlockSetRefReprs.add(lSetRefRepr);
            //					break;
            default:
                throw new FlowError("Unexpected subtree \"" +
                    lStmt.getIrName() +
                    "\". This type of subtree may not be supported yet.");
            }
        }

        fResults.put("BBlockSetRefReprs", pBBlock, lBBlockSetRefReprs);
    }
*/ //##25 END ---------------

//##25 BEGIN

protected void
record(BBlock pBBlock, FAList pSubpFlowSetReprs)
{
  SetRefReprList lBBlockSetRefReprs
    = new SetRefReprListHir( (BBlockHir) pBBlock);
  HIR lHir;
  Stmt lStmt;
  SetRefRepr lSetRefRepr;
  int lOpCode;
  int lDefIndex;
  int lNodeIndex;
  SubpFlow    lSubpFlow;    //##25
  HirIterator lHirIterator; //##25
  flow.dbg(3, "RecordSetRefReprsHir.record",
           "B" + pBBlock.getBBlockNumber());
  // BBlockStmtIterator lBBlockStmtIterator
  //   = new BBlockStmtIterator((BBlockHir) pBBlock);
  lSubpFlow = pBBlock.getSubpFlow();
  // bblockNodeIterator cannot be used until control flow analysis
  // is done.
  // For each statement in pBBlock, traverse node to give
  // SetRefRepr to it if required.
  // Nodes to which SetRefRepr is to be given are:
  //   AssignStmt, ExpStmt, and expression nodes specified by
  //   shouldAssignFlowExpId.
  BBlockStmtIterator lBBlockStmtIterator1
    = new BBlockStmtIterator( (BBlockHir) pBBlock);
  while ( (lStmt = (Stmt)lBBlockStmtIterator1.next()) !=
             BBlockStmtIterator.EOB) {
    if (lStmt != null) {
      flow.dbg(4, "Stmt", lStmt.toString());
      if ( ( (SubpFlowImpl)lSubpFlow).fHirAnalExtended) {
        for (HirIterator lIterator
              = flowRoot.hirRoot.hir.hirIterator(lStmt);
             lIterator.hasNext(); ) {
          lHir = (HIR) lIterator.next();
          if (lHir != null) {
            if (lHir instanceof LabeledStmt)
              break;  // Statement body will be processed by the next
                      // Stmt-iteration.
            lNodeIndex = lHir.getIndex();
            if ((lHir instanceof AssignStmt) ||
                (lHir instanceof ExpStmt) ||
                (AssignHashBasedFlowExpId.shouldAssignFlowExpId(lHir))) {
              flow.dbg(5, "   node ", lHir.toStringShort());
              lSetRefRepr = lSubpFlow.getSetRefReprOfIR(lNodeIndex);
              flow.dbg(5,  lSetRefRepr);
              if (lSetRefRepr == null) {
                lSetRefRepr = pBBlock.setRefRepr(lHir);
                lSubpFlow.setSetRefReprOfIR(lSetRefRepr, lNodeIndex);
              }
              flow.dbg(5, " modSyms", lSetRefRepr.modSyms());
              flow.dbg(5, " leafOperands",
                       ((SetRefReprHirEImpl)lSetRefRepr).leafOperands());
              if (! lBBlockSetRefReprs.contains(lSetRefRepr)) {
                lBBlockSetRefReprs.add(lSetRefRepr);
              }
              if (! pSubpFlowSetReprs.contains(lSetRefRepr)) {
                pSubpFlowSetReprs.add(lSetRefRepr);
              }
            }
          }
        }
      } // end of fHirAnalExtended
      else {
        lOpCode = lStmt.getOperator();
        switch (lOpCode) {
        case HIR.OP_LABELED_STMT:
        case HIR.OP_WHILE:
        case HIR.OP_FOR:
        case HIR.OP_UNTIL:
        case HIR.OP_JUMP:
        case HIR.OP_CALL:
        case HIR.OP_BLOCK:
        case HIR.OP_INF:  //##62
          break;
        case HIR.OP_ASSIGN:
        case HIR.OP_IF:
        case HIR.OP_INDEXED_LOOP:
        case HIR.OP_SWITCH:
        case HIR.OP_RETURN:
        case HIR.OP_EXP_STMT:
        case HIR.OP_STMT_UPPER:
          lSetRefRepr = pBBlock.setRefRepr(lStmt);
          lBBlockSetRefReprs.add(lSetRefRepr);
          pSubpFlowSetReprs.add(lSetRefRepr);
          break;
        default:
          throw new FlowError("Unexpected subtree \"" +
                              lStmt.getIrName() +
              "\". This type of subtree may not be supported yet.");
        }
      }
    }
  }
  fResults.put("BBlockSetRefReprs", pBBlock, lBBlockSetRefReprs);
  flow.dbg(5, "BBlockSetRefReprs", lBBlockSetRefReprs); //##25
} // record

//##25 END

} // RecordSetRefReprsHir
