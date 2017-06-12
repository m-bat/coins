/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.aflow.BBlock;
import coins.aflow.SubpFlow;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Find the strong connected component in the current control flow graph.
 * The strong connected component in the control flow graph means a loop
 * structure. The MDF module generates a loop type macro task by using
 * this information.
 **/
class FindScc{
  /** The strong connected component **/
  final LinkedList scc;
  /** The environment of the MDF module **/
  private MdfEnvironment env;
  /** The counter for depth first search **/
  private int nextDfsNum;
  /** The number of the basic blocks in the current control flow graph **/
  private int idBound;
  /** The depth first search number **/
  private int[] dfsNum;
  /** The variable used during depth first search **/
  private int[] low;
  /** The stack for depth first search **/
  private Stack stack;

  /**
   * Constructor
   * @param e The environment of the MDF module
   * @param subpFlow The flow information about the current subprogram
   **/
  FindScc(MdfEnvironment e,SubpFlow subpFlow){
    env=e;
    scc=new LinkedList();

    nextDfsNum=0;
    idBound=subpFlow.getBBlockTable().size()+1;
    dfsNum=new int[idBound];
    low=new int[idBound];

    for(int i=0;i<idBound;i++){
      dfsNum[i]=-1;
      low[i]=-1;
    }

    stack=new Stack();

    for(Iterator ite=subpFlow.getBBlocks().iterator();ite.hasNext();){
      BBlock blk=(BBlock)ite.next();
//      blk.getPDefined();
      if(dfsNum[blk.getBBlockNumber()]==-1)
        dfs(blk);
    }
  }

  /**
   * Depth first search and get the Strongly Connected Component (SCC).
   **/
  private void dfs(BBlock blk){
    dfsNum[blk.getBBlockNumber()]=nextDfsNum;
    low[blk.getBBlockNumber()]=nextDfsNum++;

    stack.push(blk);

    for(Iterator ite=blk.getSuccList().iterator();ite.hasNext();){
      BBlock succ=(BBlock)ite.next();
      
      if(dfsNum[succ.getBBlockNumber()]==-1){
        dfs(succ);
        if(low[blk.getBBlockNumber()]>low[succ.getBBlockNumber()])
          low[blk.getBBlockNumber()]=low[succ.getBBlockNumber()];
      }
      
      if(dfsNum[succ.getBBlockNumber()]<dfsNum[blk.getBBlockNumber()] &&
         stack.contains(succ) &&
         dfsNum[succ.getBBlockNumber()]<low[blk.getBBlockNumber()])
        low[blk.getBBlockNumber()]=dfsNum[succ.getBBlockNumber()];
    }

    if(low[blk.getBBlockNumber()]==dfsNum[blk.getBBlockNumber()]){
      LinkedList sccElem=new LinkedList();
      BBlock b=null;
      do{
        b=(BBlock)stack.pop();
        sccElem.addFirst(b);
      }while(b!=blk);
      scc.addFirst(sccElem); // reverse order
    }
  }

  /**
   * Debug print
   **/
  void print(){
    for(Iterator ite=scc.iterator();ite.hasNext();){
      LinkedList elem=(LinkedList)ite.next();
      env.output.print("SCC :");
      for(Iterator eite=elem.iterator();eite.hasNext();){
        BBlock blk=(BBlock)eite.next();
        env.output.print(" "+blk.getBBlockNumber()+"("+
                         blk.getLabel().getName()+")");
      }
      env.output.println();
    }
  }
}
