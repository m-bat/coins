/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.hir2c;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List; //##70

import coins.HirRoot;
import coins.Registry;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.HIR;
import coins.ir.hir.HirListImpl; //##70
import coins.ir.hir.InfNode;
import coins.sym.Sym;

///////////////////////////////////////////////////////////////////////////////
//
//  HIR-Base  To  C Language    (AssociationList)
//   (High level intermediate representation)
//
//
///////////////////////////////////////////////////////////////////////////////
class AssociationList
{
  private PrintWriter printOut;
  private LinkedList Attribute;
  private HIR hir;
  /**
   *
   * AssociationList:
   *
   *
   **/
  public AssociationList(HirRoot hirRoot, PrintWriter pOut)
  {
    printOut = pOut;
    hir = hirRoot.hir;
    Attribute = new LinkedList();
  }

  /**
   *
   * setAttribute:
   *
   *
   **/
  void setAttribute(IR phirNode)
  {

    Object infObj = (Object)phirNode.getInf(Registry.INF_KIND_OPEN_MP);
    if (infObj == null) {
      return;
    } else {
      Attribute.add(infObj);
    }
  }

  /**
   *
   * clear:
   *
   *
   **/
  void clear()
  {
    Attribute.clear();
  }

  /**
   *
   * PrintValue:
   *
   *
   **/
  void PrintValue()
  {
    StringBuffer OutString;
    InfNode Inf;
    IrList value;

    OutString = new StringBuffer();
    for (Iterator Ie = Attribute.listIterator();
         Ie.hasNext(); ) {
      Object lObject = Ie.next(); //##70
      if (lObject instanceof IrList) {
        value = (IrList)lObject;
        for (Iterator Ie1 = value.iterator();
             Ie1.hasNext(); ) {
          Object e;
          e = Ie1.next();
          if (e instanceof coins.sym.Sym) {
            OutString.append(((Sym)e).getName());
          } else {
            OutString.append(e);
          }
        }
      //##70 BEGIN
      }else if (lObject instanceof String) {
        OutString.append(lObject);
      }else {
        OutString.append(lObject.toString());
      //##70 END
      }
      printOut.println(OutString.toString());
      OutString.setLength(0);
    }
  }
}
