/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

import coins.backend.cfg.BasicBlk;
import coins.backend.util.BiList;
import coins.backend.util.BiLink;
import coins.backend.ana.Dominators;
import coins.backend.ana.Postdominators;
import coins.backend.ana.DominanceFrontiers;
import coins.backend.ana.ControlDependences;
import coins.backend.ana.LiveVariableAnalysis;
import coins.backend.ana.LiveVariableSlotwise;
import coins.backend.sym.Symbol;
//##94 import coins.aflow.BBlock;
//##94 import coins.aflow.FlowAnalSymVector;
import coins.flow.BBlock;            //##94
import coins.flow.FlowAnalSymVector; //##94
import coins.sym.Sym;
import coins.mdf.MacroTask;

import java.util.Iterator;
import java.util.List;

/**
 * This class represents the tag `info'.
 **/
class InfoTag{
  /** The list of the element `labelList' **/
  private BiList labelList;
  /** The list of the element `stringList' **/
  private BiList stringList;

  /**
   * Constructor for LIR
   * @param blk The current basic block
   **/
  InfoTag(BasicBlk blk){
    labelList=new BiList();
    stringList=new BiList();

    LabelType lType=successor(blk);
    if(lType!=null) labelList.add(lType);

    lType=predecessor(blk);
    if(lType!=null) labelList.add(lType);

    lType=dominator(blk);
    if(lType!=null) labelList.add(lType);

    lType=dominatedChildren(blk);
    if(lType!=null) labelList.add(lType);

    lType=dominanceFrontier(blk);
    if(lType!=null) labelList.add(lType);

    lType=postdominator(blk);
    if(lType!=null) labelList.add(lType);

    lType=postdominatedChildren(blk);
    if(lType!=null) labelList.add(lType);

    lType=postdominanceFrontier(blk);
    if(lType!=null) labelList.add(lType);

    StringType sType=liveIn(blk);
    if(sType!=null) stringList.add(sType);

    sType=liveOut(blk);
    if(sType!=null) stringList.add(sType);

  }

  /**
   * Constructor for HIR
   * @param blk The current basic block
   **/
  InfoTag(BBlock blk){
    labelList=new BiList();
    stringList=new BiList();

    LabelType lType=successor(blk);
    if(lType!=null) labelList.add(lType);

    lType=predecessor(blk);
    if(lType!=null) labelList.add(lType);

    lType=dominator(blk);
    if(lType!=null) labelList.add(lType);

    lType=dominatedChildren(blk);
    if(lType!=null) labelList.add(lType);

    lType=postdominator(blk);
    if(lType!=null) labelList.add(lType);

    lType=postdominatedChildren(blk);
    if(lType!=null) labelList.add(lType);

    StringType sType=liveIn(blk);
    if(sType!=null) stringList.add(sType);

    sType=liveOut(blk);
    if(sType!=null) stringList.add(sType);
  }

//##94 BEGIN
  InfoTag(coins.aflow.BBlock blk){
    labelList=new BiList();
    stringList=new BiList();

    LabelType lType=successor(blk);
    if(lType!=null) labelList.add(lType);

    lType=predecessor(blk);
    if(lType!=null) labelList.add(lType);

    lType=dominator(blk);
    if(lType!=null) labelList.add(lType);

    lType=dominatedChildren(blk);
    if(lType!=null) labelList.add(lType);

    lType=postdominator(blk);
    if(lType!=null) labelList.add(lType);

    lType=postdominatedChildren(blk);
    if(lType!=null) labelList.add(lType);

    StringType sType=liveIn(blk);
    if(sType!=null) stringList.add(sType);

    sType=liveOut(blk);
    if(sType!=null) stringList.add(sType);
  }
//##94 END
  /**
   * Constructor for MDF
   * @param mt The current macro task
   **/
  InfoTag(MacroTask mt){
    labelList=new BiList();
    stringList=new BiList();
/**
    LabelType lType=successor(blk);
    if(lType!=null) labelList.add(lType);

    lType=predecessor(blk);
    if(lType!=null) labelList.add(lType);

    StringType sType=liveIn(blk);
    if(sType!=null) stringList.add(sType);

    sType=liveOut(blk);
    if(sType!=null) stringList.add(sType);
**/
  }

  /**
   * Generate information `successor' in LIR.
   * @param blk The current basic block
   * @return The information `successor'
   **/
  private LabelType successor(BasicBlk blk){
    if(blk.succList().length()==0) return(null);
    return(new LabelType(DisplayNames.SUCC,blk.succList()));
  }

