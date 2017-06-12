/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 * The StatementType class holds the data represented by XML tag "statement".
 */
public class StatementType {
 /** the data list , each element is the data described by XML tag "exp". */
  private ViList list;

  /**
   * Constructor
   * @param root NodeList object in the DOM, is the root Element of the tag
   * @param tag  tag string to get the data
   */
  public StatementType(NodeList root, String tag) {
    try {
      for (int n = 0 ; n<root.getLength() ; ++n) {
        Element node = (Element)root.item(n);
        NodeList tagList = node.getElementsByTagName(tag);
        if (tagList.getLength() == 0) {
          return;
        } else {
          //System.out.println(tagList.getLength()+" data is found at "+tag);
          for (int m = 0 ; m<tagList.getLength() ; ++m) {
            Element node2 = (Element)tagList.item(m);
            NodeList nodeList = node2.getElementsByTagName(TagName.EXP);

            if (nodeList.getLength() == 0) {
              //System.err.println("No expression in this statement");

            } else {
              list = new ViList();
              for (int i = 0 ; i<nodeList.getLength() ; ++i) {
                ExpType exp = new ExpType((Element)nodeList.item(i));

                //System.out.println(exp.lineNo+" : "+exp.string);
                list.add(exp);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Get the data list
   * @return data list
   */
  public ViList getList() {
    return list;
  }

  /**
   * Check the data list
   * @return if there is no data then return true, else return false
   */
  public boolean isEmpty() {
    if (list == null) {
      //System.out.println("list is null");
      return true;
    }
    if (list.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

}
