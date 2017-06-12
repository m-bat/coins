/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.Op;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirNode;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;

import java.util.List;
import java.util.Vector;

/**
 * Utility class for SIMD optimization classes.
 */
public class Util {
  Util(){}

  /**
   * Search and find the LIR nodes which has the specified operation code.
   * @param root The root node of the search
   * @param opCode The specified operation code
   * @param l The list which is stored in the found LIR node
   * @return The list of LIR nodes
   **/
  BiList findTargetLir(LirNode root,int opCode,BiList l){
    if(root!=null){
      if(root.opCode==opCode){// && !l.contains(root)){
        //env.output.println("Util.java : "+root);
        l.add(root);
      }
      for(int i=0;i<root.nKids();i++){
        findTargetLir(root.kid(i),opCode,l);
      }
    }
    return(l);
  }

  /*****************/
  /* PUBLIC METHOD */
  /*****************/
  /**
   * Tests a condition, and prints messages and exits if it is false.
   * @param p    boolean which represents a condition.
   * @param s    String which represents a description of the condition.
   * @param lineno  int which represents a line number.
   */
  public static void assert2(boolean p, String s, int lineno)
      throws SimdOptException {
    if ( !p ) {
// Begin(2005.2.10)
//      System.err.println("Assertion failed: "+s+", line "+lineno);
//      System.exit(2);
      throw new SimdOptException("Assertion failed: "+s+", line "+lineno);
// End(2005.2.10)
    }
  }
  /**
   * Tests a condition, and prints messages and exits if it is false.
   * @param p    boolean which represents a condition.
   * @param s    String which represents a description of the condition.
   */
  public static void assert2(boolean p,String s)
      throws SimdOptException {
    if(!p) {
// Begin(2005.2.10)
//      System.err.println("Assertion failed: "+s);
//      System.exit(2);
      throw new SimdOptException("Assertion failed: "+s);
// End(2005.2.10)
    }
  }

  /**
   * Prints an error message and aborts.
   * @param s    Strint which represents an error message.
   */
  public static void abort(String s)
      throws SimdOptException {
// Begin(2005.2.10)
//    System.err.println("Abort: "+s);
//    System.exit(2);
    throw new SimdOptException("Abort: "+s);
// End(2005.2.10)
  }
  /**
   * Tests a condition, and prints a message and exits if it is true.
   * @param  p boolean which represents a condition.
   * @param  s String which represents an error message.
   */
public static void sorry(boolean p,String s)
      throws SimdOptException {
    if(p) {
// Begin(2005.2.10)
//      System.err.println("Sorry not implemented: "+s);
//      System.exit(2);
      throw new SimdOptException("Sorry not implemented: "+s);
// End(2005.2.10)
    }
  }
  /**
   * Prints an error message.
   * @param  s  String which represents an error message.
   */
public static void warn(String s) {
  System.err.println("Warning: "+s);
}
public static void printBlk(BasicBlk blk) {
  for(BiLink lir=blk.instrList().first();!lir.atEnd();lir=lir.next()){
    LirNode ins=(LirNode)lir.elem();
    System.out.println(ins.toString());
  }
}
public static Vector blkToVecs(BasicBlk blk) {
  Vector out=new Vector();
  Vector tmp=new Vector();
  boolean setflag=true;
  for(BiLink lir=blk.instrList().first(); !lir.atEnd(); lir=lir.next()) {
    LirNode ins=(LirNode)lir.elem();
    if(ins.opCode==Op.SET) {
      if(!setflag) {
        setflag=true;
        out.addElement(tmp);
        tmp=new Vector();
      }
      tmp.addElement(ins);
    } else {
      setflag=false;
      out.addElement(tmp);
      tmp=new Vector();
      tmp.addElement(ins);
    }
  }
  if(tmp.size()!=0) out.addElement(tmp);
  return out;
}
public static void vecsToBlk(BasicBlk blk, Vector vs) {
  BiList instlist=blk.instrList();
  instlist.clear();
  for(int i=0; i<vs.size(); i++) {
    if(vs.elementAt(i) instanceof Vector) {
      Vector v=(Vector)vs.elementAt(i);
      for(int j=0; j<v.size(); j++) {
        instlist.add(v.elementAt(j));
      }
    }
  }
}
public static void revV(Vector v) {
  int n=v.size()/2;
  for(int i=0; i<n; i++) {
    Object o1=v.elementAt(i);
    v.setElementAt(v.elementAt(v.size()-i), i);
    v.setElementAt(o1, v.size()-i);
  }
}
}