  /**
   * Generate information `successor' in HIR.
   * @param blk The current basic block
   * @return The information `successor'
   **/
  private LabelType successor(BBlock blk){
    if(blk.getSuccList().size()==0) return(null);

    BiList tmp=new BiList();
    for(Iterator ite=blk.getSuccList().iterator();ite.hasNext();){
      tmp.add(ite.next());
    }

    return(new LabelType(DisplayNames.SUCC,tmp));
  }

//##94 BEGIN
  private LabelType successor(coins.aflow.BBlock blk){
    if(blk.getSuccList().size()==0) return(null);

    BiList tmp=new BiList();
    for(Iterator ite=blk.getSuccList().iterator();ite.hasNext();){
      tmp.add(ite.next());
    }

    return(new LabelType(DisplayNames.SUCC,tmp));
  }
//##94 END
  /**
   * Generate information `predecessor' in LIR.
   * @param blk The current basic block
   * @return The information `predecessor'
   **/
  private LabelType predecessor(BasicBlk blk){
    if(blk.predList().length()==0) return(null);
    return(new LabelType(DisplayNames.PRED,blk.predList()));
  }

  /**
   * Generate information `predecessor' in HIR.
   * @param blk The current basic block
   * @return The information `predecessor'
   **/
  private LabelType predecessor(BBlock blk){
    if(blk.getPredList().size()==0) return(null);

    BiList tmp=new BiList();
    for(Iterator ite=blk.getPredList().iterator();ite.hasNext();){
      tmp.add(ite.next());
    }

    return(new LabelType(DisplayNames.PRED,tmp));
  }

//##94 BEGIN
  private LabelType predecessor(coins.aflow.BBlock blk){
    if(blk.getPredList().size()==0) return(null);

    BiList tmp=new BiList();
    for(Iterator ite=blk.getPredList().iterator();ite.hasNext();){
      tmp.add(ite.next());
    }

    return(new LabelType(DisplayNames.PRED,tmp));
  }
//##94 END
  /**
   * Generate information `dominator' in LIR.
   * @param blk The current basic block
   * @return The information `dominator'
   **/
  private LabelType dominator(BasicBlk blk){
    Dominators dom;
    dom=(Dominators)blk.flowGraph.function.require(Dominators.analyzer);
    if(dom.idom[blk.id]==null) return(null);

    BiList tmp=new BiList();
    tmp.add(dom.idom[blk.id]);

    return(new LabelType(DisplayNames.DOM,tmp));
  }

  /**
   * Generate information `dominator' in HIR.
   * @param blk The current basic block
   * @return The information `dominator'
   **/
  private LabelType dominator(BBlock blk){
    //##94 BBlock dom=blk.getImmediateDominatorForSubpFlow();
    BBlock dom = blk.getImmediateDominator();  //##94
    if(dom==null) return(null);

    BiList tmp=new BiList();
    tmp.add(dom);

    return(new LabelType(DisplayNames.DOM,tmp));
  }

//##94 BEGIN
  private LabelType dominator(coins.aflow.BBlock blk){
    //##94 BBlock dom=blk.getImmediateDominatorForSubpFlow();
    coins.aflow.BBlock dom=blk.getImmediateDominatorForSubpFlow();
    if(dom==null) return(null);

    BiList tmp=new BiList();
    tmp.add(dom);

    return(new LabelType(DisplayNames.DOM,tmp));
  }

//##94 END

  /**
   * Generate information `dominated children' in LIR.
   * @param blk The current basic block
   * @return The information `dominated children'
   **/
  private LabelType dominatedChildren(BasicBlk blk){
    Dominators dom;
    dom=(Dominators)blk.flowGraph.function.require(Dominators.analyzer);
    if(dom.kids[blk.id]==null) return(null);
    if(dom.kids[blk.id].length()==0) return(null);

    return(new LabelType(DisplayNames.DCHILD,dom.kids[blk.id]));
  }

  /**
   * Generate information `dominated children' in HIR.
   * @param blk The current basic block
   * @return The information `dominated children'
   **/
  private LabelType dominatedChildren(BBlock blk){
    //##94 List dchild=blk.getDominatedChildrenForSubpFlow();
    List dchild = blk.getDominatedChildren();  //##94
    if(dchild==null) return(null);
    if(dchild.size()==0) return(null);

    BiList tmp=new BiList();
    for(Iterator ite=dchild.iterator();ite.hasNext();){
      tmp.add(ite.next());
    }

    return(new LabelType(DisplayNames.DCHILD,tmp));
  }

//##94 BEGIN
  private LabelType dominatedChildren(coins.aflow.BBlock blk){
    List dchild=blk.getDominatedChildrenForSubpFlow();
    if(dchild==null) return(null);
    if(dchild.size()==0) return(null);

    BiList tmp=new BiList();
    for(Iterator ite=dchild.iterator();ite.hasNext();){
      tmp.add(ite.next());
    }

    return(new LabelType(DisplayNames.DCHILD,tmp));
  }
//##94 END

