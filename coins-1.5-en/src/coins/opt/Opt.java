/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.MachineParam; //##57
import coins.SymRoot;
import coins.Debug;
import coins.flow.ControlFlowImpl; //##60
import coins.flow.Flow;
import coins.flow.ControlFlow; //##63
import coins.flow.DataFlow; //##63
//##60 import coins.flow.FlowResults;
import coins.flow.FlowUtil;
import coins.flow.FlowImpl;  //##78
//##60 import coins.flow.RegisterFlowAnalClasses;
import coins.flow.SetRefRepr; //##60
import coins.flow.SetRefReprList; //##60
import coins.flow.ShowControlFlow; //##60
import coins.flow.ShowDataFlow; //##60
import coins.flow.ShowDataFlowByName; //##60
import coins.flow.SubpFlow;
import coins.driver.CompileSpecification;
import coins.driver.CoinsOptions; //##64
import coins.driver.Trace;

import coins.ir.IrList; //##70
import coins.ir.hir.BlockStmt; //##70
import coins.ir.hir.HIR;
import coins.ir.hir.InfStmt; //##70
import coins.ir.hir.Program;
import coins.ir.hir.Stmt; //##70
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubpDefinitionImpl;
import coins.sym.FlowAnalSym;  //##94
import coins.sym.Label;
import coins.sym.StringConst; //##70
import coins.sym.Subp; //##70
import coins.sym.SubpImpl; //##78
import coins.sym.Sym; //##70
import coins.sym.SymIterator; //##94
import coins.sym.SymTable; //##78
import coins.sym.SymTableIterator; //##94

import coins.flow.HirSubpFlowImpl;
//##60 import coins.flow.InitiateFlowHir;
import coins.flow.SubpFlow; //##60
import coins.flow.SubpFlowImpl; //##60
import coins.alias.AliasAnal;
import coins.alias.AliasAnalHir1;
import coins.alias.alias2.AliasAnalHir2;
import coins.alias.RecordAlias;

/**
 * <p>Optimization driver class. The following command line switches are supported:</p>
 *
 * <dl>
 * <dt>hirOpt=cf</dt>
 * <dd>HIR constant folding</dd>
 * <dt>hirOpt=cpf</dt>
 * <dd>HIR constant propagation and folding</dd>
 * <dt>hirOpt=cse</dt>
 * <dd>HIR common subexpression elimination</dd>
 * <dt>hirOpt=dce</dt>
 * <dd>HIR dead code elimination</dd>
 * <dt>lirOpt=cf</dt>
 * <dd>LIR constant folding</dd>
 * <dt>lirOpt=cpf</dt>
 * <dd>LIR constant propagation and folding</dd>
 * <dt>lirOpt=cse</dt>
 * <dd>LIR common subexpression elimination</dd>
 * <dt>lirOpt=dce</dt>
 * <dd>LIR dead code elimination</dd>
 * </dl>
 *
 * <p>Each one is done only once, and if multiple items are specified,
 *  they will be performed in the order listed above.</p>
 */
public class Opt // extends FlowOpt
{

  //------ Public fields ------//
  // Used to access Root information.
  public final FlowRoot flowRoot;
  public final IoRoot ioRoot;
  public final SymRoot symRoot;
  public final HirRoot hirRoot;

  public final Flow flow;
  //##62 public final AliasAnal fAlias;
  protected SubpFlow fSubpFlow; //##60
  public final AliasAnal fAlias; //##62
  public final Map fOptionMap; //##64
  public final CoinsOptions fOptions; //##64
  public final String fHirOpt; //##64
  public final List fKeyList; //##64
  public final int
    fDbgLevel; //##60
  //##100 BEGIN
  public int  // Complexity of the subprogram to be processed.
    fComplexityLevel, // Complexity level simple/medium/complex/
                      // very complex/ extremely complex
    fNodeCount,       // Number of HIR nodes.
    fUsedSymCount,    // Number of symbols used in executable statements.
    fBBlockCount;     // Number of basic blocks.
  //##100 END
  protected List fInlineSubps; //##70
  protected List fReformPatternList = null; //##79

  //====== Constructors ======//

