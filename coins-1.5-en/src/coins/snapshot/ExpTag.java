/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

/**
 * This class represents the tag `exp'.
 **/
class ExpTag{
  /** The elements of the tag `exp' **/
  private final String exp;
  /** The line number of the current exp in the source program **/
  private final int line;

  /**
   * Constructor
   * @param e The element of the tag
   * @param n The line number of the current exp in the source program
   **/
  ExpTag(String e,int n){
    Util util=new Util();
    exp=util.prepare(e);
    line=n;
  }

  /**
   * Get the line number of the current exp tag in the source program.
   * @return The line number of the current exp tag in the source program
   **/
  private String line(){
    return("\""+line+"\"");
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
    String str=ws+"<"+TagName.EXP;
    if(line!=-1) str+=" "+TagName.LINE+"="+line();
    str+=">"+exp+"</"+TagName.EXP+">";
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
