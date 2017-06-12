/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.cfg.BasicBlk;
import coins.backend.Function;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.ana.Dominators;
import coins.backend.Op;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.sym.Label;
import coins.backend.sym.Symbol;
import coins.backend.util.ImList;
import coins.backend.Type;
import coins.backend.opt.If2Jumpc;

import java.util.Stack;
import java.util.Hashtable;

/**
 * Replace JUMPC to logical expression or IF.
 **/
class IfConvert{
  /** The name of the options to COINS compiler driver **/
  public final String CONV_NUM="simd-conv-num";
  /** The threshold of debug print **/
  public final int THR=SimdEnvironment.OptThr;
  /** The maxmum number to collect expressions **/
  private final int MAX_NUM;
  /** The environment of the SIMD module **/
  private SimdEnvironment env;
  /** The current function **/
  private Function f;
  /** The list of LIR **/
  private BiList sortedList;

  /**
   * Constructor:
   * @param e The environment of the SIMD module
   * @param function The current function
   **/
  IfConvert(SimdEnvironment e,Function function){
    env=e;
    f=function;

    // Get the option to COINS compiler driver
    if(env.opt.isSet(CONV_NUM))
      MAX_NUM=Integer.parseInt(env.opt.getArg(CONV_NUM));
    else
      MAX_NUM=2;
  }

  /**
   * Invoke the replacement from JUMPC to logical expression or IF.
   **/
  void invoke(){
    //f.printIt(env.output);
    boolean changed=true;
    while(changed){
      ConcatBlks concatBlks=new ConcatBlks(env,f);
      concatBlks.invoke();
      //f.printIt(env.output);

      changed=false;
      sortedList=new BiList();
      checkNestedIf(f.flowGraph().entryBlk(),new BiList());

      // Iterate all over the basic blocks in the current control flow graph.
      for(BiLink p=sortedList.first();!p.atEnd();p=p.next()){
        BasicBlk blk=(BasicBlk)p.elem();
        LirNode node=(LirNode)blk.instrList().last().elem();
        if(node.opCode!=Op.JUMPC) continue;

        env.println("Branch : "+blk.id,THR);

        // check the structure of the current branch.
        env.print("    structure.....",THR);
        if(!checkBranchStructure(blk)){
          env.println("FAIL",THR);
          continue;
        }
        env.println("OK",THR);

        // check the inner instructions of the current branch
        env.print("    instruction...",THR);
        if(!checkInstruction(blk)){
          env.println("FAIL",THR);
          continue;
        }
        env.println("OK",THR);

        // check whether merge
        env.print("    merge.........",THR);
        if(!merge(blk)){
          env.println("FAIL",THR);
          continue;
        }
        env.println("OK",THR);

        reconstruct(blk);
        changed=true;
      }
    }
//    f.printIt(env.output);
  }

  /**
   * Reconstruct the edges in the current control flow graph.
   * @param blk The current basic block
   **/
  private void reconstruct(BasicBlk blk){
    BiList removeList=new BiList();
    BasicBlk succ=(BasicBlk)blk.succList().first().elem();
    BasicBlk target=(BasicBlk)succ.succList().first().elem();

    // clear succ's edges
    for(BiLink p=blk.succList().first();!p.atEnd();p=p.next()){
      succ=(BasicBlk)p.elem();
      succ.clearEdges();
      removeList.add(succ);
    }

    // maint edges
    blk.instrList().last().unlink();
    LirNode newJump=env.lir.operator(Op.JUMP,Type.UNKNOWN,
                                     env.lir.labelRef(target.label()),
                                     ImList.Empty);
    blk.instrList().last().addAfter(newJump);
    blk.maintEdges();

    for(BiLink p=removeList.first();!p.atEnd();p=p.next()){
      succ=(BasicBlk)p.elem();
      f.flowGraph().basicBlkList.remove(succ);
    }
  }

