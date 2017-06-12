/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.Function;
import coins.backend.Op;
import coins.backend.Storage;
import coins.backend.SyntaxError;
import coins.backend.Type;
import coins.backend.lir.LirBinOp;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirFconst;
import coins.backend.lir.LirLabelRef;
import coins.backend.lir.LirNaryOp;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.lir.LirUnaOp;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Class for SIMD level optimization
 */
public class SimdOpt {
  /**
   * Maximum size of a matching environment.
   */
  private static final int MAX_ENV_SIZE=33;
  /**
   * Maximum size of elements in SIMD Parallel instructions
   */
  public static final int MAX_SIMD_PARALLEL=16;
  /**
   * Flag of printing out trace informations
   */
  public boolean messageFlag=false;
  /**
   * ImList of live registers
   */
  private ImList liveRegList;
  /**
   * Function in which SIMD instructions are included
   */
  private Function func;
  /**
   * LirFactory of Function func
   */
  private LirFactory newLir;
  /**
   * Basic operation list
   */
  private LirBopList bopList;
  /**
   * Bone list
   */
  private LirBoneList boneList;
  private static int tmpgen=0;
  /**
   * A number from which a register is temporally created and then increased.
   */
  private int tmpReg=0;

  private RegGroups rgroups;
  private ContigMemAccess cma;;
  private Vector front;
  private Vector back;
  private Vector paraRegs;

// Begin(2005.3.4)
  private HashMap constantTable;
// End(2005.3.4)

  private boolean traceOK(String tag, int thval) {
    return messageFlag;
  }

