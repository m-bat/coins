/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.drivergen.process;

import coins.drivergen.InputIr;
import coins.drivergen.Options;
import coins.drivergen.Conditions;
import coins.drivergen.ProcessException;

import coins.driver.CompileSpecification;
import coins.driver.PassStopException;
import coins.driver.Suffix;
import coins.driver.Trace;
import coins.FatalError;
import coins.PassException;

import java.io.IOException;
import java.text.ParseException;

/** Abstract class for each Process
 *  this class is super class of every process which is create by 
 *  the COINS Compiler Driver API user.
 **/

/**
 * abstract class Process
 * Class process is the super class of module which is invoked by Driver.
 * Driver invoke it as belows
 *   (1) check the condition wheather this process can start
 *   (2) if the condition satisfy the precondirion
 *    (2-1) then go()
 *    (2-2) else throws ProcessExceptoin
 *   (3) go()
 **/

public abstract class Process {

  /** Navigator class **/
  private static abstract class Navi implements Navigator {}

  /** Intermediate representation which input to this process.
    If this process changes the intermediate representation,
    the process rewrites it **/
  protected InputIr ir;

  /** The command-line options for the COINS compiler driver **/
  protected Options opt;

  protected static final String CANT_START = "can't start this process";
  protected static final String NOT_C_SRC = "source is not C source";
  protected static final String NOT_F_SRC = "source is not Fortran source";
  protected static final String NOT_L_SRC = "source is not LIR source";


  /** Navigator class singleton. **/
  // Navi class must define as static final class.
  //public static final Navi navi = new Navi();

  /**
   * Constructor
   * @param ir Intermediate Representation which input to this process
   * @param opt Options for the COINS compiler driver
   **/
  Process(InputIr ir, Options opt) {
    this.ir = ir;
    this.opt = opt;
  }

  /**
   * Run the compiler driver.
   * @param condition the condition of the compile process
   **/
  abstract public void go()
    throws ProcessException, PassException, IOException;  

  /**
   * Check the present status and judge this process can invoke.
   **/
  abstract public boolean canStart();


}

