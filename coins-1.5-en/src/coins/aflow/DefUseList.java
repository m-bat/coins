/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.List;

import coins.ir.IR;


/** DefUseList interface
 *  Def-use list representaing list of definitions of a symbol
 *  where each definition has list of its use points.
 *  A DefUseList will be associated with a FlowAnalSym.
 **/
public interface DefUseList {
    /** addDefUseCell:
     *  Add DefUseCell which will be created by pDefNode that
     *  defines some symbol.
     **/
    public DefUseCell addDefUseCell(IR pDefNode);

    /** getDefUseCell:
     *  Get DefUseCell having pDefNode as its define node.
     * If such a DefUseCell does not exist, an instance of DefUseCell will be created with pDefNode as its DefNode and the corresponding (empty) UseList.
     */
    public DefUseCell getDefUseCell(IR pDefNode);

    /**
     * Just like {@link #getDefUseCell(IR)}, but if there is no DefUseCell with the specified argument as the def node, returns null.
     */
    public DefUseCell getDefUseCellRaw(IR pDefNode);

    /**
     * Returns the list of DefUseCells this DefUseList object holds.
     */
    public List getDefUseCells();
}
 // DefUseList interface
