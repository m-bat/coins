/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;

import coins.aflow.BBlock;
import coins.aflow.FlowResults;
import coins.aflow.SetRefRepr;
import coins.aflow.SetRefReprList;
import coins.aflow.SubpFlow;
import coins.aflow.UDChain;
import coins.aflow.UDList;
import coins.ir.IR;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList; //##70
import coins.ir.hir.VarNode;
import coins.sym.Type;
//////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop]  Invariant
//
//  Detect invariant expressions.
//////////////////////////////////////////////////////////////////////////
class  Invariant {
    private FlowResults fResults ;
    private LoopTable fLoopTable ;
    private HashSet   finvHash;    // Set of invariant expressions ?
    private SubpFlow  fSubpFlow;
    private LoopUtil  fUtil;
    /**
    *
    * Invariant:
    *
    * Find  loop Invariants .
    *
    * HIR Node(Invariant) --> finvHash
    *
    *
    *
    **/
    Invariant(LoopTable pTable ,SubpFlow pSubpFlow,FlowResults pResults,LoopUtil pUtil) {
        boolean change_flg;
        ListIterator  Ie;
        Iterator  Ie1;
        Exp RightExp ;
        Exp LeftExp  ;
        fResults = pResults;
        fSubpFlow = pSubpFlow;

        fUtil =  pUtil;
        fLoopTable= pTable;
        finvHash = new HashSet();
        change_flg = true;

        while (change_flg == true) {
            change_flg = false;
            for (Ie = fLoopTable.BBlockList.listIterator();Ie.hasNext();){
                BBlock CurrBlock = (BBlock)Ie.next();
                fUtil.Trace("inv="+CurrBlock.getBBlockNumber(),5);
                SetRefReprList llist =
                        (SetRefReprList)fResults.get(
                        "BBlockSetRefReprs" ,CurrBlock);
                fUtil.Trace("inv1",5);
                if (llist == null)
                    continue;
                for (Ie1 = llist.iterator(); Ie1.hasNext();) {
                fUtil.Trace("inv2",5);
                    SetRefRepr lSetRefRepr= (SetRefRepr)Ie1.next();
                    IR IRnode = lSetRefRepr.getIR();
                    if (IRnode == null)
                        break;
                    //
                    // LoopStartCondition : ExpStmt:
                    // ex)      i< 10
                    //
                    if (IRnode.getOperator() == HIR.OP_EXP_STMT)
                        IRnode = ((ExpStmt)IRnode).getExp();
                    if (IRnode == null)
                        break;
                    fUtil.Trace("InvariantEXP="+IRnode.toString(),5);
                    if ((IRnode.getOperator() != HIR.OP_ASSIGN) &&
                        (IRnode.getOperator() != HIR.OP_CMP_EQ) &&
                        (IRnode.getOperator() != HIR.OP_CMP_NE) &&
                        (IRnode.getOperator() != HIR.OP_CMP_GT) &&
                        (IRnode.getOperator() != HIR.OP_CMP_GE) &&
                        (IRnode.getOperator() != HIR.OP_CMP_LT) &&
                        (IRnode.getOperator() != HIR.OP_CMP_LE)) {
                        continue;
                    }
                    if (IsInvariant((HIR)IRnode) == true)
                        continue;
                    if (IRnode.getOperator() == HIR.OP_ASSIGN) {
                        //
                        //  expression: a = b +c
                        //  IRnode     = (=)
                        //  LeftExp    = (a)
                        //  RightExp   = (+)
                        //
                        RightExp = ((AssignStmt)IRnode).getRightSide();
                        LeftExp  = ((AssignStmt)IRnode).getLeftSide();
                        RightExp  = fUtil.SkipConv(RightExp);
                        LeftExp  = fUtil.SkipConv(LeftExp);
                    //##70 BEGIN
                    }else if (IRnode instanceof HirList) {
                      for (Iterator lListIt = ((HirList)IRnode).iterator();
                           lListIt.hasNext(); ) {
                        Object lObj = lListIt.next();
                        if (lObj instanceof Exp) {
                          if (mark_inv((Exp)lObj)) {
                            setInvariant((HIR)IRnode);
                          }
                        }
                      }
                      continue;
                    //##70 END
                    } else {
                        //
                        //  Condition :  i< 10
                        //  IRnode     = (<)
                        //  LeftExp    = null
                        //  RightExp   = (<)
                        //
                        RightExp =(Exp)IRnode;
                        RightExp  = fUtil.SkipConv(RightExp);
                        LeftExp  = null;
                    }
                    if (mark_inv((HIR)RightExp) == true) {
                        setInvariant((HIR)IRnode);
                        if (LeftExp != null) {
                            if (LeftExp.getOperator() == HIR.OP_VAR &&
                                fUtil.def_check(fLoopTable,(AssignStmt)IRnode)  == true) {
                                change_flg = true;
                                setInvariant((HIR)LeftExp);
                            }
                        }
                    }
                    if (LeftExp != null) {
                        if (LeftExp.getOperator() == HIR.OP_SUBS) {
                            //
                            //  expression: a[x+y][l+m] = z
                            //  LeftExp    = OP_SUBS(Array)
                            //
                            //  Find Invariants :(x+y) ,(l+m)
                            boolean inv_flg= mark_inv((HIR)LeftExp);
                        }
                    }
                }
            }
        }
    }
    /**
    *
    * IsInvariant:
    *
    *
    **/
    boolean IsInvariant(HIR node) {
        fUtil.Trace("IsInvariant:node="+node.toString(),5);
        if (finvHash.contains(node) == true)
            return true;
        else
            return false;
    }
    /**
    *
    * setInvariant:
    *
    *
    **/
    private void setInvariant(HIR node) {
        fUtil.Trace("Invariant:node="+node.toString(),5);
        finvHash.add(node);
    }
    /**
    *
    * mark_inv:
    *
    *
    **/
    private boolean  mark_inv(HIR pNode) {
        boolean inv_flg ;
        int ChildCount ;
        int op;
        HIR lNode;
        fUtil.Trace("mark_inv "+pNode.toStringShort(),5); //##70
        //##70 BEGIN
        if (pNode instanceof HirList) {
          boolean lFlag = false;
          for (Iterator lListIt = ((HirList)pNode).iterator();
               lListIt.hasNext(); ) {
            Object lObj = lListIt.next();
            if (lObj instanceof Exp) {
              if (mark_inv((Exp)lObj)) {
                lFlag = true;
                setInvariant((HIR)pNode);
              }
            }
          }
          return lFlag;
        }
        //##70 END

        lNode = fUtil.SkipConv((Exp)pNode);
        ChildCount = lNode.getChildCount();
        op = lNode.getOperator();
        inv_flg = false;

        if (op == HIR.OP_SUBP)
            return inv_flg;  // Functions

        if (ChildCount != 0) {
            inv_flg = true;
            for (int i = 1;i<=ChildCount;i++) {
                //
                // all child nodes.
                //
                if (mark_inv((HIR)lNode.getChild(i)) == false)
                    inv_flg = false;
            }
            if (op == HIR.OP_SUBS) {
                inv_flg = false; // Array : childNode = a[i+j]
            }
        } else {
            switch(op) {     // leaf node ex) (a), (3) ....
            case HIR.OP_CONST:
                inv_flg = true;
                break;
            case HIR.OP_VAR:
                UDChain  invUDChain;
                UDList  invUDList;
                Iterator  Iu;
                boolean loop_flg=true;
                Type symType = (((VarNode)lNode).getVar()).getSymType();
                int TypeKind = symType.getTypeKind();
                if (TypeKind == Type.KIND_VECTOR) {
                    inv_flg = false; // Array : a[i] (node=a)
                    break;
                }
                //System.out.println("---------Debug Node="+lNode.toString());
                fUtil.Trace("Invariant:UDList node="+lNode.toString(),5);
                invUDList = (UDList)fResults.get("UDList",((VarNode)lNode).getVar(),fSubpFlow);

                invUDChain = invUDList.getUDChain(lNode);
                for (Iu = invUDChain.getDefList().iterator();Iu.hasNext();){
                    Object Node0 = Iu.next(); //##70 060823
                    if (Node0 == UDChain.UNINITIALIZED)
                        continue;
                    if (Node0 instanceof HIR) { //##70 060823
                      HIR Node = (HIR)Node0; //##70 060823
                      if (Node.getOperator() != HIR.OP_ASSIGN)
                        continue; // Funcions ?
                      HIR ChildNode = (HIR)((AssignStmt)Node).getLeftSide();
                      ChildNode = (HIR)fUtil.SkipConv((Exp)ChildNode);

                      if (fUtil.loop_body(fLoopTable, (HIR)ChildNode) == true) {
                        if (IsInvariant((HIR)ChildNode) == false) {
                          loop_flg = false;
                          break;
                        }
                      }
                     //##70 060823 BEGIN
                    }else {
                      fUtil.Trace("mark_inv non-HIR node " + Node0 +
                        " " + Node0.getClass(), 5);
                      continue;
                      //##70 060823 END
                    }
                }
                inv_flg = loop_flg;
                break;
            default:
                inv_flg = false;
                break;
            }
        }
        if (inv_flg == true) {
            setInvariant((HIR)pNode);
        }
        return inv_flg;
    }
}