  /**
   * Generate information `postdominator' in LIR.
   * @param blk The current basic block
   * @return The information `postdominator'
   **/
  private LabelType postdominator(BasicBlk blk){
    Postdominators pdom;
    pdom=(Postdominators)blk.flowGraph.function.require(Postdominators.analyzer);
    if(pdom.idom[blk.id]==null) return(null);

    BiList tmp=new BiList();
    tmp.add(pdom.idom[blk.id]);

    return(new LabelType(DisplayNames.PDOM,tmp));
  }

  /**
   * Generate information `postdominator' in HIR.
   * @param blk The current basic block
   * @return The information `postdominator'
   **/
  private LabelType postdominator(BBlock blk){
    //##94 BBlock pdom=blk.getImmediatePostdominatorForSubpFlow();
    BBlock pdom = blk.getImmediatePostDominator();  //##94
    if(pdom==null) return(null);

    BiList tmp=new BiList();
    tmp.add(pdom);

    return(new LabelType(DisplayNames.PDOM,tmp));
  }

//##94 BEGIN
  private LabelType postdominator(coins.aflow.BBlock blk){
    //##94 BBlock pdom=blk.getImmediatePostdominatorForSubpFlow();
    coins.aflow.BBlock pdom=blk.getImmediatePostdominatorForSubpFlow();  //##94
    if(pdom==null) return(null);

    BiList tmp=new BiList();
    tmp.add(pdom);

    return(new LabelType(DisplayNames.PDOM,tmp));
  }
//##94 END
  /**
   * Generate information `postdominated children' in LIR.
   * @param blk The current basic block
   * @return The information `postdominated children'
   **/
  private LabelType postdominatedChildren(BasicBlk blk){
    Postdominators pdom;
    pdom=(Postdominators)blk.flowGraph.function.require(Postdominators.analyzer);
    if(pdom.kids[blk.id]==null) return(null);
    if(pdom.kids[blk.id].length()==0) return(null);

    return(new LabelType(DisplayNames.PDCHILD,pdom.kids[blk.id]));
  }

  /**
   * Generate information `postdominated children' in HIR.
   * @param blk The current basic block
   * @return The information `postdominated children'
   **/
  private LabelType postdominatedChildren(BBlock blk){
    //##94 List pdchild=blk.getPostdominatedChildrenForSubpFlow();
    List pdchild = blk.getPostDominatedChildren();  //##94
    if(pdchild==null) return(null);
    if(pdchild.size()==0) return(null);

    BiList tmp=new BiList();
    for(Iterator ite=pdchild.iterator();ite.hasNext();){
      tmp.add(ite.next());
    }

    return(new LabelType(DisplayNames.PDCHILD,tmp));
  }

//##94 BEGIN
  private LabelType postdominatedChildren(coins.aflow.BBlock blk){
    List pdchild=blk.getPostdominatedChildrenForSubpFlow();
    if(pdchild==null) return(null);
    if(pdchild.size()==0) return(null);

    BiList tmp=new BiList();
    for(Iterator ite=pdchild.iterator();ite.hasNext();){
      tmp.add(ite.next());
    }

    return(new LabelType(DisplayNames.PDCHILD,tmp));
  }
//##94 END
  /**
   * Generate information `dominance frontier' in LIR.
   * @param blk The current basic block
   * @return The information `dominance frontier'
   **/
  private LabelType dominanceFrontier(BasicBlk blk){
    DominanceFrontiers df;
    df=(DominanceFrontiers)blk.flowGraph.function.require(DominanceFrontiers.analyzer);
    if(df.frontiers[blk.id]==null) return(null);
    if(df.frontiers[blk.id].length()==0) return(null);

    return(new LabelType(DisplayNames.DF,df.frontiers[blk.id]));
  }

  /**
   * Generate information `control dependence' in LIR.
   * @param blk The current basic block
   * @return The information `control dependence'
   **/
  private LabelType postdominanceFrontier(BasicBlk blk){
    ControlDependences cdep;
    cdep=(ControlDependences)blk.flowGraph.function.require(ControlDependences.analyzer);
    if(cdep.frontiers[blk.id]==null) return(null);
    if(cdep.frontiers[blk.id].length()==0) return(null);

    return(new LabelType(DisplayNames.CDEP,cdep.frontiers[blk.id]));
  }

