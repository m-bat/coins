/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lir2c;

import coins.backend.sym.Symbol;
import coins.backend.lir.LirNode;
import coins.backend.Type;
import coins.backend.sym.SymStatic;
import coins.backend.sym.SymAuto;
import coins.backend.lir.LirIconst;
import coins.backend.Data;

/**
 * MakeDecl: make declaration of C from Symbol object.
 *
 *  now it only supports SymAuto objects.
 *
 **/
public class MakeDecl{
  /**
   * Constructor(with no arg)
   */
  public MakeDecl(){}
  
  /**
   * makeDeclAuto: static method for making declaration of C code
   * from SymAuto object.
   *
   * @param Symbol a Symbol object that makes a C declaration.
   * it must be a SymAuto object.
   * @return the string of C declaration.
   **/
  public static String makeDeclAuto(Symbol sym) {
    /** prefix */
    String prefix;
    /** postfix */
    String postfix;
    /** results (the first comes prefix, next comes results, 
        and ends with postfix) */
    String results;
    /** type string (i.e. I8, I16,...) */
    String typestr;
    /** Convert Value object (to call "replace" method of ConvValue) */
    ConvValue cv = new ConvValue();

    prefix = "";
    postfix = "";

    if (! (sym instanceof SymAuto)) {
      prefix = "//";
      results = " illegal symbol (not SymAuto object) ";
      return prefix + results;
    }

    prefix = "    "; // indent
    results = "";
    typestr = Type.toString(sym.type);

    if (typestr.equals("I8")) {
      prefix += "char";
    } else if (typestr.equals("I16")) {
      // prefix += "char";
      prefix += "short";
    } else if (typestr.equals("I32")) {
      prefix += "int";
    } else if (typestr.equals("I64")) {
      prefix += "long";
    } else if (typestr.equals("F32")) {
      prefix += "float";
    } else if (typestr.equals("F64")) {
      prefix += "double";
    } else if (typestr.equals("F128")) {
      prefix += "long double";
    } else {
      // prefix = "AGGREGATE?";

      if (sym.boundary == 1) {
        prefix += "char";
      } else if (sym.boundary == 2) {
        prefix += "short";        
      } else if (sym.boundary == 4) {
        prefix += "int";
        // prefix += "float";
      } else if (sym.boundary == 8) {
        prefix += "long";
        // prefix += "double";
      } else if (sym.boundary == 16) {
        prefix += "long double";
        // prefix += "long long";
      } else {
        prefix += "//";
        results = " unknown type (too big?) ";
      }

      if (typestr.indexOf('A',0) == 0) {
        String subtypestr = typestr.substring(1);
        int  len_of_obj;

        len_of_obj = Integer.parseInt(subtypestr);
        postfix = "[" + Integer.toString(len_of_obj / sym.boundary / 8) + "]";
      } else {
        prefix += "//";
        results = " unknown type (not I,F,A) ";
      }
    }

    results += " "+cv.replace(sym.name);

    return prefix + results + postfix;
  }

