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

import coins.HirRoot;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpListExp;
import coins.ir.hir.ExpListExpImpl;
import coins.ir.hir.NullNode;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;

public class ProcessData{
  FirToHir fHir;
  HirRoot  hirRoot;

  TypeUtility     fTypeUtil;
  DeclManager     fDeclMgr;
  HirUtility      fHirUtil;

  ProcessData(FirToHir fth){
    fHir = fth;
    hirRoot = fHir.getHirRoot();

    fDeclMgr  = fHir.getDeclManager();
    fTypeUtil = fHir.getTypeUtility();
    fHirUtil  = fHir.getHirUtility();
  }

  void process(){
    Iterator declit = fDeclMgr.f7Sym.dataList.iterator();

    // data statements loop
    while(declit.hasNext()){
      Iterator seqit = ((FirList)declit.next()).iterator();
      // data seq loop
      while(seqit.hasNext()){
        Pair p = (Pair)seqit.next();
        Iterator varit = ((FirList)p.getLeft()).iterator();
        Iterator valit = ((FirList)p.getRight()).iterator();
        // make multiple lists
        FirList vals = makeDataVals(valit);
        valit = vals.iterator();
        //
        while(varit.hasNext()){
          // normal token
          Node var = (Node)varit.next();

          if(var instanceof Token){
            // data var/val/
            init_token((Token)var, valit);
          }
          else{
            Pair pvar = (Pair)var;
            if(pvar.getLeft() instanceof Token){
              // data var(x)/val/
              init_subscr((Token)pvar.getLeft(), (FirList)pvar.getRight(), valit);
            }
            else{
              // data (dospec)/vals.../
              init_dolist((FirList)pvar.getLeft(), (Quad)pvar.getRight(), valit);
            }
          }
        }
      }
    }
  }

  private void init_token(Token var, Iterator valit){
    dp("** ProcessData/Token: " + var);
    String ident = var.getLexem();
    Var  v       = fDeclMgr.searchOrAddVar(ident);
    Type type    = v.getSymType();
    Exp  val_exp;

    if(type instanceof VectorType && !fTypeUtil.isFortranCharacterType(type)){
      val_exp = makeArrayInitializeList((VectorType)type, valit, null);
      val_exp.setType(v.getSymType());
    }
    else{
      val_exp = ((Node)valit.next()).makeExp();
    }
    dp("ProcessData/Token/val_exp: " + val_exp);
    fHir.getDeclManager().setInitialValue(v, val_exp, ident);
  }

  private void init_subscr(Token t, FirList dims, Iterator valit){
    dp("** ProcessData/subscr: " + t);

    // array element
    List         elist = new LinkedList();
    ArrayElement ae    = new ArrayElement(t, dims, null);
    Exp          val_exp;

    Exp exp = ((Node)valit.next()).makeExp();
    
    if(exp instanceof FortranCharacterExp){
      exp = ((FortranCharacterExp)exp).getBody();
      exp = exp.getExp1();
    }
    
    ae.setExp(exp);

    HashMap map = new HashMap();
    map.put(ae.getDims(), ae.getExp());

    String ident = t.getLexem();
    Var v = fDeclMgr.searchOrAddVar(ident);
    if(v.getSymType() instanceof VectorType){
      // p("regist : " + ident);
      val_exp = makeArrayInitializeList((VectorType)v.getSymType(), null, map);
      val_exp = setArrayInitializeValue(v, val_exp, ident);
      val_exp.setType(v.getSymType());
    }
    else{
      fHir.printMsgFatal("this is not array in do list :" + ident);
    }
  }
  
  private void init_dolist(FirList do_range, Quad do_spec, Iterator valit){
    dp("** ProcessData/dolist: " + do_range);

    List elist = makeElemListFromDoList(do_range, do_spec, new HashMap());
    Iterator it = elist.iterator();
    Exp val_exp;
    Map symbol_and_val_map = new HashMap();

    // read vals
    while(it.hasNext()){
      Exp exp = ((Node)valit.next()).makeExp();
      if(exp instanceof FortranCharacterExp){
        exp = ((FortranCharacterExp)exp).getBody(); 
        exp = exp.getExp1();
      }
      ArrayElement ae = (ArrayElement)it.next();
      ae.setExp(exp);

      if(symbol_and_val_map.containsKey(ae.getLexem())){
        Map m = (Map)symbol_and_val_map.get(ae.getLexem());
        m.put(ae.getDims(), ae.getExp());
      }
      else{
        HashMap m = new HashMap();
        m.put(ae.getDims(), ae.getExp());
        symbol_and_val_map.put(ae.getLexem(), m);
      }
    }

    // regist
    Iterator sit = symbol_and_val_map.keySet().iterator();
    while(sit.hasNext()){
      String ident = (String)sit.next();
      Var v = fDeclMgr.searchOrAddVar(ident);
      if(v.getSymType() instanceof VectorType){
        // p("regist : " + ident);
        Map m = (Map)symbol_and_val_map.get(ident);
        val_exp = makeArrayInitializeList((VectorType)v.getSymType(), null, m);
        val_exp = setArrayInitializeValue(v, val_exp, ident);
        val_exp.setType(v.getSymType());
      }
      else{
        fHir.printMsgFatal("this is not array in do list :" + ident);
      }
    }
  }
  