  /**
   * Make the map between the current LIR node and the list of the LIR nodes
   * which depend on the current LIR node.
   * @param blk The current basic block
   * @return The map between the LIR node and the list of LIR node which depend
   *         on it.
   **/
  private Hashtable findDepend(BasicBlk blk){
    Hashtable table=new Hashtable();

    for(BiLink p=blk.instrList().first();!p.atEnd();p=p.next()){
      LirNode node=(LirNode)p.elem();
      if(node.opCode!=Op.SET) continue;

      BiList list=(BiList)table.get(node);
      if(list==null){
        list=new BiList();
        table.put(node,list);
      }

      Util util=new Util();
      BiList tmp=util.findTargetLir(node.kid(1),Op.REG,new BiList());
      BiList depList=new BiList();
      for(BiLink q=tmp.first();!q.atEnd();q=q.next()){
        LirSymRef ref=(LirSymRef)q.elem();
        Symbol s=ref.symbol;
        depList.add(s);
      }
      if(depList.length()>0){
        for(BiLink q=p.prev();!q.atEnd();q=q.prev()){
          LirNode dep=(LirNode)q.elem();
          //env.output.println(dep);
          if(dep.opCode!=Op.SET) continue;
          
          if(depList.contains(((LirSymRef)dep.kid(0)).symbol)){
            list.add(dep);
          }
        }
      }
    }
    return(table);
  }

  /**
   * Generate the logical expression.
   * @param blk The current basic block
   * @return True if the merge is done successfully
   **/
  private boolean merge(BasicBlk blk){
    LirNode root=(LirNode)blk.instrList().last().elem();

    //env.output.println(root);
    // add copy assign about the left hand side of condition
    LirNode tmpDst=f.newTemp(root.kid(0).kid(0).type);
    LirNode tmp=env.lir.operator(Op.SET,root.kid(0).kid(0).type,tmpDst,
                                 root.kid(0).kid(0).makeCopy(env.lir),
                                 ImList.Empty);
    //env.output.println(tmp);
    root.kid(0).setKid(0,tmpDst.makeCopy(env.lir));
    blk.instrList().last().addBefore(tmp);

    // add copy assign about the right hand side of condition
    tmpDst=f.newTemp(root.kid(0).kid(1).type);
    tmp=env.lir.operator(Op.SET,root.kid(0).kid(1).type,tmpDst,
                         root.kid(0).kid(1).makeCopy(env.lir),
                         ImList.Empty);
    //env.output.println(tmp);
    root.kid(0).setKid(1,tmpDst.makeCopy(env.lir));
    blk.instrList().last().addBefore(tmp);

    //env.output.println(root);

    // merge the instructions
    BasicBlk thenPart=root.getTargets()[0].basicBlk();
    BasicBlk elsePart=root.getTargets()[1].basicBlk();
    Hashtable dependMap=findDepend(elsePart);
    BiList candidateThen=new BiList();
    BiList candidateElse=new BiList();

    for(BiLink p=thenPart.instrList().first();!p.atEnd();p=p.next()){
      LirNode thenNode=(LirNode)p.elem();
      if(thenNode.opCode!=Op.SET) continue;

      boolean find=false;
      Symbol s=((LirSymRef)thenNode.kid(0)).symbol;
      for(BiLink q=elsePart.instrList().first();!q.atEnd();q=q.next()){
        LirNode elseNode=(LirNode)q.elem();
        if(elseNode.opCode!=Op.SET) continue;

        Symbol ss=((LirSymRef)elseNode.kid(0)).symbol;
        if(s.equals(ss)){
          if(!candidateElse.contains(elseNode)){
            BiList list=(BiList)dependMap.get(elseNode);
            for(BiLink pp=list.first();!pp.atEnd();pp=pp.next()){
              LirNode dep=(LirNode)pp.elem();
              if(!candidateElse.contains(dep)) return(false);
            }
            candidateElse.add(elseNode);
            find=true;
            break;
          }
        }
      }
      if(!find){
        LirNode copyNode=env.lir.operator(Op.SET,s.type,
                                          env.lir.symRef(s),
                                          env.lir.symRef(s),
                                          ImList.Empty);
        candidateElse.add(copyNode);
      }
      candidateThen.add(thenNode);
    }

    for(BiLink p=elsePart.instrList().first();!p.atEnd();p=p.next()){
      LirNode elseNode=(LirNode)p.elem();
      if(elseNode.opCode!=Op.SET) continue;

      if(!candidateElse.contains(elseNode)){
        //env.output.println(elseNode);
        Symbol s=((LirSymRef)elseNode.kid(0)).symbol;
        LirNode copyNode=env.lir.operator(Op.SET,s.type,
                                          env.lir.symRef(s),
                                          env.lir.symRef(s),
                                          ImList.Empty);
        candidateThen.add(copyNode);
        candidateElse.add(elseNode);
      }
    }

    BiLink p=candidateThen.first();
    BiLink q=candidateElse.first();
    BiLink r=blk.instrList().last();
    LirNode condition=root.kid(0);
    for(;!p.atEnd() && !q.atEnd();){
      LirNode thenNode=(LirNode)p.elem();
      if(thenNode.opCode==Op.SET){
        LirNode thenkid=thenNode.kid(1);
        LirNode elseNode=(LirNode)q.elem();
        LirNode elsekid=elseNode.kid(1);

        LirNode mergedNode=mergeExp(thenkid,elsekid,condition,false);
        //LirNode mergedNode=mergeExp(thenkid,elseNode,condition,true);
        if(mergedNode==null) return(false);

        LirNode setNode=env.lir.operator(Op.SET,mergedNode.type,
                                         thenNode.kid(0).makeCopy(env.lir),
                                         mergedNode,ImList.Empty);
        //env.output.println(setNode);
        r.addBefore(setNode);
        f.touch();
        q=q.next();
      }
      p=p.next();
    }
    return(true);
  }

