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
import coins.sym.VectorType;


/**
  Intrinsic Utility
 */
public class IntrinsicUtility{
  static NameAndType[] intrinsicTable = null;
  FirToHir fHir;
  HIR      hir;
  Sym      sym;

  HirUtility      fHirUtil;
  TypeUtility     fTypeUtil;
  DeclManager     fDeclMgr;
  ExecStmtManager fESMgr;

  public IntrinsicUtility(FirToHir fth){
    fHir = fth;
    fTypeUtil = fHir.getTypeUtility();
    fHirUtil = fHir.getHirUtility();
    hir = fHir.getHir();
    sym = fHir.getSym();

    if(intrinsicTable == null){
      init_intrinsicTable();
    }
  }

  /** Search a function name in intrinsicTable.
   *
   * @param name
   * @return index of intrinsicTable if intrinsic function name else -1
   */
  int searchFuncName(String name){
    for (int i = 0; i < intrinsicTable.length; i++){
      if (intrinsicTable[i].name == name){
        return i;
      }
    }
    return -1;
  }

  boolean isIntrinsicCall(String id){
    return (searchFuncName(id) >= 0);
  }

  /** Make Exp node for intrinsic function call.
   * @param index intrinsicTable's index
   * @return intrinsic function call Exp.
   */
  Exp makeIntrinsicCall(String ident, FirList args){
    fESMgr = fHir.getExecStmtManager();

    int index = searchFuncName(ident);
    Exp expArg = null;
    Exp expArg2= null, libSpec;
    IrList argExps = hir.irList();
    Node hint1 = null, hint2 = null;

    int i = 0;
    for(Iterator it = args.iterator(); it.hasNext(); i+=1){
      Node n = (Node)it.next();
      Exp e = n.makeExp();
      argExps.add(e);
      if(i == 0){
        expArg = e;
        hint1 = n;
      }
      else if(i == 1){
        expArg2 = e;
        hint2 = n;
      }
    }

    argsTypeCheck(intrinsicTable[index], argExps);
    return makeIntrinsicCallCont(expArg, expArg2, argExps, index, hint1, hint2);
  }

