/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins;

import java.io.*;
import coins.sym.Sym;
import coins.ir.hir.HIR;
import coins.driver.CompileSpecification;

/**IoRoot:
<PRE>
 * Abstraction of Input/Output context.
 *  IoRoot class is used to invoke methods related to input/output
 *  and to access information related to them.
 *  For example, information of source file, object file,
 *  and methods for error message output, debug information output
 *  can be accessed via IoRoot object.
 *  Objects of other Root classes (SymRoot, HirRoot, LirRoot,
 *  FlowRoot, etc.) include a reference to the instance of IoRoot
 *  in order to enable input/output operations.
 *  All Sym objects include a reference to SymRoot object,
 *  all HIR objects include a reference to HirRoot,
 *  and so on. In this way, almost all classes has a link to
 *  the IoRoot directly or indirectly so that input/output
 *  operations can be issued in their methods.
 *
*** Input/output file:
  public final PrintStream
    printOut,         // Print out file.
    objectFile,       // Object file.
    msgOut;           // Message output file.

*** Message control object:
  public Message      // (See Message class)
    msgNote,          // Note for user.
    msgWarning,       // Obsolete
    msgRecovered,     // Recovered error that may cause other error.
    msgError,         // Compile error that discards some parts.
    msgFatal;         // Fatal error that terminates compiler process.

*** Debug control object:
  public Debug
    dbgControl,  // For Root and compiler control
    dbgHir,      // For HIR
    dbgLir,      // For LIR
    dbgSym,      // For Sym
    dbgFlow,     // For Flow
    dbgAlias,    // For Alias analysis
    dbgParse,    // For Parser
    dbgToHir,    // For AstToHir
    dbgToLir,    // For HirToLir
    dbgOpt1,     // For Optimization 1
    dbgPara1,    // For Parallelization 1
    dbgReg,      // For Register allocation
    dbgCodeGen;  // For Code generation
</PRE>
**/

public class IoRoot {
  private final CompileSpecification fSpec;    /* options etc. */
  private final File fSourceFile;
  private final String fSourceFilePath; // Absolute path of the source file. //##62

  /** Input/output file */
  public final PrintStream
    printOut,         // Print out file.
    objectFile,       // Object file.
    msgOut;           // Message output file.

  /** Message control object. */
  public Message      // (See Message class)
    msgNote,          // Note for user.
    msgWarning,       // Obsolete
    msgRecovered,     // Recovered error that may cause other error.
    msgError,         // Compile error that discards some parts.
    msgFatal;         // Fatal error that terminates compiler process.

  /** Debug control object. */
  public Debug
    dbgControl,  // For Root and compiler control
    dbgHir,      // For HIR
    //##67 dbgLir,      // For LIR
    dbgSym,      // For Sym
    dbgFlow,     // For Flow
    dbgAlias,    // For Alias analysis //##31
    dbgParse,    // For C Parser
    dbgToHir,    // For C-AstToHir
    //##74 dbgToLir,    // For HirToLir
    dbgToLir,    // For HirToLir //##76
    dbgOpt1,     // For HIR Optimization
    dbgPara1;    // For Loop Parallelization
    //##67 dbgReg,      // For Register allocation
    //##67 dbgCodeGen;  // For Code generation

  /** Reference to MachineParam */
  public final MachineParam //##12
    machineParam;

  /** Reference to the SymRoot object used to access
   *  Sym information and to invoke Sym methods.
  **/
  public SymRoot
    symRoot;

  protected String
    machineName  = "sparc",
    languageName = "C";

