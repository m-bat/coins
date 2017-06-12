/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.IrList;
import coins.ir.hir.Exp;
import coins.sym.Sym;

/** STOP statement
 */
public class StopStmt extends FStmt{
  public StopStmt(Node n, int line, FirToHir pfHir){
	super(line, pfHir);
    n_ = n;
  }
  Node n_;
  
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+"StopStmt" + "(" + n_ + ")" +"\n");
  }
  public String toString(){
    return super.toString()+"STOP statement" + "(" + n_ + ")";
  }

  
  Exp get_stopfunc(){
    return fHirUtil.makeSubpNode("s_stop",
                               Parser.INTEGER, hir.irList(), Sym.SYM_EXTERN);
  }
  
  public void process(){
    IrList args = hir.irList();
    int len = 0;
    fHir.dp("stop parameter: " + n_);
    
    if(n_ == null){
      args.add(fHirUtil.makeCharsConstNode("").getBody());
      len = 0;
    }
    else if(n_ instanceof Token){
      Token t = (Token)n_;
      Exp exp = fHirUtil.makeCharsConstNode(t.getLexem());
      if(exp instanceof FortranCharacterExp){
      	exp = ((FortranCharacterExp)exp).getBody();
      }
      
	  args.add(exp);
	        len = t.getLexem().length();
    }
    else{
      fHir.printMsgFatal("STOP requests Token, but " + n_);
    }
    args.add(fHirUtil.makeIntConstNode(len));
    
    stmt = hir.callStmt(get_stopfunc(), args);
    
    super.process();
  }
}
