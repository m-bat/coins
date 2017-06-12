/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

/** Common Interface for Module/Function transformer. **/
public interface Transformer {

  /** Return the name of the transforming engine.
   **  Used for trace tag name.
   **
   ** @return the name of the transforming engine.
   **/
  String name();

  /** Return brief description of the tranformation.
   ** @return brief description of the tranformation.
   **/
  String subject();
}
