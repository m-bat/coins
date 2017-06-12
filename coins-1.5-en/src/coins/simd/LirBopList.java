/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

//import java.lang.*;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.List;
import java.util.LinkedList;
import coins.backend.Function;
import coins.backend.SyntaxError;
import coins.backend.lir.LirNode;
//import coins.backend.sym.*;
import coins.backend.util.ImList;

/**
 * BopList class
 */
public abstract class LirBopList extends LirMatch {
  /**
   * Maximum size of a matching environment.
   */
  private static final int MAX_ENV_SIZE=33;
  /**
   * LirNode decoder for the SIMD optimization purpose.
   */
  private LirDecoder decoder;
  /**
   * Matching patterns.
   */
  public LirNode[] templateList;
  public void init(Function f) {
    f=f;
    super.newLir=f.newLir;
    decoder=new LirDecoder(f);
    templateList=initTemplist();
  }
  public abstract LirNode[] initTemplist();
  /**
   * Find a matched  pattern.
   * @param inst LIR
   * @param env 
   * @return LirNode, which represents a matched pattern.
   */
  public LirNode find(LirNode inst,LirNode[] env)
      throws SimdOptException {
    for(int i=0;i<templateList.length;i++) {
      LirNode[] env2=new LirNode[env.length];
      rewrite(env, env2);
      if(match((LirNode)templateList[i],inst,env2)) {
        rewrite(env2, env);
        return (LirNode)templateList[i];
      }
    };
    return null;
  }
  private void rewrite(LirNode[] sa, LirNode[] ta)
      throws SimdOptException {
    if(sa.length!=ta.length) Util.abort("Illega input in rewrite/LirBopList");
    for(int i=0; i<sa.length; i++) ta[i]=sa[i];
  }
  /**
   * Makes LirNodes for basic operations and registers them into bopList.
   * @param  s  String which represents bop LirNodes.
   * @return  LirNode which represents bop.
   */
  public LirNode mkBop(String s) {
    try {
      PushbackReader rd=new PushbackReader(new StringReader(s));
      ImList stmt=(ImList)ImList.readSexp(rd);
      return decoder.decodeLir(stmt);
    } catch (SyntaxError se) {
      System.out.println("mkBop:"+se.getMessage());
      return null;
    } catch (IOException msg) {
      System.out.println("mkBop:IOError");
      return null;
    }
  }
}