  /**
   * makeDeclStatic: static method for making declaration of C code
   * from SymStatic object and Data object.
   *
   * @param sym a Symbol object that makes a C declaration.
   * it must be a SymStatic object.
   * @param data a Data object that makes a C declaration.
   * @return the Decla object that contains C declaration.
   **/
  public static Decla makeDeclStatic(Symbol sym,Data data) {
    SymStatic symsta;
    String structStr = "";
    Decla dec = new Decla();
    /** prefix */
    String prefix;
    /** postfix */
    String postfix;
    /** results (the first comes prefix, next comes results, 
        and ends with postfix) */
    String results;
    /** type string (i.e. I8, I16,...) */
    String typestr;
    /** Convert Value object (to call "replace" method of ConvValue) */
    ConvValue cv = new ConvValue();

    prefix = "";
    postfix = "";

    if (! (sym instanceof SymStatic)) {
      // prefix = "//";
      // results = " illegal symbol (not SymStatic object) ";
      // return prefix + results;

      dec.indent = "// illegal symbol (not SymStatic object) ";
      return dec;
    }

    symsta = (SymStatic)sym;

    prefix = "    "; // indent
    results = "";
    typestr = Type.toString(sym.type);

    dec.indent = "";
    // dec.indent = "    ";

    if (symsta.linkage.equals("LDEF"))
      dec.storage = "static";

    if (typestr.equals("I8")) {
      dec.type = "char";
    } else if (typestr.equals("I16")) {
      // prefix += "char";
      dec.type = "short";
    } else if (typestr.equals("I32")) {
      dec.type = "int";
    } else if (typestr.equals("I64")) {
      dec.type = "long";
    } else if (typestr.equals("F32")) {
      dec.type = "float";
    } else if (typestr.equals("F64")) {
      dec.type = "double";
    } else if (typestr.equals("F128")) {
      dec.type = "long double";
    } else {
      // prefix = "AGGREGATE?";

      if (sym.boundary == 1) {
        dec.type = "char";
      } else if (sym.boundary == 2) {
        dec.type = "short";        
      } else if (sym.boundary == 4) {
        dec.type = "int";
        // prefix += "float";
      } else if (sym.boundary == 8) {
        dec.type = "long";
        // prefix += "double";
      } else if (sym.boundary == 16) {
        dec.type = "long double";
        // prefix += "long long";
      } else {
        dec.indent =  "//";
        dec.indent += " unknown type (too big?) ";
      }

      if (typestr.indexOf('A',0) == 0) {
        String subtypestr = typestr.substring(1);
        int  len_of_obj;

        len_of_obj = Integer.parseInt(subtypestr);
        // postfix = "[" + Integer.toString(len_of_obj / sym.boundary / 8) + "]";
        dec.array = "[" + Integer.toString(len_of_obj / sym.boundary / 8) + "]";
      } else {
        dec.indent = "//";
        dec.indent += " unknown type (not I,F,A) ";
      }
    }

    // results += " "+cv.replace(sym.name);
    dec.identifier = cv.replace(sym.name);


    // rewrite dec by Data object.

    structStr = AnaData.structStr(data);
    if (structStr.equals("Z")) {
      /* do nothing */
    } else if (structStr.equals("ZZ")) {
      LirNode lnode, lnode2;
      int len = data.components.length;
      int int_value;

      // get zeros
      lnode = (LirNode)data.components[0];
      // get zeros's argument(INTCONST)
      lnode2 = lnode.kid(0);
      int_value = (int)(((LirIconst)lnode2).value);
      
      dec.array = "[" + len + "][" + int_value / sym.boundary + "]";
    } else if (structStr.equals("S")) {
      /* do nothing */
    } else if (structStr.equals("LN")) {
      Convert conv = new Convert();
      LirNode lnode, lnode2;
      lnode = (LirNode)data.components[0];
      dec.type = conv.lirToTypeStr(lnode);

      if (dec.array.equals("")) { // value
        dec.initializer = LirToC.getInitializer(data);
      } else { // array that has 1 element.
        dec.array = "[1]";
        dec.initializer = LirToC.getInitializer(data);
      }
    } else if (structStr.equals("Ln")) {
      Convert conv = new Convert();
      LirNode lnode, lnode2;
      int len2;

      lnode = (LirNode)data.components[0];
      dec.type = conv.lirToTypeStr(lnode);
      len2 = lnode.nKids();

      dec.array = "[" + len2 + "]";

      dec.initializer = LirToC.getInitializer(data);
    } else if (structStr.equals("LS")) {
      LirNode lnode;
      int len2;

      dec.type = "char";
      dec.pointer = "*";
      lnode = (LirNode)data.components[0];
      len2 = lnode.nKids();
      dec.array = "[" + len2 + "]";

      dec.initializer = LirToC.getInitializer(data);
    } else if (structStr.equals("LLn") || structStr.equals("LLN")) {
      Convert conv = new Convert();
      LirNode lnode, lnode2;
      int len = data.components.length;
      int len2;

      // get list
      lnode = (LirNode)data.components[0];
      // get list of list
      len2 = lnode.nKids();

      dec.type = conv.lirToTypeStr(lnode);
      dec.array = "["+ len +"]["+ len2 +"]";

      dec.initializer = LirToC.getInitializer(data);
    } else if (structStr.equals("SS")) {
      // not happen
      /* do nothing */
    } else if (structStr.equals("LLS")) {
      // not happen
      /* do nothing */
    } else if (structStr.equals("LSn")) {
      // not happen
      /* do nothing */
    } else {
      // not happen
    }

    // return prefix + results + postfix;
    return dec;
  }
}