  /**
   * Opt.
   *
   *
   */
  public Opt(FlowRoot pFlowRoot)
  {
    flowRoot = pFlowRoot;
    flow = pFlowRoot.flow; //##60
    ioRoot = flowRoot.ioRoot;
    symRoot = flowRoot.symRoot;
    hirRoot = flowRoot.hirRoot;
    fDbgLevel = ioRoot.dbgOpt1.getLevel(); //##60
    fAlias = new AliasAnalHir2(hirRoot); //##62
    //##64 BEGIN
    fOptions = ioRoot.getCompileSpecification().getCoinsOptions();
    fHirOpt = fOptions.getArg("hirOpt");
    if (fHirOpt != null) { //##70
      fOptionMap = fOptions.parseArgument(fHirOpt, '/', '.');
      fKeyList = (List)fOptionMap.get("item_key_list");
      if (fDbgLevel > 0)
        ioRoot.dbgOpt1.print(1, "\n Opt start", "Option map " + fOptionMap
          + " key list " + fKeyList);
    //##70 BEGIN
    }else {
      fOptionMap = new HashMap();
      fKeyList = new ArrayList();
    //##70 END
    }
    //##64 END
    //##62 fAlias = new AliasAnalHir2(hirRoot);
  }

  /**
   * dbg.
   *
   *
   */
  void dbg(int level, String pHeader, Object pObject)
  {
    ioRoot.dbgOpt1.printObject(level, pHeader, pObject);
    ioRoot.dbgOpt1.println(level);
  }

  /**
   * shouldTrace.
   *
   *
   */
  boolean shouldTrace(int level)
  {
    //##64 return ioRoot.getCompileSpecification().getTrace().shouldTrace("Opt1",level);
    if (level <= fDbgLevel) //##64
      return true; //##64
    else //##64
      return false; //##64
  }

  /**
   * HIR optimizer.
   *
   *
   */
  public boolean doHir(List pOpts)
  {
    return doHir(); //##64
  }

