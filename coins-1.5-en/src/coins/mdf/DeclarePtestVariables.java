/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.SymRoot;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Subp;
import coins.sym.Param;

/**
 * Declarations of the PTEST variables.
 * These variables are for debug.
 **/
class DeclarePtestVariables{
  /** The parameter `mdf_time' **/
  final Param mdfParam;
  /** The function `mdf_mt_start_time' **/
  final Subp mtStartTime;
  /** The function `mdf_mt_end_time' **/
  final Subp mtEndTime;
  /** The function `mdf_thread_start' **/
  final Subp threadStart;
  /** The function `mdf_watching_end' **/
  final Subp watchingEnd;
  /** The function `mdf_watching_report' **/
  final Subp watchingReport;

  /**
   * Constructor
   * @param symFact The factory of the Sym
   * @param symRoot The SymRoot
   **/
  DeclarePtestVariables(Sym symFact,SymRoot symRoot){
    // PTEST library calls which are for debugging.
    // mdf_mt_start_time(int,int), mdf_mt_end_time(int), 
    // mdf_a_task_end(int), mdf_watching_end(int),
    // mdf_watching_report(void)
    SymTable symTab=symRoot.symTableCurrentSubp;
    if(symTab.search("mdf_time".intern())==null){
      mdfParam=symFact.defineParam("mdf_time".intern(),
                                   symRoot.typeInt);
    }
    else
      mdfParam=(Param)symTab.search("mdf_time".intern());

    mtStartTime=symFact.defineSubp("mdf_mt_start_time".intern(),
                                   symRoot.typeVoid);
    mtStartTime.addParam(mdfParam);
    mtStartTime.addParam(mdfParam);
    mtStartTime.setVisibility(Sym.SYM_PUBLIC);
    mtStartTime.closeSubpHeader();
      
    mtEndTime=symFact.defineSubp("mdf_mt_end_time".intern(),
                                 symRoot.typeVoid);
    mtEndTime.addParam(mdfParam);
    mtEndTime.setVisibility(Sym.SYM_PUBLIC);
    mtEndTime.closeSubpHeader();
      
    threadStart=symFact.defineSubp("mdf_thread_start".intern(),
                                   symRoot.typeVoid);
    threadStart.addParam(mdfParam);
    threadStart.setVisibility(Sym.SYM_PUBLIC);
    threadStart.closeSubpHeader();
      
    watchingEnd=symFact.defineSubp("mdf_watching_end".intern(),
                                   symRoot.typeVoid);
    watchingEnd.addParam(mdfParam);
    watchingEnd.setVisibility(Sym.SYM_PUBLIC);
    watchingEnd.closeSubpHeader();
      
    watchingReport=symFact.defineSubp("mdf_watching_report".intern(),
                                      symRoot.typeVoid);
    watchingReport.setVisibility(Sym.SYM_PUBLIC);
    watchingReport.closeSubpHeader();
  }
}
