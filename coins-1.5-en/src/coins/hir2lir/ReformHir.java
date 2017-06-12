/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.hir2lir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.aflow.BBlock; //##70
import coins.aflow.FlowAnalSymVector; //##70
import coins.aflow.FlowResults;
import coins.aflow.ShowFlow;
import coins.aflow.SubpFlow; //##70
import coins.aflow.SubpFlowImpl; //##70
import coins.driver.CoinsOptions;
import coins.driver.Trace;
import coins.hir2c.HirBaseToC;
import coins.hir2c.HirBaseToCImpl;
import coins.ir.IrList;
import coins.ir.IrListImpl;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.ForStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.IfStmt;
import coins.ir.hir.InfStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Program;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.Stmt;
import coins.ir.hir.SymNode;
import coins.ir.hir.VarNode;
import coins.lparallel.BasicInduction; //##71
import coins.lparallel.LoopParallel;
import coins.lparallel.LoopParallelImpl;
import coins.lparallel.LoopTable;
import coins.lparallel.LoopUtil;
import coins.lparallel.Reduction;
import coins.lparallel.RegisterClasses;
import coins.SymRoot;
import coins.sym.CharConst;
import coins.sym.IntConst;
import coins.sym.Param;
import coins.sym.PointerType;
import coins.sym.StringConst;
import coins.sym.StructType;
import coins.sym.Subp;
import coins.sym.SubpType;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.UnionType;
import coins.sym.Var;
import coins.sym.VectorType;

/** ReformHir class
 * Reform HIR for
 *   profiling and
 *   parallelization without using OpenMP.
 **/
