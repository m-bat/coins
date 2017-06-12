/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

import coins.PassException;
import coins.backend.cfg.FlowGraph;
import coins.backend.cfg.BasicBlk;
import coins.backend.util.BiList;
import coins.backend.util.BiLink;
//##94 import coins.aflow.SubpFlow;
//##94 import coins.aflow.BBlock;
import coins.flow.SubpFlow;   //##94
import coins.flow.BBlock;     //##94
import coins.mdf.MacroFlowGraph;
import coins.mdf.MacroTask;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * This class represents the tag `graph'
 **/
class GraphTag{
  /** The list of the nodes in the current graph **/
  private BiList nodes;
  /** The attribute `displayName' of the current graph **/
  private final String displayName;

  /**
   * Constructor for LIR
   * @param dName The attribute `displayName'
   * @param cfg The current control flow graph
   **/
  GraphTag(String dName,FlowGraph cfg){
    displayName=dName;
    nodes=new BiList();

    for(BiLink p=cfg.basicBlkList.first();!p.atEnd();p=p.next()){
      nodes.add(new NodeTag(dName,(BasicBlk)p.elem()));
    }
  }

  /**
   * Constructor for HIR
   * @param dName The attribute `displayName'
   * @param cfg The current subprogram
   **/
  GraphTag(String dName,SubpFlow cfg){
    displayName=dName;
    nodes=new BiList();

    for(Iterator ite=cfg.getReachableBBlocks().iterator();ite.hasNext();){
      nodes.add(new NodeTag(dName,(BBlock)ite.next()));
    }
  }

  /**
   * Constructor for MDF
   * @param dName The attribute `displayName'
   * @param mfg The current macro flow graph
   * @throws PassException Any exceptions in it
   **/
  GraphTag(String dName,MacroFlowGraph mfg) throws PassException{
    displayName=dName;
    nodes=new BiList();

    for(ListIterator ite=mfg.listIterator();ite.hasNext();){
      nodes.add(new NodeTag(dName,(MacroTask)ite.next()));
    }
  }

  /**
   * Get the attribute `displayName'.
   * @return The attribute `displayName'
   **/
  private String displayName(){
    return("\""+displayName+"\"");
  }

  /**
   * Generate the XML representation in string.
   * @param space The number of the white spaces
   * @return The XML representation
   **/
  public String toString(int space){
    String ws="";
    for(int i=0;i<space;i++){
      ws+="  ";
    }

    String str="";

    str+=ws+"<"+TagName.GRAPH+" "+TagName.DISPLAYNAME+"="+displayName()+">\n";
    for(BiLink p=nodes.first();!p.atEnd();p=p.next()){
      NodeTag nTag=(NodeTag)p.elem();
      str+=nTag.toString(space+1);
    }
    str+=ws+"</"+TagName.GRAPH+">\n";
    return(str);
  }

  /**
   * Generate the XML representation with no white spaces before.
   * @return The XML representation
   **/
  public String toString(){
    return(toString(0));
  }
}
