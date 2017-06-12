/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.List;

import coins.ir.IR;


/** UDList interface
 * List of UDChains that are associated with a particular FlowAnalSym
 */
public interface UDList {
    /** addUDChain:
     *  Add UDChain which will be created by pUseNode that
     *  uses some symbol.
     **/
    public UDChain addUDChain(IR pUseNode);

    /** getUDChain:
     *  Get UDChain having pUseNode as its use node.
     * If such a UDChain does not exist, an instance of UDChain will be created with pUseNode as its UseNode and the corresponding (empty) DefList.
     */
    public UDChain getUDChain(IR pUseNode);

    /**
     * Just like {@link #getUDChain(IR)}, but if there is no UDChain with the specified argument as the Use node, returns null.
     */
    UDChain getUDChainRaw(IR pUseNode);

    /**
     * Returns the list of UDChains this UDList object holds.
     */
    List getUDChains();
}
