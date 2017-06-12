/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.SymRoot;
import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpListExpImpl;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.Stmt;
import coins.sym.PointerType;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;

/**
 * File I77wrapper.java Utility class to use libI77. libI77 is IO library for
 *       F2C, distributed at http://www.netlib.org/f2c/index.html To use libI77,
 *       follow their notice.
 */

public class I77Utils{
  FirToHir        fHir;
  HIR             hir;

  Sym             sym;
  TypeUtility     fTypeUtil;
  HirUtility      fHirUtil;
  ExecStmtManager fESMgr;

  boolean         read_unit          = false;
  boolean         read_unit_internal = false;
  boolean         read_fmt           = false;
  boolean         read_rec           = false;
  boolean         read_err           = false;
  boolean         read_end           = false;
  boolean         read_iostat        = false;
  boolean         is_listed          = false;
  boolean         read_file          = false;
  boolean         read_status        = false;
  boolean         read_access        = false;
  boolean         read_form          = false;
  boolean         read_recl          = false;
  boolean         read_blank         = false;
  boolean         read_exist         = false;
  boolean         read_opened        = false;
  boolean         read_number        = false;
  boolean         read_named         = false;
  boolean         read_name          = false;
  boolean         read_sequential    = false;
  boolean         read_direct        = false;
  boolean         read_formatted     = false;
  boolean         read_unformatted   = false;
  boolean         read_nextrec       = false;
  boolean         is_inquire         = false;

  Exp             unit_              = null; // unspecified
  Exp             rec_               = null;
  Exp             iostat_            = null;
  int             err_               = -1;
  String          fmt_str_           = null;
  Exp             fmt_;

  // open specific
  Exp             i_file;
  Exp             i_filelen;
  Exp             i_status;                  // and close
  Exp             i_access;                  // and inquire
  Exp             i_accesslen;
  Exp             i_form;                    // and inquire
  Exp             i_formlen;
  Exp             i_recl;
  Exp             i_blank;
  Exp             i_blanklen;

  // inquire specific
  Exp             i_exist;
  Exp             i_opened;
  Exp             i_number;
  Exp             i_named;
  Exp             i_name;
  Exp             i_namelen;
  Exp             i_sequential;
  Exp             i_sequentiallen;
  Exp             i_direct;
  Exp             i_directlen;
  Exp             i_formatted;
  Exp             i_formattedlen;
  Exp             i_unformatted;
  Exp             i_unformattedlen;
  Exp             i_nextrec;
  Exp             func_do;
  Exp             func_s;
  Exp             func_e;
  String          read_or_write_char;

  I77Utils(FirToHir fth) {
    fHir = fth;
    hir = fth.getHir();
    sym = fth.getSym();
    fTypeUtil = fHir.getTypeUtility();
    fHirUtil = fHir.getHirUtility();
    fESMgr = fHir.getExecStmtManager();
  }

  // write
  void writeInit(FirList cList) {
    read_or_write_char = "w";
    check_control_information(cList);
    select_function();
  }

  // read
  void readInit(FirList cList, Node fmt, boolean fmt_given) {
    read_or_write_char = "r";
    if(fmt_given){
      ci_fmt(fmt);
    }
    check_control_information(cList);
    select_function();
  }

  // open
  void openInit(FirList cList) {
    i_file = fHirUtil.makeConstInt0Node();
    i_filelen = fHirUtil.makeConstInt0Node();
    i_status = fHirUtil.makeConstInt0Node();
    i_access = fHirUtil.makeConstInt0Node();
    i_form = fHirUtil.makeConstInt0Node();
    i_recl = fHirUtil.makeConstInt0Node();
    i_blank = fHirUtil.makeConstInt0Node();

    check_control_information_for_open(cList);
  }

  // close
  void closeInit(FirList cList) {
    i_status = fHirUtil.makeConstInt0Node();
    check_control_information_for_close(cList);
  }

  // inquire init
  void inquireInit(FirList cList) {
    is_inquire = true;

    unit_ = fHirUtil.makeConstInt0Node();
    i_file = fHirUtil.makeConstInt0Node();
    i_filelen = fHirUtil.makeConstInt0Node();
    i_status = fHirUtil.makeConstInt0Node();
    i_access = fHirUtil.makeConstInt0Node();
    i_form = fHirUtil.makeConstInt0Node();
    i_recl = fHirUtil.makeConstInt0Node();
    i_blank = fHirUtil.makeConstInt0Node();
    i_exist = fHirUtil.makeConstInt0Node();
    i_opened = fHirUtil.makeConstInt0Node();
    i_number = fHirUtil.makeConstInt0Node();
    i_named = fHirUtil.makeConstInt0Node();
    i_name = fHirUtil.makeConstInt0Node();
    i_sequential = fHirUtil.makeConstInt0Node();
    i_direct = fHirUtil.makeConstInt0Node();
    i_formatted = fHirUtil.makeConstInt0Node();
    i_unformatted = fHirUtil.makeConstInt0Node();
    i_nextrec = fHirUtil.makeConstInt0Node();

    check_control_information_for_inquire(cList);
  }

  // rewind, backspace, endfile
  void othersInit(FirList cList, Node fmt) {
    if(fmt == null){
      check_control_information_for_others(cList);
    }
    else{
      unit_ = fmt.makeExp();
    }
    if(unit_ == null){
      error("unit must be specified.");
    }
  }

