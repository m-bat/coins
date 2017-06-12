/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.PassException;
import coins.HirRoot;
import coins.SymRoot;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.Stmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.HirIterator;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.ExpStmt;
import coins.sym.SymTable;
import coins.sym.Label;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.SubpNode;
import coins.ir.hir.VarNode;
import coins.sym.Sym;
import coins.sym.SymIterator;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.Subp;

import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Reconstruct the current HIR before generating the macro flow graph.
 **/
class ReconstructHir{
  /** The reserved name for system call `exit()' **/
  private final String exitName="exit";
  /** The environment of the MDF module **/
  private MdfEnvironment env;
  /** The current HirRoot **/
  private HirRoot hirRoot;
  /** The current subprogram **/
  private SubpDefinition subpDef;
  /** The current symbol table **/
  private SymTable symTab;
  /** The SymRoot **/
  private SymRoot symRoot;
  /** The HIR factory **/
  private HIR hirFact;
  /** The Sym factory **/
  private Sym symFact;

  /**
   * Constructor
   * @param e The environment of the MDF module
   * @param hRoot The current HirRoot
   * @param subpDefinition The current subprogram
   * @throws PassException Any exceptions in it
   **/
  ReconstructHir(MdfEnvironment e,HirRoot hRoot,
                 SubpDefinition subpDefinition) throws PassException{
    env=e;
    hirRoot=hRoot;
    symRoot=hirRoot.symRoot;
    subpDef=subpDefinition;
    symTab=subpDef.getSymTable();
    symFact=symRoot.sym;
    hirFact=hirRoot.hir;


    makeFlatStructure();
    canonicalForm();
    //moveExit();
    configBlk();
  }

  /**
   * Translate the current HIR into the canonical form.
   * The canonical form means that the control flow graph of the current
   * subprogram has only one exit block.
   * @throws PassException Any exceptions in it
   **/
  private void canonicalForm() throws PassException{
    BlockStmt blkStmt=null;
    Stmt root=(Stmt)subpDef.getHirBody();
    for(HirIterator ite=root.hirIterator(root);
        ite.hasNextStmt();) //##72
    {
      Stmt node=(Stmt)ite.getNextStmt();
      if(blkStmt==null && (node instanceof BlockStmt)){
        blkStmt=(BlockStmt)node;
        break;
      }
    }

    Label lastLabel=symTab.generateLabel();

    // Return status of the current sub program.
    Type subpType=subpDef.getSubpSym().getReturnValueType();
    Var returnStatus=null;
    if(subpType.getTypeKind()!=Type.KIND_VOID){
      returnStatus=symFact.defineVar("_mdf_return_status".intern(),
                                     subpType);
      returnStatus.setVisibility(Sym.SYM_PRIVATE);
    }

    for(HirIterator ite=root.hirIterator(root);
        ite.hasNextStmt();) //##72
    {
      Stmt node=(Stmt)ite.getNextStmt();
      if(node instanceof ReturnStmt){
        node.addNextStmt((Stmt)hirFact.jumpStmt(lastLabel));
      }
    }

    for(HirIterator ite=root.hirIterator(root);
        ite.hasNextStmt();) //##72
    {
      Stmt node=(Stmt)ite.getNextStmt();
      if(node instanceof ReturnStmt){
        if(returnStatus!=null){
          Exp statusExp=(Exp)hirFact.varNode(returnStatus);
          Stmt setStatus=hirFact.assignStmt(statusExp,
                                            (Exp)node.getChild(1));
          node.replaceThisStmtWith(setStatus);
        }
        else{
          node.deleteThisStmt();
        }
      }
    }

    // append return statement
    Stmt labeledStmt=hirFact.labeledStmt(lastLabel,null);
    blkStmt.addLastStmt(labeledStmt);

    if(returnStatus==null)
      blkStmt.addLastStmt(hirFact.returnStmt());
    else{
      Exp statusExp=(Exp)hirFact.varNode(returnStatus);
      blkStmt.addLastStmt(hirFact.returnStmt(statusExp));
    }
  }

/**
  private void moveExit() throws PassException{
    BlockStmt blkStmt=null;
    Hashtable map=new Hashtable();
    LinkedList list=new LinkedList();

    Stmt root=(Stmt)subpDef.getHirBody();
    for(HirIterator ite=root.hirIterator(root);ite.hasNext();){
      Stmt node=(Stmt)ite.getNextStmt();

      if(blkStmt==null && (node instanceof BlockStmt)){
        blkStmt=(BlockStmt)node;
      }
      else if(node instanceof ExpStmt){
        HIR mayCall=((ExpStmt)node).getExp();
        if(mayCall!=null && mayCall.getOperator()==HIR.OP_CALL){
          //env.output.println(node.toStringWithChildren());
          Exp exp=((ExpStmt)node).getExp();
          if(exp instanceof FunctionExp){
            //env.output.println(exp.toStringWithChildren());
            SubpNode callee=((FunctionExp)exp).getFunctionNode();
            if(callee!=null){
              Sym s=callee.getSymNodeSym();
              if(exitName.equals(s.getName())){
                // The call to exit()
                if(node.getParent()!=blkStmt){
                  Stmt cloneNode=null;
                  try{
                    cloneNode=(Stmt)node.hirClone();
                  }
                  catch(Exception ex){
                    System.err.println("Fail cloning");
                    throw new PassException("mdf","fail node cloning");
                  }
                  Label newLab=symTab.generateLabel();
                  list.add(cloneNode);
                  map.put(cloneNode,newLab);

                  // replace exit call to jump
                  Stmt jumpStmt=node.jumpStmt(newLab);
                  node.replaceThisStmtWith(jumpStmt);
                }
              }
            }
          }
        }
      }
    }

    for(Iterator ite=list.iterator();ite.hasNext();){
      Stmt stmt=(Stmt)ite.next();
      Label label=(Label)map.get(stmt);
      Stmt labeledStmt=hirFact.labeledStmt(label,null);
      blkStmt.addLastStmt(labeledStmt);
      blkStmt.addLastStmt(stmt);
    }
  }
**/

