/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

import coins.PassException;
import coins.backend.Function;
import coins.backend.util.BiList;
import coins.backend.util.BiLink;
import coins.ir.hir.SubpDefinition;
//##94 import coins.aflow.FlowResults;
import coins.FlowRoot;
import coins.HirRoot;
//##94 import coins.aflow.RegisterFlowAnalClasses;
//##94 import coins.aflow.SubpFlow;
//##94 import coins.aflow.Flow;
import coins.flow.SubpFlow;        //##94
import coins.flow.HirSubpFlowImpl; //##94
import coins.flow.Flow;            //##94
import coins.sym.Subp;             //##94
import coins.mdf.MacroFlowGraph;

/**
 * This class represents the tag `function'
 **/
class FunctionTag{
  /** The list of the graphs in the current function tag **/
  private BiList graphs;
  /** The line number of the current function in the source program **/
  private final int lineNumber;
  /** The name of the current function **/
  private final String functionName;

  /**
   * Constructor for LIR
   * @param f The current function
   **/
  FunctionTag(Function f){
    lineNumber=f.sourceLineNo;
    functionName=f.symbol.name;
    graphs=new BiList();

    graphs.add(new GraphTag(DisplayNames.CFG,f.flowGraph()));
    graphs.add(new GraphTag(DisplayNames.DOMTREE,f.flowGraph()));
    graphs.add(new GraphTag(DisplayNames.PDOMTREE,f.flowGraph()));
  }

  /**
   * Constructor for MDF
   * @param mfg The current macro flow graph
   * @throws PassException Any exceptions in it
   **/
  FunctionTag(MacroFlowGraph mfg) throws PassException{
    lineNumber=mfg.subpDef.getHirBody().getLineNumber();
    functionName=mfg.subpDef.getSubpSym().getName();
    graphs=new BiList();

    graphs.add(new GraphTag(DisplayNames.MFG,mfg));
  }

  /**
   * Constructor for HIR
   * @param hirRoot The current HirRoot
   * @param subpDef The current subprogram
   **/
  FunctionTag(HirRoot hirRoot,SubpDefinition subpDef){
    lineNumber=subpDef.getHirBody().getLineNumber();
    functionName=subpDef.getSubpSym().getName();
    graphs=new BiList();

    //##94 FlowResults.putRegClasses(new RegisterFlowAnalClasses());

    FlowRoot flowRoot=hirRoot.getFlowRoot();
    if(flowRoot==null)
      flowRoot=new FlowRoot(hirRoot);
    //##94 Flow flow=flowRoot.aflow;
    Flow flow = flowRoot.flow;         //##94
    Subp lCurrentSubp = subpDef.getSubpSym(); //##94

    //##94 FlowResults results=new FlowResults(flowRoot);
    //##94 SubpFlow subpFlow=flow.subpFlow(subpDef,results);
    //##94 flowRoot.aflow.setSubpFlow(subpFlow);
    //##94 subpFlow.controlFlowAnal();
    //##94 subpFlow.makeDominatorTree();
    //##94 subpFlow.makePostdominatorTree();
    //##94 BEGIN
    SubpFlow subpFlow = flowRoot.fSubpFlow;
    if ((subpFlow == null)||
        (subpFlow.getSubpSym() != lCurrentSubp)) {
      subpFlow = new HirSubpFlowImpl(flowRoot, subpDef);
    }
    if (flowRoot.flow.getFlowAnalStateLevel() <
        coins.flow.Flow.STATE_CFG_AVAILABLE)
      flow.controlFlowAnal(subpFlow);
    if (flowRoot.flow.getFlowAnalStateLevel() <
        coins.flow.Flow.STATE_DATA_FLOW_AVAILABLE)
      flowRoot.dataFlow = flowRoot.flow.dataFlowAnal(subpDef);

    //##94 END
    //##97 System.out.print("\nsnapshot func " + subpFlow.getSubpSym().getName() + "\n");  //##97   
    graphs.add(new GraphTag(DisplayNames.CFG,subpFlow));
    graphs.add(new GraphTag(DisplayNames.DOMTREE,subpFlow));
    graphs.add(new GraphTag(DisplayNames.PDOMTREE,subpFlow));
  }

  /**
   * Get the line number of the current function in the source program.
   * @return The line number of the current function in the source program
   **/
  private String lineNumber(){
    return("\""+lineNumber+"\"");
  }

  /**
   * Get the name of the current function.
   * @return The name of the current function
   **/
  private String functionName(){
    return("\""+functionName+"\"");
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
    str+=ws+"<"+TagName.FUNCTION+" "+TagName.NAME+"="+functionName();
    str+=" "+TagName.LINE+"="+lineNumber()+">\n";
    //##97 System.out.print("\nsnapshot " + str + "\n");  //##97
    
    for(BiLink p=graphs.first();!p.atEnd();p=p.next()){
      GraphTag gTag=(GraphTag)p.elem();
      str+=gTag.toString(space+1);
    }

    str+=ws+"</"+TagName.FUNCTION+">\n";
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
