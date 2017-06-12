/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

/**
  only using in parsing.
 */

public class FormatStmt extends FStmt{
  String lexem;
  
  public FormatStmt(Node n, int line, FirToHir pfHir){
	super(line, pfHir);;
    Token t = (Token)n;
    lexem = t.getLexem();
  }
  public String getLexem(){
    return lexem;
  }
}

