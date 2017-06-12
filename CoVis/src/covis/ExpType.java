/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 * The ExpType class holds the data represented by XML tag "exp".
 */
public class ExpType {
  /** the data string */
  public String string;
  /** the line no. of the source */
  public int    lineNo;

  /**
   * Constructor
   * @param root NodeList object in the DOM, is the root Element of the tag
   */
  public ExpType(Element root) {
    try {
      String line = root.getAttribute(TagName.LINE);
      if (line.length() == 0) {
        lineNo = -1;
      } else {
        lineNo = Integer.valueOf(line).intValue();
        //System.out.println("ExpType :exp line = "+ lineNo);
      }

      NodeList childList = root.getChildNodes();
      StringBuffer buff = new StringBuffer();

      for (int j = 0; j< childList.getLength() ; ++j) {
        Node child = (Node)childList.item(j);
        if (child.getNodeType() == Node.TEXT_NODE) {
          String str = child.getNodeValue();
          if (str != null) {
            buff.append(str);
          }
        }
        string = buff.toString();
        //System.out.println("ExpType :string = "+ string);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Check the data
   * @return if there is no data then return true, else return false
   */
  public boolean isEmpty() {
    if (string == null) {
      return true;
    } else if (string.length() == 0) {
      return true;
    } else {
      return false;
    }
  }

}
