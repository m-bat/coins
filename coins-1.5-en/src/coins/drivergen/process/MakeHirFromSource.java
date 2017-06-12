/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.drivergen.process;

import coins.drivergen.InputIr;
import coins.drivergen.Options;
import coins.drivergen.Conditions;
import coins.drivergen.ProcessException;

import coins.backend.Module;
import coins.backend.Root;

import coins.IoRoot;
import coins.SymRoot;
import coins.HirRoot;

import coins.driver.Suffix;
import coins.driver.CoinsOptions;
import coins.driver.CompileSpecification;
import coins.driver.Trace;
import coins.FatalError;
import coins.PassException;

import coins.ir.hir.HIR;
import coins.ir.hir.Program;

import coins.casttohir.ToHir;
import coins.casttohir.ToHirC;
import coins.casttohir.ToHirC2;
import coins.casttohir.ToHirCOpt;
import coins.casttohir.ToHirCOpt2;
import coins.casttohir.ToHirBase;
import coins.casttohir.ToHirBaseOpt;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;

/**
 * A process implementation using the COINS Compiler Driver API.
 * This proces is a working sample of the COINS Compiler Driver API.
 **/
/**
 * MakeHirFromSource is one oth the process, convert Source file to Hir form.
 *
 **/

public class MakeHirFromSource extends Process {
  /**
   * Navigator class
   **/
  private static class Navi implements Navigator {
    public String name() {return "MakeHirFromSource";}
    public String subject() {return "make Hir from source file.";}
    public int precondition() {return Conditions.SOURCE;}
    public int postcondition() {return Conditions.HIR;}
    public boolean isExternalCommand() { return false; }

    public void go(InputIr ir, Options op) {
      try {
        (new MakeHirFromSource(ir,op)).go();
      } catch (Exception e) {
        System.err.println(e);
      }
    }
  }

  /** Navi class singleton. **/
  public static final Navi navi = new Navi();

  /** the name of this process **/
  private String myName = navi.name();

  /**
   * Constructor
   * @param ir Intermediate Representation which input to this process
   * @param opt Options for the COINS compiler driver
   **/
  MakeHirFromSource(InputIr ir, Options opt) {
    super(ir, opt);
  }

  /**
   * Run the compiler driver.
   * @param condition  the condition of the compile process
   **/
  public void go() throws ProcessException, PassException, IOException {

    if (!canStart()) {
      throw new PassException(myName, CANT_START);
    }

    try {
      makeHirFromSource(opt.sourceFile, ir.hirRoot, opt.suffix, 
                        opt.in, opt.io);
    } catch (ProcessException e) {
      throw new ProcessException(opt, myName, "something");
    } catch (Exception e) {
      System.out.println(myName+" Fatal error");
      System.out.println(myName+" Fatal error "+e);
    }

    ir.condition = Conditions.HIR;
  }

  /**
   * Check the present status and judge this process can invoke.
   **/
  public boolean canStart() {
    if (ir.condition == Conditions.SOURCE) 
      return true;
    else
      return false;
  }

/* ----------------------------------------------------------------------- */
  /**
   *
   * Makes an HIR-C tree from a C source program.  Derived classes can
   * override this method.
   *
   **/
  private HIR makeHirCFromCSource(HirRoot hirRoot, InputStream in, IoRoot io)
    throws IOException, PassException {

    CompileSpecification spec = opt.spec;
    Trace trace = opt.trace;
    trace.trace(myName, 5000, "makeHirCFromCSource");

    CoinsOptions coinsOptions = opt.coinsOptions;
    boolean fromcSpecified = false;
    if (coinsOptions.isSet(opt.HIR_OPT_OPTION)) {
      fromcSpecified
        = includedInDelimitedList(opt.HIR_OPT_ARG_FROMC,
                                  opt.OPT_OPTION_DELIMITER,
                                  coinsOptions.getArg(opt.HIR_OPT_OPTION));
    }
    ToHir tohir
      = new ToHir(hirRoot, coinsOptions.isSet(opt.OLD_LIR_OPTION), fromcSpecified);
    new ToHirC(tohir).astToHirC(in);
    new ToHirC2(tohir).visitProgram();
    if (fromcSpecified) {
      new ToHirCOpt2(tohir).visitProgram();
    } else {
      new ToHirCOpt(tohir).visitProgram();
    }
    HIR hir = (Program)hirRoot.programRoot;

    if (trace.shouldTrace("HIR", 4)) {
      trace.trace("HIR", 4, "\nHIR-C ");
      hir.setIndexNumberToAllNodes(1);
      hir.print(0);
    }
    if (io.addToTotalErrorCount(0) > 0) {
      /* Just querying the error count.
         Why a querying method is not provided? */
      throw new PassException(io.getSourceFile(),
                              "Ast to HIR-C", "Error(s) in parsing source.");
    }
    return hir;
  }

