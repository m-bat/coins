/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.util.List;
import java.util.LinkedList;
import java.util.Vector;
import coins.backend.*;
import coins.backend.lir.*;
import coins.backend.sym.*;
import coins.backend.util.BiList;
import coins.backend.util.BiLink;
import coins.backend.util.ImList;

/**
 * LIR utility class for SIMD
 */
public class LirUtil {
  /**
   * Changes a type to bits
   * @param typ int
   * @return int
   */
  public static int typeNum(int typ) {
    String s=Type.toString(typ);
    String t=s.substring(1);
    return Integer.parseInt(t);
  }
  /**
   * Changes a type to its kind(I or F or etc.)
   * @param type int
   * @return String
   */
  public static String typeKind(int typ) {
    String s=Type.toString(typ);
    return s.substring(0, 1);
  }
  /**
   * Returns MEM expression's base symbol
   * @param exp LirNode
   * @return Symbol
   */
  public static Symbol basesymbol(LirNode exp) {
    if(exp.opCode==Op.MEM) {
      return basesymbolIn(exp.kid(0));
    } else {
      return null;
    }
  }
  private static Symbol basesymbolIn(LirNode exp) {
    switch(exp.opCode) {
    case Op.FRAME:
    case Op.REG:
    case Op.STATIC:
      return ((LirSymRef)exp).symbol;
    case Op.ADD:
      if(exp.kid(1).opCode==Op.INTCONST) {
        return basesymbolIn(exp.kid(0));
      }
    default:
      return null;
    }
  }
  /**
   * Returns memory expression's displacement
   * @param exp LirNode
   * @return long
   */
  public static long dispval(LirNode exp) {
    if(exp.opCode==Op.MEM) {
      return dispvalIn(exp.kid(0));
    } else {
      return 0l;
    }
  }
  private static long dispvalIn(LirNode exp) {
    switch(exp.opCode) {
    case Op.INTCONST:
      return ((LirIconst)exp).value;
    case Op.ADD:
      if(exp.kid(1).opCode==Op.INTCONST) return dispvalIn(exp.kid(1));
      return 0l;
    default:
      return 0l;
    }
  }
  /**
   * Returns type's byte
   * @param type int
   * @return long
   */
  public static long calcIncval(int type) {
    return (long)Type.bits(type)/8;
  }
  /**
   * Transforms an ImList into Vector
   * @param xs ImList
   * @return Vector
   */
  public static Vector imlistToVector(ImList xs) {
    Vector out=new Vector();
    while(!xs.atEnd()) {
      out.addElement(xs.elem());
      xs=xs.next();
    }
    return out;
  }
  /**
   * Gets a boundary from a type
   * @param typ int
   * @return int
   */
  public static int calcBoundary(int typ) {
    int boundary;
    switch(Type.bits(typ)) {
      case 8: boundary=1; break;  // Illegal
      case 16: boundary=2; break; // Illegal
      case 32: boundary=4; break; // Illegal
      case 64: boundary=8; break;
      case 128: boundary=16; break;
      default: boundary=8;        // Illegal
    };
    return boundary;
  }
//  public static Vector btov(BiList b) {
//    Vector v=new Vector();
//    for(BiLink lir=b.first();!lir.atEnd();lir=lir.next()) {
//      LirNode ins=(LirNode)lir.elem();
//      if(ins.opCode==Op.SET) v.add(ins);
//    }
//    return v;
//  }
  /**
   * Transforms a BiList into Vector
   * @param b BiList
   * @return Vector
   */
  public static Vector btov(BiList b) {
    Vector v=new Vector();
    for(BiLink lk=b.first();!lk.atEnd();lk=lk.next()) {
      v.add(lk.elem());
    }
    return v;
  }
  /**
   * Transforms a Vecgtor into a BiList
   * @param v Vector
   * @return BiList
   */
  public static BiList vtob(Vector v) {
    BiList b=new BiList();
    for(int i=0;i<v.size();i++) {
      b.add(v.elementAt(i));
    }
    return b;
  }
  /**
   * Transforms a BiList into ImList
   * @param b BiList
   * @return ImList
   */
  public static ImList btoim(BiList b) {
    ImList im=ImList.Empty;
    for(BiLink ln=b.first(); !ln.atEnd(); ln=ln.next()) {
      Object c=ln.elem();
      im=new ImList(c, im);
    }
    return im;
  }
  /**
   * Checks if first LirNode contains second LirNode
   * @param a LirNode
   * @param b LirNode
   * @return boolean
   */
  public static boolean contains(LirNode a, LirNode b) {
    if(a.equals(b)) return true;
    switch(a.opCode) {
      case Op.REG:
      case Op.SUBREG:
      case Op.INTCONST:
      case Op.FLOATCONST:
        return false;
      default:
        {
          for(int i=0; i<a.nKids(); i++) {
            if(contains(a.kid(i), b)) return true;
          }
          return false;
        }
    }
  }
  /**
   * Checks if first LirNode is contained in second LirNode
   * @param a LirNode
   * @param b LirNode
   * @return boolean
   */
  public static boolean isUsed(LirNode a, LirNode b) {
    switch(b.opCode) {
      case Op.SET:
        {
          return contains(b.kid(1), a);
        }
      case Op.PARALLEL:
        {
          for(int i=0; i<b.nKids(); i++) {
            if(isUsed(a, b.kid(i))) return true;
          }
          return false;
        }
      default:
        {
          for(int i=0; i<b.nKids(); i++) {
            if(contains(b.kid(i), a)) return true;
          }
          return false;
        }
    }
  }
  public static LirNode[] pickupDefReferent(LirNode e) throws SimdOptException {
    Vector v=new Vector();
    pickupDefReferent(e, v);
    LirNode[] out=new LirNode[v.size()];
    for(int i=0; i<v.size(); i++) out[i]=(LirNode)v.elementAt(i);
    return out;
  }
  public static void pickupDefReferent(LirNode e, Vector v) throws SimdOptException {
    switch(e.opCode) {
      case Op.REG:
      case Op.MEM:
        {
          v.addElement(e);
          break;
        }
      case Op.USE:
      case Op.CLOBBER:
      case Op.SET:
      case Op.PARALLEL:
        {
          throw new SimdOptException("Unexpected operator");
        }
      default:
        break;
    }
  }
  public static LirNode[] pickupUseReferent(LirNode e) throws SimdOptException {
    Vector v=new Vector();
    pickupReferent(e, v);
    if(e.opCode==Op.MEM) {
      v.remove(e);
    }
    LirNode[] out=new LirNode[v.size()];
    for(int i=0; i<v.size(); i++) out[i]=(LirNode)v.elementAt(i);
    return out;
  }
  public static LirNode[] pickupReferent(LirNode e) throws SimdOptException {
    Vector v=new Vector();
    pickupReferent(e, v);
    LirNode[] out=new LirNode[v.size()];
    for(int i=0; i<v.size(); i++) out[i]=(LirNode)v.elementAt(i);
    return out;
  }
  public static void pickupReferent(LirNode e, Vector v) throws SimdOptException {
    switch(e.opCode) {
      case Op.INTCONST:
      case Op.FLOATCONST:
        {
          break;
        }
      case Op.REG:
        {
          v.addElement(e);
          break;
        }
      case Op.MEM:
        {
          v.addElement(e);
          for(int i=0; i<e.nKids(); i++) pickupReferent(e.kid(i), v);
          break;
        }
      case Op.STATIC:
        break;
      case Op.SUBREG:
      case Op.USE:
      case Op.CLOBBER:
        {
          throw new SimdOptException("Unexpected operator");
        }
      case Op.SET:
        {
          pickupReferent(e.kid(1), v);
          pickupReferent(e.kid(0), v);
          break;
        }
      case Op.PARALLEL:
      default:
        {
          for(int i=0; i<e.nKids(); i++) pickupReferent(e.kid(i), v);
          break;
        }
    }
  }
  /**
   * Makes a LirNode
   * @param factory LirFactory
   * @param c int
   * @param t int
   * @param srcs LirNode[]
   * @param opt ImList
   * @return LirNode
   */
  public static LirNode operator(LirFactory factory, int c,int t,LirNode srcs[],ImList opt) {
    int size=srcs.length;
    if(size==0) throw new IllegalArgumentException("No children.");
    if(size==1) return factory.operator(c,t,srcs[0],opt);
    if(size==2) return factory.operator(c,t,srcs[0],srcs[1],opt);
    return factory.operator(c,t,srcs,opt);
  }
  /**
   * Checks if a LirNode's operator is "shift"
   * @param e LirNode
   * @return boolean
   */
  public static boolean isShiftOp(LirNode e) {
    return (e.opCode==Op.LSHS || e.opCode==Op.LSHU
            || e.opCode==Op.RSHS || e.opCode==Op.RSHU);
  }
}
