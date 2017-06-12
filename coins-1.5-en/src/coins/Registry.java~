/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins;
//                           Jan. 2004.
/** Registry class
 * Items registered to avoid conflicts between modules of the compiler
 * or between compilers derived from the COINS compiler infrastructure.
 * Registered items are
 *   command line options,
 *   target architecture names,
 *   InfStmt (pragma) items,
 *   classes to be attached in backend,
 *   etc.
 * Verification of command line options is done seeing
 * option names and option items listed in this class
 * by calling isOptionsAreCorrect() of CheckOptions class
 * from Driver.
**/
public class
Registry
{

//====== Option names and their parameters used in coins.driver.Driver ======//

  /** Compile option names.
   *  These are specified after Driver with prefix "-"
   *  in such way as "coins.driver.Driver -S".
   *  See README.driver.en.txt.
   *  The items should be listed longer word first
   *  for words with the same heading part, so that matching succeeds
   *  by scanning in sequence.
   */
  public static final String
  OPTION[] = {
    "b",           // Same to barch. //##67
    "barch",       // Specify target architecture. //##67
                   // Should specify one of ARCH[].
    "C",           // Preserve comments while preprocessing.
    "c",           // Do not link.
    "coins",       // Control the compilation processing.
                   // See COINS[].
    "dynamic",     // Use dynamic linkage.
    "D",           // Define macro (preprocessor).  //##71
    "Dmacro",      // Define a preprocessor macro.
    "E",           // Stop after preprocessing (Preprocessing only).
    "g",           // Preserve symbols in executable files for debugging.
    "help",        // Show help messages.
    "Ipath",       // Specify include path.
    "I",           // Show archive to link.  //##71
    "Lpath",       // Specify archive search path.
    "L",           // Specify archive search path. //##71
    "larchive",    // Specify an archive to link.
    "l",           // Specify archive search path. //##71
    "mdf",         // Coarse grain parallelization.
    "O0",          // Optimization level 0.
    "O1",          // Optimization level 1.
    "O2",          // Optimization level 2.
    "O3",          // Optimization level 3.
    "O",           // Same as O1.
    "o",           // Path name of the output file.
    "pipe",        // Use pipelines.
    "p",           // Use performance monitor.  //##71
    "P",           // Prohibit #line directives.
    "S",           // Stop after compilation.
    "static",      // Use static linkage.
    "Umacro",      // Undefine a preprocessor macro.
    "U",           // Undefine a preprocessor macro. //##71
    "v",           // Print a command line when a sub-process is invoked. //##67
    "Wcategory",   // Show warning messages of specified category.
    "Wno-category",// Suppress warning messages of specified category.
    "W"            // Show warning message.  //##71
  }; // OPTION

  /**  COINS option names specifying suboption.
   *  These are specified in such way as
   *  "coins.driver.Driver -barch x86 -coins:hirOpt=cse".
   *  See README.driver.en.txt.
   */
  public static final String
  COINS[] = {
    "alias",        // Alias analysis assumption.
                    //  alias=pes : Pessimistic analysis.
                    //  alias=opt : Optimistic analysis.
    "assembler",    // Specify alternative assembly command.
    "attach",       // Attach classes to be used in backend. //##71
    "cgParallel",   // Coarse grain parallelization. //##74
    "coarseGrainParallel",  // Coarse grain parallelization. //##74
    "compile-parallel", // Do compilation in parallel.
    "debug",        // Compiler is under debugging. -coins:debug is
                    // same as -coins:preserveFiles,testHir,testSym.
    "debuginfo",    // Output debug information for trace2html.pl browser
                    // Include source LINE information to LIR.
    //##74 "doAll",        // Generate parallelizing code for do-all loops. //##71
    "gprof",        // gprof for getting profile information same as that of gcc. //##89
    "hir2c",        // Translate HIR to C program.
                    //  hir2c=new : Just after HIR creation.
                    //  hir2c=opt : After all HIR optimization.
    "hirAnal",      // Do HIR flow analysis.
    "hirOpt",       // Specify HIR optimization kind. See HIR_OPT[].
    "libdir",       // Specify library directory path.
    "linker",       // Specify alternative link command.
    "lir-opt",      // LIR/SSA optimization options. //##1.4.4.3-
    "lir2c",        // Translate LIR to C program.
                    //  lir2c=new : Just after LIR creation.
                    //  lir2c=opt : After LIR optimization.
    "lirOpt",      // LIR/SSA optimization options. //##1.4.4.3-
    //##67 "lirAnal", // Do LIR flow analysis.
    //##67 "lirOpt",  // Specify LIR optimization kind.
    "loopinversion",  // Loop inversion transformation in backend
                      // (change loop-terminating jump to conditional jump)
    "max-recovered-errors", // The number of allowable recovered errors.
    "max-warnings",   // The number of allowable warning messages.
    //##74 "mdf",     // Coarse grain parallelization. //##71
    //##67 "newlir",  // Select new backend using new LIR.
    //##67 "oldlir",  // Select old codegenerator using old LIR.
    "optspace",       // Select space as the cost of code in order to select
                      // the decrease of object code rather than
                      // the decrease of execution time.
    //##67 "p",
    //##67 "preserveFiles",  // Preserve temporal files.
    "parallelDoAll",  // Generate parallelizing code for do-all loops. //##74
                      //   parallelDoAll=OpenMP: generate OpenMP/C program.
                      //   parallelDoAll=n: generate machine code, where n is
                      //   a number specifying maximum number of parallelization.
    "pipelining",     // Software pipelining.
    "printhir",       // Print HIR generated by Fortran front.
    "profile",        // Generate simulator code for profiling.  //##71
    "property",       // Property file path.
    "preprocessor",   // Specify alternative preprocessor command.
    "regalloc", // Select register allocation (2010.3)
    "regpromote",     // Register promotion optimization for LIR. //##67
    "scalar_replace",  // Scalar replacement optimization
    "schedule",       // Instruction scheduling optimization. //##67
    "schedule-after",       // Instruction scheduling optimization. //##coins-1.4.4.3-
                      //##71 BEGIN
    "simd",           // Do SIMD parallelization. //##85
    "simulate",       // Generate simulator to count execution of
                      // subprograms.
    //##74 "simulate-bblock",// Generate simulator to count execution of
                      // basic blocks.
    //##74 "simulate-function",// Generate simulator to count execution of
                      // subprograms.
    //##74 "simulate-memAccess",// Generate simulator to get trace of memory accesses and
                      // to count execution of basic blocks.
    "simulate-host",  // Specify the host machine for above simulators.
                      //##71 END
    "snapshot",       // Make snap shot XML file for CoVis visualizer.
    "ssa-no-aggregate-exp",       // SSA option in SSA
    "ssa-no-change-loop",         // SSA option in SSA
    "ssa-no-copy-folding",        // SSA option in SSA
    "ssa-no-memory-analysis",     // SSA option in SSA
    "ssa-no-redundant-phi-elimination", // SSA option in SSA
    "ssa-no-sreedhar-coalescing", // coalescing option in SSA
    "ssa-opt",        // SSA optimization options. See SSA_OPT[].
    "ssa-with-chaitin-coalescing",    // coalescing option in SSA
    "stopafterhir2c", // Stop compilation after HIR to C translation.
    "stopafterlir2c", // Stop compilation after LIR to C translation.
    "suffix",         // Path name of suffix database file.
    "suffixoption",   // Specify suffix option.
                      // suffixoption=out-newlir generates LIR file
                      //  xxx.lir from xxx.c as the result of hir2lir.
    "target",         // Target architecture.
                      // Should specify one of ARCH[].
    "target-arch",    // Same as target. //##71
    "target-convention", // Target convention name. //##71
    //##67 "testHir",        // Test HIR integrity.
    //##67 "testSym",        // Test symbol table integrity.
    "tmpdir",         // Temporary directory for work
    "trace"           // Specify trace levels. See TRACE[].
    //##67 "with_ssa_opt"    // Do SSA optimiazation
  }; // COINS

//##67 BEGIN

  /** HIR_OPT is the list of hirOpt options.
   *  These are specified in such way as
   *  "coins.driver.Driver -coins:hirOpt=loopexp/cf/cpf/pre".
   *  See README.HirOpt.en.txt.
   */
  public static final String
    HIR_OPT[] = {
    "cf",       // Constant folding.
    "cpf",      // Constant propagation.
    "cse",      // Local common subexpression elimination.
    "dce",      // Dead code elimination.
    "evalFloat",// Do not exclude floating point expressions in constant folding. //##67
    "fromc",    // Optimization at HIR creation in C-front.
    "globalReform",  // Global pattern matching transformation. //##79
    "gt",       // Global variable temporalization.
    "inline",   // Inline expansion.
                //  inline.nn   nn: upper limit of nodes to be inlined
    "inlinedepth", // Depth of recursive inline expansion.   //##77
                // inlinedepth.2 up to 2 times recursion,
                // inlinedepth.3 up to 3 times recursion.
                // This option is given in such way as
                //   -coins:hirOpt=inline/inlinedepth.2
    "loopexp",  // Loop expansion.
    "loopif",   // Loop invariant if-statement expansion.
    "noSimplify",  // Do not simplify HIR but leave unused labels,
                // and null-else-part etc.
                // before HIR-to-LIR conversion. //##76
    "pre",      // Partial redundancy elimination.
	"presrhir", // Preprocessing for Scalar Replacement by PRE method
    "safeArrayAll",  // Declares that in all subprograms   //##81
                 // in the compile unit, all subscripted variables
                 // and pointer expressions representing array elements
                 // never access memory location outside the corresponding
                 // array and their subscript values and offset values
                 // are all non-negative.
    //##101 BEGIN
    "complexityAllowance" // Allowance level of complexity for HIR optimization,
                 //         data flow analysis, and alias analysis.
                 // complexityAllownce.1 : default (as it is specified in
                 //                        coins.flow.SubpFlowImpl).
                 // complexityAllownce.2 : allow double complexity. 
                 // complexityAllownce.3 : allow triple complexity.
                 //  ....
                 // complexityAllownce.9 : allow 9-times complexity.
                 // Other numbers are treated as the same to the default case.
                 // Higher allowance level requires large memory and long CPU time.
    //##101 END
  }; // HIR_OPT

  /** SSA_OPT is the list of SSA optimization options.
   *  These are specified in such way as
   *  "coins.driver.Driver -coins:ssa-opt=prun/cstp/cse/srd3".
   *  See README.SSA.en.txt.
   */
public static final String
  SSA_OPT[] = {
  "brig",  // Back translation using Briggs's Method
  "cbb",   // Concatenate Basic Blocks
  //  "cnt", // Counting Instructions
  "clrlin",// Clear Line Number
  "cntbb", // Counting Instructions for Basic Blocks
  "cpyp",  // Copy Propagation
  "cs",    // 
  //  "cs2",    // 
  "cse",   // Common Subexpression Elimination
  "cstp",  // Constant Folding and Propagation with Conditional Branches
  "dce",   // Dead Code Elimination
  "dc",
  // ******************** Begin 2012.3
  "ddpde", // Demand Driven Partial Dead code Elimination
  // ******************** End
  "divex", // Divide Expression into Three-Address Code (the right-hand side
           //  of assignment will have only one operator)
  "divex2",
  "dump",  // Outputs the LIR (1) after translation into the
           //  pruned SSA form, and (2) after back translation from SSA form.
  "ebe",   // Empty Block Elimination
  "esplt", // Split Critical Edge
  // ******************** Begin 2012.3
  "expde", // Exhaustive PDE
  "glia",  // Global Load Instruction Aggregation
  // ******************** End
  "gra",   // Global Reassociation
  "hli",   // Hoisting Loop-invariant Code
  "inslin", // Insert Line Number
  "lcm",   // Lazy Code Motion on SSA form
  "lir2c", // Make C program from LIR
  "mini",  // Translation to Minimal SSA form
  "num",   // SSA Line Numbering
  "osr",   // Operator Strength Reduction related to Induction Variables
           //  and Linear Function Test Replacement
  "pbb",   // Print Basic Blocks
  "pdeqp",
  "preqp", // Global Value Numbering and Partial Redundancy Elimination
           // with Efficient Question Propagation
  "prun",  // Translation to Pruned SSA form (recommended for optimization)
  "rmlin", // Remove Line Number
  "rpe",   // Redundant Phi-function Elimination
  "semi",  // Translation to Semi-Pruned SSA form
  "shlin", // Show Line Number
  "srd1",  // Back translation using Sreedhar's Method I
  "srd2",  // Back translation using Sreedhar's Method II
  "srd3",  // Back translation using Sreedhar's Method III
  "ssag",  // Making SSA graph
  "stlin", // Set Line Number
  "eqp",   // Effective Demand-driven Partial Redundancy Elimination
  "presr", // Scalar Replacement Based on Partial Redundancy Elimination
  "divex3",
  }; // SSA_OPT

  /**
   * ARCH specifies target machine architecture.
   */

  //##96 BEGIN
  public static final String
  LIR_OPT[] = {
  "brig",  // Back translation using Briggs's Method
  "cbb",   // Concatenate Basic Blocks
  "cpyp",  // Copy Propagation
  "cse",   // Common Subexpression Elimination
  "cstp",  // Constant Folding and Propagation with Conditional Branches
  "dce",   // Dead Code Elimination
  // ******************** Begin 2012.3
  "ddpde", // Demand Driven Partial Dead code Elimination
  // ******************** End
  "divex", // Divide Expression into Three-Address Code (the right-hand side
           //  of assignment will have only one operator). Applied to SSA-form.
  "divex2",// Similar to divex but this can be applied to non-SSA form.
  "dump",  // Outputs the LIR (1) after translation into the
           //  pruned SSA form, and (2) after back translation from SSA form.
  "ebe",   // Empty Block Elimination
  "esplt", // Split Critical Edge
  // ******************** Begin 2012.3
  "expde", // Exhaustive PDE
  "glia",  // Global Load Instruction Aggregation
  // ******************** End
  "gra",   // Global Reassociation
  "hli",   // Hoisting Loop-invariant Code
  "lcm",   // Lazy Code Motion on SSA form
  "lir2c", // Make C program from LIR
  "mini",  // Translation to Minimal SSA form
  "osr",   // Operator Strength Reduction related to Induction Variables
           //  and Linear Function Test Replacement
  "pdeqp",
  "preqp", // Global Value Numbering and Partial Redundancy Elimination
           // with Efficient Question Propagation
  "prun",  // Translation to Pruned SSA form (recommended for optimization)
  "rpe",   // Redundant Phi-function Elimination
  "semi",  // Translation to Semi-Pruned SSA form
  "srd1",  // Back translation using Sreedhar's Method I
  "srd2",  // Back translation using Sreedhar's Method II
  "srd3",  // Back translation using Sreedhar's Method III
  "ssag",  // Making SSA graph
  "eqp",   // Demand-driven Partial Redundancy Elimination without 
           // invoking copy propagation (to decrease register pressure).
  "presr", // Scalar replacement based on Partial Redundancy Elimination.
  "divex3",// Similar to divex but used when SSA scalar replacement is requested.
}; // LIR_OPT
//##96 END

  public static final String
    ARCH[] = {
    "alpha",      // Alpha       //##74
    "arm",        // ARM
    "mb",         // MicroBlaze  //##71
    "mips-spim",  // MIPS with convention=spim //##72
    "mips",       // MIPS
    "ppc",        // IBM Power PC
    "sh4",        // Hitachi SH-4
    "sparc",      // Sun SPARC (with multiply/divide instructions)
    "sparc-v8",   // Sun SPARC Version 8 (multiply/divide by subprogram call)
    "thumb",      // Thumb (16 bit ARM architecture) //##71
    "x86",        // Intel x86 (Linux)
    "x86simd",    // Generate SIMD instructions for IA32/SSE2. //##85
    "x86sse2",    // Intel x86 sse2 (Linux)    //##93
    "x86-cygwin", // Intel x86 (Cygwin)
    "x86_64",     // Intel 64 bit x86 (Linux) //##84
    "x86_64-mac"  // Intel 64 bit x86 (Machintosh) //##85
  }; // ARCH

//##67 END

  /** TRACE is the field names of trace option.
   *  These are specified in such way as
   *  "coins.driver.Driver -coins:trace=HIR.1/Opt1.1/Sexp/LIR.1".
   *  RegisterAllocation
   */
  public static final String
    TRACE[] = {
    "AggregateByReference", // Aggregate parameter transformation in backend
    "Alias",       // Alias analysis
    "AugmentCFG",  // AugmentCFG phase in backend
    //##67 "BGC"        // Backend garbage collection flush
    //##67 "Btime",     // Backend interval time
    //##67 "CodeGen",   // Code generator (old version)
    "Control",     // Print executed phase names of compiler. //##70
    "ConvToAsm",   // Conversion to assembler language in backend
    "default",     // Default for all trace items
    "Driver",      // Compiler driver
    "EarlyRewriting",   // Early rewriting phase in backend
    "Flow",        // Flow analysis
    "HIR",         // HIR generation/transformation
    "HIR2C",         // HIR-to-C transformation    //##98
    "InstSel",     // Instruction selection in backend
    "IntroVirReg", // Introduce virtual register phase in backend
    "JumpCanon",   // Jump-canonicalization phase in backend
    "JumpOpt",     // Jump-optimization phase in backend
    "LateRewriting",    // Late rewriting phase in backend
    "LIR",         // LIR generation/transformation
    // "ListDump",    // LIR in S-expression format after ConvToAsm in backend
    "LiveRange",   // Live range analysis in backend
    "LoopInversion",    // Loop inversion phase in backend
    "MDF",         // MDF parallelization. //##72
    "NamingFloatConst", // Naming phase for floating constant in backend
    "OptimisticCoalescingAllocation", // Register allocation in backend (2010.3)
    "Opt1",        // HIR optimization
    "Para1",       // Loop parallelization
    "Parse",       // C parser (C to C-AST)
    "PreHeaders",  // Phase to insert preheader to loops in backend
    "ReplaceFloatConst", // Replacement of floating constant in backend
    "Restruct",    // Restruct phase in backend
    "RewriteConvUF",     // Rewrite conversion operator ConvUF in backend
    "ScalarReplace",  // Scalar replacement
    "SimpleOpt",   // Simple optimization in backend
    "Sym",         // Symbol table
    "ToCFG",       // Conversion to CFG in backend
    "ToHir",       // C-AST to HIR
    "ToLinear",    // Conversion to linear (non CFG) form in backend
    //##67 "ToLir",      // HIR to LIR conversion
    "ToLir",             // HIR to LIR conversion //##76
    "ToMachineCode", // Conversion to machine code in backend
    //##67 "Sexp",       // LIR generation (new version)
    "RegisterAllocation",// Register allocation in backend
    "SSA",         // SSA optimization for LIR
    "Ssa",         // SSA transformation in backend
    "TMD",         // Process of target machine description and
                   // S-expression of assembly code generation.
    "VirReg"       // Virtual register allocation in backend
  }; // TRACE

  /**
   * ATTACH specifies the name of class to be attached to the backend.
   */
  public static final String
    ATTACH[] = {
    "coins.backend.sched.Schedule",  // Instruction scheduling
    "RegPromote", // Register promotion
    "Schedule"  // Instruction scheduling //##71
  }; // ATACH

  /**
   * HIR2C specifies the timing of generating C from HIR.
   */
  public static final String
    HIR2C[] = {
    "new", // Just after HIR crreation.
    "flo", // Just after flow analysis.
    "opt"  // After all optimizations on HIR.
  }; // HIR2C

  /**
   * HIR2C specifies the timing of generating C from LIR.
   */
  public static final String
    LIR2C[] = {
    "new", // Just after LIR crreation.
    "opt"  // After optimizations on HIR.
  }; // LIR2C

  //##74 BEGIN
  /**
   * Specify profiling items in simulation.
   */
  public static final String
  SIMULATE_OPT[] = {
    "bblock",    // Generate simulator to count execution of
                 // basic blocks.
    "function",  // Generate simulator to count execution of
                 // subprograms.
    "memAccess"  // Generate simulator to get trace of memory accesses and
                 // to count execution of basic blocks.
  };
  //##74 END

//====== Reserved name of information kind attached to IR nodes ======//

/** Reserved name of information kind (InfNode, InfStmt)
  *  Names beginning with "coins_" are reserved for basic part
  *  of this compiler.
  *  All used names should be listed here to avoid conflicts.
  */
public static final String
  INF_KIND_IR      = "coins_inf",     // for IR node
  INF_KIND_COMMENT = "coins_comment", // for comment
  INF_KIND_OPEN_MP = "coins_omp",     // for OpenMP
  INF_KIND_PRAGMA  = "coins_pragma",  // for other pragma
  INF_KIND_HIR2C   = "coins_hir2c",   // for HIR to C
  INF_KIND_PROFILE = "profile";       // for profiling pragma //##67

//##71 BEGIN
public static final String
  INF_KIND[] = {      // Name of Inf-kind specified in directives (#pragma)
    //##74 "doAll",          // Control do-all parallelization.
    //##74 "emcoins_thread", // Control threads for parallel execution.
    //##79 BEGIN
    "globalReform",   // Specify information for global pattern matching
                      // transformation in GlobalReform:
                      // #pragma globalReform patternSym pattern1 pattern2 ...
                      // #pragma globalReform target subp1 subp2 ...
                      // #pragma globalReform stmtParam param1 param2 ...
                      // #pragma globalReform noFurtherChange pattern1 pattern2 ...
    "optControl",     // Specify information for optimization.
                      // #pragma optControl functionsWithoutSideEffect f1 f2 ...
                      // #pragma optControl functionsWithSideEffect g1 g2 ...
                      // #pragma optControl inline subp1 subp2 ...
                      // #pragma optControl safeArray array1 array2 ...
                      // #pragma optControl safeArrayAll
    "parallel",       // Control threads for parallel execution.
                      // #pragma parallel init
                      // #pragma parallel end
                      // #pragma parallel doAllFunc f1 f2 ...
                      // #pragma parallel doAllFuncAll
                      // #pragma parallel doAll
                      // #pragma parallel forceDoAll (private ...) (lastPrivate ...) (reduction ...)
    "par",            // Control threads for parallel execution
                      // (same as "parallel").
    //##101 BEGIN
    "omp",            // #pragma omp -- Pragma for OpenMP.
                      // #pragma omp parallel for lastprivate(k,i) reduction(+:sum)
    "coins_omp",      // HIR-pragma for OpenMP generated by the option parallelDoAll=OpenMP.
                      // inf (list coins_omp
                      //      (list #pragma omp  parallel for lastprivate(k,i) reduction(+:sum ))   
    "emcoins_thread", // Pragma for embedded-COINS
                      // #pragma emcoins_thread init
                      // #pragma emcoins_thread doall(k)
                      // #pragma emcoins_thread end   
    //##101 END
    "simulate"        // Control simulator for profiling.
                      // #pragma simulate close subpForClose
                      // #pragma simulate memAccess subpForMemAccess
                      // #pragma simulate open subpForOpen
                      // #pragma simulate profileOff subp1 subp2 ...
                      // #pragma simulate profileOffAll
                      // #pragma simulate profileOn subp1 subp2 ...
  }; // INF_KIND

  public static final String
    //##74 INF_DO_ALL[] = {
    INF_PARALLEL[] = {
      "end",            // Terminate parallelization.
      "forceParallel",  // The succeeding loop is to be parallelized
                        // even if the loop-parallelizer does not say
                        // the loop can be parallelized.
      "init",           // Initiate parallelization.
      "parallel",       // The succeeding loop is to be parallelized if the
                        // loop-parallelizer says the loop can be parallelized.
      "subpParallel",   // Specify subprograms to be parallelized.
      "subpParallelAll" // All subprograms are to be parallelized.
    }; // PARALLEL
//##101 BEGIN
 public static final String
   INF_OMP[] = {
     "parallel"         // #pragma omp parallel for lastprivate(k,i) reduction(+:sum)
 }; // OMP
 public static final String
   INF_COINS_OMP[] = {
	 "#pragma"  // inf (list coins_omp
                //      (list #pragma omp  parallel for lastprivate(k,i) reduction(+:sum )) 	 
 }; // COINS_OMP
 public static final String
   INF_EMCOINS_THREAD[] = {
	 "init",    // #pragma emcoins_thread init
	 "doall",   // #pragma emcoins_thread doall(k)
	 "end"      // #pragma emcoins_thread end 
   }; // EMCOINS_THREAD
//##101 END
/* //##74
  public static final String
    INF_THREAD[] = {
      "init",           // Initiate parallelization.
      "end"             // Terminate parallelization.
    }; // INF_THREAD
*/ //##74

  public static final String
    INF_SIMULATE[] = {
      "close",         // Specify the subprogram to close profiling.
      "memAccess",     // Specify the subprogram to be called for
                       // tracing memory-accesses.
      "open",          // Specify the subprogram to initiate profiling.
      "profileOff",    // No profiling for specified subprograms
                       // if specified at global declaration level.
                       // Turn off the profiling switch so that profiling
                       // information is not generated  if specified in
                       // subprogram at the same level as executable statements.
      "profileOffAll", // No profiling for all subprograms
                       // (specify at global declaration level).
      "profileOn"      // Turn on the profiling switch so that
                       // profiling information is to be generated.
    }; // INF_SIMULATE

  public static final String
    INF_OPT_CONTROL[] = {
      "functionsWithoutSideEffect", // Specify subprograms that have no side effect.
      "functionsWithSideEffect",    // Specify subprograms to be excluded from
                                    // the list of functionsWithoutSideEffect.
      "inline",    // Specify subprograms to be expanded inline
                   // even if size-limitation exceeded or even if
                   // -coins:hirOpt=inline is not specified.
      "safeArray", // Specify array variables whose subscript values
                   // never exceed the range corresponding to the space allocated
                   // and subscripted values are all non-negative.
                   // A pointer pointing to an array area may also be specified
                   // as safeArray if the pointer is used to represent
                   // subscripted variables that never access outside
                   // the memory region of the corresponding array and
                   // offset value is non-negative.
                   // This should be given within the scope of a subprogram. //##81
      "safeArrayAll"  // Declares that all subscripted variables
                   // and pointer expressions representing array elements
                   // never access memory location outside the corresponding
                   // array and their subscript values and offset values
                   // are all non-negative. //##81
                   // This should be given within the scope of a subprogram. //##81
    }; // INF_OPT_CONTROL
  //##71 END

} // Registry class

