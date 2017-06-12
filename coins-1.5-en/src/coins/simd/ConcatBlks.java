/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.Op;
import coins.backend.Function;
import coins.backend.util.BiLink;
import coins.backend.cfg.BasicBlk;
import coins.backend.util.BiList;
import coins.backend.lir.LirNode;

/**
 * Concatenate basic blocks.
 * This module concatenate block A and B descibed below:
 * The basic block A has only one predecessor B.
 * The basic block B has only one successor A.
 * There are no branches in A and B. Also, there are no junction in
 * A and B.
 **/
class ConcatBlks{
  /** The environment of the SIMD module **/
  private SimdEnvironment env;
  /** The current function **/
  private Function f;

  /**
   * Constructor
   * @param e The environment of the SIMD module
   * @param func The current function
   **/
  ConcatBlks(SimdEnvironment e,Function func){
    env=e;
    f=func;
    //f.printIt(env.output);
  }

  /**
   * Concatenate basic blocks.
   **/
  void invoke(){
    BiList removeList=new BiList();
    boolean changed=true;
    while(changed){
      changed=false;
      removeList.clear();
      for(BiLink p=f.flowGraph().basicBlkList.first();!p.atEnd();p=p.next()){
        BasicBlk blk=(BasicBlk)p.elem();
        if(!removeList.contains(blk) && blk.succList().length()==1){
          BasicBlk succ=(BasicBlk)blk.succList().first().elem();

          if(f.flowGraph().basicBlkList.contains(succ) && 
             !succ.equals(f.flowGraph().exitBlk()) &&
             succ.predList().length()==1){
            changed=true;
            blk.instrList().last().unlink();
            blk.instrList().last().addAllAfter(succ.instrList());
            succ.clearEdges();
            blk.maintEdges();
            f.touch();
            removeList.add(succ);
          }
        }
      }
      for(BiLink p=removeList.first();!p.atEnd();p=p.next()){
        BasicBlk blk=(BasicBlk)p.elem();
        f.flowGraph().basicBlkList.remove(blk);
        //env.output.println("Remove : blk["+blk.id+"]");
      }
    }
  }
}
