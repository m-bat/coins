/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

import coins.backend.cfg.BasicBlk;
import coins.backend.util.BiList;
import coins.backend.util.BiLink;
import coins.aflow.BBlock;
import coins.mdf.MacroTask;

/**
 * This class represents the type `labelType'.
 **/
class LabelType extends AbstType{
  /**
   * Constructor
   * @param dName The attribute `displayName'
   * @param contents The list of the elements
   **/
  LabelType(String dName,BiList contents){
    super(dName);

    Util util=new Util();
    for(BiLink p=contents.first();!p.atEnd();p=p.next()){
      Object obj=p.elem();
      if(obj instanceof BasicBlk){
        BasicBlk blk=(BasicBlk)obj;
        elem.add(util.prepare(blk.label().name()));
      }
      /*  //##97
      else if(obj instanceof BBlock){
    	  BBlock blk=(BBlock)obj;
        elem.add(util.prepare(blk.getLabel().getName()));
      }       
       */ //##97
      //##97 BEGIN
      else if(obj instanceof coins.flow.BBlock){
    	  coins.flow.BBlock blk=(coins.flow.BBlock)obj;
          elem.add(util.prepare(blk.getLabel().getName()));
      }      
      else if(obj instanceof coins.aflow.BBlock){
    	  coins.aflow.BBlock blk=(coins.aflow.BBlock)obj;
        elem.add(util.prepare(blk.getLabel().getName()));
      }
      //##97 END
      else if(obj instanceof MacroTask){
        MacroTask blk=(MacroTask)obj;
        elem.add(util.prepare(blk.label.getName()));
      }
    }
  }
}
