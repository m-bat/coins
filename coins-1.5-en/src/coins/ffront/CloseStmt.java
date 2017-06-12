/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.sym.Sym;

class CloseStmt extends FStmt{
  FirList ciList;
  
  public CloseStmt(FirList cilist, int line, FirToHir fth){
	super(line, fth);;
    ciList = cilist;
  }

  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"CloseStmt"+"\n");
    if(ciList != null){
      ciList.print(level, spaces+"cfg  ");
    }
  }
  
  public String toString(){
    return super.toString()+" Close statement" ;
  }

  public void process(){
    ///////////////////////////////////////////////////////////////
    // create write exp
    I77Utils i77utils = new I77Utils(fHir);
    Sym sym = fHir.getSym();
    i77utils.closeInit(ciList);
    
    stmt = i77utils.close();
    
    super.process();
  }
  
  
}


