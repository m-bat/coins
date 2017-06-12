/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.mdf;

import coins.driver.Driver;
import coins.PassException;
import coins.driver.CoinsOptions;
import coins.driver.Trace;
import coins.driver.Suffix;
import coins.IoRoot;
import coins.driver.TemporaryFileManager;
import coins.HirRoot;
import coins.SymRoot;
import coins.FlowRoot;
import coins.driver.CompileSpecification;
import coins.driver.PassStopException;

import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

/**
 * CoCo - Coarse Grain Parallelization Compiler by using COINS <br>
 * CoCo is a compiler driver which generates coarse grain parallelized code.
 * It uses an extra compiler for code generation. The extra compiler should
 * be able to handle the OpenMP pragma.
 **/
public class CoCo extends Driver{
  /** The name of this compiler driver **/
  protected String myName="CoCo";
  /** The default name of the extra compile **/
  protected static final String DEFAULT_OMPC_NAME="omcc";
  /** The name of the options for this compiler driver **/
  protected static final String OMPC_NAME_OPTION = "ompc";

  /**
   * Compile the source code.
   * @param sourceFile The source file
   * @param suffix The suffix rules
   * @param in The input stream for this compiler driver
   * @param out The ouput stream of this compiler driver
   * @param io The IoRoot of this compiler driver
   * @throws IOException Any IO exception
   * @throws PassException Any exception during compile
   **/
  public void compile(File sourceFile,
		      Suffix suffix,
		      InputStream in,
		      OutputStream out,
		      IoRoot io)
    throws IOException, PassException {

    CompileSpecification spec = io.getCompileSpecification();
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    Trace trace = spec.getTrace();

    SymRoot symRoot  = new SymRoot(io);
    HirRoot hirRoot  = new HirRoot(symRoot);
    symRoot.attachHirRoot(hirRoot);
    symRoot.initiate();

    coins.snapshot.SnapShot snap=null;
    if(spec.getCoinsOptions().isSet("snapshot"))
      snap=new coins.snapshot.SnapShot(sourceFile,"coins snapsnot");

    /* pass 1 -- Source to HIR */
    makeHirFromSource(sourceFile, hirRoot, suffix, in, io);

    if(spec.getCoinsOptions().isSet("snapshot"))
      snap.shot(hirRoot,"Generated HIR");

    /* pass 4 -- HIR Optimizations & Parallelizations after Flow Analysis */
    /* Macro Data Flow */

    if(!coinsOptions.isSet("onlyhir2c")){
      MdfDriver mdfDriver=null;
      if(spec.getCoinsOptions().isSet("snapshot"))
        mdfDriver=new MdfDriver(hirRoot,io,spec,snap);
      else
        mdfDriver=new MdfDriver(hirRoot,io,spec);

      mdfDriver.invoke();
    }
    if(spec.getCoinsOptions().isSet("snapshot"))
      snap.shot(hirRoot,"Optimized HIR");

    if(spec.getCoinsOptions().isSet("snapshot"))
      snap.generateXml();

    TemporaryFileManager tmpMngr=new TemporaryFileManager();
//    List schedulerName=mdfDriver.printScheduler(tmpMngr);

    File dest=tmpMngr.createTemporaryFile(".c");
    //System.out.println(dest.getName());
    FileOutputStream fout=new FileOutputStream(dest);
    callHirBaseToC(hirRoot,symRoot,io,fout);

    List options=new LinkedList();

    String commandName=DEFAULT_OMPC_NAME;
    if(coinsOptions.isSet(OMPC_NAME_OPTION)){
      commandName=coinsOptions.getArg(OMPC_NAME_OPTION);

      // Option for using FORTE (SUN CC)
      if(commandName.equals("cc"))
        options.add("-xopenmp");
    }

    if(spec.isSet("-S")){
      options.add("-S");
      if(!spec.isSet("-o")){
        String srcName=sourceFile.getAbsolutePath();
        String dstName=srcName.substring(0,srcName.lastIndexOf(".c"))+".s";
        options.add("-o");
        options.add(dstName);
      }
    }
    else if(spec.isSet("-c")){
      options.add("-c");
      if(!spec.isSet("-o")){
        String srcName=sourceFile.getAbsolutePath();
        String dstName=srcName.substring(0,srcName.lastIndexOf(".c"))+".o";
//        System.out.println(dstName);
        options.add("-o");
        options.add(dstName+" ");
      }
    }
    else{
      if(spec.isSet("-l")){
        List list=(List)spec.getArg("-l");
        for(Iterator ite=list.iterator();ite.hasNext();){
          options.add("-l"+(String)ite.next());
        }
      }

      if(spec.isSet("-L")){
        List list=(List)spec.getArg("-L");
        for(Iterator ite=list.iterator();ite.hasNext();){
          options.add("-L"+(String)ite.next());
        }
      }
    }

    if(spec.isSet("-o")){
      options.add("-o");
      options.add(spec.getArg("-o"));
    }

    if(spec.isSet("-D")){
      List list=(List)spec.getArg("-D");
      for(Iterator ite=list.iterator();ite.hasNext();){
        options.add("-D"+(String)ite.next());
      }
    }

    if(spec.isSet("-U")){
      List list=(List)spec.getArg("-U");
      for(Iterator ite=list.iterator();ite.hasNext();){
        options.add("-U"+(String)ite.next());
      }
    }

    if(spec.isSet("-W")){
      List list=(List)spec.getArg("-W");
      for(Iterator ite=list.iterator();ite.hasNext();){
        options.add("-W"+(String)ite.next());
      }
    }

    if(spec.isSet("-I")){
      List list=(List)spec.getArg("-I");
      for(Iterator ite=list.iterator();ite.hasNext();){
        options.add("-I"+(String)ite.next());
      }
    }

    if(spec.isSet("-O")){
      List list=(List)spec.getArg("-O");
      for(Iterator ite=list.iterator();ite.hasNext();){
        options.add("-O"+(String)ite.next());
      }
    }
    options.add(dest.getPath());
    

    if(runProgram(commandName,options,null,io.msgOut,io)!=0){
      throw new PassException(sourceFile,
			      "ompc", "Error(s) in ompc.");
    }

    tmpMngr.cleanupTemporaryFiles();

    throw new PassStopException("CoCo","exit");
  }

  /**
   * Main 
   * @param args The argument for this compiler driver
   **/
  public static void main(String args[]){
    new CoCo().go(args);
  }
}
