/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.snapshot;

import coins.PassException;
import coins.HirRoot;
import coins.backend.Module;
import coins.mdf.MdfModule;
import coins.backend.util.BiList;
import coins.backend.util.BiLink;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;

/**
 * Generate the XML file related to the intermediate representation in
 * COINS Compiler Infrastructure.
 * The tags in the XML file specified the XML shema `coins.xsd'.
 * See that XSD file for more detail.
 **/
public class SnapShot{
  /** The temporary file name that this module generate **/
  public static final String TMP_FILE_NAME="coins.xml";
  /** The temporary name of the current status **/
  public static final String TMP_NAME="coins snapshot";
  /** The output stream **/
  private PrintWriter output;
  /** The source program file **/
  private final File source;
  /** The list of the tag `module' **/
  private BiList modules;
  /** The name of the current status **/
  private String name;

  /**
   * Constructor
   * @param sourceFile Ths source file
   * @param snapName The name of the current status
   **/
  public SnapShot(File sourceFile,String snapName){
    source=sourceFile;

    String sourceName=source.getAbsolutePath();
    String outputFileName;
    outputFileName=sourceName.substring(0,sourceName.lastIndexOf('.'))+".xml";

    try{
      output=new PrintWriter(new FileOutputStream(outputFileName),true);
    }
    catch(Exception e){
      output=new PrintWriter(System.out,true);
    }
    modules=new BiList();
    name=snapName.intern();
  }

  /**
   * Snapshot for LIR.
   * @param m The target module
   * @param mName The name of the target module
   **/
  public void shot(Module m,String mName){
    ModuleTag mTag=new ModuleTag(m,mName,source.getAbsolutePath());
    modules.add(mTag);
  }

  /**
   * Snapshot for HIR.
   * @param hirRoot The target module
   * @param mName The name of the target module
   **/
  public void shot(HirRoot hirRoot,String mName){
    ModuleTag mTag=new ModuleTag(hirRoot,mName,source.getAbsolutePath());
    modules.add(mTag);
  }

  /**
   * Snapshot for MDF.
   * @param m The target module
   * @param mName The name of the target module
   * @throws PassException Any exceptions in it
   **/
  public void shot(MdfModule m,String mName) throws PassException{
    ModuleTag mTag=new ModuleTag(m,mName,source.getAbsolutePath());
    modules.add(mTag);
  }

  /**
   * Generate the XML file.
   **/
  public void generateXml(){
    output.println(toString());
  }

  /**
   * Generate XML representation in string.
   * @return The XML representation
   **/
  public String toString(){
    String str="";
    str+="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
    str+="<"+TagName.PROGRAM+" "+TagName.NAME+"=\""+name+"\"\n"+
         "         xmlns=\"coins\"\n"+
         "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"+
         "         xsi:schemaLocation=\"coins ./coins.xsd\">\n";
    for(BiLink p=modules.first();!p.atEnd();p=p.next()){
      ModuleTag mTag=(ModuleTag)p.elem();
      str+=mTag.toString(1);
    }
    str+="</"+TagName.PROGRAM+">\n";

    return(str);
  }
}
