/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List; //##70
import java.util.Set;

import coins.aflow.BBlock;
import coins.aflow.SubpFlow;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR; //##70
import coins.ir.hir.ForStmt;
import coins.ir.hir.Stmt;
import coins.sym.Sym; //##70
////////////////////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop] Loop Table
//   recording characteristics of the loop and
//   statements to be added/replaced to parallelize the loop.
//
//
////////////////////////////////////////////////////////////////////////////////////////////
public class LoopTable {
    public  int fNestLevel;
    public  LinkedList InnerLoopList; // Tree --> Child
                             // List of LoopTables representing inner loops ?
    public  LoopTable  OuterLoop;     // Tree --> Parent
                                      // LoopTable of outer loop.
    public  boolean InnerLoop;        // if  (InnerLoopList.size()==0 ) InnnerLoop= true

    public  Invariant fInv;
    public  LinkedList DefVarList;

    public  ForStmt LoopStmt;  // ForStmt corresponding to this loop.
    public  BasicInduction LoopCtrInduction; // Loop control induction variable ?
    public  LinkedList IndList;          // Induction Table List
    public  LinkedList refList;          // List of Ref_Arrays showing
                                         // refered areas
    public  LinkedList finalExpList;     // (exit_cont)

    public  LinkedList ReductionADDList; // Reduction Table List (OP='+')
    public  LinkedList ReductionMULList; // Reduction Table List (OP='*')
    public  LinkedList ReductionSUBList; // Reduction Table List (op='-')
    public  LinkedList ReductionMAXList; // Reduction Table List (op="max") //##70
    public  LinkedList ReductionMINList; // Reduction Table List (op="min") //##70
    public  LinkedList ReductionMAXINDEXList; // Reduction Table List (op="maxIndex") //##70
    public  LinkedList ReductionMININDEXList; // Reduction Table List (op="minIndex") //##70


    public  LinkedList BBlockList;       // BBlock  List
    public  LinkedList InnerBBlockList;  // BBlock  List

    public  Set LastPrivate;          // Set of VarNodes of last-private variables ?
    public  Set ArrayLastPrivate;     // Set of array element nodes of
                                      // last-private arrays ?
    public  Set Private;              // Set of VarNodes of private variables ?
    public  Set StructNodeHash;       // Node(OP = HIR.OP_QUAL)

    public  long repeat_no_value    ; // repeat_no =10 i< 10
    public  long repeat_no_normalize; // repeat_no =10 i< 10 (normalize)
    public  boolean const_repeat_no ; //    i< n
    public  boolean zero_check;       // True if initial value of loop control
                                      //   variable is constant ?
    public  Exp finalExp ;            //   (n)  (last value ?)