  /**
   *
   * Makes an HIR-Bsae tree from an HIR-C tree.
   *
   **/
  private HIR makeHirBaseFromC(HirRoot hirRoot, HIR hir, IoRoot io)
    throws IOException, PassException {
    /*---- HirCToHirBase converts HIR-C to HIR-base. ----*/

    CompileSpecification spec = opt.spec;
    CoinsOptions coinsOptions = opt.coinsOptions;
    Trace trace = opt.trace;
    trace.trace(myName, 5000, "makeHirBaseFromC");

    boolean fromcSpecified = false;
    if (coinsOptions.isSet(opt.HIR_OPT_OPTION)) {
      fromcSpecified = 
        includedInDelimitedList(opt.HIR_OPT_ARG_FROMC,
                                opt.OPT_OPTION_DELIMITER,
                                coinsOptions.getArg(opt.HIR_OPT_OPTION));
    }
    ToHir tohir
      = new ToHir(hirRoot, coinsOptions.isSet(opt.OLD_LIR_OPTION),
                  fromcSpecified);
    new ToHirBase(tohir).visitProgram();
    if (fromcSpecified) {
      new ToHirBaseOpt(tohir).visitProgram();
    }
    hir.setIndexNumberToAllNodes(1);

    if (trace.shouldTrace("HIR", 3)) {
      trace.trace("HIR", 3, "\nHIR-base ");
      hir.print(0);
    }
    if (trace.shouldTrace("Sym", 3)) {
      trace.trace("Sym", 3, "\nSym after HIR generation ");
      SymRoot symroot = hirRoot.symRoot;
      symroot.symTable.printSymTableAllDetail(symroot.symTableRoot);
    }

    if (io.addToTotalErrorCount(0) > 0) {
      /* Just querying the error count.
         Why a querying method is not provided? */
      throw new PassException(io.getSourceFile(), "HIR-C to HIR-Base",
                              "Error(s) in making HIR-Base.");
    }
    if (hir.isTree()) {
      trace.trace("HIR", 2, "\nHIR-base does not violate tree structure.");
    }
    return hir;
  }

  /**
   * Reads HIR structure from an input file.
   *
   **/
  private HIR readHIR(File sourceFile, HirRoot hirRoot,
                      InputStream in, IoRoot io)
    throws IOException, PassException {
    ObjectInputStream oin = new ObjectInputStream(in);
    try {
      return (HIR)oin.readObject();
    } catch (ClassNotFoundException e) {
      io.msgError.put(sourceFile + ": class not found: " + e.getMessage());
      throw new PassException(sourceFile, myName,
                              "class not found: " + e.getMessage());
    }
  }

  /**
   * HIR tree creation from source code.
   *
   **/
  private void makeHirFromSource(File sourceFile, HirRoot hirRoot,
                                   Suffix suffix, InputStream in, IoRoot io)
    throws IOException, PassException {
    if (suffix.getLanguageName().equals("C")) {
      hirRoot.programRoot = makeHirCFromCSource(hirRoot, in, io);
      hirRoot.programRoot
        = makeHirBaseFromC(hirRoot, (HIR)hirRoot.programRoot, io);
    } else if (suffix.getLanguageName().equals("HIR")) {
      hirRoot.programRoot = readHIR(sourceFile, hirRoot, in, io);
    } else {
      io.msgError.put(sourceFile + ": Unknown programming language: "
                      + suffix.getLanguageName());
      throw new PassException(myName,
                              "Unknown language " + suffix.getLanguageName());
    }
  }

  /**
   *
   * Tests whether a string includes a term as itself or one of its elements
   * delimited by a specified delimiter.  For example,
   * includedInDelimitedList("ab", ".", "ab.cd.ef") returns true, while
   * includedInDelimitedList("ab", ".", "abc.def") returns false.
   * 
   * @param term the term to be searched for.
   * @param delimiter the delimiter.
   * @param list the string to be tested.
   * @return true if the list includes the term; false otherwise.
   **/
  private boolean includedInDelimitedList(String term,
                                            char delimiter,
                                            String list) {
    int i;
    while ((i = list.indexOf(delimiter)) != -1) {
      if (term.equals(list.substring(0, i))) {
        return true;
      } else {
        list = list.substring(i + 1);
      }
    }
    return term.equals(list);
  }


}

