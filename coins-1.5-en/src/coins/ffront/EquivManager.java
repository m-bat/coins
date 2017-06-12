/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
  Equivalence values Manager

 */
public class EquivManager extends BaseManager{

  Map id_to_variable_table_;         /// id -> union var
  List equivalence_variables_list_;  /// ((A B C) (D E) ...)
  Map in_block_var_table_;           /// id -> block_id

  SymTable global_symbol_table_, current_sym_table_;
  String unit_name_;
  DeclManager   fDeclMgr;
  CommonManager fCommonMgr;

  // constructor
  EquivManager(FirToHir fth, CommonManager cmgr, DeclManager declm){
    super(fth);
    
    id_to_variable_table_       = new HashMap();
    in_block_var_table_         = new HashMap();
    equivalence_variables_list_ = new LinkedList();
    
    fDeclMgr = declm;
    fCommonMgr = cmgr;
  }

  ////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////

  /// make Exp associated with "ident"
  Exp makeExp(String ident){
    Var v = (Var)id_to_variable_table_.get(ident);
    Exp exp;
    String block_var_id;
    if((block_var_id = (String)in_block_var_table_.get(ident)) != null){
      Elem e1 = fTypeUtil.searchElem(
        (fCommonMgr.unit_name_ + "_" + block_var_id).intern(), v.getSymType());        
      Elem e2 = fTypeUtil.searchElem(ident, e1.getSymType());
      Elem e3 = fTypeUtil.searchElem("val", e2.getSymType());
      exp =
        hir.qualifiedExp(
          hir.qualifiedExp(
            hir.qualifiedExp(hir.varNode(v),
                             hir.elemNode(e1)),
            hir.elemNode(e2)),
          hir.elemNode(e3));
    }
    else{
      Elem ue = fTypeUtil.searchElem(ident, v.getSymType());
      Elem se = fTypeUtil.searchElem("val", ue.getSymType());
      Exp ve  = hir.varNode(v);
      Exp qe  = hir.qualifiedExp(ve, hir.elemNode(ue));
      exp     = hir.qualifiedExp(qe, hir.elemNode(se));
    }

    dp("make Equivalence var exp: " + ident);

    if(fTypeUtil.isComplexType(exp.getType())){
      exp = fHirUtil.makeComplexExp(exp);
    }
    return exp;
  }

  /// 
  Sym symEquivVariable(String ident){
    Var v = (Var)id_to_variable_table_.get(ident);
    String block_var_id;
    if((block_var_id = (String)in_block_var_table_.get(ident)) != null){
      Elem e1 = fTypeUtil.searchElem(
        (fCommonMgr.unit_name_ + "_" + block_var_id).intern(), v.getSymType());        
      Elem e2 = fTypeUtil.searchElem(ident, e1.getSymType());
      Elem e3 = fTypeUtil.searchElem("val", e2.getSymType());
      return e3;
    }
    else{
      Elem ue = fTypeUtil.searchElem(ident, v.getSymType());
      Elem se = fTypeUtil.searchElem("val", ue.getSymType());
      return se;
    }
  }

  /// "ident" variable is equivalence?
  boolean isEquivVariable(String ident){
    return id_to_variable_table_.get(ident) != null;
  }

  ////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////
  void processEquiv(String unit_name){
    unit_name_ = ("p_unit_" + unit_name).intern();
    makeEquivVarsList();

    // make eq table
    Iterator it = equivalence_variables_list_.iterator();
    
    int i=0;
    while(it.hasNext()){
      EquivGroup eg = (EquivGroup)it.next();
      i++;
      String ident  = "_equiv_in_" + unit_name_ + i;
      Type equivType;
      Var v;
      String block_var_id;
      if((block_var_id = eg.searchBlockVariable()) != null){
        UnionType global_block_type = fCommonMgr.getGlobalBlockVarType(block_var_id);
        String elem_id = (fCommonMgr.unit_name_ + "_" + block_var_id).intern();
        equivType = eg.makeEquivTypeUnder(global_block_type.getSymTable());
        global_block_type.addElem(sym.defineElem(elem_id, equivType));
        eg.addIDsToTable(fCommonMgr.getGlobalBlockVar(block_var_id));
        global_block_type.finishUnionType(true);
      }
      else{
        equivType = eg.makeEquivTypeUnder(symRoot.symTableCurrent);
        v = sym.defineVar(ident, equivType);
        eg.addIDsToTable(v);
      }

      dp("Equivalence Variable: " + ident);
    }
  }

  /**
    Equivalence group.

    <pre>
    ex) (A B) (B C) is group "(A B C)"

    each of the equivalence variables has "height"
    (in this case, all var's heights are 0)
    
    ex) (A(2), B(3), C) (all vars is real type)
        A's heigth is 4.
        B's height is 0.
        C's        is 8. (real type 1 word is 4 bytes).

    as C notation.
    
    union{
      struct{
        char padding[4]
        real var;
      }A;
      struct{
        real var;
      }B;
      struct{
        char padding[4]
        real var;
      }C;
    }equiv1;

    ex) If Common variable and Equivalence variable mix, modify global common
        block union(region)type like this:
    f:
    common /B1/ a,b
    equivalence (a,x), (b,y)

    => Global Scope
    union{
      struct{
        real a;
        real b;
      } unit_name_A;

      union{
        struct{
          real var;
        }x;
      }unit_name_A_x;
    
      union{
        struct{
          real padding[1];
          real var;
        }y;
      }unit_name_A_y;
    
    } BlockA;
    
    access to x:
      BlockA.unit_name_A_a.x.var;
    </pre>
   */
  class EquivGroup{
    Map table_; // id <=> EquivElem

