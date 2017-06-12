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
import coins.driver.CommandLine;
import coins.driver.CompileSpecification;
import coins.driver.PassStopException;
import coins.driver.Trace;
import coins.FatalError;
import coins.PassException;

import coins.ir.hir.HIR;
import coins.hir2lir.ConvToNewLIR;
import coins.backend.util.ImList;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.NotSerializableException;
import java.text.ParseException;

/**
 * A process implementation using the COINS Compiler Driver API.
 * This process is a working sample of the COINS Compiler Driver API.
 **/
/**
 * MakeNewLirFromHir is one of the process, convert Hir to new formed Lir.
 *
 **/
public class MakeNewLirFromHir extends Process {
  /**
   * Navigator class
   **/
  private static class Navi implements Navigator {
    public String name() {return "MakeNewLirFromHir";}
    public String subject() {return "make Lir(new) from Hir.";}
    public int precondition() {return Conditions.HIR;}
    public int postcondition() {return Conditions.LIR;}
    public boolean isExternalCommand() { return false; }

    public void go(InputIr ir, Options op) {
      try {
        (new MakeNewLirFromHir(ir,op)).go();
      } catch (Exception e) {
        System.err.println(e);
      }
    }
  }

  /** Navi class singleton. **/
  public static final Navi navi = new Navi();

  /** process name **/
  private String myName = navi.name();

  /**
   * Constructor
   * @param ir Intermediate Representation which input to this process
   * @param opt Options for the COINS compiler driver
   **/
  MakeNewLirFromHir(InputIr ir, Options opt){
    super(ir, opt);
  }

  /**
   * Run the compiler driver.
   * @param condition  the condition of the compile process
   **/
  public void go()
    throws ProcessException, PassException, IOException {

    HirRoot hirRoot = ir.hirRoot;
    IoRoot  io = opt.io;
    Suffix  suffix = opt.suffix;

    if (!canStart()) {
      throw new PassException(myName, CANT_START);
    }

    if (suffix.getSuffixOption() != null) {
      opt.trace.trace(myName, 5000, "suffix option: " +
                      suffix.getSuffixOption());
      }

    try {
      ir.sexp = makeNewLirFromHir(ir.hirRoot, opt.io, opt.sourceFile, opt.out);

    } catch (ProcessException e) {
      throw new ProcessException(opt, myName, "something");
    } catch (Exception e) {
      System.out.println(myName+" Fatal error");
    }

    ir.condition = Conditions.LIR;
  }

  /**
   * Check the present status and judge this process can invoke.
   **/
  public boolean canStart() {
    if (ir.hirRoot == null) {
      System.out.println("no ir.hirRoot");
      return false;
    }

    if (ir.condition == Conditions.HIR) 
      return true;
    else
      return false;
  }

/* ----------------------------------------------------------------------- */
  /**
   *
   * Makes an LIR tree from an HIR tree.
   *
   **/
  private ImList makeNewLirFromHir(HirRoot hirRoot,
                                     IoRoot io,
                                     File sourceFile,
                                     OutputStream out)
    throws PassException {
    /*---- HIR to new LIR  Conversion ----*/
    CompileSpecification spec = opt.spec;
    CoinsOptions coinsOptions = opt.coinsOptions;
    Trace trace = opt.trace;
    trace.trace(myName, 5000, "Make new LIR from HIR");

    if (trace.shouldTrace("HIR", 1)) {
      trace.trace("HIR", 1, "HIR before HirToNewLir conversion");
      ((HIR)hirRoot.programRoot).print(0, true);
    }
    if (trace.shouldTrace("Sym", 2)) {
      trace.trace("Sym", 2, "Sym before HirToNewLir conversion ");
      hirRoot.symRoot.symTable.printSymTableAll(hirRoot.symRoot.symTableRoot);
      hirRoot.symRoot.symTableConst.printSymTable();
    }

    ConvToNewLIR newconvert = new ConvToNewLIR(sourceFile, out, hirRoot);
    ImList sexp = newconvert.doConvert((HIR)hirRoot.programRoot);

    return sexp;
  }
}