  /**
   * Check the instructions in the `then part' and `else part' of
   * the target JUMPC. The assign instructions whose destination is a
   * register variable are only permitted.
   * @param blk The current basic block which has JUMPC
   * @return True if the instructions are all OK
   **/
  private boolean checkInstruction(BasicBlk blk){
    LirNode root=(LirNode)blk.instrList().last().elem();
    BasicBlk thenPart=root.getTargets()[0].basicBlk();
    BasicBlk elsePart=root.getTargets()[1].basicBlk();

    // check then part's instructions
    if(!check(thenPart)) return(false);

    // check else part's instructions
    if(!check(elsePart)) return(false);

    return(true);
  }

  /**
   * Check the instructions in the `then part' and `else part' of
   * the target JUMPC. The assign instructions whose destination is a
   * register variable are only permitted.
   * @param blk The current basic block which means `then part' or
   *            `else part'
   * @return True if the instructions are all OK
   **/
  private boolean check(BasicBlk blk){
    int count=0;
    for(BiLink p=blk.instrList().first();!p.atEnd();p=p.next()){
      LirNode node=(LirNode)p.elem();
      switch(node.opCode){
        case Op.SET:{
          if(node.kid(0).opCode!=Op.REG) return(false);
          if(++count>MAX_NUM) return(false);

          // convert only integral type
          if(Type.tag(node.type)!=Type.INT) return(false);

          Util util=new Util();
          BiList list=util.findTargetLir(node.kid(1),Op.DIVU,new BiList());
          list=util.findTargetLir(node.kid(1),Op.DIVS,list);
          if(list.length()!=0) return(false);

          break;
        }
        case Op.JUMP:
        case Op.JUMPC:
        case Op.JUMPN:
        case Op.EPILOGUE: break;
        default: return(false);
      }
    }
    return(true);
  }

