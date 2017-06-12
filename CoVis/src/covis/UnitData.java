/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.io.File;

/**
 *
 * The UnitData class holds the data represented by XML tag "module".
 * One of the function in the UnitData is showed on a tab.
 */
class UnitData {
  /**
   * Unit name
   */
  private final String unitName;
	
  /**
   * Unit Module
   */
  private final Element unitRoot;
	
  /** an environment of CoVis */
  private VisEnvironment unitEnv;

  /**
   * source file code of this Unit.
   */
  public final File file;
	
  /**
   * Functions in this Unit
   */
  public ViList funcDataList;
	
  /** LIR Visualizer */
  private Visualizer visUnit;

  /** C source code's path */
  public String unitSrcName;

  /** the Function when the only one function displays. */
  public FuncData currentFunc;

  /** number of Function in this Unit */
  public int numFunc = 0;

  /**
   * Constructor<br>
   * create the Data for the tag "module"
   * 
   * @param root   Element object in the DOM,
   *               is the top Element of the "module"
   * @param env    VisEnviroment
   */
  UnitData(Element root, VisEnvironment env) {
    unitRoot = root;
    unitEnv = env;

    unitName = root.getAttribute(TagName.NAME);
    unitSrcName  =  root.getAttribute(TagName.SRC); 
    file = new File(unitSrcName);

    NodeList funcList = root.getElementsByTagName(TagName.FUNCTION);

    funcDataList = new ViList();
    numFunc = funcList.getLength();
    for (int j = 0 ; j<numFunc ; ++j) {
      Element child = (Element)funcList.item(j);
      FuncData funcData = new FuncData(this, child, env);
      funcDataList.add(funcData);

      if (currentFunc == null) {
        currentFunc = funcData;
      }
    }

  }


  /**
   * return the name of this unit.
   * @return unit name
   */
  String unitName() {
    return unitName;
  }

  /**
   * return a source file object.
   * @return source file
   */
  File getSourceFile() {
    return file;
  }

  /**
   * get the function data in the UnitData by using function name.
   * @param name    function name
   * @return        function data
   */
  public FuncData getFuncByName(String name) {
    ViList q;
    FuncData func;

    for (q = funcDataList.first(); !q.atEnd(); q=q.next()) {
      func = (FuncData)q.elem();
      if (func.funcName().equals(name)) {
        return func;
      }    
    }
    return null;
  }

  /**
   * set current function data
   */
  public void putCurrentFunc(FuncData func) {
    currentFunc = func;
  }

}
