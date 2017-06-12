/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import  coins.backend.Function;
import  coins.backend.Op;
import  coins.backend.Type;
import  coins.backend.lir.LirFactory;
import  coins.backend.lir.LirIconst;
import  coins.backend.lir.LirNode;
import  coins.backend.util.ImList;
import  coins.util.IntConst;
import  coins.util.IntLive;

/**
 * Class for the size conversion after the bound analysis.
 */
public class SizeConv {
  private BoundanalysisDwForLir badw;
  private Function function;
  private LirFactory factory;
  /**
   * Constructs a size converter.
   * @param  f  Function
   * @param  bd BoundanalysisDwForLir
   */
  SizeConv(Function f,BoundanalysisDwForLir bd) {
    badw=bd;
    function=f;
    factory=f.newLir;
  }
  /**
   * Converts a LirNode.
   * @param  ins  LirNode
   */
  public LirNode convert(LirNode ins) {
    if(ins.opCode!=Op.SET) return ins;
    LirNode insbody=reduce(convert2(ins.kid(1),ins.type));
    return factory.operator(ins.opCode,ins.type,ins.kid(0),insbody,ImList.Empty);
  }
  private LirNode convert2(LirNode ins,int ty) {
    if(ins.opCode==Op.INTCONST) {
      if(Type.bits(intToType(ins))<Type.bits(ty)) 
        return factory.operator(Op.CONVSX,ins.type,
               factory.operator(Op.CONVIT,ty,ins,ImList.Empty),ImList.Empty);
      return factory.operator(Op.CONVSX,ins.type,
               factory.operator(Op.CONVIT,intToType(ins),ins,ImList.Empty),ImList.Empty);
    };
    IntLive lv=(IntLive)badw.get(ins);
    if(lv==null) throw new IllegalArgumentException(ins.toString()+"has no value");
    // Make a new type from live bits.
    int tp=bitsToType(lv);
    if(Type.bits(tp)<Type.bits(ty)) tp=ty;
    // For MEM/REG
// Begin(2004.10.21)
//    if(ins.opCode==Op.MEM || ins.opCode==Op.REG) {
    if(ins.opCode==Op.MEM || ins.opCode==Op.REG || ins.opCode==Op.FRAME) {
// End(2004.10.21)
      if(Type.bits(ins.type)<=Type.bits(tp)) return ins;
      return factory.operator(Op.CONVSX,ins.type,
               factory.operator(Op.CONVIT,tp,ins,ImList.Empty),ImList.Empty);
    }
    // Convert children.
    LirNode[] children=new LirNode[ins.nKids()];
    for(int i=0;i<ins.nKids();i++) children[i]=convert2(ins.kid(i),tp);
    if(Type.bits(ins.type)<=Type.bits(tp)) {
      return operator(ins.opCode,ins.type,children,ImList.Empty);
    }
    return factory.operator(Op.CONVSX,ins.type,
                    factory.operator(Op.CONVIT,tp,
                             operator(ins.opCode,ins.type,children,ImList.Empty),ImList.Empty),ImList.Empty);
  }
  /**
   * Translates live bits to a type.
   * @param  lv  IntLive
   */
  private int bitsToType(IntLive lv) {
    try {
      return Type.decode("I"+adjustTypenum(leftmostPos(lv)));
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Type generation error:"+lv.toString());
    }
  }
  private int adjustTypenum(int n) {
    if(n>128) throw new IllegalArgumentException("Too large type"+n);
    if(n>64) return 128;
    if(n>32) return 64;
    if(n>16) return 32;
    if(n>8) return 16;
    return 8;
  }
  private int leftmostPos(IntLive lv) {
    IntConst c=lv.intConstValue();
//    int i=c.size-1;
    int i=c.size()-1;
//    IntConst t=IntConst.valueOf(c.size,1).lsh(i);
    IntConst t=IntConst.valueOf(c.size(),1).lsh(i);
    for(;i>=0;i--) {
      if(t.band(c).equals(t)) break;
      t=t.rshu(1);
    }
    return i;
  }
  private LirNode operator(int c,int t,LirNode srcs[],ImList opt) {
    int size=srcs.length;
    if(size==0) throw new IllegalArgumentException("No children.");
    if(size==1) return factory.operator(c,t,srcs[0],opt);
    if(size==2) return factory.operator(c,t,srcs[0],srcs[1],opt);
    return factory.operator(c,t,srcs,opt);
  }
  /**
   * Reduces size convertible patterns.
   * "CONVIT-OPERATOR-CONVSX" is a size convertible pattern, and replaced to
   * "OPERATOR" with a shrinked size type.
   * @param  ins  LirNode
   */
  private LirNode reduce(LirNode ins) {
//System.out.println("Reduce:"+ins.toString());
//    return redSimpleConv(reduce2(ins));
    return redSimpleConv(reduce2(ins, ins.type));
  }
//  private LirNode reduce2(LirNode ins) {
//    if(ins.opCode==Op.INTCONST || ins.opCode==Op.REG || ins.opCode==Op.MEM)
//      return ins;
//    if(reducible(ins)) return reduce3(ins);
//    LirNode[] children=new LirNode[ins.nKids()];
//    for(int i=0;i<ins.nKids();i++) children[i]=reduce2(ins.kid(i));
//    return operator(ins.opCode,ins.type,children,ImList.Empty);
//  }
  private LirNode reduce2(LirNode ins, int ty) {
// Begin(2004.10.21)
//    if(ins.opCode==Op.INTCONST || ins.opCode==Op.REG || ins.opCode==Op.MEM)
    if(ins.opCode==Op.INTCONST ||
        ins.opCode==Op.REG || ins.opCode==Op.MEM || ins.opCode==Op.FRAME)
// End(2004.10.21)
      return chngType(ins, ty);
    if(reducible(ins)) return reduce3(ins, ty);
    // otherwise
    LirNode[] children=new LirNode[ins.nKids()];
    for(int i=0;i<ins.nKids();i++) children[i]=reduce2(ins.kid(i), ty);
    return operator(ins.opCode,ty,children,ImList.Empty);
  }
//  private LirNode reduce3(LirNode ins) {
//    if(ins.opCode!=Op.CONVIT)
//      throw new IllegalArgumentException("reduce3"+ins.toString());
//    LirNode inschild=(LirNode)ins.kid(0);
//    LirNode[] children=new LirNode[inschild.nKids()];
//    for(int i=0;i<inschild.nKids();i++) {
//      children[i]=reduce4(inschild.kid(i));
//    };
//    return operator(inschild.opCode,ins.type,children,ImList.Empty);
//  }
  private LirNode reduce3(LirNode ins, int ty) {
    if(ins.opCode!=Op.CONVIT)
      throw new IllegalArgumentException("reduce3"+ins.toString());
    LirNode inschild=(LirNode)ins.kid(0);
    LirNode[] children=new LirNode[inschild.nKids()];
    if(Type.bits(ins.type)<Type.bits(ty)) ty=ins.type;
    for(int i=0;i<inschild.nKids();i++) {
      children[i]=reduce4(inschild.kid(i), ty);
    };
    return operator(inschild.opCode,ty,children,ImList.Empty);
  }
//  private LirNode reduce4(LirNode ins) {
//    if(ins.opCode==Op.INTCONST || ins.opCode==Op.REG || ins.opCode==Op.MEM)
//      return ins;
//    if(ins.opCode!=Op.CONVSX)
//      throw new IllegalArgumentException("reduce4"+ins.toString());
//    return reduce2(ins.kid(0));
//  }
  private LirNode reduce4(LirNode ins, int ty) {
// Begin(2004.10.21)
//    if(ins.opCode==Op.INTCONST || ins.opCode==Op.REG || ins.opCode==Op.MEM)
    if(ins.opCode==Op.INTCONST ||
        ins.opCode==Op.REG || ins.opCode==Op.MEM || ins.opCode==Op.FRAME)
// End(2004.10.21)
      return chngType(ins, ty);
    if(ins.opCode!=Op.CONVSX)
      throw new IllegalArgumentException("reduce4"+ins.toString());
    return reduce2(ins.kid(0), ty);
  }
  private LirNode chngType(LirNode ins, int ty) {
    switch(ins.opCode) {
      case Op.INTCONST:
        {
          if(Type.bits(ins.type)!=Type.bits(ty)) {
            return factory.iconst(ty, ((LirIconst)ins).value, ins.opt);
          }
          else {
            return ins;
          }
        }
      case Op.REG:
      case Op.MEM:
// Begin(2004.10.21)
      case Op.FRAME:
// End(2004.10.21)
        {
          if(Type.bits(ins.type)<Type.bits(ty)) {
            LirNode[] children=new LirNode[1];
            children[0]=ins;
            return factory.operator(Op.CONVSX, ty, children, ImList.Empty);
          }
          else if(Type.bits(ins.type)>Type.bits(ty)) {
            LirNode[] children=new LirNode[1];
            children[0]=ins;
            return factory.operator(Op.CONVIT, ty, children, ImList.Empty);
          }
          else {
            return ins;
          }
        }
      default:
        throw new IllegalArgumentException("chngType"+ins.toString());
    }
  }
  private boolean reducible(LirNode ins) {
    if(ins.opCode==Op.CONVIT) {
// Begin(2004.10.21)
//      if(ins.kid(0).opCode==Op.INTCONST || ins.kid(0).opCode==Op.REG
//          || ins.kid(0).opCode==Op.MEM) return false;
      if(ins.kid(0).opCode==Op.INTCONST ||
          ins.kid(0).opCode==Op.REG || ins.kid(0).opCode==Op.MEM || ins.kid(0).opCode==Op.FRAME)
        return false;
// End(2004.10.21)
      if(ins.kid(0).opCode==Op.LSHS || ins.kid(0).opCode==Op.LSHU
          || ins.kid(0).opCode==Op.RSHS || ins.kid(0).opCode==Op.RSHU) {
        if(ins.kid(0).kid(0).opCode!=Op.CONVSX) return false;
        return true;
      };
      for(int i=0;i<ins.kid(0).nKids();i++) {
        if(ins.kid(0).kid(i).opCode!=Op.CONVSX) return false;
      };
      return true;
    }
    else return false;
  }
  private LirNode redSimpleConv(LirNode ins) {
// Begin(2004.10.21)
//    if(ins.opCode==Op.INTCONST || ins.opCode==Op.REG || ins.opCode==Op.MEM)
    if(ins.opCode==Op.INTCONST ||
        ins.opCode==Op.REG || ins.opCode==Op.MEM || ins.opCode==Op.FRAME)
// End(2004.10.21)
      return ins;
    if(isCONV(ins) && ins.type==ins.kid(0).type)
      return redSimpleConv(ins.kid(0));
    if(ins.opCode==Op.CONVIT) {
      LirNode nxt=ins.kid(0);
      if((nxt.opCode==Op.CONVSX || nxt.opCode==Op.CONVZX) &&
         ins.type==nxt.kid(0).type)
          return redSimpleConv(nxt.kid(0));
      if(nxt.opCode==Op.INTCONST) {
        if(Type.bits(ins.type)<Type.bits(nxt.type))
          return factory.iconst(ins.type,((LirIconst)nxt).value,ImList.Empty);
      }
    };
    if(ins.opCode==Op.CONVSX) {
      LirNode nxt=ins.kid(0);
      if(nxt.opCode==Op.CONVIT && ins.type==nxt.type)
        return redSimpleConv(nxt.kid(0));
    };
    LirNode[] children=new LirNode[ins.nKids()];
    for(int i=0;i<ins.nKids();i++) {
      children[i]=redSimpleConv(ins.kid(i));
    };
    return operator(ins.opCode,ins.type,children,ImList.Empty);
  }
  private boolean isCONV(LirNode exp) {
    return (exp.opCode==Op.CONVIT || exp.opCode==Op.CONVSX ||
            exp.opCode==Op.CONVZX);
  }
  private int intToType(LirNode ins) {
    if(!(ins instanceof LirIconst))
      throw new IllegalArgumentException("Not LirIconst:"+ins.toString());
    LirIconst ic=(LirIconst)ins;
    IntConst c=IntConst.valueOf(Type.bits(ic.type),ic.value);
    IntLive lv=IntLive.valueOf(c);
    return bitsToType(lv);
  }
}
