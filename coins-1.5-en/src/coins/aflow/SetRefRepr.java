/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import coins.ir.IR;
import coins.sym.FlowAnalSym;


/**
 * <p>SetRefRepr interface</p>
 *
 * <p>Used in data flow analysis to represent a Stmt in HIR and an instruction in LIR.</p>
 * <p>This is a wrapper interface that bridges the different data structure and interfaces between HIR and LIR. Used in conjuction with
 * <code>SetRefReprList</code> and its associated <code>SetRefReprIterator</code>, this class serves as a basic building block for basic
 * data flow analysis/optimization. </p>
 *
 * @see SetRefReprImpl
 */
public interface SetRefRepr {
    final static int SETS = 0;
    final static int HAS_CONTROL = 1;
    final static int IS_RETURN = 2;

    /**
     * Returns the (top) node this SetRefRepr represents.
     *
     * @return the node this SetRefRepr represents.
     */
    IR getIR();

    /**
     * Returns the <ocde>List</code> of nodes that are associated with a <code>Sym</code> that are used (read), in the depth first order
     * that appears under the subtree returned by <code>getIR()</code>.  Nodes that correspond to <code>Sym</code>s that are possibly used
     * are included.
     */
    List useNodeList();

    /**
     * Returns the Set of the FlowAnalSyms used in this SetRefRepr. All symbols whose values can possibly referred within this SetRefRepr
     * are included, but those that can be referred to through the external calls (i.e. global symbols) within this SetRefRepr are not
     * considered.
     */
    java.util.Set useSyms();

    /**
     * Returns the IR node which links to the definitely defined (set) symbol. Returns null if <code>sets()</code> returns
     * <code>false</code>.
     *
     * @return the IR node which links to the defined (set) symbol.
     */
    IR defNode(); //##11

    /**
     * Returns the symbol that is definitely defined in this <code>SetRefRepr</code>. Returns null if <code>sets()</code> returns <code>false</code>, or, for example, in HIR, if this <code>SetRefRepr</code> corresponds to an assignment to an array element.
     */
    FlowAnalSym defSym();

    /**
     * Returns the set of symbols that are possibly modified in this
     *  <code>SetRefRepr</code>.
     * Symbols externally modified (i.e. via external calls) are not included.
     */
    Set modSyms();

    /**
     * Returns the set of symbols that are possibly defined in this <code>SetRefRepr</code>. Symbols that can be externally defined (i.e. via exteranl calls) are included as well.
     */
//    Set modSyms0();

    /**
     * Returns the set of symbols that correspond to the set of nodes that are within the LHS of this <code>SetRefRepr</code>. Returns null if <code>sets()</code> returns <code>false</code>. The return value is a superset of the return value of <code>getModSyms()</code>.
     */
    Set lhsSyms();

    /**
     * Returns the <code>FlowExpId</code> that correponds to the node returned by <code>getDefNode()</code>.
     */
    FlowExpId defFlowExpId();

    /**
     * Converts to a String object.
     *
     */
    String toString();

    /**
     * DFO iterator over the nodes under the subtree returned by <code>getIR</code>.
     */
    NodeIterator nodeIterator();

    NodeListIterator nodeListIterator();

    /**
     * DFO iterator over the nodes under the subtree returned by <code>getIR()</code>.
     *
     * @param pFromTop If <code>true</code>, iterates from the top of the subtree.
     * @param pFromLeft if <code>true</code>, iterates from the left of the subtree.
     */
    NodeListIterator nodeListIterator(boolean pFromTop, boolean pFromLeft);

    /**
     * If this is a value-setting node, returns the top node of the RHS. If not, returns the same node as <code>getIR()</code>.
     */
    IR topUseNode();

    /**
     * DFO iterator over the nodes under the subtree returned by <code>getIR()</code>, but the node returned by <code>getDefNode()</code> excluded.
     */
    Iterator useNodeIterator(); // NEEDED

    Iterator expIterator();

    /**
     * Returns a DFO iterator that iterates over nodes that have a FlowExpId attached, and, if "this" is value-setting, is not a Def node. // NEEDED
     */
    ListIterator expListIterator();

    /**
     * Returns an DFO iterator that iterates over nodes that have a FlowExpId attached, and, if "this" is value-setting, is not a Def node. // NEEDED
     *
     * @param pFromTop iterate from top?
     * @param pFromLeft iterate from left?
     */
    ListIterator expListIterator(boolean pFromTop, boolean pFromLeft);

    /**
     * Returns <code>true</code> if this <code>SetRefRepr</code> involves value-setting, other than via the external call.
     */
    boolean sets();

    //##71 boolean hasCall();
    boolean hasCallWithSideEffect(); //##71

  List callNodes();
  /**
     * Does this <code>SetRefRepr</code> is at the end of the
     * <code>BBlock</code> and has a role to determine the flow of control
     *  after this <code>SetRefRepr</code>?
     */
    boolean hasControl();

    boolean isReturn();

    boolean allFalse();

    boolean getFlag(int pFlag);

    void setFlag(int pFlag, boolean pYesNo);

    boolean writesToDefiniteAddress();

    DefVector getDKill();

    DefVector getPKill();

    DefVector getPReach();

    DefVector getDReach();

    FlowAnalSymVector getDDefined();

    FlowAnalSymVector getPDefined();

    FlowAnalSymVector getDExposed();

    FlowAnalSymVector getPExposed();

    FlowAnalSymVector getDUsed();

    FlowAnalSymVector getPUsed();

    FlowAnalSymVector getPLiveOut();

    ExpVector getPEKill();

    ExpVector getDAvailIn();

    FlowAnalSymVector getDDefIn();


    /**
     * Returns the set of symbols that are accessed by its name in the
     * subprogram containing this <code>SetRefRepr</code> and possibly
     * defined in this <code>SetRefRepr</code>.
     * Symbols that can be externally defined (i.e. via exteranl calls)
     *  are included as well
     * (differs from modSym in this point). //##25-1
     * This is used in FindPEKill, FindDEGen. //##25-1
     */
    Set modSyms00();

    BBlock getBBlock();

    public FlowAnalSym getDefSym(); //##25

    //	Set getModSymsp();
}
