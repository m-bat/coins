/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;

/** OperandSymImpl Class
 *  Operand symbol class
 *  which is inherited by VarImpl, ConstImpl, LabelImpl,
 *  and RegImpl.
 **/

public class
OperandSymImpl extends SymImpl implements OperandSym {

//===== Field declarations ======//

  /** Information locally used for flow analysis, optimization,
      parallelyzation, etc. of this subprogram.
  **/
  protected Object fFlowInf;     // Information for flow analyer.
  protected Object fOptInf;      // Information for optimizer.
  protected Object fParallelInf; // Information for parallelyzer.
  protected Object fRegAllocInf; // Information for register allocator.
  protected Object fCodeGenInf;  // Information for code generator.

public
OperandSymImpl() { }

public
OperandSymImpl( SymRoot pSymRoot )
{
  super(pSymRoot);
}

//====== Methods to get/set information ======//

public Object getFlowInf    () { return fFlowInf; }
public void   setFlowInf    ( Object pInf ) { fFlowInf = pInf; }
//##81 public Object getOptInf     () { return fOptInf; }
//##81 public void   setOptInf     ( Object pInf ) { fOptInf = pInf; }
//##81 public Object getParallelInf() { return fParallelInf; }
//##81 public void   setParallelInf( Object pInf ) { fParallelInf = pInf; }
//##81 public Object getRegAllocInf() { return fRegAllocInf; }
//##81 public void   setRegAllocInf( Object pInf ) { fRegAllocInf = pInf; }
//##81 public Object getCodeGenInf () { return fCodeGenInf; }
//##81 public void   setCodeGenInf ( Object pInf ) { fFlowInf = pInf; }

} // OperandSymImpl class

