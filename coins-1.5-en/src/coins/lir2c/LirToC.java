/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lir2c;

import coins.backend.Module;
import coins.backend.Function;
import coins.backend.sym.SymStatic;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.sym.Symbol;
import coins.backend.Data;
import coins.backend.lir.LirNode;
import coins.backend.cfg.FlowGraph;
import coins.backend.cfg.BasicBlk;
import coins.backend.Op;
import coins.backend.sym.SymTab;
import coins.backend.sym.Label;

import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.io.IOException;

/**
 * LirToC: convert LIR into C code.
 * It is called from coins.driver.Driver.
 **/
public class LirToC{
  /** print writer to output C code **/
  public PrintWriter stdout;
  /** Output Stream Writer **/
  public OutputStreamWriter out;
  /** Module to convert **/
  public Module compileUnit;

  /**
   * Constructor (with args)
   * @param cU The module to convert.
   * @param dstName The filename for the output(C code).
   **/
  public LirToC(Module cU, String dstName)  throws IOException{
    compileUnit = cU;
    stdout = new PrintWriter(new FileOutputStream(dstName), true);
  }

  /**
   *  Constructor (with args)
   *  @param cU The module to convert.
   *  @param fout The OutputStreamWriter for the output(C code).
   **/
  public LirToC(Module cU,OutputStreamWriter fout)  throws IOException{
    /* auto flush of print writer */
    boolean autoFlush = true;

    compileUnit = cU;    
    stdout = new PrintWriter(fout,autoFlush);
  }