public class
  ReformHir
{

//------ Public fields ------//

  public final IoRoot // Used to access Root information.
    ioRoot;

  public final SymRoot // Used to access Root information.
    symRoot;

  public final HirRoot // Used to access Root information.
    hirRoot;

  protected HIR
    hir;

  protected Sym
    sym;

  protected CoinsOptions
    fCoinsOptions;

  protected boolean
    fChanged;

  public FlowRoot
    flowRoot;

  protected LoopParallel
    fLoopParallel;
  // protected coins.lparallel2.LoopParallel
  //   fLoopParallel2;

  protected LoopUtil
    fUtil;
  // protected coins.lparallel2.LoopUtil
  //   fUtil2;

  protected coins.flow.SubpFlow
    fSubpFlow;
  protected coins.aflow.SubpFlow
    subpFlow;

  protected FlowResults
    fResults;

  protected SymTable
    fSymTableCurrent;

  protected List
    fSubpDefinitionList;  // List of SubpDefinitions to be added.

  protected Map
    fStatementsToBeReplaced; // Map old statement to new statement.

  protected Map
    fStatementsToBeAdded; // Map old statement and new statement
                          // to be added after the old statement.
  protected List
    fStatementsToBeDeleted; // List of statements to be deleted.

  protected Map
    fLoopStmtToLoopTable;   // Map loop statement to
        // corresponding loop table in current subprogram.
        // For enforced parallelization, corresponding LoopTable
        // is replaced by the one generated from pragma.
  protected List            // List of loop statements to be
    fLoopsToBeParallelized; // parallelized in current subprogram.
        // Only loops specified by #pragma are parallelized.
        // Loops not specified by pragma are not parallelized
        // even if loop analysis tells that they can be parallelized.
  protected int
    fMaxDegreeOfParallel = 2;

  protected Type
    fThreadType;

  protected Var
    fThreadDescriptor,
    fNumberOfThreads,
    fThreadIdArray,
    fThreadIdOfMaster;

  protected boolean
    fStackInLocalMemory;

  protected Subp
    fThreadInit,
    fThreadEnd,
    fThreadPreprocessForDoAllLoop,
    fThreadPostprocess,
    fThreadForkForDoAll,
    fThreadPreprocessForDoAllThread,
    fThreadPostprocessForDoAllThread,
    fThreadJoin,
    fThreadSelfId;

  protected int
    fDbgLevel;

  public final int
    fMaximumNumberOfReductions = 2;

//====== Constructors ======//

  /** ReformHir
   * Process HIR pragmas and change them to ones that may correspond to
   * HIR pragma.
   * If InfStmts are changed, then renumber the HIR nodes.
   */
  public
    ReformHir(HirRoot pHirRoot)
  {
    ioRoot = pHirRoot.ioRoot;
    symRoot = pHirRoot.symRoot;
    hirRoot = pHirRoot;
    sym = symRoot.sym;
    hir = hirRoot.hir;
    fDbgLevel = ioRoot.dbgHir.getLevel();
    fChanged = false;
    fCoinsOptions = ioRoot.getCompileSpecification().getCoinsOptions();
    if (fCoinsOptions.isSet("simulate")) {
      reformForProfiling();
    }
    //##74 if (fCoinsOptions.isSet("doAll"))
    if (fCoinsOptions.isSet("parallelDoAll")) //##74
    {
      reformForParallel();
    } else if (fCoinsOptions.isSet("lparallel2")) {
      // reformForParallel2();
    }
    if (fChanged) {
      Program lProgram = (Program)hirRoot.programRoot;
      HIR lProgInitPart = (HIR)lProgram.getInitiationPart();
      if ((lProgInitPart instanceof BlockStmt) &&
          (((BlockStmt)lProgInitPart).getFirstStmt() == null)) {
        lProgram.setChild2(hir.nullNode());
      }
      ((HIR)hirRoot.programRoot).finishHir();
    }
  } // ReformHir

  /**
   * Change global gragmas (pragmas placed as global declaration)
   *   (inf prof (defaultTraceOn))
   *   (inf prof (defaultTraceOff))
   *   (inf prof (subpTraceOn subp1 subp1 ...))
   *   (inf prof (subpTraceOff subp1 subp1 ...))
   * to subprogram-wise pragma.
   */
  public void
    reformForProfiling()
  {
    ioRoot.dbgHir.print(1, "\nreformForProfiling\n");
    if (!fCoinsOptions.isSet("simulate")) {
      ioRoot.dbgHir.print(2, "no simulate option\n");
      return;
    }
    Set lSubprograms = new HashSet();
    Set lToBeTraced = new HashSet();
    Set lNonTraced = new HashSet();
    Program lProgram = (Program)hirRoot.programRoot;
    IrList lSubpList = lProgram.getSubpDefinitionList();
    for (Iterator lIt = lSubpList.iterator();
         lIt.hasNext(); ) {
      SubpDefinition lSubpDef = (SubpDefinition)lIt.next();
      lSubprograms.add(lSubpDef.getSubpSym());
    }
    ioRoot.dbgHir.print(3, "Subprograms defined", lSubprograms + "\n");
    lToBeTraced.addAll(lSubprograms); // Default is to be simulated.
    List lStmtToBeDeleted = new LinkedList();
    HIR lProgInitPart = (HIR)lProgram.getInitiationPart();
    if (lProgInitPart instanceof BlockStmt) {
      for (Stmt lStmt = ((BlockStmt)lProgInitPart).getFirstStmt();
           lStmt != null;
           lStmt = lStmt.getNextStmt()) {
        if ((lStmt instanceof InfStmt) &&
            (((InfStmt)lStmt).getInfKind() == "simulate")) {
          ioRoot.dbgHir.print(3, lStmt.toString() + "\n");
          IrList lOptionList = ((InfStmt)lStmt).getInfList("simulate");
          int lIndex;
          ioRoot.dbgHir.print(4, " option list " +  lOptionList + "\n");
          String lOptionName = ((InfStmt)lStmt).getInfSubkindOf("simulate");
          if (lOptionName == null) {
            ioRoot.dbgHir.print(1, "\nUnknown option subkind " +
              ((InfStmt)lStmt).toString() + "\n");
            continue;
          }
          if (lOptionName == "profileOnAll") {
            lToBeTraced.addAll(lSubprograms);
            lStmtToBeDeleted.add(lStmt); // Do not delete here
            // because deletion may affect process of succeeding Stmt.
            fChanged = true;
          } else if (lOptionName == "profileOffAll") {
            lToBeTraced.clear();
            lNonTraced.addAll(lSubprograms);
            lStmtToBeDeleted.add(lStmt);
            fChanged = true;
          } else if (lOptionName == "profileOn") {
            lIndex = 0;
            for (Iterator lIt = lOptionList.iterator();
                 lIt.hasNext(); lIndex++) {
              Object lSubp = lIt.next();
              ioRoot.dbgHir.print(4, " " + lSubp
                + " " + lSubp.getClass() + "\n");
              if ((lSubp instanceof Subp)) {
                lToBeTraced.add(lSubp);
                lNonTraced.remove(lSubp);
              }
            }
            lStmtToBeDeleted.add(lStmt);
            fChanged = true;
          } else if (lOptionName == "profileOff") {
            lIndex = 0;
            for (Iterator lIt = lOptionList.iterator();
                 lIt.hasNext(); lIndex++) {
              Object lSubp = lIt.next();
              ioRoot.dbgHir.print(4, " " + lSubp
                + " " + lSubp.getClass() + "\n");
              if ((lIndex > 0) && (lSubp instanceof Subp)) {
                lNonTraced.add(lSubp);
                lToBeTraced.remove(lSubp);
              }
            }
            lStmtToBeDeleted.add(lStmt);
            fChanged = true;
          } else {
            ioRoot.dbgHir.print(1, "\nUnknown option " + lOptionName + "\n");
            continue;
          }
        } // End for simulate option
      } // End for each Stmt in InitiationPart
    }
      ioRoot.dbgHir.print(1, "To be profiled", lToBeTraced.toString());
      ioRoot.dbgHir.print(1, "Not to be profiled", lNonTraced.toString());
      String lProfOnOff = "profileOn"; //##060829
      for (Iterator lIt = lSubpList.iterator();
           lIt.hasNext(); ) {
        SubpDefinition lSubpDef = (SubpDefinition)lIt.next();
        Subp lSubp2 = lSubpDef.getSubpSym();
        /*
        if (!lToBeTraced.contains(lSubp2)) {
          continue;
        }*/
        if (lToBeTraced.contains(lSubp2)) //##060829
          lProfOnOff = "profileOn";       //##060829
        if (lNonTraced.contains(lSubp2))  //##060829
          lProfOnOff = "profileOff";      //##060829
        Stmt lSubpBody = lSubpDef.getHirBody();
        if (lSubpBody instanceof LabeledStmt) {
          lSubpBody = ((LabeledStmt)lSubpBody).getStmt();
        }
        if (lSubpBody instanceof BlockStmt) {
          IrList lIrList = new IrListImpl(hirRoot);
          //##060829 lIrList.add("profileOn");
          lIrList.add(lProfOnOff);  //##060829
          InfStmt lInfStmt = hir.infStmt("simulate", lIrList);
          ((BlockStmt)lSubpBody).addFirstStmt(lInfStmt);
          ioRoot.dbgHir.print(2, "add to " + lSubpDef.getSubpSym().getName(),
            lInfStmt.toString());
          fChanged = true;
        }
      } // End of SubpDefinition list processing
      // Delete processed InfStmts.
      for (Iterator lIt2 = lStmtToBeDeleted.iterator();
           lIt2.hasNext(); ) {
        Stmt lStmt2 = (Stmt)lIt2.next();
        lStmt2.deleteThisStmt();
        fChanged = true;
      }
  } // reformForProfiling

  public void
    reformForParallel()
  {
    String lOption = ioRoot.getCompileSpecification().
      //##74 getCoinsOptions().getArg("doAll");
      getCoinsOptions().getArg("parallelDoAll"); //##74
    if ((lOption != null) && (lOption.charAt(0) >= '0') &&
        (lOption.charAt(0) <= '9')) {
      fMaxDegreeOfParallel = lOption.charAt(0) - '0';
    } else {
      fMaxDegreeOfParallel = 2;
    }
    fStackInLocalMemory = false; //## REFINE
    ioRoot.dbgHir.print(1, "\nreformForParallel", "maximum degree "
      + lOption + " " + fMaxDegreeOfParallel);
    //##74 if (!fCoinsOptions.isSet("doAll"))
    if (!fCoinsOptions.isSet("parallelDoAll")) //##74
    {
      ioRoot.dbgHir.print(2, "no parallelDoAll option. Do not parallelize.\n");
      return;
    }
    flowRoot = hirRoot.getFlowRoot();
    if (flowRoot == null) {
      flowRoot = new FlowRoot(hirRoot);
    }
    Set lSubprograms = new HashSet(); // Set of subprograms defined.
    Set lToBeParallelized = new HashSet(); // Subprograms to be parallelized.
    Program lProgram = (Program)hirRoot.programRoot;
    fSubpDefinitionList = new ArrayList(); // List of SubpDefinitions to be added.
    fStatementsToBeAdded = new HashMap(); // Map statement to Stmt to be added.
    fStatementsToBeReplaced = new HashMap(); // Map old statement to new statement.
    fStatementsToBeDeleted = new ArrayList(); // Statements to be deleted.
    fLoopStmtToLoopTable = new HashMap(); // Loop statement to LoopTable.

    //-- Get symbols declared in
    //     #include coinsParallelFramework.h
    String lMissingSym = "";
    Sym lSym0 = symRoot.symTableRoot.
      search("coinsThread", Sym.KIND_VAR);
    if (lSym0 == null)
      lMissingSym = lMissingSym + " coinsThread";
    Sym lSym1 = symRoot.symTableRoot.
      search("coinsNumberOfThreads", Sym.KIND_VAR);
    if (lSym1 == null)
      lMissingSym = lMissingSym + " coinsNumberOfThreads";
    Sym lSym2 = symRoot.symTableRoot.
      search("coinsThreadInit", Sym.KIND_SUBP);
    if (lSym2 == null)
      lMissingSym = lMissingSym + " coinsThreadInit";
    Sym lSym3 = symRoot.symTableRoot.
      search("coinsThreadEnd", Sym.KIND_SUBP);
    if (lSym3 == null)
      lMissingSym = lMissingSym + " coinsThreadEnd";
    Sym lSym4 = symRoot.symTableRoot.
      search("coinsThreadPreprocessForDoAllLoop", Sym.KIND_SUBP);
    if (lSym4 == null)
      lMissingSym = lMissingSym + " coinsThreadPreprocessForDoAllLoop";
    Sym lSym5 = symRoot.symTableRoot.
      search("coinsThreadPostprocess", Sym.KIND_SUBP);
    if (lSym5 == null)
      lMissingSym = lMissingSym + " coinsThreadPostprocess";
    Sym lSym6 = symRoot.symTableRoot.
      search("coinsThreadForkForDoAll", Sym.KIND_SUBP);
    if (lSym6 == null)
      lMissingSym = lMissingSym + " coinsThreadForkForDoAll";
    Sym lSym7 = symRoot.symTableRoot.
      search("coinsThreadPreprocessForDoAllThread", Sym.KIND_SUBP);
    if (lSym7 == null)
      lMissingSym = lMissingSym + " coinsThreadPreprocessForDoAllThread";
    Sym lSym8 = symRoot.symTableRoot.
      search("coinsThreadPostprocessForDoAllThread", Sym.KIND_SUBP);
    if (lSym8 == null)
      lMissingSym = lMissingSym + " coinsThreadPostprocessForDoAllThread";
    Sym lSym9 = symRoot.symTableRoot.
      search("coinsThreadSelfId", Sym.KIND_SUBP);
    if (lSym9 == null)
      lMissingSym = lMissingSym + " coinsThreadSelfId";
    Sym lSym10 = symRoot.symTableRoot.
      search("coinsThreadIdArray", Sym.KIND_VAR);
    if (lSym10 == null)
      lMissingSym = lMissingSym + " coinsThreadIdArray";
    Sym lSym11 = symRoot.symTableRoot.
      search("<TYPEDEF coinsThread_t>", Sym.KIND_TYPE);
    if (lSym11 == null)
      lMissingSym = lMissingSym + " coinsThread_t";
    Sym lSym12 = symRoot.symTableRoot.
      search("coinsThreadJoin", Sym.KIND_SUBP);
    if (lSym12 == null)
      lMissingSym = lMissingSym + " coinsThreadJoin";
    String lSpecifiedSymbols =  lSym0 +
      " " + lSym1 + " " + lSym2 + " " + lSym3 + " " + lSym4 +
      " " + lSym5 + " " + lSym6 + " " + lSym7 + " " + lSym8 +
      " " + lSym9 + " " + lSym10 + " " + lSym11 + " " + lSym12;
    ioRoot.dbgHir.print(4, "Thread symbols " + lSpecifiedSymbols);
    if ((lSym0 instanceof Var) &&
        (lSym1 instanceof Var) &&(lSym2 instanceof Subp)&&
        (lSym3 instanceof Subp)&&(lSym4 instanceof Subp)&&
        (lSym5 instanceof Subp)&&(lSym6 instanceof Subp)&&
        (lSym7 instanceof Subp)&&(lSym8 instanceof Subp)&&
        (lSym9 instanceof Subp)&&(lSym10 instanceof Var)&&
        (lSym11 instanceof Type)) {
      fThreadDescriptor = (Var)lSym0;
      fNumberOfThreads  = (Var)lSym1;
      fThreadInit = (Subp)lSym2;
      fThreadEnd  = (Subp)lSym3;
      fThreadPreprocessForDoAllLoop = (Subp)lSym4;
      fThreadPostprocess  = (Subp)lSym5;
      fThreadForkForDoAll = (Subp)lSym6;
      fThreadPreprocessForDoAllThread  = (Subp)lSym7;
      fThreadPostprocessForDoAllThread = (Subp)lSym8;
      fThreadJoin       = (Subp)lSym12;
      fThreadSelfId     = (Subp)lSym9;
      fThreadIdArray    = (Var)lSym10;
      fThreadType       = (Type)lSym11;
    } else {
      ioRoot.msgNote.put(
        "#include coinsParallelFramework.h does not define symbols "
        + lMissingSym);
      ioRoot.msgError.put(
        "#include coinsParallelFramework.h is missing or incorrect. Parallelization aborted.");
      return;
    }
    //-- Get the set of subprograms defined and
    //   for main program, process
    //     #pragma parallel init
    //     #pragma parallel end
    IrList lSubpList = lProgram.getSubpDefinitionList();
    SubpDefinition lMainSubp = null;
    for (Iterator lIt = lSubpList.iterator();
         lIt.hasNext(); ) {
      SubpDefinition lSubpDef = (SubpDefinition)lIt.next();
      Subp lSubprogram = lSubpDef.getSubpSym();
      lSubprograms.add(lSubpDef.getSubpSym());
    } // End of lSubpList iterator
    ioRoot.dbgHir.print(2, "Subprograms defined", lSubprograms + "\n");
    // List lStmtToBeDeleted = new LinkedList();
    // Map lStmtToBeInserted = new HashMap();
    //-- Collect information given by pragmas for parallelization
    //     from statements outside subprograms (included in initiation part).
    HIR lProgInitPart = (HIR)lProgram.getInitiationPart();
    if (lProgInitPart instanceof BlockStmt) {
      for (Stmt lStmt = ((BlockStmt)lProgInitPart).getFirstStmt();
           lStmt != null;
           lStmt = lStmt.getNextStmt()) {
        if ((lStmt instanceof InfStmt) &&
            (((InfStmt)lStmt).getInfKind() == "parallel")) {
          ioRoot.dbgHir.print(3, lStmt.toString() + "\n");
          IrList lOptionList = ((InfStmt)lStmt).getInfList("parallel");
          int lIndex;
          Object lObject = lOptionList.get(0);
          ioRoot.dbgHir.print(3, " option name " + lObject
            + " " + lObject.getClass() + " " + lOptionList + "\n");
          String lOptionName;
          if (lObject instanceof String) {
            lOptionName = (String)lObject;
          } else if (lObject instanceof StringConst) {
            lOptionName = ((StringConst)lObject).getStringBody();
          } else if (lObject instanceof Sym) {
            lOptionName = ((Sym)lObject).getName();
          } else {
            ioRoot.dbgHir.print(1, "\nUnknown option kind" + lObject + "\n");
            continue;
          }
          lOptionName = lOptionName.intern();
          if (lOptionName == "doAllFunc") {
            lIndex = 0;
            for (Iterator lIt = lOptionList.iterator();
                 lIt.hasNext(); lIndex++) {
              Object lSubp = lIt.next();
              ioRoot.dbgHir.print(4, " " + lSubp
                + " " + lSubp.getClass() + "\n");
              if ((lSubp instanceof Subp)) {
                lToBeParallelized.add(lSubp);
              }
            }
            fStatementsToBeDeleted.add(lStmt);
            fChanged = true;
          } else if (lOptionName == "doAllFuncAll") {
            lToBeParallelized.addAll(lSubprograms);
          } else {
            ioRoot.dbgHir.print(1, "\nUnknown option " + lOptionName + "\n");
            continue;
          }
        } // End for lparallel option
      } // End for each Stmt in InitiationPart
    } // End of InitiationPart processing.
    ioRoot.dbgHir.print(2, "To be parallelized", lToBeParallelized.toString());

    String loopOpt;
    // Setup FlowResults class.
    FlowResults.putRegClasses(new RegisterClasses());
    CoinsOptions coinsOptions =
      ioRoot.getCompileSpecification().getCoinsOptions();
    if (coinsOptions.isSet("LoopOpt")) {
      loopOpt = coinsOptions.getArg("LoopOpt");
    } else {
      loopOpt = "";
    }
    boolean lChanged;
    //-- Do parallelization for subprograms.
    // How to transform is recorded in
    //    fStatementsToBeAdded
    //    fStatementsToBeReplaced,
    //    fStatementsToBeDeleted
    // and actual transformation is done later
    // so as the transformation may not disturb
    // analysis of program.
    for (Iterator lIt = lSubpList.iterator();
         lIt.hasNext(); ) {
      SubpDefinition lSubpDef = (SubpDefinition)lIt.next();
      Subp lSubp2 = lSubpDef.getSubpSym();
      if (lSubp2.getName().intern() == "main")
        lMainSubp = lSubpDef;
      if (lToBeParallelized.contains(lSubp2)) {
        lChanged = parallelize(lSubpDef);
      }else {
        if ( (lSubpDef == lMainSubp)) {
          // Main pragram is not to be parallelized but
          // it may contain thread initiation pragma.
          lChanged = reformForInitiation(lSubpDef);
        }else {
          // Skip subprogram unnecessary to be parallelized.
          continue;
        }
      }
      if (lChanged) {
        fChanged = true;
      }
    } // End of SubpDefinition list processing
    //-- Add subprogram definitions generated.
    for (Iterator lIt2 = fSubpDefinitionList.iterator();
         lIt2.hasNext(); ) {
      SubpDefinition lSubpDef = (SubpDefinition)lIt2.next();
      ((Program)hirRoot.programRoot).addSubpDefinition(lSubpDef);
    }
    //-- Insert statements to be added:
    //   coinsThreadInit() after "#pragma parallel init"
    //   coinsThreadEnd()  after "#pragma parallel end"
    Set lAddStmts = fStatementsToBeAdded.keySet();
    for (Iterator lItAdd = lAddStmts.iterator();
         lItAdd.hasNext(); ) {
      Stmt lOldStmt = (Stmt)lItAdd.next();
      Stmt lAddStmt = (Stmt)fStatementsToBeAdded.get(lOldStmt);
      if (fDbgLevel > 0) {
        ioRoot.dbgHir.print(2, "Add after " + lOldStmt.toStringShort(),
          lAddStmt.toString());
      }
      lOldStmt.addNextStmt(lAddStmt);
    }
    //-- Replace old statements by generated statements:
    //   replace for-loop with thread invocation sequence,
    //   replace #pragma by corresponding statement.
    Set lReplStmts = fStatementsToBeReplaced.keySet();
    for (Iterator lIt3 = lReplStmts.iterator();
         lIt3.hasNext(); ) {
      Stmt lOldStmt = (Stmt)lIt3.next();
      Stmt lNewStmt = (Stmt)fStatementsToBeReplaced.get(lOldStmt);
      if (fDbgLevel > 0) {
        ioRoot.dbgHir.print(2, "Replace Stmt " + lOldStmt.toStringShort(),
          lNewStmt.toString());
      }
      lOldStmt.replaceThisNode(lNewStmt);
    }
    // Delete processed InfStmts.
    for (Iterator lIt4 = fStatementsToBeDeleted.iterator();
         lIt4.hasNext(); ) {
      Stmt lStmt2 = (Stmt)lIt4.next();
      lStmt2.deleteThisStmt();
      fChanged = true;
    }
    if (fDbgLevel > 1) {
      ioRoot.dbgHir.print(2, "\nResult of HIR parallelization \n");
      ((Program)hirRoot.programRoot).print(1, true);
    }
    ((Program)hirRoot.programRoot).finishHir();
    symRoot.symTableRoot.setUniqueNameToAllSym();
    if (fDbgLevel > 2) {
      symRoot.symTableRoot.printSymTableAllDetail();
    }
    if (coinsOptions.isSet("hir2c")) {
      try {
        makeCSourceFromHirBase("loop", hirRoot, symRoot, ioRoot);
      }
      catch (IOException e) {
        System.err.println(e.getMessage());
      }
    }
  } // reformForParallel

  /**
   * Parallelize the subprogram pSubpDefinition.
   * @param pSubpDefinition Subprogram definition
   *     requested to be parallelized.
   * @return true if parallelizing transformation is
   *     actually done.
   */
  protected boolean
    parallelize(SubpDefinition pSubpDefinition)
  {
    boolean lChanged;
    ioRoot.dbgHir.print(2, "parallelize", pSubpDefinition.getSubpSym().getName());
    fLoopParallel =
      new LoopParallelImpl(hirRoot, ioRoot, pSubpDefinition, flowRoot);
    // Instances of SubpFlow and FlowResults are made in LoopParallelImpl.
    subpFlow = flowRoot.subpFlow;
    fResults = ((SubpFlowImpl)subpFlow).fResults;
    fLoopParallel.LoopAnal();
    ((LoopParallelImpl)fLoopParallel).ReconstructHir();
    fUtil = new LoopUtil(fResults, subpFlow);
    LinkedList LoopInfoList
      = (LinkedList)fResults.get("LoopParallelList", subpFlow); //###
    fLoopParallel.SetOpenMPInfo();
    lChanged = reformHirToParallelize();
    return lChanged;
  } // parallelize

  /**
   * Reform for thread initiation without parallelizing.
   * @param pSubpDefinition Subprogram definition
   *     that may contain thread initiation pragma.
   * @return true if transformation is
   *     actually done.
   */
protected boolean
  reformForInitiation( SubpDefinition pSubpDefinition)
{
  boolean lChanged = false;
  ioRoot.dbgHir.print(2, "reformForInitiation", pSubpDefinition.getSubpSym().getName());
  if (fDbgLevel >= 4) {
    if (subpFlow != null) {
      ioRoot.dbgHir.print(3, "\nHIR before transformation \n");
      subpFlow.getSubpDefinition().print(1, true);
    }
  }
  //-- Search pragmas for parallelization.
  Stmt lSubpBody = pSubpDefinition.getHirBody();
  for (HirIterator lStmtIt = hir.hirIterator(lSubpBody);
       lStmtIt.hasNextStmt(); ) {
    Stmt lStmt = lStmtIt.getNextStmt();
    if (fDbgLevel > 2)
      ioRoot.dbgHir.print(6, " " + lStmt.toStringShort());
    if (lStmt instanceof InfStmt) {
      InfStmt lInfStmt = (InfStmt)lStmt;
      String lInfKind = lInfStmt.getInfKind().intern();
      if (lInfKind == "parallel") {
        ioRoot.dbgHir.print(3, lStmt.toString() + "\n");
        IrList lOptionList = ((InfStmt)lStmt).getInfList(lInfKind);
        int lIndex;
        Object lObject = lOptionList.get(0);
        ioRoot.dbgHir.print(3, " option name " + lObject
          + " " + lObject.getClass() + " " + lOptionList + "\n");
        String lOptionName = "";
          if (lObject instanceof String) {
            //-- Insert coinsThreadInit()
            // for "#pragma parallel init" and
            // insert coinsThreadEnd()
            // for "#pragma parallel end"
            lOptionName = (String)lObject;
            if (lOptionName.intern() == "init") {
              // Generate
              //  coinsNumberOfThreads = coinsThreadInit(fMaxDegreeOfParallel);
              IrList lParamList = hir.irList();
              lParamList.add(hir.intConstNode(fMaxDegreeOfParallel));
              AssignStmt lThreadInitStmt = hir.assignStmt(
                hir.varNode(fNumberOfThreads),
                hir.functionExp(hir.subpNode(fThreadInit),
                lParamList));
              fStatementsToBeReplaced.put(lStmt, lThreadInitStmt);
              lChanged = true;
            }
            else if (lOptionName.intern() == "end") {
              // Generate
              //  coinsThreadEnd();
              ExpStmt lThreadEndStmt = hir.callStmt(
                hir.subpNode(fThreadEnd),
                hir.irList());
              fStatementsToBeAdded.put(lStmt, lThreadEndStmt);
              lChanged = true;
            }
          }
      } // End of "parallel"
    } // End of InfStmt processing
  } // End of Stmt iterator
  return lChanged;
} // reformForInitiation

  /**
   * Decide how to transform HIR of SubpDefinition
   * indicated by subpFlow so as it can be executed in parallel.
   * The results are recorded in
   *    fStatementsToBeAdded
   *    fStatementsToBeReplaced,
   *    fStatementsToBeDeleted.
   * @return true if HIR is to be actually transformed.
   */
  protected boolean reformHirToParallelize()
  {
    LinkedList LoopInfoList
      = (LinkedList)fResults.get("LoopParallelList", subpFlow);
    ioRoot.dbgHir.print(2, "reformHirToParallelize",
      LoopInfoList.toString());
    if (fDbgLevel >= 4) {
      ioRoot.dbgHir.print(3, "\nHIR before parallelization \n");
      subpFlow.getSubpDefinition().print(1, true);
    }
    fLoopStmtToLoopTable = new HashMap();
    fLoopsToBeParallelized = new ArrayList();
    SubpDefinition lSubpDefinition = subpFlow.getSubpDefinition();
    //-- Map loop statement to corresponding LoopTable.
    for (Iterator lLoopIt = LoopInfoList.listIterator();
         lLoopIt.hasNext(); ) {
      LoopTable lTable = (LoopTable)lLoopIt.next();
      fLoopStmtToLoopTable.put(lTable.LoopStmt, lTable);
      ioRoot.dbgHir.print(2, "LoopTable for ",
        lTable.LoopStmt.toString());
    }
    //-- Search pragmas for parallelization.
    Stmt lSubpBody = lSubpDefinition.getHirBody();
    for (HirIterator lStmtIt = hir.hirIterator(lSubpBody);
         lStmtIt.hasNextStmt(); ) {
      Stmt lStmt = lStmtIt.getNextStmt();
      if (fDbgLevel > 2)
        ioRoot.dbgHir.print(6, " " + lStmt.toStringShort());
      if (lStmt instanceof InfStmt) {
        InfStmt lInfStmt = (InfStmt)lStmt;
        String lInfKind = lInfStmt.getInfKind().intern();
        if ((lInfKind == "parallel")||
            (lInfKind == "doAll")) {
          ioRoot.dbgHir.print(3, lStmt.toString() + "\n");
          IrList lOptionList = ((InfStmt)lStmt).getInfList(lInfKind);
          int lIndex;
          Object lObject = lOptionList.get(0);
          ioRoot.dbgHir.print(3, " option name " + lObject
            + " " + lObject.getClass() + " " + lOptionList + "\n");
          String lOptionName = "";
          if (lInfKind == "parallel") {
            if (lObject instanceof String) {
              //-- Insert coinsThreadInit()
              // for "#pragma parallel init" and
              // insert coinsThreadEnd()
              // for "#pragma parallel end"
              lOptionName = (String)lObject;
              if (lOptionName.intern() == "init") {
                // Generate
                //  coinsNumberOfThreads = coinsThreadInit(fMaxDegreeOfParallel);
                IrList lParamList = hir.irList();
                lParamList.add(hir.intConstNode(fMaxDegreeOfParallel));
                AssignStmt lThreadInitStmt = hir.assignStmt(
                  hir.varNode(fNumberOfThreads),
                  hir.functionExp(hir.subpNode(fThreadInit),
                  lParamList));
                fStatementsToBeReplaced.put(lStmt, lThreadInitStmt);
              }
              else if (lOptionName.intern() == "end") {
                // Generate
                //  coinsThreadEnd();
                ExpStmt lThreadEndStmt = hir.callStmt(
                  hir.subpNode(fThreadEnd),
                  hir.irList());
                fStatementsToBeAdded.put(lStmt, lThreadEndStmt);
              }
            }
          } // End of "parallel"
          //##74 else if ((lInfKind == "parallel")&&
          if ((lInfKind == "parallel")&&
               (lObject instanceof String)) {
            Object lUnexpectedItem = null;
            lOptionName = ((String)lObject).intern();
            ioRoot.dbgHir.print(3, " option name " + lOptionName + "\n");
            if ((lOptionName == "doAll")||
                (lOptionName == "forceDoAll")) {
              //-- Find corresponding loop statement.
              // parallel doAll
              // parallel forceDoAll \n
              //   (private x1 x2 ... )) \n
              //   (lastPrivate y1 y2 ...) \n
              //   (reduction (op1 z1 v1) (op2 z2 v2) ...)
              // where, op1, op2, ... are one of
              // "+", "*", "-", "max", "min", "maxIndex", "minIndex",
              // x1, x2, ... are private variables,
              // y1, y2, ... are last-private variables,
              // z1, z2, ... are reduction variables,
              // v1, v2, ... are arrays in computing maxIndex, minIndex.
              LoopStmt lLoopNextToInfStmt = null;
              if (lInfStmt.getNextStmt() instanceof LoopStmt) {
                lLoopNextToInfStmt = (LoopStmt)lInfStmt.getNextStmt();
              }else if ((lInfStmt.getNextStmt() instanceof LabeledStmt)&&
                        (((LabeledStmt)lInfStmt.getNextStmt()).getStmt()
                            instanceof LoopStmt)){
                lLoopNextToInfStmt =
                  (LoopStmt)((LabeledStmt)lInfStmt.getNextStmt()).getStmt();
              }else {
                // Search the first LoopStmt placed after the InfStmt.
                boolean lInfAppeared = false;
                for (HirIterator lHirIt = hir.hirIterator(lInfStmt.getParent());
                  lHirIt.hasNextStmt(); ) {
                  Stmt lBrotherStmt = lHirIt.getNextStmt();
                  if ((fDbgLevel > 2)&&lBrotherStmt != null)
                    ioRoot.dbgHir.print(6, " " + lBrotherStmt.toStringShort());
                  if (lBrotherStmt == lInfStmt)
                    lInfAppeared = true;
                  else if (lInfAppeared &&
                           (lBrotherStmt instanceof LoopStmt)) {
                    lLoopNextToInfStmt = (LoopStmt)lBrotherStmt;
                    break;
                  }
                }
              }
              if (lLoopNextToInfStmt == null) {
                ioRoot.msgRecovered.put("Loop statement is not found after "
                  + lInfStmt.toStringWithChildren());
                continue;
              }
              if (fDbgLevel > 2)
                ioRoot.dbgHir.print(4, "Parallelize",
                  lLoopNextToInfStmt.toStringShort());
              LoopTable lLoopTableOfLoopStmt =
                (LoopTable)fLoopStmtToLoopTable.get(lLoopNextToInfStmt);
              if ((lLoopTableOfLoopStmt != null)&&  //##72
                  (lLoopTableOfLoopStmt.getParaFlag()||
                  (lOptionName == "forceDoAll"))) {
                // If the LoopTable has parallel flag or
                // forceParallel is specified, chose it
                // as a loop to be parallelized.
                fLoopsToBeParallelized.add(lLoopNextToInfStmt);
                if (lOptionName == "forceDoAll") {
                  //-- "forceDoAll" is specified.
                  // Override the LoopTable with the one
                  // generated according to the specifications
                  // given in the InfStmt (#pragma).
                  LoopTable lNewLoopTable = new LoopTable(
                    (ForStmt)lLoopNextToInfStmt, subpFlow);
                  for (Iterator lInfIt1 = lOptionList.iterator();
                    lInfIt1.hasNext(); ) {
                    Object lItem = lInfIt1.next();
                    if (fDbgLevel > 0) {
                      ioRoot.dbgHir.print(3, "\n option item " + lItem
                        + " " + lItem.getClass() + " " + lObject);
                      if (lItem instanceof IrList)
                        ioRoot.dbgHir.print(3, " elem " + ((IrList)lItem).get(0));
                    }
                    if ((lItem instanceof IrList)&&
                        (((IrList)lItem).get(0) instanceof String)) {
                      IrList lItemList = (IrList)lItem;
                      String lItemKind = ((String)lItemList.get(0)).intern();
                      ioRoot.dbgHir.print(3, "\n item kind " + lItemKind );
                      if ((lItemKind == "private")||
                          (lItemKind == "lastPrivate")) {
                        //   (private x1 x2 ... ))
                        //   (lastPrivate y1 y2 ...)
                        Set lVarNodes = new HashSet();
                        for (int lIndex1 = 1; lIndex1 < lItemList.size();
                             lIndex1++) {
                          Object lSubItem1 = lItemList.get(lIndex1);
                          ioRoot.dbgHir.print(3, "\n subItem " + lSubItem1 + " " + lSubItem1.getClass() );
                          if (lSubItem1 instanceof Var) {
                            lVarNodes.add(hir.varNode((Var)lSubItem1));
                          }else  {
                            lUnexpectedItem = lSubItem1;
                            continue;
                          }
                        }
                        if (lItemKind == "private") {
                          lNewLoopTable.Private = lVarNodes;
                        }else {
                          lNewLoopTable.LastPrivate = lVarNodes;
                        }
                      }else if (lItemKind == "reduction") {
                        // (reduction (op1 z1 v1) (op2 z2 v2) ...)
                        // where, op1, op2, ... are one of
                        // "+", "*", "-", "max", "min", "maxIndex", "minIndex"
                        // z1, z2, ... are reduction variables,
                        // v1, v2, ... are arrays whose indexes are to be
                        // searched if op1, op2, ... are minIndex or maxIndex.
                        LinkedList lRedAddList = new LinkedList();
                        LinkedList lRedMulList = new LinkedList();
                        LinkedList lRedSubList = new LinkedList();
                        LinkedList lRedMaxList = new LinkedList();
                        LinkedList lRedMinList = new LinkedList();
                        LinkedList lRedMaxIndexList = new LinkedList();
                        LinkedList lRedMinIndexList = new LinkedList();
                        for (int lIndex2 = 1; lIndex2 < lItemList.size();
                             lIndex2++) {
                          int lReductionOperator;
                          String lOperatorName = "";
                          Var lReductionVar = null;
                          Exp lArrayExp = null;
                          Object lSubItem2 = lItemList.get(lIndex2);
                          if (lSubItem2 instanceof IrList) {
                            ioRoot.dbgHir.print(3, " list size " +
                              ((IrList)lSubItem2).size());
                            // Get reduction descriptor.
                            boolean lSingleItem = false;
                            IrList lReductionDescriptor;
                            for (Iterator lItemIt2 = ((IrList)lSubItem2).iterator();
                                 lItemIt2.hasNext(); ) {
                              Object lSubItem3 = lItemIt2.next();
                              ioRoot.dbgHir.print(3, "\n subItem3 " + lSubItem3
                                + " " + lSubItem3.getClass());
                              if (lSubItem3 instanceof StringConst) {
                                ioRoot.dbgHir.print(3,
                                  " There is only one reduction item of size "
                                  + ((IrList)lSubItem2).size());
                                lReductionDescriptor = (IrList)lSubItem2;
                                lSingleItem = true;
                              }else if (lSubItem3 instanceof IrList){
                                // There are several reduction descriptors.
                                lReductionDescriptor = (IrList)lSubItem3;
                              }else {
                                if (! lSingleItem)
                                  lUnexpectedItem = lSubItem3;
                                continue;
                              }
                              // Process the reduction descriptor.
                              if (! (lReductionDescriptor.get(0) instanceof StringConst)) {
                                lUnexpectedItem = lReductionDescriptor.get(0);
                                continue;
                              }
                              lOperatorName =
                                ((StringConst)lReductionDescriptor.get(0)).
                                getStringBody().intern();
                              ioRoot.dbgHir.print(3, "\n operatorName " + lOperatorName);
                              if (lOperatorName == "+")
                                lReductionOperator = HIR.OP_ADD;
                              else if (lOperatorName == "*")
                                lReductionOperator = HIR.OP_MULT;
                              else if (lOperatorName == "-")
                                lReductionOperator = HIR.OP_SUB;
                              else
                                lReductionOperator = HIR.OP_NULL;
                              if (! ((lReductionDescriptor.get(1) instanceof Var))) {
                                lUnexpectedItem = lReductionDescriptor.get(1);
                                continue;
                              }
                              lReductionVar = (Var)lReductionDescriptor.get(1);
                              ioRoot.dbgHir.print(3, " ReductionVar " + lReductionVar);
                              if (lReductionDescriptor.size() > 2) {
                                if (lReductionDescriptor.get(2) instanceof Var) {
                                  lArrayExp =
                                    hir.varNode((Var)lReductionDescriptor.get(2));
                                  ioRoot.dbgHir.print(3, " arrayExp " +
                                    lArrayExp.toString());
                                }else {
                                  lUnexpectedItem = lReductionDescriptor.get(2);
                                  continue;
                                }
                              }
                              Reduction lNewReduction;
                              if (lReductionOperator == HIR.OP_NULL) {
                                lNewReduction = new Reduction(null,
                                  hir.varNode(lReductionVar),
                                  hir.varNode(lReductionVar),
                                  lOperatorName, lArrayExp);
                              }else {
                                lNewReduction = new Reduction(null,
                                  hir.varNode(lReductionVar),
                                  hir.varNode(lReductionVar),
                                  lReductionOperator);
                              }
                              if (lReductionOperator == HIR.OP_ADD)
                                lRedAddList.add(lNewReduction);
                              else if (lReductionOperator == HIR.OP_MULT)
                                lRedMulList.add(lNewReduction);
                              else if (lReductionOperator == HIR.OP_SUB)
                                lRedSubList.add(lNewReduction);
                              else if (lOperatorName == "max")
                                lRedMaxList.add(lNewReduction);
                              else if (lOperatorName == "max")
                                lRedMaxList.add(lNewReduction);
                              else if (lOperatorName == "min")
                                lRedMinList.add(lNewReduction);
                              else if (lOperatorName == "maxIndex")
                                lRedMaxIndexList.add(lNewReduction);
                              else if (lOperatorName == "minIndex")
                                lRedMinIndexList.add(lNewReduction);
                              if (lSubItem3 instanceof String)
                                break; // There is only one reduction descriptor.
                            }
                          }else  {
                            lUnexpectedItem = lSubItem2;
                            continue;
                          }
                        } // End of reduction list
                        lNewLoopTable.ReductionADDList = lRedAddList;
                        lNewLoopTable.ReductionMULList = lRedMulList;
                        lNewLoopTable.ReductionSUBList = lRedSubList;
                        lNewLoopTable.ReductionMAXList = lRedMaxList;
                        lNewLoopTable.ReductionMINList = lRedMinList;
                        lNewLoopTable.ReductionMAXINDEXList = lRedMaxIndexList;
                        lNewLoopTable.ReductionMININDEXList = lRedMinIndexList;
                      } // End of reduction
                    }else {
                      if ((! (lItem instanceof String))||
                          (((String)lItem).intern() != "forceDoAll"))
                        lUnexpectedItem = lItem;
                      continue;
                    }
                  } // End of parameter list for forceParallel
                  // Replace LoopTable by the one generated
                  // from pragma specifications.
                  if (fDbgLevel > 1) {
                    ioRoot.dbgHir.print(2, "\nNew LoopTable ");
                    lNewLoopTable.print(1);
                  }
                  fLoopStmtToLoopTable.put(lLoopNextToInfStmt, lNewLoopTable);
                } // End of forceParallel
              } // End of paraFlag or forceParallel
            }
            if (lUnexpectedItem != null) {
              ioRoot.msgRecovered.put(
                "Unexpected item in doAll/forceDoAll " +
                lUnexpectedItem.toString() + " "
                + lUnexpectedItem.getClass());
            }
          } // End of doAll
        } // End of "parallel"
      } // End of InfStmt processing
    } // End of Stmt iterator

    // printList(LoopInfoList, "LoopInfoList"); //###
    //-- Set up SymTable pointers to prepare for HIR modification.
    fSymTableCurrent = lSubpDefinition.getSymTable();
    symRoot.symTableCurrent = fSymTableCurrent;
    symRoot.symTableCurrentSubp = fSymTableCurrent;
    LoopTable lTable;
    boolean lChanged = false;
    for (Iterator lLoopIt = fLoopsToBeParallelized.iterator();
         lLoopIt.hasNext(); ) {
      LoopStmt lLoopStmt = (LoopStmt)lLoopIt.next();
      lTable = (LoopTable)fLoopStmtToLoopTable.get(lLoopStmt);
      ioRoot.dbgHir.print(3, "Loop to be parallelized", lTable.LoopStmt.toStringShort());
      if (ioRoot.dbgHir.getLevel() >= 3) {
        printLoopTable(lTable);
      }
      changeLoopToSubprogram(lTable);
      lChanged = true;
    } // End of For-Loop
    return lChanged;
  } // reformHirToParallelize

  /**
   * Change loop indicated by pTable to a subprogram
   * to be executed in parallel.
   * @param pTable Loop table of the loop to be parallelized.
   */
  protected void
    changeLoopToSubprogram(LoopTable pTable)
  {
    Subp lSubp = subpFlow.getSubpSym();
    ForStmt lLoopStmt = pTable.LoopStmt; // Loop statement of the loop.
    BBlock lStartBlock = subpFlow.getBBlockOfIR(lLoopStmt.getIndex());
    BBlock lEndBlock = subpFlow.getBBlockOfIR(
    lLoopStmt.getLoopEndPart().getIndex());
    //##71 BEGIN
    BBlock lLoopExitBlock = subpFlow.getBBlockOfIR(
       lLoopStmt.getLoopStartCondition().getIndex());
    if (lLoopExitBlock == null) {
      lLoopExitBlock = subpFlow.getBBlockOfIR(
        lLoopStmt.getLoopBackPoint().getIndex());
    }
    //##71 END
    ioRoot.dbgHir.print(3, "changeLoopToSubprogram", lLoopStmt.toStringShort()
      + " StartBlock " + lStartBlock + " endBlock " + lLoopExitBlock
      + " endBlock " + lEndBlock); //##71
    List lInLoopVarList = new ArrayList(); // Variables appeared in the loop.
    List lInLoopParam = new ArrayList(); // Parameters appeaered in the loop.
    Set lPrivate = varNodeToVarSet(pTable.Private); // Private variables.
    Set lLastPrivate = varNodeToVarSet(pTable.LastPrivate); // Last-private variables.
    Set lReductions = new HashSet(); // Reduction variables.
    Set lReductionsToInitiate = new HashSet(); // Set of reduction variables
      // requiring initiation (variables specified in
      //   ReductionMAXList, ReductionMINList,
      //   ReductionMAXINDEXList, ReductionMININDEXList) //##71
    List lAlreadyGlobal = new ArrayList(); // Global variables.
    List lChangeToLocal = new ArrayList(); // Variables changed to local.
    // List lPassAsParam = new ArrayList(); // Formal parameters are passed as parameter.
    List lChangeToGlobal = new ArrayList(); // Change parameters to global.
    List lPassPointer = new ArrayList(); // Variables to be written back.
    // Receive values of last private variables and reductions.
    // Reductions are added later as tail elements.
    Set lInductionVars = new HashSet(); // Set of induction variables of the loop. //##71
    // List lInLoopSubp = new ArrayList();
    // List lAggregateVars = new ArrayList();
    //-- Pick up all variables appeared in the loop.
    for (HirIterator lIt1 = hir.hirIterator(lLoopStmt);
         lIt1.hasNext(); ) {
      HIR lHir = lIt1.next();
      if (lHir instanceof VarNode) {
        Var lVar = (Var)((VarNode)lHir).getSymNodeSym();
        if (!lInLoopVarList.contains(lVar)) {
          lInLoopVarList.add(lVar);
        }
      }
    }
    lReductionsToInitiate.addAll(getReductionVar(pTable.ReductionMAXList)); //##71
    lReductionsToInitiate.addAll(getReductionVar(pTable.ReductionMINList)); //##71
    lReductionsToInitiate.addAll(getReductionVar(pTable.ReductionMAXINDEXList)); //##71
    lReductionsToInitiate.addAll(getReductionVar(pTable.ReductionMININDEXList)); //##71
    lReductions.addAll(getReductionVar(pTable.ReductionADDList));
    lReductions.addAll(getReductionVar(pTable.ReductionSUBList));
    lReductions.addAll(getReductionVar(pTable.ReductionMULList));
    //##71 lReductions.addAll(getReductionVar(pTable.ReductionMAXList));
    //##71 lReductions.addAll(getReductionVar(pTable.ReductionMINList));
    //##71 lReductions.addAll(getReductionVar(pTable.ReductionMAXINDEXList));
    //##71 lReductions.addAll(getReductionVar(pTable.ReductionMININDEXList));
    lReductions.addAll(lReductionsToInitiate); //##71
    FlowAnalSymVector lLiveIn = lStartBlock.getPLiveIn();
    //##71 FlowAnalSymVector lLiveOut = lEndBlock.getPLiveOut();
    //##71 BEGIN
    FlowAnalSymVector lLiveOutFromExit = lLoopExitBlock.getPLiveOut();
    FlowAnalSymVector lLiveInToEnd = lEndBlock.getPLiveIn();
    FlowAnalSymVector lLiveOutFromEnd = lEndBlock.getPLiveOut();
    FlowAnalSymVector lExposedAtEnd = lEndBlock.getPExposed();
    FlowAnalSymVector lLiveOut = subpFlow.flowAnalSymVector();
    lLiveOutFromExit.vectorAnd(lLiveInToEnd, lLiveOut);
    if (fDbgLevel > 3) {
      printSet(lLiveOutFromExit.flowAnalSyms(), "LiveOutFromExit");
      printSet(lLiveInToEnd.flowAnalSyms(), "LiveInToEnd");
      printSet(lExposedAtEnd.flowAnalSyms(), "ExposedAtEnd");
      printSet(lLiveOutFromEnd.flowAnalSyms(), "LiveOutFromEnd");
      printSet(lLiveOut.flowAnalSyms(), "LiveOut");
    }
    //##71 END
    // FlowAnalSymVector lUsed = subpFlow.flowAnalSymVector();
    FlowAnalSymVector lDefined = subpFlow.flowAnalSymVector();
    Set lLiveInVars = lLiveIn.flowAnalSyms();
    Set lLiveOutVars = lLiveOut.flowAnalSyms();
    Set lCopyToChild = new HashSet();
    Set lCopyBack = new HashSet();
    LinkedList lBBlockList = pTable.BBlockList;
    for (Iterator lIt2 = lBBlockList.iterator();
         lIt2.hasNext(); ) {
      BBlock lBBlock = (BBlock)lIt2.next();
      // lUsed.vectorOr(lBBlock.getPUsed(), lUsed);
      lDefined.vectorOr(lBBlock.getPDefined(), lDefined);
    }
    // Set lUsedSyms = lUsed.flowAnalSyms();
    Set lDefinedVars = lDefined.flowAnalSyms();
    for (Iterator lIt3 = lSubp.getParamList().iterator();
         lIt3.hasNext(); ) {
      Sym lParam = (Sym)lIt3.next();
      if (lInLoopVarList.contains(lParam)) {
        lInLoopParam.add(lParam);
        lChangeToGlobal.add(lParam);
        lCopyToChild.add(lParam); //##71
      }
    }
    //##71 BEGIN
    //-- Get the set of induction variables.
    for (Iterator lIt4 = pTable.IndList.iterator();
         lIt4.hasNext(); ) {
      BasicInduction lBasicInduction = (BasicInduction)lIt4.next();
      VarNode lVarNode = lBasicInduction.getVarNode();
      Var lIndVar = (Var)lVarNode.getSymNodeSym();
      lInductionVars.add(lIndVar);
    }
    //##71 END
    //-- Process variables appeared in the loop.
    for (Iterator lIt4 = lInLoopVarList.iterator();
         lIt4.hasNext(); ) {
      Sym lSym = (Sym)lIt4.next();
      if (lSym.isGlobal()) {
        lAlreadyGlobal.add(lSym);
      }
      if (lPrivate.contains(lSym)) {
        if (lSym.isGlobal()) {
          // Change to local so as to be private.
          if (! lChangeToLocal.contains(lSym))
            lChangeToLocal.add(lSym);
        }
        // If private variable is local, leave as local (do nothing).
      } else if (lLastPrivate.contains(lSym)) {
        if (lSym.isGlobal()) {
          // Change to local so as to be last private.
          if (! lChangeToLocal.contains(lSym))
            lChangeToLocal.add(lSym);
        }
        // Master thread coveres the last iteration so that
        // write back operation is not required for
        // last private variable.
      } else if (lReductions.contains(lSym)) {
        if (lSym.isGlobal()) {
          // Treat the reduction variable as local.
          if (! lChangeToLocal.contains(lSym))
            lChangeToLocal.add(lSym);
        }
        // Add pointer for the reduction as tail element later.
      }else {
        if (lSym.isGlobal()) {
          // Other global variables are not changed
        }else {
          // Other local variable.
          if (lLiveInVars.contains(lSym)&&
              (! lDefinedVars.contains(lSym))) {
            // Local variable refered and not defined.
            // Change it to global.
            if (! lChangeToGlobal.contains(lSym)) {
              lChangeToGlobal.add(lSym);
              lCopyToChild.add(lSym); //##71
            }
            if (fStackInLocalMemory) {
              lCopyToChild.add(lSym);
            }
          }else if (lLiveOutVars.contains(lSym) &&
                    lDefinedVars.contains(lSym)) {
             // It is defined in the loop and live-out from
             // the loop. Change it to global
             if (! lChangeToGlobal.contains(lSym)) {
               lChangeToGlobal.add(lSym);
               lCopyToChild.add(lSym); //##71
             }
            if (fStackInLocalMemory) {
              lCopyBack.add(lSym);
            }
          }
        }
      }
    } // End of lInLoopVarList iteration
    //-- Pass pointers that point to reduction variable receiver.
    for (Iterator lItR3 = lReductions.iterator();
         lItR3.hasNext(); ) {
      Var lRedVar3 = (Var)lItR3.next();
      lPassPointer.add(lRedVar3);
    }
    if ((fDbgLevel > 2) && (ioRoot.dbgHir.getLevel() > 0)) {
      printLoopTable(pTable);
      printList(lInLoopVarList, "InLoopVarList");
      printSet(lDefinedVars, "Defined");
      printSet(lCopyToChild, "CopyToChild");
      printSet(lLiveOutVars, "LiveOut");
      printSet(lCopyBack, "CopyBack");
      printSet(lInductionVars, "InductionVarriables");
      printSet(lReductions, "Reductions");
      printSet(lReductionsToInitiate, "ReductionsToInitiate"); //##71
      printList(lChangeToGlobal, "ChangeToGlobal");
      printList(lChangeToLocal, "ChangeToLocal");
      printList(lPassPointer, "PassPointer");
    }
    SubpDefinition lSubpDef =
      makeSubpDefinition(pTable,
      lInLoopVarList, lChangeToLocal, lChangeToGlobal,
      lInductionVars, lReductions, lReductionsToInitiate,
      lPassPointer, lCopyToChild, lCopyBack);
  } // changeLoopToSubprogram

  /**
   * Make subprogram definition corresponding to the loop
   * represented by pTable.
   * @param pTable Loop table of the loop.
   * @param pParamList list of formal parameters
   *   appering in the loop.
   * @param pInLoopVarList list of variables appering in the loop.
   * @param pChangeToLocal variables to be changed from global to local.
   * @param pChangeToGlobal variables to be changed from local to global.
   * @param pInductionVars set of induction variables in the loop.
   * @param pReductionVarSet set of reduction variables in the loop.
   * @param pReductionsToInitiate set of reduction variables
   *        to be initialized in the generated subprgoram.
   * @param pPassPointer set of variables passing write back receiver
   *        including reductions.
   * @param pCopyToChild set of variables to be copied from parent
   *        to child (neither last private nore reduction).
   * @param pCopyBack set of variabled to be copied from child
   *        to parent (neither last private nore reduction).
   * @return SubpDefinition generated.
   */
  protected SubpDefinition
    makeSubpDefinition(LoopTable pTable,
    List pInLoopVarList, List pChangeToLocal,
    List pChangeToGlobal, Set pInductionVars,
    Set pReductionVarSet, Set pReductionsToInitiate,
    List pPassPointer, Set pCopyToChild, Set pCopyBack)
  {
    Subp lCaller = subpFlow.getSubpSym();
    SymTable lCallerSymTable = subpFlow.getSubpDefinition().getSymTable();
    //-- Make Subp symbol corresponding to the loop.
    SubpDefinition lSubpDef;
    Subp lCalleeSubp;
    String lNewSubpName = fSymTableCurrent.generateSymName(
      subpFlow.getSubpSym().getName() + "_loop");
    // Change SymTableCurrent
    symRoot.symTableCurrent = symRoot.symTableRoot;
    lCalleeSubp = sym.defineSubp(
      lNewSubpName, sym.pointerType(symRoot.typeVoid));
    lCalleeSubp.setFlag(Sym.FLAG_GENERATED_SYM, true);
    lCalleeSubp.setVisibility(Sym.SYM_PUBLIC);
    // Restore SymTableCurrent
    symRoot.symTableCurrent = lCaller.getSymTable();
    //-- Get information of the loop and
    //   prepare variables to hold information for generating subprogram.
    ForStmt lForStmt = pTable.LoopStmt;
    Set lLastPrivate = new HashSet(); // Last-private variables.
    // Set lArrayLastPrivate = varNodeToVarSet(pTable.ArrayLastPrivate);
    Set lPrivate = varNodeToVarSet(pTable.Private);
    List lReductionList = getReductionList(pTable);
    Set lVarsToBeReplaced = new HashSet();
    List lFormalParamList = new ArrayList();
    Map lWriteBackPointerParam = new HashMap(); // Map write-back variable
        // to corresponding pointer parameters.
    Map lOldSymToNewVarExp = new HashMap(); // Map old symbol
       // to new variable expression for replacement.
    lVarsToBeReplaced.addAll(pChangeToLocal);
    // Change last-private nodes in the loop table to Var symbols.
    lLastPrivate = varNodeToVarSet(pTable.LastPrivate);
    //##71 BEGIN
    Map lSetInitValue = new HashMap(); // Map of local variable
       // and corresponding global variable to be used for
       // setting initial value in such case as induction
       // variable to be initialized in the subprogram
       // corresponding to the loop.
    Set lVarsToInitiate = new HashSet();
    lVarsToInitiate.addAll(pInductionVars);
    lVarsToInitiate.removeAll(pReductionVarSet);
    lVarsToInitiate.addAll(pReductionsToInitiate);
    Var lPrimaryInductionVar = getPrimaryInductionVar(lForStmt);
    if (lPrimaryInductionVar != null)
      lVarsToInitiate.remove(lPrimaryInductionVar);
    for (Iterator lVarIt1 = lVarsToInitiate.iterator();
         lVarIt1.hasNext(); ) {
      Var lIndVar = (Var)lVarIt1.next();
      Var lGlobalToBeInitiated = defineVar("_"+lIndVar.getName()+"_global",
        lIndVar.getSymType(), symRoot.symTableRoot, null);
      lSetInitValue.put(lIndVar, lGlobalToBeInitiated);
    }
    //##71 END
    if (fDbgLevel > 0) {
      ioRoot.dbgHir.print(1, "makeSubpDefinition for", lForStmt.toStringShort());
      if (fDbgLevel > 2) {
        printList(pInLoopVarList, "InLoopVarList");
        printList(pChangeToLocal, "ChangeToLocal");
        printList(pChangeToGlobal, "ChangeToGlobal");
        printList(pPassPointer, "PassPointer");
        printSet(lPrivate, "Private");
        printSet(lLastPrivate, "LastPrivate");
        printSet(pInductionVars, "InductionVars");
        printSet(pReductionVarSet, "Reductions");
        printSet(pReductionsToInitiate, "ReductionsToInitiate");
        printSet(pCopyToChild, "CopyToChild");
        printSet(pCopyBack, "CopyBack");
        printSet(lVarsToInitiate, "VarsToInitiate"); //##71
        ioRoot.dbgHir.print(5, "\nSetInitValue map " + lSetInitValue);
      }
    }
    //-- Generate statements to prepare for parallelization
    //   corresponding to:
    // coinsThreadPreprocessForDoAllLoop(
    //    coinsNumberOfThreads,
    //    loopCount,
    //    _index_from,
    //    _index_to);
    BlockStmt lBlockForCallStmt = hir.blockStmt(null);
    VectorType lVectorOfDegree = sym.vectorType(
      symRoot.typeLong, fMaxDegreeOfParallel);
    Var lFromVect = defineVar("_index_from", lVectorOfDegree,
      lCallerSymTable, lCaller);
    Var lToVect = defineVar("_index_to", lVectorOfDegree,
      lCallerSymTable, lCaller);
    IrList lArgListOfPreprocess1 = hir.irList();
    lArgListOfPreprocess1.add(hir.varNode(fNumberOfThreads));
    // Loop start condition may be of the form
    //    _indexVar < n - _startValue
    // where _startValue is used for induction normalization.
    // To initialize _startValue, loop-initiation part should
    // be executed.
    Stmt lOrgInitPart = pTable.originalLoopInit;
    if (lOrgInitPart != null) {
      lOrgInitPart = (Stmt)lOrgInitPart.copyWithOperands();
      lBlockForCallStmt.addLastStmt(lOrgInitPart);
      ioRoot.dbgHir.print(2, " Add loopInitPart",
        lOrgInitPart.toStringWithChildren());
    }
    for (Iterator lListIt = pTable.addConditionPart.iterator();
         lListIt.hasNext(); ) {
      Stmt lPreStmt = (Stmt)((Stmt)lListIt.next()).copyWithOperandsChangingLabels(null);
      lBlockForCallStmt.addLastStmt(lPreStmt);
      ioRoot.dbgHir.print(2, " Add conditional part",
        lPreStmt.toStringWithChildren());
    }
    // Loop count expression can be get from 2nd operand of loop-start condition.
    Exp lLoopCountOfForStmt = (Exp)lForStmt.getLoopStartCondition().getChild2();
    Exp lLoopCountArg = (Exp)lLoopCountOfForStmt.copyWithOperands();
    if (lLoopCountArg.getType() != symRoot.typeLong) {
      lLoopCountArg = hir.convExp(symRoot.typeLong, lLoopCountArg);
    }
    lArgListOfPreprocess1.add(lLoopCountArg);
    lArgListOfPreprocess1.add(hir.decayExp(hir.varNode(lFromVect)));
    lArgListOfPreprocess1.add(hir.decayExp(hir.varNode(lToVect)));
    ExpStmt lPreprocessForDoAllLoop = hir.callStmt(
      hir.subpNode(fThreadPreprocessForDoAllLoop),
      lArgListOfPreprocess1);
    lBlockForCallStmt.addLastStmt(lPreprocessForDoAllLoop);
    // Generate
    //   for (_I = 0; _I < fNumberOfThreads-1; _I = _I + 1) { }
    // The master thread is treated as the last thread,
    // and so, _I < fNumberOfThreads-1; instead of
    //         _I < fNumberOfThreads-1;
    Var lI1 = defineVar("_I", symRoot.typeLong,
      lCallerSymTable, lCaller);
    BlockStmt lBlockInLoop1 = hir.blockStmt(null); // Block within ForStmt to be generated.
    ForStmt lForStmt1 = hir.forStmt(
      hir.assignStmt(hir.varNode(lI1), hir.intConstNode(0)),
      hir.exp(HIR.OP_CMP_LT, hir.varNode(lI1),
              hir.exp(HIR.OP_SUB,
                hir.varNode(fNumberOfThreads),
                hir.intConstNode(1))),
      lBlockInLoop1,
      hir.assignStmt(hir.varNode(lI1),
        hir.exp(HIR.OP_ADD, hir.varNode(lI1), hir.intConstNode(1))));
    // Generate
    //   coinsThreadForkForDoAll(
    //     &coinsThread[_I], SubpCorrespondingToTheLoop,
    //     coinsThreadIdArray[_I],
    //     _index_from[_I],
    //     _index_to[_I],
    //     address to receive write back values);
    // and add it to lBlockInLoop1.
    BlockStmt lChangeToGlobalBlock = hir.blockStmt(null); // Block
        // that contains statements to set parameters in global area.
    IrList lArgListOfFork = hir.irList();
    lArgListOfFork.add(
      hir.addrExp(hir.subscriptedExp(hir.varNode(fThreadDescriptor),
        hir.varNode(lI1))));
    lArgListOfFork.add(hir.subpNode(lCalleeSubp));
    lArgListOfFork.add(
      hir.subscriptedExp(hir.varNode(fThreadIdArray),
        hir.varNode(lI1)));
    lArgListOfFork.add(hir.subscriptedExp(hir.varNode(lFromVect),
      hir.varNode(lI1)));
    lArgListOfFork.add(hir.subscriptedExp(hir.varNode(lToVect),
      hir.varNode(lI1)));

    // Process pChangeToGlobal.
    // Parameters are read only and they are changed to global.
    // Local variables refered without defining are changed to global.
    for (Iterator lItVar1 = pChangeToGlobal.iterator();
         lItVar1.hasNext(); ) {
      Var lReferedVar = (Var)lItVar1.next();
      Var lGlobalToBePassed;
      Type lGlobalType = lReferedVar.getSymType();
      ioRoot.dbgHir.print(4, "\n Change to global " + lReferedVar.getName()
        + " type " + lGlobalType); //##71
      if (lGlobalType instanceof VectorType) {
        // If vector, pass its element pointer as global variable.
        lGlobalType = sym.pointerType(((VectorType)lGlobalType).getElemType(),
            ((VectorType)lGlobalType).getElemCount());
      }
      lGlobalToBePassed = defineVar("_"+lReferedVar.getName()+"_global",
        lGlobalType, symRoot.symTableRoot, null);
      ioRoot.dbgHir.print(4, " generated Var " + lGlobalToBePassed.getName()
        + " type " + lGlobalType); //##71
      AssignStmt lSetToGlobal;
      if (lReferedVar.getSymType() instanceof VectorType) {
        if (pCopyToChild.contains(lReferedVar)) {
          // Copy the vector.
          //## REFINE
          //##71 BEGIN
          lOldSymToNewVarExp.put(lReferedVar,
            hir.undecayExp(hir.varNode(lGlobalToBePassed),
            ((VectorType)lReferedVar.getSymType()).getElemCount()));
          lSetToGlobal = hir.assignStmt(
            hir.varNode(lGlobalToBePassed),
            hir.decayExp(hir.varNode(lReferedVar)));
          // else if (pointer cannot be used) copy the vector.
          ioRoot.dbgHir.print(4, "\n set global pointer " + lSetToGlobal.toStringWithChildren()); //##71
          lChangeToGlobalBlock.addLastStmt(lSetToGlobal);
          //##71
        }else {
          // if (pointer can be used) change to pointer expression.
          lOldSymToNewVarExp.put(lReferedVar,
            hir.undecayExp(hir.varNode(lGlobalToBePassed),
            ((VectorType)lReferedVar.getSymType()).getElemCount()));
          lSetToGlobal = hir.assignStmt(
            hir.varNode(lGlobalToBePassed),
            hir.decayExp(hir.varNode(lReferedVar)));
          // else if (pointer cannot be used) copy the vector.
          ioRoot.dbgHir.print(4, "\n set global " + lSetToGlobal.toStringWithChildren()); //##71
          lChangeToGlobalBlock.addLastStmt(lSetToGlobal);
        }
      }else if ((lReferedVar.getSymType() instanceof PointerType)&&
                ((PointerType)lReferedVar.getSymType()).isDeclaredAsArray()) {
        lOldSymToNewVarExp.put(lReferedVar, hir.varNode(lGlobalToBePassed));
        ioRoot.dbgHir.print(4, " pointerDeclaredAsArray " +
          lReferedVar + " " + lGlobalToBePassed); //##71
        lSetToGlobal = hir.assignStmt(
          hir.varNode(lGlobalToBePassed),
          hir.varNode(lReferedVar));
        ioRoot.dbgHir.print(4, "\n set pointer value " + lSetToGlobal.toStringWithChildren()); //##71
        lChangeToGlobalBlock.addLastStmt(lSetToGlobal);
      }else {
        // Scalar variable. Copy its value.
        lOldSymToNewVarExp.put(lReferedVar, hir.varNode(lGlobalToBePassed));
        if (pCopyToChild.contains(lReferedVar)) {
          lSetToGlobal = hir.assignStmt(
            hir.varNode(lGlobalToBePassed),
            hir.varNode(lReferedVar));
          ioRoot.dbgHir.print(4, "\n set value " + lSetToGlobal.toStringWithChildren()); //##71
          lChangeToGlobalBlock.addLastStmt(lSetToGlobal);
        }
      }
    }
    //##71 BEGIN
    // Set initial value to global variables used to pass
    // value to the subprogram corresponding to the loop
    // This is required to avoid using variable number of
    // parameters in the subprogram generated. (Variable number
    // of parameters make the subprogram complicated.)
    for (Iterator lVarIt3 = lVarsToInitiate.iterator();
         lVarIt3.hasNext(); ) {
      Var lVarToInit = (Var)lVarIt3.next();
      Var lGlobalVar = (Var)lSetInitValue.get(lVarToInit);
      AssignStmt lSetInitVal = hir.assignStmt(
          hir.varNode(lGlobalVar), hir.varNode(lVarToInit));
      lChangeToGlobalBlock.addLastStmt(lSetInitVal);
    }
    //##71 END
    if (lChangeToGlobalBlock.getFirstStmt() != null) {
      // There are variables to be changed to global or
      // to be initiated.
      ioRoot.dbgHir.print(4, "\n add lChangeToGlobalBlock " +
        lChangeToGlobalBlock.toStringWithChildren()); //##71
      //##71 lBlockInLoop1.addLastStmt(lChangeToGlobalBlock);
      lBlockForCallStmt.addLastStmt(lChangeToGlobalBlock); //##71
    }
    //-- Prepare variables to receive the values of reduction variable
    //   computed by threads and add its address to the argument list.
    Map lReductionReceiver = new HashMap(); // Map reduction variable
               // to its receiver variable.
    int lReductionCount = 0;
    for (Iterator lItR1 = pReductionVarSet.iterator();
         lItR1.hasNext(); ) {
      Var lRedVar = (Var)lItR1.next();
      VectorType lVectorType = sym.vectorType(
        lRedVar.getSymType(), fMaxDegreeOfParallel);
      Var lReceiverVar = defineVar("_"+lRedVar.getName()+"_receiver",
        lVectorType, lCallerSymTable, lCaller);
      lReductionReceiver.put(lRedVar, lReceiverVar);
      lArgListOfFork.add(hir.addrExp(hir.subscriptedExp(
        hir.varNode(lReceiverVar), hir.varNode(lI1))));
      lReductionCount++;
    }
    //-- Add null parameters up to fMaximumNumberOfReductions.
    for (int lRCount = lReductionCount; lRCount < fMaximumNumberOfReductions;
         lRCount++) {
      ConstNode lNullNode = hir.intConstNode(0);
      lArgListOfFork.add(lNullNode);
    }
    // Generate the statement to fork and
    // add it to the body of the loop.
    ExpStmt lForkStmt = hir.callStmt(
      hir.subpNode(fThreadForkForDoAll),lArgListOfFork);
    lBlockInLoop1.addLastStmt(lForkStmt);
    // Add the generated ForStmt that does fork operation.
    lBlockForCallStmt.addLastStmt(lForStmt1);
    //---- Generate the loop to be executed by the master thread.
    //-- Get the basic induction variable of the loop.
    /* //##71
    // Var lInductionVar = null;
    // Stmt lLoopInitPart = lForStmt.getLoopInitPart();
    // Stmt lLoopStepStmt = lForStmt.getLoopStepPart();
    if (fDbgLevel > 1) {
      ioRoot.dbgHir.print(2, " loopInitPart",
        lLoopInitPart.toStringWithChildren().toString());
      ioRoot.dbgHir.print(2, " loopStepPart",
        lLoopStepStmt.toStringWithChildren().toString());
    }
    if (lLoopInitPart instanceof BlockStmt) {
      lLoopInitPart = ((BlockStmt)lLoopInitPart).getFirstStmt();
    }
    if (lLoopStepStmt instanceof LabeledStmt) {
      lLoopStepStmt = ((LabeledStmt)lLoopStepStmt).getStmt();
    }
    if (lLoopStepStmt instanceof BlockStmt) {
      lLoopStepStmt = ((BlockStmt)lLoopStepStmt).getFirstStmt();
    }
    if (lLoopInitPart instanceof AssignStmt) {
      if (lLoopInitPart.getChild1()instanceof VarNode) {
        if (lLoopStepStmt instanceof AssignStmt) {
          if (lLoopStepStmt.getChild1()instanceof VarNode) {
            lInductionVar
              = (Var)((VarNode)lLoopInitPart.getChild1()).getSymNodeSym();
          }
        }
      }
    }
    */ //##71
    if (lPrimaryInductionVar == null) {
      ioRoot.msgNote.put("Induction variable not found in " +
        lForStmt.toStringShort());
      return null;
    }
    // Make the copy of the loop statement of the form:
    //   for (lInductionVar = _index_from[fNumberOfThread-1];
    //        lInductionVar < _index_to[fNumberOfThread-1];
    //        lIndectionVar = lInductionVar+1) { }
    ForStmt lCopiedForStmtForMaster
      = (ForStmt)lForStmt.copyWithOperandsChangingLabels(null);
    Stmt lLoopBodyForMasterThread;
    if (lCopiedForStmtForMaster.getLoopBodyPart()instanceof LabeledStmt) {
      lLoopBodyForMasterThread = ((LabeledStmt)lCopiedForStmtForMaster.
        getLoopBodyPart()).getStmt();
    } else {
      lLoopBodyForMasterThread = lCopiedForStmtForMaster.getLoopBodyPart();
    }
    lLoopBodyForMasterThread.cutParentLink();
    ForStmt lForStmtForMasterThread = hir.forStmt(
      hir.assignStmt(hir.varNode(lPrimaryInductionVar),
        hir.subscriptedExp(hir.varNode(lFromVect),
          hir.exp(HIR.OP_SUB,
            hir.varNode(fNumberOfThreads),
            hir.intConstNode(1)))),
      hir.exp(HIR.OP_CMP_LT,
        hir.varNode(lPrimaryInductionVar),
        hir.subscriptedExp(hir.varNode(lToVect),
          hir.exp(HIR.OP_SUB,
            hir.varNode(fNumberOfThreads),
            hir.intConstNode(1)))),
      lLoopBodyForMasterThread,
      (Stmt)lCopiedForStmtForMaster.getLoopStepPart().copyWithOperands());
    // Copy InfList
    if (lForStmt.getInfList() != null) {
      lForStmtForMasterThread.copyInfListFrom(lForStmt);
    }
    // Add the generated for-statement.
    lBlockForCallStmt.addLastStmt(lForStmtForMasterThread);
    //-- Generate
    //    coinsThreadPostprocess(
    //        coinsNumberOfThreads, loopCount)
    //   and add it.
    IrList lArgListOfPostprocess = hir.irList();
    lArgListOfPostprocess.add(hir.varNode(fNumberOfThreads));
    lArgListOfPostprocess.add(lLoopCountOfForStmt.copyWithOperands());
    ExpStmt lPostprocessStmt = hir.callStmt(
      hir.subpNode(fThreadPostprocess),lArgListOfPostprocess);
    lBlockForCallStmt.addLastStmt(lPostprocessStmt);
    // Add reduction synthesizing statements later.
    if (fDbgLevel > 2) {
      ioRoot.dbgHir.print(2, "\nGenerated ForStmt \n");
      lBlockForCallStmt.print(2, false);
    }
    //-- Prepare for the subprogram to be generated.
    //   lCalleeSubp was already declared.
    // Change SymTableCurrent in order to push SymTable.
    symRoot.symTableCurrent = symRoot.symTableRoot;
    SymTable lNewSymTable = symRoot.symTableRoot.pushSymTable(lCalleeSubp);
    String lVarName, lParamName;
    Var lVar;
    Param lParam, lIndexFrom, lIndexTo, lThreadId;
    Type lParamType, lVarType, lPtrType;
    SubpType lSubpType;
    // to new symbol in the subprogram to be generated.
    BlockStmt lSubpBody = hir.blockStmt(null);
    //---- Make formal parameters and subprogram header.
    // Formal parameter list includes
    //   Index range (indexFrom, indexTo),
    //   Formal parameters appearing in the loop and
    //   reduction variable receivers.
    //-- Generate parameters that specify index range for the for-loop.
    lThreadId = defineParam("_threadId", symRoot.typeInt, lNewSymTable);
    lCalleeSubp.addParam(lThreadId);
    lIndexFrom = defineParam("_indexFrom", symRoot.typeLong, lNewSymTable);
    lCalleeSubp.addParam(lIndexFrom);
    lIndexTo = defineParam("_indexTo", symRoot.typeLong, lNewSymTable);
    lCalleeSubp.addParam(lIndexTo);
    //-- Add pointers to be passed (including reduction receivers).
    int lPointerParamCount = 0;
    for (Iterator lIt2 = pPassPointer.iterator();
         lIt2.hasNext(); ) {
      lVar = (Var)lIt2.next();
      lVarType = lVar.getSymType();
      lPtrType = sym.pointerType(lVarType, symRoot.symTableRoot);
      lParam = defineParam(lVar.getName(), lPtrType, lNewSymTable);
      lCalleeSubp.addParam(lParam);
      lWriteBackPointerParam.put(lVar, lParam);
      lFormalParamList.add(lParam);
      lPointerParamCount++;
    }
    // Add void pointers if lPointerParamCount < fMaximumNumberOfReductions.
    for (int lParamCount = lPointerParamCount;
         lParamCount < fMaximumNumberOfReductions; lParamCount++) {
      lParam = defineParam("_voidPtr",
        sym.pointerType(symRoot.typeVoid, symRoot.symTableRoot),
        lNewSymTable);
      lCalleeSubp.addParam(lParam);
    }
    lCalleeSubp.closeSubpHeader();
    //-- Make new variables corresponding to old variables.
    for (Iterator lIt2 = pInLoopVarList.iterator();
         lIt2.hasNext(); ) {
      lVar = (Var)lIt2.next();
      if (pChangeToGlobal.contains(lVar)) {
        // Already registered as one to be changed to global.
        continue;
      }
      if (lVar.isGlobal()) {
        if (pChangeToLocal.contains(lVar)) {
          // Change global to local
          lVarType = lVar.getSymType();
          Var lNewVar = defineVar(lVar.getName(), lVarType,
                                  lNewSymTable, lCalleeSubp);
          lOldSymToNewVarExp.put(lVar, hir.varNode(lNewVar));
          continue;
        } else {
          // Do not rewite global variable.
          continue;
        }
      } else {
        // Local variable.
        // Treat as new local variable in the generated subprogram.
        String lLocalVarName = lVar.getName();
        Var lLocalVar = sym.defineVar(lLocalVarName, lVar.getSymType());
        lOldSymToNewVarExp.put(lVar, hir.varNode(lLocalVar));
      }
      if (pReductionVarSet.contains(lVar)) {
        // If global, it is registered in pChangeToLocal.
        // If local, then treat it as local.
      }
    } // End of pInLoopVarList iteration.
    //-- Generate
    //     coinsThreadPreprocessForDoAllThread();
    ExpStmt lPreprocessForThread = hir.callStmt(
      hir.subpNode(fThreadPreprocessForDoAllThread),
      hir.irList());
    lSubpBody.addLastStmt(lPreprocessForThread);
    //-- Set initial value to reduction variables.
    for (Iterator lItR2 = pReductionVarSet.iterator();
         lItR2.hasNext(); ) {
      Var lRedVar2 = (Var)lItR2.next();
      //##74 Exp lInitValue = initialValueOfReduction(pTable, lRedVar2);
      //##74 BEGIN
      Exp lInitValue;
      if (pTable.getReductionVarSet(pTable.ReductionMAXINDEXList).contains(lRedVar2)||
          pTable.getReductionVarSet(pTable.ReductionMININDEXList).contains(lRedVar2)) {
        lInitValue = hir.varNode(lIndexFrom);
      }else {
        lInitValue = initialValueOfReduction(pTable, lRedVar2);
      }
      if (fDbgLevel > 3) {
        ioRoot.dbgHir.print(3, "\nReductionVar " + lRedVar2.getName()
          + " maxIndexList " + pTable.getReductionVarSet(pTable.ReductionMAXINDEXList)
          + " minIndexList " + pTable.getReductionVarSet(pTable.ReductionMININDEXList));
        ioRoot.dbgHir.print(3, "\n initial value " +
          lInitValue.toStringWithChildren());
      }
      //##74 END
      Exp lReductionVarExp = hir.varNode(lRedVar2);
      if (lOldSymToNewVarExp.containsKey(lRedVar2))
        lReductionVarExp = (Exp)((HIR)lOldSymToNewVarExp.get(lRedVar2)).copyWithOperands();
      AssignStmt lInitValAssign = hir.assignStmt(
        lReductionVarExp,
        lInitValue);
      lSubpBody.addLastStmt(lInitValAssign);
    }
    //##71 BEGIN
    for (Iterator lVarIt4 = lVarsToInitiate.iterator();
      lVarIt4.hasNext(); ) {
      Var lVarToInit2 = (Var)lVarIt4.next();
      if (pTable.getReductionVarSet(pTable.ReductionMAXINDEXList).contains(lVarToInit2)||
          pTable.getReductionVarSet(pTable.ReductionMININDEXList).contains(lVarToInit2)) {
        // Already initiated.
        continue;
      }
      Var lGlobalVar2 = (Var)lSetInitValue.get(lVarToInit2);
      VarNode lReceiver;
      if (lOldSymToNewVarExp.containsKey(lVarToInit2))
        lReceiver = (VarNode)((Exp)lOldSymToNewVarExp.
           get(lVarToInit2)).copyWithOperands();
      else
        lReceiver = hir.varNode(lVarToInit2);
      AssignStmt lSetInitVal2 = hir.assignStmt(
        lReceiver, hir.varNode(lGlobalVar2));
      lSubpBody.addLastStmt(lSetInitVal2);
    }
    //##71 END
    //---- Generate statements to be placed before for-statement.
    Stmt lInitPart = pTable.originalLoopInit;
    if (lInitPart != null) {
      lInitPart = (Stmt)lInitPart.copyWithOperands();
      lInitPart = (Stmt)rewriteVariables(lInitPart, lOldSymToNewVarExp);
      lSubpBody.addLastStmt(lInitPart);
    }
    for (Iterator lListIt = pTable.addConditionPart.iterator();
         lListIt.hasNext(); ) {
      Stmt lPreStmt = (Stmt)((Stmt)lListIt.next()).copyWithOperandsChangingLabels(null);
      lPreStmt = (Stmt)rewriteVariables(lPreStmt, lOldSymToNewVarExp);
      lSubpBody.addLastStmt(lPreStmt);
    }
    //---- Generate for-statement for the subprogram.
    // Make the copy of the loop statement.
    ForStmt lCopiedForStmt
      = (ForStmt)lForStmt.copyWithOperandsChangingLabels(null);
    // Change variables to corresponding parameter or new variable.
    lCopiedForStmt = (ForStmt)rewriteVariables(lCopiedForStmt, lOldSymToNewVarExp);
    // Get new induction variable.
    Var lNewInductionVar = lPrimaryInductionVar;
    if (lOldSymToNewVarExp.containsKey(lPrimaryInductionVar)) {
      lNewInductionVar = (Var)((HIR)lOldSymToNewVarExp.
        get(lPrimaryInductionVar)).getSym();
    }
    //-- Get the loop body of the loop and construct
    //   new loop statement using the new induction variable.
    Stmt lLoopBody;
    if (lCopiedForStmt.getLoopBodyPart()instanceof LabeledStmt) {
      lLoopBody = ((LabeledStmt)lCopiedForStmt.getLoopBodyPart()).getStmt();
    } else {
      lLoopBody = lCopiedForStmt.getLoopBodyPart();
    }
    lLoopBody.cutParentLink();
    ForStmt lNewForStmt = hir.forStmt(
      hir.assignStmt(hir.varNode(lNewInductionVar),
        hir.varNode(lIndexFrom)),
      hir.exp(HIR.OP_CMP_LT,
        hir.varNode(lNewInductionVar), hir.varNode(lIndexTo)),
      lLoopBody,
      // (Stmt)lCopiedForStmt.getLoopStepPart().copyWithOperands()
      hir.assignStmt(
        hir.varNode(lNewInductionVar),
        hir.exp(HIR.OP_ADD,
          hir.varNode(lNewInductionVar),hir.intConstNode(1)))
      );
    lSubpBody.addLastStmt(lNewForStmt);
    //---- Generate statements for write back operation. ----//
    /*
    //-- Write back last private variables.
    for (Iterator lIt6 = lLastPrivate.iterator();
         lIt6.hasNext(); ) {
      Stmt lWriteBackStmt = null;
      Var lLPVar = (Var)lIt6.next();
      // Get pointer parameter corresponding to the last private variable.
      Var lLPPtr = null;
      if (lWriteBackPointerParam.containsKey(lLPVar))
        lLPPtr = (Var)lWriteBackPointerParam.get(lLPVar);
      if ((lLPVar == null)||(lLPPtr == null)) {
        ioRoot.msgRecovered.put("Write back pointer not found for "
          + lLPVar + " " + lLPPtr);
        continue;
      }
      // Generate write back statements.
      Var lFromVar = lLPVar;
      if (lOldSymToNewSym.containsKey(lLPVar)) {
        lFromVar = (Var)lOldSymToNewSym.get(lLPVar);
      }
      Type lVarType1 = lLPVar.getSymType();
      if (lVarType1.isScalar()) {
        lWriteBackStmt = hir.assignStmt(
          hir.contentsExp(hir.varNode(lLPPtr)),
          hir.varNode(lFromVar));
      }else if (lVarType1 instanceof VectorType) {
        lWriteBackStmt = writeVector(lFromVar, lLPPtr, lNewSymTable);
        if (lWriteBackStmt == null) // Error was found.
          continue;  // Skip this variable.
      }
      lSubpBody.addLastStmt(lWriteBackStmt);
    } // End of last private loop
    */
    //-- Write back reduction variables.
    for (Iterator lItR2 = pReductionVarSet.iterator();
         lItR2.hasNext(); ) {
      Var lRedVar = (Var)lItR2.next();
      // Get pointer parameter corresponding to the reduction variable.
      Var lRedPtr = null;
      if (lWriteBackPointerParam.containsKey(lRedVar))
        lRedPtr = (Var)lWriteBackPointerParam.get(lRedVar);
      if (lRedPtr == null) {
        ioRoot.msgRecovered.put("Write back pointer not found for "
          + lRedVar);
        continue;
      }
      // Generate write back statements.
      Exp lFromVarExp = hir.varNode(lRedVar);
      if (lOldSymToNewVarExp.containsKey(lRedVar)) {
        lFromVarExp = (Exp)((HIR)lOldSymToNewVarExp.get(lRedVar)).copyWithOperands();
      }
      Type lVarTypeR = lRedVar.getSymType();
      Stmt lWriteBackRedStmt = hir.assignStmt(
        hir.contentsExp(hir.varNode(lRedPtr)),
        lFromVarExp);
      lSubpBody.addLastStmt(lWriteBackRedStmt);
    } // End of reduction loop
    //-- Generate
    //     coinsThreadPostprocessForDoAllThread();
    ExpStmt lPostprocessForThread = hir.callStmt(
      hir.subpNode(fThreadPostprocessForDoAllThread),
      hir.irList());
    lSubpBody.addLastStmt(lPostprocessForThread);

    //-- Construct a subprogram definition having the loop statement.
    lSubpBody.setSymTable(lNewSymTable); // This is required for hir2c transformation.
    lSubpDef = hir.subpDefinition(lCalleeSubp, lNewSymTable);
    lSubpDef.setHirBody(lSubpBody);
    lCalleeSubp.setSymTable(lNewSymTable);
    // Do not add lSubpDef to programRoot because it will cause
    // ConcurrentModificationException.
    // ((Program)hirRoot.programRoot).addSubpDefinition(lSubpDef);
    fSubpDefinitionList.add(lSubpDef);
    if (fDbgLevel >= 4) {
      lSubpDef.printHir("Generated subprogram");
    }
    // Restore SymTableCurrent
    lNewSymTable.popSymTable();
    symRoot.symTableCurrent = fSymTableCurrent;
    symRoot.symTableCurrentSubp = fSymTableCurrent;

    //---- Add write back operation if required.
    for (Iterator lItLiveOut = pCopyBack.iterator();
         lItLiveOut.hasNext(); ){
      Var lLiveOutVar = (Var)lItLiveOut.next();
      if (lOldSymToNewVarExp.containsKey(lLiveOutVar)) {
        Exp lVarExp = (Exp)((HIR)lOldSymToNewVarExp.get(lLiveOutVar)).
          copyWithOperands();
        // if scalar variable, make assign statement;
        AssignStmt lWriteBack1 = hir.assignStmt(
          hir.varNode(lLiveOutVar), lVarExp);
        lBlockForCallStmt.addLastStmt(lWriteBack1);
        // else if vaector then copy the vector.
        //## REFINE
      }
    }
    //---- Add reduction synthesizing statements.
    int lOperator;
    String lOperatorName = "";
    for (Iterator lItR5 = pReductionVarSet.iterator();
         lItR5.hasNext(); ) {
      Var lRedVar5 = (Var)lItR5.next();
      Var lReceiver5 = (Var)lReductionReceiver.get(lRedVar5);
      Reduction lReduction = getReductionForVar(lRedVar5, lReductionList);
      if (lReduction == null) {
        continue;
      }
      lOperator = lReduction.op;
      if (lOperator == HIR.OP_SUB) {
        // Result of subtract-reduction should be added.
        lOperator = HIR.OP_ADD;
      }
      lOperatorName = lReduction.opName;
      //-- Do synthesizing operation according to the operator.
      //-- Generate
      // for (_Ir = 0; _Ir < fNumberOfThreads-1; _Ir = _Ir + 1) {
      //   reductionVar = reductionVar
      //                  reductionOperator reductionReceiver[_Ir];
      // }
      Var lIndexRed5 = defineVar("_indexR", symRoot.typeInt,
        lCallerSymTable,lCaller);
      ForStmt lReductionSynthesis;
      BlockStmt lRedLoopBody = hir.blockStmt(null);
      if (lOperator != HIR.OP_NULL) { // ADD, MUL, SUB
         // reductionVar = reductionVar
        //                  reductionOperator reductionReceiver[_Ir];
        AssignStmt lReductionAssign = hir.assignStmt(
          hir.varNode(lRedVar5),
          hir.exp(lOperator,
                  hir.varNode(lRedVar5),
                  hir.subscriptedExp(hir.varNode(lReceiver5),
                    hir.varNode(lIndexRed5))));
        lRedLoopBody.addLastStmt(lReductionAssign);
      }else { // max, min, maxIndex, minIndex
        int lCmpOperator = HIR.OP_CMP_GT;
        if ((lOperatorName == "min")||(lOperatorName == "minIndex"))
          lCmpOperator = HIR.OP_CMP_LT;
        AssignStmt lReviseValue = hir.assignStmt(
            hir.varNode(lRedVar5),
            hir.subscriptedExp(hir.varNode(lReceiver5),
              hir.varNode(lIndexRed5)));
        Exp lCompareExp;
        if ((lOperatorName == "max")||(lOperatorName == "min")) {
          lCompareExp = hir.exp(
            lCmpOperator,
            hir.subscriptedExp(hir.varNode(lReceiver5),
              hir.varNode(lIndexRed5)),
            hir.varNode(lRedVar5));
        }else {
          if (lReduction.arrayExp != null) {
            Exp lArrayExp = (Exp)lReduction.arrayExp;
            lCompareExp = hir.exp(
              lCmpOperator,
              hir.subscriptedExp(
                (Exp)lArrayExp.copyWithOperands(),
                hir.subscriptedExp(hir.varNode(lReceiver5),
                    hir.varNode(lIndexRed5))),
              hir.subscriptedExp(
                (Exp)lArrayExp.copyWithOperands(),
                hir.varNode(lRedVar5)));
          }else {
            ioRoot.msgRecovered.put("Array expression is not given as reduction parameter");
            lCompareExp = hir.constNode(symRoot.boolConstFalse);
          }
        }
        IfStmt lIfStmt = hir.ifStmt(
          lCompareExp,
          lReviseValue,
          null);
        lRedLoopBody.addLastStmt(lIfStmt);
      } // End of max, min, maxIndex, minIndex
      // for (_Ir = lStartIndex; _Ir < fNumberOfThreads-1; _Ir = _Ir + 1)
      lReductionSynthesis = hir.forStmt(
        hir.assignStmt(hir.varNode(lIndexRed5),
          hir.intConstNode(0)),
        hir.exp(HIR.OP_CMP_LT,
          hir.varNode(lIndexRed5),
          hir.exp(HIR.OP_SUB,
            hir.varNode(fNumberOfThreads),
            hir.intConstNode(1))),
        lRedLoopBody,
        hir.assignStmt(hir.varNode(lIndexRed5),
          hir.exp(HIR.OP_ADD, hir.varNode(lIndexRed5),
                  hir.intConstNode(1))));
      lBlockForCallStmt.addLastStmt(lReductionSynthesis);
    } // End of lItRed5 (reduction index)
    if (fDbgLevel > 2) {
      ioRoot.dbgHir.print(2, "\nGenerated ForStmt including call\n");
      lBlockForCallStmt.print(2, false);
    }
    fStatementsToBeReplaced.put(lForStmt, lBlockForCallStmt);
    return lSubpDef;
  } // makeSubpDefinition

  /**
   * Rewrite variables in pNewHir according to the map
   *     pOldSymToNewVarExp.
   * @param pNewHir Statement or expression to be changed.
   * @param pOldSymToNewVarExp maps old variable to new variable expression.
   */
  protected HIR
    rewriteVariables( HIR pNewHir, Map pOldSymToNewVarExp)
  {
    //-- Change variables to corresponding parameter or new variable.
    if ((pNewHir == null)||(pOldSymToNewVarExp == null))
      return null;
    if (fDbgLevel > 1) {
      ioRoot.dbgHir.print(2, "rewriteVariables " + pNewHir.toStringShort(),
        pOldSymToNewVarExp.toString());
    }
    for (HirIterator lIt3 = hir.hirIterator(pNewHir);
         lIt3.hasNext(); ) {
      HIR lHir = lIt3.next();
      if (lHir instanceof VarNode) {
        Var lOldVar = (Var)((VarNode)lHir).getSymNodeSym();
        if (pOldSymToNewVarExp.containsKey(lOldVar)) {
          // Var lNewVar = (Var)lOldSymToNewSym.get(lOldVar);
          // ((VarNode)lHir).setSymNodeSym(lNewVar);
          Exp lNewVarExp =
            (Exp)((HIR)pOldSymToNewVarExp.get(lOldVar)).copyWithOperands();
          lHir.replaceThisNode(lNewVarExp);
          if (fDbgLevel > 0) {
            ioRoot.dbgHir.print(4, "replace", lOldVar.getName() +
              " to " + lNewVarExp.toStringShort());
          }
        }
      }
    }
    return pNewHir;
  } // rewriteVariables

  public Var
    defineVar(String pHeader, Type pType, SymTable pSymTable, Subp pDefinedIn)
  {
    SymTable lSymTableSave = symRoot.symTableCurrent;
    symRoot.symTableCurrent = pSymTable;
    String lName = pSymTable.generateSymName(pHeader);
    Var lVar = sym.defineVar(lName, pType, pDefinedIn);
    lVar.setFlag(Sym.FLAG_GENERATED_SYM, true);
    if (pSymTable == symRoot.symTableRoot) {
      lVar.setVisibility(sym.SYM_PUBLIC);
      lVar.setStorageClass(Var.VAR_STATIC);
    } else {
      lVar.setVisibility(sym.SYM_PRIVATE);
      lVar.setStorageClass(Var.VAR_AUTO);
    }
    // Restore symTableCurrent.
    symRoot.symTableCurrent = lSymTableSave;
    return lVar;
  } // defineVar

  public Param
    defineParam(String pHeader, Type pType, SymTable pSymTable)
  {
    SymTable lSymTableSaved = symRoot.symTableCurrent;
    symRoot.symTableCurrent = pSymTable;
    String lName = pSymTable.generateSymName(pHeader);
    Param lParam = sym.defineParam(lName, pType);
    lParam.setFlag(Sym.FLAG_GENERATED_SYM, true);
    symRoot.symTableCurrent = lSymTableSaved;
    return lParam;
  } // defineParam

//##71 BEGIN
  /**
   * Get the primary induction variable of the loop.
   * @param pForStmt loop statement.
   * @return the primary induction variable
   */
  protected Var
  getPrimaryInductionVar( ForStmt pForStmt )
{
  Stmt lLoopInitPart = pForStmt.getLoopInitPart();
  Stmt lLoopStepStmt = pForStmt.getLoopStepPart();
  Var lInductionVar = null;
  if (fDbgLevel > 1) {
    ioRoot.dbgHir.print(3, "getPrimaryInductionVar", pForStmt.toString());
    ioRoot.dbgHir.print(5, " loopInitPart",
      lLoopInitPart.toStringWithChildren().toString());
    ioRoot.dbgHir.print(5, " loopStepPart",
      lLoopStepStmt.toStringWithChildren().toString());
  }
  if (lLoopInitPart instanceof BlockStmt) {
    lLoopInitPart = ((BlockStmt)lLoopInitPart).getFirstStmt();
  }
  if (lLoopStepStmt instanceof LabeledStmt) {
    lLoopStepStmt = ((LabeledStmt)lLoopStepStmt).getStmt();
  }
  if (lLoopStepStmt instanceof BlockStmt) {
    lLoopStepStmt = ((BlockStmt)lLoopStepStmt).getFirstStmt();
  }
  if (lLoopInitPart instanceof AssignStmt) {
    if (lLoopInitPart.getChild1()instanceof VarNode) {
      if (lLoopStepStmt instanceof AssignStmt) {
        if (lLoopStepStmt.getChild1()instanceof VarNode) {
          lInductionVar
            = (Var)((VarNode)lLoopInitPart.getChild1()).getSymNodeSym();
        }
      }
    }
  }
  if (fDbgLevel > 0)
    ioRoot.dbgHir.print(3, " return " + lInductionVar);
  return lInductionVar;
} // getPrimaryInductionVar
//##71 END

  protected Stmt
    writeVector( Var pFromVar, Var pToVar,
                SymTable pSymTable )
  { // This is not refered after the revision of last-private variable treatment.
    ioRoot.dbgHir.print(2, "writeVector",
      " from " + pFromVar.toString() + " to " + pToVar.toString());
    if ((! (pToVar.getSymType() instanceof PointerType))||
        (! (((PointerType)pToVar.getSymType()).getPointedType() instanceof VectorType))||
        (! (pFromVar.getSymType() instanceof VectorType))) {
      ioRoot.msgRecovered.put("Type mismatch in writeVector of ReformHir from " +
        pFromVar.getSymType().toString() + " to " + pToVar.getSymType().toString());
      return null;
    }
    VectorType lType1 = (VectorType)pFromVar.getSymType();
    int lDimension;
    if (lType1.getElemType().isScalar()) {
      // 1 dimension array.
      lDimension = 1;
    }else if ((lType1.getElemType() instanceof VectorType)&&
     ((VectorType)lType1.getElemType()).isScalar()) {
     // 2 dimension array
     lDimension = 2;
   }else if ((lType1.getElemType() instanceof VectorType)&&
     (((VectorType)((VectorType)lType1.getElemType())).getElemType().isScalar())) {
     // 3 dimension array
     lDimension = 3;
   }else {
     ioRoot.msgRecovered.put("More than 3 dimension array in writeVector of ReformHir from " +
       pFromVar.getSymType().toString() + " to " + pToVar.getSymType().toString());
     return null;
   }
   VarNode lVarNodeL = hir.varNode(pToVar);
   VarNode lVarNodeR = hir.varNode(pFromVar);
   Var lIndex1 = pSymTable.generateVar(symRoot.typeInt);
   BlockStmt lBlock1 = hir.blockStmt(null);

   if (lDimension == 1) {
     //-- Generate assign-statement
     //   *(lVarNodeL + lIndex1) = lVarNodeR[lIndex1];
     AssignStmt lAssign11 = hir.assignStmt(
       hir.subscriptedExp(
         hir.contentsExp(lVarNodeL),
         hir.varNode(lIndex1)),
       hir.subscriptedExp(lVarNodeR, hir.varNode(lIndex1)));
     lBlock1.addLastStmt(lAssign11);
   }else if (lDimension == 2) {
     //-- Generate for-statement
     //   for (lIndex2 = lowerBound2; lIndex2 < lowerBound2+lelmCount2;
     //        lIndex2 = lIndex2 + 1) {
     //     lVarNodeL[lIndex1][lIndex2] = lVarNodeR[lIndex1][lIndex2];
     //   }
     VectorType lType2 = (VectorType)lType1.getElemType();
     Var lIndex2 = pSymTable.generateVar(symRoot.typeInt);
     Exp lArrayExp = hir.undecayExp(lVarNodeL, lType1.getElemCount(),
                      lType1.getLowerBound());
     AssignStmt lAssign12 = hir.assignStmt(
        hir.subscriptedExp(
          hir.subscriptedExp(lArrayExp, hir.varNode(lIndex2)),
          hir.varNode(lIndex1)),
          hir.subscriptedExp(
        hir.subscriptedExp(lVarNodeR, hir.varNode(lIndex2)),
        hir.varNode(lIndex1)));
     BlockStmt lBlock2 = hir.blockStmt(lAssign12);
     ForStmt lForStmt2 = hir.forStmt(
      hir.assignStmt(hir.varNode(lIndex2),
        hir.intConstNode(lType2.getLowerBound())),
      hir.exp(HIR.OP_CMP_LT, hir.varNode(lIndex2),
              hir.intConstNode(lType2.getLowerBound() +
                lType2.getElemCount())),
      lBlock2,
      hir.assignStmt(hir.varNode(lIndex2),
        hir.exp(HIR.OP_ADD, hir.varNode(lIndex2),
                hir.intConstNode(1))));
      lBlock1.addLastStmt(lForStmt2);
   }else if (lDimension == 3) {
     //-- Generate for-statement
     //   for (lIndex2 = lowerBound2; lIndex2 < lowerBound2+lelmCount2;
     //        lIndex2 = lIndex2 + 1) {
     //     for (lIndex3 = lowerBound3; lIndex3 < lowerBound3+lelmCount3;
     //          lIndex3 = lIndex3 + 1) {
     //       lVarNodeL[lIndex1][lIndex2][lIndex3]
     //          = lVarNodeR[lIndex1][lIndex2][lIndex3];
     //   }
     VectorType lType2 = (VectorType)lType1.getElemType();
     Var lIndex2 = pSymTable.generateVar(symRoot.typeInt);
     VectorType lType3 = (VectorType)((VectorType)lType1.getElemType()).getElemType();
     Var lIndex3 = pSymTable.generateVar(symRoot.typeInt);
     Exp lArrayExp2 = hir.undecayExp(lVarNodeL, lType1.getElemCount(),
                      lType1.getLowerBound());
     AssignStmt lAssign13 = hir.assignStmt(
        hir.subscriptedExp(
          hir.subscriptedExp(
            hir.subscriptedExp(lArrayExp2, hir.varNode(lIndex3)),
            hir.varNode(lIndex2)),
          hir.varNode(lIndex1)),
        hir.subscriptedExp(
          hir.subscriptedExp(
            hir.subscriptedExp(lVarNodeR, hir.varNode(lIndex3)),
             hir.varNode(lIndex2)),
          hir.varNode(lIndex1)));
     BlockStmt lBlock4 = hir.blockStmt(lAssign13);
     ForStmt lForStmt4 = hir.forStmt(
      hir.assignStmt(hir.varNode(lIndex3),
        hir.intConstNode(lType3.getLowerBound())),
      hir.exp(HIR.OP_CMP_LT, hir.varNode(lIndex3),
              hir.intConstNode(lType3.getLowerBound() +
                lType3.getElemCount())),
      lBlock4,
      hir.assignStmt(hir.varNode(lIndex3),
        hir.exp(HIR.OP_ADD, hir.varNode(lIndex3),
                hir.intConstNode(1))));
     BlockStmt lBlock5 = hir.blockStmt(lForStmt4);
     ForStmt lForStmt5 = hir.forStmt(
      hir.assignStmt(hir.varNode(lIndex2),
        hir.intConstNode(lType2.getLowerBound())),
      hir.exp(HIR.OP_CMP_LT, hir.varNode(lIndex2),
              hir.intConstNode(lType2.getLowerBound() +
                lType2.getElemCount())),
      lBlock5,
      hir.assignStmt(hir.varNode(lIndex2),
        hir.exp(HIR.OP_ADD, hir.varNode(lIndex2),
                hir.intConstNode(1))));
      lBlock1.addLastStmt(lForStmt5);
   }
   //-- Generate for-statement
   //   for (lIndex1 = lowerBound1; lIndex1 < lowerBound1+elemCount1;
   //        lIndex1 = lIndex1 + 1)
   //     lBlock1;
   ForStmt lForStmt1 = hir.forStmt(
      hir.assignStmt(hir.varNode(lIndex1),
        hir.intConstNode(lType1.getLowerBound())),
      hir.exp(HIR.OP_CMP_LT, hir.varNode(lIndex1),
              hir.intConstNode(lType1.getLowerBound() +
                lType1.getElemCount())),
      lBlock1,
      hir.assignStmt(hir.varNode(lIndex1),
        hir.exp(HIR.OP_ADD, hir.varNode(lIndex1),
                hir.intConstNode(1))));
    if (fDbgLevel > 1) {
      ioRoot.dbgHir.print(2, "Result of writeVector",
        " from " + pFromVar.toString() + " to " + pToVar.toString());
      lForStmt1.print(2);
    }
    return lForStmt1;
  } // writeVector

  protected void
    printLoopTable(LoopTable pTable)
  {
    pTable.print(1);
    pTable.DebugInductionList(fUtil);
  } // printLoopTable

  /**
   * Print the list pList with header pHeader.
   * @param pList list of items to be printed.
   * @param pHeader string explaining the set in short.
   */
  public void
    printList(List pList, String pHeader)
  {
    if (pList == null) {
      return;
    }
    System.out.print("\n" + pHeader + "(");
    for (Iterator lIt = pList.iterator();
         lIt.hasNext(); ) {
      Object lItem = lIt.next();
      if (lItem == null) {
        continue;
      }
      if (lItem instanceof List) {
        printList((List)lItem, " ");
      } else if (lItem instanceof HIR) {
        System.out.print(" " + ((HIR)lItem).toStringShort());
      } else if (lItem instanceof Sym) {
        System.out.print(" " + ((Sym)lItem).getName());
      } else if (lItem instanceof LoopTable) {
        ((LoopTable)lItem).print(1);
      } else {
        System.out.print(" class:" + lItem.getClass().toString()
          + " " + lItem.toString());
      }
    }
    System.out.print(")");
  } // printList

  /**
   * Print the set pSet with header indicated by pHeader.
   * @param pSet set of items.
   * @param pHeader header string to be printed.
   */
  public void
    printSet(Set pSet, String pHeader)
  {
    if (pSet == null) {
      return;
    }
    System.out.print("\n" + pHeader + "[");
    for (Iterator lIt = pSet.iterator();
         lIt.hasNext(); ) {
      Object lItem = lIt.next();
      if (lItem == null) {
        continue;
      }
      if (lItem instanceof List) {
        printList((List)lItem, " ");
      } else if (lItem instanceof HIR) {
        System.out.print(" " + ((HIR)lItem).toStringShort());
      } else if (lItem instanceof Sym) {
        System.out.print(" " + ((Sym)lItem).getName());
      } else {
        System.out.print(" " + lItem.toString());
      }
    }
    System.out.print("]");
  } // printSet

  /**
   * Change the set of VarNodes to a set of corresponding Vars.
   * @param pVarNodes set of VarNodes.
   * @return the set of Var symbols.
   */
  protected Set
  varNodeToVarSet( Set pVarNodes )
{
  Set lVarSet = new HashSet();
  for (Iterator lIt1 = pVarNodes.iterator();
       lIt1.hasNext(); ) {
   Object lItem = lIt1.next();
    if (lItem instanceof VarNode)
      lVarSet.add((Var)((VarNode)lItem).getSymNodeSym());
    else if (lItem instanceof Var)
      lVarSet.add((Var)lItem);
  }
  return lVarSet;
} // varNodeToVarSet

/**
 * Get the variable represented by pExp or by
 * its child 1 node.
 * @param pExp
 * @return the variable represented by pExp.
 */
protected Var
getVarOfExp( Exp pExp )
{
  if (pExp == null)
    return null;
  if (pExp instanceof VarNode)
    return (Var)((VarNode)pExp).getSymNodeSym();
  return getVarOfExp((Exp)pExp.getChild1());
} // getVarOfExp

/**
 * Get all reduction variables each of which corresponds to
 * the reduction table in pReductionList.
 * @param pReductionList list of reductions.
 * @return set of reduction variables.
 */
protected Set
getReductionVar( List pReductionList )
{
  Set lVarSet = new HashSet();
  if (pReductionList != null) {
    for (Iterator lIt1 = pReductionList.iterator();
         lIt1.hasNext(); ) {
      Reduction lRed = (Reduction)lIt1.next();
      if (lRed.DefVarNode != null) {
        lVarSet.add(lRed.DefVarNode.getSymNodeSym());
      }
    }
  }
  return lVarSet;
} // getReductionVar

/**
 * Get the list of reductions for the loop
 * represented by pTable.
 * @param pTable LoopTable of the loop.
 * @return list of reductions.
 */
protected List
getReductionList( LoopTable pTable )
{
  List lListOfReductions = new LinkedList();
  lListOfReductions.addAll(pTable.ReductionADDList);
  lListOfReductions.addAll(pTable.ReductionMULList);
  lListOfReductions.addAll(pTable.ReductionSUBList);
  lListOfReductions.addAll(pTable.ReductionMAXList);
  lListOfReductions.addAll(pTable.ReductionMINList);
  lListOfReductions.addAll(pTable.ReductionMAXINDEXList);
  lListOfReductions.addAll(pTable.ReductionMININDEXList);
  if (fDbgLevel >= 4)
    printList(lListOfReductions, "getReductionList");
  return lListOfReductions;
} // getReductionList

protected Reduction
getReductionForVar( Var pVar, List pReductionList )
{
  Reduction lReduction = null;
  for (Iterator lIt = pReductionList.iterator();
       lIt.hasNext(); ) {
    lReduction = (Reduction)lIt.next();
    if (lReduction.DefVarNode.getSymNodeSym() == pVar)
      return lReduction;
  }
  ioRoot.msgRecovered.put("Reduction table not found for "
    + pVar.getName());
  return lReduction;
} // getReductionForVar

protected Exp
initialValueOfReduction( LoopTable pTable, Var pReductionVar )
{
  Exp lInitValue;
  Type lRedType = pReductionVar.getSymType();
  if (getReductionVar(pTable.ReductionMULList).contains(pReductionVar)) {
    if (lRedType.isFloating()) {
      lInitValue = hir.constNode(sym.floatConst(1.0, lRedType));
    }else {
      lInitValue = hir.constNode(sym.intConst(1, lRedType));
    }
  }else {
    if (lRedType.isFloating()) {
      lInitValue = hir.constNode(sym.floatConst(0.0, lRedType));
    }else {
      lInitValue = hir.constNode(sym.intConst(0, lRedType));
    }
  }
  return lInitValue;
} // initialValueOfReduction

  //-------------------------------//
  // makeCSourceFromHirBase, createHir2CFile are copied from coins.lparallel.
  // callHirBaseToC is copied from coins.driver.
  protected boolean
  makeCSourceFromHirBase(String timing,
    HirRoot hirRoot,
    SymRoot symRoot,
    IoRoot io) throws IOException
  {
    File fHir2CFile = createHir2CFile(io);
    FileOutputStream out = new FileOutputStream(fHir2CFile);
    callHirBaseToC(hirRoot, symRoot, io, out);
    return true;
  } // makeCSourceFromHirBase

  private File
  createHir2CFile(IoRoot io) throws IOException
  {
    File openMPFile;
    CoinsOptions coinsOptions =
      io.getCompileSpecification().getCoinsOptions();
    File source = io.getSourceFile();
    String sourcePath = source.getPath();
    String root = sourcePath.substring(0, sourcePath.lastIndexOf('.'));
    String Hir2CFileName = root.concat("-loop.c");
    // ex) filename =foo-loop.c
    openMPFile = new File(Hir2CFileName);
    return (openMPFile);
  } // createHir2CFile

  /**
   * Translates HIR-Base into a C source program and writes it to an
   * OutputStream.
   *
   * @param hirRoot an HirRoot object.
   * @param symRoot a SymRoot object.
   * @param io the IoRoot.
   * @param out an OutputStream to which the C source program is written.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected void
  callHirBaseToC(HirRoot hirRoot,
    SymRoot symRoot,
    IoRoot io,
    OutputStream out) throws IOException
  {
    Trace trace = io.getCompileSpecification().getTrace();
    HirBaseToCImpl HirToC;
    HirToC = new HirBaseToCImpl(hirRoot, symRoot,
      new PrintStream(out), trace);
    /* PrintStream should be OutputStream */
    HirToC.Converter();
  } // callHirBaseToC

