/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.drivergen.process;

import coins.drivergen.InputIr;
import coins.drivergen.Options;

/** Interface for process navigator. */
public interface Navigator {

  /** Return the name ot the Process.
   *  the name is used for indicate the process,
   *  and specified at the process order sheet which is written by user.
   *  @return the name of the process.
   */
  String name();

  /**
   * Return the brief description of the process.
   * @return true if transformation suceeded.
   */
  String subject();

  /**
   * The condition that this process can be start.
   * @return the condition
   */
  int precondition();

  /**
   * The condition that this process has done.
   * @return the condition
   */
  int postcondition();

  /**
   * If this process calls the external command, then return true.
   * @retrun true if it calls the external command
   */
  boolean isExternalCommand();

  /**
   * go to the process
   */
  void go(InputIr ir, Options op);
}