  /**
   * the main routine of convertion.
   **/
  public void invoke() {
    SymTab glname = compileUnit.globalSymtab;
    Symbol sym;
    SymStatic symsta;
    BiList elemList = compileUnit.elements;
    BiLink p, q, r;
    Function func;
    Convert conv = new Convert();
    HashMap text_ldef = new HashMap();
    Object  obj = new Object();
    ConvValue convValue = new ConvValue();


    stdout.println("/* Module Name = \""+ compileUnit.name +"\" */\n");


    // HashMap isStaticMap = getIsStaticMap(glname);

    HashMap isFuncOrDataMap = getIsFuncOrDataMap(compileUnit);

    BiList glList = glname.symbols();


    for (p = glList.first();!p.atEnd();p=p.next()) {
      sym = (Symbol)p.elem();
      if (sym instanceof SymStatic) {
        symsta = (SymStatic)sym;
        if ((symsta.segment.equals(".text")) && 
            (symsta.linkage.equals("LDEF"))) {
          text_ldef.put(convValue.replace(symsta.name),obj);
          // stdout.println("//(literal string) " + symsta.toString());
        } 
        // add by T.Kimura
        else {
          if (! symsta.linkage.equals("XREF")) {
            if (symsta.linkage.equals("LDEF")) {
              if (! isFuncOrDataMap.containsKey(convValue.replace(symsta.name))) {
                // unhappen 
                stdout.println("// static (do decralation) " + symsta.toString());
              } else {
                // stdout.println("// static (not decralation) " + symsta.toString());
              }
            } else if (symsta.linkage.equals("XDEF")) {
              if (! isFuncOrDataMap.containsKey(convValue.replace(symsta.name))) {
                // unhappen 
                stdout.println("//(do decralation) " + symsta.toString());
              } else {
                // stdout.println("//(not decralation) " + symsta.toString());
              }
            }
          } else {
            String symname = convValue.replace(symsta.name);
            if (symname.equals("printf")) {
              stdout.println("#include <stdio.h>");
            } else if (symname.equals("malloc")) {
              stdout.println("#include <stdlib.h>");
            } else if (symname.equals("sin")) {
              stdout.println("#include <math.h>");
            } else {
              // stdout.println("//...(" + convValue.replace(symsta.name) + ")");
            }
          }
        }
      } else {
        // NOT STATIC objects in global symbol table are ignored.

        // stdout.println("/* global symbol table contains not STATIC object ("
        //                + convValue.replace(sym.name) + "). */");
      }
    }

    /* at first, get global symbols in elemList */
    for (q = elemList.first();!q.atEnd();q=q.next()) {
      if (q.elem() instanceof Function) {
        /* do nothing */
      } else {
        if (q.elem() instanceof Data) {
          Data data;
          String results;
          LirNode lnode;
          int     array_length;

          data = (Data)q.elem();

          if (text_ldef.containsKey(convValue.replace(data.symbol.name))) {
//              results = "static char ";
//              results += convValue.replace(data.symbol.name);
//              lnode = (LirNode)data.components[0];
//              array_length = lnode.nKids();
//              results += "[" + Integer.toString(array_length) + "] = ";
//              results += conv.invoke(data.components[0]);

//              stdout.println(results + ";");

            Symbol sym_f_or_d;

            sym_f_or_d = (Symbol)isFuncOrDataMap.get(
              convValue.replace(data.symbol.name));

            Decla dec = MakeDecl.makeDeclStatic(sym_f_or_d,data);

            stdout.println("" + dec.toString() + ";");

          } else {
            Symbol sym_f_or_d;

            // show Data section.

            // stdout.println("/*\n");

            // debug ---
            // stdout.println("/* begin data section");
            // data.printIt(stdout);
            // stdout.println("end data section */\n");


            sym_f_or_d = (Symbol)isFuncOrDataMap.get(
              convValue.replace(data.symbol.name));

            Decla dec = MakeDecl.makeDeclStatic(sym_f_or_d,data);

            // debug
            // stdout.println("// " + sym_f_or_d.toString());


            // stdout.println("// " + dec.toString() + ";\n");
            stdout.println("" + dec.toString() + ";");

            // debug ---
            // stdout.println("// STRUCT = " + AnaData.structStr(data) + "\n");

//              stdout.println("(DATA \"" + data.symbol.name + "\"");
//              if (data.components.length <= 1) {
//                stdout.print(""+ conv.invoke(data.components[0]) + "");
//              } else {
//                stdout.print("{");
//                int i;

//                for (i= 0; i < data.components.length - 1; i++) {
//                  // stdout.print("" + data.components[i].toString());
//                  stdout.print(""+ conv.invoke(data.components[i]) + ",");
//                }
//                stdout.print(""+ conv.invoke(data.components[i]));
//                stdout.print("}");
//              }
//              stdout.println(")");

//              stdout.println("\n*/\n");



          }
        }
      }
    }

    /* and next, get function in elemList */
    for (q = elemList.first();!q.atEnd();q=q.next()) {
      if (q.elem() instanceof Function) {
        func = (Function)q.elem();
        invoke2(func);
      } else {
        if (q.elem() instanceof Data) {
          /* do nothing */
        }
      }
    }
  }

  /**
   * invoke2: invoke2 method converts the Function object to the C code.
   * it is called from coins.k3.lir2c.LirToC@invoke.
   * @param func the function to convert.
   **/
  public void invoke2(Function func) {
    FlowGraph cfg = func.flowGraph();
    Symbol sym = func.symbol;
    Symbol symtmp;
    BiList bbl = cfg.basicBlkList;
    BiLink q;
    BasicBlk blk;
    BiList lir;
    BiLink r;
    LirNode lnode;
    Convert conv = new Convert();
    String results;
    Label bblk_label;
    ConvValue cvalue = new ConvValue();
    HashMap   argline_hmap;

    stdout.println("");

    /** get the Epilogue of the function. */
    stdout.print(getEpilogue(func)+" ");

    // stdout.println("function name = " + sym.name);
    stdout.print(sym.name+"(");

    /** get the prologue of the function. */
    stdout.print(getPrologue(func));
    argline_hmap = getArgMap(func);

    stdout.println(") {");


    SymTab loname = func.localSymtab;
    BiList loList = loname.symbols();
    String declline;


    for (r = loList.first();!r.atEnd();r=r.next()) {
      symtmp = (Symbol)r.elem();
      if (! argline_hmap.containsKey(cvalue.replace(symtmp.name))) {
        declline = MakeDecl.makeDeclAuto(symtmp)+";";
        stdout.println(declline);
      }
    }

    for ( q = bbl.first();!q.atEnd();q=q.next()) {
      blk = (BasicBlk)q.elem();
      bblk_label =blk.label();


      // output label object(excepts the label ".blk1")
      if (! bblk_label.name().equals(".blk1")) {
        stdout.println("\n" + bblk_label.toString().replace('.','_')+":");
      }

      lir = blk.instrList();
      for (r = lir.first();!r.atEnd();r=r.next()){
        lnode = (LirNode)r.elem();

        // show lir (debug) ---
        //stdout.println("//" + lnode.toString());

        results = conv.invoke(lnode);
        if (!(results.endsWith(":") ||
          results.endsWith("}") ||
          results.endsWith(";"))) {
          
          if (! results.equals(""))
            stdout.println(results + ";");
          else
            stdout.println("");
        } else {
          stdout.println(results);
        }

      }
    }
    stdout.println("}\n");
  }
  