    /**
      build equiv group with some equivalence variables
     */
    EquivGroup(FirList vars){
      table_ = new HashMap();
      addElems(vars, 0);
    }

    void addIDsToTable(Var v){
      Iterator it = table_.keySet().iterator();
      String block_id = null;
      while(it.hasNext()){
        String ident = (String)it.next();
        if(!fCommonMgr.isBlockVariable(ident)){
          id_to_variable_table_.put(ident, v);
        }
        else{
          block_id = ident;
        }
      }
      if(block_id != null){
        it = table_.keySet().iterator();
        while(it.hasNext()){
          String ident = (String)it.next();
          if(!fCommonMgr.isBlockVariable(ident)){
            in_block_var_table_.put(ident, block_id);
          }
        }
      }
    }
    
    
    /**
      add new equivalence group. new group must be related with this equiv group
     */
    void addElems(FirList vars){
      Iterator it = vars.iterator();
      while(it.hasNext()){
        Node n = (Node)it.next();
        if(isContainNode(n)){
          // fth_.p(" // " + getHeight(getNodeLexem(n)));
          addElems(vars, getHeight(getNodeLexem(n)) - getNodeHeight(n));
          break;
        }
      }
    }
    /**
      with base
     */
    void addElems(FirList vars, int base){
      Iterator it = vars.iterator();
      // fth_.p("base : " + base);
      while(it.hasNext()){
        Node n = (Node)it.next();
        if(isContainNode(n)){
          //K must check height
        }
        else{
          String ident  = getNodeLexem(n);
          Type   type   = getNodeType(n);
          int    height = getNodeHeight(n);

          //fth_.p("// " + height + " : " + ident);
          table_.put(ident, new EquivElem(ident, type, height + base));
        }
      }
    }
    
    int getNodeHeight(Node n){
      String id = getNodeLexem(n);
      boolean is_block_var = fCommonMgr.isBlockVariable(id);
      
      if(n instanceof Pair){
        // this node is array
        Type type = getNodeType(n);
        if(type == null){
          printMsgFatal("subscript on not dimension variable: " + id);
        }
        
        if(type instanceof VectorType){
          VectorType vt  = (VectorType)type;
          long elem_size = vt.getElemType().getSizeValue();
          if(elem_size == 0){
            printMsgFatal("unknown table size");
          }
          
          FirList dims = (FirList)((Pair)n).getRight();
          Iterator dit = dims.iterator();
          int [] dimcount  = new int[7];
          int [] dimsize   = new int[7];
          int    d = 0; // depth
          int    h = 0; // height

          type = vt;
          while(dit.hasNext()){
            vt = (VectorType)type;
            Token t = (Token)dit.next();
            dimsize [d] = (int)vt.getElemType().getSizeValue();
            dimcount[d] = (int)(t.getConstValue().intValue() - vt.getLowerBound());
            
            type = vt.getElemType();
            d++;
          }
          for(int i=0;i<d;i++){
            h += dimcount[i] * dimsize[d-i-1];
          }

          if(is_block_var){
            h += fCommonMgr.getHeightOnBlockVar(id);
          }
          return h;
        }
        else{
          printMsgFatal("unknown equivalence target type variable: " + id);
        }
      }
      else{
        // not dimension
        if(is_block_var){
          return fCommonMgr.getHeightOnBlockVar(id);
        }
        else{
          return 0;
        }
      }
      return 0;
    }
    
    int getHeight(String ident){
      EquivElem e = (EquivElem)table_.get(ident);
      return e.height_;
    }
    
    int getMaxHeight(){
      int max = 0;
      Iterator it = table_.keySet().iterator();
      while(it.hasNext()){
        EquivElem ee = (EquivElem)table_.get(it.next());
        max = max < ee.height_ ? ee.height_ : max;
      }
      return max;
    }

    /**
      If contain block(common) variable, return this indentifier.
      If not, return null.
     */
    String searchBlockVariable(){
      Iterator it = table_.keySet().iterator();
      while(it.hasNext()){
        EquivElem ee = (EquivElem)table_.get(it.next());
        if(fCommonMgr.isBlockVariable(ee.ident_)){
          return ee.ident_;
        }
      }
      return null;
    }
    
