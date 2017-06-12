/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.LinkedList;
import java.util.Iterator;
import coins.ir.hir.HIR;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.VarNode;
import coins.aflow.SubpFlow;
import coins.sym.Var;
import coins.sym.SymTable;
////////////////////////////////////////////////////////////////////////////////////////////
//
//    [parallel loop]  (TmpVarManager) 
//
////////////////////////////////////////////////////////////////////////////////////////////
public class  TmpVarManager {
    private SubpFlow fSubpFlow;
    private LoopUtil fUtil;
    private LinkedList fTmpVarList;
    /**
    *
    * TmpVarManager:
    *  
    **/
    TmpVarManager(SubpFlow pSubpFlow,LoopUtil pUtil) {
        fSubpFlow = pSubpFlow;
        fUtil = pUtil;
        fTmpVarList = new LinkedList();

    }
    /**
    *
    * makeTmpVar:
    *
    *
    *
    **/
    public Var makeTmpVar (HIR pNode ) {
        Var v ;
        v = ((VarNode)pNode).getVar();

        for (Iterator Ie = fTmpVarList.iterator();Ie.hasNext();) {
            TmpVarRef  vRef=(TmpVarRef)Ie.next();
            VarNode    keyVarNode  = (VarNode)vRef.getOriginalVarNode();
            if (v == keyVarNode.getVar()) {
                return (vRef.getTmpVar());
            }
        }
        TmpVarRef tRef = new  TmpVarRef(fSubpFlow,pNode);
        fTmpVarList.add(tRef);
        return (tRef.getTmpVar());
    }
}
