/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 * The LabelType class holds the data represented by XML tag "labelType".
 */
public class LabelType {
  /** the attribute "displayName" */
  private String name;
  /** the list of the data represented by XML tag "elem" */
  private ViList list;

  /**
   * Constructor
   * @param root Element object in the DOM, and the root Element of the data
   */
  public LabelType(Element root) {
    createData(root);
  }
  
  /**
   * Constructor
   * @param root Element object in the DOM, and the root Element of the data
   * @param tag  target tag name to be listed
   */
  public LabelType(Element root, String tag) {
    NodeList nodeList = root.getElementsByTagName(tag);
    //System.out.println("  tag = "+tag);
    int num = nodeList.getLength();
    if (num > 1) {
      //System.out.println("There are "+num+" "+tag+". too many");
    } else if (num == 1) {
      createData((Element)nodeList.item(0));
    }
  }

  /**
   * create Data
   * @param root Element object in the DOM, and the root Element of the data
   */
  private void createData(Element root) {
    list = new ViList();

    name = root.getAttribute(TagName.DISPLAYNAME);
    //System.out.print(name+"> : ");

    try {
      NodeList nodeList = root.getElementsByTagName(TagName.ELEM);

      //System.out.println("There are "+nodeList.getLength()+" "+TagName.ELEM);
      for (int i = 0 ; i<nodeList.getLength() ; ++i) {
        Element node = (Element)nodeList.item(i);

        NodeList childList = node.getChildNodes();
        for (int j = 0; j< childList.getLength() ; ++j) {
          Node child = childList.item(j);
          StringBuffer buff = new StringBuffer();
          if (child.getNodeType() == Node.TEXT_NODE) {
            String str = child.getNodeValue();
            if (str != null) {
              buff.append(str);
            }
            //System.out.println("LabelType : "+buff);
            list.add(buff.toString());
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
   * Get the name
   * @return displayname
   */
  public String getName() {
    return name;
  }

  /**
   * Get the first data of the list
   * @return data
   */
  public String getFirstValue() {
    ViList a = list.first();
    return (String)a.elem();
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
