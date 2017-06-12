/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.Function;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.Symbol;
import coins.backend.Op;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.ana.LiveVariableSlotwise;
import coins.backend.ana.LiveVariableAnalysis;

import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.LinkedList;

/**
 * Generate DAG (Directed Acyclic Graph)
 **/
class GenerateDag{
  /** The environment of the SIMD module **/
  private SimdEnvironment env;
  /** The current function **/
  private Function f;
  /** The map between the variables and the expression **/
  private Hashtable instTable;

  /**
   * Constructor:
   * @param e The environment of the SIMD module
   * @param func The current function
   **/
  GenerateDag(SimdEnvironment e,Function func) {
    env=e;
    f=func;

    for(BiLink p=f.flowGraph().basicBlkList.first();!p.atEnd();p=p.next()){
      BasicBlk blk=(BasicBlk)p.elem();
      // Generate DAG
      invoke(blk);
    }
  }

  /**
   * Generate DAG
   * @param blk The current basic block
   **/
  void invoke(BasicBlk blk){
    Util util=new Util();
    LiveVariableAnalysis liveAna;
    liveAna=(LiveVariableAnalysis)f.require(LiveVariableSlotwise.analyzer);
    instTable=new Hashtable();

    // Generate DAG for each basic blocks
    for(BiLink p=blk.instrList().first();!p.atEnd();p=p.next()){
      // The current LIR node
      LirNode node=(LirNode)p.elem();

      switch(node.opCode){
        // If the current node is an assign expression
        case Op.SET:{
          // Replace the variables on the right hand side
          replace(node.kid(1),node,1);
          
          if(node.kid(0).opCode!=Op.REG){
            replace(node.kid(0),node,0);
          }
          
          // Check whether the MEM node is in the left hand side of the
          // current LIR.
          BiList list=util.findTargetLir(node,Op.MEM,new BiList());
          if(list.length()==0){ // There are no MEM nodes in the current LIR.
            LirNode n=(LirNode)instTable.get(node.kid(0));
            if(n!=null)
              instTable.remove(node.kid(0));

            // put the current LIR to the map
            instTable.put(node.kid(0),node.kid(1));
          }

          // There are some MEM nodes in the left hand side of the current
          // LIR and it has REG node on the right hand side.
          else if(node.kid(0).opCode==Op.REG){
            instTable.remove(node.kid(0));
          }
          break;
        }
/** Don't care
        case Op.CALL:
        case Op.PROLOGUE:
        case Op.EPILOGUE: break;
        default:{
          replace(node,null,-1);
        }
**/
      }
    }

    // Remove unnecessary expressions
    BiList liveOut=liveAna.liveOut(blk);
    for(BiLink p=blk.instrList().last();!p.atEnd();p=p.prev()){
      LirNode node=(LirNode)p.elem();
      //env.output.println(node);
      if(node.opCode==Op.SET){
        // Can't remove the current expression because the target symbol
        // lives out from the current basic block.
        if(node.kid(0).opCode==Op.REG &&
           liveOut.contains(((LirSymRef)node.kid(0)).symbol)){
          liveOut.remove(((LirSymRef)node.kid(0)).symbol);
          continue;
        }

        // Check whether the MEM node is in the current LIR.
        if(util.findTargetLir(node,Op.MEM,new BiList()).length()==0){
          // There are no MEM node in the current LIR.
          boolean used=false;

          // Check whether the variable which defined by "node" is used
          // between "node" and the last LIR of the current basic block.
          for(BiLink q=p.next();!q.atEnd();q=q.next()){
            LirNode n=(LirNode)q.elem();
            //env.output.println("\t"+n);

            // Check whether the variable which defined by "node" is used
            // in "n".
            if(n.opCode==Op.SET){
              // "n" is an assign expression.
              if(n.kid(0).opCode==Op.REG){
                BiList list=util.findTargetLir(n.kid(1),Op.REG,new BiList());
                for(BiLink pp=list.first();!pp.atEnd();pp=pp.next()){
                  LirNode nn=(LirNode)pp.elem();
                  if(nn.equals(node.kid(0))){
                    used=true;
                    break;
                  }
                }
                if(used) break;

                if(n.kid(0).equals(node.kid(0))) break;
              }
              else{
                BiList list=util.findTargetLir(n,Op.REG,new BiList());
                for(BiLink pp=list.first();!pp.atEnd();pp=pp.next()){
                  LirNode nn=(LirNode)pp.elem();
                  if(nn.equals(node.kid(0))){
                    used=true;
                    break;
                  }
                }
                if(used) break;
              }
            }
            else{
              BiList list=util.findTargetLir(n,Op.REG,new BiList());
              for(BiLink pp=list.first();!pp.atEnd();pp=pp.next()){
                LirNode nn=(LirNode)pp.elem();
                if(nn.equals(node.kid(0))){
                  used=true;
                  break;
                }
              }
              if(used) break;
            }
          }
          if(!used){
            // node is an unnecessary LIR
//            env.output.println("remove : "+node);
            p.unlink();
          }
        }
      }
    }

    f.touch();
//    blk.printIt(env.output);
  }

  /**
   * Replace the specified LIR node by expression.
   * @param node The target LIR
   * @param parent The parent of the target LIR
   * @param place The place of the target LIR in the parent
   **/
  private void replace(LirNode node,LirNode parent,int place){
//    env.output.println(node);
    LirNode replaced=(LirNode)instTable.get(node);
    if(replaced!=null){
//      env.output.println("++++++ "+replaced+"  "+parent);
      parent.setKid(place,replaced.makeCopy(env.lir));
    }
    else{
      for(int i=0;i<node.nKids();i++){
        replace(node.kid(i),node,i);
      }
    }
  }
}