  /**
   * Reconstruct HIR before dividing the input program into macro tasks.
   *   1) insert labels before and after function call
   *   2) insert labels before and after loop structure
   **/
  private void configBlk(){
    Stmt root=(Stmt)subpDef.getHirBody();
    for(HirIterator ite=root.hirIterator(root);
        ite.hasNextStmt();) //##72
    {
      Stmt node=(Stmt)ite.getNextStmt();

      if(node instanceof LoopStmt){
        //env.output.println("+++++++++++++++++++++++++++++++++ "+node);
        // Insert labels before and after loop structure.
        attachLabel(node);
      }
      else if(node instanceof ExpStmt){
        //env.output.println(node.toStringWithChildren());
        HIR mayCall=((ExpStmt)node).getExp();
        if(mayCall!=null && mayCall.getOperator()==HIR.OP_CALL){
          // Insert labels before and after function call.
          attachLabel(node);
        }
      }
    }
  }

  /**
   * Insert labels before and after the specified statement.
   * Each labels are new generated.
   * @param stmt The current statement
   **/
  private void attachLabel(Stmt stmt){
    //env.output.println("******************* ATTACH LABEL");
    //env.output.println(stmt.toStringWithChildren());

    // Insert before the statement.
    Stmt upper=stmt.getPreviousStmt();
    if(upper!=null && !(upper instanceof LabeledStmt)){
//      env.output.println("+++++++++++++++++ upper");
//      env.output.println(upper.toStringWithChildren());
      Label newLab=symTab.generateLabel();
//      env.output.println("--------------- "+newLab+" "+stmt);

      Stmt labeledStmt=hirFact.labeledStmt(newLab,null);
      stmt.insertPreviousStmt(labeledStmt);
    }

    // Insert after the statement.
    Stmt below=stmt.getNextStmt();
    if(below!=null && //!(stmt instanceof ReturnStmt) &&
       !(below instanceof LabeledStmt)){
//      env.output.println("+++++++++++++++++ below");
//      env.output.println(below.toStringWithChildren());

      Label newLab=symTab.generateLabel();
//      env.output.println("--------------- "+newLab);

      Stmt labeledStmt=hirFact.labeledStmt(newLab,null);
      //stmt.addNextStmt(labeledStmt,stmt.getBlockStmt());
      stmt.addNextStmt(labeledStmt);
    }
  }

  /**
   * Translate the current HIR into the flat scope structure.
   * The flat scope structure means that the subprogram has the only one
   * scope.
   **/
  private void makeFlatStructure(){
//    env.output.println(symTab);
//    symRoot.symTableUnique.printSymTable();
    int count=0;
    Stmt stmt=(Stmt)subpDef.getHirBody();

    for(HirIterator ite=stmt.hirIterator(stmt);ite.hasNext();){
      HIR hir=(HIR)ite.next();
//      env.output.println(hir);
      if(hir instanceof BlockStmt){
//        env.output.println("find aaaaaaaaaaaaaaaaaaaaaaaaa");
        SymTable localSymTab=((BlockStmt)hir).getSymTable();
        if(localSymTab!=null && symTab!=localSymTab){
          count++;
//          env.output.println("\t"+localSymTab);
//          localSymTab.printSymTable();
          for(SymIterator site=localSymTab.getSymIterator();site.hasNext();){
            Sym s=(Sym)site.next();
//            env.output.print("\t  "+s+" --> ");
            String newName="_mdf_"+s.getName()+"_"+count;
//            env.output.println(newName);
            Sym newSym=symTab.defineUnique(newName,s.getSymKind(),null);
            newSym.setSymType(s.getSymType());

            replaceSymbol((BlockStmt)hir,s,newSym);
          }
        }
      }
    }

//    symTab.printSymTableDetail();
  }

  /**
   * Replacing the local symbols into global symbol and renaming.
   * @param blkStmt The current block statement in HIR
   * @param orgSym The original symbol
   * @param newSym The renamed symbol
   **/
  private void replaceSymbol(BlockStmt blkStmt,Sym orgSym,Sym newSym){
    for(HirIterator ite=blkStmt.hirIterator(blkStmt);ite.hasNext();){
      HIR hir=(HIR)ite.next();

      if(hir instanceof VarNode){
//        env.output.println(hir.toStringWithChildren());
        Sym s=((VarNode)hir).getSymNodeSym();
        if(s==orgSym){
          ((VarNode)hir).setSymNodeSym(newSym);
        }
//        env.output.println("\t"+hir.toStringWithChildren());
      }
    }
  }
}