  /**
   * Check the current `if-then-else' structure.
   * The permitted structure is as follows:
   * (1)`then part' has only one predecessor and also `else part' has only
   *     one predecessor. The predecessor of `then part' and the predecessor
   *     of `else part' are the same.
   * (2)`then part' has only one successor and also `else part' has only one
   *    successor. The successor of `then part' and the successor of
   *    `else part' are the same.
   * @param blk The current basic block
   * @return True if the structure is all OK
   **/
  private boolean checkBranchStructure(BasicBlk blk){
    LirNode node=(LirNode)blk.instrList().last().elem();
    BasicBlk thenPart=node.getTargets()[0].basicBlk();
    BasicBlk elsePart=node.getTargets()[1].basicBlk();

    LirNode lastNode1=(LirNode)thenPart.instrList().last().elem();
    LirNode lastNode2=(LirNode)elsePart.instrList().last().elem();

    if((thenPart.predList().length()==1 && elsePart.predList().length()==1) &&
       (lastNode1.opCode==Op.JUMP && lastNode2.opCode==Op.JUMP) &&
       (lastNode1.getTargets()[0]==lastNode2.getTargets()[0])){
      return(true);
    }

    return(false);
  }

  /**
   * Sort the basic blocks in the current control flow graph.
   * @param blk The current basic block
   * @paran visited The list of basic blocks which are already visited
   * @return The sorted list of the basic blocks
   **/
  private BiList checkNestedIf(BasicBlk blk,BiList visited){
    if(visited.contains(blk)) return(visited);
    visited.add(blk);

    Dominators dom=(Dominators)f.require(Dominators.analyzer);
    LirNode lastNode=(LirNode)blk.instrList().last().elem();
    if(lastNode.opCode==Op.JUMPC){
      Label[] targets=lastNode.getTargets();
      
      for(BiLink p=dom.kids[blk.id].first();!p.atEnd();p=p.next()){
        BasicBlk b=(BasicBlk)p.elem();
        boolean isTarget=false;
        for(int i=0;i<targets.length;i++){
          if(b==targets[i].basicBlk()){
            isTarget=true;
            break;
          }
        }
        if(isTarget)
          visited=checkNestedIf(b,visited);
        else
          visited=checkNestedIf(b,visited);
      }
    }
    else{
      for(BiLink p=dom.kids[blk.id].first();!p.atEnd();p=p.next()){
        BasicBlk b=(BasicBlk)p.elem();
        visited=checkNestedIf(b,visited);
      }
    }

    if(lastNode.opCode==Op.JUMPC){
      sortedList.addNew(blk);
    }

    return(visited);
  }

  /**
   * Generate IF node.
   **/
  void makeIfNode(){
    // Iterate all over the basic blocks in the current control flow graph.
    for(BiLink p=f.flowGraph().basicBlkList.first();!p.atEnd();p=p.next()){
      BasicBlk blk=(BasicBlk)p.elem();
      for(BiLink q=blk.instrList().first();!q.atEnd();q=q.next()){
        LirNode node=(LirNode)q.elem();
        if(node.opCode==Op.SET){
          if(usingTstNode(node)){
            // Convert to IF node
            Hashtable map=new Hashtable();
            BiList list=replaceTstNode(node,null,-1,new BiList(),map);

            for(BiLink pp=list.last();!pp.atEnd();pp=pp.prev()){
              LirNode n=(LirNode)pp.elem();

//              Symbol s=(Symbol)map.get(n.toString());
              Symbol s=(Symbol)map.get(n);
              LirNode symRef=env.lir.symRef(s);
              LirNode ifNode=env.lir.operator(Op.IF,n.type,
                                              n.makeCopy(env.lir),
                                              env.lir.iconst(n.type,-1),
                                              env.lir.iconst(n.type,0),
                                              ImList.Empty);
              LirNode assign=env.lir.operator(Op.SET,symRef.type,symRef,ifNode,
                                              ImList.Empty);
              q.addBefore(assign);
/*
              LirNode fNode=(LirNode)blk.instrList().first().elem();
              if(fNode.opCode!=Op.PROLOGUE)
                blk.instrList().addFirst(assign);
              else
                blk.instrList().first().addAfter(assign);
*/
            }
          }
          else{
            if(Type.tag(node.type)==Type.INT &&
               Type.bits(node.type)<32){
//              env.output.println("$$$$$$$$$$$$$$$$ "+node);
              LirNode newNode=insertConvert(node.type,
                                            integralPromotion(node.kid(1)));
              node.setKid(1,newNode);
            }
          }
        }
      }
    }
//    f.flowGraph().printIt(env.output);
    f.apply(If2Jumpc.trig);
  }

