/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.HirRoot;
import coins.ir.IrList;
import coins.ir.hir.SubpDefinition;
import coins.PassException;
import coins.ir.hir.Program;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * This class holds the macro flow graphs in the current compile unit.
 **/
public class MdfModule{
  /** The current compile unit **/
  private final IrList subpDefList;
  /** The environment of the MDF module **/
  private MdfEnvironment env;
  /** The current HirRoot **/
  private HirRoot hirRoot;
  /** The macro flow graphs in the current compile unit **/
  public final List flowGraph;

  /**
   * Constructor
   * @param e The environment of the MDF module
   * @param hRoot The current HirRoot
   **/
  MdfModule(MdfEnvironment e,HirRoot hRoot){
    env=e;
    hirRoot=hRoot;
    // Get the list of the current sub programs.
    subpDefList=((Program)hirRoot.programRoot).getSubpDefinitionList();

    flowGraph=new LinkedList();
  }

  /**
   * Generate macro flow graphs in the current compile unit.
   * @throws PassException Any exceptions in it
   **/
  void generateMfg() throws PassException{
    // Operate for each sub program.
    for(Iterator subpDefIterator=subpDefList.iterator();
        subpDefIterator.hasNext();){
      // Get the current sub program.
      SubpDefinition subpDef=(SubpDefinition)subpDefIterator.next();

      if((env.opt.isSet("mdf-only-main") && 
          subpDef.getSubpSym().getName().equals("main")) ||
         !env.opt.isSet("mdf-only-main")){

        env.println("MDF : doing "+subpDef.getSubpSym().getName(),env.OptThr);
        hirRoot.symRoot.symTableCurrent=subpDef.getSymTable();

        // pre-operation
        //   * Translate the hierarchical declaration of variables into the 
        //     flat one
        //   * Make an unified exit block from the current CFG
        //   * Insert labels before and after function call
        //   * Insert labels before and after loop structure
        ReconstructHir reconst=new ReconstructHir(env,hirRoot,subpDef);
        env.println("MDF : reconstruct done for "+
                    subpDef.getSubpSym().getName(),env.OptThr);
        
        // Divide the current sub program into macro task and construct the
        // macro flow graph.
        MacroFlowGraph mfg=new MacroFlowGraph(env,hirRoot,subpDef);
        flowGraph.add(mfg);
        env.println("MDF : generated for "+
                    subpDef.getSubpSym().getName(),env.OptThr);
        
//        subpDef.print(0);
        // Change the structure of the current program into `switch-case'
        // style.
        ChangeStructure changeStructure=new ChangeStructure(env,mfg);
        env.println("MDF : changed for "+
                    subpDef.getSubpSym().getName(),env.OptThr);

//        if(check()){
//          throw new PassException("MDF","NOT TREE in MdfModule");
//        }
      }
    }
  }

  /**
   * Change the program structure into switch-case.
   * @throws PassException Any exceptions in it
   **/
  void changeStructure() throws PassException{
    for(Iterator ite=flowGraph.iterator();ite.hasNext();){
      MacroFlowGraph mfg=(MacroFlowGraph)ite.next();

      // Change the structure of the current program into `switch-case'
      // style.
      ChangeStructure changeStructure=new ChangeStructure(env,mfg);
    }
  }

  /**
   * Debug method
   * @return True if any trouble
   **/
  boolean check(){
    // Operate for each sub program.
    for(Iterator subpDefIterator=subpDefList.iterator();
        subpDefIterator.hasNext();){
      // Get the current sub program.
      SubpDefinition subpDef=(SubpDefinition)subpDefIterator.next();

      subpDef.setIndexNumberToAllNodes(0);
      if(!subpDef.isTree()){
        env.output.println("NOT TREE");
        return(true);
      }

//      subpDef.print(0);
    }
    return(false);
  }
}
