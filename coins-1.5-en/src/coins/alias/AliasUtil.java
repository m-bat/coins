/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * AliasUtil.java
 *
 * Created on August 12, 2003, 4:00 PM
 */

package coins.alias;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import coins.IoRoot;
import coins.SymRoot;
import coins.ir.hir.HIR;
import coins.sym.BaseType;
import coins.sym.Elem;
import coins.sym.PointerType;
import coins.sym.StructType;
import coins.sym.Subp;
import coins.sym.SubpType;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.UnionType;
import coins.sym.VectorType;

/**
 * A collection of utility methods. Most methods are static,
 * but some methods need references to root objects such
 * as <code>HirRoot</code> or <code>SymRoot</code> and so
 * are instance methods.
 * @author  hasegawa
 */
public class AliasUtil
{

  /**
   * The <code>SymRoot</code> object shared by every module
   * in the program.
   */
  public final SymRoot symRoot;
  public final IoRoot io;

  //	private Set fPredefineds;

  //    protected static final String PREDEFINED = "aliaspredefined";

  /**
   * Creates a new instance of AliasUtil.
   *
   * @param pSymRoot <code>SymRoot</code> object shared by
   * every module in the program.
   */
  public AliasUtil(SymRoot pSymRoot)
  {
    symRoot = pSymRoot;
    io = symRoot.ioRoot;
    //		fPredefineds = readPredefineds(pSymRoot.ioRoot);
  }

  /**
   * Creates a new instance of AliasUtil that is associated with
   * a specified instance of alias analysis.
   *
   * @param pAliasAnal the Alias
   * /**
    * Returns <code>true</code> if the specified argument is
    * the malloc C library function.
    *
    * @param pSubp <code>Subp</code> <code>Sym</code> to test for.
    * @return true if the specified argument is the malloc
    *  C library function <code>Sym</code>.
    */
   public static boolean ismalloc(Subp pSubp)
   {
     return pSubp.getName().equals("malloc") &&
       (pSubp.getVisibility() == Sym.SYM_EXTERN ||
        pSubp.getVisibility() == Sym.SYM_PUBLIC);
   }

  /**
   * Returns <code>true</code> if the specified argument is known
   * to be well behaved. Well-behavedness of a subprogram here
   *  means it neither modifies objects (an area in the memory
   * space) that may be accessed from the code outside of the
   * subprogram nor stores the addresses of such objects in
   * storage accessible outside of the subprogram nor it
   * returns a pointer to such objects. I/O is irrelevant.
   * Most library functions are well behaved.
   *
   * @param pSubp <code>Subp</code> <code>Sym</code> to test for.
   * @return true if the specified argument is well behaved.
   */
  public static boolean isPredefined(Subp pSubp, Set pPredefined)
  {
    return pPredefined.contains(pSubp.getName()) &&
      (pSubp.getVisibility() == Sym.SYM_EXTERN ||
       pSubp.getVisibility() == Sym.SYM_PUBLIC);
  }

  /**
   * Type-based alias testing. Source language (currently only C
   * is implemented) rules may prohibit lvalues of certain types
   * to alias. This method tests for such aliasing possiblity.
   * Our aliasing rule dictates an aggregate-type object alias
   * to its member object, so this method returns
   * <code>true</code> for an aggregate type and its member type.
   *
   * @param pType <code>Type</code> of an lvalue to test for aliasing.
   * @param pType0 <code>Type</code> of an lvalue to test for aliasing.
   * @return true if lvalues of the specified <code>Type</code>s
   * can possibly alias.
   */
  public boolean mayAlias(Type pType, Type pType0)
  {
    return mayAliasBareAndSigned(toBareAndSigned(pType), toBareAndSigned(pType0));
  }

