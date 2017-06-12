/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.Op;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.Symbol;

import java.util.Comparator;

/**
 * Class for order relation of L-expressions
 */
public class LirOrder implements Comparator {
  private Relation orderRel;
  /**
   * Saturates an order
   */
  public void saturate() throws SimdOptException {
    orderRel.saturate();
  }
  /**
   * Puts a relation to an order relation
   * @param r Relation
   */
  public void put(Relation r) {
    orderRel=r;
  }
  /**
   * Adds to an order relation
   * @param e1 LirNode
   * @param e2 LirNode
   */
  public void add(LirNode e1, LirNode e2) throws SimdOptException {
    orderRel.add(e1, e2);
  }
  /**
   * Compares ordering of objects
   * @param o1 Object
   * @param o2 Object
   * @return int
   */
  public int compare(Object o1, Object o2) {
    int retval=0;
    if((o1 instanceof LirNode)&&(o2 instanceof LirNode)) {
      LirNode e1=(LirNode)o1;
      LirNode e2=(LirNode)o2;
      if(orderRel.isRelated(e1, e2)) retval=-1;
      else if(orderRel.isRelated(e2, e1)) retval=1;
      else {
// for debug
//System.out.println("Unordered:");
//System.out.println("  "+e1);
//System.out.println("  "+e2);
//
        // If two exps are not related, any ordering is allowed.
// Begin(2004.8.26)
//        String s1=name(e1);
//        String s2=name(e2);
//        retval=s1.compareTo(s2);
        retval=-10000;
// End(2004.8.26)
      }
// for debug
//System.out.println("Compare:");
//System.out.println("  "+e1);
//System.out.println("  "+e2);
//System.out.println("  result="+retval);
//
    }
    return retval;
  }
  private String name(LirNode e) {
    switch(e.opCode) {
      case Op.PARALLEL:
      case Op.SET:
        return name(e.kid(0));
      case Op.REG:
        {
          LirSymRef symref=(LirSymRef)e;
          Symbol sym=symref.symbol;
          return sym.name;
        }
      default:
        return e.toString();
    }
  }
  /**
   * Checks if an object is equal to this
   * @param o Object
   * @return boolean
   */
  public boolean equals(Object o) {
    return true;
  }
  /**
   * Prints an order relation
   */
  public void printOrder() { orderRel.printRel(); }
}
