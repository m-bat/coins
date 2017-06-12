/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.List;

import coins.ir.IR;


/**
 * This is the reverse of the DefUseCell interface.
 */
public interface UDChain {
    /**
     * <P>Indicates that the variable may not have been set LOCALLY, i.e., within the subprogram, before used. This may be an entry to what is returned by <code>getDefList()</code>. The name "UNINITIALIZED" may be misleading, since external variables may have already been initialized outside of the scope of this analysis (SubpFlow).</P>
     *
     * <P>Please note that this is not a full-featured "IR node", and call to most of the methods in <code>IR</code> interface will fail (<code>UnsupportedOperationException</code> thrown).</P>
     */
    final static IR UNINITIALIZED = DefUseCell.UNINITIALIZED;

    /**
     * <P>Indicates that the variable will not be used LOCALLY, i.e., within the subprogram. This may be returned by <code>getUseNode()</code>. The name "UNUSED" may be misleading, since external variables may be going to be used outside of the scope of this analysis (SubpFlow).</P>
     *
     * <P>Please note that this is not a full-featured "IR node", and call to most of the methods in <code>IR</code> interface will fail (<code>UnsupportedOperationException</code> thrown).</P>
     */
    final static IR UNUSED = new UseNode("Unused");

    /**
     * <P>Indicates the (imaginary?) node that initializes the formal parameter. This may be an entry for what is returned by <code>getDefList()</code>.</P>
     *
     * <P>Please note that this is not a full-featured "IR node", and call to most of the methods in <code>IR</code> interface will fail (<code>UnsupportedOperationException</code> thrown).</P>
     */
    final static IR PARAM = DefUseCell.PARAM;

    /**
     * <P>Indicates the (imaginary) node that modifies a variable outside of the <code>SubpFlow</code> under consideration as a result of an external call from the <code>SubpFlow</code>. This may be returned by <code>getDefNode()</code>.</P>
     *
     * <P>Please note that this is not a full-featured "IR node", and call to most of the methods in <code>IR</code> interface will fail (<code>UnsupportedOperationException</code> thrown).</P>
     */
//    final static IR EXT_DEF = DefUseCell.EXT_DEF;

    /**
     * <P>Indicates the (imaginary) node that uses a variable outside of the <code>SubpFlow</code> under consideration as a result of an external call from the <code>SubpFlow</code>.
     *
     * <P>Please note that this is not a full-featured "IR node", and call to most of the methods in <code>IR</code> interface will fail (<code>UnsupportedOperationException</code> thrown).</P>
     */
//    final static IR EXT_USE = new UseNode("ExtUse");

    /**
     * Returns the Use node for this UDChian.
     */
    public IR getUseNode();

    /**
     * Returns the list of Def nodes for this UDChain. Here, a Def node is a top subtree node for the statement/instruction that sets the value for the Sym associated with this UDChain.
     */
    public List getDefList();

    /**
     * Adds a specified node to this UDChain.
     */
    public void addDefNode(IR pDefNode);

    final static class UseNode extends DefUseCell.DefNode {
        private UseNode(String pKind) {
            super(pKind);
        }
    }
}
