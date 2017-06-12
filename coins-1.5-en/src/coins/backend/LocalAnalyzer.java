/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

/** Interface for function analyzer. */
public interface LocalAnalyzer {

  /** Analyze a funtion.
   ** @param func function to be analyzed.
   ** @return analysis of function.
   **/
  LocalAnalysis doIt(Function func);

  /** Return the name of the analysis engine.
   **  Used for trace tag name.
   **
   ** @return the name of the analysis engine.
   **/
  String name();
}
