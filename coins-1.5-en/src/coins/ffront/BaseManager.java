/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.HirRoot;
import coins.SymRoot;
import coins.ir.hir.HIR;
import coins.sym.Sym;


public class BaseManager{
  FirToHir fHir;
  TypeUtility fTypeUtil;
  HirUtility  fHirUtil;
  
  Sym sym;                 // an instance to generate Sym objects
  HIR hir;                 // an instance to generate HIR objects
  
  SymRoot symRoot;  // root of symbol information
  HirRoot hirRoot;
  
  BaseManager(FirToHir fth){
    fHir = fth;
    fTypeUtil = fth.getTypeUtility();
    fHirUtil  = fth.getHirUtility();
    
    symRoot = fHir.getSymRoot();
    hirRoot = fHir.getHirRoot();
    
    sym     = fHir.getSymRoot().sym;
    hir     = fHir.getHirRoot().hir;
  }
  
  void dp(String msg){
    fHir.dp(msg);
  }

  public void printMsgFatal(String pMsg){
    fHir.printMsgFatal(pMsg);
  }
  public void printMsgRecovered(String pMsg){
    fHir.printMsgRecovered(pMsg);
  }

}

