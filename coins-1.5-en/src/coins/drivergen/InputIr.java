/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.drivergen;

import coins.IoRoot;
import coins.SymRoot;
import coins.HirRoot;
import coins.backend.Module;
import coins.FlowRoot;
import coins.backend.util.ImList;

/**
 * A driver implementation using the COINS Compiler Driver API.
 * This driver is a working sample of the COINS Compiler Driver API, and can 
 * be a modifiction basis or an inheritance base class of your own compiler
 * driver.
 **/
public class InputIr {
  /**
   * IoRoot
   **/
  public static IoRoot ioRoot = null;

  /**
   * HirRoot
   **/
  public static HirRoot hirRoot = null;

  /**
   * SymRoot
   **/
  public static SymRoot symRoot = null;

  /**
   * ImList
   **/
  public static ImList sexp = null;

  /**
   * FlowRoot
   **/
  public static FlowRoot hirFlowRoot = null;

  /**
   * Module
   **/
  public static Module unit = null;

  /**
   * Condition which describe the status of compile pass
   **/
  public static int condition = Conditions.SOURCE;

  /**
   * Constructor
   **/
  public InputIr(IoRoot io) {
    symRoot = new SymRoot(io);
    hirRoot = new HirRoot(symRoot);
    symRoot.attachHirRoot(hirRoot);
    symRoot.initiate();

  };
}
