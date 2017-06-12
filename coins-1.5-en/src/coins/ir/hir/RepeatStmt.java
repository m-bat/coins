/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;


/**
<PRE>
 *  Repeat-while-statement (do-while, repeat-until) interface.
 *  RepeatStmt is created as a general loop where contents of
 *    LoopInitPart, ConditionalInitPart_, StartCondition_,
 *    LoopStepPart_, LoopEndPart_
 *  are null at first (but they may become not null
 *  by some optimizing transformation).
</PRE>
**/
public interface
RepeatStmt extends LoopStmt {

} // RepeatStmt interface
