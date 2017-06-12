/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.util.Vector;
import coins.backend.Function;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.Symbol;
import coins.backend.util.ImList;

/**
 * Class for combining memory access L-expressions
 */
public class ContigMemAccess {
  private LirFactory newLir;
  private int[] cnts8={16, 8};
  private int[] cnts16={8, 4};
  private int[] cnts32={4, 2};
  private int[] cnts64={2};
  private Vector paraRegs;

  /**
   * Constructs a ContigMemAccess object
   * @param f Function
   * @param paraRegs Vector
   */
  ContigMemAccess(Function f, Vector paraRegs) {
    newLir=f.newLir;
    this.paraRegs=paraRegs;
  }
  /**
   * Combines L-expressions of {loading from/storing to} memory
   * @param in Vector
   */
  public Vector collectMemAccess(Vector in) {
    int n=in.size();
    LirNode[] ia=new LirNode[n];
    in.copyInto(ia);
    collectMemLoad(ia);
    collectMemStore(ia);
    Vector out=new Vector();
    for(int i=0;i<n;i++) if(ia[i]!=null) out.addElement(ia[i]);
    return out;
  }
  private void collectMemLoad(LirNode[] ia) {
    int i,j;
    for(i=0;i<ia.length;i++) {
      if(ia[i]==null) continue;
      if(ia[i].opCode==Op.PARALLEL) continue;
      if(!isMemLoadLir(ia[i])) continue;
      int[] a=new int[SimdOpt.MAX_SIMD_PARALLEL];
      int k=pickupMemLoadLir(ia, i, a);
      if(k==0) continue;
      LirNode[] instArray=new LirNode[k+1];
      for(j=0;j<k;j++) {
        instArray[j+1]=ia[a[j]]; ia[a[j]]=null;
      }
      instArray[0]=ia[i];
      Vector regs=new Vector();
      for(int n=0; n<k+1; n++) {
        LirNode inst=instArray[n];
        regs.add(inst.kid(0));
      }
      paraRegs.add(regs);
      ia[i]=null;
      int lpos=i;
      LirNode inst=
        newLir.operator(Op.PARALLEL,Type.UNKNOWN,instArray, ImList.Empty);
//      if(!isDefined(inst,ia,i+1,lpos)) {
//        ia[i]=inst;
//      } else if(!isUsed(inst,ia,i+1,lpos)) {
//        ia[lpos]=inst;
//      } else {
//        Util.abort("combineSimd:Def-Use relation broken! >> "+inst.toString());
//      }
      ia[lpos]=inst;
    }
  }
  private void collectMemStore(LirNode[] ia) {
    int i,j;
    for(i=0;i<ia.length;i++) {
      if(ia[i]==null) continue;
      if(ia[i].opCode==Op.PARALLEL) continue;
      if(!isMemStoreLir(ia[i])) continue;
      int[] a=new int[SimdOpt.MAX_SIMD_PARALLEL];
      int k=pickupMemStoreLir(ia, i, a);
      if(k==0) continue;
      LirNode[] instArray=new LirNode[k+1];
      for(j=0;j<k;j++) {
        instArray[j+1]=ia[a[j]]; ia[a[j]]=null;
      }
      instArray[0]=ia[i];
      ia[i]=null;
      int lpos=i;
      LirNode inst=
        newLir.operator(Op.PARALLEL,Type.UNKNOWN,instArray, ImList.Empty);
//      if(!isDefined(inst,ia,i+1,lpos)) {
//        ia[i]=inst;
//      } else if(!isUsed(inst,ia,i+1,lpos)) {
//        ia[lpos]=inst;
//      } else {
//        Util.abort("combineSimd:Def-Use relation broken! >> "+inst.toString());
//      }
      ia[lpos]=inst;
    }
  }
  private boolean isMemLoadLir(LirNode inst) {
//    if(inst.opCode!=Op.SET) return false;
//    if(inst.kid(0).opCode!=Op.REG) return false;
//    if(inst.kid(1).opCode==Op.MEM) return true;
//    return false;
    return inst.opCode==Op.SET && inst.kid(0).opCode==Op.REG
            && inst.kid(1).opCode==Op.MEM;
  }
  private boolean isMemStoreLir(LirNode inst) {
//    if(inst.opCode!=Op.SET) return false;
//    if(inst.kid(1).opCode==Op.MEM) return true;
//    return false;
    return inst.opCode==Op.SET && inst.kid(0).opCode==Op.MEM;
  }
  private int pickupMemLoadLir(LirNode[] ia, int i, int[] a) {
    int k=0;
    if(ia[i].opCode!=Op.SET) return 0;
    if(ia[i].kid(1).opCode!=Op.MEM) return 0;
    Symbol basesymbol=LirUtil.basesymbol(ia[i].kid(1));
    if(basesymbol==null) return 0;
    long currentval=LirUtil.dispval(ia[i].kid(1));
    long incval=LirUtil.calcIncval(ia[i].kid(1).type);
    int typ=ia[i].type;
    int[] b=new int[a.length];
    for(int j=i+1; j<ia.length; j++) {
      if(ia[j]==null) continue;
      if(ia[j].opCode!=Op.SET) continue;
      if(ia[j].type!=typ) continue;
      if(LirUtil.basesymbol(ia[j].kid(1))!=basesymbol) continue;
      if((LirUtil.dispval(ia[j].kid(1))-currentval)%incval!=0) continue;
      b[k++]=j;
    }
    // Order a[j] (j=0,...,k-1) in ia[a[j]]'s displacement.
    int kc=0;
    for(int j=0; j<k; j++) {
      currentval+=incval;
      int jc;
      for(jc=0; jc<k; jc++) {
        if(b[jc]<0) continue;
        if(LirUtil.dispval(ia[b[jc]].kid(1))==currentval) break;
      }
      if(jc<k) {
        a[kc++]=b[jc];
        b[jc]=-1;
      }
    }
    k=kc;
    int[] cnts=adjustnums(typ);
    if(cnts==null) return 0;
    for(int n=0; n<cnts.length; n++) {
      int m=cnts[n];
      if(k>=m-1) return m-1;
    }
    return 0;
  }
  private int pickupMemStoreLir(LirNode[] ia, int i, int[] a) {
    int k=0;
    if(ia[i].opCode!=Op.SET) return 0;
    if(ia[i].kid(0).opCode!=Op.MEM) return 0;
    Symbol basesymbol=LirUtil.basesymbol(ia[i].kid(0));
    if(basesymbol==null) return 0;
    long currentval=LirUtil.dispval(ia[i].kid(0));
    long incval=LirUtil.calcIncval(ia[i].kid(1).type);
    int typ=ia[i].type;
    int[] b=new int[a.length];
    for(int j=i+1; j<ia.length; j++) {
      if(ia[j]==null) continue;
      if(ia[j].opCode!=Op.SET) continue;
      if(ia[j].type!=typ) continue;
      if(LirUtil.basesymbol(ia[j].kid(0))!=basesymbol) continue;
      if((LirUtil.dispval(ia[j].kid(0))-currentval)%incval!=0) continue;
      LirNode src=ia[j].kid(1);
      if(src.opCode==Op.INTCONST || src.opCode==Op.FLOATCONST) continue;
      b[k++]=j;
    }
    // Order a[j] (j=0,...,k-1) in ia[a[j]]'s displacement.
    int kc=0;
    for(int j=0; j<k; j++) {
      currentval+=incval;
      int jc;
      for(jc=0; jc<k; jc++) {
        if(b[jc]<0) continue;
        if(LirUtil.dispval(ia[b[jc]].kid(0))==currentval) break;
      }
      if(jc<k) {
        a[kc++]=b[jc];
        b[jc]=-1;
      }
    }
    k=kc;
    int[] cnts=adjustnums(typ);
    if(cnts==null) return 0;
    for(int n=0; n<cnts.length; n++) {
      int m=cnts[n];
      if(k>=m-1) return m-1;
    }
    return 0;
  }
/*
  private Symbol basesymbol(LirNode exp) {
    if(exp.opCode==Op.MEM) {
      return basesymbolIn(exp.kid(0));
    } else {
      return null;
    }
  }
  private Symbol basesymbolIn(LirNode exp) {
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
  private long dispval(LirNode exp) {
    if(exp.opCode==Op.MEM) {
      return dispvalIn(exp.kid(0));
    } else {
      return 0l;
    }
  }
  private long dispvalIn(LirNode exp) {
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
  private long calcIncval(int type) {
    return (long)Type.bits(type)/8;
  }
 */
  private int[] adjustnums(int type) {
    switch(Type.bits(type)) {
      case 8:
        return cnts8;
      case 16:
        return cnts16;
      case 32:
        return cnts32;
      case 64:
        return cnts64;
      default:
        return null;
    }
  }
}
