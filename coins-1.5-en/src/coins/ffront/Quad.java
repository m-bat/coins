/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

public class Quad extends Triple{
  Node last;
  public Quad(Node pLeft, Node pRight, Node pExtra, Node pLast, FirToHir pfHir) {
    super(pLeft, pRight, pExtra, pfHir);
    last = pLast;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    if (last == null)
      fHir.debugPrint(level, spaces + "  null\n");
    else
      last.print(level, spaces+"  ");
  }
  public Node getLast(){
    return last;
  }
}