  /** 
   * revurLirNode: the experimental method that traverse LirNodes recursively.
   * no need to call this method in coins.k3.lir2c.LirToC@invoke2.
   * @param root the root node of the LirNodes.
   * @param stdout the print writer to output(C code).
   **/
  public void recurLirNode(LirNode root,PrintWriter stdout) {
    int i, n;
    
    n = root.nKids();
    stdout.print("("+n);
    
    if ( n == 0) {
      /* do nothing */
    } else {
      for (i = 0; i < n ; i++) {
        recurLirNode(root.kid(i),stdout);
      }
    }
    stdout.print(")");
  }

  /**
   * getEpilogue: this method gets the epilogue of the function.
   * @param func The function to get the epilogue.
   * @return The string of C code that for Epilogue.
   **/
  public String getEpilogue(Function func) {
    String results = "";

    FlowGraph cfg = func.flowGraph();
    Symbol sym = func.symbol;
    BiList bbl = cfg.basicBlkList;
    BiLink q;
    BasicBlk blk;
    BiList lir;
    BiLink r;
    LirNode lnode, child1;
    int opcode;
    Convert conv = new Convert();    

    for ( q = bbl.first();!q.atEnd();q=q.next()) {
      blk = (BasicBlk)q.elem();

      lir = blk.instrList();
      for (r = lir.first();!r.atEnd();r=r.next()){
        lnode = (LirNode)r.elem();

        opcode = lnode.opCode;
        if (opcode == Op.EPILOGUE) {
          if (lnode.nKids() < 2) {
            results = "void";
          } else if (lnode.nKids() == 2) {
            child1 = lnode.kid(1);
            results = conv.lirToTypeStr(child1);
          }
        }
      }
    }

    return results;
  }

  /**
   * getPrologue: this method gets the prologue of the function.
   * @param func The function to get the prologue.
   * @return The string of C code that for Prologue.
   **/
  public String getPrologue(Function func) {
    String results = "";

    FlowGraph cfg = func.flowGraph();
    Symbol sym = func.symbol;
    BiList bbl = cfg.basicBlkList;
    BiLink q;
    BasicBlk blk;
    BiList lir;
    BiLink r;
    LirNode lnode, child1;
    int opcode, i, num_src;
    Convert conv = new Convert();    

    for ( q = bbl.first();!q.atEnd();q=q.next()) {
      blk = (BasicBlk)q.elem();

      lir = blk.instrList();
      for (r = lir.first();!r.atEnd();r=r.next()){
        lnode = (LirNode)r.elem();

        opcode = lnode.opCode;
        if (opcode == Op.PROLOGUE) {
          num_src = lnode.nKids();
          if (num_src < 2) {
            results = "void";
          } else {
            for (i = 1; i < num_src; i++) {
              child1 = lnode.kid(i);
              results += conv.lirToTypeStr(child1);
              results += " " + conv.dump(child1,false,lnode);
              if (i != num_src - 1) {
                results += ", ";
              }
            }
          }
        }
      }
    }

    return results;
  }

