/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.sym.Sym;

class InquireStmt extends FStmt{
  FirList ciList, optIoList;
  Node    fmt_;
  
  public InquireStmt(FirList cList, int line, FirToHir pfHir) {
    super(line, pfHir);
    fmt_      = null;
    ciList    = cList;
  }

  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"InquireStmt"+"\n");
    if(ciList != null){
      ciList.print(level, spaces+"cfg  ");
    }
    if(optIoList != null){
      optIoList.print(level, spaces+"out  ");
    }
  }

  public String toString(){
    return super.toString()+" Inquire statement" ;
  }

  public void process(){
    ///////////////////////////////////////////////////////////////
    // create write exp
    I77Utils i77utils = new I77Utils(fHir);
    Sym sym = fHir.getSym();
    i77utils.inquireInit(ciList);
    
    stmt = i77utils.inquire();
  }
}