  /**
   * Constructs a SimdOpt object and initialize bopList,boneList and simdReg.
   * @param  f  Function
   */
  public SimdOpt(Function f) {
    func=f;
    newLir=f.newLir;
    bopList=new LirBopList_x86(f);
    boneList=new LirBoneList_x86(f);
// Begin(2005.2.7)
//    bopList=(LirBop)(Class.forName("coins.simd.LirBop_"+targetName)).newInstance();
//    bopList.init(f);
//    boneList=(LirBoneList)(Class.forName("coins.simd.LirBoneList_"+targetName)).newInstance();
//    boneList.init(f);
// End(2005.2.7)
    initSimdReg();
    paraRegs=new Vector();
    cma=new ContigMemAccess(f, paraRegs);
// Begin(2005.3.4)
    constantTable=new HashMap();
// End(2005.3.4)
// Begin(2005.3.15)
    tmpgen++;
// End(2005.3.15)
  }
  /**
   * Invokes methods for SIMD optimization.
   * @param  live ImList,which has liveness information.
   * @param  in Vector,which contains LIR instructions as elements.
   * @param  rrn ReplaceRegNames, which replaces and has info. of replaced regs
   * @return Vector, which contains SIMD optiimized LIR instructions.
   */
  public Vector invoke(BiList live,Vector in, ReplaceRegNames rrn)
      throws SimdOptException {
// Begin(2004.12.21)
//    liveRegList=btoim(live);
    liveRegList=LirUtil.btoim(live);
// End(2004.12.21)
    Vector is=new Vector();
    front=new Vector();
    back=new Vector();
    int i;

    if(traceOK("SIMD", 1)) System.out.println("SimdOpt");
    if(in.size()==0) return in;
    if(traceOK("SIMD", 1)) {
      for(i=0;i<in.size();i++) System.out.println(((LirNode)in.elementAt(i)).toString());
    }
    is=preprocess(in);
// decompose
    if(traceOK("SIMD", 1)) System.out.println("decompose");
    is=decompose(is);
    if(traceOK("SIMD", 1)) {
      for(i=0;i<is.size();i++) System.out.println(((LirNode)is.elementAt(i)).toString());
    }
// collect Mem Access
    if(traceOK("SIMD", 1)) System.out.println("collect Mem Access");
    is=cma.collectMemAccess(is);
    if(traceOK("SIMD", 1)) {
      for(i=0;i<is.size();i++) System.out.println(((LirNode)is.elementAt(i)).toString());
    }
// combine
    if(traceOK("SIMD", 1)) System.out.println("combine");
    this.rgroups=new RegGroups(is);
    is=combineSimd(is);
    if(traceOK("SIMD", 1)) {
      for(i=0;i<is.size();i++) System.out.println(((LirNode)is.elementAt(i)).toString());
    }
//// collect Mem Access
//    if(traceOK("SIMD", 1)) System.out.println("collect Mem Access");
//    is=cma.collectMemAccess(is);
//    if(traceOK("SIMD", 1)) {
//      for(i=0;i<is.size();i++) System.out.println(((LirNode)is.elementAt(i)).toString());
//    }

// rearrange
    if(traceOK("SIMD", 1)) System.out.println("rearrange");
    LirRearrange rearrange=new LirRearrange();
    is=rearrange.invoke(is, rrn);
    if(traceOK("SIMD", 1)) {
      for(i=0;i<is.size();i++) System.out.println(((LirNode)is.elementAt(i)).toString());
    }

// replaceToOldReg
    if(traceOK("SIMD", 1)) System.out.println("replaceToOldReg");
    rrn.toOldName(is);
    if(traceOK("SIMD", 1)) {
      for(i=0;i<is.size();i++) System.out.println(((LirNode)is.elementAt(i)).toString());
    }
// restrict 1
    if(traceOK("SIMD", 1)) System.out.println("restrict 1");
    is=restrictInst(is);
    if(traceOK("SIMD", 1)) {
      for(i=0;i<is.size();i++) System.out.println(((LirNode)is.elementAt(i)).toString());
    }
// restrict 2
    if(traceOK("SIMD", 1)) System.out.println("restrict 2");
    is=restrictInst(is);
    if(traceOK("SIMD", 1)) {
      for(i=0;i<is.size();i++) System.out.println(((LirNode)is.elementAt(i)).toString());
    }
// restrict 3
    if(traceOK("SIMD", 1)) System.out.println("restrict 3");
    is=restrictInst(is);
    if(traceOK("SIMD", 1)) {
      for(i=0;i<is.size();i++) System.out.println(((LirNode)is.elementAt(i)).toString());
    }
// allocate
    if(traceOK("SIMD", 1)) System.out.println("allocate");
    is=allocateSimdReg(is);
    if(traceOK("SIMD", 1)) {
      for(i=0;i<is.size();i++) System.out.println(((LirNode)is.elementAt(i)).toString());
    }
// cleanup
    if(traceOK("SIMD", 1)) System.out.println("cleanup");
    CleanUpLir clup=new CleanUpLir(is);
    clup.invoke();
    if(traceOK("SIMD", 1)) {
      for(i=0;i<is.size();i++) System.out.println(((LirNode)is.elementAt(i)).toString());
    }
    is=postprocess(is);
    return(is);
  }
  private Vector preprocess(Vector in) {
    Vector out=new Vector();
    for(int i=0; i<in.size(); i++) {
      LirNode inst=(LirNode)in.elementAt(i);
      switch(inst.opCode) {
        case Op.PROLOGUE:
          {
            front.addElement(inst);
            break;
          }
        case Op.EPILOGUE:
        case Op.JUMP:
          {
            back.addElement(inst);
            break;
          }
        default:
          {
            out.addElement(inst);
            break;
          }
      }
    }
    return out;
  }
  private Vector postprocess(Vector in) {
    Vector out=new Vector();
    for(int i=0; i<front.size(); i++) out.addElement(front.elementAt(i));
    for(int i=0; i<in.size(); i++) out.addElement(in.elementAt(i));
    for(int i=0; i<back.size(); i++) out.addElement(back.elementAt(i));
    return out;
  }
  /**
   * Decomposes DAG into basic operations(bop).
   * @param  in Vector,which contains DAGs as its elements.
   * @return Vector, which contains basic operations as its elements.
   */
  public Vector decompose(Vector in)
      throws SimdOptException {
    int i;
    Vector var=new Vector();
    Vector val=new Vector();
    Vector org=new Vector();
    Vector out=new Vector();
    //;; Basic block is assumed.
    for(i=0;i<in.size();i++) {
      LirNode inst=(LirNode)in.elementAt(i);
      // Non-SET is passed.
      if(inst.opCode!=Op.SET) {
        out.addElement(inst);
        continue;
      }
      LirNode v=inst.kid(0); // Target node
      if(v.opCode!=Op.REG) {
        if(v.opCode!=Op.MEM ||
            (v.opCode==Op.MEM &&
              (inst.kid(1).opCode==Op.MEM || inst.kid(1).opCode==Op.REG))) {
          var.addElement(inst.kid(0));
          val.addElement(inst.kid(1));
          org.addElement(inst.kid(0));
          continue;
        }
      }
// Begin(2004.12.21)
//      if(inst.kid(1).opCode==Op.INTCONST) {
      if(inst.kid(1).opCode==Op.INTCONST || inst.kid(1).opCode==Op.FLOATCONST) {
// End(2004.12.21)
        var.addElement(inst.kid(0));
        val.addElement(inst.kid(1));
        org.addElement(inst);
        continue;
      };
      LirNode r=decompose1(inst.kid(1),var,val,org);
      if(r.opCode==Op.FRAME) {
        var.addElement(newReg(r.type));
        val.addElement(r);
        org.addElement(r);
      } else
        if(r.opCode==Op.REG) {
          //
          if(var.size()>0) {
            if(r.equals(var.lastElement()) && inst.kid(0).opCode!=Op.MEM) {
              var.setElementAt(inst.kid(0),var.size()-1);
            }
            else {
              var.addElement(inst.kid(0));
              val.addElement(r);
              org.addElement(inst.kid(1));
            }
          } else {
            out.addElement(inst);
          }
        } else {
          //
          Util.assert2(r.opCode==Op.INTCONST,
                      "r.car()==intconstCell");
          var.addElement(newReg(r.type));
          val.addElement(r);
          org.addElement(r);
        }
    }
    for(i=0;i<var.size();i++) {
      out.addElement(newLir.operator(Op.SET,
                                     (int)((LirNode)var.elementAt(i)).type,
                                     (LirNode)var.elementAt(i),
                                     (LirNode)val.elementAt(i),
                                     ImList.Empty));
    }
    return out;
  }
  private LirNode decompose1(LirNode e,Vector var,Vector val,Vector org)
      throws SimdOptException {
    int i;
// Begin(2005.3.4)
    // Return the corresponding register if calculated.
    for(i=0;i<var.size();i++) if(e.equals((LirNode)org.elementAt(i))) {
      return (LirNode)var.elementAt(i);
    }
// End(2005.3.4)
// Begin(2005.3.4)
//// Begin(2004.12.13)
//// Not changed yet.
////    if(e.opCode==Op.INTCONST || e.opCode==Op.FLOATCONST) {
////      var.addElement(newReg(e.type));
////      val.addElement(e);
////      org.addElement(e);
////      return (LirNode)var.lastElement();
////    }
//    if(e.opCode==Op.FRAME || e.opCode==Op.REG || e.opCode==Op.INTCONST ||
//        e.opCode==Op.FLOATCONST || e.opCode==Op.STATIC)
//      return e;
////    if(e.opCode==Op.FRAME || e.opCode==Op.REG || e.opCode==Op.STATIC)
////      return e;
//// End(2004.12.13)
    if(e.opCode==Op.INTCONST || e.opCode==Op.FLOATCONST) {
      LirNode v=(LirNode)constantTable.get(e);
      if(v==null) {
        v=newReg(e.type);
        constantTable.put(e, v);
      }
      var.addElement(v);
      val.addElement(e);
      org.addElement(e);
      return (LirNode)var.lastElement();
    }
    if(e.opCode==Op.FRAME || e.opCode==Op.REG || e.opCode==Op.STATIC)
      return e;
    if(e.opCode==Op.MEM) {
      var.addElement(newReg(e.type));
      val.addElement(e);
      org.addElement(e);
      return (LirNode)var.lastElement();
    }
// End(2005.3.4)
// Begin(2005.3.4)
//    // Return the corresponding register if calculated.
//    for(i=0;i<var.size();i++) if(e.equals((LirNode)org.elementAt(i))) {
//      return (LirNode)var.elementAt(i);
//    }
// End(2005.3.4)
    // Find a basic operation.
    LirNode[] env=new LirNode[MAX_ENV_SIZE];
    for(i=0;i<MAX_ENV_SIZE;i++) env[i]=null;
    LirNode b=bopList.find(e,env);
    Util.assert2(b!=null,
               "b!=null  "+e);
    // Decompose operands
    LirNode[] operand=new LirNode[MAX_ENV_SIZE];
    for(i=0;i<MAX_ENV_SIZE;i++) {
      if(env[i]!=null) operand[i]=decompose1(env[i],var,val,org);
    }
    // 
    var.addElement(newReg(b.type));
    val.addElement(bopList.replace(b,operand));
    org.addElement(e);
    return (LirNode)var.lastElement();
  }
  /**
   * Combines SIMD instructions.
   * @param  in Vector, which contains LirNodes as its elements.
   * @return Vector, which contains combined instructions.
   */
  public Vector combineSimd(Vector in) throws SimdOptException {
    int n=in.size();
    LirNode[] ia=new LirNode[n];
    in.copyInto(ia);
    int i,j;
    //
    LirDefUseRel duRel=new LirDefUseRel();
    duRel.mkDefUseRel(in);
    LirOrder order=new LirOrder();
    order.put(duRel.getRelation());
    order.saturate();
    //
    for(i=0;i<n;i++) {
      if(ia[i]==null) continue;
      if(ia[i].opCode==Op.PARALLEL) continue;
      if(ia[i].opCode!=Op.SET) continue;
      LirNode[] env=new LirNode[MAX_ENV_SIZE];
      for(j=0;j<MAX_ENV_SIZE;j++) env[j]=null;
      ImList b=boneList.find(ia[i],env);
      if(b==null) continue;
      int[] a=new int[MAX_SIMD_PARALLEL];
      //;;No check for dependencies.
      //;;There may be another way to do "combineSimd" in "decompose" phase.
      int k=pickupWithBone(ia, i, b, env, a, order);
      if(k==0) {
        LirNode[] tmpArray=new LirNode[1];
        tmpArray[0]=ia[i];
        ia[i]=newLir.operator(Op.PARALLEL, Type.UNKNOWN, tmpArray, ImList.Empty);
        continue;
      }
// Begin(2004.9.29)
//      if(k<2) continue;
      if(k<1) continue;
// End(2004.9.29)
      LirNode[] instArray=new LirNode[k+1];
      for(j=0;j<k;j++) {
        instArray[k-1-j]=ia[a[j]];
      }
      instArray[k]=ia[i];
      int lpos=i;
      if(k>0) lpos=a[k-1];
      if(hasReference(instArray)) continue;
      for(int m=0; m<k; m++) ia[a[m]]=null;
      ia[i]=null;
      LirNode inst=newLir.operator(Op.PARALLEL,Type.UNKNOWN,instArray, ImList.Empty);
      if(!isDefined(inst,ia,i+1,lpos)) {
        ia[i]=inst;
      } else if(!isUsed(inst,ia,i+1,lpos)) {
        ia[lpos]=inst;
      } else {
// Begin(2004.7.14)............. Temporal!!!!
//        Util.abort("combineSimd:Def-Use relation broken! >> "+inst.toString());
        ia[i]=inst;
// End(2004.7.14)
      }
    }
    Vector out=new Vector();
    for(i=0;i<n;i++) if(ia[i]!=null) out.addElement(ia[i]);
    return out;
  }
  /**
   * Pick up LIR nodes which have the same pattern of the given bone.
   * @param  ia    LirNode[]
   * @param  i     int
   * @param  b     ImList
   * @param  env   LirNode[]  Assigned values for HOLEs.
   * @param  a     int[]
   * @return int, which is the number of successfully pattern-matched LIRs.
   */
  private int pickupWithBone(LirNode[] ia, int i, ImList b,
      LirNode[] env, int[] a, LirOrder order)
      throws SimdOptException {
    int k=0;
    for(int j=i+1; j<ia.length; j++) {
      if(ia[j]==null) continue;
      if(k>=a.length) break;
//      if(boneList.chkBoneCnstr(b,ia[j],env, rgroups)) a[k++]=j;
      if(boneList.chkBoneCnstr(b,ia[j],env, rgroups) &&
          !existUsePath(order, ia, a, k, ia[j]))
        a[k++]=j;
    }
    int[] a1=new int[a.length];
    Vector defseq=new Vector();
    int k1=chkParaRegs(ia, i, b, env, a, k, a1, defseq);
//
//System.out.println("pickupWithBone:\n  "+ia[i]);
//System.out.print("  ");
//for(int idx=0;idx<k;idx++) System.out.print(a[idx]+",");
//System.out.println();
//System.out.println("  k1="+k1);
//
    if(k1>0) {
      paraRegs.add(defseq);
      for(int n=0; n<a.length; n++) {
        a[n]=a1[n];
      }
      k=k1;
    }
    ImList cnts=boneList.boneParacnts(b);
    while(!cnts.atEnd()) {
      int m=(int)((Integer)cnts.elem()).intValue();
      if(k>=m-1) return m-1;
      cnts=cnts.next();
    }
    return -1;
  }
  private boolean existUsePath(LirOrder order, LirNode[] ia, int[] a, int k, LirNode e) {
    for(int i=0; i<k; i++) {
      if(order.compare(ia[a[i]], e)==-1) return true;
    }
    return false;
  }
  private boolean hasReference(LirNode[] lirs) throws SimdOptException {
    Vector left=new Vector();
    Vector right=new Vector();
    for(int i=0; i<lirs.length; i++) {
      if(lirs[i].opCode==Op.SET) {
        LirUtil.pickupReferent(lirs[i].kid(0), left);
        LirUtil.pickupReferent(lirs[i].kid(1), right);
      }
    }
    boolean res=false;
    for(int i=0; i<left.size(); i++) {
      if(right.contains(left.elementAt(i))) return true;
    }
    return res;
  }
  /*
   */
  private int chkParaRegs(LirNode[] ia, int i, ImList b, LirNode[] env,
    int[] a, int k, int[] newa, Vector outseq) throws SimdOptException {
// We assume newa is initialized.
// if env[i] is REG, then look for a regseq from paraRegs
// Begin(2005.3.4)
//    outseq.add(env[0]);
// End(2005.3.4)
//System.out.println("ia["+i+"]="+ia[i]);
//System.out.println("k="+k);
    Vector[] regseqs=new Vector[MAX_ENV_SIZE];
    for(int j=1; j<env.length; j++) {
      if(env[j]==null) continue;
      if(env[j].opCode!=Op.REG) continue;
      regseqs[j]=findRegSeq(env[j]);
    }
// For each ia[a[n]], check wheter it has registers which match to regseq.
    int newk=0;
// Bebin(2005.2.28)
/*
    for(int n=0; n<k; n++) {
      if(a[n]==-1) continue;
      LirNode inst=ia[a[n]];
      LirNode[] localenv=new LirNode[MAX_ENV_SIZE];
      for(int j=0; j<localenv.length; j++) localenv[j]=null;
      LirNode bb=boneList.boneBody(b);
      boolean matched=boneList.match(bb, inst, localenv);
      if(!matched) continue;
      for(int j=1; j<localenv.length; j++) {
        if(localenv[j]==null) continue;
        if(localenv[j].opCode!=Op.REG) continue;
        if(regseqs[j]==null) continue;
        if(regseqs[j].size()<=newk+1) break;
        LirNode reg=(LirNode)regseqs[j].elementAt(newk+1);
        if(localenv[j]==reg) continue;
        matched=false;
        break;
      }
      if(!matched) continue;
      if(localenv[0]!=null && localenv[0].opCode==Op.REG) {
        outseq.addElement(localenv[0]);
      }
      else {
        outseq.addElement(null);
      }
      newa[newk++]=a[n];
    }
*/
    // Initialize localenv.
    LirNode[][] localenv=new LirNode[k+1][];
    for(int n=0; n<k+1; n++) {
      LirNode[] lenv=new LirNode[MAX_ENV_SIZE];
      localenv[n]=lenv;
    }
    boolean matched;
    LirNode bb=boneList.boneBody(b);
    matched=boneList.match(bb, ia[i], localenv[0]);
    for(int n=0; n<k; n++) {
      matched=boneList.match(bb, ia[a[n]], localenv[n+1]);
      if(!matched) {
        throw new SimdOptException("CombineSimd:Unmatched inst.!"+"\ninst:  "+ia[i]+"\nunmatched:  "+ia[a[n]]);
      }
    }
    int[] idx=new int[k];
    for(int n=0; n<k; n++) idx[n]=-1;
    // Find paraRegs.
    for(int j=1; j<MAX_ENV_SIZE; j++) {
      if(regseqs[j]==null) continue;
      int n;
      for(n=0; n<k; n++) {
        int n1;
        if(regseqs[j].size()<=n+1) break;
        for(n1=0; n1<k; n1++) {
          if(regseqs[j].elementAt(n+1)==localenv[n1+1][j]) break;
        }
        if(n1<k) {
          if(idx[n]==-1) {
            idx[n]=n1;
//System.out.println("inst:  "+ia[i]);
//System.out.println("  j="+j);
//System.out.println("  n="+n);
//System.out.println("  n1="+n1);
//System.out.println("  new idx[n]="+idx[n]);
//System.out.println("  ia[a[n1]]="+ia[a[n1]]);
//System.out.println("  regseqs[j].elementAt(n+1)="+regseqs[j].elementAt(n+1));
//System.out.println("  localenv[n1+1][j]="+localenv[n1+1][j]);
          }
          if(idx[n]!=n1) {
//System.out.println("inst:  "+ia[i]);
//System.out.println("  j="+j);
//System.out.println("  n="+n);
//System.out.println("  n1="+n1);
//System.out.println("  idx[n]="+idx[n]);
//System.out.println("  regseqs[j].elementAt(n+1)="+regseqs[j].elementAt(n+1));
//System.out.println("  localenv[n1+1][j]="+localenv[n1+1][j]);
            throw new SimdOptException("CombineSimd:Unmatched regsister!");
          }
        }
      }
      newk=n;
    }
    for(int n=0; n<newk; n++) {
// Begin(2005.3.11)
      if(idx[n]==-1) return 0;
// End(2005.3.11)
      newa[n]=a[idx[n]];
    }
//System.out.println("inst:  "+ia[i]);
//System.out.println("  outseq:");
//System.out.print("  ");
//System.out.print(localenv[0][0]+",");
    outseq.addElement(localenv[0][0]);
    for(int n=0; n<newk; n++) {
//System.out.print(localenv[idx[n]+1][0]+",");
      outseq.addElement(localenv[idx[n]+1][0]);
    }
//System.out.println();
// End(2005.2.28)
    return newk;
  }
  /**
   * Finds a register sequence in paraRegs whose first element is e.
   * @param  e    LirNode
   * @return Vector, which is a sequence of REG and whose first element is e.
   */
  private Vector findRegSeq(LirNode e) {
    if(e.opCode!=Op.REG) return null;
    for(int i=0; i<paraRegs.size(); i++) {
      Vector regs=(Vector)paraRegs.elementAt(i);
      if(regs==null) continue;
      if((LirNode)regs.elementAt(0)==e) return regs;
    }
    return null;
  }
  /**
   * Tests if variables defined in an instruction are used
   * in instruction between specified positions.
   * @param  inst  LirNode
   * @param  ia    LirNode[]
   * @param  fpos  int which is a starting position to check
   * @param  lpos  int which is a last position to check
   * @return  boolean, which represents "used or not".
   */
  private boolean isUsed(LirNode inst,LirNode[] ia,int fpos,int lpos) {
    Vector vars=new Vector();
    addDefVar(inst,vars);
    if(vars.size()==0) return false;
    if(lpos<fpos || ia.length<lpos) return false;
    for(int i=0;i<vars.size();i++) {
      for(int j=fpos;j<lpos;j++) {
        if(ia[j]==null) continue;
        if(used((Symbol)vars.elementAt(i),ia[j])) return true;
      }
    };
    return false;
  }
  /**
   * Tests if variables used in an instruction are defined
   * in instruction between specified positions.
   * @param  inst  LirNode
   * @param  ia    LirNode[]
   * @param  fpos  int which is a starting position to check
   * @param  lpos  int which is a last position to check
   * @return  boolean, which represents "defined or not".
   */
  private boolean isDefined(LirNode inst,LirNode[] ia,int fpos,int lpos) {
    Vector vars=new Vector();
    addUsedVar(inst,vars);
    if(vars.size()==0) return false;
    if(lpos<fpos || ia.length<lpos) return false;
    for(int i=0;i<vars.size();i++) {
      for(int j=fpos;j<lpos;j++) {
        if(ia[j]==null) continue;
        if(defined((Symbol)vars.elementAt(i),ia[j])) return true;
      }
    };
    return false;
  }
  /**
   * Applies constraints for instructions and registers.
   * @param  in Vector, which contains combined instructions(LirNodes).
   * @return Vector, which contains constraints satisfied instructions (LirNodes).
   */
  public Vector restrictInst(Vector in)
      throws SimdOptException {
    Vector out=new Vector();
    Vector oldReg=new Vector();
    Vector newReg=new Vector();
    Vector live=liveReg(in);
    for(int i=0;i<in.size();i++) {
      LirNode inst=(LirNode)in.elementAt(i);
      if(inst.opCode!=Op.PARALLEL) {
//        System.out.println("Not Parallel:"+inst);
        LirNode newinst=changeReg(inst, oldReg, newReg);
        if(newinst==null) newinst=inst;
        out.addElement(newinst);
        continue;
      };
      ImList b=boneList.find(inst.kid(0));
      if(b==null) {
//        System.out.println("Bone not found:"+inst);
        LirNode newinst=changeReg(inst, oldReg, newReg);
        if(newinst==null) newinst=inst;
        out.addElement(newinst);
        continue;
      };
      LirNode[][] operand=new LirNode[MAX_SIMD_PARALLEL][];
      int k=0;
      for(int j=0;j<inst.nKids();j++) {
        operand[k]=new LirNode[MAX_ENV_SIZE];
        boolean f=boneList.match((LirNode)boneList.boneBody(b),inst.kid(j),operand[k++]);
        Util.assert2(f,"f");
      }
      Vector prepare=new Vector();
      for(int j=0;j<k;j++) {
        for(int n=0;n<MAX_ENV_SIZE;n++) {
          int r;
          // Replace to the new register.
          if(operand[j][n]!=null && (r=oldReg.indexOf(operand[j][n]))>=0) {
            operand[j][n]=(LirNode)newReg.elementAt(r);
          }
        }
        //;; 
        // d: the output HOLE number( 0 is a default value.)
        int d=Integer.parseInt((String)(boneList.boneHolenum(b)));
        if(d!=0 && !operand[j][d].equals(operand[j][0])) {
          Util.assert2(!oldReg.contains(operand[j][0]),
                     "!oldReg.contains(operand[j][0])");
          if(operand[j][d].opCode==Op.REG) {
            Vector lv=(Vector)live.elementAt(i+1);
            if(operand[j][2]!=null &&
                 lv.contains(((LirSymRef)operand[j][d]).symbol)) {
              // If exchangeable then exchange HOLE 1 and HOLE 2.
              if(!((String)(boneList.boneChng(b))).equals("nil") && d<=2) {
                LirNode t=operand[j][1];
                operand[j][1]=operand[j][2]; operand[j][2]=t;
              }
              if(operand[j][d].opCode==Op.REG &&
                  !lv.contains(((LirSymRef)operand[j][d]).symbol)) {
                oldReg.addElement(operand[j][0]);
                newReg.addElement(operand[j][d]);
                operand[j][0]=operand[j][d];
              }
// Begin(2004.10.20)
              else {
                prepare.add(0, genSetLir(operand[j][0], operand[j][d]));
                operand[j][d]=operand[j][0];
              }
// End(2004.10.20)
            }
            else {
              oldReg.addElement(operand[j][0]);
              newReg.addElement(operand[j][d]);
              operand[j][0]=operand[j][d];
            }
          } else {
            // INTCONST is required.
System.out.println("operand["+j+"]["+d+"]="+operand[j][d]);
            Util.assert2(operand[j][d].opCode==Op.INTCONST,
                       "operand[j][d].car()==intconstCell");
            // Replace INTCONST to REG.
            prepare.add(0,genLoadConst(operand[j][0],operand[j][d]));
            operand[j][d]=operand[j][0];
          }
        }
      }
      LirNode newinst;
// Begin(2004.10.20)
      if(prepare.size()>0) {
        if(isRegTrans(prepare)) {
          for(int px=0; px<prepare.size(); px++) {
            out.addElement(prepare.elementAt(px));
          }
        }
//      if(prepare.size()>0) {
//        LirNode[] prepareArray=new LirNode[prepare.size()];
//        prepare.copyInto(prepareArray);
//        newinst=
//          newLir.operator(Op.PARALLEL,Type.UNKNOWN,prepareArray,ImList.Empty);
////        System.out.println("Prepare:"+newinst);
//        out.addElement(newinst);          
//      }
        else {
          LirNode[] prepareArray=new LirNode[prepare.size()];
          prepare.copyInto(prepareArray);
          newinst=
            newLir.operator(Op.PARALLEL,Type.UNKNOWN,prepareArray,ImList.Empty);
//          System.out.println("Prepare:"+newinst);
          out.addElement(newinst);          
        }
      }
// End(2004.10.20)
      // No rewriting.
      if(((String)(boneList.boneReplnum(b))).equals("nil")) {
        LirNode[] instSrc=new LirNode[k];
        for(int j=0;j<k;j++)
          instSrc[k-1-j]=boneList.replace((LirNode)boneList.boneBody(b),operand[j]);
        newinst=
          newLir.operator(Op.PARALLEL,Type.UNKNOWN,instSrc, ImList.Empty);
//        System.out.println("NotReplaced:"+newinst);
        out.addElement(newinst);          
      }
      // Rewrites by a rewriting rule.
      else {
        int n=Integer.parseInt((String)(boneList.boneReplnum(b)));
        ImList r=boneList.rewriteList[n];
        while(!r.atEnd()) {
          LirNode[] instSrc=new LirNode[k];
          for(int j=0;j<k;j++)
            instSrc[k-1-j]=boneList.replace((LirNode)r.elem(),operand[j]);
          newinst=
            newLir.operator(Op.PARALLEL,Type.UNKNOWN,instSrc, ImList.Empty);
//          System.out.println("Replaced:"+newinst);
          out.addElement(newinst);            
          r=r.next();
        }
      }
    }
    return out;
  }
  private boolean isRegTrans(Vector v) {
    for(int i=0; i<v.size(); i++) {
      LirNode inst=(LirNode)v.elementAt(i);
      if(!isRegReg(inst)) return false;
    }
    return true;
  }
  private boolean isRegReg(LirNode inst) {
    return inst.opCode==Op.SET &&
            inst.kid(0).opCode==Op.REG &&
            inst.kid(1).opCode==Op.REG;
  }
  private LirNode changeReg(LirNode e, Vector onames, Vector nnames) {
    switch(e.opCode) {
      case Op.REG:
        {
          int r=onames.indexOf(e);
          if(r<0) return null;
          return (LirNode)nnames.elementAt(r);
        }
      case Op.INTCONST:
        return null;
      default:
        {
          LirNode[] newkids=new LirNode[e.nKids()];
          boolean tf=false;
          for(int i=0; i<e.nKids(); i++) {
            newkids[i]=changeReg(e.kid(i), onames, nnames);
            if(newkids[i]!=null) {tf=true; continue;}
            newkids[i]=e.kid(i);
          }
          if(!tf) return null;
          return LirUtil.operator(newLir, e.opCode, e.type, newkids, e.opt);
        }
    }
  }
  /**
   * Allocate SIMD registers.
   * @param  in Vector, which contains SIMD instructions (LirNodes).
   * @return Vector, which contains SIMD register-allocated instructions.
   */
  public Vector allocateSimdReg(Vector in)
      throws SimdOptException {
    Vector out=new Vector();
    Vector live=liveReg(in);
    for(int i=0;i<in.size();i++) {
      LirNode inst=(LirNode)in.elementAt(i);
      if(inst.opCode!=Op.PARALLEL) {out.addElement(inst);continue;};
      int p=inst.nKids();
      int idx=simdRegIndex(p, inst.kid(0).type);
      // b : a bone
      ImList b;
      LirNode inst0;
      b=boneList.find(inst);
//System.out.println("inst:  "+inst);
//System.out.println("bone:  "+b);
      // inst has a PARALLEL-form bone or its kid has a bone or none.
      if(b!=null) {
        inst0=inst;
        p=1;
      }
      else { 
        inst0=inst.kid(0);
        b=boneList.find(inst0);
      }
      if(b==null) {out.addElement(inst);continue;};
      LirNode[] regAssign=new LirNode[MAX_ENV_SIZE];
      // For the case of simdgroups
      ImList simdgroups=(ImList)boneList.boneSubgroups(b);
      if(p<2 && simdgroups!=ImList.Empty) {
        LirNode[] operand1=new LirNode[MAX_ENV_SIZE];
        boolean f=boneList.match((LirNode)boneList.boneBody(b),inst0,operand1);
        Util.assert2(f,"f");
        while(!simdgroups.atEnd()) {
          ImList holenums=(ImList)simdgroups.elem();
          Util.assert2(!holenums.atEnd(),"Illegal Bone info:Empty simdgroup!");
          Vector hnv=LirUtil.imlistToVector(holenums);
          int hn=Integer.parseInt((String)hnv.elementAt(0));
          int r;
          for(r=0;r<simdReg[idx].length;r++){
            if(simdRegUsed[idx][r]!=null &&
               simdRegUsed[idx][r].contains(((LirSymRef)operand1[hn]).symbol)) break;
          }
          if(r==simdReg[idx].length){
            r=getSimdReg(idx);
            Vector v=new Vector();
            for(int j=0;j<hnv.size();j++) {
// Begin(2005.1.11)
//              v.addElement(((LirSymRef)operand1[Integer.parseInt((String)hnv.elementAt(j))]).symbol);
              Util.assert2(operand1[Integer.parseInt((String)hnv.elementAt(j))] instanceof LirSymRef,
                "operand : not reg.");
              LirSymRef symref=(LirSymRef)operand1[Integer.parseInt((String)hnv.elementAt(j))];
              v.addElement(symref.symbol);
// End(2005.1.11)
            };
            simdRegUsed[idx][r]=v;
          };
          Util.sorry(simdRegUsed[idx][r].size()!=hnv.size(),"size:simdRegUsed[r]!=hnv");
          for(int j=0;j<hnv.size();j++) {
            Util.sorry(simdRegUsed[idx][r].elementAt(j)!=((LirSymRef)operand1[Integer.parseInt((String)hnv.elementAt(j))]).symbol,
                       "simdRegUsed[r]!=hnv");
          }
          mkSubregs(regAssign, simdReg[idx][r], hnv, simdRegType[idx], operand1[hn].type);
          simdgroups=simdgroups.next();
        };
        // Initialize a used simd-reg which has no live regs.
        checkLive_1:
        for(int r=0;r<simdReg[idx].length;r++) if(simdRegUsed[idx][r]!=null) {
          for(int u=0;u<simdRegUsed[idx][r].size();u++) {
            Vector lv=(Vector)live.elementAt(i+1);
            if(lv.contains((Symbol)simdRegUsed[idx][r].elementAt(u))) continue checkLive_1;
          }
          simdRegUsed[idx][r]=null;
        }
        LirNode[] instSrc=new LirNode[1];
        mergeMatchedValue(regAssign, operand1);
        instSrc[0]=boneList.replace((LirNode)boneList.boneBody(b),regAssign);
        LirNode newinst;
        if(instSrc[0].opCode==Op.PARALLEL) {
          newinst=instSrc[0];
        }
        else {
          newinst=newLir.operator(Op.PARALLEL,Type.UNKNOWN,instSrc, ImList.Empty);
        }
        Vector sv=new Vector();
        Vector tv=new Vector();
        boolean torf=boneList.matchReg((LirNode)in.elementAt(i),newinst,sv,tv);
        // Make a copy from the previous form to the subreg-embedded form
        if(torf) {
          Vector usedregs=usedReg((LirNode)in.elementAt(i));
          for(int cnt=0;cnt<tv.size();cnt++){
            LirNode tc=(LirNode)tv.elementAt(cnt);
            LirSymRef sc=(LirSymRef)sv.elementAt(cnt);
            if(tv.elementAt(cnt)!=null && tc.opCode==Op.SUBREG) {
              Vector lv=(Vector)live.elementAt(i);
              if(lv.contains(sc.symbol) && usedregs.contains(sc.symbol)) {
                out.addElement(newLir.operator(Op.SET,tc.type,tc,sc, ImList.Empty));
              }// if
            }// if
          }// for(cnt)
        };// if
	Vector adjinsts=adjustAlignment(newinst);
	for(int adjcnt=0;adjcnt<adjinsts.size();adjcnt++) out.addElement((LirNode)adjinsts.elementAt(adjcnt));
        if(torf) {
          for(int cnt=0;cnt<tv.size();cnt++){
            LirNode tc=(LirNode)tv.elementAt(cnt);
            LirSymRef sc=(LirSymRef)sv.elementAt(cnt);
            if(tv.elementAt(cnt)!=null && tc.opCode==Op.SUBREG) {
              Vector lv=(Vector)live.elementAt(i+1);
              if(lv.contains(sc.symbol)) {
                out.addElement(newLir.operator(Op.SET,sc.type,sc,tc, ImList.Empty));
              }// if
            }// if
          }// for(cnt)
        };// if
        continue;
      };
      // For the case of no subgroups
      LirNode[][] operand=new LirNode[p][];
      int k=0;
      for(int j=0;j<inst.nKids();j++) {
        operand[k]=new LirNode[MAX_ENV_SIZE];
        boolean f=boneList.match((LirNode)boneList.boneBody(b),inst.kid(j),operand[k++]);
        Util.assert2(f,"f");
      }
      LirNode[][] reg=new LirNode[p][];
      for(int j=0;j<p;j++) reg[j]=new LirNode[MAX_ENV_SIZE];
      //;; 
      //;; 
      for(int n=0;n<MAX_ENV_SIZE;n++) if(operand[0][n]!=null) {
        //System.out.println("operand[0]["+n+"]="+operand[0][n]); // by TF
        // If HOLE n has non-substitution constraint, embedded value is used.
        if(((ImList)boneList.boneNosubsthnum(b)!=ImList.Empty)
            && isMem(n,(ImList)(boneList.boneNosubsthnum(b)))) {
          for(int j=0;j<p;j++) reg[j][n]=operand[j][n];
          continue;
        }
        // If embedded values are all MEM, thier values are used.
        boolean mflag=true;
        for(int j=0; j<p; j++) {
          if(operand[j][n].opCode!=Op.MEM) {
            mflag=false; break;
          }
        }
        if(mflag) {
          for(int j=0;j<p;j++) reg[j][n]=operand[j][n];
          continue;
        }
        // If embedded values are all INTCONST, their values are used.
        mflag=true;
        for(int j=0; j<p; j++) {
          if(operand[j][n].opCode!=Op.INTCONST) {
            mflag=false; break;
          }
        }
        if(mflag) {
          for(int j=0;j<p;j++) reg[j][n]=operand[j][n];
          continue;
        }
        // All objects which match to holes must be registers.
        for(int j=0;j<k;j++) {
          Util.assert2(operand[j][n].opCode==Op.REG,
                     "operand["+j+"]["+n+"].car()==regCell");
        }
        int r;
        // Find a used simd register which contains a specified value on head.
        Vector v=new Vector();
        for(int j=0;j<k;j++) v.addElement(((LirSymRef)operand[j][n]).symbol);
        r=findSimdReg(simdRegUsed[idx], v, simdReg[idx].length);
        if(r==simdReg[idx].length) {
          r=getSimdReg(idx);
          simdRegUsed[idx][r]=v;
        }
        for(int j=1;j<k;j++) {
          Util.sorry(!simdRegUsed[idx][r].contains(((LirSymRef)operand[j][n]).symbol),"Bad combination.");
        }
        //;; Necessary to check ordering.
        // Make a REG of the simd register.
        LirNode rr=newLir.symRef(Op.REG,simdRegType[idx],simdReg[idx][r],ImList.Empty);
        for(int j=0;j<p;j++) {
          LirNode iconst=newLir.iconst(typeI32,j,ImList.Empty);
          reg[j][n]=newLir.operator(Op.SUBREG,operand[0][n].type,rr,iconst,ImList.Empty);
        }
      }
      // Initialize a used simd-reg which has no live regs.
      checkLive:
      for(int r=0;r<simdReg[idx].length;r++) if(simdRegUsed[idx][r]!=null) {
        for(int u=0;u<simdRegUsed[idx][r].size();u++) {
          Vector lv=(Vector)live.elementAt(i+1);
          if(lv.contains((Symbol)simdRegUsed[idx][r].elementAt(u))) continue checkLive;
        }
        simdRegUsed[idx][r]=null;
      }
      if(((String)boneList.boneReplnum(b)).equals("nil")) {
        LirNode[] instSrc=new LirNode[p];
        for(int j=p-1;j>=0;j--) {
          instSrc[j]=boneList.replace((LirNode)boneList.boneBody(b),reg[j]);
        }
        LirNode newinst=newLir.operator(Op.PARALLEL,Type.UNKNOWN,instSrc, ImList.Empty);
        Vector sv=new Vector();
        Vector tv=new Vector();
        boolean torf=boneList.matchReg((LirNode)in.elementAt(i),newinst,sv,tv);
        // Make a copy from the previous form to the subreg-embedded form
        if(torf) {
          for(int cnt=0;cnt<tv.size();cnt++) {
            LirNode target=(LirNode)tv.elementAt(cnt);
            LirNode source=(LirNode)sv.elementAt(cnt);
            if(target!=null && target.opCode==Op.SUBREG) {
              Vector lv=(Vector)live.elementAt(i);
              if(lv.contains(((LirSymRef)source).symbol)) {
                out.addElement(newLir.operator(Op.SET,target.type,target,source, ImList.Empty));
              }// if
            }// if
          }// for(cnt)
        };// if
        Vector adjinsts=adjustAlignment(newinst);
        for(int adjcnt=0;adjcnt<adjinsts.size();adjcnt++) out.addElement((LirNode)adjinsts.elementAt(adjcnt));
        if(torf) {
          for(int cnt=0;cnt<tv.size();cnt++){
            LirNode target=(LirNode)tv.elementAt(cnt);
            LirNode source=(LirNode)sv.elementAt(cnt);
            if(target!=null && target.opCode==Op.SUBREG) {
              Vector lv=(Vector)live.elementAt(i+1);
              if(lv.contains(((LirSymRef)source).symbol)) {
                out.addElement(newLir.operator(Op.SET,source.type,source,target, ImList.Empty));
              }// if
            }// if
          }// for(cnt)
        };// if
      } else {
        LirNode[] reg1=new LirNode[MAX_ENV_SIZE];
        for(int n=0;n<10;n++) {
          if(reg[0][n]!=null) reg1[n]=reg[0][n];
        }
        int n=Integer.parseInt((String)boneList.boneReplnum(b));
        out.addElement(boneList.replace((LirNode)boneList.rewriteList[n].elem(),reg1));
      }
    }
    return out;
  }
  private void mergeMatchedValue(LirNode[] assign, LirNode[] origs) {
    for(int i=0; i<assign.length; i++) {
      if(assign[i]==null) assign[i]=origs[i];
    }
  }
  /**
   * Makes an assignment to subregs.
   * @param assign LirNode[] which is an assignment.
   * @param regname Symbol which is a name for a simd register.
   * @param hnv Vector which contains number strings of holes.
   * @param regtyp int which is a whole register's type
   * @param subregtype int which is a type for subregs
   */
  private void mkSubregs(LirNode[] assign, Symbol regname, Vector hnv,
      int regtyp, int subregtyp) {
    LirNode rr=newLir.symRef(Op.REG,regtyp,regname, ImList.Empty);
    for(int j=0;j<hnv.size();j++) {
      int k=Integer.parseInt((String)hnv.elementAt(j));
      LirNode iconst=newLir.iconst(typeI32,j,ImList.Empty);
      assign[k]=newLir.operator(Op.SUBREG, subregtyp, rr, iconst, ImList.Empty);
    }
  }
  /**
   * Picks up used registers in an instruction.
   * @param  x  LirNode which is an instruction
   * @return Vector, which contains used registers (LirNode)
   */
  private Vector usedReg(LirNode x) {
    Vector v=new Vector();
    if(x.opCode==Op.PARALLEL) {
      for(int i=0;i<x.nKids();i++) {
        if(x.kid(i).opCode==Op.SET) addUsedReg(x.kid(i).kid(1),v);
      }
    } else {
      if(x.opCode==Op.SET) addUsedReg(x.kid(1),v);
    };
    return v;
  }
  /**
   * Picks up registers defined in an instruction.
   * @param  x  LirNode which is an instruction
   * @return Vector, which contains used registers (LirNode)
   */
  private Vector defReg(LirNode x) {
    Vector v=new Vector();
    if(x.opCode==Op.PARALLEL) {
      for(int i=0;i<x.nKids();i++) {
        if(x.kid(i).opCode==Op.SET && ((LirNode)x.kid(i).kid(0)).opCode==Op.REG)
          v.addElement(((LirSymRef)x.kid(i).kid(0)).symbol);
      }
    } else {
      if(x.opCode==Op.SET && x.kid(0).opCode==Op.REG)
        v.addElement(((LirSymRef)x.kid(0)).symbol);
    }
    return v;
  }
  /**
   * Tests if an int belongs to a list.
   * @param  x int
   * @param  ys ImList which contains integers as string forms
   * @return boolean which is true if an int belongs to a list and false o.w.
   */
  private boolean isMem(int x,ImList ys) {
    while(!ys.atEnd()) {
      if(x==Integer.parseInt((String)ys.elem())) return true;
      ys=ys.next();
    }
    return false;
  }
  /**
   * Applies the adjust alignment constraint.
   * @param  inst LirNode which is applied the constraint.
   * @return Vector which contains instructions.
   */
  private Vector adjustAlignment(LirNode inst) {
    Vector out=new Vector();
    LirNode[] env=new LirNode[MAX_ENV_SIZE];
    for(int i=0;i<MAX_ENV_SIZE;i++) env[i]=null;
    ImList b=boneList.find(inst,env);
    if(b==null || ((String)boneList.boneReplnum(b)).equals("nil")) {
      out.addElement(inst);
      return out;
    };

    ImList instlist=
      boneList.rewriteList[Integer.parseInt((String)boneList.boneReplnum(b))];
    while(!instlist.atEnd()) {
      LirNode c=genAndReplace((LirNode)instlist.elem(),env);
      if(c.opCode==Op.PARALLEL || c.opCode==Op.SET) {
        out.addElement(c);
      };
      instlist=instlist.next();
    }
    return out;
  }
  /**
   * Copies an LirNode in which every HOLE is substituted to a generated register.
   * @param  e  LirNode
   * @param  env LirNode[]
   * @return LirNode which is a result of replacement.
   */
  private LirNode genAndReplace(LirNode e,LirNode[] env) {
    if(e.opCode==Op.HOLE) {
      int i=(int)((LirIconst)e.kid(0)).value;
      LirNode r=env[i];
      if(r!=null) return r;
      LirNode nreg;
      if(e.type==typeI32) {
        nreg=newReg(typeI32);
        env[i]=nreg;
        return nreg;
      }
    }
    if(e instanceof LirIconst) return e;
    if(e instanceof LirFconst) return e;
    if(e instanceof LirLabelRef) return e;
    if(e instanceof LirSymRef) return e;
    if(e instanceof LirUnaOp) {
      LirNode operand0=genAndReplace(e.kid(0),env);
      return newLir.operator(e.opCode,e.type,operand0, ImList.Empty);
    }
    if(e instanceof LirBinOp) {
      LirNode operand0=genAndReplace(e.kid(0),env);
      LirNode operand1=genAndReplace(e.kid(1),env);
      return newLir.operator(e.opCode,e.type,operand0,operand1,ImList.Empty);
    }
    if(e instanceof LirNaryOp) {
      LirNode[] operands=new LirNode[e.nKids()];
      for(int i=0;i<e.nKids();i++) operands[i]=genAndReplace(e.kid(i),env);
      return newLir.operator(e.opCode,e.type,operands,ImList.Empty);
    }
    return e;
  }
  /**
   * Calculate live registers.
   * @param  in Vector, which contains instruction (LirNodes).
   * @return Vector, which contains vectors of live registers (symbols).
   */
  public Vector liveReg(Vector in) {
    Vector live=new Vector();
    ImList c;
    c=liveRegList;
    while(!c.atEnd()) { live.addElement(c.elem()); c=c.next(); }
    return liveReg(in, live);
  }
  public Vector liveReg(Vector in, Vector live) {
// Begin(2004.12.21)
//    Vector live=new Vector();
//    ImList c;
//    c=liveRegList;
//    while(!c.atEnd()) { live.addElement(c.elem()); c=c.next(); }
// End(2004.12.21)
    Vector out=new Vector();
    out.addElement(live.clone());
    for(int i=in.size();i>0;) {
      LirNode inst=(LirNode)in.elementAt(--i);
      switch(inst.opCode) {
        case Op.PARALLEL:
          {
            for(int j=0;j<inst.nKids();j++)
              if(inst.kid(j).kid(0).opCode==Op.REG)
                live.removeElement(((LirSymRef)inst.kid(j).kid(0)).symbol);
            for(int j=0;j<inst.nKids();j++)
              addUsedReg(inst.kid(j).kid(1),live);
            break;
          }
        case Op.SET:
          {
            if(inst.kid(0).opCode==Op.REG)
              live.removeElement(((LirSymRef)inst.kid(0)).symbol);
            addUsedReg(inst.kid(1),live);
            break;
          }
        default:
          {
            for(int j=0; j<inst.nKids(); j++)
              addUsedReg(inst.kid(j), live);
            break;
          }
      }
      out.insertElementAt(live.clone(),0);
    }
    return out;
  }
  /**
   * Collects used register names.
   * @param  e  LirNode
   * @param  v  Vector
   */
  private void addUsedReg(LirNode e,Vector v) {
    if(e instanceof LirIconst) return;
    if(e instanceof LirFconst) return;
    if(e instanceof LirLabelRef) return;
    if(e instanceof LirSymRef) {
      if(e.opCode==Op.REG) {
        if(!v.contains(((LirSymRef)e).symbol))
          v.addElement(((LirSymRef)e).symbol);
      }
      return;
    }
    for(int i=0;i<e.nKids();i++) addUsedReg(e.kid(i),v);
    return;
  }
  /**
   * Collects used variables.
   * @param  e  LirNode
   * @param  v  Vector
   */
  private void addUsedVar(LirNode e,Vector v) {
    if(e.opCode==Op.SET) addUsedReg(e.kid(1),v);
    if(e.opCode==Op.PARALLEL) {
      for(int i=0;i<e.nKids();i++) addUsedReg(e.kid(i),v);
    };
  }
  /**
   * Collects defined variables.
   * @param  e  LirNode
   * @param  v  Vector
   */
  private void addDefVar(LirNode e,Vector v) {
    if(e.opCode==Op.SET) {
      if(e.kid(0).opCode==Op.REG && !v.contains(((LirSymRef)e.kid(0)).symbol)) {
        v.addElement(((LirSymRef)e.kid(0)).symbol);
      } else if(e.kid(0).opCode==Op.SUBREG && e.kid(0).kid(1).opCode==Op.REG &&
                !v.contains(((LirSymRef)e.kid(0).kid(1)).symbol)) {
        v.addElement(((LirSymRef)e.kid(0).kid(1)).symbol);
      };
    } else if(e.opCode==Op.PARALLEL) {
      for(int i=0;i<e.nKids();i++) addDefVar(e.kid(i),v);
    };
  }
  private static int decode(String s) {
    try {
      return Type.decode(s);
    } catch (SyntaxError e) {
      return -1;
    }
  }
  private int simdRegIndexMax=2;
  private int simdRegIndex(int cnt, int typ) {
    int nType=cnt * LirUtil.typeNum(typ);
    if(nType==64) return 0;
    if(nType==128) return 1;
    return 0;
  }
  private static final int typeI32=decode("I32");
  private static final int[] simdRegType={decode("I64"), decode("I128")};
  private final String[][] simdRegStr={
//    {"%MM0", "%MM1", "%MM2", "%MM3", "%MM4", "%MM5", "%MM6", "%MM7"},
//    {"%XMM0", "%XMM1", "%XMM2", "%XMM3", "%XMM4", "%XMM5", "%XMM6", "%XMM7"},
/*
    {"%MM0", "%MM1", "%MM2", "%MM3", "%MM4", "%MM5", "%MM6", "%MM7",
     "%MM8", "%MM9", "%MM10", "%MM11", "%MM12", "%MM13", "%MM14", "%MM15",
     "%MM16", "%MM17", "%MM18", "%MM19", "%MM20", "%MM21", "%MM22", "%MM23",
     "%MM24", "%MM25", "%MM26", "%MM27", "%MM28", "%MM29", "%MM30", "%MM31"},
    {"%XMM0", "%XMM1", "%XMM2", "%XMM3", "%XMM4", "%XMM5", "%XMM6", "%XMM7",
     "%XMM8", "%XMM9", "%XMM10", "%XMM11", "%XMM12", "%XMM13", "%XMM14", "%XMM15",
     "%XMM16", "%XMM17", "%XMM18", "%XMM19", "%XMM20", "%XMM21", "%XMM22", "%XMM23",
     "%XMM24", "%XMM25", "%XMM26", "%XMM27", "%XMM28", "%XMM29", "%XMM30", "%XMM31"},
*/

    {"MM0", "MM1", "MM2", "MM3", "MM4", "MM5", "MM6", "MM7",
     "MM8", "MM9", "MM10", "MM11", "MM12", "MM13", "MM14", "MM15",
     "MM16", "MM17", "MM18", "MM19", "MM20", "MM21", "MM22", "MM23",
     "MM24", "MM25", "MM26", "MM27", "MM28", "MM29", "MM30", "MM31"},
    {"XMM0", "XMM1", "XMM2", "XMM3", "XMM4", "XMM5", "XMM6", "XMM7",
     "XMM8", "XMM9", "XMM10", "XMM11", "XMM12", "XMM13", "XMM14", "XMM15",
     "XMM16", "XMM17", "XMM18", "XMM19", "XMM20", "XMM21", "XMM22", "XMM23",
     "XMM24", "XMM25", "XMM26", "XMM27", "XMM28", "XMM29", "XMM30", "XMM31"},

  };
  /**
   * Symbols of SIMD registers
   */
  private final Symbol[][] simdReg={new Symbol[32], new Symbol[32]};
  /**
   * Makes and registers SIMD registers.
   */
  private void initSimdReg() {
    for(int idx=0; idx<simdRegIndexMax; idx++) {
      for(int i=0;i<simdReg[idx].length;i++) {
        String name=simdRegStr[idx][i];
        Symbol sym=func.localSymtab.get(name);
        if(sym==null) {
          sym=func.module.globalSymtab.get(name);
          if(sym==null) {
            sym=func.localSymtab.addSymbol(name,storageREG,
                  simdRegType[idx],
// Begin(2005.2.8)
//                  4,
                  LirUtil.calcBoundary(simdRegType[idx]),
// End(2005.2.8)
                  0,
                  ImList.Empty);
          }
        }
// Begin(2005.3.23)
        String regset=getRegset(idx);
        sym.setOpt(ImList.list("&regset", regset));
// End(2005.3.23)
        simdReg[idx][i]=sym;
      }
    }
  }
  private String getRegset(int idx) {
    if(idx==0) return "*reg-simd-I64*";
    if(idx==1) return "*reg-simd-I128*";
    return "*reg-simd-I128*";
  }
  /**
   * Temporally used SIMD register names.
   */
  private Vector[][] simdRegUsed={
    new Vector[simdReg[0].length],
    new Vector[simdReg[1].length]
  };
  private int findSimdReg(Vector[] regnames, Vector v, int length) {
    int r;
    for(r=0; r<length; r++) {
      if(regnames[r]==null) continue;
      boolean b=true;
      for(int i=0; i<regnames[r].size() && i<v.size(); i++) {
        if(regnames[r].elementAt(i)!=v.elementAt(i)) {b=false; break;}
      }
      if(b) break;
    }
    return r;
  }
  /**
   * Finds an unused SIMD register.
   * @return int which represents the number of a SIMD register
   */
  private int getSimdReg(int idx)
      throws SimdOptException {
    int r;
    for(r=0;r<simdReg[idx].length;r++) if(simdRegUsed[idx][r]==null) return r;
    Util.sorry(true,"No registers.");
    return -1; // Dummy
  }
  private LirNode genLoadConst(LirNode r,LirNode c) {
    return newLir.operator(Op.SET,r.type,r,c,ImList.Empty);
  }
  private LirNode genSetLir(LirNode d, LirNode s) {
    return newLir.operator(Op.SET,d.type,d,s,ImList.Empty);
  }
  /**
   * Creates a new register.
   * @param  t int which is a seed of a new register
   * @return LirNode which represents a new register
   */
  private LirNode newReg(int t) {
//    Symbol newregsym=mkSym(t,"$"+tmpReg++);
// Begin(2005.3.15)
//    Symbol newregsym=mkSym(t,"sv$"+tmpReg+++"%");
    Symbol newregsym=mkSym(t,"sv$"+tmpgen+"b"+tmpReg+++"%");
// End(2005.3.15)
    return newLir.symRef(Op.REG,t,newregsym,ImList.Empty);
  }
  private static final int storageREG=storageDecode("REG");
  private static int storageDecode(String s) {
    try {
      return Storage.decode(s);
    } catch (SyntaxError e) {
      return -1;
    }
  }
  /**
   * Finds a smbol from its name and generates a symbol if not registered.
   * @param  t  int which represents a type of a register
   * @param  s  String which is a symbol name
   * @return Symbol
   */
  private Symbol mkSym(int t,String s) {
    Symbol sym=func.localSymtab.get(s);
    if(sym==null) {
      sym=func.module.globalSymtab.get(s);
      if(sym==null) {
// Begin(2005.2.8)
//        sym=func.localSymtab.addSymbol(s,storageREG,t,4,0,ImList.Empty);
        sym=func.localSymtab.addSymbol(s,storageREG,t,
              LirUtil.calcBoundary(t),0,ImList.Empty);
// End(2005.2.8)
      }
    }
    return sym;
  }
  /**
   * Tests if a symbol is defined in an instruction.
   * @param  var  Symbol
   * @param  inst LirNode
   * @return boolean which is true if var is defined in inst and false o.w.
   */
  public boolean defined(Symbol var,LirNode inst) {
    if(inst.opCode==Op.SET) {
      LirNode dest=inst.kid(0);
      if(dest.opCode==Op.REG) {
        LirSymRef reg=(LirSymRef)dest;
        return reg.symbol.equals(var);
      };
      if(dest.opCode==Op.SUBREG) {
        if(dest.kid(1).opCode==Op.REG) {
          LirSymRef reg=(LirSymRef)dest.kid(1);
          return reg.symbol.equals(var);
        }
      };
      return false;
    };
    if(inst.opCode==Op.PARALLEL) {
      for(int i=0;i<inst.nKids();i++) {
        if(defined(var,inst.kid(i))) return true;
      }
    };
    return false;
  }
  /**
   * Tests if a symbol is used in an instruction (LirNode).
   * @param  var Symbol which is checked.
   * @param  inst LirNode which represents an instruction.
   * @return boolean which is true if var is used in inst and false otherwise.
   */
  public boolean used(Symbol var,LirNode inst) {
    if(inst.opCode==Op.SET) {
           return (inst.kid(0).opCode==Op.MEM &&
                   used1(var,inst.kid(0).kid(0)) ||
                   used1(var,inst.kid(1)));
    };
    if(inst.opCode==Op.PARALLEL) {
      for(int i=0;i<inst.nKids();i++) {
        if(used(var,inst.kid(i))) return true;
      };
      return false;
    };
    return false;
  }
  private boolean used1(Symbol var,LirNode e) {
    if(e instanceof LirIconst) return false;
    if(e instanceof LirFconst) return false;
    if(e instanceof LirLabelRef) return false;
    if(e instanceof LirSymRef) {
      if(e.opCode==Op.REG) {
        LirSymRef reg=(LirSymRef)e;
        return reg.symbol.equals(var);
      };
      return false;
    }
    for(int i=0;i<e.nKids();i++) {
      if(used1(var,e.kid(i))) return true;
    }
    return false;
  }
// Begin(2004.12.21)
//  private Vector btov(BiList b) {
//    Vector v=new Vector();
//    for(BiLink lir=b.first();!lir.atEnd();lir=lir.next()) {
//      LirNode ins=(LirNode)lir.elem();
//      if(ins.opCode==Op.SET) v.add(ins);
//    }
//    return v;
//  }
//  private BiList vtob(Vector v) {
//    BiList b=new BiList();
//    for(int i=0;i<v.size();i++) {
//      b.add(v.elementAt(i));
//    }
//    return b;
//  }
//  private ImList btoim(BiList b) {
//    ImList im=ImList.Empty;
//    for(BiLink ln=b.first(); !ln.atEnd(); ln=ln.next()) {
//      Object c=ln.elem();
//      im=new ImList(c, im);
//    }
//    return im;
//  }
// Begin(2004.12.21)
  private void printSimdRegUsed() {
    System.out.println("simdRegUsed:");
    for(int idx=0; idx<simdRegIndexMax; idx++) {
      for(int i=0; i<simdReg[idx].length; i++) {
        if(simdRegUsed[idx][i]!=null) {
          for(int j=0; j<simdRegUsed[idx][i].size();
              j++) {
            if(simdRegUsed[idx][i].elementAt(j)!=null) {
              System.out.println("  simdRegUsed["+i+"].elementAt("+j+")="+
                                simdRegUsed[idx][i].elementAt(j).toString());
            }
          }
        }
      }
    }
  }
}
