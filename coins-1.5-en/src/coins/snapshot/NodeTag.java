/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

import coins.PassException;
import coins.backend.cfg.BasicBlk;
import coins.backend.util.BiList;
import coins.backend.ana.Dominators;
import coins.backend.ana.Postdominators;
//##94 import coins.aflow.BBlock;
import coins.flow.BBlock;   //##94
import coins.mdf.MacroTask;

import java.util.Iterator;
import java.util.List;

/**
 * This class represents the tag `node'.
 **/
class NodeTag{
  /** The tag `id' in the current node **/
  private final LabelType id;
  /** The tag `next' in the current node **/
  private final LabelType next;
  /** The tag `prev' in the current node **/
  private final LabelType prev;
  /** The tag `statement' in the current node **/
  private final StatementTag statement;
  /** The tag `info' in the current node **/
  private final InfoTag info;

  /**
   * Constructor for LIR
   * @param dName The attribute `displayName'
   * @param blk The current basic block
   **/
  NodeTag(String dName,BasicBlk blk){
    BiList tmp=new BiList();
    tmp.add(blk);
    id=new LabelType(blk.label().name(),tmp);

    // make edges
    // if CFG
    if(dName.equals(DisplayNames.CFG)){
      if(blk.succList().length()>0)
        next=new LabelType(DisplayNames.SUCC,blk.succList());
      else
        next=null;

      if(blk.predList().length()>0)
        prev=new LabelType(DisplayNames.PRED,blk.predList());
      else
        prev=null;
    }
    // if Dominator Tree
    else if(dName.equals(DisplayNames.DOMTREE)){
      Dominators dom;
      dom=(Dominators)blk.flowGraph.function.require(Dominators.analyzer);
      if(dom.kids[blk.id]!=null && dom.kids[blk.id].length()>0)
        next=new LabelType(DisplayNames.DCHILD,dom.kids[blk.id]);
      else
        next=null;

      if(dom.idom[blk.id]!=null){
        BiList tmp1=new BiList();
        tmp1.add(dom.idom[blk.id]);
        prev=new LabelType(DisplayNames.DOM,tmp1);
      }
      else
        prev=null;
    }
    // if Postdominator Tree
    else if(dName.equals(DisplayNames.PDOMTREE)){
      Postdominators pdom;
      pdom=(Postdominators)blk.flowGraph.function.require(Postdominators.analyzer);
      if(pdom.kids[blk.id]!=null && pdom.kids[blk.id].length()>0)
        next=new LabelType(DisplayNames.PDCHILD,pdom.kids[blk.id]);
      else
        next=null;

      if(pdom.idom[blk.id]!=null){
        BiList tmp1=new BiList();
        tmp1.add(pdom.idom[blk.id]);
        prev=new LabelType(DisplayNames.PDOM,tmp1);
      }
      else
        prev=null;
    }
    else{
      next=null;
      prev=null;
    }

    // make statements
    statement=new StatementTag(blk.instrList());

    // make other information
    info=new InfoTag(blk);
  }

