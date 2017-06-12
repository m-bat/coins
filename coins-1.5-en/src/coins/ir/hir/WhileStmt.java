/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;


/**
<PRE>
 *  While-statement interface.
 *  WhileStmt is created as a general loop where contents of
 *    LoopInitPart_, ConditionalInitPart_, EndCondition_,
 *    LoopStepPart_, LoopEndPart_
 *  are null at first (but they may become not null
 *  by some optimizing transformation).
</PRE>
**/
public interface
WhileStmt extends LoopStmt {

} // WhileStmt interface
