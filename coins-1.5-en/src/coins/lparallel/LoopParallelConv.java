/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;

import coins.HirRoot;
import coins.Registry;
import coins.aflow.DefUseCell;
import coins.aflow.DefUseList;
import coins.aflow.FlowResults;
import coins.aflow.SubpFlow;
import coins.aflow.FlowUtil;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.BlockStmtImpl;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpImpl;
import coins.ir.hir.ForStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.Stmt;
import coins.ir.hir.StmtImpl;
import coins.ir.hir.VarNode;
import coins.sym.Var;
import coins.sym.SymTable;
public class LoopParallelConv {
    private SubpFlow fSubpFlow;
    private FlowResults fresults;
    private HIR  hir;
    private LoopUtil fUtil;
    private HirRoot  HirRoot;
    /**
    *
    * LoopParallelConv
    * Convert HIR for parallelization according to the
    * information recorded in LoopTable which contains
    * statements to be added or to be replaced ?
    *
    **/
    public LoopParallelConv(SubpFlow pSubpFlow,FlowResults presults) {
        fSubpFlow = pSubpFlow;
        fresults = presults;
        hir =fresults.flowRoot.hirRoot.hir;
        fUtil = new LoopUtil(fresults,fSubpFlow);
        HirRoot = fresults.flowRoot.hirRoot;
    }
    /**
    *
    * showLoopAnalResult:
    *
    *
    **/
    public void showLoopAnalResult() {
        LinkedList LoopInfo;
        ListIterator  Ie;
        ListIterator  Ie1;
        Iterator  Ie2;
        LoopTable lTable;
        ArrayAreaAnalyzer  ArrayAnalysis ;
        fUtil.Trace("showLoopAnalResult",1); //##70
        ArrayAnalysis = new ArrayAreaAnalyzer(null,fUtil);
        ArrayAnalysis.setFlowResults(null);
        ArrayAnalysis.setLoopExitBBlock(null) ;
        LoopInfo = (LinkedList)fresults.get("LoopParallelList",fSubpFlow);
        for (Ie = LoopInfo.listIterator();Ie.hasNext();) {
            lTable = (LoopTable)Ie.next();
            fUtil.Trace("NestLevel=" + lTable.fNestLevel,4);
            if (lTable.getParaFlag(fUtil,"showLoopAnalResult") == true) {
                fUtil.Trace("LastPrivate",4);
                for (Ie2 =lTable.LastPrivate.iterator();Ie2.hasNext();) {
                    Exp VarNode = (Exp) Ie2.next();
                    fUtil.Trace(VarNode.toString(),4);
                }
                fUtil.Trace("Private",4);
                for (Ie2 =lTable.Private.iterator();Ie2.hasNext();) {
                    Exp VarNode = (Exp) Ie2.next();
                    fUtil.Trace(VarNode.toString(),4);
                }
            } else {
                fUtil.Trace("VAR:true-dependence",4);
                for (Ie1 = lTable.varResultList.listIterator();Ie1.hasNext();) {
                    Var var = (Var) Ie1.next();
                    fUtil.Trace(var.getName(),4);
                }
                ArrayAnalysis.printaryelmList("true-dependence",lTable.mod_euseResultList);
                ArrayAnalysis.printaryelmList("anti-dependence", lTable.use_modResultList);
                ArrayAnalysis.printaryelmList("output-dependence", lTable.mod_modResultList);
            }
        }
    }
    /**
    *
    * Option:
    *
    *
    **/
    /*
    public void Option(String lOpt) {
        fUtil.Trace("---pass:Optin",1);
        if (lOpt.equals("OuterLoop")) {
            DeleteInnerLoop();
        } else {
            DeleteOuterLoop();
        }
    }
    */
    /**
    *
    *  DeleteOuterLoop:
    *
    *
    **/
    private void  DeleteOuterLoop()
    {
        ListIterator  Ie;
        LinkedList LoopInfo;
        LoopTable lTable ;
        fUtil.Trace("DeleteOuterLoop" ,1); //##70
        LoopInfo = (LinkedList)fresults.get("LoopParallelList",fSubpFlow);

        for (Ie = LoopInfo.listIterator();Ie.hasNext();) {
            lTable = (LoopTable)Ie.next();
            if (lTable.InnerLoop == false)  {
                Ie.remove();
            }
        }
    }
    /**
    *
    *  DeleteInnerLoop:
    *
    *
    **/
    private void  DeleteInnerLoop()
    {
        ListIterator  Ie;
        LinkedList LoopInfo;
        LoopTable lTable ;
        fUtil.Trace("DeleteInnerLoop" ,1); //##70
        LoopInfo = (LinkedList)fresults.get("LoopParallelList",fSubpFlow);
        for (Ie = LoopInfo.listIterator();Ie.hasNext();) {
            lTable = (LoopTable)Ie.next();
            if (lTable.OuterLoop != null)  {
                Ie.remove();
            }
        }
    }
    /**
    *
    * AddConditionalInitPart:
    *
    *
    **/
    void AddConditionalInitPart() {
        LinkedList LoopInfo;
        LoopTable lTable;

        fUtil.Trace("---pass:AddConditionalInitPart",1);

        LoopInfo = (LinkedList)fresults.get("LoopParallelList",fSubpFlow);
        for (Iterator Ie = LoopInfo.listIterator();Ie.hasNext();) {
            lTable = (LoopTable)Ie.next();
            if (lTable.getParaFlag(fUtil,"AddConditionalInitPart") == false)
                continue;
            if  (lTable.addConditionPart.size() != 0 && lTable.zero_check == false) {
                  //
                  //
                  // InitPart   <-- add
                  // ConditionalInitPart
                  // ForLoop
                  //
                  //
                  Stmt InitStmt =lTable.LoopStmt.getLoopInitPart();
                 //##70 try {
                   //##70 addLoopPreStmt1(lTable,(Stmt)((StmtImpl)InitStmt).clone());
                   addLoopPreStmt1(lTable,(Stmt)((StmtImpl)InitStmt).copyWithOperands()); //##70
                 //##70 }catch (CloneNotSupportedException v)  {
                      //
                      // Error !!
                      //
                 //##70 }
            }
            for (Iterator Ie1 = lTable.addConditionPart.iterator();Ie1.hasNext();){
                Stmt st= (Stmt)Ie1.next();

                fUtil.Trace("addInitPart "+st.toString(),4);
                //##70 addLoopPreStmt(lTable,st);
                addLoopPreStmt(lTable,(Stmt)st.copyWithOperands()); //##70
            }
        }
    }
    /**
    *
    * DeleteInduction:
    *
    *
    **/
    public void DeleteInduction() {
        LinkedList LoopInfo;
        ListIterator  Ie;
        Iterator  Ie1;
        LoopTable lTable;

        LoopInfo = (LinkedList)fresults.get("LoopParallelList",fSubpFlow);
        fUtil.Trace("---pass:DeleteInduction",1);
        DeleteInductionLoopList(LoopInfo,true);
    }
    /**
    *
    *  DeleteInductionLoopList:
    *
    *
    **/
    void DeleteInductionLoopList(LinkedList LoopInfo,boolean Child) {

        ListIterator  Ie;
        Iterator  Ie1;
        LoopTable lTable;
        for (Ie = LoopInfo.listIterator();Ie.hasNext();) {
            lTable=(LoopTable)Ie.next();
            if (Child == true) {
                DeleteInductionLoopList(lTable.InnerLoopList,Child);
            }
            DeleteInductionLoop(lTable);
        } // End of For Loop

    }
    /**
    *
    * DeleteInductinLoop:
    *
    *
    **/
    void DeleteInductionLoop(LoopTable pTable) {
        ConstNode C1;
        Exp C2;
        DefUseList DUList ;
        DefUseCell DUChain ;
        LinkedList LoopInfo;
        boolean loopbody ;
        LoopTable lTable;
        LinkedList deleteStmt;
        Exp CommonLeft;
        Exp tmpRight ;
        deleteStmt = new LinkedList();
        fUtil.Trace("---pass:DeleteInductionLoop",1);

        lTable = pTable;

        if (lTable.getParaFlag(fUtil,"DeleteInductionLoop") == false)
            return;

        for (Iterator Ie1 =lTable.IndList.iterator();Ie1.hasNext();) {
            BasicInduction IndTable = (BasicInduction)Ie1.next();
            loopbody = true;
            //
            // Change Private
            //
            ChangePrivate(lTable,IndTable);
            if (IndTable.loop_ctr == true) {
                //
                //    [CREATE Normalize_loopIndex]
                //
                //   for (v=0;i<n;v=v+1)
                //
                if (lTable.addInit != null) {
                    Stmt InitStmt =lTable.LoopStmt.getLoopInitPart();
                    Stmt NextStmt= InitStmt;
                    while (NextStmt != null) {
                        if (NextStmt.getOperator() == HIR.OP_BLOCK) {
                            NextStmt = ((BlockStmt)NextStmt).getFirstStmt();
                        }
                        //##70 try {
                          //##70 addLoopPreStmt1(lTable,(Stmt)((StmtImpl)NextStmt).clone());
                          addLoopPreStmt1(lTable,(Stmt)((StmtImpl)InitStmt).copyWithOperands()); //##70
                        //##70 }catch (CloneNotSupportedException v)  {
                            //
                            // Error !!
                            //
                        //##70 }
                        NextStmt = NextStmt.getNextStmt();
                    }
                    //##70 lTable.LoopStmt.replaceSource1(lTable.addInit);
                    lTable.LoopStmt.replaceSource1(lTable.addInit.copyWithOperands()); //##70
                }
                if (lTable.addCond != null) {
                    //##70 lTable.LoopStmt.setLoopStartCondition(lTable.addCond);
                    lTable.LoopStmt.setLoopStartCondition((Exp)lTable.addCond.copyWithOperands()); //##70
                }
                if (lTable.addStep != null) {
                    fUtil.Trace("addStep:"+lTable.addStep,4);
                    //lTable.LoopStmt.addToLoopStepPart(lTable.addStep);
                    //##70 addToLoopStepPart(lTable,lTable.addStep);
                    addToLoopStepPart(lTable,(Stmt)lTable.addStep.copyWithOperands()); //##70
                }
            } else  {
                // [CREATE STMT]
                //
                // j=  (inc*tmp#1) + ini
                // LEFT = RIGHT
                //

                // j=  (inc*tmp#1) + ini
                // |
                // + --> TopDefNodeL
                //
                HIR TopDefNodeL =(HIR)fUtil.getTopVarNode((HIR)IndTable.DefVarNode);

                boolean PrivateExist = false;
                boolean LastPrivateExist = false;
                VarNode NewVarNode=null;
                Exp Left=null;
                HIR TopDefNode=null ;
                Exp RightDef=null;
                CommonLeft = null;
                tmpRight = null;

                if (TopDefNodeL.getOperator() != HIR.OP_VAR) {
                    //
                    // common or complex
                    // common DefVarNode --> tmpVarNode
                    Var  v = fUtil.getTmpVarManager().makeTmpVar(IndTable.DefVarNode);
                    NewVarNode =(VarNode)hir.varNode(v);
                    CommonLeft     = (Exp)TopDefNodeL.copyWithOperands();
                    tmpRight     = (Exp)NewVarNode.copyWithOperands();
                    fUtil.Trace("Common tmpVarNode:"+NewVarNode.toString(),4);
                    if (lTable.Private.contains(IndTable.DefVarNode) == true) {
                       PrivateExist = true;
                       fUtil.Trace("add Private(NewVarMode):"+NewVarNode.toString(),4);
                       lTable.Private.add(NewVarNode);
                       lTable.Private.remove(IndTable.DefVarNode);
                    }
                    if (lTable.LastPrivate.contains(IndTable.DefVarNode) == true) {
                       LastPrivateExist = true;
                      lTable.LastPrivate.add(NewVarNode);
                      lTable.LastPrivate.remove(IndTable.DefVarNode);
                       fUtil.Trace("add LastPrivate(NewVarNode):"+NewVarNode.toString(),4);
                    }
                }
                if (PrivateExist == true || LastPrivateExist == true) {
                   Left     = (Exp)NewVarNode.copyWithOperands();
                   RightDef = (Exp)NewVarNode.copyWithOperands();
                } else {
                   Left= (Exp)TopDefNodeL.copyWithOperands();
                   TopDefNode =(HIR)fUtil.getTopVarNode((HIR)lTable.LoopCtrInduction.DefVarNode);
                   RightDef = (Exp)TopDefNode.copyWithOperands();
                }
                C1 = (ConstNode)hir.intConstNode((int)IndTable.inc);
                Exp RightMULT = new ExpImpl(HirRoot,HIR.OP_MULT,C1,RightDef);

                if (IndTable.InitConstFlag == true) {
                    C2 = (Exp)hir.intConstNode((int)IndTable.InitConstValue);
                } else  {
                    //##70 C2 = (Exp)IndTable.InitConstNode;
                    C2 = (Exp)IndTable.InitConstNode.copyWithOperands(); //##70
                }

                Exp Right = new ExpImpl(HirRoot,HIR.OP_ADD,RightMULT,C2);

                // [REPLACE]
                //
                // j = j+1
                // a[j] =x Change ==> a[j+1]  = x
                //

                DUList = (DefUseList)fresults.get("DefUseList",
                IndTable.DefVarNode.getVar(),fSubpFlow); //## subpFlow ?
                if (fUtil.IsAutoVarNode(IndTable.DefVarNode) == false)
                    loopbody = false;
                fUtil.Trace(IndTable.DefVarNode.toString(),1);
                DUChain =
                    (DefUseCell)DUList.getDefUseCell(fUtil.getParentASSIGN(IndTable.DefVarNode));

                for (Iterator Iu = DUChain.getUseList().iterator();Iu.hasNext();){
                    IR Use = (IR)Iu.next();
                    if (!(Use instanceof VarNode))
                        continue;
                    VarNode UseNode = (VarNode)Use;
                    if (fUtil.loop_body(lTable,(HIR)UseNode) == false) {
                        loopbody = false;
                        continue;
                     }
                     if (fUtil.NodeOrder(IndTable.UseVarNode,UseNode) > 0) {
                        HIR TopUseNode =fUtil.getTopVarNode((HIR)UseNode);
                        HIR lParent = (HIR)TopUseNode.getParent();
                        TopUseNode = TopUseNode.copyWithOperands();
                        Exp NewNode = null;
                        if (PrivateExist == true || LastPrivateExist == true) {
                            NewNode = new ExpImpl(HirRoot,HIR.OP_ADD,
                                          (Exp)NewVarNode.copyWithOperands(),C1);
                        } else {
                            HIR TopUseNodes = TopUseNode.copyWithOperands();
                            NewNode = new ExpImpl(HirRoot,HIR.OP_ADD,(Exp)TopUseNodes,C1);
                            fUtil.Trace("2:"+NewNode.toString(),1);
                        }
                        for (int i = 1; i <= lParent.getChildCount(); i++) {
                            if (lParent.getChild(i) == (IR)TopUseNode) {
                                lParent.replaceSource(i, NewNode);
                                break;
                            }
                        }
                     } else {
                        if (PrivateExist == true || LastPrivateExist == true) {
                             HIR TopUseNode =fUtil.getTopVarNode((HIR)UseNode);
                             HIR lParent = (HIR)TopUseNode.getParent();
                             Exp NewNode = null;
                            NewNode = (Exp)NewVarNode.copyWithOperands();
                            fUtil.Trace("3:"+NewVarNode.toString(),1);
                            fUtil.Trace("3:"+NewNode.toString(),1);
                            for (int i = 1; i <= lParent.getChildCount(); i++) {
                                if (lParent.getChild(i) == (IR)TopUseNode) {
                                    fUtil.Trace("4:"+NewNode.toString(),1);
                                    lParent.replaceSource(i, NewNode);
                                    break;
                                }
                            }
                        }
                     }
                }
                // [ADD]
                //
                // j=  (inc*tmp#1) + ini
                //

                Stmt addStmt = hir.assignStmt( Left, Right);
                addLoopBodyStmt(lTable,addStmt);
                if (loopbody == false) { // Use Node
                    loopbody = true;
                    //##70 try {
                        // LastPrivate:
                        //
                        // [ADD]
                        // Loop
                        //     ...
                        // End Loop
                        // tmp=  (inc*tmp#1) + ini (addLoopNextStmt)
                        // common=  tmp <====      (addLoopNextStmt)
                        //
                        if (CommonLeft != null) {
                             addLoopNextStmt(lTable,hir.assignStmt( CommonLeft, tmpRight));
                        }
                        //##70 addLoopNextStmt(lTable,(Stmt)((StmtImpl)addStmt).clone());
                        addLoopNextStmt(lTable,(Stmt)((StmtImpl)addStmt).copyWithOperands()); //##70
                        //##72 lTable.LastPrivate.add(lTable.LoopCtrInduction.DefVarNode);
                        // As for LoopCtrInduction, is not it add to private ? //##72
                        // In this path, it should be last-private because
                        // in the following loop,
                        //   for (i=0; i<n ; i++) {
                        //     x[i]= k + i;
                        //     c[i]= i * i;
                        //     k=k-2;
                        //     sum = sum + x[i];
                        //   }
                        // after exiting from the loop, k is computed by a statement
                        //   k = (-2)*i + 100;
                        // and this requires i to be last-private. //##72
                        lTable.LastPrivate.add(lTable.LoopCtrInduction.DefVarNode); //##72
                    //##70 }
                    //##70 catch (CloneNotSupportedException v)  {
                        //
                        // Error !!
                        //
                    //##70 }
                 }

                 //
                 // [DELETE]
                 //
                 // j=  j +1
                 //
                 IndTable.stmt.deleteThisStmt();
                 //deleteStmt.add(IndTable.stmt);
             }
        }
        for (Iterator Ie =deleteStmt.listIterator();Ie.hasNext();) {
            Stmt st = (Stmt)Ie.next();
            st.deleteThisStmt();
        }
    }
    /**
    *
    * ChangeCommonPrivate:
    *
    *
    **/
    public void ChangeCommonPrivate() {
        LinkedList LoopInfo;
        ListIterator  Ie;
        Iterator  Ie1;
        LoopTable lTable;

        LoopInfo = (LinkedList)fresults.get("LoopParallelList",fSubpFlow);
        fUtil.Trace("---pass:ChaneCommonPrivate",1);
        ChangeCommonPrivateLoopList(LoopInfo,true);
    }
    /**
    *
    *  ChangeCommonPrivateLoopList:
    *
    *
    **/
    void ChangeCommonPrivateLoopList(LinkedList LoopInfo,boolean Child) {

        ListIterator  Ie;
        Iterator  Ie1;
        LoopTable lTable;
        for (Ie = LoopInfo.listIterator();Ie.hasNext();) {
            lTable=(LoopTable)Ie.next();
            if (Child == true) {
                ChangeCommonPrivateLoopList(lTable.InnerLoopList,Child);
            }
            ChangeCommonPrivateLoop(lTable);
        } // End of For Loop
    }
    /**
    *
    * ChangeCommonPrivateLoop:
    *
    *
    **/
    void ChangeCommonPrivateLoop(LoopTable pTable) {

        LoopTable lTable;
        HashSet addSet;
        HashSet deleteSet;
        VarNode vNode;

        fUtil.Trace("---pass:ChangeCommonPrivateLoop",1);

        addSet   = new  HashSet();
        deleteSet= new  HashSet();
        lTable = pTable;

        if (lTable.getParaFlag(fUtil,"ChangeCommonPrivateLoop") == false)
            return;
        for (Iterator Ie =lTable.Private.iterator();Ie.hasNext();) {
                HIR privateNode =(HIR)Ie.next();
                HIR privateNodeParent = (HIR) privateNode.getParent();
                if (privateNodeParent == null)
                    continue;
                if (FlowUtil.IsComplexNode(privateNode) == true)
                    continue;
                if(privateNodeParent.getOperator() == HIR.OP_QUAL) {
                    deleteSet.add(privateNode);
                    addSet.add(ChangeCommonPrivateNode(lTable,privateNode));
                }
        }
        lTable.Private.addAll(addSet);         // tmpNode
        lTable.Private.removeAll(deleteSet);   // common or complex
        addSet.clear();
        deleteSet.clear();
        for (Iterator Ie =lTable.LastPrivate.iterator();Ie.hasNext();) {
                HIR privateNode =(HIR)Ie.next();
                HIR parentNode =(HIR)privateNode.getParent();
                if (parentNode == null)
                    continue;
                if (FlowUtil.IsComplexNode(privateNode) == true)
                    continue;
                if( privateNode.getParent().getOperator() == HIR.OP_QUAL) {
                    deleteSet.add(privateNode);
                    //
                    // 2005.03.09
                    //  I.Fukuda
                    //addSet.add(ChangeCommonPrivateNode(lTable,privateNode));
                    //
                    VarNode  tmp =ChangeCommonPrivateNode(lTable,privateNode);
                    addSet.add(tmp);
                    HIR lNode =(HIR)fUtil.getTopVarNode((HIR)privateNode);
                    //##70 addLoopNextStmt(lTable,hir.assignStmt((Exp)lNode,(Exp)tmp)); //##?
                    addLoopNextStmt(lTable,hir.assignStmt((Exp)lNode.copyWithOperands(),(Exp)tmp.copyWithOperands())); //##70
                }
        }
         lTable.LastPrivate.addAll(addSet);         // tmpNode
         // As for tmpNode, is not it add to private ? //##72
         lTable.LastPrivate.removeAll(deleteSet);   // common or complex
    }
    /**
    *
    * ChangeCommonPrivateNode:
    *
    *   CommonNode => CommonName.v
    *
    *
    **/
    private VarNode ChangeCommonPrivateNode (LoopTable pLoopTable,HIR pCommonNode ) {
        SubpDefinition subp    ;
        SymTable LocalSymTable ;
        Var tmpVar             ;
        VarNode vNode;
        LoopTable lTable;
        fUtil.Trace("---pass:ChangeCommonPrivateNode",4);
        lTable = pLoopTable;
        vNode =(VarNode) pCommonNode;
        subp = fSubpFlow.getSubpDefinition();
        LocalSymTable = subp.getSubpSym().getSymTable();

        tmpVar = LocalSymTable.generateVar(vNode.getType(),vNode.getVar());

        fUtil.Trace("tmpVar="+tmpVar.toString(),4);
        fUtil.Trace("comm="+pCommonNode.toString(),4);
        for (Iterator Ie =lTable.StructNodeHash.iterator();Ie.hasNext();) {
            HIR sNode =(HIR)Ie.next();
            VarNode lNode = (VarNode)fUtil.SkipConv((Exp)sNode.getChild1());
            VarNode rNode = (VarNode)fUtil.SkipConv((Exp)sNode.getChild2());
            fUtil.Trace("l"+lNode.toString(),4);
            fUtil.Trace("r"+rNode.toString(),4);
            if ( (rNode.getVar() == vNode.getVar()) || (lNode.getVar() == vNode.getVar() )) {
                VarNode tmpNode = (VarNode)hir.varNode(tmpVar);
                fUtil.Trace("replace",4);
                sNode.replaceThisNode(tmpNode);
            }
        }
        return (VarNode)hir.varNode(tmpVar);
    }
    /**
    *
    *   addLoopBodyStmt:
    *
    *
    */
    private void addLoopBodyStmt (LoopTable lTable,Stmt addStmt) {
        Stmt addp;
        Stmt addp1;
        fUtil.Trace("addLoopBodyStmt "+addStmt.toStringWithChildren(),4); //##70
        Stmt BodyStmt = lTable.LoopStmt.getLoopBodyPart();
        addp  = ((LabeledStmt)BodyStmt).getStmt();
        addp1 = ((BlockStmt)addp).getFirstStmt();
        //##70 try {
           //##70 addp1.insertPreviousStmt((Stmt)((StmtImpl)addStmt).clone(), null);
           addp1.insertPreviousStmt((Stmt)((StmtImpl)addStmt).copyWithOperands()); //##70
        //##70 }
        //##70 catch (CloneNotSupportedException v)  {
                            //
                            // Error !!
                            //
        //##70 }
    }
    /**
    * addLoopPreStmt:
    * Add pStmt to be executed before executing the loop statement.
    *
    */
    private void addLoopPreStmt (LoopTable lTable,Stmt pStmt) {
        Stmt addStmt=null;
        //##70 try {
            //##70 addStmt = (Stmt)((StmtImpl)pStmt).clone();
            addStmt = (Stmt)((StmtImpl)pStmt).copyWithOperands();
       //##70 }
       //##70 catch (CloneNotSupportedException v)  {
                            //
                            // Error !!
                            //
       //##70 }
       fUtil.Trace("addLoopPreStmt "+pStmt.toStringWithChildren(),4); //##70
       /* //##72
       if (lTable.zero_check == false) {
           //
           // ex ) for(i=0;i<n;i++)
           //
           lTable.LoopStmt.addToConditionalInitPart(addStmt);
       } else {
           //
           // ex ) for(i=0;i<10;i++)
           //
           addLoopPreStmt1(lTable,pStmt);
       }
       */ //##72
       addLoopPreStmt1(lTable,pStmt); //##72
    }
    /**
    * addLoopPreStmt1:
    * Place pStmt in front of the loop indicated by lTable. //##72
    *
    */
    private void addLoopPreStmt1 (LoopTable lTable,Stmt pStmt) {
        Stmt addStmt=null;

       //##70 try {
            //##70 addStmt = (Stmt)((StmtImpl)pStmt).clone();
            addStmt = (Stmt)((StmtImpl)pStmt).copyWithOperands();
       //##70 }
      //##70  catch (CloneNotSupportedException v)  {
                            //
                            // Error !!
                            //
       //##70 }
       Stmt preStmt = lTable.LoopStmt.getPreviousStmt();
       fUtil.Trace("addLoopPreStmt1 "+pStmt.toStringWithChildren() + " preStmt " + preStmt,4); //##70
       if (preStmt == null) {
            Stmt parentStmt = (Stmt)((HIR)lTable.LoopStmt).getParent() ;
            BlockStmt block = (BlockStmt)new BlockStmtImpl(HirRoot,addStmt);
            if (parentStmt.getOperator() == HIR.OP_LABELED_STMT) {
                ((LabeledStmt)parentStmt).setStmt(block);
               lTable.LoopStmt =  (ForStmt)lTable.LoopStmt.copyWithOperands();
                //##70 addStmt.addNextStmt(lTable.LoopStmt,block);
                addStmt.addNextStmt(lTable.LoopStmt); //##70
            } else {
                //##70 lTable.LoopStmt.insertPreviousStmt(addStmt,null);
                lTable.LoopStmt.insertPreviousStmt(addStmt); //##70
            }
        } else {
            //##70 lTable.LoopStmt.insertPreviousStmt(addStmt,lTable.LoopStmt.getBlockStmt());
            lTable.LoopStmt.insertPreviousStmt(addStmt); //##70
        }
    }
    /**
    *
    *   ChangePrivate:
    *
    *
    */
    private void ChangePrivate (LoopTable lTable ,BasicInduction IndTable) {
        VarNode DefNode ;
        DefNode = IndTable.DefVarNode;
        fUtil.Trace("---pass:ChangePrivate",1);
        fUtil.Trace("DefNode="+DefNode.toString(),4);

        if (IndTable.loop_ctr == true) {
            for (Iterator Ie =lTable.Private.iterator();Ie.hasNext();) {
                HIR vNode =(HIR)Ie.next();
                fUtil.Trace("vNode="+vNode.toString(),4);
                if (((VarNode)vNode).getVar() == DefNode.getVar()) {
                    lTable.Private.remove(vNode);
                    break;
                }
            }
        }
        /*
        } else {
            for (Iterator Ie = lTable.LastPrivate.iterator();Ie.hasNext();) {
                HIR vNode =(HIR)Ie.next();
                fUtil.Trace("LastPrivate: vNode="+vNode.toString(),5);
                if (((VarNode)vNode).getVar() == DefNode.getVar()) {
                    lTable.LastPrivate.remove(vNode);
                    break;
                }
            }
        }
        */
    }
    /**
    *
    *   addLoopNextStmt:
    *
    *
    */
    private void addLoopNextStmt (LoopTable lTable,Stmt addStmt) {
        Stmt addp;
        Stmt addp1;
        Stmt NextStmt = lTable.LoopStmt.getNextStmt();
        if (NextStmt != null) {
          fUtil.Trace("addLoopNextStmt to "+NextStmt.toStringShort()
                      + " " + addStmt.toStringWithChildren(),4); //##70
            //##70 NextStmt.insertPreviousStmt( addStmt, null);
            NextStmt.insertPreviousStmt( (Stmt)addStmt.copyWithOperands()); //##70
        }
    }
    public void addToLoopStepPart(LoopTable lTable,Stmt pStmt ) {
        Stmt lStmt, lNewStmt;
        if (pStmt == null)
            return;
        pStmt.cutParentLink();
        fUtil.Trace("addLoopStepPart "+pStmt.toStringWithChildren(),4); //##70
        lStmt = lTable.LoopStmt.getLoopStepPart();
        if(lStmt == null) {
            //System.out.println("Debug Add"+pStmt.toString());
            //##70 lTable.LoopStmt.replaceSource(6, pStmt);
            lTable.LoopStmt.replaceSource(6, pStmt.copyWithOperands()); //##70
        } else {
            //##70 lNewStmt = lStmt.combineStmt(pStmt, false);
            lNewStmt = ((Stmt)lStmt.copyWithOperands()).
              combineStmt((Stmt)pStmt.copyWithOperands(), false); //##70
            if (lNewStmt != lStmt)
              //##70 lTable.LoopStmt.replaceSource(6, lStmt);
              lTable.LoopStmt.replaceSource(6, lNewStmt.copyWithOperands()); //##70
       }
    }
    private void Trace(String s) {
        //ioRoot.printOut.println(s);
        //System.out.println(s );
    }
}
