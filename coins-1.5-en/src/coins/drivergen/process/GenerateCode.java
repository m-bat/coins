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

import coins.driver.CoinsOptions;
import coins.driver.CommandLine;
import coins.driver.Trace;
import coins.FatalError;
import coins.PassException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.text.ParseException;

/**
 * A process implementation using the COINS Compiler Driver API.
 * This process is a working sample of the COINS Compiler Driver API.
 **/
/**
 * GenerateCode is one of the process, convert Lir to assember code
 * 
 **/
public class GenerateCode extends Process {
  /**
   * Navigator class
   **/
  private static class Navi implements Navigator {

    public String name() {return "GenerateCode";}
    public String subject() {return "generate code from Lir.";}
    public int precondition() {return Conditions.LIR;}
    public int postcondition() {return Conditions.CODE;}
    public boolean isExternalCommand() { return false; }

    public void go(InputIr ir, Options op) {
      try {
        (new GenerateCode(ir,op)).go();
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
  GenerateCode(InputIr ir,Options opt){
    super(ir, opt);
  }

  /**
   * Run the compiler driver.
   * @param condition  the condition of the compile process
   **/
  public void go() throws ProcessException, PassException, IOException {
    //System.out.println(name+".go");

    if (!canStart()) {
      throw new PassException(myName, CANT_START);
    }

    if (ir.unit == null) {
      CoinsOptions coinsOptions = opt.coinsOptions;
      Root root = new Root(opt.spec, new PrintWriter(System.out, true));
      String targetName
        = coinsOptions.getArg(CommandLine.COINS_TARGET_NAME_OPTION);
      String targetConvention
        = coinsOptions.getArg(CommandLine.COINS_TARGET_CONVENTION_OPTION);
      opt.trace.trace(myName, 5000, "target name = " + targetName);

        if (ir.sexp == null) {
          throw new PassException(myName, "no ImList");
        }
        ir.unit = Module.loadSLir(ir.sexp, targetName, targetConvention, root);
        if (ir.unit == null) {
          throw new PassException(myName, "no Module");
        }
    }

    ir.unit.basicOptimization();
    ir.unit.generateCode(opt.out);

    ir.condition = Conditions.CODE;
  }

  /**
   * Check the present status and judge this process can invoke.
   **/
  public boolean canStart() {
    if (ir.sexp == null)
      return false;

    if (ir.condition == Conditions.LIR) 
      return true;
    else
      return false;
  }

}

