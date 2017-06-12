/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.drivergen.process;

import coins.drivergen.InputIr;
import coins.drivergen.Options;
import coins.drivergen.ProcessException;

import coins.driver.CoinsOptions;
import coins.driver.CommandLine;
import coins.driver.CompileSpecification;
import coins.driver.CompilerDriver;
import coins.driver.CompilerImplementation;
import coins.driver.CompileSpecification;
import coins.driver.Trace;
import coins.FatalError;
import coins.PassException;

import java.util.HashMap;
import java.io.IOException;

/**
 * Class ProcessManager has a table for Process and name.
 **/
public class ProcessManager {
  /** Table for Processes **/
  private HashMap processMap = new HashMap();

  /** read/write memory area **/
  private InputIr ir;

  /** read only data area **/
  private Options op;

  /**
   * Constructor
   **/
  public ProcessManager(){
    registProcess(MakeHirFromSource.navi);
    registProcess(MakeNewLirFromHir.navi);
    registProcess(GenerateCode.navi);
  }

  /**
   * Regist the Process to processMap
   **/
  private void registProcess(Navigator p) {
    if (p != null)
      setProcess(p.name(), p);
  }

  public void setProcess(String name, Navigator p) {
    processMap.put(name, p);
  }

  public Navigator getProcess(String name) {
    return (Navigator)processMap.get(name);
  }

  /**
   * Initialize the common area for processes
   * @param ir   InputIr (read/write memory area)
   * @param op   Options (read only data area)
   **/
  public void init(InputIr ir, Options op) {
    this.ir = ir;
    this.op = op;
  }

  /**
   * Go to each process in order to the process names
   **/
  public void go(String[] procName)
    throws ProcessException, PassException, IOException {
    Navigator navi;

    for (int i = 0; i < procName.length; ++i) {
      navi = null;
      navi = getProcess(procName[i]);
      if (navi == null)
        throw new PassException(procName[i], "can't find the process");
      else {
        navi.go(ir, op);
      }
    }
  }

}