  Exp setArrayInitializeValue(Var v, Exp val_exp, String ident){
    if(v.getInitialValue() != null){
      ExpListExp el1 = (ExpListExp)val_exp;
      ExpListExp el2 = (ExpListExp)v.getInitialValue();

      val_exp = mergeArrayInitializeList(el1, el2);

      if(val_exp == null){
        dp("el1: " + exp2str(el1));
        dp("el2: " + exp2str(el2));
        fHir.printMsgFatal("Attempt to specify second initial value for element of " + ident);
        return null;
      }
    }
    fHir.getDeclManager().setInitialValue(v, val_exp, ident);
    return val_exp;
  }


  FirList makeDataVals(Iterator valit){
    FirList vals = new FirList(fHir);
    while(valit.hasNext()){
      Node val = (Node)valit.next();
      // val.print(0, "===>");
      // fth_.p("--- " + val.getClass());

      if(val instanceof Token || val instanceof UnaryNode || val instanceof ComplexConstNode){
        vals.add(val);
      }
      else if(val instanceof Pair){
        HasConstValue v = (HasConstValue)(((Pair)val).getLeft());
        int n = v.getConstValue().intValue();
        while(n-- > 0){
          vals.add(((Pair)val).getRight());
        }
      }
      else{
        fHir.printMsgFatal("unknown data type!!!!! : " + val.getClass());
        continue;
      }
    }
    return vals;
  }

  class ArrayElement{
    String ident_;
    int idx_[] = new int[7];
    int dim_;
    Exp val_ = null;

    /**
     * ArrayElement
     *
     * ex) A(1,2) => ArrayElement("A", [1,2], ..)
     *
     * @param ident Parameter ID
     * @param dims  dimension information(include nums(1,2,..) and params(i,j,...)
     * @param env   DoList parameter environment(i=3,j=2, ...)
     */
    public ArrayElement(Token ident, FirList dims, Map env){
      ident_ = ident.getLexem();
      Iterator it = dims.iterator();
      dim_ = 0;
      while(it.hasNext()){
        Token idx = (Token)it.next();
        int i;
        if(idx.getKind() == Parser.IDENT){
          i = ((Number)env.get(idx.getLexem())).intValue();
        }
        else{
          i = ((HasConstValue)idx).getConstValue().intValue();
        }
        idx_[dim_++] = i;
      }
    }

    public String toString(){
      StringBuffer buff = new StringBuffer();
      buff.append(ident_);
      buff.append("(");
      for(int i=0;i<dim_;i++){
        buff.append(Integer.toString(idx_[i]));
        buff.append((i+1<dim_) ? "," : ")");
      }
      if(val_ != null){
        buff.append(" as "+val_);
      }
      return buff.toString();
    }

    public void setExp(Exp e){
      val_ = e;
    }

    public Exp getExp(){
      return val_;
    }

    public List getDims(){
      LinkedList list = new LinkedList();
      for(int i=0;i<dim_;i++){
        list.add(new Integer(idx_[dim_-i-1]));
      }
      return list;
    }

    public String getLexem(){ return ident_; }
  }


  List makeElemListFromDoList(FirList do_range, Quad do_spec, HashMap env){
    String do_val = ((Token)do_spec.getLeft()).getLexem();
    int beg = ((HasConstValue)do_spec.getRight()).getConstValue().intValue();
    int end = ((HasConstValue)do_spec.getExtra()).getConstValue().intValue();
    int step= (do_spec.getLast() == null) ?
    1 : ((HasConstValue)do_spec.getLast()).getConstValue().intValue();
    List list = new LinkedList();

    for(int i=beg;i<=end;i+=step){
      env.put(do_val,new Integer(i));
      Iterator it = do_range.iterator();
      while(it.hasNext()){
        Pair p = (Pair)it.next();
        if(p.getLeft() instanceof Token){
          list.add(new ArrayElement((Token)p.getLeft(),(FirList)p.getRight(),env));
        }
        else{
          // recursive do list
          list.addAll(makeElemListFromDoList((FirList)p.getLeft(),
                                             (Quad)p.getRight(),
                                             (HashMap)env.clone()));
        }
      }
    }
    return list;
  }


