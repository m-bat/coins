/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import coins.ir.IR;
import coins.sym.FlowAnalSym;

/**
 * SetRefRepr interface
 *
 * Used in data flow analysis to represent a Stmt in HIR and
 * an instruction in LIR.
 * This is a wrapper interface that bridges the different data
 * structure and interfaces between HIR and LIR. Used in
 * conjuction with SetRefReprList and its associated
 * SetRefReprIterator, this class serves as a basic building
 * block for basic data flow analysis/optimization.
 *
 * @see SetRefReprImpl
 */
public interface SetRefRepr
{
  final static int SETS = 0;
  final static int HAS_CONTROL = 1;
  final static int IS_RETURN = 2;

  /**
   * Returns the IR node this SetRefRepr represents.
   * getIR() usually refers to the top subtree corresponding to this SetRefREpr,
   * but in case of IfStmt it refers to if-condition expression, and
   * in case of SwitchStmt, it refers to selection-expression.
   * The top subtree corresponding to this SetRefRepr can be get by
   * subpFlow.getLinkedSubtreeOfExpId(getCorrespondingExpId()). //##62
   * @return the node this SetRefRepr represents.
   */
  IR getIR();

  /**
   * Returns the List of nodes that are associated with a Sym  that
   * are used (read), in the depth first order
   * that appears under the subtree returned by getIR().
   * Nodes that correspond to Syms that are possibly used
   * are included.
   */
  List useNodeList();

  /**
   * Returns the Set of the FlowAnalSyms used in this SetRefRepr.
   * All symbols whose values can be possibly referred within this SetRefRepr
   * are included, but those that can be referred to through the external
   * calls (i.e. global symbols) within this SetRefRepr are not
   * considered.
   */
  java.util.Set useSyms();

  public Set getUseFlowAnalSyms(); //##60

//##70 BEGIN
  /**
   * Returns the List of FlowAnalSyms used in this SetRefRepr.
   * All symbols whose values can be possibly referred within this SetRefRepr
   * are included, but those that can be referred to through the external
   * calls (i.e. global symbols) within this SetRefRepr are not
   * considered.
   */
  public List useSymList();
  //##70 END

  /**
   * Returns the IR node which links to the definitely defined (set) symbol.
   * Returns null if sets() returns false.
   *
   * @return the IR node which links to the defined (set) symbol.
   */
  IR defNode(); //##11

  /**
   * Returns the symbol that is definitely defined in this SetRefRepr.
   * (Neither consider the possibility of global variable modification
   * by subprogram call nore consider modification by alias relations.)
   * Returns null if sets() returns false, or, for example, in HIR,
   * if this SetRefRepr corresponds to an assignment to an array element.
   */
  FlowAnalSym defSym();

  /**
   * Returns the set of symbols that are possibly modified in this
   * SetRefRepr. The possibility of global variable modification
   * by subprogram call and modification of address-taken variables
   * by assignment to pointed memory area are not considered
   * (use modSyms00 if they should be considered //##73).
   */
  Set modSyms();

  /**
   * Returns the set of symbols that are possibly defined in this <code>SetRefRepr</code>. Symbols that can be externally defined (i.e. via exteranl calls) are included as well.
   */
//  Set modSyms0();

  /**
   * Returns the set of symbols that correspond to the set of nodes that are within the LHS of this <code>SetRefRepr</code>. Returns null if <code>sets()</code> returns <code>false</code>. The return value is a superset of the return value of <code>getModSyms()</code>.
   */
  Set lhsSyms();

  /**
   * Returns the <code>FlowExpId</code> that correponds to the node returned by <code>getDefNode()</code>.
   */
  //##60 FlowExpId defFlowExpId();

  /**
   * Converts to a String object.
   *
   */
  String toString();

  /**
     * DFO iterator over the nodes under the subtree returned by <code>getIR</code>.
   */
  //##60 NodeIterator nodeIterator();

  NodeListIterator nodeListIterator();

  /**
   * DFO iterator over the nodes under the subtree returned by <code>getIR()</code>.
   *
     * @param pFromTop If <code>true</code>, iterates from the top of the subtree.
     * @param pFromLeft if <code>true</code>, iterates from the left of the subtree.
   */
  //##60 NodeListIterator nodeListIterator(boolean pFromTop, boolean pFromLeft);

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

  /**
   * Returns the set of symbols that are accessed by its name in the
   * subprogram containing this <code>SetRefRepr</code> and possibly
   * defined in this <code>SetRefRepr</code>.
   * Symbols that can be externally defined (i.e. via exteranl calls)
   *  are included as well
   * (differs from modSym in this point). //##25-1
   * This is used in FindPEKill, FindDEGen,  //##25-1
   * and findDefUseExhaustively(), findUseDefExhaustively(). //##73
   */
  Set modSyms00();

  public FlowAnalSym getDefSym(); //##25

  public BBlock getBBlock(); //##63
;
}
