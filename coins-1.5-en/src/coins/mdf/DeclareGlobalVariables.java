/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.SymRoot;
import coins.sym.Sym;
import coins.sym.Var;
import coins.sym.SymTable;

/**
 * Declaration of the global variables.
 **/
class DeclareGlobalVariables{
  /** The variable `_MDF_PROGRAM_END' **/
  final Var programEndVar;

  /**
   * Constructor
   * @param symFact The factory of Sym
   * @param symRoot The SymRoot
   **/
  DeclareGlobalVariables(Sym symFact,SymRoot symRoot){
    SymTable symTab=symRoot.symTableCurrentSubp;
    // The variable which checks whether all macro tasks are marked finish.

    if(symTab.search("_MDF_PROGRAM_END".intern())==null){
      programEndVar=symFact.defineVar("_MDF_PROGRAM_END".intern(),
                                      symRoot.typeInt);
      programEndVar.setVisibility(Sym.SYM_PUBLIC);
    }
    else
      programEndVar=(Var)symTab.search("_MDF_PROGRAM_END".intern());
  }
}
