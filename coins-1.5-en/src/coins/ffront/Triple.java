/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

public class Triple extends Pair{
  Node extra;
  public Triple(Node pLeft, Node pRight, Node pExtra, FirToHir pfHir) {
    super(pLeft, pRight, pfHir);
    extra = pExtra;
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    if (extra == null)
      fHir.debugPrint(level, spaces + "  null\n");
    else
      extra.print(level, spaces+"  ");
  }
  public Node getExtra(){
    return extra;
  }
}
