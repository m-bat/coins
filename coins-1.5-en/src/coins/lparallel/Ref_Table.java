/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.LinkedList;

import coins.aflow.BBlock;
import coins.aflow.FlowAnalSymVector;
import coins.aflow.SubpFlow;
///////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop]  Ref_Table
//  Refered areas (variables) covered by the corresponding BBlock ?
//
///////////////////////////////////////////////////////////////////////////////
class Ref_Table {
    BBlock  BasicBlock;       // Basic block corresponding to this table.
    FlowAnalSymVector mod;    // Set of variables modified.
    FlowAnalSymVector use;    // Set of variables used.
    FlowAnalSymVector ddef;   // Set of variables definitely defined.
    FlowAnalSymVector euse;   // Set of variables used without defining.
    LinkedList modArrayList;  // Ref_Array List of mod-arrays.
    LinkedList useArrayList;  // Ref_Array List of use-arrays.
    LinkedList ddefArrayList; // Ref_Array List of ddef-arrays.
    LinkedList euseArrayList; // Ref_Array List of euse-arrays.
    /**
    *
    * Ref_Table:
    *
    **/
    Ref_Table(SubpFlow pSubpFlow,BBlock pBlock) {
        BasicBlock = pBlock;
        modArrayList = new LinkedList();
        useArrayList = new LinkedList();
        ddefArrayList = new LinkedList();
        euseArrayList = new LinkedList();
        mod=  pSubpFlow.flowAnalSymVector();
        use=  pSubpFlow.flowAnalSymVector();
        ddef= pSubpFlow.flowAnalSymVector();
        euse= pSubpFlow.flowAnalSymVector();
    }
}
