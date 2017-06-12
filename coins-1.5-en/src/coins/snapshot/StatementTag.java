/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

import coins.backend.util.BiList;
//##94 import coins.aflow.BBlockSubtreeIterator;
//##94 import coins.aflow.BBlock;
import coins.flow.BBlockSubtreeIterator;  //##94
import coins.flow.BBlock;                 //##94

/**
 * This class represents the tag `statement'
 **/
class StatementTag{
  /** The tag `hir' **/
  private StatementType hir;
  /** The tag `lir' **/
  private StatementType lir;
  /** The tag `hir2c **/
  private StatementType hir2c;
  /** The tag `lir2c **/
  private StatementType lir2c;

  /**
   * Constructor for LIR
   * @param instr The list of the instruction in the current basic block
   **/
  StatementTag(BiList instr){
    hir=null;
    hir2c=null;

    lir=StatementType.lir(instr);
    lir2c=StatementType.lir2c(instr);
  }

  /**
   * Constructor for HIR
   * @param ite The iterator for iterating the statements in the current basic
   *            block
   **/
  StatementTag(BBlockSubtreeIterator ite){
    hir=StatementType.hir(ite);
    hir2c=null;

    lir=null;
    lir2c=null;
  }

//##94 BEGIN
  StatementTag(coins.aflow.BBlockSubtreeIterator ite){
    hir=StatementType.hir(ite);
    hir2c=null;

    lir=null;
    lir2c=null;
  }
//##94 END

  /**
   * Constructor for MDF
   * @param blks The basic blocks which are inside of the current macro task
   **/
  //##94 StatementTag(BBlock[] blks)
  StatementTag(coins.aflow.BBlock[] blks) //##94
  {
    //##94 hir=StatementType.mdf(blks);
    hir2c=null;

    lir=null;
    lir2c=null;
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

    if(hir!=null && !hir.toString().equals("")){
      str+=ws+"<"+TagName.HIR+">\n";
      str+=hir.toString(space+1);
      str+=ws+"</"+TagName.HIR+">\n";
    }
    if(lir!=null && !lir.toString().equals("")){
      str+=ws+"<"+TagName.LIR+">\n";
      str+=lir.toString(space+1);
      str+=ws+"</"+TagName.LIR+">\n";
    }
    if(hir2c!=null && !hir2c.toString().equals("")){
      str+=ws+"<"+TagName.HIR2C+">\n";
      str+=hir2c.toString(space+1);
      str+=ws+"</"+TagName.HIR2C+">\n";
    }
    if(lir2c!=null && !lir2c.toString().equals("")){
      str+=ws+"<"+TagName.LIR2C+">\n";
      str+=lir2c.toString(space+1);
      str+=ws+"</"+TagName.LIR2C+">\n";
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