  ///////////////////////////////////////////////
  ///////////////////////////////////////////////
  // declare types
  /**
   * declare cilist at global scope
   *
   * <pre>
   * typedef struct
   * {	flag   cierr;  // int
   *    ftnint ciunit; // int
   *    flag   ciend;  // int
   *    char  *cifmt;  // char *
   *    ftnint cirec;  // int
   *  } cilist;
   * </pre>
   */
  Type get_type_cilist() {
    SymRoot sr = fHir.getSymRoot();
    String names[] = {
      "cierr", "ciunit", "ciend", "cifmt", "cirec",
    };
    Type types[] = {
      sr.typeInt,
      sr.typeInt,
      sr.typeInt,
      sym.pointerType(sr.typeChar),
      sr.typeInt,
    };
    return fTypeUtil.getGlobalStructType("__libi77_cilist", names, types);
  }

  /**
   * declare icilist at global scope
   *
   * <pre>
   * typedef struct
   * {
   *   flag icierr;   // int
   *   char *iciunit; // char *
   *   flag iciend;   // int
   *   char *icifmt;  // char *
   *   ftnint icirlen;// int
   *   ftnint icirnum;// int
   * } icilist;
   * </pre>
   */
  Type get_type_icilist() {
    SymRoot sr = fHir.getSymRoot();
    String names[] = {
      "icierr", "iciunit", "iciend", "icifmt", "icirlen", "icirnum",
    };
    Type types[] = {
      sr.typeInt,
      sym.pointerType(sr.typeChar),
      sr.typeInt,
      sym.pointerType(sr.typeChar),
      sr.typeInt,
      sr.typeInt,
    };
    return fTypeUtil.getGlobalStructType("__libi77_icilist", names, types);
  }

  /**
   * declare olist at global scope
   *
   * <pre>
   *             typedef struct
   *             {	flag   oerr;    // int
   *             ftnint ounit;   // int
   *             char  *ofnm;    // char *
   *             ftnlen ofnmlen; // int
   *             char  *osta;    // char *
   *             char  *oacc;    // char *
   *             char  *ofm;     // char *
   *             ftnint orl;     // int
   *             char  *oblnk;   // char *
   *             } olist;
   * </pre>
   */
  Type get_type_olist() {
    SymRoot sr = fHir.getSymRoot();
    String names[] = {
      "oerr",
      "ounit",
      "ofnm",
      "ofnmlen",
      "osta",
      "oacc",
      "ofm",
      "orl",
      "oblnk",
    };
    Type types[] = {
      sr.typeInt,
      sr.typeInt,
      sym.pointerType(sr.typeChar),
      sr.typeInt,
      sym.pointerType(sr.typeChar),
      sym.pointerType(sr.typeChar),
      sym.pointerType(sr.typeChar),
      sr.typeInt,
      sym.pointerType(sr.typeChar),
    };
    return fTypeUtil.getGlobalStructType("__libi77_olist", names, types);
  }

  /**
   * declare cllist at global scope
   *
   * <pre>
   *   typedef struct{
   * 	  flag   cerr;   // int
   *      ftnint cunit;  // int
   *      char  *csta;   // char *
   *    } cllist;
   * </pre>
   */
  Type get_type_cllist() {
    SymRoot sr = fHir.getSymRoot();
    String names[] = {
      "cerr", "cunit", "csta",
    };
    Type types[] = {
      sr.typeInt, sr.typeInt, sym.pointerType(sr.typeChar),
    };
    return fTypeUtil.getGlobalStructType("__libi77_cllist", names, types);
  }

  /**
   * declare inlist at global scope
   *
   * <pre>
   typedef struct
   {
   flag inerr;
   ftnint  inunit;
   char   *infile;
   ftnlen  infilen;
   ftnint *inex;      //parameters in standard's order
   ftnint *inopen;
   ftnint *innum;
   ftnint *innamed;
   char   *inname;
   ftnlen  innamlen;
   char   *inacc;
   ftnlen  inacclen;
   char   *inseq;
   ftnlen  inseqlen;
   char   *indir;
   ftnlen  indirlen;
   char   *infmt;
   ftnlen  infmtlen;
   char   *inform;
   ftnint  informlen;
   char   *inunf;
   ftnlen  inunflen;
   ftnint *inrecl;
   ftnint *innrec;
   char   *inblank;
   ftnlen  inblanklen;
   } inlist;
   </pre>
   */
  Type get_type_inlist() {
    SymRoot sr = fHir.getSymRoot();
    String names[] = {
      "inerr",
      "inunit",
      "infile",
      "infilen",
      "inex",
      "inopen",
      "innum",
      "innamed",
      "inname",
      "innamlen",
      "inacc",
      "inacclen",
      "inseq",
      "inseqlen",
      "indir",
      "indirlen",
      "infmt",
      "infmtlen",
      "inform",
      "informlen",
      "inunf",
      "inunflen",
      "inrecl",
      "innrec",
      "inblank",
      "inblanklen",
    };
    Type types[] = {
      sr.typeInt, // err
      sr.typeInt, // inunit
      sym.pointerType(sr.typeChar), sr.typeInt, // infilelen
      sym.pointerType(sr.typeInt), // inex
      sym.pointerType(sr.typeInt), // inopen
      sym.pointerType(sr.typeInt), // innum
      sym.pointerType(sr.typeInt), // innamed

      sym.pointerType(sr.typeChar),// inname
      sr.typeInt, // innamlen

      sym.pointerType(sr.typeChar),// inacc
      sr.typeInt, // inacclen

      sym.pointerType(sr.typeChar),// inseq
      sr.typeInt,

      sym.pointerType(sr.typeChar),// indir
      sr.typeInt,

      sym.pointerType(sr.typeChar),// infmt
      sr.typeInt,

      sym.pointerType(sr.typeChar),// inform
      sr.typeInt,

      sym.pointerType(sr.typeChar),// inunf
      sr.typeInt,

      sym.pointerType(sr.typeInt), // inrecl
      sym.pointerType(sr.typeInt), // innrec

      sym.pointerType(sr.typeChar),// inblank
      sr.typeInt,
    };
    return fTypeUtil.getGlobalStructType("__libi77_inlist", names, types);
  }

