/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * AliasDriver.java
 *
 * Created on July 18, 2003, 11:36 AM
 */

package coins.alias;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.PassException;
import coins.SymRoot;
import coins.backend.Module;
import coins.backend.Root;
import coins.backend.util.ImList;
import coins.driver.CoinsOptions;
import coins.driver.CommandLine;
import coins.driver.CompileSpecification;
import coins.driver.CompileStatus;
import coins.driver.CompilerDriver;
import coins.driver.Driver;
import coins.driver.Suffix;
import coins.driver.Trace;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList;
import coins.ir.hir.HirVisitorModel2;
import coins.ir.hir.PointedExp;
import coins.ir.hir.Program;
import coins.ir.hir.QualifiedExp;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubscriptedExp;

/**
 *
 * @author  hasegawa
 */
public class AliasDriver extends Driver
{
    
    
    
    public void makeHirAliasAnalysis(HirRoot hirRoot)
    {
        CoinsOptions lCoinsOpts = hirRoot.ioRoot.getCompileSpecification().getCoinsOptions();
        
        //		removeUndecays(hirRoot);
        coins.ir.IrList subpDefList //##12
        = ((Program)hirRoot.programRoot).getSubpDefinitionList();
        Iterator subpDefIterator = subpDefList.iterator();
        while (subpDefIterator.hasNext())
        {
            SubpDefinition subpDef = (SubpDefinition)(subpDefIterator.next());
            hirRoot.symRoot.useSymTableOfSubpDefinition(subpDef);
            AliasAnal lAlias = new AliasAnalHir1(hirRoot);
            lAlias.prepareForAliasAnalHir(subpDef);
            if (lCoinsOpts.isSet("aliasopt"))
                testAliasByOptimizing(lAlias, subpDef, hirRoot);
            else
            {
                lAlias.printAliasPairs(subpDef);
//                new MyVisitor(lAlias, subpDef, hirRoot);
            }
            
        }
    }
        
    
    protected boolean testAliasByOptimizing(AliasAnal pAliasAnal, SubpDefinition pSubpDef, HirRoot pHirRoot)
    {
        return new SubpOptimizer(pAliasAnal, pSubpDef, pHirRoot).process();
    }
    
    private class SubpOptimizer extends HirVisitorModel2
    {
        private AliasAnal fAliasAnal;
        private SubpDefinition fSubpDef;
        private boolean fOptimized;
        
        private SubpOptimizer(AliasAnal pAliasAnal, SubpDefinition pSubpDef, HirRoot pHirRoot)
        {
            super(pHirRoot);
            fAliasAnal = pAliasAnal;
            fSubpDef = pSubpDef;
        }
        
        private boolean process()
        {
            visit(fSubpDef.getHirBody());
            return fOptimized;
        }
        
        public void atSubscritedExp(SubscriptedExp pExp)
        {
            tryOpt(pExp);
        }
        
        public void atQualifiedExp(QualifiedExp pExp)
        {
            tryOpt(pExp);
        }
        
        public void atPointedExp(PointedExp pExp)
        {
            tryOpt(pExp);
        }
        
        public void atExp(Exp pExp)
        {
            switch (pExp.getOperator())
            {
                case HIR.OP_CONTENTS:
                case HIR.OP_UNDECAY:
                    tryOpt(pExp);
                    break;
                default:
                    visitChildren(pExp);
            }
        }
        
        private void tryOpt(Exp pExp)
        {
            if (fAliasAnal.isLvalue(pExp))
            {
                AliasGroup lAliasGroup = fAliasAnal.getAliasGroupFor(pExp);
                coins.sym.Type lType = pExp.getType();
                if (lType.getAlignment() == lType.getSizeValue())
                    for (Iterator lIt = lAliasGroup.iterator(); lIt.hasNext();)
                    {
                        Exp lExp = (Exp)lIt.next();
                        if (fAliasAnal.mustAlias(pExp, lExp) && lType == lExp.getType() && lType.getAlignment() == lType.getSizeValue() && lExp.getOperator() == HIR.OP_VAR)
                        {
                            replaceNode(pExp, lExp.copyWithOperands());
                            fOptimized = true;
                            return;
                        }
                    }
            }
            visitChildren(pExp);
        }
    }
    
    
    public static void replaceNode(HIR pOld, HIR pNew)
    {
        HIR lParent;
        HirList lHirList;
        
        if (pOld instanceof HIR)
        {
            lParent = (HIR)pOld.getParent();
            if (lParent.getOperator() == HIR.OP_LIST)
            {
                lHirList = (HirList)lParent;
                lHirList.set(lHirList.indexOf(pOld), pNew);
                pNew.setParent(lHirList);
            } else
                lParent.replaceSource(((HIR)pOld).getChildNumber(), pNew);
        }
    }
    
    private List initTimingList(CoinsOptions coinsOptions,
    String option,
    char delimiter)
    {
        if (coinsOptions.isSet(option))
        {
            return separateDelimitedList(coinsOptions.getArg(option), delimiter);
        } else
        {
            return new ArrayList();
        }
    }
    
    private List initHirToCTimingList(CoinsOptions coinsOptions)
    {
        return initTimingList(coinsOptions,
        HIR_TO_C_OPTION,
        HIR_TO_C_OPTION_DELIMITER);
    }
    
    private List initLirToCTimingList(CoinsOptions coinsOptions)
    {
        return initTimingList(coinsOptions,
        LIR_TO_C_OPTION,
        LIR_TO_C_OPTION_DELIMITER);
    }
    
