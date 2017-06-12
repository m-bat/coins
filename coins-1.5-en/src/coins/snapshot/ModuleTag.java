/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

import coins.PassException;
import coins.backend.util.BiList;
import coins.backend.util.BiLink;
import coins.backend.Module;
import coins.backend.Function;
import coins.mdf.MdfModule;
import coins.mdf.MacroFlowGraph;
import coins.ir.IrList;
import coins.ir.hir.Program;
import coins.ir.hir.SubpDefinition;
import coins.HirRoot;

import java.util.Iterator;

/**
 * This class represents the tag `module'
 **/
class ModuleTag{
  /** The list of the functions **/
  private BiList functions;
  /** The name of the current module **/
  private final String moduleName;
  /** The path to the source file **/
  private final String filepath;

  /**
   * Constructor for LIR
   * @param m The current module
   * @param fName The name of the current module
   * @param fPath The path to the source file
   **/
  ModuleTag(Module m,String fName,String fPath){
    moduleName=fName;
    filepath=fPath;
    functions=new BiList();

    for(BiLink p=m.elements.first();!p.atEnd();p=p.next()){
      if(p.elem() instanceof Function){
        FunctionTag fTag=new FunctionTag((Function)p.elem());
        functions.add(fTag);
      }
    }
  }

  /**
   * Constructor for HIR
   * @param hirRoot The current HirRoot
   * @param fName The name of the current module
   * @param fPath The path to the source file
   **/
  ModuleTag(HirRoot hirRoot,String fName,String fPath){
    moduleName=fName;
    filepath=fPath;
    functions=new BiList();

    // Get the list of the current sub programs.
    IrList subpDefList=((Program)hirRoot.programRoot).getSubpDefinitionList();

    // Operate for each sub program.
    for(Iterator subpDefIterator=subpDefList.iterator();
        subpDefIterator.hasNext();){
      // Get the current sub program.
      SubpDefinition subpDef=(SubpDefinition)subpDefIterator.next();
      FunctionTag fTag=new FunctionTag(hirRoot,subpDef);
      functions.add(fTag);
    }
  }

  /**
   * Constructor for MDF
   * @param m The current MdfModule
   * @param fName The name of the current module
   * @param fPath The path to the source file
   * @throws PassException Any exceptions in it
   **/
  ModuleTag(MdfModule m,String fName,String fPath) throws PassException{
    moduleName=fName;
    filepath=fPath;
    functions=new BiList();

    for(Iterator ite=m.flowGraph.iterator();ite.hasNext();){
      MacroFlowGraph mfg=(MacroFlowGraph)ite.next();
      FunctionTag fTag=new FunctionTag(mfg);
      functions.add(fTag);
    }
  }

  /**
   * Get the name of the current module.
   * @return The name of the current module
   **/
  private String moduleName(){
    return("\""+moduleName+"\"");
  }

  /**
   * Get the path to the source program which include the current module
   * @return The path to the source program
   **/
  private String filepath(){
    return("\""+filepath+"\"");
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
    str+=ws+"<"+TagName.MODULE+" "+TagName.NAME+"="+moduleName();
    str+=" "+TagName.SRC+"="+filepath()+">\n";

    for(BiLink p=functions.first();!p.atEnd();p=p.next()){
      FunctionTag fTag=(FunctionTag)p.elem();
      str+=fTag.toString(space+1);
    }

    str+=ws+"</"+TagName.MODULE+">\n";
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
