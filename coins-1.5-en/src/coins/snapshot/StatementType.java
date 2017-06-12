/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

import coins.backend.Op;
import coins.backend.util.BiList;
import coins.backend.util.BiLink;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirIconst;
import coins.lir2c.Convert;
//##94 import coins.aflow.BBlockSubtreeIterator;
//##94 import coins.aflow.BBlock;
import coins.flow.BBlockSubtreeIterator; //##94
import coins.flow.BBlock;                //##94
import coins.ir.hir.HIR;
import coins.ir.hir.Stmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.IfStmt;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.LabeledStmt;

/**
 * This class represents the type `statementType'.
 **/
class StatementType{
  /** For HIR **/
  static final int HIR=0;
  /** For LIR **/
  static final int LIR=1;
  /** For HIR2C **/
  static final int HIR2C=2;
  /** For LIR2C **/
  static final int LIR2C=3;

  /**
   * Static method to generate the HIR statements.
   * @param subtreeIte The iterator to iterate the instructions in the current
   *                   basic block
   * @return The tag `statementType' for HIR
   **/
  static StatementType hir(BBlockSubtreeIterator subtreeIte){
    return(new StatementType(HIR,subtreeIte));
  }

//##94 BEGIN
  static StatementType hir(coins.aflow.BBlockSubtreeIterator subtreeIte){
    return(new StatementType(HIR,subtreeIte));
  }

//##94 END

  /**
   * Static method to generate the LIR statements.
   * @param instr The list of the instructions in the current basic block
   * @return The tag `statementType' for LIR
   **/
  static StatementType lir(BiList instr){
    return(new StatementType(LIR,instr));
  }

  /**
   * Static method to generate the LIR2C statements.
   * @param instr The list of the instructions in the current basic block
   * @return The tag `statementType' for LIR2C
   **/
  static StatementType lir2c(BiList instr){
    return(new StatementType(LIR2C,instr));
  }

  /**
   * Static method to generate the MDF statements.
   * @param blks The basic blocks in the current macro task
   * @return The tag `statementType' for MDF
   **/
  static StatementType mdf(BBlock[] blks){
    return(new StatementType(HIR,blks));
  }

  /** The list of the tag `exp' in the current statement type **/
  private BiList exp;

  /**
   * Constructor for LIR and LIR2C
   * @param type The kind of the statement type
   * @param instr The list of the instructions in the current basic block
   **/
  private StatementType(int type,BiList instr){
    exp=new BiList();
    int currentLine=-1;

    for(BiLink p=instr.first();!p.atEnd();p=p.next()){
      LirNode node=(LirNode)p.elem();
      if(node.opCode==Op.LINE){
        currentLine=(int)((LirIconst)node.kid(0)).value;
        continue;
      }

      switch(type){
        case LIR:{
          exp.add(new ExpTag(node.toString(),currentLine));
          break;
        }
        case LIR2C:{
          Convert conv=new Convert();
          String str=conv.invoke(node);
          if(!str.equals(""))
            exp.add(new ExpTag(str,currentLine));
          break;
        }
      }
      currentLine=-1;
    }
  }

  /**
   * Constructor for HIR
   * @param type The kind of the statement type
   * @param ite The iterator to iterate the instructions in the current basic
   *            block
   **/
  private StatementType(int type,BBlockSubtreeIterator ite){
    exp=getHir(type,ite,new BiList());
  }

//##94 BEGIN
  private StatementType(int type,coins.aflow.BBlockSubtreeIterator ite){
    exp=getHir(type,ite,new BiList());
  }

//##94 END
  /**
   * Constructor for MDF
   * @param type The kind of the statement type
   * @param blks The basic blocks in the current macro task
   **/
  private StatementType(int type,BBlock[] blks){
    exp=new BiList();
    for(int i=0;i<blks.length;i++){
      exp=getHir(type,blks[i].bblockSubtreeIterator(),exp);
    }
  }

  /**
   * Get the list of HIRs.
   * @param type The kind of the statement type
   * @param ite The iterator to iterate the instructions in the target basic
   *            block
   * @param exp The list of the elements of the current statement type
   * @return The list of the elements of the current statement type
   **/
  private BiList getHir(int type,BBlockSubtreeIterator ite,BiList exp){
    for(;ite.hasNext();){
      HIR node=(HIR)ite.next();
      int currentLine=-1;
      boolean shortTerm=false;

      if(node instanceof Stmt){
        if(node instanceof BlockStmt ||
           node instanceof LabeledStmt ||
           node instanceof IfStmt ||
           node instanceof SwitchStmt ||
           node instanceof LoopStmt) shortTerm=true;

        currentLine=((Stmt)node).getLineNumber();
      }
      if(currentLine==0) currentLine=-1;

      switch(type){
        case HIR:{
          if(shortTerm)
            exp.add(new ExpTag(node.toString(),currentLine));
          else
            exp.add(new ExpTag(node.toStringWithChildren(),currentLine));
          break;
        }
      }
    }
    return(exp);
  }

//##94 BEGIN
  private BiList getHir(int type,coins.aflow.BBlockSubtreeIterator ite,BiList exp){
    for(;ite.hasNext();){
      HIR node=(HIR)ite.next();
      int currentLine=-1;
      boolean shortTerm=false;

      if(node instanceof Stmt){
        if(node instanceof BlockStmt ||
           node instanceof LabeledStmt ||
           node instanceof IfStmt ||
           node instanceof SwitchStmt ||
           node instanceof LoopStmt) shortTerm=true;

        currentLine=((Stmt)node).getLineNumber();
      }
      if(currentLine==0) currentLine=-1;

      switch(type){
        case HIR:{
          if(shortTerm)
            exp.add(new ExpTag(node.toString(),currentLine));
          else
            exp.add(new ExpTag(node.toStringWithChildren(),currentLine));
          break;
        }
      }
    }
    return(exp);
  }

//##94 END
  /**
   * Generate the XML representation in string.
   * @param space The number of the white spaces
   * @return The XML representation
   **/
  public String toString(int space){
    String str="";

    for(BiLink p=exp.first();!p.atEnd();p=p.next()){
      ExpTag eTag=(ExpTag)p.elem();
      str+=eTag.toString(space)+"\n";
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