  private int
    fTotalErrorCount = 0, /* Total number of errors detected
                             in the compile unit.
                             There is no method to reset this counter. */
    fMessageCount = 0;  /* Accumulated number of messages issued
                           in a subprogram, etc.
                           This may be reset for each subprogram. */

//### BEGIN
  public IoRoot(File pSourceFile,
                PrintStream pPrintOut, PrintStream pObjectFile,
                PrintStream pMsgOut, CompileSpecification pSpec
                )
  {
   this(pSourceFile, pPrintOut, pObjectFile, pMsgOut, pSpec,
          "sparc", "C");
  }
//### END
  public IoRoot(File pSourceFile,
                PrintStream pPrintOut, PrintStream pObjectFile,
                PrintStream pMsgOut, CompileSpecification pSpec,
                String pMachineName, String pLanguageName)
  {
    fSourceFile = pSourceFile;
    fSourceFilePath = fSourceFile.getAbsolutePath(); //##62
    printOut = pPrintOut;
    objectFile = pObjectFile;
    msgOut = pMsgOut;
    fSpec = pSpec;
    //##51 BEGIN
    if (pMachineName != null) {
      //##81 machineName  = pMachineName;
      machineName = pMachineName.intern(); //##81
    }
    if (pLanguageName != null) {
      //##81 languageName = pLanguageName;
      languageName = pLanguageName.intern(); //##81
    }
    String lTarget = null;
    if ((pSpec != null)&&(pSpec.getCoinsOptions() != null))
      lTarget = pSpec.getCoinsOptions().getArg("target");
    if (lTarget == null)
      lTarget = "sparc";
    // System.out.println(" IoRoot target name " + lTarget); //###
    if (lTarget.equals("sparc")) {
      machineParam = new MachineParamSparc(this);
    }else if (lTarget.equals("x86")||lTarget.equals("x86-cygwin")) { //##53
       machineParam = new MachineParamX86(this);
     }else if (lTarget.equals("x86_64")||lTarget.equals("x86_64-mac")) { //##85
        machineParam = new MachineParamX86_64(this);  //##81
    }else if (lTarget.equals("arm")) { //##70
       machineParam = new MachineParamArm(this); //##70
    }else if (lTarget.equals("mips")||lTarget.equals("mips-spim")) { //##72
       machineParam = new MachineParamMips(this); //##72
    }else if (lTarget.equals("sh4")) { //##72
       machineParam = new MachineParamSH4(this); //##72
    }else if (lTarget.equals("ppc")) { //##72
       machineParam = new MachineParamPpc(this); //##72
    }else if (lTarget.equals("alpha")) { //##72
       machineParam = new MachineParamAlpha(this); //##72
   }else if (lTarget.equals("mb")) {
        machineParam = new MachineParamMicroBlaze(this);
    }else
      machineParam = new MachineParam(this);
    //##51 END
    initiate();
  }

  /* ====== Methods ====== */

  /** Initiate IoRoot by initiating Debug information and
   *  Message information.
  **/
  private void
  initiate()
  {
    Debug lDebug;
    Message lMessage;
    lDebug  = new Debug(this);
    lDebug.initiate();
    lMessage = new Message(this);
    lMessage.initiate();
  }

  /** Get the CompilerSpecification currently active.
  **/
  public CompileSpecification
  getCompileSpecification()
  {
    return fSpec;
  }

  /** Get the source file
  **/
  public File
  getSourceFile()
  {
    return fSourceFile;
  }

//##62 BEGIN
  /** Get absolute path of the source file
  **/
  public String
  getSourceFilePath()
  {
    return fSourceFilePath;
  }
//##62 END

  //##51 BEGIN
  public String
  getLanguageName()
  {
    return languageName;
  }
  //##51 END

  // ##81 BEGIN
  public String
  getMachineName()
{
  return machineName;
}
//##81 END

  /** Increment the message counter.
  **/
  public synchronized void
  incrementMessageCount()
  {
    fMessageCount++;
  }

  /** Get the value of the message counter.
  **/
  public synchronized int
  getMessageCount()
  {
    return fMessageCount;
  }

  /** Reset the message counter.
  **/
  public synchronized void
  resetMessageCount()
  {
    fMessageCount = 0;
  }

  /** Add pCount to the total-message-counter.
  **/
  public synchronized int
  addToTotalErrorCount(int pCount)
  {
    fTotalErrorCount = fTotalErrorCount + pCount;
    return fTotalErrorCount;
  }

/** toStringObject:
 *  Get the string image of given object pItem.
 *  If pItem is null, give "null".
 *  This method may be usefull to get string image of
 *  objects that might be null.
 *  @param pItem: object to get the string image.
 *  @return the string image of the object or "null"
 *      if the object is null.
**/
  public static String
  toStringObject(Object pItem)
  {
    if (pItem == null) {
      return "null";
    } else {
      return pItem.toString();
    }
  }

/** toStringObjectShort:
 *  Get the short string image of given object pItem.
 *  @param pItem: object to get the string image.
 *  @return the short string image of the object or "null"
 *      if the object is null.
**/
  public static String
  toStringObjectShort(Object pItem)
  {
    if (pItem == null) {
      return "null";
    } else {
      if (pItem instanceof HIR)
        return ((HIR)pItem).toStringShort();
      else if (pItem instanceof Sym)
        return ((Sym)pItem).toStringShort();
      else
        return pItem.toString();
    }
  }

} // IoRoot
