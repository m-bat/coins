/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lir2c;

/**
 * ConvValue: converts value name to regular(not contains C operators)
 * value name.
 **/
public class ConvValue{
  /**
     Constructor(with no arg)
  */
  public ConvValue(){}

  /** 
   * replace: it replaces reserved characters ("@","%",...) to under score 
   * characters.
   * It uses java.lang.String@replace method.
   *
   * @param old_str old string (that will convert to regular value name).
   * @return the replaced string.
   **/
  public String replace(String old_str) {
    String results;
    String tmp_str;

    /** some characters(i.e. "@","%") are replaced to under score char. */
    results = old_str.replace('@','_');
    results = results.replace('%','_');

    /* period -> under score */
    results = results.replace('.','_');

    /** if the string starts with digits, then add the string "val_".
     */
    tmp_str = results.replace('0','@');
    tmp_str = tmp_str.replace('1','@');
    tmp_str = tmp_str.replace('2','@');
    tmp_str = tmp_str.replace('3','@');
    tmp_str = tmp_str.replace('4','@');
    tmp_str = tmp_str.replace('5','@');
    tmp_str = tmp_str.replace('6','@');
    tmp_str = tmp_str.replace('7','@');
    tmp_str = tmp_str.replace('8','@');
    tmp_str = tmp_str.replace('9','@');

    if (tmp_str.startsWith("@")) {
      results = "val_" + results;
    } else {
      /* do nothing */
      // results = "" + results;
    }

    return results;
  }
}