  /**
   * Generate information `live in' in LIR.
   * @param blk The current basic block
   * @return The information `live in'
   **/
  private StringType liveIn(BasicBlk blk){
    LiveVariableAnalysis live;
    live=(LiveVariableAnalysis)blk.flowGraph.function.require(LiveVariableSlotwise.analyzer);
    BiList tmp=live.liveIn(blk);

    if(tmp.length()==0) return(null);

    BiList in=new BiList();
    for(BiLink p=tmp.first();!p.atEnd();p=p.next()){
      Symbol s=(Symbol)p.elem();
      in.add(s.name);
    }

    return(new StringType(DisplayNames.LIVEIN,in));
  }

  /**
   * Generate information `live in' in HIR.
   * @param blk The current basic block
   * @return The information `live in'
   **/
  private StringType liveIn(BBlock blk){
    //##94 FlowAnalSymVector live=blk.getPLiveIn();
    FlowAnalSymVector live = blk.getLiveIn();  //##94
    if(live.isZero()) return(null);

    BiList tmp=new BiList();
    for(Iterator ite=live.flowAnalSyms().iterator();ite.hasNext();){
      tmp.add(((Sym)ite.next()).getName());
    }

    return(new StringType(DisplayNames.LIVEIN,tmp));
  }

//##94 BEGIN
  private StringType liveIn(coins.aflow.BBlock blk){
    //##94 FlowAnalSymVector live=blk.getPLiveIn();
    coins.aflow.FlowAnalSymVector live=blk.getPLiveIn(); //##94
    if(live.isZero()) return(null);

    BiList tmp=new BiList();
    for(Iterator ite=live.flowAnalSyms().iterator();ite.hasNext();){
      tmp.add(((Sym)ite.next()).getName());
    }

    return(new StringType(DisplayNames.LIVEIN,tmp));
  }
//##94 END
  /**
   * Generate information `live out' in LIR.
   * @param blk The current basic block
   * @return The information `live out'
   **/
  private StringType liveOut(BasicBlk blk){
    LiveVariableAnalysis live;
    live=(LiveVariableAnalysis)blk.flowGraph.function.require(LiveVariableSlotwise.analyzer);
    BiList tmp=live.liveOut(blk);

    if(tmp.length()==0) return(null);

    BiList in=new BiList();
    for(BiLink p=tmp.first();!p.atEnd();p=p.next()){
      Symbol s=(Symbol)p.elem();
      in.add(s.name);
    }

    return(new StringType(DisplayNames.LIVEOUT,in));
  }

  /**
   * Generate information `live out' in HIR.
   * @param blk The current basic block
   * @return The information `live out'
   **/
  private StringType liveOut(BBlock blk){
    //##94 FlowAnalSymVector live=blk.getPLiveOut();
    FlowAnalSymVector live = blk.getLiveOut();  //##94
    if(live.isZero()) return(null);

    BiList tmp=new BiList();
    for(Iterator ite=live.flowAnalSyms().iterator();ite.hasNext();){
      tmp.add(((Sym)ite.next()).getName());
    }
    return(new StringType(DisplayNames.LIVEOUT,tmp));
  }

//##94 BEGIN
    private StringType liveOut(coins.aflow.BBlock blk){
      //##94 FlowAnalSymVector live=blk.getPLiveOut();
      coins.aflow.FlowAnalSymVector live = blk.getPLiveOut();
      if(live.isZero()) return(null);

      BiList tmp=new BiList();
      for(Iterator ite=live.flowAnalSyms().iterator();ite.hasNext();){
        tmp.add(((Sym)ite.next()).getName());
      }
    return(new StringType(DisplayNames.LIVEOUT,tmp));
  }
//##94 END

  /**
   * Generate the XML representation in string.
   * @param space The number of the white spaces
   * @return The XML representation
   **/
  public String toString(int space){
    String ws1="";
    for(int i=0;i<space+1;i++){
      ws1+="  ";
    }

    String ws="";
    for(int i=0;i<space;i++){
      ws+="  ";
    }

    String str=ws+"<"+TagName.INFO+">\n";
    for(BiLink p=labelList.first();!p.atEnd();p=p.next()){
      LabelType lType=(LabelType)p.elem();
      str+=ws1+"<"+TagName.LABEL_LIST+" "+TagName.DISPLAYNAME+"=";
      str+=lType.displayName()+">\n";
      str+=lType.toString(space+2);
      str+=ws1+"</"+TagName.LABEL_LIST+">\n";
    }
    for(BiLink p=stringList.first();!p.atEnd();p=p.next()){
      StringType sType=(StringType)p.elem();
      str+=ws1+"<"+TagName.STRING_LIST+" "+TagName.DISPLAYNAME+"=";
      str+=sType.displayName()+">\n";
      str+=sType.toString(space+2);
      str+=ws1+"</"+TagName.STRING_LIST+">\n";
    }
    str+=ws+"</"+TagName.INFO+">\n";
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
