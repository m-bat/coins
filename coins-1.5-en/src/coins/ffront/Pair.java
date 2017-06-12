/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;


/*K
  Pair is the super class of all 2-valued nodes. //##68
 */

public class Pair implements Node{
  FirToHir fHir;
  Node left;
  Node right;

  public Pair(Node p1, Node p2, FirToHir pfHir){
    left  = p1;
    right = p2;
    fHir  = pfHir;
  }

  public Node getLeft()  { return left;}
  public Node getRight() { return right;}

  public void print(int level, String spaces) {
    //fHir.debugPrint(level, spaces +
    //  "(-"+ ((left !=null) ? left.getClass().toString()  : "null" ) + ", "
    //      + ((right!=null) ? right.getClass().toString() : "null" ) + "-)");
    // fHir.debugPrint(level, spaces + "\n");
    if (left == null){
      fHir.debugPrint(level, spaces + "  null\n");
    }
    else{
      left.print(level, spaces+"  ");
    }

    if (right == null){
      fHir.debugPrint(level, spaces + "  null\n");
    }
    else{
      right.print(level, spaces+"  ");
    }
  }

  public String toString(){
    String ls = left  == null ? "null" : left.toString();
    String rs = right == null ? "null" : right.toString();
    return ls + " " + rs;
  }
  public Exp makeExp(){
    fHir.printMsgFatal("Fatal Error: Pair is not an Expression(makeExp).");
    return null;
  }
  public Exp makeArgAddr(FStmt pFStmt){
    fHir.printMsgFatal("Fatal Error: Pair is not an Expression(makeArgAddr).");
    return null;
  }
}
