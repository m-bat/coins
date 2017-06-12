/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.PassException;
import coins.ir.hir.HIR;
import coins.ir.IrList;
import coins.sym.FlowAnalSym;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Declaration of the pragmas.
 **/
class DeclarePragmas{
  /** The pragma `#ifdef _OPENMP' **/
  final IrList ifdef;
  /** The pragma `#endif' **/
  final IrList endif;
  /** The pragma `#ifdef PTEST' **/
  final IrList ptest;
  /** The pragma `#pragma omp parallel ' **/
  final IrList parallel;

  /**
   * Constructor
   * @param env The environment of the MDF module
   * @param mfg The current macro flow graph
   * @throws PassException Any exceptions in it
   **/
  DeclarePragmas(MdfEnvironment env,MacroFlowGraph mfg) throws PassException{
    HIR hirFact=mfg.hirRoot.hir;

    // #ifdef OPENMP and #endif 
    ifdef=hirFact.irList();
    ifdef.add("#ifdef _OPENMP");

    endif=hirFact.irList();
    endif.add("#endif");

    // #ifdef PTEST
    ptest=hirFact.irList();
    ptest.add("#ifdef PTEST");

    // #pragma omp parallel
    parallel=hirFact.irList();
    parallel.add("#pragma omp parallel ");

    // Attach the non-initialized thread private variables to the pragma.
    DataDependence ddep=new DataDependence(env,mfg);
    LinkedList privateVar=ddep.threadPrivateVariable();
    if(privateVar.size()>0){
      parallel.add("private(");
      boolean first=true;
      for(ListIterator ite=privateVar.listIterator();ite.hasNext();){
        FlowAnalSym s=(FlowAnalSym)ite.next();
        if(first){
          parallel.add(s.getName());
          first=false;
        }
        else parallel.add(","+s.getName());
      }
      parallel.add(") ");
    }

    // Attach the initialized thread private variables to the pragma.
    parallel.add("firstprivate(");
    parallel.add(mfg.taskSym.getName()+","+mfg.idSym.getName());
    parallel.add(")");
  }
}