  /**
   * Constructor for HIR
   * @param dName The attribute `displayName'
   * @param blk The current basic block
   **/
  NodeTag(String dName,BBlock blk){
    BiList tmp=new BiList();
    tmp.add(blk);
    id=new LabelType(blk.getLabel().getName(),tmp);

    debug("\n", "BBlock", blk.toStringShort()); //##97
    // make edges
    // if CFG
    if(dName.equals(DisplayNames.CFG)){
      if(blk.getSuccList().size()>0){
    	debug(" ", "succListSize", "" + blk.getSuccList().size()); //##97
        tmp=new BiList();
        for(Iterator ite=blk.getSuccList().iterator();ite.hasNext();){
          tmp.add((BBlock)ite.next());
        }
        next=new LabelType(DisplayNames.SUCC,tmp);
      }
      else
        next=null;

      if(blk.getPredList().size()>0){
      	debug(" ", "predListSize", "" + blk.getSuccList().size()); //##97
        tmp=new BiList();
        for(Iterator ite=blk.getPredList().iterator();ite.hasNext();){
          tmp.add((BBlock)ite.next());
        }
        prev=new LabelType(DisplayNames.PRED,tmp);
      }
      else
        prev=null;
    }

    // if Dominator Tree
    else if(dName.equals(DisplayNames.DOMTREE)){
      //##94 List dchild=blk.getDominatedChildrenForSubpFlow();
      List dchild=blk.getDominatedChildren();  //##94
      if(dchild!=null && dchild.size()>0){
        BiList tmp1=new BiList();
        for(Iterator ite=dchild.iterator();ite.hasNext();){
          tmp1.add(ite.next());
        }
        next=new LabelType(DisplayNames.DCHILD,tmp1);
      }
      else
        next=null;

      //##94 BBlock dom=blk.getImmediateDominatorForSubpFlow();
      BBlock dom = blk.getImmediateDominator();  //##94
      if(dom!=null){
        BiList tmp1=new BiList();
        tmp1.add(dom);
        prev=new LabelType(DisplayNames.DOM,tmp1);
      }
      else
        prev=null;
    }
    // if Postdominator Tree
    else if(dName.equals(DisplayNames.PDOMTREE)){
      //##94 List pdchild=blk.getPostdominatedChildrenForSubpFlow();
      List pdchild = blk.getPostDominatedChildren();  //##94
      if(pdchild!=null && pdchild.size()>0){
        BiList tmp1=new BiList();
        for(Iterator ite=pdchild.iterator();ite.hasNext();){
          tmp1.add(ite.next());
        }
        next=new LabelType(DisplayNames.PDCHILD,tmp1);
      }
      else
        next=null;

      //##94 BBlock pdom=blk.getImmediatePostdominatorForSubpFlow();
      BBlock pdom = blk.getImmediatePostDominator();  //##94
      if(pdom!=null){
        BiList tmp1=new BiList();
        tmp1.add(pdom);
        prev=new LabelType(DisplayNames.PDOM,tmp1);
      }
      else
        prev=null;
    }
    else{
      next=null;
      prev=null;
    }
    // make statements
    statement=new StatementTag(blk.bblockSubtreeIterator());

    // make other information
    info=new InfoTag(blk);
  }

//##94 BEGIN
  NodeTag(String dName,coins.aflow.BBlock blk){
    BiList tmp=new BiList();
    tmp.add(blk);
    id=new LabelType(blk.getLabel().getName(),tmp);

    // make edges
    // if CFG
    if(dName.equals(DisplayNames.CFG)){
      if(blk.getSuccList().size()>0){
        tmp=new BiList();
        for(Iterator ite=blk.getSuccList().iterator();ite.hasNext();){
          tmp.add((BBlock)ite.next());
        }
        next=new LabelType(DisplayNames.SUCC,tmp);
      }
      else
        next=null;

      if(blk.getPredList().size()>0){
        tmp=new BiList();
        for(Iterator ite=blk.getPredList().iterator();ite.hasNext();){
          tmp.add((BBlock)ite.next());
        }
        prev=new LabelType(DisplayNames.PRED,tmp);
      }
      else
        prev=null;
    }

    // if Dominator Tree
    else if(dName.equals(DisplayNames.DOMTREE)){
      List dchild=blk.getDominatedChildrenForSubpFlow();
      if(dchild!=null && dchild.size()>0){
        BiList tmp1=new BiList();
        for(Iterator ite=dchild.iterator();ite.hasNext();){
          tmp1.add(ite.next());
        }
        next=new LabelType(DisplayNames.DCHILD,tmp1);
      }
      else
        next=null;

      //##94 BBlock dom=blk.getImmediateDominatorForSubpFlow();
      coins.aflow.BBlock dom = blk.getImmediateDominatorForSubpFlow();  //##94
      if(dom!=null){
        BiList tmp1=new BiList();
        tmp1.add(dom);
        prev=new LabelType(DisplayNames.DOM,tmp1);
      }
      else
        prev=null;
    }
    // if Postdominator Tree
    else if(dName.equals(DisplayNames.PDOMTREE)){
      List pdchild=blk.getPostdominatedChildrenForSubpFlow();
      if(pdchild!=null && pdchild.size()>0){
        BiList tmp1=new BiList();
        for(Iterator ite=pdchild.iterator();ite.hasNext();){
          tmp1.add(ite.next());
        }
        next=new LabelType(DisplayNames.PDCHILD,tmp1);
      }
      else
        next=null;

      //##94 BBlock pdom=blk.getImmediatePostdominatorForSubpFlow();
      coins.aflow.BBlock pdom = blk.getImmediatePostdominatorForSubpFlow();  //##94
      if(pdom!=null){
        BiList tmp1=new BiList();
        tmp1.add(pdom);
        prev=new LabelType(DisplayNames.PDOM,tmp1);
      }
      else
        prev=null;
    }
    else{
      next=null;
      prev=null;
    }
    // make statements
    statement=new StatementTag(blk.bblockSubtreeIterator());

    // make other information
    info=new InfoTag(blk);
  }
//##94 END

