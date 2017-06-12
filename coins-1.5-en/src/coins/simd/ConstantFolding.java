/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.Function;
import coins.backend.Op;
import coins.backend.SyntaxError;
import coins.backend.Type;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirBinOp;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirFconst;
import coins.backend.lir.LirNaryOp;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirUnaOp;
import coins.backend.util.BiLink;
import coins.backend.util.ImList;

/**
 * Class for constant folding
 */
public class ConstantFolding {
  private int typeI8=decode("I8");
  private int typeI16=decode("I16");
  private int typeI32=decode("I32");
  private int typeI64=decode("I64");
  private int typeF32=decode("F32");
  private int typeF64=decode("F64");
  private LirFactory fact;

  /**
   * Constructs a ConstantFolding object
   * @param f Function
   */
  public ConstantFolding(Function f) {
    fact=f.newLir;
  }
  /**
   * Do constant folding for each statements in a basic block
   * @param blk BasicBlk
   */
  public void invoke(BasicBlk blk) {
    if(!blk.instrList().isEmpty()) {
      BiLink lir=blk.instrList().first();
      while(!lir.atEnd()) {
        LirNode ins=(LirNode)lir.elem();
        if(ins.opCode==Op.SET) {
          LirNode newins=constantFolding(ins);
          if(newins!=ins) lir.setElem(newins);
        };
        lir=lir.next();
      }
    }
  }
  private LirNode constantFolding(LirNode ins) {
    if(ins.opCode==Op.SET) {
      LirNode s0=constantFolding(ins.kid(0));
      LirNode s1=constantFolding(ins.kid(1));
      if(s0!=ins.kid(0) && s1!=ins.kid(1)) {
        return fact.operator(ins.opCode,ins.type,s0,s1,ImList.Empty);
      }
      if(s0!=ins.kid(0) && s1==ins.kid(1)) {
        return fact.operator(ins.opCode,ins.type,s0,ins.kid(1),ImList.Empty);
      }
      if(s0==ins.kid(0) && s1!=ins.kid(1)) {
        return fact.operator(ins.opCode,ins.type,ins.kid(0),s1,ImList.Empty);
      }
      // Otherwise
      else {return ins;}
    }
    else if(isEvaluableOp(ins)) {
      LirNode s0=constantFolding(ins.kid(0));
      LirNode s1=constantFolding(ins.kid(1));
      //Evaluate
      if(isConst(s0) && isConst(s1)) {
        return evaluate(ins.opCode,ins.type,s0,s1);
      }
      //Not evaluated
      if(s0!=ins.kid(0) && s1==ins.kid(1))
        return fact.operator(ins.opCode,ins.type,s0,ins.kid(1).makeCopy(fact),ImList.Empty);
      if(s0==ins.kid(0) && s1!=ins.kid(1))
        return fact.operator(ins.opCode,ins.type,ins.kid(0).makeCopy(fact),s1,ImList.Empty);
      if(s0!=ins.kid(0) && s1!=ins.kid(1))
        return fact.operator(ins.opCode,ins.type,s0,s1,ImList.Empty);
      // Otherwise
      return ins;
    }
    else if(ins instanceof LirIconst) {
      return ins;
    }
    else if(ins instanceof LirFconst) {
      return ins;
    }
    else if(ins instanceof LirUnaOp) {
      LirNode s0=constantFolding(ins.kid(0));
      if(s0!=ins.kid(0)) return fact.operator(ins.opCode,ins.type,s0,ImList.Empty);
      // Otherwise
      return ins;
    }
    else if(ins instanceof LirBinOp) {
      LirNode s0=constantFolding(ins.kid(0));
      LirNode s1=constantFolding(ins.kid(1));
      if(s0!=ins.kid(0) && s1==ins.kid(1))
        return fact.operator(ins.opCode,ins.type,s0,ins.kid(1).makeCopy(fact),ImList.Empty);
      if(s0==ins.kid(0) && s1!=ins.kid(1))
        return fact.operator(ins.opCode,ins.type,ins.kid(0).makeCopy(fact),s1,ImList.Empty);
      if(s0!=ins.kid(0) && s1!=ins.kid(1))
        return fact.operator(ins.opCode,ins.type,s0,s1,ImList.Empty);
      // Otherwise
      return ins;
    }
    else if(ins instanceof LirNaryOp) {
      LirNode[] src=new LirNode[ins.nKids()];
      boolean changed=false;
      for(int i=0;i<ins.nKids();i++) {
        if((src[i]=constantFolding(ins.kid(i)))!=ins.kid(i)) changed=true;
      };
      if(changed) {
        for(int i=0;i<ins.nKids();i++) {
          if(src[i]==ins.kid(i)) src[i]=ins.kid(i).makeCopy(fact);
        };
        return fact.operator(ins.opCode,ins.type,src,ImList.Empty);
      }
      else {return ins;}
    };
    return ins;
  }
  private boolean isConst(LirNode e) {
    if(e==null) return false;
    else return(e.opCode==Op.INTCONST || e.opCode==Op.FLOATCONST);
  }
  private boolean isEvaluableOp(LirNode e) {
    return (
      e.opCode==Op.ADD ||
      e.opCode==Op.SUB ||
      e.opCode==Op.MUL ||
      e.opCode==Op.DIVS ||
      e.opCode==Op.DIVU ||
      e.opCode==Op.MODS ||
      e.opCode==Op.MODU ||
      e.opCode==Op.BAND ||
      e.opCode==Op.BOR ||
      e.opCode==Op.BXOR ||
      e.opCode==Op.LSHS ||
      e.opCode==Op.LSHU ||
      e.opCode==Op.RSHS ||
      e.opCode==Op.RSHU
      );
  }
  private LirNode evaluate(int op,int type,LirNode e1,LirNode e2) {
    Number r;
    if(type==typeI8 || type==typeI16 || type==typeI32 || type==typeI64) {
      LirIconst c1=(LirIconst) e1;
      LirIconst c2=(LirIconst) e2;
      r=Evaluation.calc(c1.value,c2.value,convOp(op));
      return fact.iconst(type,r.longValue(),ImList.Empty);
    };
//    if(type==typeI64) {
//      LirIconst c1=(LirIconst) e1;
//      LirIconst c2=(LirIconst) e2;
//      r=Evaluation.calc(c1.value,c2.value,convOp(op));
//      return fact.iconst(type,r.longValue(),ImList.Empty);
//    };
//    if(type==typeF32) {
    if(type==typeF32 || type==typeF64) {
      LirFconst c1=(LirFconst) e1;
      LirFconst c2=(LirFconst) e2;
      r=Evaluation.calc(c1.value,c2.value,convOp(op));
      return fact.fconst(type,r.doubleValue(),ImList.Empty);
    };
//    if(type==typeF64) {
//      LirFconst c1=(LirFconst) e1;
//      LirFconst c2=(LirFconst) e2;
//      r=Evaluation.calc(c1.value,c2.value,convOp(op));
//      return fact.fconst(type,r.doubleValue(),ImList.Empty);
//    };
    return null;
  }
  private String convOp(int opCode) {
  String op=null;
  if(opCode==Op.NEG)   op="-";
  else if(opCode==Op.ADD)   op="+";
  else if(opCode==Op.SUB)   op="-";
  else if(opCode==Op.MUL)   op="*";
  else if(opCode==Op.DIVS)   op="/";
  else if(opCode==Op.DIVU)   op="/";
  else if(opCode==Op.DIVU)  op="/";
  else if(opCode==Op.MODS)   op="%";
  else if(opCode==Op.MODU)  op="%";
  else if(opCode==Op.BAND)  op="&";
  else if(opCode==Op.BXOR)  op="^";
  else if(opCode==Op.BOR)   op="|";
  else if(opCode==Op.LSHS)   op="<<";
  else if(opCode==Op.LSHU)  op="<<";
  else if(opCode==Op.RSHS)   op=">>";
  else if(opCode==Op.RSHU)  op=">>";
  return op;
  }
  private static int decode(String s) {
    try {
      return Type.decode(s);
    } catch (SyntaxError e) {
      return -1;
    }
  }
}