  /**
   * declare alist at global scope
   *
   * <pre>
   *             // rewind, backspace, endfile
   *             typedef struct
   *             {	flag aerr;
   *             ftnint aunit;
   *             } alist;
   * </pre>
   */
  Type get_type_alist() {
    SymRoot sr = fHir.getSymRoot();
    String names[] = {
      "aerr", "aunit",
    };
    Type types[] = {
      sr.typeInt, sr.typeInt,
    };
    return fTypeUtil.getGlobalStructType("__libi77_alist", names, types);
  }

  ///////////////////////////////////////////////
  ///////////////////////////////////////////////
  // analyze control information listg
  boolean ci_unit(Node t) {
    if(read_unit){
      //K error
      error("already specified : unit");
    }
    read_unit = true;
    if(t == null){
      // none
    }
    else{
      unit_ = t.makeExp();
    }

    if(unit_ instanceof FortranCharacterExp||
       (unit_ != null && fTypeUtil.isFortranCharacterType(unit_.getType()))){
      read_unit_internal = true;
      dp("[IO] unit (internal) : " + unit_);
    }
    else{
      dp("[IO] unit: " + unit_);
    }
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_fmt(Node n) {
    if(read_fmt){
      //K error
      error("already specified : fmt");
    }
    read_fmt = true;
    if(n == null){
      // none
    }
    else if(n instanceof Token && ((Token) n).getKind() == Parser.INT_CONST){
      Token t = (Token) n;

      // label no
      fmt_str_ = (String) fHir.f7Sym.formatMap.get(
        "" + (Integer.parseInt(t.getLexem())));
      if(fmt_str_ == null){
        //K error
        error("No such format statement: " + t.getLexem());
      }
    }
    else{
      fmt_ = n.makeExp();
      if(fmt_ instanceof FortranCharacterExp){
        fmt_ = ((FortranCharacterExp) fmt_).getBody();
      }
      else{
        error("Format must be CHARACTER: " + fmt_);
      }
    }
    dp("[IO] fmt: " + fmt_);
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_rec(Node t) {
    if(read_rec){
      //K error
      error("already specified : rec");
    }
    read_rec = true;

    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_iostat(Node t) {
    if(read_iostat){
      //K error
      error("already specified : iostat");
    }
    read_iostat = true;

    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_err(Node t) {
    if(read_err){
      //K error
      error("already specified : err");
    }
    read_err = true;

    fHir.printMsgWarn("err is not supported.");
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_end(Node t) {
    if(read_end){
      //K error
      error("already specified : end");
    }
    if(read_or_write_char == "w"){
      error("can't use 'END' with writing");
    }
    read_end = true;
    return true;
  }

  ///////////////////////////////////////////////
  // open specific
  boolean ci_file(Node t) {
    if(read_file){
      //K error
      error("already specified : file");
    }
    Exp exp = t.makeExp();
    FortranCharacterExp fn = fTypeUtil.castFortranCharacterExp(exp);
    i_file = fn.getBody();
    i_filelen = fn.getLength();
    read_file = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_status(Node t) {
    if(read_status){
      //K error
      error("already specified : iostat");
    }
    i_status = fTypeUtil.castFortranCharacterExp(t.makeExp()).getBody();

    read_status = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_access(Node t) {
    if(read_access){
      //K error
      error("already specified : iostat");
    }

    Exp exp = t.makeExp();
    FortranCharacterExp str = fTypeUtil.castFortranCharacterExp(exp);
    i_access = str.getBody();
    i_accesslen = str.getLength();

    read_access = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_form(Node t) {
    if(read_form){
      //K error
      error("already specified : iostat");
    }
    Exp exp = t.makeExp();
    FortranCharacterExp str = fTypeUtil.castFortranCharacterExp(exp);
    i_form = str.getBody();
    i_formlen = str.getLength();

    read_form = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_recl(Node t) {
    if(read_recl){
      //K error
      error("already specified : iostat");
    }
    if(is_inquire != true){
      i_recl = t.makeExp();
    }
    else{
      i_recl = hir.exp(HIR.OP_ADDR, fHirUtil.castToInteger(t.makeExp()));
    }

    read_recl = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_blank(Node t) {
    if(read_blank){
      //K error
      error("already specified : iostat");
    }
    Exp exp = t.makeExp();
    FortranCharacterExp str = fTypeUtil.castFortranCharacterExp(exp);
    i_blank = str.getBody();
    i_blanklen = str.getLength();

    read_blank = true;
    return true;
  }

  ///////////////////////////////////////////////
  ///////////////////////////////////////////////

  // inquire specific
  ///////////////////////////////////////////////
  boolean ci_exist(Node t) {
    if(read_exist){
      //K error
      error("already specified : exist");
    }
    i_exist = hir.exp(HIR.OP_ADDR, fHirUtil.castToInteger(t.makeExp()));

    read_exist = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_opened(Node t) {
    if(read_opened){
      //K error
      error("already specified : opened");
    }
    i_opened = hir.exp(HIR.OP_ADDR, fHirUtil.castToInteger(t.makeExp()));

    read_opened = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_number(Node t) {
    if(read_number){
      //K error
      error("already specified : number");
    }
    i_number = hir.exp(HIR.OP_ADDR, t.makeExp());

    read_number = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_named(Node t) {
    if(read_named){
      //K error
      error("already specified : named");
    }
    i_named = hir.exp(HIR.OP_ADDR, t.makeExp());

    read_named = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_name(Node t) {
    if(read_name){
      //K error
      error("already specified : name");
    }
    Exp exp = t.makeExp();
    FortranCharacterExp str = fTypeUtil.castFortranCharacterExp(exp);
    i_name = str.getBody();
    i_namelen = str.getLength();

    read_name = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_sequential(Node t) {
    if(read_sequential){
      //K error
      error("already specified : sequential");
    }
    Exp exp = t.makeExp();
    FortranCharacterExp str = fTypeUtil.castFortranCharacterExp(exp);
    i_sequential = str.getBody();
    i_sequentiallen = str.getLength();

    read_sequential = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_direct(Node t) {
    if(read_direct){
      //K error
      error("already specified : direct");
    }
    Exp exp = t.makeExp();
    FortranCharacterExp str = fTypeUtil.castFortranCharacterExp(exp);
    i_direct = str.getBody();
    i_directlen = str.getLength();

    read_direct = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_formatted(Node t) {
    if(read_formatted){
      //K error
      error("already specified : formatted");
    }
    Exp exp = t.makeExp();
    FortranCharacterExp str = fTypeUtil.castFortranCharacterExp(exp);
    i_formatted = str.getBody();
    i_formattedlen = str.getLength();

    read_formatted = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_unformatted(Node t) {
    if(read_unformatted){
      //K error
      error("already specified : unformatted");
    }
    Exp exp = t.makeExp();
    FortranCharacterExp str = fTypeUtil.castFortranCharacterExp(exp);
    i_unformatted = str.getBody();
    i_unformattedlen = str.getLength();

    read_unformatted = true;
    return true;
  }

  ///////////////////////////////////////////////
  boolean ci_nextrec(Node t) {
    if(read_nextrec){
      //K error
      error("already specified : nextrec");
    }
    i_nextrec = hir.exp(HIR.OP_ADDR, t.makeExp());

    read_nextrec = true;
    return true;
  }

  ///////////////////////////////////////////////
  void check_control_information(FirList ciList) {
    int i = 0;

    // if there is 'end' information, that's error!
    // int end = -1;
    if(ciList == null){
      ciList = new FirList(fHir);
    }

    // read control information list
    Iterator it = ciList.iterator();
    while (it.hasNext()){
      Pair p = (Pair) it.next();
      Token k = (Token) p.getLeft();
      Node t = (Node) p.getRight();

      if(k == null){
        if(i == 0){
          ci_unit(t);
        }
        else if(i == 1){
          ci_fmt(t);
        }
      }
      else{
        String kind = k.getLexem();

        if(kind == "unit"){
          ci_unit(t);
        }
        else if(kind == "fmt"){
          ci_fmt(t);
        }
        else if(kind == "rec"){
          ci_rec(t);
        }
        else if(kind == "err"){
          ci_err(t);
        }
        else if(kind == "end"){
          ci_end(t);
        }
        else if(kind == "iostat"){
          ci_iostat(t);
        }
        else{
          //K error!
          error("unkown IO control information :" + kind);
        }
        i = 3;
      }
      i += 1;
    }
    // set
    if(unit_ == null){
      unit_ = fHirUtil.makeIntConstNode(read_or_write_char == "r" ? 5 : 6);
    }
    if(rec_ == null){
      rec_ = fHirUtil.makeIntConstNode(0);
    }
  }

  void check_control_information_for_open(FirList ciList) {
    if(ciList == null){
      ciList = new FirList(fHir);
    }
    // read control information list
    Iterator it = ciList.iterator();
    int i = 0;
    while (it.hasNext()){
      Pair p = (Pair) it.next();
      Token k = (Token) p.getLeft();
      Node t = (Node) p.getRight();

      if(k == null){
        if(i == 0){
          ci_unit(t);
        }
      }
      else{
        String kind = k.getLexem();

        if(kind == "unit"){
          ci_unit(t);
        }
        else if(kind == "err"){
          ci_err(t);
        }
        else if(kind == "iostat"){
          ci_iostat(t);
        }
        // open specific
        else if(kind == "file"){
          ci_file(t);
        }
        else if(kind == "status"){
          ci_status(t);
        }
        else if(kind == "access"){
          ci_access(t);
        }
        else if(kind == "form"){
          ci_form(t);
        }
        else if(kind == "recl"){
          ci_recl(t);
        }
        else if(kind == "blank"){
          ci_blank(t);
        }
        else{
          //K error!
          error("unkown IO control information :" + kind);
        }
      }
      i += 1;
    }
  }

  void check_control_information_for_close(FirList ciList) {
    if(ciList == null){
      ciList = new FirList(fHir);
    }
    // read control information list
    Iterator it = ciList.iterator();
    int i = 0;

    while (it.hasNext()){
      Pair p = (Pair) it.next();
      Token k = (Token) p.getLeft();
      Node t = (Node) p.getRight();

      if(k == null){
        if(i == 0){
          ci_unit(t);
        }
      }
      else{
        String kind = k.getLexem();

        if(kind == "unit"){
          ci_unit(t);
        }
        else if(kind == "err"){
          ci_err(t);
        }
        else if(kind == "iostat"){
          ci_iostat(t);
        }
        // close specific
        else if(kind == "status"){
          ci_status(t);
        }
        else{
          //K error!
          error("unkown IO control information :" + kind);
        }
      }
      i += 1;
    }
  }

  void check_control_information_for_inquire(FirList ciList) {
    if(ciList == null){
      error("INQUIRE: control information must be specified");
    }
    // read control information list
    Iterator it = ciList.iterator();
    while (it.hasNext()){
      Pair p = (Pair) it.next();
      Token k = (Token) p.getLeft();
      Node t = (Node) p.getRight();

      String kind = k.getLexem();

      if(kind == "unit"){
        ci_unit(t);
      }
      else if(kind == "err"){
        ci_err(t);
      }
      else if(kind == "iostat"){
        ci_iostat(t);
      }
      else if(kind == "file"){
        ci_file(t);
      }
      // inquire specific
      else if(kind == "exist"){
        ci_exist(t);
      }
      else if(kind == "opened"){
        ci_opened(t);
      }
      else if(kind == "number"){
        ci_number(t);
      }
      else if(kind == "named"){
        ci_named(t);
      }
      else if(kind == "name"){
        ci_name(t);
      }
      else if(kind == "access"){
        ci_access(t);
      }
      else if(kind == "sequential"){
        ci_sequential(t);
      }
      else if(kind == "direct"){
        ci_direct(t);
      }
      else if(kind == "form"){
        ci_form(t);
      }
      else if(kind == "formatted"){
        ci_formatted(t);
      }
      else if(kind == "unformatted"){
        ci_unformatted(t);
      }
      else if(kind == "recl"){
        ci_recl(t);
      }
      else if(kind == "nextrec"){
        ci_nextrec(t);
      }
      else if(kind == "blank"){
        ci_blank(t);
      }

      else if(kind == "status"){
        ci_status(t);
      }

      else{
        //K error!
        error("unkown IO control information :" + kind);
      }
    }
  }

  void check_control_information_for_others(FirList ciList) {
    if(ciList == null){
      ciList = new FirList(fHir);
    }
    // read control information list
    Iterator it = ciList.iterator();
    int i = 0;
    while (it.hasNext()){
      Pair p = (Pair) it.next();
      Token k = (Token) p.getLeft();
      Node t = (Node) p.getRight();

      if(k == null){
        if(i == 0){
          ci_unit(t);
        }
      }
      else{
        String kind = k.getLexem();

        if(kind == "unit"){
          ci_unit(t);
        }
        else if(kind == "err"){
          ci_err(t);
        }
        else if(kind == "iostat"){
          ci_iostat(t);
        }
        // close specific
        else{
          //K error!
          error("unkown IO control information :" + kind);
        }
      }
      i += 1;
    }
  }

  ///////////////////////////////////////////////
  ///////////////////////////////////////////////

  void select_function() {
    String f_do;
    String f_se;

    if(read_fmt){
      if(fmt_ == null && fmt_str_ == null){
        // listed
        f_do = "do_lio";
        if(read_unit_internal){
          f_se = "sli";
        }
        else{
          f_se = "sle";
        }
        is_listed = true;
        fmt_ = fHirUtil.makeConstInt0Node();
      }
      else{
        // formatted
        if(read_unit_internal){
          // internal
          f_do = "do_fio";
          f_se = "sfi";
        }
        else{
          // external
          f_do = "do_fio";
          f_se = "sfe";
        }
      }
    }
    else{
      // unformatted
      f_do = "do_uio";
      f_se = "sue";
      fmt_ = fHirUtil.makeIntConstNode(0);
    }
    //
    func_do = fHirUtil.makeSubpNode(f_do.intern(), Parser.INTEGER,
                                    hir.irList(), Sym.SYM_EXTERN);
    func_s = fHirUtil.makeSubpNode(("s_" + read_or_write_char + f_se).intern(),
                                   Parser.INTEGER, hir.irList(), Sym.SYM_EXTERN);
    func_e = fHirUtil.makeSubpNode(("e_" + read_or_write_char + f_se).intern(),
                                   Parser.INTEGER, hir.irList(), Sym.SYM_EXTERN);
  }

  Stmt io_start() {
    IrList args = hir.irList();
    BlockStmt stmt = hir.blockStmt(null);
    Var v;
    java.util.List list = new java.util.LinkedList();

    if(read_unit_internal){
      // error("unsupport read_unit_internal");
      Type type = get_type_icilist();
      String name = fESMgr.getTempName() + "_icilist_";
      v = sym.defineVar(name.intern(), type);
      v.setStorageClass(Var.VAR_STATIC);

      // "icierr"
      list.add(fHirUtil.makeIntConstNode(read_err ? 1 : 0));

      // "iciunit"
      list.add(fHirUtil.makeConstInt0Node());
      stmt.addLastStmt(
          fHirUtil.makeAssignStmt(
            hir.qualifiedExp(hir.varNode(v),
                 	         hir.elemNode(fTypeUtil.searchElem("iciunit", type))),
        ((FortranCharacterExp)unit_).getBody()));

      // "iciend"
      list.add(fHirUtil.makeIntConstNode(read_end ? 1 : 0));

      // "icifmt"
      dp("fmt_str_: " + fmt_str_);
      if(fmt_str_ != null){
        fmt_ = fHirUtil.makeCharsConstNode(fmt_str_).getBody();
        list.add(fmt_);
      }
      else{
        list.add(fHirUtil.makeIntConstNode(0));
        stmt.addLastStmt(fHirUtil.makeAssignStmt(
          hir.qualifiedExp(
            hir.varNode(v),
            hir.elemNode(fTypeUtil.searchElem("icifmt", type))),
          fmt_));
      }

      // "icirlen"
      list.add(((FortranCharacterExp)unit_).getLength());

      // "icirnum",
      list.add(fHirUtil.makeIntConstNode(1));
    }
    else{
      Type type = get_type_cilist();
      String name = fESMgr.getTempName() + "_cilist_";
      v = sym.defineVar(name.intern(), type);
      v.setStorageClass(Var.VAR_STATIC);
      {
        //"cierr",
        list.add(fHirUtil.makeIntConstNode(read_err ? 1 : 0));
        //"ciunit",
        if(unit_ instanceof ConstNode){
          list.add(unit_);
        }
        else{
          list.add(fHirUtil.makeIntConstNode(0));
          stmt.addLastStmt(fHirUtil.makeAssignStmt(
            hir.qualifiedExp(
              hir.varNode(v),
              hir.elemNode(fTypeUtil.searchElem("ciunit", type))),
            unit_));
        }

        //"ciend",
        list.add(fHirUtil.makeIntConstNode(read_end ? 1 : 0));

        //"cifmt",
        if(fmt_str_ != null){
          fmt_ = fHirUtil.makeCharsConstNode(fmt_str_).getBody();
          list.add(fmt_);
        }
        else{
          list.add(fHirUtil.makeIntConstNode(0));
          stmt.addLastStmt(fHirUtil.makeAssignStmt(
            hir.qualifiedExp(
              hir.varNode(v),
              hir.elemNode(fTypeUtil.searchElem("cifmt", type))),
            fmt_));
        }

        //"cirec",
        if(rec_ instanceof ConstNode){
          list.add(rec_);
        }
        else{
          list.add(fHirUtil.makeIntConstNode(0));
          stmt.addLastStmt(fHirUtil.makeAssignStmt(
            hir.qualifiedExp(
              hir.varNode(v),
              hir.elemNode(fTypeUtil.searchElem("cirec".intern(),
                                                type))),
            unit_));
        }
      }
    }

    fHir.getDeclManager().setInitialValue(
      v,
      new ExpListExpImpl(fHir.getHirRoot(), list));

    args.add(hir.exp(HIR.OP_ADDR, hir.varNode(v)));
    stmt.addLastStmt(hir.callStmt(func_s, args));
    return stmt;
  }

  ExpStmt io_end() {
    return hir.callStmt(func_e, hir.irList());
  }

  /**
   * These parameterss is followed by macro "TY*" in "libI77/lio.h".
   *
   * <pre>
   #define TYADDR  1
   #define TYSHORT 2
   #define TYLONG  3
   #define TYREAL  4
   #define TYDREAL 5
   #define TYCOMPLEX  6
   #define TYDCOMPLEX 7
   #define TYLOGICAL  8
   #define TYCHAR 9
   #define TYSUBR 10
   #define TYINT1 11
   #define TYLOGICAL1 12
   #define TYLOGICAL2 13
   #ifdef Allow_TYQUAD
   #undef TYQUAD
   #define TYQUAD 14
   #endif
   </pre>
   */
  int get_lio_type(Type rt) {
    int kind = rt.getTypeKind();
    dp("[IO] lio type: " + rt);
    switch (kind) {
    case Type.KIND_BOOL:
      return 8;
    case Type.KIND_SHORT:
      return 2;
    case Type.KIND_INT:
      return 3;
    case Type.KIND_LONG:
      return 3;
    case Type.KIND_LONG_LONG:
      return 3;

      /* fortran character */
    case Type.KIND_CHAR:
      return 9;
    case Type.KIND_U_CHAR:
      return 9;
    case Type.KIND_POINTER:
      return 9;

    case Type.KIND_ADDRESS:
      return 1;

    case Type.KIND_FLOAT:
      return 4;
    case Type.KIND_DOUBLE:
      return 5;

    case Type.KIND_VECTOR:
      return 0;
    case Type.KIND_SUBP:
      return 10;// subr
    }

    if(rt == fTypeUtil.getComplexDoubleStructType()){
      return 7; // double complex
    }
    else if(rt == fTypeUtil.getComplexStructType()){
      return 6; // complex
    }

    error("unknown I/O type: " + rt + "(" + rt.getTypeKind() + ")");
    return 0;
    /*
     * <pre>

    case Type.KIND_UNDEF:
    case Type.KIND_UNSIGNED_LOWER_LIM :
    case Type.KIND_ENUM :
    case Type.KIND_UNION:
    case Type.KIND_INT_UPPER_LIM    :
    case Type.KIND_FLOAT_LOWER_LIM  :
    case Type.KIND_OFFSET  :
    case Type.KIND_FLOAT_UPPER_LIM  :
    case Type.KIND_STRING           :
    case Type.KIND_BASE_LIM         :
    case Type.KIND_U_CHAR           :
    case Type.KIND_U_SHORT          :
    case Type.KIND_U_INT            :
    case Type.KIND_U_LONG           :
    case Type.KIND_U_LONG_LONG      :
    case Type.KIND_DEFINED :
    case Type.KIND_VOID    :
    case Type.KIND_LONG_DOUBLE      :
    </pre>
     */
  }

  // read/write n
  ExpStmt io_do(Node n) {
    /*
     * TODO: exp => ArgParamExp
     */
    IrList args = hir.irList();
    Exp exp = n.makeExp();
    Exp len = null;
    Type type = exp.getType();
    Type et = null;

    if(exp instanceof FortranCharacterExp){
      len = ((FortranCharacterExp) exp).getLength();
      exp = ((FortranCharacterExp) exp).getBody();
      type = exp.getType();
    }
    if(is_listed){
      // list type
      Type rt;
      if(type instanceof VectorType){
        rt = type;
        while (rt instanceof VectorType){
          rt = ((VectorType) rt).getElemType();
        }
      }
      else{
        rt = type;
      }
      args.add(fHirUtil.makeArgAddr(
        fESMgr.getCurrentStmt(),
        fHirUtil.makeLongConstNode(get_lio_type(rt))));////
    }

    /////////////////////////////////////////////////////
    // size
    if(type instanceof VectorType && !fTypeUtil.isFortranCharacterType(type)){      Exp ne = ((VectorType) type).getElemCountExp();
      et = ((VectorType) type).getElemType();
      while (et instanceof VectorType){
        ne = hir.exp(HIR.OP_MULT, ne, ((VectorType) et).getElemCountExp());
        et = ((VectorType) et).getElemType();
      }
      args.add(fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(), ne));
    }
    else{
      args.add(
        fHirUtil.makeArgAddr(
          fESMgr.getCurrentStmt(),
          fHirUtil.makeLongConstNode(1)));/////
    }

    /////////////////////////////////////////////////////
    // target pointer
    if(n instanceof ComplexConstNode){
      // complex
      ComplexConstNode cc = (ComplexConstNode) n;
      args.add(cc.makeArgAddr(fESMgr.getCurrentStmt()));
    }
    else{
      // args.add(n.makeArgAddr(fESMgr.getCurrentStmt()));
      // dp("[IO] io_do#output: " + exp);
      //args.add(fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(), exp));
      if(read_or_write_char == "r"){
        if(exp instanceof ComplexExp){
          if(n instanceof Token){
            Var var = (Var) fHir.getDeclManager().searchOrAddVar(
              ((Token) n).getLexem());
            if(var.getSymType() instanceof PointerType){
              args.add(hir.varNode(var));
            }
            else{
              args.add(hir.exp(HIR.OP_ADDR, hir.varNode(var)));
            }
          }
          else{
            fHir.printMsgError("Unsupport type read statement");
          }
        }
        else{
          args.add(hir.exp(HIR.OP_ADDR, exp));
        }
      }
      else{
        args.add(fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(), exp));
      }
    }

    ///////////////////////////////////////////////////
    // target size
    if(et != null){
      args.add(fHirUtil.makeIntConstNode((int) et.getSizeValue()));
    }
    else if(len != null){
      args.add(len);
    }
    else{
      args.add(fHirUtil.makeIntConstNode((int) type.getSizeValue()));
    }
    
    return hir.callStmt(func_do, args);
  }

  Stmt dolist(Node n) {
    // dl.print(0,"dolist =>");
    // stmt.addGeneratedStmt();
    if(n instanceof DoListNode){
      // do list
      FirList body = (FirList) ((DoListNode) n).getLeft();
      Quad q = (Quad) ((DoListNode) n).getRight();
      Exp v = q.getLeft().makeExp();
      Exp init_val = q.getRight().makeExp();
      Exp cond = q.getExtra().makeExp();
      Exp s;

      if(q.getLast() == null){
        s = fHirUtil.makeConstInt1Node();
      }
      else{
        s = q.getLast().makeExp();
      }

      Stmt forbody = null;
      {
        FStmt inner_for_stmt = new FStmt(0, fHir);
        FStmt prev_stmt = fESMgr.getCurrentStmt();
        fESMgr.setCurrentStmt(inner_for_stmt);

        forbody = dolist(body);

        fESMgr.setCurrentStmt(prev_stmt);
        if(inner_for_stmt.generatedStmts != null){
          inner_for_stmt.stmt = forbody;
          forbody = inner_for_stmt.getResult();
        }
      }
      return hir.forStmt(
        fHirUtil.makeAssignStmt(v, init_val),
        hir.exp(HIR.OP_CMP_LE, v, cond),
        forbody,
        fHirUtil.makeAssignStmt(v, hir.exp(HIR.OP_ADD, v, s)));
    }
    else if(n instanceof FirList){
      Iterator it = ((FirList) n).iterator();
      BlockStmt bstmt = hir.blockStmt(null);
      while (it.hasNext()){
        bstmt.addLastStmt(dolist((Node) it.next()));
      }
      return bstmt;
    }
    else{
      return io_do(n);
    }
  }

  Stmt open() {
    IrList args = hir.irList();
    Exp func_open = fHirUtil.makeSubpNode("f_open", Parser.INTEGER,
                                          hir.irList(), Sym.SYM_EXTERN);

    Type type = get_type_olist();
    String name = fESMgr.getTempName() + "_olist_";
    Var v = sym.defineVar(name.intern(), type);
    v.setStorageClass(Var.VAR_STATIC);
    args.add(hir.exp(HIR.OP_ADDR, hir.varNode(v)));

    //// init args
    // oerr
    fESMgr.addStmt(fHirUtil.qassign(
      v, "oerr",
      fHirUtil.makeIntConstNode(read_err ? 1 : 0)));
    // ounit
    fESMgr.addStmt(fHirUtil.qassign(v, "ounit", unit_));
    // ofnm;
    fESMgr.addStmt(fHirUtil.qassign(v, "ofnm", i_file));
    // ofnmlen
    fESMgr.addStmt(fHirUtil.qassign(v, "ofnmlen", i_filelen));
    // osta
    fESMgr.addStmt(fHirUtil.qassign(v, "osta", i_status));
    // oacc
    fESMgr.addStmt(fHirUtil.qassign(v, "ofm", i_form));
    // orl
    fESMgr.addStmt(fHirUtil.qassign(v, "orl", i_recl));
    // oblnk
    fESMgr.addStmt(fHirUtil.qassign(v, "oblnk", i_blank));

    return hir.callStmt(func_open, args);
  }

  Stmt close() {
    IrList args = hir.irList();
    Exp func_close = fHirUtil.makeSubpNode(
      "f_clos", Parser.INTEGER,
      hir.irList(), Sym.SYM_EXTERN);

    Type type = get_type_cllist();
    String name = fESMgr.getTempName() + "_cllist_";
    Var v = sym.defineVar(name.intern(), type);
    v.setStorageClass(Var.VAR_STATIC);
    java.util.List list = new java.util.LinkedList();
    args.add(hir.exp(HIR.OP_ADDR, hir.varNode(v)));

    //// init args
    // cerr
    fESMgr.addStmt(fHirUtil.qassign(v, "cerr",
                                    fHirUtil.makeIntConstNode(read_err ? 1 : 0)));
    // cunit
    fESMgr.addStmt(fHirUtil.qassign(v, "cunit", unit_));
    // csta
    fESMgr.addStmt(fHirUtil.qassign(v, "csta", i_status));

    return hir.callStmt(func_close, args);
  }

  Stmt inquire() {
    IrList args = hir.irList();
    Exp func_open = fHirUtil.makeSubpNode("f_inqu", Parser.INTEGER,
                                          hir.irList(), Sym.SYM_EXTERN);

    Type type = get_type_inlist();
    String name = fESMgr.getTempName() + "_inlist_";
    Var v = sym.defineVar(name.intern(), type);
    v.setStorageClass(Var.VAR_STATIC);
    args.add(hir.exp(HIR.OP_ADDR, hir.varNode(v)));

    // inerr
    fESMgr.addStmt(fHirUtil.qassign(v, "inerr",
                                    fHirUtil.makeIntConstNode(read_err ? 1 : 0)));
    // inunit;
    fESMgr.addStmt(fHirUtil.qassign(v, "inunit", unit_));
    // infile;
    fESMgr.addStmt(fHirUtil.qassign(v, "infile", i_file));
    // infilen;
    if(i_filelen != null){
      fESMgr.addStmt(fHirUtil.qassign(v, "infilen", i_filelen));
    }
    // inex;
    fESMgr.addStmt(fHirUtil.qassign(v, "inex", i_exist));
    // inopen;
    fESMgr.addStmt(fHirUtil.qassign(v, "inopen", i_opened));
    // innum;
    fESMgr.addStmt(fHirUtil.qassign(v, "innum", i_number));
    // innamed;
    fESMgr.addStmt(fHirUtil.qassign(v, "innamed", i_named));
    // inname;
    fESMgr.addStmt(fHirUtil.qassign(v, "inname", i_name));
    // innamlen;
    if(i_namelen != null){
      fESMgr.addStmt(fHirUtil.qassign(v, "innamlen", i_namelen));
    }
    // inacc;
    fESMgr.addStmt(fHirUtil.qassign(v, "inacc", i_access));
    // inacclen;
    if(i_accesslen != null){
      fESMgr.addStmt(fHirUtil.qassign(v, "inacclen", i_accesslen));
    }
    // inseq;
    fESMgr.addStmt(fHirUtil.qassign(v, "inseq", i_sequential));
    // inseqlen;
    if(i_sequentiallen != null){
      fESMgr.addStmt(fHirUtil.qassign(v, "inseqlen", i_sequentiallen));
    }
    // indir;
    fESMgr.addStmt(fHirUtil.qassign(v, "indir", i_direct));
    // indirlen;
    if(i_directlen != null){
      fESMgr.addStmt(fHirUtil.qassign(v, "indirlen", i_directlen));
    }
    // inform;
    fESMgr.addStmt(fHirUtil.qassign(v, "inform", i_form));
    // informlen;
    if(i_formlen != null){
      fESMgr.addStmt(fHirUtil.qassign(v, "informlen", i_formlen));
    }
    // infmt;
    fESMgr.addStmt(fHirUtil.qassign(v, "infmt", i_formatted));
    // infmtlen;
    if(i_formattedlen != null){
      fESMgr.addStmt(fHirUtil.qassign(v, "infmtlen", i_formattedlen));
    }
    // inunf;
    fESMgr.addStmt(fHirUtil.qassign(v, "inunf", i_unformatted));
    // inunflen;
    if(i_unformattedlen != null){
      fESMgr.addStmt(fHirUtil.qassign(v, "inunflen", i_unformattedlen));
    }
    // inrecl;
    fESMgr.addStmt(fHirUtil.qassign(v, "inrecl", i_recl));
    // innrec;
    fESMgr.addStmt(fHirUtil.qassign(v, "innrec", i_nextrec));
    // inblank;
    fESMgr.addStmt(fHirUtil.qassign(v, "inblank", i_blank));
    // inblanklen;
    if(i_blanklen != null){
      fESMgr.addStmt(fHirUtil.qassign(v, "inblanklen", i_blanklen));
    }

    return hir.callStmt(func_open, args);
  }

  Stmt others(String ident) {
    IrList args = hir.irList();
    Exp func = fHirUtil.makeSubpNode(ident.intern(), Parser.INTEGER,
                                     hir.irList(), Sym.SYM_EXTERN);

    Type type = get_type_alist();
    String name = fESMgr.getTempName() + "_alist_";
    Var v = sym.defineVar(name.intern(), type);
    v.setStorageClass(Var.VAR_STATIC);
    args.add(hir.exp(HIR.OP_ADDR, hir.varNode(v)));

    //// init args
    // cerr
    fESMgr.addStmt(fHirUtil.qassign(
      v,
      "aerr",
      fHirUtil.makeIntConstNode(read_err ? 1 : 0)));

    // cunit
    fESMgr.addStmt(fHirUtil.qassign(v, "aunit", unit_));
    // csta
    // not supported ??

    return hir.callStmt(func, args);
  }

  void dp(String str) {
    fHir.dp(str);
  }

  void error(String msg) {
    fHir.printMsgFatal(msg);
  }
}

