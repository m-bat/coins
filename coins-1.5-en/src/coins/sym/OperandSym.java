/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

//========================================//

/** OperandSym interface
 *  OperandSymImpl class interface.
 *
 *  Interface for symbols that can be an operand of executable
 *  operations.
 *  It is inherited by Var, Const, Label, and Reg.
 *  Subp (subprogram) does not inherit OperandSym
 *  because subprogram has many information that are not used
 *  in other symbols.
**/
public interface
OperandSym extends Sym
{

/** Get/set information locally used for flow analysis, optimization,
 *  parallelyzation, etc. of this subprogram.
**/
public Object getFlowInf    ();
public void   setFlowInf    ( Object pInf );

/* 
public Object getOptInf     ();
public void   setOptInf     ( Object pInf );
public Object getParallelInf();
public void   setParallelInf( Object pInf );
public Object getRegAllocInf();
public void   setRegAllocInf( Object pInf );
public Object getCodeGenInf ();
public void   setCodeGenInf ( Object pInf );
*/ 
} // OperandSym interface


