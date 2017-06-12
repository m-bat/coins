/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import coins.aflow.BBlock;
import coins.aflow.BBlockVector;
import coins.aflow.Flow;
import coins.aflow.FlowResults;
import coins.aflow.SetRefRepr;
import coins.aflow.SetRefReprList;
import coins.aflow.SubpFlow;
import coins.aflow.FlowUtil;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.IR;
import coins.ir.hir.VarNode;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.FlowAnalSym;
import coins.sym.Sym;
///////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop]  Loop Util
//
//  Utilities used in loop analysis ?
///////////////////////////////////////////////////////////////////////////////
public //##70
class  LoopUtil {
    //##70 private FlowResults fResults ;
    public final FlowResults fResults; //##70
    //##70 private SubpFlow fSubpFlow ;
    public final SubpFlow fSubpFlow; //##70
    private Flow fFlow ;
    private LinkedList DefVarList ;
    private TmpVarManager fTmpVarManager;
    public boolean TraceFlag ;
    public String TraceString;
    /**
    *
    * LoopUtil:
    *
    *
    **/
    public //##70
    LoopUtil(FlowResults pResults,SubpFlow pSubpFlow) {
        fResults = pResults;
        fSubpFlow = pSubpFlow;
        fFlow = pResults.flowRoot.aflow;
        DefVarList = new LinkedList();
        fTmpVarManager=new TmpVarManager(pSubpFlow, this);

        TraceFlag = true;
        TraceString = "";
    }
    /**
    *
    * dom_check:
    *
    * Dominate : true
    *
    **/
    boolean  dom_check(BBlock pBBlock,BBlock pExitBlock) {
        List dom ;
        dom = (List) pExitBlock.getDomForSubpFlow();

        if (dom.contains(pBBlock)== true) {
            return true;
        }
        return false;
    }
    /**
    *
    * loop_body:
    *
    *
    **/
    boolean  loop_body(LoopTable lTable,HIR node) {
        BBlock basicBlock;
        basicBlock = (BBlock)fResults.get("BBlockForNode",node);

        Trace("loop_body BBlock=" +basicBlock.getBBlockNumber(),5);

        return lTable.BBlockList.contains(basicBlock);

    }
    /**
    *
    * inner_loop_body:
    *
    *
    **/
    boolean  inner_loop_body(LoopTable lTable,HIR node) {
        BBlock basicBlock;
        basicBlock = (BBlock)fResults.get("BBlockForNode",node);

        Trace("loop_body BBlock=" +basicBlock.getBBlockNumber(),5);

        return lTable.InnerBBlockList.contains(basicBlock);

    }
    /**
    *
    * TraceBBlockList:
    *
    *
    **/
    void TraceBBlockList(LoopTable lTable) {

        Trace("-----TraceBBlockList-----",5);

        for (Iterator Ie = lTable.BBlockList.iterator();Ie.hasNext();) {
            BBlock basicBlock=(BBlock)Ie.next();
            Trace(" BBlock=" +basicBlock.getBBlockNumber(),5);
            Trace(" BBlock=" +basicBlock.getLabel().toString(),5);
            for (Iterator Ie1 =basicBlock.getSuccList().iterator();Ie1.hasNext();) {
                BBlock Succ=(BBlock)Ie1.next();
                Trace(" SuccBBlock=" +Succ.getBBlockNumber(),5);
                Trace(" SuccBBlock=" +Succ.getLabel().toString(),5);
            }
        }

        Trace("-----TraceInnerBBlockList-----",5);

        for (Iterator Ie = lTable.InnerBBlockList.iterator();Ie.hasNext();) {
            BBlock basicBlock = (BBlock)Ie.next();
            Trace(" BBlock=" +basicBlock.getBBlockNumber(),5);
        }

    }
    /**
    *
    * getLoopTable:
    *
    *
    **/
    LoopTable getLoopTable(LoopTable pTable,BBlock pBlock) {

        if(pTable.InnerBBlockList.contains(pBlock) == true)
                return pTable;
        for (Iterator Ie = pTable.InnerLoopList.iterator();Ie.hasNext();) {
            LoopTable vTable = getLoopTable((LoopTable)Ie.next(),pBlock);
            if (vTable != null)
                return vTable;
        }
        return null;
    }
    /**
    *
    * IsInduction:
    *
    *
    **/
    boolean  IsInduction(LoopTable lTable,VarNode node) {
        for (Iterator Ie = lTable.IndList.iterator();Ie.hasNext();) {
            BasicInduction IndTable = (BasicInduction)Ie.next();
            if (EQVar((Exp)node ,IndTable.DefVarNode) == true)
                return true;
        }
        return false;
    }
    /**
    *
    * IsPrivate:
    *
    *
    **/
    boolean  IsPrivate(LoopTable pTable,Var var) {

        for (Iterator Ie =pTable.Private.iterator();Ie.hasNext();) {
            VarNode vNode =(VarNode)Ie.next();
            Trace("IsPrivate="+vNode.getVar().toString(),5);
            if (var == vNode.getVar() )
                return true;
        }
        return false;
    }
    /**
    *
    * IsLastPrivate:
    *
    *
    **/
    boolean  IsLastPrivate(LoopTable pTable,Var var) {

        for (Iterator Ie =pTable.LastPrivate.iterator();Ie.hasNext();) {
            VarNode vNode =(VarNode)Ie.next();
            Trace("IsPrivate="+vNode.getVar().toString(),5);
            if (var == vNode.getVar() )
                return true;
        }
        return false;
    }
    /**
    *
    * IsReduction:
    *
    *
    **/
    boolean  IsReduction(LoopTable lTable,Var var) {
        Reduction RedTable;

        for (Iterator Ie =lTable.ReductionADDList.iterator();Ie.hasNext();) {
            RedTable = (Reduction)Ie.next();
            if (var == RedTable.DefVarNode.getVar() )
                return true;
        }
        for (Iterator Ie =lTable.ReductionMULList.iterator();Ie.hasNext();) {
            RedTable = (Reduction)Ie.next();
            if (var == RedTable.DefVarNode.getVar() )
                return true;
        }
        for (Iterator Ie =lTable.ReductionSUBList.iterator();Ie.hasNext();) {
            RedTable = (Reduction)Ie.next();
            if (var == RedTable.DefVarNode.getVar() )
                return true;
        }
        return false;
    }
    /**
    *
    * NodeOrder:
    *
    *
    */
    int NodeOrder(Exp Node1,Exp Node2) {

        if (Node1 == Node2)
        return  0;

        if (dom_check((BBlock)fResults.get("BBlockForNode",Node1),
                    (BBlock)fResults.get("BBlockForNode",Node2)) == false)
            return -1;

        if (fResults.get("BBlockForNode",Node1)
            != fResults.get("BBlockForNode",Node2))
            return 1;

        SetRefReprList llist =
        (SetRefReprList)fResults.get("BBlockSetRefReprs" ,fResults.get("BBlockForNode", Node1));

        for (Iterator Ie = llist.iterator(); Ie.hasNext();) {
            SetRefRepr lSetRefRepr=(SetRefRepr)Ie.next();
            List StmSet = lSetRefRepr.useNodeList();
            if (StmSet.contains(Node1) == true) {
                return (1);
            }
            if (StmSet.contains(Node2) == true) {
                return (-1);
            }
        }
        return 1;
    }
    /**
    *
    * SkipConv:
    *
    *     ex)
    *     expression :p = (int) a;
    *     exp        := (int) [node]
    *     return     ; a [node] ;
    *
    */
    Exp  SkipConv(Exp exp ) {
        Exp Curr = exp;
        Type CurrType;
        while(Curr.getOperator() == HIR.OP_CONV) {
            //if (Curr.getType().getTypeKind() !=
            //   Curr.getExp1().getType().getTypeKind())
            //    break;
            Curr = Curr.getExp1();

        }
        return SkipCommonTag(Curr);
    }
    /**
    *
    * IsAutoVarNode:
    *
    * AutoVarNode   true
    * else          false
    *
    */
    boolean IsAutoVarNode(HIR pNode) {

          boolean ret;
          HIR Curr = getTopVarNode(pNode);

          while(Curr.getChildCount() !=0) {
            Curr = (HIR)Curr.getChild1();
          }

          Var vSym = ((VarNode)Curr).getVar();
          int sClass =vSym.getStorageClass();

          if (sClass == Var.VAR_AUTO || sClass == Var.VAR_REGISTER) {
              ret = true;
          } else {
              ret = false;
          }
          return ret;
    }
    /**
    *
    * getParentASSIGN:
    *
    *
    */
    HIR getParentASSIGN(HIR pNode) {
        HIR Curr = pNode;
        Type CurrType;
        while(Curr.getOperator() != HIR.OP_ASSIGN) {
            Curr = (HIR)Curr.getParent();
        }
        return (Curr);
    }
    /**
    *
    * SkipCommonTag:
    *
    *
    */
    Exp  SkipCommonTag(Exp exp ) {
        Exp Curr = exp;
        Type CurrType;
        if(Curr.getOperator() == HIR.OP_QUAL) {
            if(Curr.getChild1().getOperator() == HIR.OP_SUBS)
                Curr = (Exp)FlowUtil.getQualVarNode((Exp)Curr.getChild1());
            else
                Curr = (Exp)FlowUtil.getQualVarNode(Curr);
        }
        return Curr;
    }
    /**
    *
    * getTmpVarManager:
    *
    *
    */
    TmpVarManager getTmpVarManager() {
        return (fTmpVarManager);
    }
    /**
    *
    * getTopVarNode:
    *
    *
    */
    HIR getTopVarNode(HIR pNode) {
        HIR Curr =pNode;
        while(Curr.getParent().getOperator() == HIR.OP_QUAL) {
            Curr = (HIR)Curr.getParent();
        }
        return (Curr);
    }
    /**
    *
    * IsVarNode:
    *
    *
    */
    boolean IsVarNode(HIR pNode) {
        Sym lSym;
        lSym = pNode.getSym();
        if (lSym == null)
            return false;
        if (lSym instanceof FlowAnalSym)
            return true;
        return false;
    }
    /**
    *
    * EQVar:
    *
    *
    */
    boolean EQVar(Exp exp ,VarNode var) {
        if (exp == null) return false;
        if (IsVarNode((HIR)exp) == false) return false;
        if (exp.getVar()  != var.getVar()) return false;
        return true;
    }
    /**
    *
    * getLoopBBlockList:
    *     LOOP(head,tail) = { blist(Bn, ....Bm)}
    *
    *
    *
    */
     public void getLoopBBlockList(LinkedList blist,BBlock head,BBlock tail,BBlock body) {
        int i,maxBBlockNo;

        BBlockVector markBit;
        BBlock end;

Trace("---pass:getLoopBBlockList",1);

/* //##70
Trace("head="+head.getBBlockNumber(),5);
Trace("tail="+tail.getBBlockNumber(),5);
Trace("body="+body.getBBlockNumber(),5);
*/ //##70
        Trace("head="+head,5); //##70
        Trace("tail="+tail,5); //##70
        Trace("body="+body,5); //##70

        markBit= fFlow.bblockVector(fSubpFlow);

        BBlockListSearch(markBit,head,head,tail,body);
        maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
        for (i = 1;i<=maxBBlockNo;i++) {
            if ( markBit.getBit(i) == 1) {
                Trace("add BBlock ="+fSubpFlow.getBBlock(i).getBBlockNumber(),5);
                blist.add(fSubpFlow.getBBlock(i));
            }
        }
    }
    public void getLoopBBlockListXXXXX(LinkedList blist,BBlock head,BBlock tail,BBlock body) {
        int i,maxBBlockNo;

        BBlockVector markBit;
        BBlock end;

Trace("---pass:getLoopBBlockList",1);

Trace("head="+head.getBBlockNumber(),5);
Trace("tail="+tail.getBBlockNumber(),5);

        markBit= fFlow.bblockVector(fSubpFlow);

        boolean b= BBlockListSearchXXX(markBit,head,head,tail,blist);
        blist.add(head);
        blist.add(tail);
       /*
        maxBBlockNo = fSubpFlow.getNumberOfBBlocks();
        for (i = 1;i<=maxBBlockNo;i++) {
            if ( markBit.getBit(i) == 1) {
                blist.add(fSubpFlow.getBBlock(i));
            }
        }
       */
    }
    /**
    *
    * getLoopInnerBBlockList:
    *
    *
    *
    */
    void getLoopInnerBBlockList(LoopTable pLoopTable) {

        LoopTable lTable;

        for (Iterator Ie = pLoopTable.BBlockList.iterator();Ie.hasNext();)
            pLoopTable.InnerBBlockList.add(Ie.next());

        for (Iterator Ie1 = pLoopTable.InnerLoopList.iterator();Ie1.hasNext();) {
            lTable = (LoopTable)Ie1.next(); // Child Loop
            for (Iterator Ie2=lTable.BBlockList.iterator();Ie2.hasNext();) {
                    BBlock b = (BBlock)Ie2.next();
                    Trace("removeBBlock="+b.getBBlockNumber(),5);
                    pLoopTable.InnerBBlockList.remove(b);
            }
        }
    }
    /**
    *
    * BBlockListSearch:
    *
    *
    */
    void BBlockListSearch(BBlockVector markBit, BBlock curr,BBlock head,BBlock tail,BBlock body) {


Trace("BBlockListSearch---------------",5);
Trace("curr="+curr,5); //##70
Trace("head="+head,5); //##70
Trace("tail="+tail,5); //##70
Trace("body="+body,5); //##70
Trace("-------------------------------",5);
        if (markBit.getBit(curr.getBBlockNumber()) == 1)
            return ;

        markBit.setBit(curr.getBBlockNumber());


        List SuccList = curr.getSuccList();

        for (ListIterator Ie =SuccList.listIterator();Ie.hasNext();)  {
            BBlock Succ = (BBlock)Ie.next();
Trace("succ="+Succ.getBBlockNumber(),5);
            if (curr == head) {
                if (Succ != body)
                    continue;
            }
            BBlockListSearch(markBit,Succ,head,tail,body);
        }
    }
    boolean BBlockListSearchXXX(BBlockVector markBit, BBlock curr,BBlock head,BBlock tail,
                          LinkedList addList) {

        if (markBit.getBit(curr.getBBlockNumber()) == 1) {
            if (addList.contains(curr) == true)
                return true;
             else
                return false;
        }
        markBit.setBit(curr.getBBlockNumber());

        List SuccList = curr.getSuccList();
        if (SuccList.size() ==0)
            return false;
        for (ListIterator Ie =SuccList.listIterator();Ie.hasNext();)  {
            BBlock Succ = (BBlock)Ie.next();
            if (addList.contains(Succ) == true)
                continue;
            if (Succ == tail) {
                return true;
            }
            if (Succ == head) {
                return false;
            }
            if (BBlockListSearchXXX(markBit,Succ,head,tail,addList) == true)
                addList.add(Succ);
        }
        return true;
    }
    /**
    *
    * IsScalar:
    *
    *
    **/
    boolean IsScalar(HIR pNode) {
       int op ;
       boolean b;
       b =true;
       op = pNode.getOperator();
       if (op == HIR.OP_SUBS)
           b = false;
        return b;

    }
    /**
    *
    * def_check:
    *
    *
    **/
    boolean  def_check(LoopTable lTable,AssignStmt AssignNode ) {
        int defCount;
        VarNode Var;

        VarNode LeftVar = (VarNode)SkipConv(AssignNode.getLeftSide());
        defCount =0;
        Trace("def_check LeftVar =" +LeftVar.toString(),5);
        for (Iterator Ie = lTable.DefVarList.iterator(); Ie.hasNext(); ) {
            Var = (VarNode)Ie.next();
            Trace("def_check tVar =" +Var.toString(),5);
            if (EQVar((Exp)Var,LeftVar) == true)
                defCount ++;
        }
        Trace("def_check count =" +defCount,5);
        if (defCount == 1)
            return true; // Only One
        else
            return false;
    }
    /**
    *
    * LoopNextBBlock:
    *
    */
    public BBlock LoopNextBBlock(LoopTable lTable)
    {
        BBlock LoopBack;
        BBlock Exit;
        LoopBack=(BBlock)fResults.getBBlockForLabel(
                                        lTable.LoopStmt.getLoopBackLabel());
        Exit = (BBlock)fResults.getBBlockForLabel(
                    lTable.LoopStmt.getLoopStepLabel());
            List SuccList = LoopBack.getSuccList();
        Trace("Loop NextLoopBack=" +LoopBack.getBBlockNumber(),5);
        Trace("Loop NextExit=" +Exit.getBBlockNumber(),5);

        for (ListIterator Ie = SuccList.listIterator();Ie.hasNext();)  {
            BBlock Succ = (BBlock)Ie.next();
            if (lTable.BBlockList.contains(Succ) == false)
                return Succ;
        }
        return (BBlock)null;
    }
    /**
    *
    * Trace:
    *
    *  print basic parallelizer information.
    *
    *  Driver Option :  [-coins:trace=Para1.n]
    *
    **/
    void Trace(String s,int n) {
        // System.out.println("Trace="+s);
        if (TraceFlag == true) {
            if (s.indexOf("pass") != -1)
                fResults.flowRoot.hirRoot.ioRoot.dbgPara1.print(n,
                    "//"     + TraceString+ "    " + s+ "\n");
            else
                fResults.flowRoot.hirRoot.ioRoot.dbgPara1.print(n,
                    "//    " + TraceString+ "    " + s+ "\n");
        }
    }
}
