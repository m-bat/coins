/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

import coins.backend.cfg.BasicBlk;
import coins.backend.util.BiList;
import coins.backend.util.BiLink;

/**
 * The super class of the LabelType and StringType.
 * This type has a list of the element, which is a information about the nodes
 * of a graph structure.
 * Also, it has a attribute `displayName' which specify the name of the
 * information.
 **/
abstract class AbstType{
  /** The list of the element **/
  protected BiList elem;
  /** The attribute of this type **/
  protected final String displayName;

  /**
   * Constructor
   * @param dName The attribute `displayName'
   **/
  AbstType(String dName){
    displayName=dName;
    elem=new BiList();
  }

  /**
   * Get the attribute `displayName'.
   * @return The attribute `displayName'
   **/
  public String displayName(){
    return("\""+displayName+"\"");
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
    for(BiLink p=elem.first();!p.atEnd();p=p.next()){
      str+=ws+"<"+TagName.ELEM+">"+(String)p.elem()+"</"+TagName.ELEM+">\n";
    }
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
