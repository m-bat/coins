/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;
import java.util.LinkedList;

import coins.ir.hir.Exp;

public class FirList extends LinkedList implements Node{
  FirToHir fHir;
  public FirList (FirToHir pfHir) {
    super(); fHir = pfHir;
  }
  public FirList (Object pElem, FirToHir pfHir) {
    this(pfHir);
    addFirst(pElem);
  }
/*  public FirList list (FirToHir pfHir) {
    return new FirList(pfHir);
  }
  public FirList list (Object pElem, FirToHir pfHir) {
    FirList fList = new FirList(pfHir);
    return fList.addedFirst(pElem);
  }
*/
  public FirList addedFirst (Object pElem) {
    if (pElem == null){
      return this;
    }
    super.addFirst(pElem);
    return this;
  }
  public FirList addedLast (Object pElem) {
    if (pElem == null) return this;
    super.addLast(pElem);
    return this;
  }
  public FirList addList(FirList list){
    for (Iterator it = list.iterator(); it.hasNext(); )
      this.addLast(it.next());
    return this;
  }
  public void print(int level, String spaces){
    Iterator it = this.iterator();
    while(it.hasNext()){
      Node n = (Node)it.next();
      if(n != null){
        n.print(level, spaces+"  ");
      }
    }
  }
  public String toString(){
    return "FirList";
  }
/*  public Object clone(){
    FirList clone = new FirList();
    for (Iterator it = this.iterator(); it.hasNext(); )
      clone.addLast(it.next());
    return clone;
  }
*/
  public Exp makeExp(){
     fHir.printMsgFatal("Fatal Error: FirList is not an Expression.");
     return null;
  }
  public Exp makeArgAddr(FStmt pFStmt){
     fHir.printMsgFatal("Fatal Error: FirList is not an Argument.");
     return null;
  }
}