  public boolean doHir() //##64
  {
    boolean lOptimized = false;

    if (fDbgLevel > 0)
     ioRoot.dbgOpt1.print(1, "\ndoHir options:" + fHirOpt); //##64     ioRoot.dbgOpt1.print(1, "\ndoHir options:" + pOpts.toString());
    coins.aflow.FlowResults.putRegClasses(new coins.aflow.
      RegisterFlowAnalClasses());
    CompileSpecification lSpec = ioRoot.getCompileSpecification();
    //##79 BEGIN
    fReformPatternList = new ArrayList();
    if (fKeyList.contains("globalReform")) {
      // Do global reform before other optimizations.
      GlobalReform lReform = new GlobalReform(hirRoot);
      lOptimized = lReform.doReform( fReformPatternList);
    }
    /* //##83
    //##80 BEGIN
    else if (fKeyList.contains("globalReform2")) {
      // Do global reform before other optimizations.
      GlobalReform2 lReform = new GlobalReform2(hirRoot);
      lOptimized = lReform.doReform( fReformPatternList);
    }
    //##80 END
    */ //##83
    //##79 END
    //##70 BEGIN
    //-- Search for #pragma inline subp1 subp2 ...
    //   and list up subprograms if there are.
    fInlineSubps = new ArrayList();
    List lSubprograms = new ArrayList();
    Program lProgram = (Program)hirRoot.programRoot;
    IrList subpDefList = lProgram.getSubpDefinitionList();
    //##83 Map subpToSubpDefMap = new HashMap(); //##83
    for (Iterator lIt = subpDefList.iterator();
         lIt.hasNext(); ) {
      SubpDefinition lSubpDef = (SubpDefinition)lIt.next();
      lSubprograms.add(lSubpDef.getSubpSym());
      //##83 subpToSubpDefMap.put(lSubpDef.getSubpSym(), lSubpDef); //##83
    }
    ioRoot.dbgOpt1.print(2, "Subprograms defined", lSubprograms + "\n");
    HIR lProgInitPart = (HIR)lProgram.getInitiationPart();
    if (lProgInitPart instanceof BlockStmt) {
      for (Stmt lStmt = ((BlockStmt)lProgInitPart).getFirstStmt();
           lStmt != null;
           lStmt = lStmt.getNextStmt()) {
        if ((lStmt instanceof InfStmt) &&
            (((InfStmt)lStmt).getInfKind() == "optControl")) {
          ioRoot.dbgOpt1.print(3, lStmt.toString() + "\n");
          IrList lOptionList = ((InfStmt)lStmt).getInfList("optControl");
          String lSubkind = ((InfStmt)lStmt).getInfSubkindOf("optControl");
          if ((lSubkind != null)&&(lSubkind == "inline")) {
            for (Iterator lIt = lOptionList.iterator();
                   lIt.hasNext(); ) {
              Object lSubp = lIt.next();
              ioRoot.dbgOpt1.print(4, " " + lSubp
                + " " + lSubp.getClass() + "\n");
              if ((lSubp instanceof Subp)) {
                fInlineSubps.add(lSubp);
              }
            }
          }
          } // #pragma inline
      } // End for each Stmt in InitiationPart
    } // End of programInitPart
    ioRoot.dbgOpt1.print(2, "Subprograms specified in #pragma inline",
      fInlineSubps.toString());
    if ((! fOptions.isSet("hirOpt")&&
         fInlineSubps.isEmpty())) {
      ioRoot.dbgOpt1.print(1,
        "\nNeither hirOpt nore #pragma inline is specified.\n");
      return false;
    }
    //##70 END
    Trace lTrace = lSpec.getTrace();
    if (fKeyList.contains("inline")
        ||(! fInlineSubps.isEmpty()) //##70
        ) { //##64
      symRoot.symTableRoot.setUniqueNameToAllSym(); //##64
    }

    resetAllSymFlowInf(); //##94
    //##70 coins.ir.IrList subpDefList
    //##70   = ((Program)hirRoot.programRoot).getSubpDefinitionList();
    Iterator subpDefIterator = subpDefList.iterator();
    //##83 Iterator subpIterator = lSubprograms.iterator();

    while (subpDefIterator.hasNext())
    //##83 while (subpIterator.hasNext())  //##83
    {
      //##83 Subp lSubp = (Subp)subpIterator.next(); //##83
      SubpDefinition subpDef = (SubpDefinition)(subpDefIterator.next());
      //##83 SubpDefinition subpDef = (SubpDefinition)subpToSubpDefMap.get(lSubp);
      symRoot.subpCurrent = subpDef.getSubpSym();
      flowRoot.subpUnderAnalysis = subpDef.getSubpSym();
      if ((fDbgLevel > 0)||(ioRoot.dbgControl.getLevel() > 0)) { //##78
        String lFileName = symRoot.subpCurrent.getDefinedFile(); //##78
        System.out.print("\nhirOpt " + lFileName
          + " " +symRoot.subpCurrent.getName()); //##78
        ioRoot.dbgOpt1.print(3, "\n fSubpFlow " + flowRoot.fSubpFlow //##94
           + " subpFlow " + flowRoot.subpFlow);                      //##94
     }
      if (fReformPatternList.contains(symRoot.subpCurrent)) {
        ioRoot.dbgOpt1.print(2, "\n Skip global reform pattern "
          + symRoot.subpCurrent.getName());
        continue;
      }
      //##56 BEGIN
      RecordAlias lRecordAlias = null;
      // If the subpDefinition is not indexed, set index number.
      if ((subpDef.getIndex() == 0) &&
          (subpDef.getChild1().getIndex() == 0))
        subpDef.setIndexNumberToAllNodes(0, true);
        //##56 END
      //##78 BEGIN
      coins.aflow.FlowResults results = new coins.aflow.FlowResults(flowRoot); //##60
      coins.aflow.SubpFlow lSubpFlowA = flowRoot.aflow.subpFlow(subpDef,
        results); //##66
      flowRoot.aflow.setSubpFlow(lSubpFlowA); //##66
      //##78 END
      //##94 BEGIN
      if (flowRoot.fSubpFlow != null) {
        flowRoot.fSubpFlow.resetFlowSymLinkForRecordedSym();
        flowRoot.fSubpFlow.resetGlobalFlowSymLink();
      }
      //##94 END
      SubpFlow lSubpFlow = new HirSubpFlowImpl(flowRoot, subpDef); //##60
      // Above statement overwrites fSubpFlowCurrent of FlowImpl. //##78
      //##65 flowRoot.fSubpFlow = lSubpFlow; // This is set in HirSubpFlowImpl. //##60
      fSubpFlow = lSubpFlow; //##60
      // Print:
      // HIR before optimization
      //
      if (fDbgLevel > 0) {
        ioRoot.dbgOpt1.print(2, "\n  optimize " + //##78
          subpDef.getSubpSym().toString()); //##78
        if (fDbgLevel >= 4) { //##56
          ioRoot.dbgOpt1.print(3, "\nHIR before optimization");
          if (fOptions.isSet("hirOpt") ||
              fInlineSubps.contains(subpDef.getSubpSym())) {
            subpDef.print(1, true); //##56
          }
        }
      }

      // It is desirable to do loop expansion before other optimization. //##62
      for (Iterator Ie = fKeyList.iterator();
           Ie.hasNext(); ) { //##64
        String lOpt = (String)Ie.next();
        ControlFlow lControlFlow; //##65
        DataFlow lDataFlow; //##65
        //##100 if (fDbgLevel > 2)
        if (fDbgLevel >= 1)  //##100
          ioRoot.dbgOpt1.print(1, "\noptimize " +
            subpDef.getSubpSym().toString() + " by " + lOpt); //##62
        boolean llOptimized = false; //##56
        boolean lHirPrinted = false; //##66
          //##60 FlowResults results = new FlowResults(flowRoot);
          //##60 SubpFlow lSubpFlow = flow.subpFlow(subpDef, results);
          //##60 flowRoot.flow.setSubpFlow(lSubpFlow); //##60

        //##78 coins.aflow.FlowResults results = new coins.aflow.FlowResults(flowRoot); //##60
        //##63 coins.aflow.SubpFlow lSubpFlowA = flowRoot.aflow.subpFlow(subpDef,
        //##63   results); //##60
        //##63 flowRoot.aflow.setSubpFlow(lSubpFlowA); //##60
        //##78 coins.aflow.SubpFlow lSubpFlowA = flowRoot.aflow.subpFlow(subpDef,
        //##78   results); //##66
        //##78 flowRoot.aflow.setSubpFlow(lSubpFlowA); //##66
        ShowDataFlow lShowDataFlow = null; //##63
        int lThreshold = flowRoot.hirRoot.machineParam
          .costOfInstruction(MachineParam.COST_INDEX_TEMP_LOAD); //##57
        //##60 lThreshold = lThreshold + 3; //##56
        //##67 lThreshold = lThreshold + 1; //##60
        lThreshold = lThreshold + 3; //##67
        PRE lPre; //##56
        //##100 if (fDbgLevel > 2)
        //##100 ioRoot.dbgOpt1.print(3, "\n lOpt " +lOpt + "\n"); //##66
        if (fDbgLevel >= 1) //##100
          ioRoot.dbgOpt1.print(1, "\n lOpt " +lOpt + "\n"); //##100
        if (lOpt.equals("pre")) { //##56
          if (llOptimized)  //##78
            lSubpFlow.resetFlowSymLinkForRecordedSym(); //##78
          if (fKeyList.contains("subsptr")) //##64
            lPre = new PRE(fSubpFlow, subpDef, true, lThreshold); //##62
          else //##56
            lPre = new PRE(fSubpFlow, subpDef, false, lThreshold); //##62
          llOptimized = lPre.doPRE(); //##56
          lHirPrinted = true; //##66
        }
        else if (lOpt.equals("cse")) { //##56
          if (!fKeyList.contains("pre")) { //##64
            ((SubpFlowImpl)lSubpFlow).fHirAnalExtended = true;
            // Flow Initiate
            if (! lSubpFlow.isComputed(lSubpFlow.CF_CFG)) { //##65
              if (llOptimized) //##78
                lSubpFlow.resetFlowSymLinkForRecordedSym(); //##78
              lControlFlow = new ControlFlowImpl(flowRoot, lSubpFlow, subpDef); //##60
            }
            if (lSubpFlow.isFailed()) { //##78
              ioRoot.msgRecovered.put(5011, "\n Skip optimization " + lOpt);
              continue; //##78
            }
            //##100 BEGIN
            fComplexityLevel = lSubpFlow.getComplexityLevel();
            fNodeCount = lSubpFlow.getNumberOfNodes();
            fUsedSymCount = lSubpFlow.getUsedSymCount();
            fBBlockCount  = lSubpFlow.getNumberOfBBlocks();
            ioRoot.dbgOpt1.print(1, "\n ComplexityLevel=" + fComplexityLevel +
               " NodeCount=" + fNodeCount + " UsedSymCounmt=" + fUsedSymCount 
               + " BBlockCount " + fBBlockCount + "  "); 
            if (fComplexityLevel >= 5) {
                ioRoot.dbgOpt1.print(1, "\n SKIP " + lOpt + " because of high complexity \n"); //##66
              continue;
            }
            //##100 END
            CommonSubexpElimHirE cse = new CommonSubexpElimHirE(flowRoot,
              lThreshold); //##62
            cse.estimateExpCost(subpDef);
            llOptimized = cse.doBBlockLocal(lSubpFlow); //##56
            lOptimized |= llOptimized;
            lHirPrinted = true; //##66
          }
          else {
            if (fDbgLevel >= 0)
              ioRoot.dbgOpt1.print(2,
                "\n Skip cse optimization because pre does cse.\n");
          }
        }
        //##66 BEGIN
        else if (lOpt.equals("cpf")) {
          if (! lSubpFlow.isComputed(lSubpFlow.CF_CFG))
            lControlFlow = flow.controlFlowAnal(fSubpFlow);
          if (lSubpFlow.isFailed()) { //##78
            ioRoot.msgRecovered.put(5011, "\n Skip optimization " + lOpt);
            continue; //##78
          }
          //##100 BEGIN
          fComplexityLevel = lSubpFlow.getComplexityLevel();
          fNodeCount = lSubpFlow.getNumberOfNodes();
          fUsedSymCount = lSubpFlow.getUsedSymCount();
          fBBlockCount = lSubpFlow.getNumberOfBBlocks();
          ioRoot.dbgOpt1.print(1, "\n ComplexityLevel=" + fComplexityLevel +
                  " NodeCount=" + fNodeCount + " UsedSymCounmt=" + fUsedSymCount 
                  + " BBlockCount " + fBBlockCount + "  "); 
          if (fComplexityLevel >= 5) {
              ioRoot.dbgOpt1.print(1, "\n SKIP " + lOpt + " because of high complexity \n"); //##66
            continue;
          }
          //##100 END          
          if (! lSubpFlow.isComputed(lSubpFlow.DF_DEFUSE)) {
            lDataFlow = flow.dataFlowAnal(subpDef);
            lShowDataFlow = new ShowDataFlow(lDataFlow);
          }
          ConstPropagationAndFolding cpaf = new ConstPropagationAndFoldingHir(
             results);
          llOptimized = cpaf.doSubp(lSubpFlow);
          lOptimized |= llOptimized;
        }
        else if (lOpt.equals("cpfold")) {
          //##66 BEGIN
          lSubpFlowA.controlFlowAnal(); //##62
          if (lSubpFlow.isFailed()) { //##78
            ioRoot.msgRecovered.put(5011, "\n Skip optimization " + lOpt);
            continue; //##78
          }
          coins.aflow.InitiateFlowHir lInitiateFlow
            = new coins.aflow.InitiateFlowHir(results);
          //##66 lInitiateFlow.initiate(lSubpFlowA);
          //##66 results.clear();
          results.find("Initiate", lSubpFlowA);
          ConstPropagationAndFoldingOld cpaf = new ConstPropagationAndFoldingHirOld(
              results);
          llOptimized = cpaf.doSubp(lSubpFlowA); //##66
          lOptimized |= llOptimized;
        }
        else if(lOpt.equals("presrhir")) {
            if(! lSubpFlow.isComputed(lSubpFlow.CF_CFG)) {
              lControlFlow = flow.controlFlowAnal(fSubpFlow);
              if(llOptimized)
                lSubpFlow.resetFlowSymLinkForRecordedSym();
              lControlFlow = new ControlFlowImpl(flowRoot, lSubpFlow, subpDef);
            }
            if(lSubpFlow.isFailed()) {
              ioRoot.msgRecovered.put(5011, "\n Skip optimization "+ lOpt);
              continue;
            }
            PresrHir presr=new PresrHir(fSubpFlow, subpDef);
            llOptimized = presr.doIt();
            lOptimized |= llOptimized;
            lHirPrinted = true;
          }
        //##66 END
        //##79 BEGIN
        if (lOpt.equals("globalReform")||lOpt.equals("globalReform2")) {
          if (fDbgLevel >= 0)
            ioRoot.dbgOpt1.print(2,
              "\nglobalReform is already processed.\n");
        }
        //##79 END
        else { //##62
          // Neither cse nor pre nor cpf.
          //
          // Flow Initiate
          //
          //##62 ((SubpFlowImpl)lSubpFlow).allocateSetRefReprTable();
          //##63 ((coins.aflow.SubpFlowImpl)lSubpFlowA).allocateSetRefReprTable(); //##62
          //##60 lSubpFlow.controlFlowAnal();
          //##63 lSubpFlowA.controlFlowAnal(); //##62
          if (! lSubpFlow.isComputed(lSubpFlow.CF_CFG)) { //##65
            if (llOptimized) //##78
              lSubpFlow.resetFlowSymLinkForRecordedSym(); //##78
            lControlFlow = flow.controlFlowAnal(fSubpFlow); //##63
          }
          if (lSubpFlow.isFailed()) { //##78
            ioRoot.msgRecovered.put(5011, "\n Skip optimization " + lOpt);
            continue; //##78
          }
          //##100 BEGIN
          fComplexityLevel = lSubpFlow.getComplexityLevel();
          fNodeCount = lSubpFlow.getNumberOfNodes();
          fUsedSymCount = lSubpFlow.getUsedSymCount();
          fBBlockCount  = lSubpFlow.getNumberOfBBlocks();
          ioRoot.dbgOpt1.print(1, "\n ComplexityLevel=" + fComplexityLevel +
                  " NodeCount=" + fNodeCount + " UsedSymCounmt=" + fUsedSymCount 
                  + " BBlockCount " + fBBlockCount + "  "); 
          if (fComplexityLevel >= 5) {
              ioRoot.dbgOpt1.print(1, "\n SKIP " + lOpt + " because of high complexity \n"); //##66
            continue;
          }
          //##100 END
          //##63 coins.aflow.ShowFlow lShowFlow = new coins.aflow.ShowFlow(results); //##62
          //##63 if (lSpec.getTrace().shouldTrace("Flow", 2)) { //##62
          //##63   lShowFlow.showControlFlow(lSubpFlowA); //##62
          //##63 } //##62
          //##63 if (lOpt.equals("cpf")) { //##56
          //##66 if (lOpt.equals("cpf")||lOpt.equals("dce")) { //##63
          if (lOpt.equals("dce")) { //##66
            /* //##63
            coins.aflow.InitiateFlowHir lInitiateFlow
              = new coins.aflow.InitiateFlowHir(results);
            lInitiateFlow.initiate(lSubpFlowA);
            results.clear();
            results.find("Initiate", lSubpFlowA);
              */ //##63
            if (! lSubpFlow.isComputed(lSubpFlow.DF_DEFUSE)) { //##65
              lDataFlow = flow.dataFlowAnal(subpDef); //##63
              if (lSubpFlow.isFailed()) { //##78
                ioRoot.msgRecovered.put(5011, "\n Skip optimization " + lOpt);
                continue; //##78
              }
              lShowDataFlow = new ShowDataFlow(lDataFlow); //##63
            }
          } //##56
          //##62 coins.aflow.ShowFlow lShowFlow = new coins.aflow.ShowFlow(results);
          //##56 RecordAlias lRecordAlias = null;
          if (lOpt.equals("cf")) {
            //##63 ConstFolding cf = new ConstFolding(results);
            ConstFolding cf = new ConstFoldingHir(results); //##63
            //##63 llOptimized = cf.doSubp(lSubpFlowA);
            llOptimized = cf.doSubp(lSubpFlow); //##63
            lOptimized |= llOptimized;
          }
          /* //##66
          if (lOpt.equals("cpf")) {
            //##66 ConstPropagationAndFolding cpaf = new ConstPropagationAndFolding(
            //##66    results);
            ConstPropagationAndFolding cpaf = new ConstPropagationAndFoldingHir(
                results);
            //##63 llOptimized = cpaf.doSubp(lSubpFlowA); //##56
            //##66 llOptimized = cpaf.doSubp(lSubpFlow); //##63
            llOptimized = cpaf.doSubp(lSubpFlowA); //##66
            lOptimized |= llOptimized;
          }
            */ //##66
          if (lOpt.equals("dce")) {
            //##64 DeadCodeElim dce = new DeadCodeElim(results);
            DeadCodeElim dce = new DeadCodeElim(results, this); //##64            //##63 llOptimized = dce.doSubp(lSubpFlowA); //##56
            llOptimized = dce.doSubp(lSubpFlow); //##63
            lOptimized |= llOptimized;
          }
          if (lOpt.equals("gt")) {
            // Change SubpFlow parameter of GlobalVariableTemporalize
            // to flow.SubpFlow (from aflow.SubpFlow) //##60
            GlobalVariableTemporalize lGt =
            //##63   new GlobalVariableTemporalize(subpDef, lSubpFlowA, fAlias);
            new GlobalVariableTemporalize(subpDef, lSubpFlow, fAlias); //##63
            boolean lisOptimizedGt = lGt.doSubprogram();
            llOptimized = lisOptimizedGt; //##57
            lOptimized |= llOptimized;  //##57
          }
          //////// Start: 2005.02.17 S.Noishi ////////
          // It is desirable to do loop expansion before other optimization.
          if (lOpt.equals("loopif")) {
            LoopUnswitching lLoopUnswitching =
              new LoopUnswitching(hirRoot);
            llOptimized = lLoopUnswitching.doSubprogram(subpDef);
            lOptimized |= llOptimized;
          }
          if (lOpt.equals("loopexp")) {
        	//##100 BEGIN
            if (fComplexityLevel >= 4) {
                  ioRoot.dbgOpt1.print(1, "\n SKIP " + lOpt + " because of high complexity \n"); //##66
              continue;
            }
        	//##100 END
            LoopUnrolling lLoopUnrolling = new LoopUnrolling(hirRoot);
            llOptimized = lLoopUnrolling.doSubprogram(subpDef);
            lOptimized |= llOptimized;
          }
          //////// End: 2005.02.17 S.Noishi ////////
          //##64 BEGIN
          if (lOpt.equals("inline")) {
          	//##100 BEGIN
              if (fComplexityLevel >= 4) {
                    ioRoot.dbgOpt1.print(1, "\n SKIP " + lOpt + " because of high complexity \n"); //##66
                continue;
              }
          	//##100 END       	  
            String lOptionValue = (String)fOptionMap.get("inline");
            String lInlineDepth = "1"; //##77
            if (fOptionMap.containsKey("inlinedepth")) //##77
              lInlineDepth= (String)fOptionMap.get("inlinedepth"); //##77
            //##70 Inline inline = new Inline(hirRoot, symRoot, ioRoot,lOptionValue);
            Inline inline = new Inline(hirRoot, symRoot, ioRoot,
              lOptionValue, fInlineSubps, true
              , lInlineDepth);  //##77
            llOptimized = inline.changeSubp(subpDef);
            lOptimized |= llOptimized;
          }
          //##64 END
        } // End of neither cse nor pre nor cpf
          if (llOptimized) {
            if (! lHirPrinted) { //##66
              if (fDbgLevel >= 2) {
                ioRoot.dbgOpt1.print(2,
                  "\nHIR of " + subpDef.getSubpSym().toString()
                  + " after " + lOpt + " optimization");
                subpDef.print(1, true); //##56
              }
            subpDef.finishHir();
            fSubpFlow.setComputedFlag(fSubpFlow.CF_INDEXED);
          } //##66
            //##62 fSubpFlow.setComputedFlag(fSubpFlow.CF_CFG);
            fSubpFlow.resetComputedFlag(fSubpFlow.CF_CFG);
            fSubpFlow.setFlowAnalStateLevel(Flow.STATE_CFG_RESTRUCTURING);
        }
        //##57 BEGIN
        if (llOptimized) {
          if (fDbgLevel > 0) {
            ioRoot.dbgOpt1.print(1,
              "\nHIR of " + subpDef.getSubpSym().toString()
              + " is changed by " + lOpt + " optimization\n");
          }
        }
        else {
          if (fDbgLevel > 0) {
            ioRoot.dbgOpt1.print(1,
              "\nHIR of " + subpDef.getSubpSym().toString()
              + " is not changed by " + lOpt + " optimization\n");
          }
        }
        //##57 END
      } //End of For loop of KeyList iteration
      //##70 BEGIN
      if (fKeyList.isEmpty()&&
          (! fInlineSubps.isEmpty())) {
        if (fDbgLevel > 0)
          ioRoot.dbgOpt1.print(2, "Trial of inline expansion",
            "by #pragma ");
        String lInlineDepth2 = "1"; //##77
        if (fOptionMap.containsKey("inlinedepth")) //##77
          lInlineDepth2= (String)fOptionMap.get("inlinedepth"); //##77
        Inline inline = new Inline(hirRoot, symRoot, ioRoot,
            "", fInlineSubps,false
            , lInlineDepth2); //##77
          lOptimized |= inline.changeSubp(subpDef);
       }
        //##70 END
       releaseFlowInf(subpDef); //##78
    } // End Of While of SubpDefinition iteration
    //##70 BEGIN
    if (lOptimized) {
      ((HIR)hirRoot.programRoot).finishHir();
    }
    //##70 END
    return lOptimized;
  } // doHir

//##78 BEGIN
  /**
   * Release flow information so that
   * storage space can be reused and avoid OutofMemoryException.
   * @param pSubpDef SubpDefinition for which flow information
   *     is to be released.
   */
public void
releaseFlowInf( SubpDefinition pSubpDef )
{
  Subp lSubp = pSubpDef.getSubpSym();
  ioRoot.dbgOpt1.print(2, "releaseFlowInf", pSubpDef.getSubpSym().getName());
  IrList lLabelList = ((SubpImpl)lSubp).getLabelDefList();
  if ((lLabelList != null)&&
      (((FlowImpl)flowRoot.flow).fSubpFlowCurrent instanceof coins.aflow.SubpFlow)) {
    ioRoot.dbgOpt1.print(2, " reset flow Inf of labels");
    for (Iterator lItL = lLabelList.iterator();
         lItL.hasNext(); ) {
      Label lLabel = (Label)lItL.next();
      lLabel.setBBlock(null);
    }
  }
  flowRoot.flow.resetAllFlowInf(lSubp);
  symRoot.symTableFlow = null;
  flowRoot.fSubpFlow = null;
  flowRoot.subpFlow = null;
} // releaseFlowInf
//##78 END

//##94 BEGIN
/**
 * Reset flow information of all symbols.
 */
public void
resetAllSymFlowInf()
{
  ioRoot.dbgOpt1.print(2, "\nresetAllSymFlowInf ");
  SymTable lSymTable;
  for (SymTableIterator lSymTableIt
       = symRoot.symTableRoot.getSymTableIterator();
       lSymTableIt.hasNext(); ) {
    lSymTable = lSymTableIt.next();
    for (SymIterator lIt = lSymTable.getSymIterator();
         lIt.hasNext(); ) {
      Sym lSym = lIt.next();
      if (lSym instanceof Label) {
        if (((Label)lSym).getBBlock() != null) {
          if (fDbgLevel > 2)
            ioRoot.dbgOpt1.print(3, " " + lSym.getName());
          ((Label)lSym).setBBlock(null);
        }
      }else if (lSym instanceof FlowAnalSym) {
        if (((FlowAnalSym)lSym).getIndex() != 0) {
          if (fDbgLevel > 2)
            ioRoot.dbgOpt1.print(3, " " + lSym.getName());
          ((FlowAnalSym)lSym).setIndex(0);
        }
      }
    }
  }
}
//##94 END

} // Opt class
