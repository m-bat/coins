/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.io.PushbackReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.LinkedList;
import coins.backend.Function;
import coins.backend.Op;
import coins.backend.SyntaxError;
import coins.backend.lir.LirNode;
import coins.backend.sym.Symbol;
import coins.backend.util.ImList;

/**
 * BoneList class.
 */
public abstract class LirBoneList extends LirMatch {
  /**
   * Maximum size of a matching environment.
   */
  private static final int MAX_ENV_SIZE=33;
  /**
   * LirNode decoder for the SIMD optimization purpose.
   */
  private LirDecoder decoder;
  /**
   * Matching patterns and attributes (called "bone info.").
   */
  public ImList[] templateList;
  public ImList[] auxtemplateList;
  /**
   * Initializes boneList.
   */

  public abstract ImList[] initBoneList();

  /**
   * Rewrite patterns.
   */
  public ImList[] rewriteList;
  /**
   * Initializes rewriteList.
   */
  public abstract ImList[] initRewriteList();

  public abstract ImList[] initAuxBoneList();

  public void init(Function f) {
    f=f;
    super.newLir=f.newLir;
    decoder=new LirDecoder(f);
    templateList=initBoneList();
    auxtemplateList=initAuxBoneList();
    rewriteList=initRewriteList();
  }
  /**
   * Find a matched template(a pattern and attributes).
   * @param inst LIR
   * @param env 
   * @return ImList, which represents a matched template.
   */
  public ImList find(LirNode inst,LirNode[] env) {
    if(templateList==null) return null;
    for(int i=0;i<templateList.length;i++) {
      for(int j=0; j<env.length; j++) env[j]=null;
      if(match((LirNode)(((ImList)templateList[i]).next()).elem(),inst,env)) 
        return (ImList)templateList[i];
    };
    for(int i=0;i<auxtemplateList.length;i++) {
      for(int j=0; j<env.length; j++) env[j]=null;
      if(match((LirNode)(((ImList)auxtemplateList[i]).next()).elem(),inst,env)) {
        if(chkAuxCond(i, inst))
          return (ImList)auxtemplateList[i];
      }
    };
    return null;
  }

// First parameter i must correspond to the index of auxtemplateList.
  public abstract boolean chkAuxCond(int i, LirNode inst);

