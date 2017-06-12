/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import coins.ir.IrList;
import coins.ir.hir.Exp;
import coins.sym.Elem;
import coins.sym.StructType;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.UnionType;
import coins.sym.Var;
import coins.sym.VectorType;

/**
  Manage Common variables
<pre>
  Block variables storage image in C
  
  union{
    struct{
      int a;
      int b;
    } unit_name_A;
    struct{
      int c;
      int d;
    } unit_name_B;
  } BlockNameA;
  ...
</pre>
 */

public class CommonManager extends BaseManager{
  Map id_table_; // id <-> block_name
  Map bk_table_; // block_name <-> ((id1, id2, ...) ..)
  
  SymTable g_sym_table_, c_sym_table_;
  String unit_name_;

  // Map blockType = new HashMap();
  DeclManager fDeclMgr;
  
  CommonManager(FirToHir fth, DeclManager declm){
    super(fth);
    id_table_ = new HashMap();
    bk_table_ = new HashMap();

    fDeclMgr = declm;
  }
  
  
  /**
    Process Common statements in an program unit.
    
   */
  void processCommon(String unit_name){
    Iterator it;
    g_sym_table_ = symRoot.symTableRoot;
    c_sym_table_ = symRoot.symTableCurrent;
    symRoot.symTableCurrent.reopenSymTable(g_sym_table_);

    unit_name_ = ("p_unit_" + unit_name).intern();

    // into the global scope
    
    it = fHir.f7Sym.commonList.iterator();

    // make bk_table_
    while(it.hasNext()){
      // ((Node)it.next()).print(0,"process common : ");
      Pair p = (Pair)it.next();
      Token t = (Token)p.getLeft();
      String block_name = (t == null) ? "__ANONYMOUS_BLOCK__" : "BLOCK__" + t.getLexem() + "__";
      block_name = block_name.intern();
      
      FirList block_vars = (FirList)p.getRight();
      FirList registed_vars = (FirList)bk_table_.get(block_name);
      
      if(registed_vars != null){
        registed_vars.addAll(block_vars);
      }
      else{
        bk_table_.put(block_name, block_vars);
      }
    }

    // make id_table_
    it = bk_table_.keySet().iterator();
    while(it.hasNext()){
      String block_name = (String)it.next();
      FirList vars      = (FirList)bk_table_.get(block_name);
      // p("blk  :" + block_name);
      // vars.print(0,"vars :");
      makeBlockStructure(block_name, vars);
    }

    // return to local scope
    symRoot.symTableCurrent.reopenSymTable(c_sym_table_);
  }

  
  void makeBlockStructure(String name, FirList list){
    Iterator it  = list.iterator();
    StructType bk_type = null;
    IrList elems = hir.irList();
    Sym tag;
    SymTable base_table;
    UnionType union_type;

    // search block variable
    Sym block_parent = g_sym_table_.searchLocal(name, Sym.KIND_VAR);
    if(block_parent == null){
      // first block definition
      tag = g_sym_table_.generateTag(("tag_" + name).intern());
      union_type = sym.unionType(hir.irList(), tag);
      tag.setSymType(union_type);
      union_type.setSymTable(g_sym_table_);
      
      base_table = g_sym_table_.pushSymTable(null);
      g_sym_table_.popSymTable();
      
      union_type.setSymTable(base_table);

      block_parent = defineGlobalVal(name, union_type);
    }
    else{
      // already defined
      // p("---> " + block_parent.toStringDetail());
      union_type = (UnionType)block_parent.getSymType();
      base_table  = union_type.getSymTable();
    }
    
    g_sym_table_.reopenSymTable(base_table);

    base_table.pushSymTable(null);
    while(it.hasNext()){
      Pair  p = (Pair)it.next();
      Token t = (Token)p.getLeft();
      FirList dims = (FirList)p.getRight();
      
      String ident = t.getLexem();
      
      Sym s = c_sym_table_.searchLocal(ident, Sym.KIND_VAR);
      Type type;
      if(s != null){
        type = s.getSymType();
        s.remove();
      }
      else{
        type = fDeclMgr.getImplicitType(ident);
      }

      if(dims != null){
        if(s != null){
          Type vtype = s.getSymType();
          if(vtype instanceof VectorType && 
             !fTypeUtil.isFortranCharacterVectorType((VectorType)vtype)){
            printMsgFatal("Can't put this array in block. Already defined: " + ident);
            continue;
          }
        }
        type = fTypeUtil.getArrayType(type, dims, fDeclMgr);
      }
      
      elems.add(sym.defineElem(ident, type));
      id_table_.put(ident, block_parent);
      
      dp("common val : " + ident + " as " + type);
    }
    g_sym_table_.reopenSymTable(base_table);
    
    
    tag = base_table.generateTag();
    bk_type = sym.structType(elems, tag);
    tag.setSymType(bk_type);
    tag.setFlag(Sym.FLAG_COMMON, true);
    
    bk_type.setSymTable(base_table);
    
    // p("" + name + "::" + unit_name_);
    union_type.addElem(sym.defineElem(unit_name_, bk_type));
    union_type.finishUnionType(false);
    g_sym_table_.reopenSymTable(g_sym_table_);
  }