  public void compile(File sourceFile,
		      Suffix suffix,
		      InputStream in,
		      OutputStream out,
		      IoRoot io)
    throws IOException, PassException {

    CompileSpecification spec = io.getCompileSpecification();

    CoinsOptions coinsOptions = spec.getCoinsOptions();
    List hirToCTimings = initHirToCTimingList(coinsOptions);
    List lirToCTimings = initLirToCTimingList(coinsOptions);

    Trace trace = spec.getTrace();

    SymRoot symRoot  = new SymRoot(io);
    HirRoot hirRoot  = new HirRoot(symRoot);
    symRoot.attachHirRoot(hirRoot);
    symRoot.initiate();

    boolean useOldLir = coinsOptions.isSet(OLD_LIR_OPTION);
    boolean useNewLir = coinsOptions.isSet(NEW_LIR_OPTION);
    if ((! useOldLir) && (! useNewLir)) {
      if (DEFAULT_LIR_OPTION.equals(OLD_LIR_OPTION)) {
	useOldLir = true;
	coinsOptions.set(OLD_LIR_OPTION);
      } else {
	useNewLir = true;
	coinsOptions.set(NEW_LIR_OPTION);
      }
    }
    boolean skipHIR = suffix.getLanguageName().equals("LIR");
    boolean isLirOutput = false;
    if (suffix.getSuffixOption() != null) {
      trace.trace(myName, 5000, "suffix option: " + suffix.getSuffixOption());
      isLirOutput = suffix.getSuffixOption().equals(OUT_NEW_LIR_OPTION);
    }

    // LirRoot lirRoot = null; // 2004.06.08 S.Noishi
    FlowRoot hirFlowRoot = null;
    FlowRoot lirFlowRoot = null;
    ImList sexp = null;

    checkLIROptionsIntegrity(sourceFile,
			     useOldLir, useNewLir, isLirOutput, skipHIR, io);

    if (! skipHIR) {

      /* pass 1 -- Source to HIR */
      makeHirFromSource(sourceFile, hirRoot, suffix, in, io);

      /* pass 2 -- HIR Optimizations Before Flow Analysis */
      optimizeHirBeforeFlowAnalysis(hirRoot, symRoot, io);
      makeCSourceFromHirBase("new", hirToCTimings, hirRoot, symRoot, io);

            /* pass 2.5 -- HIR Alias Analysis */
            boolean alias = coinsOptions.isSet("alias");
            boolean optalias = coinsOptions.isSet("optalias");
            if (alias || optalias || true)
                makeHirAliasAnalysis(hirRoot);

            /* pass 3 -- flow analysis */
      if (coinsOptions.isSet(HIR_FLOW_ANAL_OPTION)) {
	hirFlowRoot = makeHIRFlowAnalysis(hirRoot, symRoot, io);
      } else {
	hirFlowRoot = new FlowRoot(hirRoot);
      }
      makeCSourceFromHirBase("flo", hirToCTimings, hirRoot, symRoot, io);

      /* pass 4 -- HIR Optimizations & Parallelizations after Flow Analysis */
      setOptimizationOptions(spec, coinsOptions, useNewLir);
      optimizeHirAfterFlowAnalysis(hirRoot, hirFlowRoot, symRoot, io);
      makeCSourceFromHirBase("opt", hirToCTimings, hirRoot, symRoot, io);

      /* pass 5 -- checking HIR and Symbol Table */
      testSym(hirRoot, io);
      testHir(hirRoot, hirFlowRoot, io);

      /* pass 6 -- HIR to LIR */
      sexp = makeNewLirFromHir(hirRoot, io, sourceFile, out, isLirOutput);
      if (isLirOutput) {
	trace.trace(myName, 5000, "LIR file is created. Quitting compile.");
	return;
      }

    } else { /* (! useOldLir) && skipHIR */
      sexp = makeLIRFromLIRSource(in, io);
    }

    /* pass 7 -- Code generation */
    Root root = new Root(spec, new PrintWriter(System.out, true));
    Module unit = Module.loadSLir(sexp, coinsOptions.getArg("target"),
				  "standard", root);
    makeCSourceFromLir("new", lirToCTimings, unit, io);

    /* SSA optimization */
    if (spec.getCoinsOptions().isSet(coins.ssa.OptionName.SSA_OPT)) {
      unit.apply(new coins.ssa.SsaDriver(unit, io, spec));
      /* Simple/JumpOpt again */
      unit.apply(coins.backend.opt.SimpleOpt.trig);
      unit.apply(coins.backend.opt.JumpOpt.trig);
    } else {
      unit.basicOptimization();
    }
    makeCSourceFromLir("opt", lirToCTimings, unit, io);
    unit.generateCode(out);

    if (trace.shouldTrace("Sym", 1)) {
      trace.trace("Sym", 1, "\nSym after code generation ");
      symRoot.symTable.printSymTableAllDetail(symRoot.symTableRoot);
      symRoot.symTableConst.printSymTableDetail();
    }
  }
    
    
    
    /**
     * A main function.<br>
     *
     * Makes a compile specification from a command line.  Creates an compiler
     * driver API object giving the compile specification.  Creates a driver
     * implementation object and pass it to the API object to start compilation.
     *
     * @param args a command line.
     **/
    public static void main(String[] args)
    {
        try {
            CompileSpecification spec = new CommandLine(args);
            int status = new CompilerDriver(spec).go(new AliasDriver());
            System.exit(status);
	} catch (ParseException e) {
            System.err.println(e.getMessage());
	    System.exit(CompileStatus.ABNORMAL_END);
        }
    }
}

