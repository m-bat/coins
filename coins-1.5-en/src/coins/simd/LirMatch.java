/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.util.List;
import java.util.LinkedList;
import java.util.Vector;
import coins.backend.Op;
import coins.backend.lir.LirBinOp;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirFconst;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNaryOp;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.lir.LirUnaOp;
import coins.backend.sym.Symbol;
import coins.backend.util.ImList;

/**
 * Matching of patterns and LirNode.
 */
public class LirMatch {
  /**
   * LirFactory
   */
  public LirFactory newLir;
  /**
   *
   * @param x an LirNode, which is a pattern.
   * @param y an LirNode
   * @param env an LirNode array, whose index is used as a hole number.
   * @return boolean, which represents matching is successful or not.
   */
  public boolean match(LirNode x,LirNode y,LirNode[] env) {
//System.out.println("match("+x+"\n  "+y+")");
    if(x==y) {
//System.out.println("  x==y: true");
      return true;
    }
    if(x.opCode==Op.HOLE) {
//System.out.println("  HOLE:");
      if(x.type!=y.type) {
//System.out.println("  type not matched.");
        return false;
      }
      int n=(int)((LirIconst)((LirUnaOp)x).kid(0)).value;
      if(env[n]!=null) {
//System.out.println("  env["+n+"]:"+y.equals(env[n]));
        return(y.equals(env[n]));
      }
      env[n]=y;
//System.out.println("  matching succeeds.");
      return true;
    };
    if(x.opCode!=y.opCode||x.type!=y.type) {
//System.out.println("  opCode or type unmatched.");
      return false;
    }
    if((x instanceof LirIconst) && (y instanceof LirIconst)) {
//System.out.println("  ");
      return((int)((LirIconst)x).value==(int)((LirIconst)y).value);
    }
    if((x instanceof LirFconst) && (y instanceof LirFconst)) {
//System.out.println("  ");
      return((double)((LirFconst)x).value==(double)((LirFconst)y).value);
    }
    if((x instanceof LirSymRef) && (y instanceof LirSymRef)) {
//System.out.println("  ");
      return((Symbol)((LirSymRef)x).symbol==(Symbol)((LirSymRef)y).symbol);
    }
    // Cases of LirUnaOp,LirBinOp,LirNaryOp
    int n=x.nKids();
    if(n!=y.nKids()) {
//System.out.println("  Different number of kids.");
      return false;
    }
    for(int i=0;i<n;i++) {
      if(!match(x.kid(i),y.kid(i),env)) {
//System.out.println("  "+i+" th kid: unmatched.");
        return false;
      }
    }
//System.out.println("  matching succeeds.");
    return true;
  }
  public LirNode replace(LirNode e,LirNode[] env) {
    if(e.opCode==Op.HOLE) {
      return env[(int)((LirIconst)((LirUnaOp)e).kid(0)).value];
    }
    if(e instanceof LirUnaOp) {
      LirNode esub=replace(e.kid(0),env);
      if(esub.equals(e.kid(0))) return e;
      return newLir.operator(e.opCode,e.type,esub,ImList.Empty);
    };
    if(e instanceof LirBinOp) {
      LirNode esub0=replace(e.kid(0),env);
      LirNode esub1=replace(e.kid(1),env);
      if(esub0.equals(e.kid(0)) && esub1.equals(e.kid(1)))
        return e;
      return newLir.operator(e.opCode,e.type,esub0,esub1,ImList.Empty);
    };
    if(e instanceof LirNaryOp) {
      int n=e.nKids();
      LirNode[] src=new LirNode[n];
      for(int i=0;i<n;i++) src[i]=replace(e.kid(i),env);
      for(int i=0;i<n;i++) {
        if(!src[i].equals(e.kid(i)))
          return newLir.operator(e.opCode,e.type,src,ImList.Empty);
      };
      return e;
    }
    return e;
  }
  public boolean matchReg(LirNode x,LirNode y,Vector xv,Vector yv) {
    if(x==y) return true;
    if(x.opCode==Op.REG) {
      for(int i=0;i<xv.size();i++) {
        if(((LirNode)xv.elementAt(i)).opCode==Op.REG
          && ((LirSymRef)xv.elementAt(i)).symbol==((LirSymRef)x).symbol
          && isEqual((LirNode)yv.elementAt(i), y))
            return true;
//System.out.println("isEqual:"+isEqual((LirNode)yv.elementAt(i), y));
//System.out.println("  1:"+(LirNode)yv.elementAt(i));
//System.out.println("  2:"+y);
      }
//System.out.println("matchReg add:");
//System.out.println("  x:"+x);
//System.out.println("  y:"+y);
      xv.addElement(x);
      yv.addElement(y);
      return true;
    }
    if(x.opCode!=y.opCode||x.type!=y.type) return false;
    if((x instanceof LirIconst) && (y instanceof LirIconst))
      return((int)((LirIconst)x).value==(int)((LirIconst)y).value);
    if((x instanceof LirFconst) && (y instanceof LirFconst))
      return((double)((LirFconst)x).value==(double)((LirFconst)y).value);
    if((x instanceof LirSymRef) && (y instanceof LirSymRef))
      return((Symbol)((LirSymRef)x).symbol==(Symbol)((LirSymRef)y).symbol);
    if((x instanceof LirUnaOp) && (y instanceof LirUnaOp))
      return matchReg(x.kid(0),y.kid(0),xv,yv);
    if((x instanceof LirBinOp) && (y instanceof LirBinOp))
      return(matchReg(x.kid(0),y.kid(0),xv,yv)&&matchReg(x.kid(1),y.kid(1),xv,yv));
    if((x instanceof LirNaryOp) && (y instanceof LirNaryOp)) {
      int n=x.nKids();
      if(n!=y.nKids()) return false;
      for(int i=0;i<n;i++) {
        if(!matchReg(x.kid(i),y.kid(i),xv,yv)) return false;
      };
      return true;
    };
    return false;
  }
  private boolean isEqual(LirNode x, LirNode y) {
    if(x==y) return true;
    if(x.opCode!=y.opCode) return false;
    switch(x.opCode) {
      case Op.INTCONST:
        {
          return x==y;
        }
      case Op.REG:
        {
          return(((LirSymRef)x).symbol==((LirSymRef)y).symbol);
        }
      default :
        {
          for(int i=0; i<x.nKids(); i++) {
            if(!isEqual(x.kid(i), y.kid(i))) return false;
          }
          return true;
        }
    }
  }
}