  private boolean mayAliasBareAndSigned(Type pType, Type pType0)
  {
    int lKind = pType.getTypeKind();
    int lKind0 = pType0.getTypeKind();

    if (pType.isBasicType() || lKind == Type.KIND_POINTER)
      if (pType0.isBasicType() || lKind0 == Type.KIND_POINTER)
        return lKind == Type.KIND_CHAR || lKind0 == Type.KIND_CHAR ||
          areCompatible(pType, pType0);
      else
        return mayAliasBareAndSigned(pType0, pType);

    switch (lKind) {
      case Type.KIND_VECTOR:
        return mayAliasBareAndSigned(pType0,
          toBareAndSigned(((VectorType)pType).getElemType()));
        //			case Type.KIND_POINTER:
        //				return areCompatible(((PointerType)pType).
        //       getPointedType(), ((PointerType)pType0).getPointedType());
        //				return lKind0 == Type.KIND_POINTER &&
        //           ((lPointed = ((PointerType)pType).getPointedType())
        //               .getTypeKind() == Type.KIND_VOID ||
        //                (lPointed0 = ((PointerType)pType0).getPointedType())
        //                .getTypeKind() == Type.KIND_VOID  ||
        //                   areCompatible(lPointed, lPointed0));
      case Type.KIND_STRUCT:
        for (Iterator lIt = ((StructType)pType).getElemList().iterator();
             lIt.hasNext(); )
          if (mayAliasBareAndSigned(pType0,
            toBareAndSigned(((Elem)lIt.next()).getSymType())))
            return true;
        return false;
      case Type.KIND_UNION:
        for (Iterator lIt = ((UnionType)pType).getElemList().iterator();
             lIt.hasNext(); )
          if (mayAliasBareAndSigned(pType0,
            toBareAndSigned(((Elem)lIt.next()).getSymType())))
            return true;
        return false;
      case Type.KIND_ENUM:
        return mayAliasBareAndSigned(pType0, symRoot.typeInt);
      case Type.KIND_SUBP:
      default:
        throw new AliasError("Unexpected.");
    }
  }

  private static Type toBare(Type pType)
  {
    //		System.out.println("pType = " + pType.getName());
    //		System.out.println("pType's origin = "  + pType.getOrigin());
    Type lType = pType;
    Type lNewType;
    return pType.getFinalOrigin();
//        do
//        {
//            lNewType = lType.getOrigin();
//            if (lNewType == null || lNewType == lType)
//                return lType;
//            lType = lNewType;
//        } while(true);
  }

  private Type toSigned(Type pType)
  {
    switch (pType.getTypeKind()) {
      case Type.KIND_U_CHAR:
        return symRoot.typeChar;
      case Type.KIND_U_SHORT:
        return symRoot.typeShort;
      case Type.KIND_U_INT:
        return symRoot.typeInt;
      case Type.KIND_U_LONG:
        return symRoot.typeLong;
      case Type.KIND_U_LONG_LONG:
        return symRoot.typeLongLong;
    }
    return pType;
  }

  public Type toBareAndSigned(Type pType)
  {
    return toSigned(toBare(pType));
  }

//    private boolean areCompatible(Type pType, Type pType0)
//    {
//        if (pType.getTypeKind() != pType0.getTypeKind())
//            return false;
//        if (!haveSameQual(pType, pType0))
//            return false;
//        if (pType.isBasicType())
//            return true;
//        switch (pType.getTypeKind())
//        {
//            case Type.KIND_VECTOR:
//                return ((VectorType)pType).getElemCount() == ((VectorType)pType0).getElemCount() && areCompatible(((VectorType)pType).getElemType(), ((VectorType)pType0).getElemType());
//            case Type.KIND_POINTER:
//                return areCompatible(((PointerType)pType).getPointedType(), ((PointerType)pType0).getPointedType());
//            case Type.KIND_STRUCT:
//            case Type.KIND_UNION:
//                return pType == pType0; // Does this suffice?
//            case Type.KIND_ENUM:
//                return areCompatible( toBare(pType0), symRoot.typeInt);
//            case Type.KIND_SUBP:
//                SubpType lSubpType = (SubpType)pType;
//                SubpType lSubpType0 = (SubpType)pType0;
//                //				return lSubpType.isCompatibleWith(lSubpType0); // This may not be the ANSI C compatibility.
//                if (lSubpType.getReturnType() != lSubpType0.getReturnType())
//                    return false;
//                if (!(lSubpType.getParamTypeList().isEmpty() && lSubpType.hasOptionalParam()))
//                    if (!(lSubpType0.getParamTypeList().isEmpty() && lSubpType0.hasOptionalParam()))
//                        return compBothParamTyped(lSubpType, lSubpType0);
//                    else
//                        return compParamTypedAndNotTyped(lSubpType, lSubpType0);
//                else if (!(lSubpType0.getParamTypeList().isEmpty() && lSubpType0.hasOptionalParam()))
//                    return compParamTypedAndNotTyped(lSubpType0, lSubpType);
//                return true;
//
//
//            default:
//                throw new AliasError("Unexpected.");
//        }
//    }

