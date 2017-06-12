/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.sym.Sym;

class ReadStmt extends FStmt{
  FirList ciList, optIoList;
  Node    fmt_;
  boolean fmt_given_ = false;

  public ReadStmt(FirList cList, FirList ioList, int line, FirToHir pfHir) {
    super(line, pfHir);
    fmt_      = null;
    ciList    = cList;
    optIoList = ioList;
  }
  public ReadStmt(Node fmt, FirList ioList, int line, FirToHir pfHir) {
    super(line, pfHir);
    fmt_      = fmt;
    fmt_given_ = true;
    optIoList = ioList;
    ciList    = null;
  }

  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"ReadStmt"+"\n");
    if(ciList != null){
      ciList.print(level, spaces+"cfg  ");
    }
    if(optIoList != null){
      optIoList.print(level, spaces+"out  ");
    }
  }

  public String toString(){
    return super.toString()+" READ statement" ;
  }

  public void process(){
    ///////////////////////////////////////////////////////////////
    // create write exp
    I77Utils i77utils = new I77Utils(fHir);
    Sym sym = fHir.getSym();
    i77utils.readInit(ciList, fmt_, fmt_given_);

    addGeneratedStmt(i77utils.io_start());
    if(optIoList != null){
      Iterator it = optIoList.iterator();
      while(it.hasNext()){
        Node n = (Node)it.next();
        if(n instanceof Pair){
          // Do List
          addGeneratedStmt(i77utils.dolist((Pair)n));
        }
        else{
          addGeneratedStmt(i77utils.io_do(n));
        }
      }
    }
    addGeneratedStmt(i77utils.io_end());
    super.process();
  }

}