    Type makeEquivTypeUnder(SymTable c_table){
      SymTable b_table = null;
      UnionType utype;
      SymTable current_table = symRoot.symTableCurrent;
      // change symTableCurrent
      symRoot.symTableCurrent = c_table;
      {
        Iterator it   = table_.keySet().iterator();
        IrList   list = hir.irList();
        int      max  = getMaxHeight();

        while(it.hasNext()){
          EquivElem ee = (EquivElem)table_.get(it.next());
          if(fDeclMgr.isBlockVariable(ee.ident_)){
            // height check(must be max height)
            if(ee.height_ != max){
              printMsgFatal("Can't extend common block by equivalence: " + ee.ident_);
            }
          }
          else{
            list.add(addUnionElem(ee, max, c_table));
          }
        }
        // c_table.reopenSymTable(c_table);

        Sym tag = c_table.generateTag();
        utype = sym.unionType(list, tag);
        tag.setSymType(utype);

        utype.setSymTable(c_table);
        tag.setSymType(utype);
      }
      // restore symTableCurrent
      symRoot.symTableCurrent = current_table;
      utype.finishUnionType(true);
      return utype;
    }
    
    Elem addUnionElem(EquivElem ee, int max, SymTable c_table){
      IrList list = hir.irList();
      SymTable current_table = symRoot.symTableCurrent;
      SymTable b_table = symRoot.symTableCurrent.pushSymTable(null);
      ////
      int h = max - ee.height_;
      if(h != 0){
        // add padding
        VectorType vt   = sym.vectorType(fTypeUtil.getCharType(), h);
        list.add(sym.defineElem("padding", vt));
      }
      list.add(sym.defineElem("val", ee.type_));

      // remove symbol already registed
      Sym s = current_table.searchLocal(ee.ident_, Sym.KIND_VAR);
      if(s != null){
        s.remove();
      }
      
      ////
      symRoot.symTableCurrent.reopenSymTable(current_table);
      
      Sym tag = c_table.generateTag();
      StructType stype = sym.structType(list, tag);
      stype.setSymTable(b_table);
      tag.setSymType(stype);
      
      return sym.defineElem(ee.ident_, stype);
    }
    
    /**
      (A B C) + B => true
     */
    boolean isContainNode(Node n){
      String ident = getNodeLexem(n);
      return table_.get(ident) != null;
    }
    /**
      (A B C) + (B X) => true
     */
    boolean isContainNodes(FirList vars){
      Iterator it = vars.iterator();
      while(it.hasNext()){
        Node n = (Node)it.next();
        if(isContainNode(n)){
          return true;
        }
      }
      return false;
    }

    /**
      Node must be type Token or Pair.
     */
    String getNodeLexem(Node n){
      return (n instanceof Token) ?
        ((Token)n).getLexem() :
        ((Token)((Pair)n).getLeft()).getLexem();
    }
    
    Type getNodeType(Node n){
      String id = getNodeLexem(n);
      Sym    s;

      if(fDeclMgr.isBlockVariable(id)){
        s = fDeclMgr.symBlockVariable(id);
      }
      else{
        s = fDeclMgr.searchOrAddVar(id);
      }
      return s.getSymType();
    }
    
    public String toString(){
      StringBuffer ret = new StringBuffer("--\n");
      Iterator it = table_.keySet().iterator();
      while(it.hasNext()){
        EquivElem ee = (EquivElem)table_.get(it.next());
        ret.append("  * ");
        ret.append(ee.toString() + "/" + (getMaxHeight() - ee.height_) + "\n");
      }
      return ret.toString();
    }
    
    /**
      equiv element class.

      this class has ident_, type_, height_ attrs.
     */
    class EquivElem{
      public String ident_;
      public Type   type_;
      public int    height_;
      EquivElem(String id, Type t, int h){
        dp("EquivElem: " + id);
        ident_  = id;
        type_   = t;
        height_ = h;
      }
      public String toString(){
        return "(" + ident_ + " as " + type_ + "(" + height_ + "))";
      }
    }
  }

  
  /**
    make eq_list_

    <pre>
    f7Sym.equivList : ((A B) (B C) (D E F) ...)
    =>
    eq_list_        : ((A B C) (D E F) ...)

    id_list_table_ => (A => (A B C), B => (A B C), ...)
    
    A,B, ... : Pair(Token[Ident] , subsc)
    </pre>
   */
  void makeEquivVarsList(){
    Iterator eqst_it = fHir.f7Sym.equivList.iterator();
    while(eqst_it.hasNext()){
      FirList eql_line = (FirList)eqst_it.next();
      Iterator eqgr_it = eql_line.iterator();
      
      while(eqgr_it.hasNext()){
        FirList  eq_grp  = (FirList)eqgr_it.next();
        EquivGroup eg    = findAndAddToEquivGroup(eq_grp);
        if(eg != null){
          equivalence_variables_list_.add(eg);
        }
      }
    }
  }

  /**
    return equivalence group contains a node of n.
    if no equivalence group, return new equivalence group contains n.
   */
  EquivGroup findAndAddToEquivGroup(FirList n){
    Iterator it = equivalence_variables_list_.iterator();
    while(it.hasNext()){
      EquivGroup eg = (EquivGroup)it.next();
      if(eg.isContainNodes(n)){
        eg.addElems(n);
        return null;
      }
    }
    // not found
    return new EquivGroup(n);
  }  
}