  public boolean areCompatible(Type pType, Type pType0)
  {
    if (!haveSameQual(pType, pType0))
      return false;

    if (pType.getTypeKind() != pType0.getTypeKind())
      if (pType.getTypeKind() != Type.KIND_ENUM &&
          pType0.getTypeKind() != Type.KIND_ENUM)
        return false;
      else if (pType.getTypeKind() == Type.KIND_ENUM)
        return areCompatible(pType0, symRoot.typeInt);
      else
        return areCompatible(pType, symRoot.typeInt);

    if (pType.isBasicType())
      return pType.getFinalOrigin() == pType0.getFinalOrigin();
    switch (pType.getTypeKind()) {
      case Type.KIND_VECTOR:
        return (pType.getFlag(Sym.FLAG_INCOMPLETE_TYPE) ||
                pType0.getFlag(Sym.FLAG_INCOMPLETE_TYPE) ||
                ((VectorType)pType).getElemCount() ==
                ((VectorType)pType0).getElemCount()) &&
          areCompatible(((VectorType)pType).getElemType(),
          ((VectorType)pType0).getElemType());
      case Type.KIND_POINTER:
        return areCompatible(((PointerType)pType).getPointedType(),
          ((PointerType)pType0).getPointedType());
      case Type.KIND_STRUCT:
      case Type.KIND_UNION:
        return pType.getFinalOrigin() == pType0.getFinalOrigin(); // Does this suffice?
      case Type.KIND_ENUM:
        return true;
//                return areCompatible( toBare(pType0), symRoot.typeInt);
      case Type.KIND_SUBP:
        SubpType lSubpType = (SubpType)pType;
        SubpType lSubpType0 = (SubpType)pType0;
        //				return lSubpType.isCompatibleWith(lSubpType0); // This may not be the ANSI C compatibility.
        if (lSubpType.getReturnType() != lSubpType0.getReturnType())
          return false;
        if (!(lSubpType.getParamTypeList().isEmpty() &&
              lSubpType.hasOptionalParam()))
          if (!(lSubpType0.getParamTypeList().isEmpty() &&
                lSubpType0.hasOptionalParam()))
            return compBothParamTyped(lSubpType, lSubpType0);
          else
            return compParamTypedAndNotTyped(lSubpType, lSubpType0);
        else if (!(lSubpType0.getParamTypeList().isEmpty() &&
                   lSubpType0.hasOptionalParam()))
          return compParamTypedAndNotTyped(lSubpType0, lSubpType);
        return true;

      default:
        throw new AliasError("Unexpected.");
    }
  }

  private static boolean haveSameQual(Type pType, Type pType0)
  {
    return pType.isConst() == pType0.isConst() &&
      pType.isVolatile() == pType0.isVolatile();
  }

  private boolean compBothParamTyped(SubpType pSubpType, SubpType pSubpType0)
  {
    Iterator lIt, lIt0;
    if (pSubpType.getParamTypeList().size() !=
        pSubpType0.getParamTypeList().size())
      return false;
    else
      for (lIt = pSubpType.getParamTypeList().iterator(),
           lIt0 = pSubpType0.getParamTypeList().iterator();
           lIt.hasNext() && lIt0.hasNext(); )
        if (!areCompatible(toBare((Type)lIt.next()),
          toBare((Type)lIt0.next())))
          return false;
    return true;
  }

  private boolean compParamTypedAndNotTyped(SubpType pSubpType,
    SubpType pSubpType0)
  {
    Type lParamType;

    if (pSubpType.hasOptionalParam())
      return false;
    else
      for (Iterator lIt = pSubpType.getParamTypeList().iterator(); lIt.hasNext(); ) {
        lParamType = (Type)lIt.next();
        if (!areCompatible(toBare(lParamType), daPromo(toBare(lParamType))))
          return false;
      }
    return true;
  }

