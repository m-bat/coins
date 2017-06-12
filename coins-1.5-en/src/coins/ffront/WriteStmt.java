/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.sym.Sym;

/** WRITE statement
 *     WRITE '(' ci_list ')' opt_io_list
 * Currently, ci_list must be list of two '*'s, and
 * opt_io_list must be list of variables.
 */
public class WriteStmt extends FStmt{

  private FirList ciList;    // ci_list: list of two (null, null)
  private FirList optIoList; // opt_io_list

  public WriteStmt(FirList cList, FirList ioList, int line, FirToHir pfHir) {
    super(line, pfHir);
    ciList    = cList;
    optIoList = ioList;
  }

  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"WriteStmt"+"\n");
    ciList.   print(level, spaces+"cfg  ");
    if(optIoList != null){
      optIoList.print(level, spaces+"out  ");
    }
  }

  public String toString(){
    return super.toString()+" WRITE statement" ;
  }

  /////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////



  public void process(){
    // print(0, "");
    ///////////////////////////////////////////////////////////////
    // create write exp
    I77Utils i77utils = new I77Utils(fHir);
    Sym sym = fHir.getSym();
    i77utils.writeInit(ciList);

    addGeneratedStmt(i77utils.io_start());
    if(optIoList != null){
      Iterator it = optIoList.iterator();
      while(it.hasNext()){
        Node n = (Node)it.next();
        if(n instanceof Pair && !(n instanceof ComplexConstNode)){
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

