/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

/**
 * Utility
 **/
public class Util{
  /**
   * Constructor
   **/
  Util(){}

  /**
   * Escape some charactors. For example, translate `<' into "&lt;".
   * @param str The string literal
   * @return The escaped string literal
   **/
  public String prepare(String str){
    String tmp="";
    for(int i=0;i<str.length();i++){
      char c=str.charAt(i);
      switch(c){
        case '&':{
          tmp+="&amp;";
          break;
        }
        case '<':{
          tmp+="&lt;";
          break;
        }
        case '>':{
          tmp+="&gt;";
          break;
        }
        case '\n':{
          break;
        }
        default:{
          tmp+=c;
        }
      }
    }

    return(tmp);
//    str=str.replaceAll("&","&amp;");
//    str=str.replaceAll("<","&lt;");
//    str=str.replaceAll(">","&gt;");
//    str=str.replaceAll("\n","");
//
//    return(str);
  }

  /**
   * Unescape some charactors. For example, translate "&lt;" into `<'.
   * @param str The string literal
   * @return The unescaped string literal
   **/
/**
  public String revPrepare(String str){
    str=str.replaceAll("&gt;",">");
    str=str.replaceAll("&lt;","<");
    str=str.replaceAll("&amp;","&");

    return(str);
  }
**/
}
