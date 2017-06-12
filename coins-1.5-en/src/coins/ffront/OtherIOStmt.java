/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.sym.Sym;

class OtherIOStmt extends FStmt{
  FirList ciList;
  Node    fmt_;
  String  func_;
  
  public OtherIOStmt(String func, FirList cilist, Node fmt, int line, FirToHir pfHir){
	super(line, pfHir);
    ciList = cilist;
    fmt_   = fmt;
    func_  = func;
  }

  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"OtherIOStmt("+"(" + func_ + ")" + "\n");
    if(ciList != null){
      ciList.print(level, spaces+"cfg  ");
    }
  }
  
  public String toString(){
    return super.toString()+" OtherIO statement("+func_+")";
  }

  public void process(){
    ///////////////////////////////////////////////////////////////
    // create write exp
    I77Utils i77utils = new I77Utils(fHir);
    Sym sym = fHir.getSym();
    i77utils.othersInit(ciList, fmt_);
    
    stmt = i77utils.others(func_);
    
    super.process();
  }
  
  
}