//-----------------------------------------//
/* ////////
  public void
    reformForParallel2()
  {
    ioRoot.dbgHir.print(1, "\nreformForParallel2\n");
    if (!fCoinsOptions.isSet("lparallel")) {
      ioRoot.dbgHir.print(2, "no lparallel option\n");
      return;
    }
    Set lSubprograms = new HashSet();
    Set lToBeParallelized = new HashSet();
    Program lProgram = (Program)hirRoot.programRoot;
    IrList lSubpList = lProgram.getSubpDefinitionList();
    for (Iterator lIt = lSubpList.iterator();
         lIt.hasNext(); ) {
      SubpDefinition lSubpDef = (SubpDefinition)lIt.next();
      lSubprograms.add(lSubpDef.getSubpSym());
    }
    ioRoot.dbgHir.print(2, "Subprograms defined", lSubprograms + "\n");
    HIR lProgInitPart = (HIR)lProgram.getInitiationPart();
    if (lProgInitPart instanceof BlockStmt) {
      List lStmtToBeDeleted = new LinkedList();
      for (Stmt lStmt = ((BlockStmt)lProgInitPart).getFirstStmt();
           lStmt != null;
           lStmt = lStmt.getNextStmt()) {
        if ((lStmt instanceof InfStmt) &&
            (((InfStmt)lStmt).getInfKind() == "lparallel")) {
          ioRoot.dbgHir.print(3, lStmt.toString() + "\n");
          IrList lOptionList = ((InfStmt)lStmt).getInfList("lparallel");
          int lIndex;
          Object lObject = lOptionList.get(0);
          ioRoot.dbgHir.print(3, " option name " + lObject
            + " " + lObject.getClass() + " " + lOptionList + "\n");
          String lOptionName;
          if (lObject instanceof String) {
            lOptionName = (String)lObject;
          } else if (lObject instanceof StringConst) {
            lOptionName = ((StringConst)lObject).getStringBody();
          } else if (lObject instanceof Sym) {
            lOptionName = ((Sym)lObject).getName();
          } else {
            ioRoot.dbgHir.print(1, "\nUnknown option kind" + lObject + "\n");
            continue;
          }
          lOptionName = lOptionName.intern();
          if (lOptionName == "subpParallel") {
            lIndex = 0;
            for (Iterator lIt = lOptionList.iterator();
                 lIt.hasNext(); lIndex++) {
              Object lSubp = lIt.next();
              ioRoot.dbgHir.print(4, " " + lSubp
                + " " + lSubp.getClass() + "\n");
              if ((lSubp instanceof Subp)) {
                lToBeParallelized.add(lSubp);
              }
            }
            lStmtToBeDeleted.add(lStmt);
            fChanged = true;
          } else if (lOptionName == "subpParallelAll") {
            lToBeParallelized.addAll(lSubprograms);
          } else {
            ioRoot.dbgHir.print(1, "\nUnknown option " + lOptionName + "\n");
            continue;
          }
        } // End for lparallel option
      } // End for each Stmt in InitiationPart
      ioRoot.dbgHir.print(2, "To be parallelized", lToBeParallelized.toString());
      boolean lChanged;
      for (Iterator lIt = lSubpList.iterator();
           lIt.hasNext(); ) {
        SubpDefinition lSubpDef = (SubpDefinition)lIt.next();
        Subp lSubp2 = lSubpDef.getSubpSym();
        if (!lToBeParallelized.contains(lSubp2)) {
          continue;
        }
        lChanged = parallelize2(lSubpDef);
        if (lChanged) {
          fChanged = true;
        }
      } // End of SubpDefinition list processing
      // Delete processed InfStmts.
      for (Iterator lIt2 = lStmtToBeDeleted.iterator();
           lIt2.hasNext(); ) {
        Stmt lStmt2 = (Stmt)lIt2.next();
        lStmt2.deleteThisStmt();
        fChanged = true;
      }
    } // End of processing global pragmas.

  } // reformForParallel2

  protected boolean
    parallelize2(SubpDefinition pSubpDefinition)
  {
    flowRoot = hirRoot.getFlowRoot();
    if (flowRoot == null) {
      flowRoot = new FlowRoot(hirRoot);
    }
    ioRoot.dbgHir.print(2, "parallelize2", pSubpDefinition.getSubpSym().getName());
    fLoopParallel2 =
      new coins.lparallel2.LoopParallelImpl(hirRoot, ioRoot, pSubpDefinition,
      flowRoot);

    fResults = flowRoot.aflow.results();
    //## ControlFlow lControlFlow = flowRoot.flow.controlFlowAnal(fSubpFlow);
    //## DataFlow lDataFlow = flowRoot.flow.dataFlowAnal(pSubpDefinition);
    fLoopParallel2.LoopAnal();
    ((coins.lparallel2.LoopParallelImpl)fLoopParallel2).ReconstructHir();
    // fUtil2 = new coins.lparallel2.LoopUtil(fSubpFlow);
    fLoopParallel2.SetOpenMPInfo();
    reformHirToParallelize2();
    return true; // REFINE
  } // parallelize2

  protected boolean reformHirToParallelize2()
  {
    LinkedList LoopInfoList
      = (LinkedList)fResults.get("LoopParallelList", fSubpFlow);
    ioRoot.dbgHir.print(3, "reformHirToParallelize2", LoopInfoList.toString());
    coins.lparallel2.LoopTable lTable;
    boolean lChanged = false;
    for (Iterator lIt = LoopInfoList.iterator();
         lIt.hasNext(); ) {
      lTable = (coins.lparallel2.LoopTable)lIt.next();
      ioRoot.dbgHir.print(3, "Loop", lTable.LoopStmt.toStringShort());
      // Process outer loop only. (Do not change inner loop to subprogram)
      if (lTable.getParaFlag(fUtil2, "setParamInfoLoop") == true) {
        changeLoopToSubprogram2(lTable);
        lChanged = true;
      }
    } // End of For Loop
    return lChanged;
  } // reformHirToParallelize2

  protected void
    changeLoopToSubprogram2(coins.lparallel2.LoopTable pTable)
  {
    ForStmt lLoopStmt = pTable.LoopStmt;
    ioRoot.dbgHir.print(3, "changeLoopToSubprogram", lLoopStmt.toStringShort());
    Set lGlobalVars = new HashSet();
    Set lLocalVars = new HashSet();
    Set lInLoopVars = new HashSet(); // Local variables that
    // neither live-in nore live-out the loop.
    BBlock lStartBlock = subpFlow.getBBlockOfIR(
      lLoopStmt.getLoopInitPart().getIndex());
    BBlock lEndBlock = lLoopStmt.getLoopEndLabel().getBBlock();
    FlowAnalSymVector lLiveIn = lStartBlock.getPLiveIn();
    FlowAnalSymVector lLiveOut = lStartBlock.getPLiveOut();
    Set lLiveInVars = lLiveIn.flowAnalSyms();
    Set lLiveOutVars = lLiveOut.flowAnalSyms();
    if ((fDbgLevel > 2) && (ioRoot.dbgHir.getLevel() > 0)) {
      printLoopTable2(pTable);
    }
  } // changeLoopToSubprogram2

  protected void
    printLoopTable2(coins.lparallel2.LoopTable pTable)
  {
    pTable.print(1);
  } // printLoopTable
*/ ////////

} // ReformHir class
