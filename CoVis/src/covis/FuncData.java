/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 * The FuncData class holds the data represented by XML tag "function".
 * the data is showed on a tab.
 */
class FuncData {
  /** Function name */
  private String functionName;

  /** Function start line number at the source file */
  private final int  srcLine;

  /** an environment of CoVis */
  private VisEnvironment unitEnv;

  /** parent of this class data */
  public UnitData unitData;

  /** SubFuncData (Tag: Graph) list */
  public ViList subFuncDataList;
  /** current shown SubFuncData */
  public SubFuncData currentSubFuncData;

  /** selected Block */
  public BlockData currentBlock;

  /**
   * Constructor<br>
   * 
   * @param unitData  parent data of this FuncData
   * @param funcRoot  Element object in the DOM, is the root Element of
   *                  the data
   * @param env   VisEnvironment
   */
  FuncData(UnitData unitData, Element funcRoot, VisEnvironment env) {
    this.unitData = unitData;

    functionName = funcRoot.getAttribute(TagName.NAME);
    String line = funcRoot.getAttribute(TagName.LINE);
    if (line != null)
      srcLine = Integer.valueOf(line).intValue();
    else
      srcLine = 0;

    //System.out.println("FuncData : function = " +functionName);

    NodeList list = funcRoot.getElementsByTagName(TagName.GRAPH);

    subFuncDataList = new ViList();

    for (int i = 0 ; i<list.getLength() ; ++i) {
      Element element = (Element)list.item(i);
      //System.out.println("  "+i+"st element");
      SubFuncData subFunc = new SubFuncData(this, element, env);
      subFuncDataList.add(subFunc);
      if (currentSubFuncData == null)
        currentSubFuncData = subFunc;
    }

  }

  /**
   * return Function name
   * @return string of function name
   */
  public String funcName() {
    return functionName;
  }

  /**
   * get the first SubFuncData in this FuncData.
   * @return the first SubFunction
   */
  public SubFuncData firstSubFuncData() {
    if (subFuncDataList == null)
      return null;
    else if (subFuncDataList.isEmpty())
      return null;
    else {
      //ViList list = subFuncDataList.first();
      return (SubFuncData)subFuncDataList.firstElem();
    }
  }

  /**
   * get the SubFunction (graph) which title matches with name
   * @param name    graph name
   * @return        subFunction object
   */
  public SubFuncData getGraphByName(String name) {
    ViList q;
    SubFuncData subFunc;

    for (q = subFuncDataList.first(); !q.atEnd(); q=q.next()) {
      subFunc = (SubFuncData)q.elem();
      if (subFunc.title.equals(name)) {
        return subFunc;
      }    
    }
    return null;
  }

  /**
   * set current SubFuncData
   * @param subFunc selected SubFuncData
   */
  public void setSubFuncData(SubFuncData subFunc) {
    currentSubFuncData = subFunc;
  }

  /**
   * get the source line no.
   * @return source start line no. of this function
   */
  public int srcLineNo() {
    return srcLine;
  }

}
