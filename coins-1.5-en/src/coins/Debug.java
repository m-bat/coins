/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins;

import coins.driver.Trace; //##11
import coins.driver.CompileSpecification; //##12
import coins.sym.Sym;
import coins.ir.IR;

/** Debug:
<PRE>
 *  Debug class for printing debug information.
 *  Debug-print can be controlled by command option
 *  -coins:trace=xx1.n1/xx2.n2 ... where
 *  xx1, xx2, .. are one of
 *    Control  -- What phases and (principal) modules are executed
 *    HIR      -- HIR information
 *    Sym      -- Sym and symbol table information
 *    Flow     -- Control flow and data flow information
 *    Alias    -- Alias analysis information //##31
 *    Parse    -- C Parser information
 *    ToHir    -- C-AST (abstract syntax tree of C) to HIR transformation inf
 *    Opt1     -- HIR optimizer information
 *    Para1    -- Loop parallelizer information
 *  and n1, n2, ... are debug level of corresponding debug-control.
 *    0: no output, 1: major, ... 9: very much detailed information.
 *  Example:
 *    -coins:trace=HIR.1/Sym.1/Flow.2
 *  Note:
 *   There are other debug-print control items that do not use
 *   this Debug class. See TRACE of Registry.java.
</PRE>
**/
public class
Debug
{

//---- Instance fields ------//

  private final IoRoot
    fIoRoot;      // IoRoot received at instanciation.

  /** fDebugLevel:
   *  If fDebugLevel is set to 0, no debug information is printed.
   *  The print method will print debug information if its debug
   *  level is less than or equal to fDebugLevel.
   *  This level can be seen in such way as
   *    if (ioRoot.dbgHir.getLevel() > 2) ...
   *  See print(....), getLevel().
  **/
  private int
    fDebugLevel;

  /** Header to be attached to show information kind.
  **/
  private String
    fHeader;       // Header to be attached to show information kind.

//---- Constructor ------//

/** Debug:
 *  Constructor to initiate all Debug objects.
**/
public
Debug( IoRoot pIoRoot )
{
  fIoRoot = pIoRoot;
//##  initiate(pIoRoot);
} // debug

/** Debug:
 *  Constructor for each Debug class. Record the header and debug level.
**/
public
Debug( IoRoot pIoRoot, String pHeader, int pDebugLevel )
{
  fIoRoot       = pIoRoot;
  fHeader       = pHeader;
  fDebugLevel   = pDebugLevel;
} // debug

//---- Methods ------//

/** Initialize debug control information.
**/
public void
initiate( )
{
  int lControl, lHir, lLir, lSym, lFlow,
      lAlias,  //##31
      lParse, lToHir, lToLir,
      lOpt1, lPara1, lReg, lCodeGen;
  CompileSpecification lSpec;
  Trace                 lTrace;
  lSpec = fIoRoot.getCompileSpecification();
  if (lSpec != null) {
    lTrace = lSpec.getTrace();
  lControl =lTrace.getTraceLevel("Control");
  lHir     =lTrace.getTraceLevel("HIR");
  lLir     =lTrace.getTraceLevel("LIR");
  lSym     =lTrace.getTraceLevel("Sym");
  lFlow    =lTrace.getTraceLevel("Flow");
  lAlias   =lTrace.getTraceLevel("Alias");
  lParse   =lTrace.getTraceLevel("Parse");
  lToHir   =lTrace.getTraceLevel("ToHir");
  lToLir   =lTrace.getTraceLevel("ToLir");
  lOpt1    =lTrace.getTraceLevel("Opt1");
  lPara1   =lTrace.getTraceLevel("Para1");
  //##67 lReg     =lTrace.getTraceLevel("Reg");
  //##67 lCodeGen =lTrace.getTraceLevel("CodeGen");
  //  System.out.println("Debug level " + lControl + " " + lHir +
  //  " " + lLir + " " + lSym + " " + lFlow + " " + lParse +
  //  " " + lToHir + " " + lToLir + " " + lOpt1 + " " + lPara1 +
  //  " " + lReg + " " + lCodeGen);
  }else {
    lControl = 0;
    lHir     = 0;
    lLir     = 0;
    lSym     = 0;
    lFlow    = 0;
    lAlias    = 0;
    lParse   = 0;
    lToHir   = 0;
    lToLir   = 0;
    lOpt1    = 0;
    lPara1   = 0;
    lReg     = 0;
    lCodeGen = 0;
  }
  fIoRoot.dbgControl = new Debug(fIoRoot, "DBGC",   lControl);
  fIoRoot.dbgHir     = new Debug(fIoRoot, "DBGH",   lHir);
  //##67 fIoRoot.dbgLir     = new Debug(fIoRoot, "DBGL",   lLir);
  fIoRoot.dbgSym     = new Debug(fIoRoot, "DBGS",   lSym);
  fIoRoot.dbgFlow    = new Debug(fIoRoot, "DBGF",   lFlow);
  fIoRoot.dbgAlias   = new Debug(fIoRoot, "DBGA",   lAlias); //##31
  fIoRoot.dbgParse   = new Debug(fIoRoot, "DBGP",   lParse);
  fIoRoot.dbgToHir   = new Debug(fIoRoot, "DBGtoH", lToHir);
  // fIoRoot.dbgToLir   = new Debug(fIoRoot, "DBGtoL", lToLir);
  fIoRoot.dbgToLir   = new Debug(fIoRoot, "DBGtoL", lToLir); //##76
  fIoRoot.dbgOpt1    = new Debug(fIoRoot, "DBGO1",  lOpt1);
  fIoRoot.dbgPara1   = new Debug(fIoRoot, "DBGP1",  lPara1);
  //##67 fIoRoot.dbgReg     = new Debug(fIoRoot, "DBGR",   lReg);
  //##67 fIoRoot.dbgCodeGen = new Debug(fIoRoot, "DBGCG",  lCodeGen);
} // initiate

/** print with at-parameter:
 *  Print debug message if pLevel <= fDebugLevel
 *  after starting new line.
 *  @param pLevel: Information level (more higher, more detail).
 *  @param pAt   : Shows where the message is issued (method name, etc.)
 *  @param pMessage: Message to be printed.
**/
public void
print( int pLevel, String pAt, String pMessage )
{
  if (pLevel <= fDebugLevel) {
    fIoRoot.printOut.print("\n" + fHeader + " " + pAt + " " + pMessage );
  }
} // print with at-parameter

/** print without at-parameter:
 *  Print debug message if pLevel <= fDebugLevel at the end of
 *  current line (without starting new line).
 *  @param pLevel: Information level (more higher, more detail).
 *  @param pMessage: Message to be printed.
**/
public void
print( int pLevel, String pMessage )
{
  if (pLevel <= fDebugLevel) {
    fIoRoot.printOut.print(" " + pMessage );
  }
} // print without at-parameter

/** println:
 *  Line feed if pLevel <= fDebugLevel.
**/
public void
println( int pLevel )
{
  if (pLevel <= fDebugLevel) {
    fIoRoot.printOut.print("\n");
  }
} // println

/** printObject:
 *  Print pObject.toString() if pObject is not null after pHeader.
 *  If pObject is null, print null.
 *  This will be useful to print without testing a parameter is null or not.
 *  This does not change the parameter to String if debug level is
 *  lower than pLevel.
**/
public void
printObject( int pLevel, String pHeader, Object pObject)
{
  if (pLevel <= fDebugLevel) {
    if (pObject != null)
      fIoRoot.printOut.print(" " + pHeader + " "  + pObject.toString() );
    else
      fIoRoot.printOut.print(" " + pHeader + " null" );
  }
} // printObject

public void
setLevel( int pLevel )
{
  fDebugLevel = pLevel;
}

public int
getLevel()
{
  return fDebugLevel;
}

} // Debug class

