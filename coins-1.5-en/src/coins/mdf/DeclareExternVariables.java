/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.sym.SymTable;
import coins.SymRoot;
import coins.sym.Sym;
import coins.sym.Subp;
import coins.sym.Type;
import coins.sym.Param;

/**
 * Declaration of the extern variables.
 **/
class DeclareExternVariables{
  /** The function `omp_init_Lock()' **/
  final Subp initLock;
  /** The function `omp_set_lock()' **/
  final Subp setLock;
  /** The function `omp_unset_lock()' **/
  final Subp unsetLock;
  /** The function `omp_destroy_lock()' **/
  final Subp destroyLock;
  /** The function `omp_get_thread_num()' **/
  final Subp getThreadNum;
  /** The function `omp_num_threads()' **/
  final Subp setNumThreads;

  /**
   * Constructor
   * @param symFact The factory of the Sym
   * @param symRoot The SymRoot
   **/
  DeclareExternVariables(Sym symFact,SymRoot symRoot){
    SymTable symTab=symRoot.symTableCurrentSubp;
    // OpenMP library calls which are for parallelizing.
    // omp_init_lock, omp_destroy_lock, omp_set_lock, omp_unset_lock,
    // omp_get_thread_num, omp_set_num_threads

    if(symTab.search("omp_set_num_threads".intern())==null){
      setNumThreads=symFact.defineSubp("omp_set_num_threads".intern(),
                                       symRoot.typeVoid);
      Param callParam=symFact.defineParam("omp-set_num_threads".intern(),
                                          symRoot.typeInt);
      setNumThreads.setVisibility(Sym.SYM_EXTERN);
      setNumThreads.addParam(callParam);
      setNumThreads.closeSubpPrototype();
    }
    else
      setNumThreads=(Subp)symTab.search("omp_set_num_threads".intern());

    if(symTab.search("omp_init_lock".intern())==null){
      initLock=symFact.defineSubp("omp_init_lock".intern(),
                                  symRoot.typeVoid);
      Type voidPointer=symFact.pointerType(symRoot.typeVoid);
      Param callParam=symFact.defineParam("omp-init-lock".intern(),
                                          symFact.pointerType(voidPointer));
      initLock.setVisibility(Sym.SYM_EXTERN);
      initLock.addParam(callParam);
      initLock.closeSubpPrototype();
    }
    else
      initLock=(Subp)symTab.search("omp_init_lock".intern());

    if(symTab.search("omp_set_lock".intern())==null){
      setLock=symFact.defineSubp("omp_set_lock".intern(),symRoot.typeVoid);
      setLock.setVisibility(Sym.SYM_EXTERN);
      Type voidPointer=symFact.pointerType(symRoot.typeVoid);
      Param callParam=symFact.defineParam("omp-set-lock".intern(),
                                          symFact.pointerType(voidPointer));
      setLock.addParam(callParam);
      setLock.closeSubpPrototype();
    }
    else
      setLock=(Subp)symTab.search("omp_set_lock".intern());

    if(symTab.search("omp_unset_lock".intern())==null){
      unsetLock=symFact.defineSubp("omp_unset_lock".intern(),symRoot.typeVoid);
      unsetLock.setVisibility(Sym.SYM_EXTERN);
      Type voidPointer=symFact.pointerType(symRoot.typeVoid);
      Param callParam=symFact.defineParam("omp-unset-lock".intern(),
                                          symFact.pointerType(voidPointer));
      unsetLock.addParam(callParam);
      unsetLock.closeSubpPrototype();
    }
    else
      unsetLock=(Subp)symTab.search("omp_unset_lock".intern());

    if(symTab.search("omp_destroy_lock".intern())==null){
      destroyLock=symFact.defineSubp("omp_destroy_lock".intern(),
                                     symRoot.typeVoid);
      destroyLock.setVisibility(Sym.SYM_EXTERN);
      Type voidPointer=symFact.pointerType(symRoot.typeVoid);
      Param callParam=symFact.defineParam("omp-destroy-lock".intern(),
                                          symFact.pointerType(voidPointer));
      destroyLock.addParam(callParam);
      destroyLock.closeSubpPrototype();
    }
    else
      destroyLock=(Subp)symTab.search("omp_destroy_lock".intern());

    if(symTab.search("omp_get_thread_num".intern())==null){
      getThreadNum=symFact.defineSubp("omp_get_thread_num".intern(),
                                      symRoot.typeInt);
      getThreadNum.setVisibility(Sym.SYM_EXTERN);
      getThreadNum.closeSubpPrototype();
    }
    else
      getThreadNum=(Subp)symTab.search("omp_get_thread_num".intern());

  }
}