  /**
   *  getArgMap: this method gets the declaration arguments in the Function.
   *  for example:
   *    func(int a, int b){}
   *    will get the hash entries ("int a","-none-") ("int b","-none-")
   * @param func The function to get argument map.
   * @return The HashMap that contains declaration arguments. 
   **/
  HashMap getArgMap(Function func) {
    String results = "";

    FlowGraph cfg = func.flowGraph();
    Symbol sym = func.symbol;
    BiList bbl = cfg.basicBlkList;
    BiLink q;
    BasicBlk blk;
    BiList lir;
    BiLink r;
    LirNode lnode, child1;
    int opcode, i, num_src;
    Convert conv = new Convert();    
    HashMap   argline_hmap = new HashMap();
    Object    obj = new Object();

    for ( q = bbl.first();!q.atEnd();q=q.next()) {
      blk = (BasicBlk)q.elem();

      lir = blk.instrList();
      for (r = lir.first();!r.atEnd();r=r.next()){
        lnode = (LirNode)r.elem();

        opcode = lnode.opCode;
        if (opcode == Op.PROLOGUE) {
          num_src = lnode.nKids();
          if (num_src < 2) {
            // results = "void";
          } else {
            for (i = 1; i < num_src; i++) {
              child1 = lnode.kid(i);
              results = conv.dump(child1,false,lnode);
              argline_hmap.put(results,obj);
            }
          }
        }
      }
    }

//    return results;
    return argline_hmap;
  }


  /**
   *  getIsStaticMap: this method gets the IsStatic Map in the global symbol table.
   *
   * @param gmap the global symbol table.
   * @return The HashMap that contains static values. 
   **/
  HashMap getIsStaticMap(SymTab gmap) {
    HashMap isStat_hmap = new HashMap();
    String results = "";

    Symbol sym;
    SymStatic symsta;
    BiLink p;
    Object  obj = new Object();
    ConvValue convValue = new ConvValue();

    BiList glList = gmap.symbols();


    for (p = glList.first();!p.atEnd();p=p.next()) {
      sym = (Symbol)p.elem();
      if (sym instanceof SymStatic) {
        symsta = (SymStatic)sym;
        if (symsta.linkage.equals("LDEF")) {
          isStat_hmap.put(convValue.replace(symsta.name),obj);
          // stdout.println("//(is static) " + symsta.toString());
        } 
      }
    }

    return isStat_hmap;
  }

  /**
   *  getIsFuncOrDataMap: this method gets the Function-or-Data Map in the Module object.
   *
   * @param cU the Module to get HashMap entry.
   * @return The HashMap that contains the names of value that is Function or Data. 
   **/
  HashMap getIsFuncOrDataMap(Module cU) {
    HashMap isF_or_D_hmap = new HashMap();
    String results = "";
    BiList elemList = cU.elements;
    BiLink q;
    Function func;
    Data data;

    Symbol sym;

    Object  obj = new Object();
    ConvValue convValue = new ConvValue();

    SymTab glname = cU.globalSymtab;

    for (q = elemList.first();!q.atEnd();q=q.next()) {
      if (q.elem() instanceof Function) {
        func = (Function)q.elem();
        sym = func.symbol;

        isF_or_D_hmap.put(convValue.replace(sym.name),obj);

      } else if (q.elem() instanceof Data) {
        data = (Data)q.elem();
        sym = data.symbol;

        Symbol content_symbol = glname.get(sym.name);

        isF_or_D_hmap.put(convValue.replace(sym.name),content_symbol);
      }
    }

    return isF_or_D_hmap;
  }

  public static String getInitializer(Data data) {
    Convert conv = new Convert();
    String results = "";

    if (data.components.length <= 1) {
      results += ""+ conv.invoke(data.components[0]) + "";
    } else {
      results += "{";

      int i;

      for (i= 0; i < data.components.length - 1; i++) {
        // stdout.print("" + data.components[i].toString());
        results += ""+ conv.invoke(data.components[i]) + ",";
      }
      results += ""+ conv.invoke(data.components[i]);
      results += "}";
    }

    return results;
  }
}

