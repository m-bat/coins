/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.sym.Sym;

class OpenStmt extends FStmt{
  FirList ciList;
  
  public OpenStmt(FirList cilist, int line, FirToHir pfHir){
	super(line, pfHir);
    ciList = cilist;
  }

  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"OpenStmt"+"\n");
    if(ciList != null){
      ciList.print(level, spaces+"cfg  ");
    }
  }
  
  public String toString(){
    return super.toString()+" Open statement" ;
  }

  public void process(){
    ///////////////////////////////////////////////////////////////
    // create write exp
    I77Utils i77utils = new I77Utils(fHir);
    Sym sym = fHir.getSym();
    i77utils.openInit(ciList);
    
    stmt = i77utils.open();
    
    super.process();
  }
  
  
}