  private Type daPromo(Type pBareType)
  {
    if (pBareType.isInteger())
      return iPromo((BaseType)pBareType);
    if (pBareType.getTypeKind() == Type.KIND_FLOAT)
      return symRoot.typeDouble;
    return pBareType;
  }

  private Type iPromo(BaseType pType)
  {
    switch (pType.getTypeKind()) {
      case Type.KIND_SHORT:
      case Type.KIND_CHAR:
        if (pType.getSizeValue() <= symRoot.typeInt.getSizeValue())
          return symRoot.typeInt;
        else
          return symRoot.typeU_Int;
      case Type.KIND_U_CHAR:
      case Type.KIND_U_SHORT:
        if (pType.getSizeValue() < symRoot.typeInt.getSizeValue())
          return symRoot.typeInt;
        else
          return symRoot.typeU_Int;
    }
    return pType;
  }

  /**
   * Returns a <code>String</code> representation of the
   * specified argument that is more representative than the
   * one its <code>toString</code> method provides.
   * The returned <code>String</code> contains representation
   * for the children of the specified argument.
   *
   * @param pHIR HIR node whose <code>String</code> representation
   * is to be returned.
   * @return a more detailed <code>String</code> representation
   * of the specified argument than the one its
   * <code>toString</code> method provides.
   */
  static String toString(HIR pHIR)
  {
    StringBuffer lBuff = new StringBuffer();

    lBuff.append("(");
    lBuff.append(pHIR);
    for (int i = 1; i <= pHIR.getChildCount(); i++)
      lBuff.append(toString((HIR)pHIR.getChild(i)));
    lBuff.append(")");
    return lBuff.toString();
  }

  /**
   * Checks if the specified constant subscript is within
   * the bounds for the specified <code>VectorType</code> object.
   *
   * @param pType <code>VectorType</code> object to test against.
   * @param pSubscript constant subscript that is tested to see
     * if it is in the allowed range for the given <code>VectorType</code> object.
   * @return true if the specified subscript is within the allowed
   * ranged for the specified <code>VectorType</code>.
   */
  static boolean subscriptCheck(VectorType pType, int pSubscript)
  {
    return pType.getLowerBound() <= pSubscript &&
      pSubscript < pType.getLowerBound() + pType.getElemCount();
  }

  public static boolean isScalar(Type pType)
  {
    int lTypeKind = pType.getTypeKind();
    if (lTypeKind >= Type.KIND_BOOL && lTypeKind <= Type.KIND_POINTER)
      return true;
    return false;
  }

  public static Set getComponentScalarTypes(Type pType)
  {
    Set lResult = new HashSet();

    if (isScalar(pType))
      lResult.add(pType);
    else
      switch (pType.getTypeKind()) {
        case Type.KIND_VECTOR:
          return getComponentScalarTypes(((VectorType)pType).getElemType());
        case Type.KIND_STRUCT:
        case Type.KIND_UNION:
          for (Iterator lIt = pType.getElemList().iterator();
               lIt.hasNext(); )
            lResult.addAll(getComponentScalarTypes(((Elem)lIt.next())
              .getSymType()));
          break;

        default:
          throw new AliasError("Unexpected.");
      }
    return lResult;
  }

  public Elem getFirstElem(StructType pStructType)
  {
    Elem lElem = (Elem)pStructType.getElemList().get(0);
    if (lElem.evaluateDisp() != 0)
      throw new AliasError("Unexpected.");
    return lElem;
  }

  public static boolean areWeakCompatible(Type pType, Type pType0)
  {
    throw new AliasError();
  }

  /**
   * Prints out the <code>String</code> representation of the
   * <code>pBody</code> object headed by <code>pHeader</code>
   * if the debug level for this alias analysis category is
   * greater than or equal to <code>pLevel</code>.
   *
   * @param pLevel the debug level required to actually print
   * the debug information.
   * @param pHeader header for the information
   * @param pBody body of the information
   * @see coins.Debug#printObject(int, String, Object)
   */
  public void dbg(int pLevel, String pHeader, Object pBody)
  {
    String lStr = pBody == null ? "null" : pBody.toString();
    if (io.getCompileSpecification().getTrace().
        getTraceLevel(AliasAnal.CATEGORY_NAME) >= pLevel) {
      io.printOut.print(" " + pHeader + " " + lStr);
      io.printOut.println();
    }
  }
}