  /**
    @param vt    target variable type(vector type)
    @param valit constants
    @param m     DoList environments
   */
  Exp makeArrayInitializeList(VectorType vt, Iterator valit, Map m){
    int dimsize  = vt.getDimension();
    int dims[]   = new int[7];
    int dimlow[] = new int[7];
    int i;

    if(fTypeUtil.isFortranCharacterType(fTypeUtil.getVectorBaseType(vt))){
      // dimsize>1){
      dimsize--;
    }

    for(i=0;i<dimsize;i++){
      dims  [dimsize - i - 1] = (int)vt.getElemCount();
      dimlow[dimsize - i - 1] = (int)vt.getLowerBound();

      if(vt.getElemType() instanceof VectorType){
        vt = (VectorType)vt.getElemType();
      }
    }

    if(m != null){
      return makeArrayInitializeList_r(dimsize, dims, dimlow, new LinkedList(), m);
    }
    else{
      return makeArrayInitializeList_r(dimsize, dims, valit);
    }
  }

  Exp makeArrayInitializeList_r(int dim, int dimidx[], int dimlow[], LinkedList dims, Map m){
    if(dim == 0){
      dp("list :" + dims);
      dp("map  :" + m.keySet());
      if(m.containsKey(dims)){
        return (Exp)m.get(dims);
      }
      else{
        return fHirUtil.makeNullNode();
        // return fHirUtil.makeConstInt0Node();
      }
    }
    else{
      java.util.List list = new java.util.LinkedList();
      for(int i=0;i<dimidx[dim-1];i++){
        LinkedList l = (LinkedList)dims.clone();
        l.add(new Integer(i+dimlow[dim-1]));
        list.add(makeArrayInitializeList_r(dim-1, dimidx, dimlow, l, m));
      }
      return new ExpListExpImpl(hirRoot, list);
    }
  }

  Exp makeArrayInitializeList_r(int dim, int []dims, Iterator it){
    if(dim == 0){
      Node n = (Node)it.next();
      dp("processData#makeArrayInitializeList_r(dim:0): " + n);
      Exp exp = n.makeExp();
      if(exp instanceof FortranCharacterExp){
        exp = ((FortranCharacterExp)exp).getBody();
        exp = exp.getExp1(); // undecay
      }
      return exp;
    }
    else{
      java.util.List list = new java.util.LinkedList();
      for(int i=0;i<dims[dim-1];i++){
        list.add(makeArrayInitializeList_r(dim-1,dims,it));
      }
      return new ExpListExpImpl(hirRoot,list);
    }
  }

  /**
    mergeArrayInitializeList
    (nil nil A   nil)
    (nil B   nil nil) => (nil B A nil)
   */
  ExpListExp mergeArrayInitializeList(ExpListExp el1, ExpListExp el2){
    java.util.List list = new java.util.LinkedList();

    if(el1.length() != el2.length()){
      dp("mergeArrayInitializeList length mismatch: " +
         el1.length() + ", " + el2.length());
      
      return null;
    }
    
    for(int i=0;i<el1.length();i++){
      Exp e1 = el1.getExp(i);
      Exp e2 = el2.getExp(i);

      if(e1 instanceof NullNode){
        e1 = null;
      }
      if(e2 instanceof NullNode){
        e2 = null;
      }

      if(e1 instanceof ExpListExp){
        ExpListExp ele = mergeArrayInitializeList((ExpListExp)e1, (ExpListExp)e2);
        if(ele == null){
          return null;
        }
        list.add(ele);
      }
      else if(e1 == null && e2 != null){
        list.add(e2);
      }
      else if(e1 != null && e2 == null){
        list.add(e1);
      }
      else if(e1 == null && e2 == null){
        list.add(fHirUtil.makeNullNode());
      }
      else{
        return null;
      }
    }
    return new ExpListExpImpl(hirRoot, list);
  }

  String exp2str(ExpListExp el){
    Iterator it = el.iterator();
    String ret = "(" + el.size() + ")>> ";
    
    while(it.hasNext()){
      Exp e = (Exp)it.next();
      ret = ret + e.toString() + ", ";
    }
    return ret;
  }
  
  void dp(String str){
    fHir.dp(str);
  }

  
  /* obsolete */
  
  /*
    settlementArrayInitializeList
    (nil nil A B nil) => (0 0 A B 0)
   */
  void settlementArrayInitializeList(ExpListExp elist, Type vt){
    int len = elist.length();
    for(int i=0; i<len; i++){
      Exp e = elist.getExp(i);
      if(e == null){
        elist.setExp(i, fHirUtil.makeNullNode());
        // elist.setExp(i, fHirUtil.makeConstInt0Node());
      }
      else if(e instanceof ExpListExp){
        settlementArrayInitializeList(
          (ExpListExp)elist.getExp(i),
          ((VectorType)vt).getElemType());
      }
    }
    // set
    elist.setType(vt);
  }
}