  /**
   * Constructor for MDF
   * @param dName The attribute `displayName'
   * @param mt The current macro task
   * @throws PassException Any exception in it
   **/
  NodeTag(String dName,MacroTask mt) throws PassException{
    BiList tmp=new BiList();
    tmp.add(mt);
    id=new LabelType(mt.label.getName(),tmp);

    // make edges
    // if MFG
    if(dName.equals(DisplayNames.MFG)){
      if(mt.succList.size()>0){
        tmp=new BiList();
        for(Iterator ite=mt.succList.iterator();ite.hasNext();){
          tmp.add((MacroTask)ite.next());
        }
        next=new LabelType(DisplayNames.SUCC,tmp);
      }
      else
        next=null;

      if(mt.predList.size()>0){
        tmp=new BiList();
        for(Iterator ite=mt.predList.iterator();ite.hasNext();){
          tmp.add((MacroTask)ite.next());
        }
        prev=new LabelType(DisplayNames.PRED,tmp);
      }
      else
        prev=null;
    }
    else{
      next=null;
      prev=null;
    }

    // make statements
    statement=new StatementTag(mt.blks());

    // make other information
    info=new InfoTag(mt);
  }

  /**
   * Generate the XML representation in string.
   * @param space The number of the white spaces
   * @return The XML representation
   **/
  public String toString(int space){
    String ws1="";
    for(int i=0;i<space;i++){
      ws1+="  ";
    }

    String ws="";
    for(int i=0;i<space+1;i++){
      ws+="  ";
    }

    String str="";
    str+=ws1+"<"+TagName.NODE+">\n";

    str+=ws+"<"+TagName.ID+" "+TagName.DISPLAYNAME+"="+id.displayName()+">\n";
    str+=id.toString(space+2);
    str+=ws+"</"+TagName.ID+">\n";

    if(next!=null){
      str+=ws+"<"+TagName.NEXT;
      str+=" "+TagName.DISPLAYNAME+"="+next.displayName()+">\n";
      str+=next.toString(space+2);
      str+=ws+"</"+TagName.NEXT+">\n";
    }

    if(prev!=null){
      str+=ws+"<"+TagName.PREV;
      str+=" "+TagName.DISPLAYNAME+"="+prev.displayName()+">\n";
      str+=prev.toString(space+2);
      str+=ws+"</"+TagName.PREV+">\n";
    }

    str+=ws+"<"+TagName.STATEMENT+">\n";
    str+=statement.toString(space+2);
    str+=ws+"</"+TagName.STATEMENT+">\n";

    str+=info.toString(space+1);

    str+=ws1+"</"+TagName.NODE+">\n";
    return(str);
  }

  /**
   * Generate the XML representation with no white spaces before.
   * @return The XML representation
   **/
  public String toString(){
    return(toString(0));
  }
  
  //##97 BEGIN
  /**
   * pHeader: "" or spaces or "\n"
   * pName  : name of the data to be printed (pData)
   * pData  : Data to be printed
   */
  void debug(String pHeader, String pName, String pData) {
	//##97 System.out.print(pHeader + " " + pName + " " + pData);
  }
  //##97 END
}