  Exp makeIntrinsicCallCont(Exp expArg, Exp expArg2, IrList argExps, int index, Node hint, Node hint2){
    NameAndType nat = intrinsicTable[index];
    String ident = nat.name;

    //
    switch (nat.kind) {

      ///////////////////
    case  MATH_LIB:
      if(fTypeUtil.isComplexType(expArg.getType())){
        // redispatch
        int idx = searchFuncName(nat.complexFunc);
        return makeIntrinsicCallCont(expArg, expArg2, argExps, idx, hint, hint2);
      }
      else{
        return makeLibCall(nat.libName, expArg, Parser.DOUBLE_PREC);
      }

      ///////////////////
    case MATH_LIB2:
      return makeLibCall2(nat.libName, expArg, expArg2, Parser.DOUBLE_PREC);

      ///////////////////
    case COMPLEX_LIB:{
      String name = fESMgr.getTempName() + "_intrinsic_dst_";
      Var dst = sym.defineVar(name.intern(), fTypeUtil.getComplexStructType());
      Exp dstExp = hir.varNode(dst);

      makeComplexCall(nat.libName,
                      hint.makeArgAddr(fESMgr.getCurrentStmt()),
                      hir.exp(HIR.OP_ADDR, dstExp));

      return fHirUtil.makeComplexExp(dstExp);
    }

      ///////////////////
    case CONV_TO_INTEGER:
      if(expArg instanceof ComplexExp){
        return hir.convExp(fTypeUtil.getIntType(), ((ComplexExp)expArg).getRealPart());
      }
      else if(expArg instanceof FortranCharacterExp){
        FortranCharacterExp fcexp = (FortranCharacterExp)expArg;

        if(fcexp.getBody().getType() instanceof VectorType){
          return hir.convExp(fTypeUtil.getIntType(),
                             hir.contentsExp(
                               hir.decayExp(fcexp.getBody())));
        }
        else{
          return hir.convExp(fTypeUtil.getIntType(),
                             hir.contentsExp(fcexp.getBody()));
        }
      }
      else{
        return hir.convExp(fTypeUtil.getIntType(), expArg);
      }

    case CONV_TO_REAL:
      if (expArg instanceof ComplexExp){
        return ((ComplexExp)expArg).getRealPart();
      }
      else{
        return hir.convExp(fTypeUtil.getRealType(), expArg);
      }

    case CONV_TO_DOUBLE:
      if(expArg instanceof ComplexExp){
        return ((ComplexExp)expArg).getRealPart();
      }
      else{
        return hir.convExp(fTypeUtil.getDoubleType(), expArg);
      }

    case CONV_TO_CHAR:
      {
        Exp tmp = tempVarNode("char", fTypeUtil.charArray(1));
        fESMgr.addStmt(
          makeAssignStmt(hir.subscriptedExp(tmp, fHirUtil.makeConstInt0Node()),
                         expArg));
        return tmp;
      }

    case CONV_TO_COMPLEX:
      if(expArg instanceof ComplexExp){
        return expArg;
      }
      else if(expArg2 == null){
        return new ComplexExp(
          fHirUtil.castIfNeeded(expArg, fTypeUtil.getRealType()),
          fHirUtil.makeConstReal0Node(), fHir);
      }
      else{
        return new ComplexExp(
          fHirUtil.castIfNeeded(expArg,  fTypeUtil.getRealType()),
          fHirUtil.castIfNeeded(expArg2, fTypeUtil.getRealType()),
          fHir);
      }
      ///////////////////
    case INDEX:
      {
        Exp s_copy = fHirUtil.makeSubpNode(
          "i_indx", Parser.INTEGER, hir.irList(), Sym.SYM_EXTERN);
        IrList args = hir.irList();
        FortranCharacterExp e1 = (FortranCharacterExp) expArg,
        e2 = (FortranCharacterExp) expArg2;
        args.add(e1.getBody());
        args.add(e2.getBody());
        // length
        args.add(e1.getLength());
        args.add(e2.getLength());
        return hir.functionExp(s_copy, args);
      }

      ///////////////////
    case TRUNCATE:
      return hir.convExp(expArg.getType(),
                         hir.convExp(fTypeUtil.getIntType(), expArg));

    case NEAREST:
      return hir.convExp(expArg.getType(), funcNEAREST(expArg));

    case NEAREST_INT:
      return funcNEAREST(expArg);

    case LENGTH:
      return ((FortranCharacterExp)expArg).getLength();

    case IMAGINARY_PART:
    case DIMAGINARY_PART:
      return ((ComplexExp)expArg).getImagPart();
      
//      return ((DoubleComplexExp)expArg).getImagPart();
      
    case CONJUGATE:
      return new ComplexExp(
        ((ComplexExp)expArg).getRealPart(),
        hir.exp(HIR.OP_NEG, ((ComplexExp)expArg).getImagPart()), fHir);

    case DCONJUGATE:
      return new DoubleComplexExp(
          ((DoubleComplexExp)expArg).getRealPart(),
          hir.exp(HIR.OP_NEG, ((DoubleComplexExp)expArg).getImagPart()), fHir);
      
      ///////////////////
    case MOD:
      if(expArg.getType() == fTypeUtil.getIntType() &&
         expArg2.getType() == fTypeUtil.getIntType()){
        return hir.exp(HIR.OP_MOD, expArg, expArg2);
      }
      else{
        /**
         * a1 mod a2
         *
         * =>
         * a1 - ((int)(a1 / a2) * a2)
         *
         * =>
         * e1 = a1 / a2
         * e2 = (int) e1
         * e3 = e2 * a2
         */
        expArg = fHirUtil.convToDouble(expArg);
        expArg2= fHirUtil.convToDouble(expArg2);

        Exp e1 = hir.exp(HIR.OP_DIV, expArg, expArg2);
        Exp e2 = hir.convExp(fTypeUtil.getIntType(), e1);
        Exp e3 = hir.exp(HIR.OP_MULT, fHirUtil.convToDouble(e2), expArg2);
        return hir.exp(HIR.OP_SUB, expArg, e3);
      }

      /*
        tmp = a1;
        if(a2 >= 0){
          if(a1 < 0){
            tmp = -a1;
          }
        }
        else{
          if(a1 > 0){
            tmp = -a1;
          }
        }
       */
    case SIGN:
      {
        Type type1 = expArg.getType();
        Type type2 = expArg2.getType();
        Exp tmp   = tempVarNode("sign", type1);
        
        fESMgr.addStmt(makeAssignStmt(tmp, expArg));
        
        Stmt neg1 = makeAssignStmt(tmp, hir.exp(HIR.OP_NEG, expArg));
        Stmt neg2 = makeAssignStmt(tmp, hir.exp(HIR.OP_NEG, expArg));
        
        Exp  zn1   = fHirUtil.makeTyped0Node(type1);
        Exp  zn2   = fHirUtil.makeTyped0Node(type2);

        fESMgr.addStmt(
          hir.ifStmt(
            // cond
            hir.exp(HIR.OP_CMP_GE, expArg2, zn2),
            // then
            hir.ifStmt(hir.exp(HIR.OP_CMP_LT, expArg, zn1),
                       neg1, null),
            // else
            hir.ifStmt(hir.exp(HIR.OP_CMP_GT, expArg, zn1),
                       neg2, null)));

        return tmp;
      }

      /**
        tmp = a1 - a2;
        if(tmp < 0){
          tmp = 0;
        }
       */
    case DIM:
      {
        /**
         * dim(a1, a2):
         *   if(a1 > a2){
         *     ans = a1 - a2;
         *   }
         *   else{
         *     ans = 0;
         *   }
         */
        Type type = fHirUtil.checkTwoExpType(expArg, expArg2);
        expArg  = fHirUtil.castIfNeeded(expArg, type);
        expArg2 = fHirUtil.castIfNeeded(expArg2, type);

        Exp tmp = tempVarNode("dim", type);
        fESMgr.addStmt(makeAssignStmt((Exp)tmp,
                                      hir.exp(HIR.OP_SUB, expArg, expArg2)));
        fESMgr.addStmt(
          hir.ifStmt(
            // cond
            hir.exp(HIR.OP_CMP_LT, tmp, fHirUtil.makeTyped0Node(type)),
            // then
            makeAssignStmt(tmp, fHirUtil.makeTyped0Node(type)),
            // else
            null));

        return tmp;
      }

    case DPROD:
      // real -> real -> double
      return hir.exp(HIR.OP_MULT,
                     hir.convExp(fTypeUtil.getDoubleType(), expArg),
                     hir.convExp(fTypeUtil.getDoubleType(), expArg2));

      ///////////////////
    case ABS:
      Type argType = expArg.getType();
      Exp constZero = null;
      Exp temp      = null;

      if (argType.isFloating()){
        if (argType.getTypeKind() == Type.KIND_DOUBLE){
          constZero = fHirUtil.makeConstDouble0Node();
          temp = hir.varNode(fESMgr.makeDoubleTemp());
        }
        else{
          constZero = fHirUtil.makeConstReal0Node();
          temp = hir.varNode(fESMgr.makeRealTemp());
        }
      }
      else if (argType.isInteger()) {
        constZero = fHirUtil.makeConstInt0Node();
        temp = hir.varNode(fESMgr.makeIntTemp());
      }
      else if (expArg instanceof ComplexExp){
        Exp lExpReal = ((ComplexExp)expArg).getRealPart();
        Exp lExpImag = ((ComplexExp)expArg).getImagPart();
        temp = hir.exp(HIR.OP_ADD,
                       hir.exp(HIR.OP_MULT, lExpReal, lExpReal),
                       hir.exp(HIR.OP_MULT, lExpImag, lExpImag));
        return makeLibCall("sqrt", temp, Parser.DOUBLE_PREC);
      }
      else {
        fHir.printMsgFatal("unimplemented abs function "+ident);
        return fHirUtil.makeConstInt1Node();
      }

      Stmt stmt = makeAssignStmt(temp, expArg);
      fESMgr.getCurrentStmt().addGeneratedStmt(stmt);
      Exp condition = hir.exp(HIR.OP_CMP_LT, temp, constZero);
      Exp negation  = hir.exp(HIR.OP_NEG,    temp);

      stmt = hir.ifStmt(condition,
                        makeAssignStmt(temp, negation), null);

      fESMgr.addStmt(stmt);
      return temp;

      ///////////////////
    case POWER:
      Type type1 = expArg.getType();
      Type type2 = expArg2.getType();
      if (type1.isInteger() && type2.isInteger()){
        return intPowExp(expArg, expArg2);
      }
      else if (type1 == fTypeUtil.getComplexStructType() ||
               type2 == fTypeUtil.getComplexStructType()){
        return complexPowExp(expArg, expArg2);
      }
      else{
        return makeLibCall2("pow", expArg, expArg2, Parser.DOUBLE_PREC);
      }


    case MAX:
      return funcMAX(argExps);
    case MAX_R:
      return hir.convExp(fTypeUtil.getRealType(), funcMAX(argExps));
    case MAX_I:
      return hir.convExp(fTypeUtil.getIntType(), funcMAX(argExps));

    case MIN:
      return funcMIN(argExps);
    case MIN_R:
      return hir.convExp(fTypeUtil.getRealType(), funcMIN(argExps));
    case MIN_I:
      return hir.convExp(fTypeUtil.getIntType(), funcMIN(argExps));

      /////////////////////////////////////////////////////////////////////////
    case NOT_YET:
      fHir.printMsgFatal("BUG: NOT_YET");
      return null;
      ///////////////////
    default: // NOT_YET
      fHir.printMsgFatal("unimplemented function "+ident+" with " + expArg.getType());
      return null;
    }
  }

