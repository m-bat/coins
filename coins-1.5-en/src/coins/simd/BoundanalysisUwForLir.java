/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package  coins.simd;

import  coins.backend.Function;
import  coins.backend.Op;
import  coins.backend.Type;
import  coins.backend.cfg.BasicBlk;
import  coins.backend.lir.LirIconst;
import  coins.backend.lir.LirNode;
import  coins.backend.util.BiLink;
import  coins.util.IntBound;
import  coins.util.IntConst;
import  java.util.Enumeration;
import  java.util.Hashtable;

/**
 * Class for the upward bound analysis.
 */
public class BoundanalysisUwForLir {
  private Function function;
  private Hashtable boundTableUW;
  BoundanalysisUwForLir(Function f) {
    function=f;
    boundTableUW=new Hashtable();
  }
/**
 * Constructs an upward bound analyser.
 * @param  ins  LirNode
 */
  public void boundanalysisUw(LirNode ins) {
    eval(ins,boundTableUW);
  }
/**
 * Calculates a bound of each node.
 * @param  ins  LirNode
 * @param  tbl  Hashtable
 */
  public IntBound eval(LirNode ins,Hashtable tbl) {
    IntBound result=(IntBound)tbl.get(ins);
    if(result!=null) {return result;}
    int code=ins.opCode;
    switch(code) {
      case Op.INTCONST:
        {
          long v=(long)((LirIconst)ins).value;
          IntConst c=IntConst.valueOf(Type.bits(ins.type),v);
          return new IntBound(c);
        }
//      case Op.FLOATCONST:
      case Op.STATIC:
      case Op.FRAME:
      case Op.REG:
        {
          IntConst min=typeToIntConstMin(ins.type);
          IntConst max=typeToIntConstMax(ins.type);
          IntBound b=new IntBound(min,max);
          tbl.put(ins,b);
          return b;
        }
//      case Op.SUBREG:
//      case Op.LABEL:
//      case Op.DEFLABEL:
      case Op.NEG:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=b0.neg();
          tbl.put(ins,b1);
          return b1;
        }
      case Op.BNOT:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=b0.bnot();
          tbl.put(ins,b1);
          return b1;
        }
      case Op.CONVSX:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=b0.convsx(Type.bits(ins.type));
          tbl.put(ins,b1);
          return b1;
        }
      case Op.CONVZX:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=b0.convzx(Type.bits(ins.type));
          tbl.put(ins,b1);
          return b1;
        }
      case Op.CONVIT:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=b0.convit(Type.bits(ins.type));
          tbl.put(ins,b1);
          return b1;
        }
//      case Op.CONVFX:
//      case Op.CONVFT:
//      case Op.CONVFI:
//      case Op.CONVSF:
//      case Op.CONVUF:
      case Op.MEM:
        {
          eval(ins.kid(0),tbl);
          IntConst min=typeToIntConstMin(ins.type);
          IntConst max=typeToIntConstMax(ins.type);
          IntBound b=new IntBound(min,max);
          tbl.put(ins,b);
          return b;
        }
//      case Op.JUMP:
//      case Op.USE:
//      case Op.CLOBBER:
      case Op.ADD:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.add(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.SUB:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.sub(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.MUL:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.mul(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.DIVS:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.divs(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.DIVU:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.divu(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.MODS:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.mods(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.MODU:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.modu(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.BAND:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.band(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.BOR:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.bor(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.BXOR:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.bxor(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.LSHS:
      case Op.LSHU:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.lsh(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.RSHS:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.rshs(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.RSHU:
        {
          IntBound b0=eval(ins.kid(0),tbl);
          IntBound b1=eval(ins.kid(1),tbl);
          IntBound b2=b0.rshu(b1);
          tbl.put(ins,b2);
          return b2;
        }
      case Op.TSTEQ:
      case Op.TSTNE:
      case Op.TSTLTS:
      case Op.TSTLES:
      case Op.TSTGTS:
      case Op.TSTGES:
      case Op.TSTLTU:
      case Op.TSTLEU:
      case Op.TSTGTU:
      case Op.TSTGEU:
        {
          eval(ins.kid(0),tbl);
          eval(ins.kid(1),tbl);
          IntConst c=IntConst.valueOf(Type.bits(ins.type),1);
          IntBound b=new IntBound(c);
          tbl.put(ins,b);
          return b;
        }
      case Op.SET:
        {
          eval(ins.kid(1),tbl);
          return null;
        }
//      case Op.JUMPC:
//      case Op.CALL:
//      case Op.JUMPN:
//      case Op.PROLOGUE:
//      case Op.EPILOGUE:
//      case Op.PARALLEL:
      default:
        return null;
    }
  }
  private IntConst typeToIntConstMax(int type) {
    int s=Type.bits(type);
    switch(s) {
      case 8:
        {
          return IntConst.valueOf(8,0xFFL);
        }
      case 16:
        {
          return IntConst.valueOf(16,0xFFFFL);
        }
      case 32:
        {
          return IntConst.valueOf(32,0xFFFFFFFFL);
        }
      case 64:
        {
          return IntConst.valueOf(64,0xFFFFFFFFFFFFFFFFL);
        }
//      case 128:
//        {
//          return IntConst.valueOf(128,0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFL);
//        }
      default:
        {
          return null;
        }
    }
  }
  private IntConst typeToIntConstMin(int type) {
    return IntConst.valueOf(Type.bits(type),0);
  }
/**
 * Prints out a bound value of each node.
 */
  public void show() {
    if(boundTableUW!=null) {
      Enumeration keys=boundTableUW.keys();
      while(keys.hasMoreElements()) {
        LirNode nd=(LirNode)keys.nextElement();
        System.out.println("KEY:"+nd.toString());
        IntBound bd=(IntBound)boundTableUW.get(nd);
        System.out.println("BOUND:"+bd.toString());
      }
    }
  }
/**
 * Retrieves a corresponding bound value for a node.
 * @param  e  LirNode
 */
  public IntBound get(LirNode e) {
    return (IntBound)boundTableUW.get(e);
  }
/**
 * Stores a bound value for a node.
 * @param  e  LirNode
 * @param  b  IntBound
 */
  public void put(LirNode e,IntBound b) {
    boundTableUW.put(e,b);
  }
}
