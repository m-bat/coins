/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.util.HashMap;

/**
 *
 * The SubFuncData class holds the data represented by XML tag "graph"
 * graph represent the function (CFG,DOM,PDOM, etc.).
 */
class SubFuncData {
  /**
   * Function name
   */
  public String title;

  /**
   * Function data (it is the parent of SubFuncData)
   */
  private final FuncData func;

  /**
   * basic block list in the SubFuncData
   */
  public ViList blockList;
	
  /** A hash that contains label strings and basic blocks  */
  public HashMap blockHash;

  /**
   * Constructor
   * 
   * @param func  parent data of this SubFuncData
   * @param root  Element object in the DOM, is the root Element of the data
   * @param env   VisEnvironment
   */
  SubFuncData(FuncData func, Element root, VisEnvironment env) {
    this.func = func;

    title = root.getAttribute(TagName.DISPLAYNAME);
    NodeList list = root.getElementsByTagName(TagName.NODE);

    blockList = new ViList();
    blockHash = new HashMap();

    for (int i = 0 ; i<list.getLength() ; ++i) {
      Element element = (Element)list.item(i);
      BlockData block = new BlockData(this, element);
      blockHash.put(block.label, block);
      blockList.add(block);
    }
  }


  /**
   * return specified type of code can be displayed
   * @return true if the code can be displayed
   */
  public boolean isAvalable(int type) {
    if (type == VisEnvironment.SOURCE) {
      if (func.unitData.unitSrcName == null) {
        return false;
      } else {
        return true;
      }
    }
    BlockData blk = (BlockData)blockList.first().elem();
    if (blk == null)
      return false;
    else {
      return !blk.isEmpty(type);
    }
  }

}
