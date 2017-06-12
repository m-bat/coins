/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import coins.aflow.FlowAdapter;
import coins.aflow.FlowResults;
import coins.aflow.SubpFlow;
import coins.ir.IrList; //##74
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ForStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator; //##74
import coins.ir.hir.IfStmt;
import coins.ir.hir.InfStmt; //##74
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SwitchStmt;

/////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop] Find Loop Parallel List
//
//
/////////////////////////////////////////////////////////////////////////
public class FindLoopParallelList
  extends FlowAdapter
{
  private LinkedList fLoopInfo;
  private SubpFlow fSubpFlow;
  private LoopUtil fUtil;
  private boolean lParallelizeFlag; //##74
  int fDbgLevel; //##74
  /**
   *
   * FindLoopParallelList:
   *
   *
   **/
  public FindLoopParallelList(FlowResults pResults)
  {
    super(pResults);
  }

  /**
   *
   *
   *
   **/
  public void find(SubpFlow pSubpFlow)
  {
    Stmt hirBody;
    hirBody = (Stmt)pSubpFlow.getSubpDefinition().getHirBody();
    fLoopInfo = new LinkedList();
    fSubpFlow = pSubpFlow;
    fUtil = new LoopUtil(fResults, fSubpFlow);
    fDbgLevel = ioRoot.dbgPara1.getLevel(); //##74
    lParallelizeFlag = false; //##74
    HirBodySearch(hirBody, (LoopTable)null);
    fResults.put("LoopParallelList", pSubpFlow, fLoopInfo);
    ioRoot.dbgPara1.print(3, "\nput LoopParallelList " + fLoopInfo); //##74
  }

  /**
   *
   * find:
   *
   *
   **/
  public void find(SubpFlow pSubpFlow, LinkedList pList)
  {
    LinkedList LoopInfo;
    Stmt hirBody;

    //
    // pList --> ForStmt1 --> ForStmt2 ---- ForStmtN
    //

    hirBody = (Stmt)pSubpFlow.getSubpDefinition().getHirBody();
    fLoopInfo = new LinkedList();
    fSubpFlow = pSubpFlow;
    fUtil = new LoopUtil(fResults, fSubpFlow);
    fDbgLevel = ioRoot.dbgPara1.getLevel(); //##74
    lParallelizeFlag = false; //##74
    HirBodySearch(hirBody, (LoopTable)null);
    LoopInfo = getLoopList(pList);
    fResults.put("LoopParallelList", pSubpFlow, LoopInfo);
    ioRoot.dbgPara1.print(3, "\nput LoopParallelList LoopInfo " + LoopInfo); //##74
  }

  /**
   *
   * getLoopList:
   *
   *
   **/
  private LinkedList getLoopList(LinkedList pList)
  {
    LinkedList LoopInfo;
    LoopInfo = new LinkedList();
    for (Iterator Ie = pList.iterator(); Ie.hasNext(); ) {
      ForStmt Loopst = (ForStmt)Ie.next();
      LoopTable l = getLoopTable(fLoopInfo, Loopst);
      if (l != null) {
        /*
                         if (pList.contains(l.OuterLoop.LoopStmt) == false) {
            l.OuterLoop = null;
                         }
         */
        LoopInfo.add(l);
      }

    }
    return (LoopInfo);
  }

  /**
   *
   * getLoopTable:
   *
   *
   **/
  private LoopTable getLoopTable(LinkedList pLoopInfo, ForStmt pForStmt)
  {
    if (pLoopInfo == null) {
      return null;
    }
    for (Iterator Ie = pLoopInfo.iterator(); Ie.hasNext(); ) {
      LoopTable lTable = (LoopTable)Ie.next();
      LoopTable l = getLoopTable(lTable.InnerLoopList, pForStmt);
      if (l != null) {
        return (l);
      }
      if (pForStmt == lTable.LoopStmt) {
        return (lTable);
      }
    }
    return null;
  }

  /**
   *
   * FindLoop:
   *
   *
   **/
  private void FindLoop(SubpFlow pSubpFlow, LoopTable pTable)
  {

    for (Iterator Ie = pTable.InnerLoopList.iterator(); Ie.hasNext(); ) {
      FindLoop(pSubpFlow, (LoopTable)Ie.next());
    }
    fResults.find("LoopParallel", pSubpFlow, pTable);
  }

  /**
   *
   * HirBodySearch:
   *
   *
   **/
  private void HirBodySearch(Stmt pTree, LoopTable pTable)
  {

    LoopSearch(pTree, pTable, 0);

  }

  /**
   *
   * LoopSearch:
   *
   *
   **/
  private boolean LoopSearch(Stmt pstmt, LoopTable pTable, int LoopNestLevel)
  {
    Stmt nextStmt;
    IfStmt stmtIF;
    LoopStmt stmtLOOP;
    SwitchStmt stmtSWITCH;
    LoopTable lTable;
    int op;
    boolean ForExist;

    nextStmt = pstmt;
    ForExist = false;
    lTable = pTable;

    while (nextStmt != null) {
      op = nextStmt.getOperator();

      switch (op) {
        case HIR.OP_IF:
          stmtIF = (IfStmt)nextStmt;
          if (LoopSearch(stmtIF.getThenPart(), pTable, LoopNestLevel) == true) {
            ForExist = true;
          }
          if (LoopSearch(stmtIF.getElsePart(), pTable, LoopNestLevel) == true) {
            ForExist = true;
          }
          nextStmt = nextStmt.getNextStmt();
          break;
        //##74 BEGIN
      case HIR.OP_INF:
        InfStmt lInfStmt = (InfStmt)nextStmt;
        String lInfKind = lInfStmt.getInfKind().intern();
        //##74 if (lInfKind == "doAll")
        if (lInfKind == "parallel") //##74
        {
          IrList lOptionList = lInfStmt.getInfList(lInfKind);
          Object lObject = lOptionList.get(0);
          ioRoot.dbgPara1.print(3, lInfStmt.toString() + " " + lObject
            + " " + lObject.getClass() + " " + lOptionList + "\n");
          //##74 if ((lObject instanceof String) &&
          //##74  ((String)lObject).intern() == "parallel")
          if (lObject instanceof String)
          {
            String lOptionName = (String)((String)lObject).intern(); //##74
            if ((lOptionName == "doAll")||     //##74
                (lOptionName == "forceDoAll")) //##74
              // Set parallelizing flag.
              lParallelizeFlag = true;
          }
        } // End of doAll
        nextStmt = nextStmt.getNextStmt();
        break;
        //##74 END
        case HIR.OP_FOR:
        case HIR.OP_INDEXED_LOOP:
          if (lParallelizeFlag) { //##74
            // Parallelize this loop and its inner loops.
            ioRoot.dbgPara1.print(3, "\n parallelize " + nextStmt.toStringShort());
            stmtLOOP = (LoopStmt)nextStmt;
            lTable = SetLoopInfo((ForStmt)nextStmt, pTable);
            ForExist = true;
            if (LoopSearch(stmtLOOP.getLoopBodyPart(), lTable, LoopNestLevel) == true) {
              ForExist = true;
            }
            nextStmt = nextStmt.getNextStmt();
            lParallelizeFlag = false;
          //##74 BEGIN
          }else {
            // Unnecessary to be parallelized.
            ioRoot.dbgPara1.print(3, "\n do not parallelize " + nextStmt.toStringShort());
            stmtLOOP = (LoopStmt)nextStmt;
            if (LoopSearch(stmtLOOP.getLoopBodyPart(), pTable, LoopNestLevel) == true) {
              ForExist = true;
            }
            nextStmt = nextStmt.getNextStmt();
          }
          //##74 END
          break;
        case HIR.OP_WHILE:
        case HIR.OP_UNTIL:
          stmtLOOP = (LoopStmt)nextStmt;
          if (LoopSearch(stmtLOOP.getLoopBodyPart(), pTable, LoopNestLevel) == true) {
            ForExist = true;
          }
          nextStmt = nextStmt.getNextStmt();
          break;
        case HIR.OP_LABELED_STMT:

          //##70 nextStmt = ((LabeledStmt)nextStmt).getStmt();
          //if (nextStmt== null ) {
          //       nextStmt = nextStmt.getNextStmt();
          //}
          //##70 BEGIN
          if (((LabeledStmt)nextStmt).getStmt() != null) {
            nextStmt = ((LabeledStmt)nextStmt).getStmt();
          } else {
            // Statement body is null. Get the next statement.
            nextStmt = nextStmt.getNextStmt();
          }
          //##70 END
          break;
        case HIR.OP_JUMP:
          nextStmt = nextStmt.getNextStmt();
          break;
        case HIR.OP_SWITCH:
          stmtSWITCH = (SwitchStmt)nextStmt;
          if (LoopSearch(stmtSWITCH.getBodyStmt(), pTable, LoopNestLevel) == true) {
            ForExist = true;
          }
          nextStmt = nextStmt.getNextStmt();
          break;
        case HIR.OP_EXP_STMT: // ==>OP_CALL
        case HIR.OP_CALL: //?
          nextStmt = nextStmt.getNextStmt();
          break;
        case HIR.OP_BLOCK:
          if (LoopSearch(((BlockStmt)nextStmt).getFirstStmt(), pTable,
            LoopNestLevel) == true) {
            ForExist = true;
          }
          nextStmt = nextStmt.getNextStmt();
          break;
        case HIR.OP_ASSIGN:
          nextStmt = nextStmt.getNextStmt();
          break;
        case HIR.OP_RETURN:
          nextStmt = nextStmt.getNextStmt();
          break;
        case HIR.OP_SEQ:
          nextStmt = nextStmt.getNextStmt();
          break;
        default:
          nextStmt = nextStmt.getNextStmt();
      }
    }
    return ForExist;
  }

  /**
   *
   *  SetLoopInfo:
   *
   *
   **/
  private LoopTable SetLoopInfo(ForStmt pstmt, LoopTable pTable)
  {
    LoopTable lTable;
    lTable = new LoopTable(pstmt, fSubpFlow);
    if (pTable == null) {
      fLoopInfo.add(lTable); // Top Loop
    }
    if (pTable != null) {
      // Child Loop
      pTable.InnerLoopList.add(lTable);
      pTable.InnerLoop = false;
      lTable.OuterLoop = pTable;
      lTable.fNestLevel = pTable.fNestLevel + 1;
    }
    return lTable;
  }

  /**
   *
   * Trace:
   *
   *  print basic parallelizer information.
   *
   *  Driver Option :  [-coins:trace=Para1.n]
   *
   *
   *
   **/
  private void Trace(String s, int n)
  {
    fResults.flowRoot.hirRoot.ioRoot.dbgPara1.print(n, "//" + s + "\n");
  }
}
