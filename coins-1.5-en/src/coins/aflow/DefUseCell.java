/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.List;

import coins.ir.IR;


/** DefUseCell interface
 *
 *  Def-use list cell representaing
 *  a definition and list of its use points. This interface does not take aliases into consideration.
 **/
public interface DefUseCell {
    /**
     * <P>Indicates that the variable may not have been set LOCALLY, i.e., within the subprogram, before used. This may be returned by <code>getDefNode</code>. The name "UNINITIALIZED" may be misleading, since external variables may have already been initialized outside of the scope of this analysis (SubpFlow).</P>
     *
     * <P>Please note that this is not a full-featured "IR node", and call to most of the methods in <code>IR</code> interface will fail (<code>UnsupportedOperationException</code> thrown).</P>
     */
    final static IR UNINITIALIZED = new DefNode("Uninitialized");

    /**
     * <P>Indicates the (imaginary?) node that initializes the formal parameter. This may be returned by <code>getDefNode()</code>.</P>
     *
     * <P>Please note that this is not a full-featured "IR node", and call to most of the methods in <code>IR</code> interface will fail (<code>UnsupportedOperationException</code> thrown).</P>
     */
    final static IR PARAM = new DefNode("Param");

    /**
     * <P>Indicates the (imaginary) node that modifies a variable outside of the <code>SubpFlow</code> under consideration as a result of an external call from the <code>SubpFlow</code>. This may be returned by <code>getDefNode()</code>.</P>
     *
     * <P>Please note that this is not a full-featured "IR node", and call to most of the methods in <code>IR</code> interface will fail (<code>UnsupportedOperationException</code> thrown).</P>
     */
//    final static IR EXT_DEF = new DefNode("ExtDef");

    /**
     * <P>Indicates the (imaginary) node that uses a variable outside of the <code>SubpFlow</code> under consideration as a result of an external call from the <code>SubpFlow</code>.
     *
     * <P>Please note that this is not a full-featured "IR node", and call to most of the methods in <code>IR</code> interface will fail (<code>UnsupportedOperationException</code> thrown).</P>
     */
//    final static IR EXT_USE = UDChain.EXT_USE;

    /**
     * Returns the node (def node of this DefUseCell) that sets the value for the symbol this DefUseCell is associated with. The node returned is an HIR AssignStmt node or LIR SET node or formal parameter node (PARAM) or node that signifies the symbol is uninitialed (UNINITIALIZED).
     */
    public IR getDefNode();

    /**
     * Returns the list of nodes that may use the value set at the def node (returned by <code>getDefNode()</code>).
     */
    public List getUseList();

    /**
     * Adds <code>pUseNode</code> to the list of use nodes (returned by <code>getUseList()</code>).
     */
    public void addUseNode(IR pUseNode);

    static class DefNode extends IrAdapter //IrListImpl
     {
        String fKind;

        DefNode(String pKind) {
            fKind = pKind;
        }

        public int hashCode()
        {
            return fKind.hashCode();
        }
        
        public boolean equals(Object pObj) {
            if (pObj instanceof DefNode) {
                if (fKind.equals(((DefNode) pObj).fKind)) {
                    return true;
                }
            }

            return false;
        }

        public String toString() {
            return fKind;
        }
    }
}
 // DefUseCell interface