  /**
   * Find a matched template(a pattern and attributes).
   * @param inst LIR
   * @return ImList, which represents a matched template.
   */
  public ImList find(LirNode inst) {
    LirNode[] env=new LirNode[MAX_ENV_SIZE];
    for(int n=0;n<MAX_ENV_SIZE;n++) env[n]=null;
    return find(inst,env);
  }
  /**
   * Makes bones and registers them into boneList.
   * @param  infostr String which represents a bone info.
   * @param  bodystr  String which represents a bone body.
   * @return  ImList which represents a bone.
   */
  public ImList mkBone(String infostr,String bodystr) {
    try {
      PushbackReader irder=new PushbackReader(new StringReader(infostr));
      ImList info=(ImList)ImList.readSexp(irder);
      PushbackReader brder=new PushbackReader(new StringReader(bodystr));
      ImList bbody=(ImList)ImList.readSexp(brder);
      LirNode nd=decoder.decodeLir(bbody);
      return new ImList(info,new ImList(nd,ImList.Empty));
    } catch (SyntaxError se) {
      System.out.println("mkBone:"+se.getMessage());
      return null;
    } catch (IOException msg) {
      System.out.println("mkBone:IOError");
      return null;
    }
  }
  /**
   * Makes LirNodes for rewrite and registers them into rewriteList.
   * @param  s  String which represents rewrited LirNodes.
   * @return  ImList which represents rewrited LirNodes.
   */
  public ImList mkRw(String s) {
    try {
      PushbackReader rd=new PushbackReader(new StringReader(s));
      ImList stmts=(ImList)ImList.readSexp(rd);
      ImList lirs=ImList.Empty;
      while(!stmts.atEnd()) {
        lirs=new ImList(decoder.decodeLir((ImList)stmts.elem()),lirs);
        stmts=stmts.next();
      }
      return lirs.destructiveReverse();
    } catch (SyntaxError se) {
      System.out.println("mkRw:"+se.getMessage());
      return null;
    } catch (IOException msg) {
      System.out.println("mkRw:IOError");
      return null;
    }
  }
  /**
   * Test consistency between a LirNode and an environment.
   * @param bone A template(i.e. a pattern and attributes)
   * @param inst LirNode
   * @param env An environment
   * @return true if consistent.
   */
  public boolean chkBoneCnstr(ImList bone,LirNode inst,LirNode[] env,
                  RegGroups rgs) {
    LirNode[] localenv=new LirNode[MAX_ENV_SIZE];
    if(match(boneBody(bone),inst,localenv)) {
      for(int i=0; i<MAX_ENV_SIZE; i++) {
        if(env[i]==null & localenv[i]==null) break;
        if(!rgs.isEquivalent(env[i], localenv[i])) return false;
        if(env[i].opCode!=localenv[i].opCode) return false;
        if(LirUtil.isShiftOp(inst.kid(1)) && env[i].opCode==Op.INTCONST && env[i]!=localenv[i])
          return false;
      }
      if(((String)boneSharedhnum(bone)).equals("nil")) return true;
      int n=Integer.parseInt((String)boneSharedhnum(bone));
      return(localenv[n].equals(env[n]));
    }
    return false;
  }
  /**
   * Get attributes from a template.
   * @param b A template
   * @return Attributes
   */
  public ImList boneInfo(ImList b) {
    return (ImList)b.elem();
  }
  /**
   * Get a pattern from a template.
   * @param b A template
   * @return A pattern
   */
  public LirNode boneBody(ImList b) {
    return (LirNode)b.elem2nd();
  }
  /**
   * Get the parallel count attribute from a template.
   * @param b A template
   * @return String, which represents the parallel count.
   */
  public ImList boneParacnts(ImList b) {
    return paracnts(boneInfo(b));
  }
  /**
   * Get the parallel count attribute from a template.
   * @param binfo Attributes
   * @return String, which represents the parallel count.
   */
  public ImList paracnts(ImList binfo) {
    if(binfo.length()==0) return null;
    return toIntList((ImList)binfo.elem());
  }
  private ImList toIntList(ImList is) {
    if(is==ImList.Empty) return ImList.Empty;
    ImList iList=
      new ImList((Integer)Integer.decode((String)is.elem()), toIntList(is.next()));
    return iList;
  }
  /**
   * Get the hole number attribute from a template.
   * @param b A template
   * @return String, which represents the hole number used as the output.
   */
  public String boneHolenum(ImList b) {
    return holenum(boneInfo(b));
  }
  /**
   * Get the hole number attribute from a template.
   * @param binfo Attributes
   * @return String, which represents the hole number used as the output.
   */
  public String holenum(ImList binfo) {
    if(binfo.length()<2) return "nil";
    return (String)binfo.elem2nd();
  }
  /**
   * Get the changeability attribute from a template.
   * @param b A template
   * @return String, which represents "changeable or not".
   */
  public String boneChng(ImList b) {
    return chng(boneInfo(b));
  }
  /**
   * Get the changeability attribute from a template.
   * @param binfo Attributes
   * @return String, which represents "changeable or not".
   */
  public String chng(ImList binfo) {
    if(binfo.length()<3) return "nil";
    return (String)binfo.elem3rd();
  }
  /**
   * Get the replace number attribute from a template.
   * @param b A template
   * @return String, which represents a number of a patten in the rewriteList.
   */
  public String boneReplnum(ImList b) {
    return replnum(boneInfo(b));
  }
  /**
   * Get the replace number attribute from a template.
   * @param binfo Attributes
   * @return String, which represents a number of a patten in the rewriteList.
   */
  public String replnum(ImList binfo) {
    if(binfo.length()<4) return "nil";
    return (String)binfo.elem4th();
  }
  /**
   * Get the shared hole number attribute from a template.
   * Registers, which match with the HOLE having this hole number,
   * must be same.
   * @param b A template
   * @return String, which represents a shared hole number.
   */
  public String boneSharedhnum(ImList b) {
    return sharedhnum(boneInfo(b));
  }
  /**
   * Get the shared hole number attribute from a template.
   * Registers, which match with the HOLE having this hole number,
   * must be same.
   * @param binfo Attributes
   * @return String, which represents a shared hole number.
   */
  public String sharedhnum(ImList binfo) {
    if(binfo.length()<6) return "nil";
    return (String)binfo.elem6th();
  }
  /**
   * Get non substituted hole numbers attribute from a template.
   * A Hole, which have a non substituted hole number, is not substituted to
   * a subregister.
   * @param b A template
   * @return ImList, whose elements are non substituted hole numbers.
   */
  public ImList boneNosubsthnum(ImList b) {
    return nosubsthnum(boneInfo(b));
  }
  /**
   * Get non substituted hole numbers attribute from a template.
   * A Hole, which have a non substituted hole number, is not substituted to
   * a subregister.
   * @param binfo Attributes
   * @return ImList, whose elements are non substituted hole numbers.
   */
  public ImList nosubsthnum(ImList binfo) {
    if(binfo.length()<7) return ImList.Empty;
    return (ImList)((ImList)binfo.next()).elem6th();
  }
  /**
   * Get the subgroups attribute from a template.
   * A subgroup is a set of hole numbers,which corresponds to a subregister.
   * @param b A template
   * @return ImList represents a set of subgroups.
   */
  public ImList boneSubgroups(ImList b) {
    return subgroups(boneInfo(b));
  }
  /**
   * Get the subgroups attribute from a template.
   * A subgroup is a set of hole numbers,which corresponds to a subregister.
   * @param binfo Attributes
   * @return ImList represents a set of subgroups.
   */
  public ImList subgroups(ImList binfo) {
    if(binfo.length()<8) return ImList.Empty;
    return (ImList)binfo.next().next().elem6th();
  }
}