    public  Ref_Table ControlRef;  // Refered areas covered by the whole loop.
    public  Ref_Table ExpandRef;
    private boolean para_flag;     // true if the loop can be parallelized ?
    public  boolean analysis_flag;
    //
    // add HIR
    //  (HIR added to normalize the loop ?)
    //   for (i=e1;i<e2;i=i+e3)
    //   =>for (#tmp=0;#tmp<(e2-e1)/e3 ;#tmp= #tmp+1)
    //          addInit  addCond        addStep
    public LinkedList addConditionPart; // List of AssignStmts to set
                          // value to variable used in condition part ?
    public LinkedList addConditionDefList; // List of left-hand-side VarNode
                          // in AssignStmts listed in addCondition part ?
    public Stmt addInit;  // Initiation part of added Stmt ?
    public Exp addCond;   // Condition Exp of added Stmt ?
    public Stmt addStep;  // Step part of added Stmt ?
    public final Stmt originalLoopInit; //##70
    //
    // Result List
    // showing obstacles for parllelization (made when unparallelizable).
    public LinkedList mod_euseResultList;
    public LinkedList use_modResultList;
    public LinkedList mod_modResultList;
    public LinkedList varResultList;
    public LinkedList varUnParalellLastPrivateList;
    /**
    *
    *  LoopTable
    *
    *
    **/
    public LoopTable(ForStmt pstmt,SubpFlow pSubpFlow)
    {
        LoopStmt = pstmt;
        InnerLoopList = new LinkedList();
        OuterLoop     = null;

        IndList = new LinkedList();
        refList = new LinkedList();
        DefVarList = new LinkedList();
        finalExpList  = new LinkedList();

        ReductionADDList  = new LinkedList();
        ReductionSUBList  = new LinkedList();
        ReductionMULList  = new LinkedList();
        ReductionMAXList  = new LinkedList();      //##70
        ReductionMINList  = new LinkedList();      //##70
        ReductionMAXINDEXList  = new LinkedList(); //##70
        ReductionMININDEXList  = new LinkedList(); //##70

        BBlockList  = new LinkedList();
        InnerBBlockList  = new LinkedList();

        LastPrivate = new HashSet();
        ArrayLastPrivate = new HashSet();
        Private     = new HashSet();
        StructNodeHash = new HashSet();
        repeat_no_value  = -1;
        repeat_no_normalize= 0;
        zero_check = false;

        ControlRef =  new Ref_Table(pSubpFlow,(BBlock)null);
        ExpandRef  = new Ref_Table(pSubpFlow,(BBlock)null);
        para_flag  = false;
        analysis_flag = false;
        InnerLoop = true;
        addConditionPart = new LinkedList();
        addConditionDefList = new LinkedList();
        addInit = null;
        Exp addCond = null;
        Stmt addStep = null;
        originalLoopInit = (Stmt)pstmt.getLoopInitPart().copyWithOperands(); //##70
        //
        // Result List
        //
        mod_euseResultList = new LinkedList();
        use_modResultList = new LinkedList();
        mod_modResultList = new  LinkedList();
        varResultList = new LinkedList();
        varUnParalellLastPrivateList = new LinkedList();
        refList = new LinkedList();

    }
    /**
    *
    * setParaFlag:
    *
    *
    **/
    void setParaFlag(LoopUtil pUtil,boolean pFlag ,String comment) {

        para_flag = pFlag;
        pUtil.Trace(comment+"\t:setParaFlag NestLevel="+fNestLevel+ "\tFlag="+para_flag,5);

    }
    /**
    *
    * getParaFlag:
    *
    *
    */
    public //##70
    boolean getParaFlag(LoopUtil pUtil,String comment) {
        pUtil.Trace(comment+"\t:getParaFlag NestLevel="+fNestLevel+ "\tFlag="+para_flag,5);
        return para_flag;
    }
    public boolean getParaFlag()  {
        return para_flag;
    }
    /**
    *
    * DebugInductionList:
    *
    *
    */
   public //##70
    void DebugInductionList(LoopUtil pUtil)
    {
        pUtil.Trace("-----DebugInductionList-----",4);
        for (Iterator Ie = IndList.iterator();Ie.hasNext();) {
            BasicInduction IndTable = (BasicInduction)Ie.next();
            IndTable.DebugInduction(pUtil);
        }
        pUtil.Trace("----------------------------",4);
    }
  //##70 BEGIN
      public void
      print( int pLevel )
      {
          System.out.print("\nLoopTable of " + LoopStmt.toStringShort()
            + " nestLevel " + fNestLevel + " isInner " + InnerLoop
            //##73 + " " + this
            + " para_flag " + getParaFlag());
          printList(DefVarList, " DefVarList");
          printList(BBlockList, " BBlockList");
          printList(InnerBBlockList, " InnerBBlockList");
          printSet(LastPrivate, " LastPrivate");
          printSet(ArrayLastPrivate, " ArrayLastPrivate");
          printSet(Private, " Private");
          printList(mod_euseResultList, " mod_euseResultList");
          printList(use_modResultList, " use_modResultList");
          printList(mod_modResultList, " mod_modResultList");
          printList(varResultList, " varResultList");
          printList(varUnParalellLastPrivateList, " varUnParalellLastPrivateList");
          printList(addConditionPart, " addConditionPart"); //##70
          printList(addConditionDefList, " addConditionDefList"); //##70
          //##74 BEGIN
          printList(ReductionADDList, " reductionAddList");
          printList(ReductionSUBList, " reductionSubList");
          printList(ReductionMULList, " reductionMullList");
          printList(ReductionMAXList, " reductionMaxList");
          printList(ReductionMINList, " reductionMinList");
          printList(ReductionMAXINDEXList, " reductionMaxIndexList");
          printList(ReductionMININDEXList, " reductionMinIndexList");
          //##74 END
          if (originalLoopInit != null) {
            System.out.print(" originalLoopInit "
              + originalLoopInit.toStringWithChildren()+ "\n");
          }
          if (InnerLoopList != null) {
            System.out.print("\n InnnerLoopList level " + pLevel + 1 + " ");
            for (Iterator lIt2 = InnerLoopList.iterator();
                 lIt2.hasNext(); ) {
              LoopTable lInnerLoop = (LoopTable)lIt2.next();
              lInnerLoop.print(pLevel + 1);
            }
          }
      } // printLoopTable

      protected void
      printList( List pList, String pHeader )
      {
        if (pList == null)
          return;
          System.out.print("\n" + pHeader + "(");
          for (Iterator lIt = pList.listIterator();
               lIt.hasNext(); ) {
            Object lItem = lIt.next();
            if (lItem == null)
              continue;
            if (lItem instanceof List) {
              printList((List)lItem, " ");
            }else if (lItem instanceof HIR){
              System.out.print(" " + ((HIR)lItem).toStringWithChildren());
            }else if (lItem instanceof Sym){
              System.out.print(" " + ((Sym)lItem).getName());
            }else if (lItem instanceof LoopTable){
              ((LoopTable)lItem).print(1);
            }else if (lItem instanceof Reduction) { //##74
              ((Reduction)lItem).DefVarNode.getSym().getName(); //##74
            }else {
              System.out.print(" " + lItem.toString());
            }
          }
          System.out.print(")");
      } // printList

      protected void
      printSet( Set pSet, String pHeader )
      {
        if (pSet == null)
          return;
          System.out.print("\n" + pHeader + "[");
          for (Iterator lIt = pSet.iterator();
               lIt.hasNext(); ) {
            Object lItem = lIt.next();
            if (lItem == null)
              continue;
            if (lItem instanceof List) {
              printList((List)lItem, " ");
            }else if (lItem instanceof HIR){
              System.out.print(" " + ((HIR)lItem).toStringShort());
            }else if (lItem instanceof Sym){
              System.out.print(" " + ((Sym)lItem).getName());
            }else {
              System.out.print(" " + lItem.toString());
            }
          }
          System.out.print("]");
      } // printSet

//##70 END

//##74 BEGIN
public Set getReductionVarSet( List pReductionList )
{
  Set lVarSet = new HashSet();
  for (Iterator lIter = pReductionList.iterator();
       lIter.hasNext(); ) {
    Object lObj = lIter.next();
    if (lObj instanceof Reduction) {
      lVarSet.add(((Reduction)lObj).DefVarNode.getSym());
    }
  }
  return lVarSet;
} // getReductionVarSet
//##74 END
}