  Var defineGlobalVal(String ident, Type type){
    Var v = null;
    v = (Var)g_sym_table_.define(ident, Sym.KIND_VAR , null);
    v.setSymType(type);
    v.setVisibility(Sym.SYM_PUBLIC);
    v.setStorageClass(Var.VAR_STATIC);
    return v;
  }

  /**
    @param blocked identifier
   */
  Exp makeExp(String ident){
    Var bkvar = (Var)id_table_.get(ident);
    if(bkvar == null){
      printMsgFatal("no such block variable : " + ident);
      return null;
    }
    Elem belem = fTypeUtil.searchElem(unit_name_, bkvar.getSymType());
    Elem elem  = fTypeUtil.searchElem(ident,      belem.getSymType());

    Exp exp = hir.qualifiedExp(
      hir.qualifiedExp(hir.varNode(bkvar),
                        hir.elemNode(belem)),
      hir.elemNode(elem));
    
    dp("Common Var: "+ ident);
    dp("Common exp: "+ exp);
    
    if(fTypeUtil.isComplexType(exp.getType())){
      exp = fHirUtil.makeComplexExp(exp);
    }
    
    return exp;
  }
  
  /**
    @return true if ident is block variable
    @return false ident is not block variable
   */
  boolean isBlockVariable(String ident){
    // p("isBlockVariable ? " + ident + " : " + (id_table_.get(ident) != null));
    return id_table_.get(ident) != null;
  }

  Sym symBlockVariable(String ident){
    Var bkvar = (Var)id_table_.get(ident);
    if(bkvar == null){
      printMsgFatal("no such block variable : " + ident);
      return null;
    }
    Elem belem = fTypeUtil.searchElem(unit_name_, bkvar.getSymType());
    Elem elem  = fTypeUtil.searchElem(ident,      belem.getSymType());
    
    return elem;
  }

  UnionType getGlobalBlockVarType(String ident){
    Var bkvar = (Var)id_table_.get(ident);
    if(bkvar == null){
      printMsgFatal("no such block variable : " + ident);
      return null;
    }
    return ((UnionType)bkvar.getSymType());
  }

  Var getGlobalBlockVar(String ident){
    Var bkvar = (Var)id_table_.get(ident);
    if(bkvar == null){
      printMsgFatal("no such block variable : " + ident);
      return null;
    }
    return bkvar;
  }
  
  int getHeightOnBlockVar(String ident){
    int height = 0;
    Var bkvar = (Var)id_table_.get(ident);
    if(bkvar == null){
      printMsgFatal("no such block variable: " + ident);
      return -1;
    }
    Elem belem  = fTypeUtil.searchElem(unit_name_, bkvar.getSymType());
    IrList list = ((StructType)belem.getSymType()).getElemList();
    Iterator it = list.iterator();
    
    while(it.hasNext()){
      Elem elem = (Elem)it.next();
      if(elem.getName() == ident){
        return height;
      }
      height += elem.getSymType().getSizeValue();
    }
    
    printMsgFatal("no such block variable: " + ident);
    return -1;
  }

  void setInitialValue(String lexem, Exp exp){
    
    
  }

  void commitInitialValue(){
    //SetDataStmt datacode = (SetDataStmt)hir.setDataStmt(hir.varNode(v), e);
    //((Program)hirRoot.programRoot).addInitiationStmt((Stmt)datacode.copyWithOperands());
  }
}