  Exp tempVarNode(String str, Type type){
    return hir.varNode(sym.defineVar(fESMgr.getTempName(str), type));
  }
  Stmt makeAssignStmt(Exp e1, Exp e2){
    return fHirUtil.makeAssignStmt(e1, e2);
  }


  /**
    x ** y (x,y: Integer)
    if(y  <= 4){
      (x * ...)
    else{
      ipow(x,y)
    }
   */
  Exp intPowExp(Exp e1, Exp e2){
    HIR hir = fHir.getHir();
    IrList aParamList = hir.irList();
    IrList fParamList = hir.irList();

    aParamList.add(fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(), e1));
    aParamList.add(fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(), e2));
    // fParamList.add(pExp.getType());

    Exp libSpec = fHirUtil.makeSubpNode("pow_ii", Parser.INTEGER,
                                        fParamList, Sym.SYM_EXTERN);
    return hir.functionExp(libSpec, aParamList);
  }

  Exp complexPowExp(Exp e1, Exp e2){
    HIR hir = fHir.getHir();
    IrList aParamList = hir.irList();
    IrList fParamList = hir.irList();

    String name = fESMgr.getTempName() + "_intrinsic_dst_";

    Var dst = sym.defineVar(name.intern(), fTypeUtil.getComplexDoubleStructType());
    Exp dstExp = hir.varNode(dst);
    Exp libSpec;

    aParamList.add(hir.exp(HIR.OP_ADDR, dstExp));

    if(e2.getType() == fTypeUtil.getIntType()){
      aParamList.add(fHirUtil.makeArgAddr(
        fESMgr.getCurrentStmt(),
        new DoubleComplexExp((ComplexExp)e1, fHir)));

      aParamList.add(fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(), e2));
      libSpec = fHirUtil.makeSubpNode("pow_zi", Parser.INTEGER,
                                      fParamList, Sym.SYM_EXTERN);
    }
    else{
      if(e1 instanceof ComplexExp){
        aParamList.add(
          fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(),
                               new DoubleComplexExp((ComplexExp)e1, fHir)));
      }
      else{
        Exp e = new DoubleComplexExp(e1, null, fHir);
        aParamList.add(fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(), e));
      }

      if(e2 instanceof ComplexExp){
        aParamList.add(
          fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(),
                               new DoubleComplexExp((ComplexExp)e2, fHir)));
      }
      else{
        Exp e = new DoubleComplexExp(e2, null, fHir);
        aParamList.add(fHirUtil.makeArgAddr(fESMgr.getCurrentStmt(), e));
      }
      libSpec = fHirUtil.makeSubpNode("pow_zz", Parser.INTEGER,
                                      fParamList, Sym.SYM_EXTERN);
    }
    fESMgr.addStmt(hir.callStmt(libSpec, aParamList));

    return fHirUtil.makeComplexExp(dstExp);
  }


  /**
    int(a+.5) if a>=0
    int(a-.5) if a<0
   */
  Exp funcNEAREST(Exp arg){
    Type type = arg.getType();
    Exp tmp = tempVarNode("nearest", type);
    fESMgr.addStmt(makeAssignStmt(tmp, arg));
    fESMgr.addStmt(
      hir.ifStmt(hir.exp(HIR.OP_CMP_GE, tmp, fHirUtil.makeTyped0Node(type)),
                 makeAssignStmt(tmp,
                                hir.exp(HIR.OP_ADD, tmp,
                                        fHirUtil.castIfNeeded(
                                          fHirUtil.makeFloatConstNode("0.5"),
                                          type))),
                 makeAssignStmt(tmp,
                                hir.exp(HIR.OP_SUB, tmp,
                                        fHirUtil.castIfNeeded(
                                          fHirUtil.makeFloatConstNode("0.5"),
                                          type)))));
    return hir.convExp(fTypeUtil.getIntType(), tmp);
  }


  /**
    <pre>
    max(x,y,...)
    =>
    temp = x;
    if(temp < y){
      temp = y;
    }
    @return Exp(temp)
    </pre>
   */
  Exp funcMAX(IrList args){
    Iterator it = args.iterator();
    Exp e0      = (Exp)it.next();
    Type type   = e0.getType();
    Exp tmp     = tempVarNode("max", type);

    fESMgr.addStmt(makeAssignStmt(tmp, e0));

    while(it.hasNext()){
      e0 = fHirUtil.castIfNeeded((Exp)it.next(), type);
      fESMgr.addStmt(
        hir.ifStmt(
          hir.exp(HIR.OP_CMP_LT,
                  tmp, e0),
          makeAssignStmt(tmp, e0),
          null));
    }
    return tmp;
  }
  /**
    <pre>
    min(x,y,...)
    =>
    temp = x;
    if(temp > y){
      temp = y;
    }
    @return Exp(temp)
    </pre>
   */
  Exp funcMIN(IrList args){
    Iterator it = args.iterator();
    Exp e0 = (Exp)it.next();
    Type type = e0.getType();
    Exp tmp= tempVarNode("min", type);

    fESMgr.addStmt(makeAssignStmt(tmp, e0));

    while(it.hasNext()){
      e0 = fHirUtil.castIfNeeded((Exp)it.next(), type);
      fESMgr.addStmt(
        hir.ifStmt(
          hir.exp(HIR.OP_CMP_GT,
                  tmp, e0),
          makeAssignStmt(tmp, e0),
          null));
    }
    return tmp;
  }

  /** Make Exp node for library-function call with one parameter.
   *
   * A -> A
   *
   * @param name library function name
   * @param pExp actual parameter
   * @param type return type
   * @return library function call Exp.
   */
  Exp makeLibCall(String name, Exp pExp, int type){
    HIR hir = fHir.getHir();
    IrList aParamList = hir.irList();
    IrList fParamList = hir.irList();
    Type return_type  = pExp.getType();

    if (type == Parser.DOUBLE_PREC){
      pExp = fHirUtil.convToDouble(pExp);
    }
    aParamList.add(pExp);
    // fParamList.add(pExp.getType());

    Exp libSpec = fHirUtil.makeSubpNode(name.intern(), type, fParamList, Sym.SYM_EXTERN);

    return convertTo(hir.functionExp(libSpec, aParamList), return_type);
  }

  /** Make Exp node for library-function call with two parameters.
   *
   * A -> A -> A
   *
   * @param name library function name
   * @param pExp actual parameter
   * @param type return type
   * @return library function call.
   */
  Exp makeLibCall2(String name, Exp pExp1, Exp pExp2, int type){
    HIR hir = fHir.getHir();
    IrList aParamList = hir.irList();
    IrList fParamList = hir.irList();
    Type return_type  = pExp1.getType();

    if (type == Parser.DOUBLE_PREC){
      pExp1 = fHirUtil.convToDouble(pExp1);
      pExp2 = fHirUtil.convToDouble(pExp2);
    }
    aParamList.add(pExp1);
    //fParamList.add(pExp1.getType());
    aParamList.add(pExp2);
    //fParamList.add(pExp2.getType());

    Exp libSpec = fHirUtil.makeSubpNode(name.intern(), type, fParamList, Sym.SYM_EXTERN);
    return convertTo(hir.functionExp(libSpec, aParamList), return_type);
  }

  Exp convertTo(Exp exp, Type type){
    if(exp.getType() == type){
      return exp;
    }
    else{
      return hir.convExp(type, exp);
    }
  }

  void makeComplexCall(String name, Exp src, Exp dst){
    HIR hir = fHir.getHir();
    IrList aParamList = hir.irList();
    IrList fParamList = hir.irList();

    aParamList.add(dst);
    aParamList.add(src);

    Exp libSpec = fHirUtil.makeSubpNode(name.intern(), Parser.INTEGER, fParamList, Sym.SYM_EXTERN);
    fESMgr.addStmt(hir.callStmt(libSpec, aParamList));
  }

  /** Intrinsic function's name and type.
   */
  class NameAndType {
    String name;
    String libName;
    int kind;
    int type1 = 0;
    int type2 = 0;
    String complexFunc;

    NameAndType(String name, int k, int t1){
      this.name = name;
      this.kind = k;
      this.type1 = t1;
    }
    NameAndType(String name, int k, int t1, int t2){
      this(name, k, t1);
      this.type2 = t2;
    }
    NameAndType(String name, int k, String pName, int t1){
      this(name, k, t1);
      this.libName = pName;
    }
    NameAndType(String name, int k, String pName, int t1, String cf){
      this(name, k, t1);
      this.libName = pName;
      this.complexFunc = cf;
    }

    NameAndType(String name, int k, String pName, int t1, int t2){
      this(name, k, t1, t2);
      this.libName = pName;
    }
  }

  // types of intrinsic functions
  static final int NOT_YET      = 0;
  static final int MATH_LIB     = 1;
  static final int MATH_LIB2    = 2;

  static final int CONV_TO_REAL    = 4;
  static final int CONV_TO_INTEGER = 5;
  static final int CONV_TO_DOUBLE  = 6;
  static final int CONV_TO_COMPLEX = 7;
  static final int CONV_TO_CHAR    = 8;

  static final int ABS            = 10;
  static final int POWER          = 11;
  static final int TRUNCATE       = 12;
  static final int NEAREST        = 13;
  static final int NEAREST_INT    = 14;
  static final int LENGTH         = 15;

  static final int IMAGINARY_PART = 17;
  static final int CONJUGATE      = 18;
  static final int DIMAGINARY_PART= 21;
  static final int DCONJUGATE     = 22;
  
  /*
  static final int EXP_COMPLEX    = 20;
  static final int LOG_COMPLEX    = 21;
  static final int SIN_COMPLEX    = 22;
  static final int COS_COMPLEX    = 23;
  static final int SQRT_COMPLEX   = 24;
   */
  static final int COMPLEX_LIB    = 20;

  static final int SIGN = 26;
  static final int DIM  = 27;
  static final int DPROD= 28;
  static final int MOD  = 29;
  //  static final int ATAN2= 29;

  static final int MAX  = 30;
  static final int MAX_R= 31;
  static final int MAX_I= 32;

  static final int MIN  = 33;
  static final int MIN_R= 34;
  static final int MIN_I= 35;

  static final int INDEX= 40;


  // types of arguments type restriction
  static final int TYPE_NO     = 0; // no...
  static final int TYPE_REAL   = 1; // real
  static final int TYPE_DOUBLE = 2; // double
  static final int TYPE_INT    = 3; // int
  static final int TYPE_COMPLEX = 7; // complex
  static final int TYPE_DCOMPLEX = 10; // complex
  static final int TYPE_CHARACTER = 4; // character

  static final int TYPE_FLOAT  = 5; // float(real, double)
  static final int TYPE_NUMBER = 6; // number(int, real, double, complex)
  static final int TYPE_FC     = 8; // float and complex
  static final int TYPE_FI     = 9; // float and integer


  static final int TYPE_ONE_OR_TWO    = 0x100;
  static final int TYPE_OVER_TWO      = 0x200;

  boolean typeCheck(Type type, int expected_type){
    // dp("typeCheck: " + type + "(expected: " + expected_type + ")");

    switch(expected_type){
    case TYPE_REAL:
      return type == fTypeUtil.getRealType();
    case TYPE_DOUBLE:
      return type == fTypeUtil.getDoubleType();
    case TYPE_INT:
      return type == fTypeUtil.getIntType();
    case TYPE_COMPLEX:
      return fTypeUtil.isComplexType(type);
    case TYPE_DCOMPLEX:
      return fTypeUtil.isDoubleComplexType(type);
    case TYPE_CHARACTER:
      return fTypeUtil.isFortranCharacterType(type);

    case TYPE_FLOAT:
      if(type == fTypeUtil.getRealType() ||
         type == fTypeUtil.getDoubleType()){
        return true;
      }
      else{
        return false;
      }
    case TYPE_NUMBER:
      if(type == fTypeUtil.getRealType()   ||
         type == fTypeUtil.getDoubleType() ||
         type == fTypeUtil.getIntType()    ||
         fTypeUtil.isComplexType(type)){
        return true;
      }
      else{
        return false;
      }
    case TYPE_FC:
      if(type == fTypeUtil.getRealType()   ||
         type == fTypeUtil.getDoubleType() ||
         fTypeUtil.isComplexType(type)){
        return true;
      }
      else{
        return false;
      }
    case TYPE_FI:
      if(type == fTypeUtil.getRealType()   ||
         type == fTypeUtil.getDoubleType() ||
         type == fTypeUtil.getIntType()){
        return true;
      }
      else{
        return false;
      }
    default:
      fHir.printMsgFatal("BUG: argsTypeCheck");
      return false;
    }
  }


  boolean argsTypeCheck(NameAndType nat, IrList argsExps){
    Iterator it = argsExps.iterator();
    int num = 0;
    while(it.hasNext()){
      num++;
      Exp  e = (Exp)it.next();
      Type t;
      if(e instanceof FortranCharacterExp){
        t = ((FortranCharacterExp)e).getBody().getType();
      }
      else{
        t = e.getType();
      }

      if(typeCheck(t, nat.type1 & 0xff) == false){
        fHir.printMsgRecovered("[IntrinsicFunction] Argument type is incollect for "
                               + nat.name + ": " + t);
        return false;
      }
    }

    // number check
    if(num != 1 &&
       !(nat.type2 != 0 ||
         ((nat.type1 | TYPE_ONE_OR_TWO) != 0) ||
         ((nat.type1 | TYPE_OVER_TWO)   != 0))){

      fHir.printMsgRecovered("arguments error: too many args");
      return false;
    }
    if(num != 2 &&
       (nat.type2 != 0)){
      fHir.printMsgRecovered("arguments error: too many, or few args");
      return false;
    }
    return true;
  }



  void init_intrinsicTable(){
    NameAndType[] tempTable = {
      new NameAndType("_power",POWER, TYPE_NUMBER),
      new NameAndType("int",   CONV_TO_INTEGER, TYPE_NUMBER),
      new NameAndType("ifix",  CONV_TO_INTEGER, TYPE_REAL),
      new NameAndType("idint", CONV_TO_INTEGER, TYPE_DOUBLE),

      new NameAndType("real",  CONV_TO_REAL, TYPE_NUMBER),
      new NameAndType("float", CONV_TO_REAL, TYPE_INT),
      new NameAndType("sngl",  CONV_TO_REAL, TYPE_DOUBLE),

      new NameAndType("dble",  CONV_TO_DOUBLE, TYPE_NUMBER),
      new NameAndType("cmplx", CONV_TO_COMPLEX, TYPE_NUMBER | TYPE_ONE_OR_TWO),
      new NameAndType("ichar", CONV_TO_INTEGER, TYPE_CHARACTER),
      new NameAndType("char",  CONV_TO_CHAR, TYPE_INT),

      new NameAndType("aint",  TRUNCATE, TYPE_FLOAT),
      new NameAndType("dint",  TRUNCATE, TYPE_DOUBLE),

      new NameAndType("anint", NEAREST,  TYPE_FLOAT),
      new NameAndType("dnint", NEAREST,  TYPE_FLOAT),
      new NameAndType("nint",  NEAREST_INT, TYPE_FLOAT),
      new NameAndType("idnint",NEAREST_INT, TYPE_DOUBLE),

      new NameAndType("abs",   ABS, TYPE_NUMBER),
      new NameAndType("iabs",  ABS, TYPE_INT),
      new NameAndType("dabs",  ABS, TYPE_DOUBLE),
      new NameAndType("cabs",  ABS, TYPE_COMPLEX),

      new NameAndType("len",   LENGTH, TYPE_CHARACTER),

      new NameAndType("aimag", IMAGINARY_PART, TYPE_COMPLEX),
      new NameAndType("conjg", CONJUGATE,  TYPE_COMPLEX),

      new NameAndType("dimag", DIMAGINARY_PART, TYPE_DCOMPLEX),
      new NameAndType("dconjg",DCONJUGATE,      TYPE_DCOMPLEX),

      new NameAndType("sqrt",  MATH_LIB, "sqrt", TYPE_FC, "csqrt"),
      new NameAndType("dsqrt", MATH_LIB, "sqrt", TYPE_DOUBLE),
      new NameAndType("csqrt", COMPLEX_LIB, "c_sqrt", TYPE_COMPLEX),

      new NameAndType("exp",   MATH_LIB, "exp", TYPE_FC, "cexp"),
      new NameAndType("dexp",  MATH_LIB, "exp", TYPE_DOUBLE),
      new NameAndType("cexp",  COMPLEX_LIB, "c_exp", TYPE_COMPLEX),

      new NameAndType("log",   MATH_LIB, "log", TYPE_FC, "clog"),
      new NameAndType("alog",  MATH_LIB, "log", TYPE_REAL),
      new NameAndType("dlog",  MATH_LIB, "log", TYPE_DOUBLE),
      new NameAndType("clog",  COMPLEX_LIB, "c_log", TYPE_COMPLEX),

      new NameAndType("log10", MATH_LIB, "log10", TYPE_FLOAT),
      new NameAndType("alog10",MATH_LIB, "log10", TYPE_REAL),
      new NameAndType("dlog10",MATH_LIB, "log10", TYPE_DOUBLE),

      new NameAndType("sin",   MATH_LIB, "sin", TYPE_FC, "csin"),
      new NameAndType("dsin",  MATH_LIB, "sin", TYPE_DOUBLE),
      new NameAndType("csin",  COMPLEX_LIB, "c_sin", TYPE_COMPLEX),

      new NameAndType("cos",   MATH_LIB, "cos", TYPE_FC, "ccos"),
      new NameAndType("dcos",  MATH_LIB, "cos", TYPE_DOUBLE),
      new NameAndType("ccos",  COMPLEX_LIB, "c_cos", TYPE_COMPLEX),

      new NameAndType("tan",   MATH_LIB, "tan", TYPE_FLOAT),
      new NameAndType("dtan",  MATH_LIB, "tan", TYPE_DOUBLE),
      new NameAndType("asin",  MATH_LIB, "asin",TYPE_FLOAT),
      new NameAndType("dasin", MATH_LIB, "asin",TYPE_DOUBLE),
      new NameAndType("acos",  MATH_LIB, "acos",TYPE_FLOAT),
      new NameAndType("dacos", MATH_LIB, "acos",TYPE_DOUBLE),
      new NameAndType("atan",  MATH_LIB, "atan",TYPE_FLOAT),
      new NameAndType("datan", MATH_LIB, "atan",TYPE_DOUBLE),
      new NameAndType("atan2", MATH_LIB2, "atan2", TYPE_FLOAT, TYPE_FLOAT),
      new NameAndType("datan2",MATH_LIB2, "atan2", TYPE_DOUBLE,TYPE_DOUBLE),
      new NameAndType("sinh",  MATH_LIB, "sinh", TYPE_FLOAT),
      new NameAndType("dsinh", MATH_LIB, "sinh", TYPE_DOUBLE),
      new NameAndType("cosh",  MATH_LIB, "cosh", TYPE_FLOAT),
      new NameAndType("dcosh", MATH_LIB, "cosh", TYPE_DOUBLE),
      new NameAndType("tanh",  MATH_LIB, "tanh", TYPE_FLOAT),
      new NameAndType("dtanh", MATH_LIB, "tanh", TYPE_DOUBLE),

      new NameAndType("mod",   MOD,   TYPE_FI, TYPE_FI),
      new NameAndType("amod",  MOD,   TYPE_REAL, TYPE_REAL),
      new NameAndType("dmod",  MOD,   TYPE_DOUBLE, TYPE_DOUBLE),
      new NameAndType("sign",  SIGN,  TYPE_FI, TYPE_FI),
      new NameAndType("isign", SIGN,  TYPE_INT, TYPE_INT),
      new NameAndType("dsign", SIGN,  TYPE_DOUBLE, TYPE_DOUBLE),
      new NameAndType("dim",   DIM,   TYPE_FI, TYPE_FI),
      new NameAndType("idim",  DIM,   TYPE_INT, TYPE_INT),
      new NameAndType("ddim",  DIM,   TYPE_DOUBLE, TYPE_DOUBLE),
      new NameAndType("dprod", DPROD, TYPE_REAL, TYPE_REAL),

      new NameAndType("max",   MAX,   TYPE_FI   | TYPE_OVER_TWO),
      new NameAndType("max0",  MAX,   TYPE_INT  | TYPE_OVER_TWO),
      new NameAndType("amax1", MAX,   TYPE_REAL | TYPE_OVER_TWO),
      new NameAndType("dmax1", MAX,   TYPE_DOUBLE| TYPE_OVER_TWO),
      new NameAndType("amax0", MAX_R, TYPE_INT  | TYPE_OVER_TWO),
      new NameAndType("max1",  MAX_I, TYPE_REAL | TYPE_OVER_TWO),

      new NameAndType("min",   MIN,   TYPE_FI   | TYPE_OVER_TWO),
      new NameAndType("min0",  MIN,   TYPE_INT  | TYPE_OVER_TWO),
      new NameAndType("amin1", MIN,   TYPE_REAL | TYPE_OVER_TWO),
      new NameAndType("dmin1", MIN,   TYPE_DOUBLE | TYPE_OVER_TWO),
      new NameAndType("amin0", MIN_R, TYPE_INT  | TYPE_OVER_TWO),
      new NameAndType("min1",  MIN_I, TYPE_REAL | TYPE_OVER_TWO),

      new NameAndType("index", INDEX, TYPE_CHARACTER, TYPE_CHARACTER),
      //      new NameAndType("atan2", ATAN2, TYPE_FLOAT, TYPE_FLOAT),
      //      new NameAndType("datan2",ATAN2, TYPE_DOUBLE, TYPE_DOUBLE),
    };
    intrinsicTable = tempTable;
  }


  void dp(String msg){
    fHir.dp(msg);
  }
}