  /**
   * Do integral promotion to the current LIR node.
   * @param node The current LIR
   * @return Return the integral promoted LIR
   **/
  private LirNode integralPromotion(LirNode node){
    switch(node.opCode){
      case Op.SUBREG:
      case Op.LABEL:
      case Op.FRAME:
      case Op.STATIC:
      case Op.MEM: return(node);
    }

    switch(node.opCode){
      case Op.CONVSX:
      case Op.CONVZX:
      case Op.CONVIT:
      case Op.CONVFX:
      case Op.CONVFT:
      case Op.CONVFI:
      case Op.CONVSF:
      case Op.CONVUF:break;
      default:{
        for(int i=0;i<node.nKids();i++){
          node.setKid(i,integralPromotion(node.kid(i)));
        }
      }
    }

    switch(node.opCode){
      case Op.CONVSX:
      case Op.CONVZX:
      case Op.CONVIT:
      case Op.CONVFX:
      case Op.CONVFT:
      case Op.CONVFI:
      case Op.CONVSF:
      case Op.CONVUF:
      case Op.REG:
      case Op.INTCONST:
      case Op.FLOATCONST:{
        node=insertConvert(Type.type(Type.INT,32),node);
        break;
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
      case Op.BAND:
      case Op.BOR:
      case Op.BXOR:
      case Op.LSHS:
      case Op.LSHU:
      case Op.RSHS:
      case Op.RSHU:
      case Op.SUB:
      case Op.DIVS:
      case Op.DIVU:
      case Op.MODS:
      case Op.MODU:
      case Op.MUL:
      case Op.ADD:{
//        env.output.println(node);
        node=env.lir.operator(node.opCode,Type.type(Type.INT,32),
                              node.kid(0).makeCopy(env.lir),
                              node.kid(1).makeCopy(env.lir),
                              ImList.Empty);
        break;
      }
      case Op.NEG:
      case Op.BNOT:{
        node=env.lir.operator(node.opCode,Type.type(Type.INT,32),
                              node.kid(0).makeCopy(env.lir),
                              ImList.Empty);
        break;
      }
      default:{

      }
    }

    return(node);
  }

  /**
   * Replace the TSTxx node (ex: TSTNE, TSTEQ) into temporal variable node.
   * Collect the TSTxx node and put into a list.
   * @param node The current LIR node
   * @param parent The parent of the current LIR node
   * @param place The place of the current LIR node in the parent node
   * @param list The list of the TSTxx node
   * @param map The map between the TSTxx node and the temporal variable
   * @return The list of the TSTxx node
   **/
  private BiList replaceTstNode(LirNode node,LirNode parent,int place,
                                BiList list,Hashtable map){
    for(int i=0;i<node.nKids();i++)
      replaceTstNode(node.kid(i),node,i,list,map);

    switch(node.opCode){
      case Op.TSTEQ:
      case Op.TSTNE:
      case Op.TSTLTS:
      case Op.TSTLES:
      case Op.TSTGTS:
      case Op.TSTGES:
      case Op.TSTLTU:
      case Op.TSTLEU:
      case Op.TSTGTU:
      case Op.TSTGEU:{
        Symbol s=(Symbol)map.get(node);
        if(s==null){
          s=((LirSymRef)f.newTemp(node.type)).symbol;
          map.put(node,s);
          list.add(node);
        }
        LirNode symRef=env.lir.symRef(s);
        parent.setKid(place,insertConvert(parent.type,symRef));
      }
    }

    return(list);
  }

  /**
   * Check whether the current LIR node has some TSTxx nodes.
   * @param node The current LIR node
   * @return True if the current LIR node has some TSTxx nodes
   **/
  private boolean usingTstNode(LirNode node){
    switch(node.opCode){
      case Op.TSTEQ:
      case Op.TSTNE:
      case Op.TSTLTS:
      case Op.TSTLES:
      case Op.TSTGTS:
      case Op.TSTGES:
      case Op.TSTLTU:
      case Op.TSTLEU:
      case Op.TSTGTU:
      case Op.TSTGEU: return(true);
      default:{
        for(int i=0;i<node.nKids();i++){
          if(usingTstNode(node.kid(i))) return(true);
        }
      }
    }
    return(false);
  }

  /**
   * Insert the CONVxx node (ex: CONVSX, CONVIT) for integral promotion.
   * @param type The type of the destination node
   * @param target The current LIR node
   * @return The LIR node which inserted the CONVxx node
   **/
  private LirNode insertConvert(int type,LirNode target){
    if(type==target.type) return(target);

    if(Type.tag(target.type)==Type.INT){
      if(Type.tag(type)==Type.INT){
        // convzx or convit
        if(Type.bits(target.type)>Type.bits(type)){
          // convit
          return(env.lir.operator(Op.CONVIT,type,
                                  target.makeCopy(env.lir),
                                  ImList.Empty));
        }
        else{
          // convsx
          return(env.lir.operator(Op.CONVSX,type,
                                  target.makeCopy(env.lir),
                                  ImList.Empty));
        }
      }
      else if(Type.tag(type)==Type.FLOAT){
        // convuf
        return(env.lir.operator(Op.CONVSF,type,
                                target.makeCopy(env.lir),
                                ImList.Empty));
      }
      else{ // aggregate
        return(null);
      }
    }
    else if(Type.tag(target.type)==Type.FLOAT){
      if(Type.tag(type)==Type.INT){
        // convfi
        return(env.lir.operator(Op.CONVFS,type,
                                target.makeCopy(env.lir),
                                ImList.Empty));
      }
      else if(Type.tag(type)==Type.FLOAT){
        // convfx or convft
        if(Type.bits(target.type)>Type.bits(type)){
          // convft
          return(env.lir.operator(Op.CONVFT,type,
                                  target.makeCopy(env.lir),
                                  ImList.Empty));
        }
        else{
          // convfx
          return(env.lir.operator(Op.CONVFX,type,
                                  target.makeCopy(env.lir),
                                  ImList.Empty));
        }
      }
      else{ // aggregate
        return(null);
      }
    }
    return(null);
  }

  /**
   * Generate the IF node or the logical expression node.
   * @param thenNode The `then part' of the current `if-then-else' structure
   * @param elseNode The `else part' of the current `if-then-else' structure
   * @param condition The conditional expression of the current `if-then-else'
   *                  structure
   * @param makeIfNode True if generate the IF node
   * @return The IF node or the logical expression node
   **/
  private LirNode mergeExp(LirNode thenNode,LirNode elseNode,LirNode condition,
                           boolean makeIfNode){
    if(condition.type!=thenNode.type){
      condition=insertConvert(thenNode.type,condition);
    }

    if(condition==null) return(null);

    if(makeIfNode){
      LirNode ifNode=env.lir.operator(Op.IF,thenNode.type,
                                      condition.makeCopy(env.lir),
                                      thenNode.makeCopy(env.lir),
                                      elseNode.makeCopy(env.lir),
                                      ImList.Empty);
      return(ifNode);
    }
    else{
      LirNode thenPart=env.lir.operator(Op.BAND,thenNode.type,
                                        thenNode.makeCopy(env.lir),
                                        condition.makeCopy(env.lir),
                                        ImList.Empty);
      LirNode bnotNode=env.lir.operator(Op.BNOT,condition.type,
                                        condition.makeCopy(env.lir),
                                        ImList.Empty);
      LirNode elsePart=env.lir.operator(Op.BAND,elseNode.type,elseNode,
                                        bnotNode,ImList.Empty);
      LirNode borNode=env.lir.operator(Op.BOR,thenPart.type,thenPart,
                                       elsePart,ImList.Empty);
      return(borNode);
    }
  }
}
