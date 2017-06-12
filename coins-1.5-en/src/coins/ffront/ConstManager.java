/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.ir.hir.Exp;
import coins.sym.Type;

/**
  Constant manager.

  Constant is declared by parameter statement.

  ex: parameter(pi=3.14, ...)
 */

public class ConstManager extends BaseManager{
  java.util.HashMap CurrentParameterMap;
  DeclManager       fDeclMgr;
  TypeUtility       fTypeUtil;

  public ConstManager(FirToHir fth, DeclManager dmgr){
    super(fth);
    fDeclMgr  = dmgr;
    fTypeUtil = fHir.getTypeUtility();
    CurrentParameterMap = new java.util.HashMap();
  }

  /**
    Process parameter list.
   */
  void processParameterDeclStatement(FirList list){
    Iterator lit = list.iterator();
    while(lit.hasNext()){
      // pair ::= (name . value)
      Pair p = (Pair)lit.next();
      Token  ident = (Token)p.getLeft();
      Node   value =        p.getRight();
      String name = ident.getLexem();
      Type   type = fDeclMgr.getSymbolType(name);

      if(isConstName(name)){
        // duplicate error
        printMsgFatal("[Parameter] This symbol is already declared as parameter: " + name);
      }
      if(fDeclMgr.isDefinedSymbol(name)){
        // delete this symbol
        dp("- parameter: delete from symbol table: " + name);
        fDeclMgr.deleteFromSymbolTable(name);
      }

      dp("Parameter(" + name + " = " + value + " as " + type + ")");
      CurrentParameterMap.put(name,
                              new ParameterObject(name, value, type, fHir));
    }
  }

  //////////////////////////////////////////////////////////////
  // ((name ParamObject) ...)
  class ParameterObject{
    String name_;
    Node node_;
    Type type_;
    FirToHir fHir;

    public ParameterObject(String name,Node n,Type t, FirToHir fth){
      name_ = name;
      node_ = n;
      type_ = t;
      fHir  = fth;
    }
    
    FNumber getConstValue(){
      FNumber n = ((HasConstValue)node_).getConstValue();
      if(type_.isInteger()){
        return FNumber.make(n.intValue());
      }
      if(type_.isFloating()){
        return FNumber.make(n.doubleValue());
      }
      if(fTypeUtil.isComplexType(type_)){
        //K not implemented
      }
      fHir.printMsgFatal("unkown parameter: ??");
      return null;
    }
    
    Exp makeExp(){
      Exp  e = node_.makeExp();
      Type t = e.getType();

      if(t != type_ &&
         !(fTypeUtil.isComplexType(t) &&
           fTypeUtil.isComplexType(type_))){
        
        if(fTypeUtil.isComplexType(type_)){
          return new ComplexExp(e, fHirUtil.makeConstReal0Node(), fHir);
        }
        else if(fTypeUtil.isComplexType(t)){
          if(type_.isFloating()){
            return ((ComplexExp)e).getRealPart();
          }
          else{
            return hir.convExp(type_, e);
          }
        }
        else{
          return hir.convExp(type_, e);
        }
      }
      else if(fTypeUtil.isDoubleComplexType(type_)){
        ComplexExp ce = (ComplexExp)e;
        return new DoubleComplexExp(ce.getRealPart(), ce.getImagPart(), fHir);
      }
      else{
        return e;
      }
    }
    
    Exp makeArgAddr(FStmt stmt){
      Exp  e = node_.makeExp();
      Type t = e.getType();
      
      if(t != type_){
        return fHirUtil.makeArgAddr(stmt, makeExp());
      }
      else{
        return node_.makeArgAddr(stmt);
      }
    }
    
    void setType(Type t){
      type_ = t;
    }
  }


  FNumber getConstValue(Token name){
    return getConstValue(name.getLexem());
  }
  FNumber getConstValue(String name){
    if(CurrentParameterMap == null){
      return null;
    }
    ParameterObject po = (ParameterObject)CurrentParameterMap.get(name);
    if(po != null){
      return po.getConstValue();
    }
    else{
      return null;
    }
  }

  Exp getConstExp(String name){
    if(CurrentParameterMap == null){
      return null;
    }
    ParameterObject po = (ParameterObject)CurrentParameterMap.get(name);
    if(po != null){
      return po.makeExp();
    }
    else{
      return null;
    }
  }

  Exp getConstArgAddr(String name,FStmt stmt){
    if(CurrentParameterMap == null){
      return null;
    }
    ParameterObject po = (ParameterObject)CurrentParameterMap.get(name);
    if(po != null){
      return po.makeArgAddr(stmt);
    }
    else{
      return null;
    }
  }

  boolean setParameterType(String name,Type t){
    ParameterObject po = (ParameterObject)CurrentParameterMap.get(name);
    if(po != null){
      po.setType(t);
      return true;
    }
    else{
      return false;
    }
  }
  boolean isConstName(String name){
    return CurrentParameterMap.get(name) != null;
  }
}




