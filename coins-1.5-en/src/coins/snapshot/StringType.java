/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

import coins.backend.util.BiList;
import coins.backend.util.BiLink;

/**
 * This class represents the type `stringType'.
 **/
class StringType extends AbstType{
  /**
   * Constructor
   * @param dName The attribute `displayName'
   * @param contents The list of the element of the current stringType
   **/
  StringType(String dName,BiList contents){
    super(dName);

    Util util=new Util();
    for(BiLink p=contents.first();!p.atEnd();p=p.next()){
      elem.add(util.prepare((String)p.elem()));
    }
  }
}
