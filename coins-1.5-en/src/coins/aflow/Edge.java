/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.sym.FlagBox;


/** Edge interface
 *  Interface for edge connecting basic blocks in the control flow graph.
**/
public interface Edge {
    //==== Flag numbers ====//
    public static final int // Used in flagBox method.
     LOOP_BACK_EDGE = 11;

    //==== Flag numbers ====//
    public static final int // Used in flagBox method.
     LOOP_EXIT_EDGE = 12;

    public BBlock getFromBBlock();

    public BBlock getToBBlock();

    /** flagBox:
     *  Record flags for an edge.
     *  Usage example:
     *    if (edgeName.flagBox().getFlag(Edge.LOOP_BACK_EDGE)) ...;
     *    edgeName.flagBox().setFlag(Edge.LOOP_BACK_EDGE, true);
    **/
    public FlagBox flagBox();

    //---- Following methods are used when changing CFG. ----//
    public void setFromBBlock(BBlock pBBlock);

    public void setToBBlock(BBlock pBBlock);
}
 // Edge interface
