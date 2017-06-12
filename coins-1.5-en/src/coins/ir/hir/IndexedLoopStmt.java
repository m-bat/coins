/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.sym.Var;

/**
<PRE>
 *  IndexedLoop-statement interface.
 *  IndexedLoopStmt is created as a general loop where contents of
 *    ConditionalInitPart_, EndCondition_, LoopEndPart_
 *  are null at first (but they may become not null
 *  by some optimizing transformation).
 *  IndexedLoopStmt represents a Fortran type loop where
 *  value of loop index is incremented or decremented by loop
 *  step value starting from loop start value and stops
 *  to loop before crossing the barrier of loop end value.
 *
 *  The loop index is a simple variable.
 *  It may be integer variable (short int, int, long int, long long int)
 *  or floating variable (float, double).
 *  The values of loop start value, loop end value, and
 *  loop step value are fixed at entry to the loop
 *  and do not change until terminating the loop.
 *  If upward parameter is true, then the loop index is incremented
 *  up to but not exceeding the loop end value.
 *  If upward parameter is false, then the loop index is decremented
 *  down to but not crossing the loop end value.
 *  The loop start value, loop end value, loop step value are
 *  converted to the type of loop index if their type differ
 *  from the type of the loop index.
 *  If the index variable is a floating variable, then repetition
 *  count is computed at the entry to the loop by the formula
 *    MAX( INT( (<loop end value> - <loop start value>
 *               + <loop step value>) / <loop step value>), 0)
 *  if upward parameter is true, or by the formula
 *    MAX( INT( (<loop start value> - <loop end value>
 *               + <loop step value>) / <loop step value>), 0)
 *  if upward parameter is false.
 *  The loop step value should be positive.
</PRE>
**/
public interface
IndexedLoopStmt extends LoopStmt {

/** getLoopIndex
 *  Get the loop index variable of this loop.
 *  @return loop index variable.
**/
public Var
getLoopIndex();

/** getStartValue
 *  Get the start value of the loop index.
 *  @return loop start value.
**/
public Exp
getStartValue();

/** getEndValue
 *  Get the end value of the loop index.
 *  @return loop end value.
**/
public Exp
getEndValue();

/** getStepValue
 *  Get the step value of this loop.
 *  @return loop step value.
**/
public Exp
getStepValue();

/** isUpward
 *  See whether upward or downword.
 *  @return true if upward, else return false.
**/
public boolean
isUpward();

} // IndexedLoopStmt interface
