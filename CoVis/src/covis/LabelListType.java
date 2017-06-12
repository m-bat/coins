/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 * The LabelListType class holds the data represented by XML tag "labelList"
 * and "stringList".
 */
public class LabelListType {
  /**  list of LabelType */
  private ViList listList;

  /**
   * Constructor
   * @param root Element object in the DOM, is the root Element of the tag
   * @param tag  tag string to get the data
   */
  public LabelListType(Element root, String tag) {
    try {
      NodeList nodeList = root.getElementsByTagName(tag);
      if (nodeList.getLength() == 0)
        return;

      //System.out.println("There are "+nodeList.getLength()+" "+tag);
      listList = new ViList();

      for (int i = 0 ; i<nodeList.getLength() ; ++i) {
        LabelType base = new LabelType((Element)nodeList.item(i));
        if (!base.isEmpty())
          listList.add(base);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Get the data list
   * @return data list
   */
  public ViList getListList() {
    return listList;
  }

  /**
   * Check the data list
   * @return if there is no data then return true, else return false
   */
  public boolean isEmpty() {
    if (listList == null) {
      //System.out.println("list is null");
      return true;
    }
    if (listList.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

}
