/* ---------------------------------------------------------------------
%   Copyright (C) 2012 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.anallir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

public class AliasDriver extends Driver {

    private List initTimingList(CoinsOptions coinsOptions, String option,
            char delimiter) {
        if (coinsOptions.isSet(option)) {
            return separateDelimitedList(coinsOptions.getArg(option), delimiter);
        } else {
            return new ArrayList();
        }
    }

    private List initHirToCTimingList(CoinsOptions coinsOptions) {
        return initTimingList(coinsOptions, HIR_TO_C_OPTION,
                HIR_TO_C_OPTION_DELIMITER);
    }

    private List initDumpHirTimingList(CoinsOptions coinsOptions) {
        return initTimingList(coinsOptions, DUMP_HIR_OPTION,
                DUMP_HIR_OPTION_DELIMITER);
    }

    private List initLirToCTimingList(CoinsOptions coinsOptions) {
        return initTimingList(coinsOptions, LIR_TO_C_OPTION,
                LIR_TO_C_OPTION_DELIMITER);
    }

    public void compile(File sourceFile, Suffix suffix, InputStream in,
            OutputStream out, IoRoot io) throws IOException, PassException {

        CompileSpecification spec = io.getCompileSpecification();

        CoinsOptions coinsOptions = spec.getCoinsOptions();
        List hirToCTimings = initHirToCTimingList(coinsOptions);
        List dumpHirTimings = initDumpHirTimingList(coinsOptions);
        List lirToCTimings = initLirToCTimingList(coinsOptions);

        Trace trace = spec.getTrace();

        SymRoot symRoot = new SymRoot(io);
        HirRoot hirRoot = new HirRoot(symRoot);
        symRoot.attachHirRoot(hirRoot);
        symRoot.initiate();

        boolean useOldLir = coinsOptions.isSet(OLD_LIR_OPTION);
        boolean useNewLir = coinsOptions.isSet(NEW_LIR_OPTION);
        if ((!useOldLir) && (!useNewLir)) {
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
            trace.trace(myName, 5000, "suffix option: "
                    + suffix.getSuffixOption());
            isLirOutput = suffix.getSuffixOption().equals(OUT_NEW_LIR_OPTION);
        }

        FlowRoot hirFlowRoot = null;
        ImList sexp = null;
        coins.snapshot.SnapShot snap = null;
        if (spec.getCoinsOptions().isSet("snapshot"))
            snap = new coins.snapshot.SnapShot(sourceFile, "coins snapsnot");

        checkLIROptionsIntegrity(sourceFile, useOldLir, useNewLir, isLirOutput,
                skipHIR, io);

        setOptimizationOptions(spec, coinsOptions, useNewLir);
        trace.trace(myName, 5000, "equivalent COINS options: "
                + coinsOptions.toString());

        if (!skipHIR) {
            /* pass 1 -- Source to HIR */
            makeHirFromSource(sourceFile, hirRoot, suffix, in, io);

            if (spec.getCoinsOptions().isSet("snapshot"))
                snap.shot(hirRoot, "Generated HIR");

            /* pass 2 -- HIR Optimizations Before Flow Analysis */
            optimizeHirBeforeFlowAnalysis(hirRoot, symRoot, io);
            makeCSourceFromHirBase("new", hirToCTimings, hirRoot, symRoot, io);
            dumpHirBase("new", dumpHirTimings, hirRoot, symRoot, io);

            /* pass 3 -- flow analysis */
            if (coinsOptions.isSet(HIR_FLOW_ANAL_OPTION)) {
                hirFlowRoot = makeHIRFlowAnalysis(hirRoot, symRoot, io);
            } else {
                hirFlowRoot = new FlowRoot(hirRoot);
            }
            makeCSourceFromHirBase("flo", hirToCTimings, hirRoot, symRoot, io);
            dumpHirBase("flo", dumpHirTimings, hirRoot, symRoot, io);

            /*
             * pass 4 -- HIR Optimizations & Parallelizations after Flow
             * Analysis
             */
            optimizeHirAfterFlowAnalysis(hirRoot, hirFlowRoot, symRoot, io);
            makeCSourceFromHirBase("opt", hirToCTimings, hirRoot, symRoot, io);
            dumpHirBase("opt", dumpHirTimings, hirRoot, symRoot, io);

            /* pass 5 -- checking HIR and Symbol Table */
            testSym(hirRoot, io);
            testHir(hirRoot, hirFlowRoot, io);

            if (spec.getCoinsOptions().isSet("snapshot"))
                snap.shot(hirRoot, "Optimized HIR");

            /* pass 6 -- HIR to LIR */
            sexp = makeNewLirFromHir(hirRoot, io, sourceFile, out, isLirOutput);
            if (isLirOutput) {
                trace.trace(myName, 5000,
                        "LIR file is created. Quitting compile.");
                return;
            }

        } else { /* (! useOldLir) && skipHIR */
            sexp = makeLIRFromLIRSource(in, io);
        }

        /* pass 7 -- Code generation */
        Root root = new Root(spec, new PrintWriter(System.out, true));
        String targetName = coinsOptions
                .getArg(CommandLine.COINS_TARGET_NAME_OPTION);
        String targetConvention = coinsOptions
                .getArg(CommandLine.COINS_TARGET_CONVENTION_OPTION);
        trace.trace(myName, 5000, "target name = " + targetName);

        //AliasInformation info = new AliasAnalyzer().analyze(new Module(sexp,
        //        targetName, targetConvention, root));

        Module unit = Module.loadSLir(sexp, targetName, targetConvention, root);
        makeCSourceFromLir("new", lirToCTimings, unit, io);

        if (spec.getCoinsOptions().isSet("snapshot"))
            snap.shot(unit, "Generated LIR");

        /* SSA optimization */
        if (spec.getCoinsOptions().isSet(coins.ssa.OptionName.SSA_OPT)) {
            unit.apply(new coins.ssa.SsaDriver(unit, io, spec));
            /* Simple/JumpOpt again */
            unit.apply(coins.backend.opt.SimpleOpt.trig);
            unit.apply(coins.backend.opt.JumpOpt.trig);
        } else {
            unit.basicOptimization();
        }
        if (spec.getCoinsOptions().isSet("simd")) {
            unit.apply(new coins.simd.SimdDriver(unit, io, spec));
        }
        makeCSourceFromLir("opt", lirToCTimings, unit, io);

        if (spec.getCoinsOptions().isSet("snapshot"))
            snap.shot(unit, "Optimized LIR");

        if (spec.getCoinsOptions().isSet("snapshot"))
            snap.generateXml();

        unit.generateCode(out);

        if (trace.shouldTrace("Sym", 1)) {
            trace.trace("Sym", 1, "\nSym after code generation ");
            symRoot.symTable.printSymTableAllDetail(symRoot.symTableRoot);
            symRoot.symTableConst.printSymTableDetail();
        }
    }

    private void loadDefaultSettings(CompileSpecification spec) {
        defaultSettings = new Properties();
        CoinsOptions coinsOptions = spec.getCoinsOptions();
        File libDir = coinsOptions.getLibDir();
        File settingFile = new File(libDir, DEFAULT_SETTING);
        if (settingFile.exists()) {
            try {
                defaultSettings.load(new FileInputStream(settingFile));
            } catch (IOException e) {
                spec.getWarning().warning(
                        "Couldn't load the default setting file: "
                                + settingFile.getPath());
            }
        }
    }

    protected void go(String[] args) {
        try {

            CompileSpecification spec = new CommandLine(args);
            loadDefaultSettings(spec);

            Root.init(spec);

            int status = new CompilerDriver(spec).go(this);
            System.exit(status);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(CompileStatus.ABNORMAL_END);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new AliasDriver().go(args);
    }
    
}
