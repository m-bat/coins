/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import coins.ir.hir.HIR;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.VarNode;
import coins.aflow.SubpFlow;
import coins.sym.Var;
import coins.sym.SymTable;
////////////////////////////////////////////////////////////////////////////////////////////
//
//    [parallel loop]  (TmpVarRef) 
//
////////////////////////////////////////////////////////////////////////////////////////////
public class  TmpVarRef {
    public HIR  fOriginalVarNode;  // Original Var Node
    public Var  fTmpVar;           // Tmp Var
    private SubpFlow fSubpFlow;
    /**
    *
    * TmpVarRef:
    *  
    **/
    TmpVarRef(SubpFlow pSubpFlow,HIR pOriginalVarNode) {
        fOriginalVarNode  = pOriginalVarNode;
        fSubpFlow = pSubpFlow;

        makeTmpVar(pOriginalVarNode);

    }
    /**
    *
    * makeTmpVar:
    *
    *
    *
    **/
    private void makeTmpVar (HIR pNode ) {
        SubpDefinition subp    ;
        SymTable LocalSymTable ;
        VarNode vNode;
        LoopTable lTable;

        vNode =(VarNode) pNode;
        subp = fSubpFlow.getSubpDefinition();

        //
        // get Local SymbolTable 
        //
        LocalSymTable = subp.getSubpSym().getSymTable();

        //
        // generate Var (Symbol)
        //
        //  #tmp
        //
        fTmpVar = LocalSymTable.generateVar(vNode.getType(),vNode.getVar());
    }
    /**
    *
    * getTmpVar:
    *
    *
    *
    **/
    public Var getTmpVar() {
        return fTmpVar;
   } 
    /**
    *
    * get:getOriginalVarNode
    *
    *
    *
    **/
    public HIR getOriginalVarNode() {
        return (HIR)fOriginalVarNode;
   } 
}
