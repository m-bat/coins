/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;


/**
<PRE>
 *  For-statement interface.
 *  ForStmt is created as a general loop where contents of
 *     ConditionalInitPart_, EndCondition_, LoopEndPart_
 *  are null at first (but they may become not null
 *  by some optimizing transformation).
</PRE>
**/
public interface
ForStmt extends LoopStmt {

} // ForStmt interface
