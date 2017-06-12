/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.ir.IrList;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.Stmt;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;

/** Concat Expression
 */
public class ConcatNode extends Pair implements HasConstValue{
  public ConcatNode(Node left, Node right, FirToHir pfHir) {
    super(left, right, pfHir);
  }
  public void print(int level, String spaces){
    fHir.debugPrint(level, spaces+"<concat:"+" "+ left + " // " + right + ">\n");
    super.print(level, spaces);
  }
  public String toString(){
    return "ConcatExp";
  }
  
  HirUtility      fHirUtil;
  TypeUtility     fTypeMgr;
  DeclManager     fDeclMgr;
  ExecStmtManager fESMgr;
  HIR hir;
  Sym sym;

  Exp func_, evp, evl;
  int length_, size_;
  
  
  FirList get_concatenatee(){
    FirList concatenatee_list = new FirList(fHir);
    
    traverse_concatenatee(concatenatee_list, left);
    traverse_concatenatee(concatenatee_list, right);
    
    return concatenatee_list;
  }
  
  void traverse_concatenatee(FirList list, Node target){
    if(target instanceof ConcatNode){
      ConcatNode ce = (ConcatNode)target;
      traverse_concatenatee(list, ce.getLeft());
      traverse_concatenatee(list, ce.getRight());
    }
    else{
      list.add(target);
    }
  }

  void prepare(){
    hir = fHir.getHir();
    sym = fHir.getSym();
    fHirUtil = fHir.getHirUtility();
    fDeclMgr = fHir.getDeclManager();
    fESMgr   = fHir.getExecStmtManager();
    fTypeMgr = fHir.getTypeUtility();
    fHir.dp("ConcatExp#makeExp: " + left + " // " + right);

    FirList concatenatee_list = get_concatenatee();
    int size = concatenatee_list.size();
    
    String name;
    Type   type;
    
    name = fESMgr.getTempName() + "_vec_ptr_";
    type = sym.vectorType(sym.pointerType(fTypeMgr.getCharType()), size);
    Var vec_ptr = sym.defineVar(name.intern(), type);
    
    name = fESMgr.getTempName() + "_vec_len_";
    type = sym.vectorType(fTypeMgr.getIntType(), size);
    Var vec_len = sym.defineVar(name.intern(), type);
    
    Iterator it = concatenatee_list.iterator();
    evp = hir.varNode(vec_ptr);
    evl = hir.varNode(vec_len);
    int length = 0;
    int i = 0;
    while(it.hasNext()){
      Node n = (Node)it.next();
      FortranCharacterExp e = (FortranCharacterExp)n.makeExp();
      Exp e_len;
      
      fESMgr.addStmt(
        hir.assignStmt(
          hir.subscriptedExp(evp, fHirUtil.makeIntConstNode(i)),
          e.getBody()));

      e_len = e.getLength();
      fESMgr.addStmt(
        hir.assignStmt(
          hir.subscriptedExp(evl, fHirUtil.makeIntConstNode(i)),
          e_len));

      length += e_len.evaluateAsInt();
      i++;
    }

    // s_cat(str, a__2, i__2, &c__3, 100L);
    func_ = fHirUtil.makeSubpNode("s_cat",
                                  Parser.INTEGER,
                                  hir.irList(), Sym.SYM_EXTERN);
    length_ = length;
    size_   = size;
  }
  
  /** Make HIR Exp node of this concat expression.
   */
  public Exp makeExp(){
    prepare();
    String name   = fESMgr.getTempName() + "_concat_dst_";
    Var vt = sym.defineVar(name.intern(),
                           sym.vectorType(fTypeMgr.getCharType(), length_));

    Exp et = hir.varNode(vt);

    IrList args = hir.irList();
    args.add(et);
    args.add(evp);
    args.add(evl);
    args.add(fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(),
                                  fHirUtil.makeIntConstNode(size_)));
    args.add(fHirUtil.makeIntConstNode(length_));
    fESMgr.addStmt(hir.callStmt(func_, args));

    return et;
  }

  /** Make HIR Exp node from this expression
   * as a call-by-address parameter in pCallStmt.
   *
   * @param pCallStmt a call statement or a function call expression
   * @return Exp node.
   */
  public Exp makeArgAddr(FStmt pCallStmt){
    return makeExp();
  }

  public Stmt assignWithConcatString(Node dstnode){
    prepare();
    IrList args = hir.irList();
    Exp dstarg, dstsize;

    if(dstnode instanceof SubstringNode){
      FortranCharacterExp dst = (FortranCharacterExp)dstnode.makeExp();
      dstarg  = hir.exp(HIR.OP_ADDR, dst.getBody());
      dstsize = dst.getLength();
    }
    else{
      FortranCharacterExp dst =
        (FortranCharacterExp)dstnode.makeExp();
      dstarg = dstnode.makeArgAddr(fESMgr.getCurrentStmt());
      dstsize= dst.getLength();
    }

    args.add(dstarg);
    args.add(evp);
    args.add(evl);
    args.add(fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(),
                                  fHirUtil.makeIntConstNode(size_)));
    args.add(dstsize);
    
    return hir.callStmt(func_, args);
  }
  
  
  public FNumber getConstValue(){
    return null;
  }
  
  void dp(String msg){
    fHir.dp(msg);
  }
}


