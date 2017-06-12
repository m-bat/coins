/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins;

/** CompileError: 
 *  Exception for error processing.
 *  As for warning, use warning method in Root.
**/
public class 
CompileError extends RuntimeException //##7
{

  int fErrorLevel;
  int fErrorNumber;

public 
CompileError( int pErrorNumber, String pMessage ) {
  super("Error " + pErrorNumber + " " + pMessage + ".");
  fErrorLevel = 2;
  fErrorNumber = pErrorNumber;
}

public
CompileError( String pMessage ) {
  super(pMessage);
}

public
CompileError() {
}

// Error Level  Message
//number
// 0000-0999 Messages related to compiler control.
// 1000-1999 Messages related to intermediate representation.
// 2000-2999 Messages related to syntax analysis. 
// 3000-4999 Messages related to semantic analysis. 
// 5000-5999 Messages related to flow analysis and code optimization. 
// 6000-6999 Messages related to parallelization. 
// 7000-7999 Messages related to back end.
// 8000-8999 Messages related to others. 
 
//   100 3 IO error. (Main)
//  1010 2 Symbol xxx of kind yy can not be redefined as zz. (SymTableImpl)
//  1101 2 CloneNotSupportedException. (StmtImpl)
//  1102 2 CloneNotSupportedException. (IrListImpl)
//  1103 2 IrList.clone() ClassCastException. (IrListImpl)
//  1210 2 ConditionalInitPart does not take if-statement form. (LoopStmtImpl)
//  1211 2 ConditionalInitPart does not take if-statement form. (LoopStmtImpl)

} // CompileError class

