/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.sym.PointerType;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.VectorType;

/**
 * This class offers the cast function.
 * <pre>
 *  . explicit/implicit cast
 *  . usual arithmetic converson
 *  . integral promotion
 *  . comparison result type
 *  . ?: resultant type
 * etc.
 * </pre>
 *  @author  Shuichi Fukuda
**/
class ToHirCast
{
  private Type[]  toUnsigned; // signed->unsigned conversion table
  private Type[]  toSigned;   // unsigned->signed conversion table

  private final ToHir toHir;
  private final HIR   hir;     // HIR instance (used to create HIR objects)
  private final Sym   sym;     // Sym instance (used to create Sym objects)
  protected int fDbgLevel; //##67

  //-------------------------------------------------------------------
  ToHirCast(ToHir tohir)
  {
    toHir = tohir;
    hir   = tohir.hirRoot.hir;
    sym   = tohir.hirRoot.sym;
    fDbgLevel = tohir.hirRoot.ioRoot.dbgToHir.getLevel(); //##67
    toHir.debug.print(1, "ToHirCast\n"); //##71
    toUnsigned = new Type[]{
      null, //0
      null, //1
      null, //2
      toHir.symRoot.typeU_Short, //3
      toHir.symRoot.typeU_Int, //4
      toHir.symRoot.typeU_Long, //5
      toHir.symRoot.typeU_LongLong, //6
      toHir.symRoot.typeU_Char, //7
      toHir.symRoot.typeU_Char, //8
      toHir.symRoot.typeU_Short, //9
      toHir.symRoot.typeU_Int, //10
      toHir.symRoot.typeU_Long, //11
      toHir.symRoot.typeU_LongLong, //12
      null, //13
      null, //14
      null, //15
      toHir.symRoot.typeFloat, //16
      toHir.symRoot.typeDouble, //17
      toHir.symRoot.typeLongDouble, //18
    };
    toSigned = new Type[]{
      null, //0
      null, //1
      null, //2
      toHir.symRoot.typeShort, //3
      toHir.symRoot.typeInt, //4
      toHir.symRoot.typeLong, //5
      toHir.symRoot.typeLongLong, //6
      toHir.symRoot.typeChar, //7
      toHir.symRoot.typeChar, //8
      toHir.symRoot.typeShort, //9
      toHir.symRoot.typeInt, //10
      toHir.symRoot.typeLong, //11
      toHir.symRoot.typeLongLong, //12
      null, //13
      null, //14
      null, //15
      toHir.symRoot.typeFloat, //16
      toHir.symRoot.typeDouble, //17
      toHir.symRoot.typeLongDouble, //18
    };
  }
  //-------------------------------------------------------------------
  /**
   * Output debug message.
   *
   * @param  level Debug level.
   * @param  mes Debug message.
  **/
  private void message(int level,String mes)
  {
    toHir.debug.print(level,"Ca",mes);
  }
  //-------------------------------------------------------------------
  /**
   * Warning message of inappropriate implicit cast.
   *
   * @param  t1 Target type.
   * @param  t2 Original type.
  **/
  private void warning(Type t1,Type t2)
  {
   toHir.warning("implicit cast from "+t2+" to "+t1);
  }
  //-------------------------------------------------------------------
  /**
   * Error message of invalid implicit cast.
   *
   * @param  t1 Target type.
   * @param  t2 Original type.
  **/
  private void error(Type t1,Type t2)
  {
    toHir.error("invalid cast from "+t2+" to "+t1);
  }
  //-------------------------------------------------------------------
  // explicit cast
  //-------------------------------------------------------------------
  /**
   * Cast for HIR type object.
   *
   * @param   t1 Ttarget type.
   * @param   e2 Expression.
   * @return  Casted expression.
  **/
  Exp cast(Type t1,Exp e2)
  {
    if (fDbgLevel > 3) //##67
      message(6,"cast T1="+t1+" E2="+e2);
    Type t2 = e2.getType();
    if( t1==t2 )
      return e2; // If e2 has already the same type, return e2 unchanged.
    if( t1.getUnqualifiedType()==t2.getUnqualifiedType() )
      return hir.convExp(t1,e2); // If origin types are the same, simply cast.

    switch( t1.getTypeKind() )
    {
    case Type.KIND_ENUM:
    case Type.KIND_BOOL:
    case Type.KIND_SHORT:
    case Type.KIND_INT:
    case Type.KIND_LONG:
    case Type.KIND_LONG_LONG:
    case Type.KIND_CHAR:
    case Type.KIND_U_CHAR:
    case Type.KIND_U_SHORT:
    case Type.KIND_U_INT:
    case Type.KIND_U_LONG:
    case Type.KIND_U_LONG_LONG:
    case Type.KIND_ADDRESS:
    case Type.KIND_OFFSET:      return castToInteger(t1,t2,e2);
    case Type.KIND_FLOAT:
    case Type.KIND_DOUBLE:
    case Type.KIND_LONG_DOUBLE: return castToFloat(t1,t2,e2);
    case Type.KIND_POINTER:     return castToPointer(t1,t2,e2);
    }
    error(t1,t2); //error("Invalid cast ("+t2+" to "+t1+")");
    return e2;
  }
  //-------------------------------------------------------------------
  /**
   * Cast to integer.
   *
   * @param   t1 Target type.
   * @param   e2 Expression.
   * @return  Casted expression.
  **/
  private Exp castToInteger(Type t1,Type t2,Exp e2)
  {
    switch( e2.getType().getTypeKind() )
    {
    case Type.KIND_ENUM:
    case Type.KIND_BOOL:
    case Type.KIND_SHORT:
    case Type.KIND_INT:
    case Type.KIND_LONG:
    case Type.KIND_LONG_LONG:
    case Type.KIND_CHAR:
    case Type.KIND_U_CHAR:
    case Type.KIND_U_SHORT:
    case Type.KIND_U_INT:
    case Type.KIND_U_LONG:
    case Type.KIND_U_LONG_LONG:
    case Type.KIND_ADDRESS:
    case Type.KIND_OFFSET:
    case Type.KIND_FLOAT:
    case Type.KIND_DOUBLE:
    case Type.KIND_LONG_DOUBLE:
    //case Type.KIND_VOID:
      return hir.convExp(t1,e2);

    case Type.KIND_POINTER:
      //warning(t1,e2.getType()); // Inappropriate cast (pointer to integral)
      // Implicit cast is examined at each place and warning will be issued
      // if necessary.
      return hir.convExp(t1,e2);

    //case Type.KIND_VECTOR:
    //  warning(t1,e2.getType()); // Inappropriate cast (array to integer)
    //  return hir.convExp(t1,toHir.decayExp(e2));

    //case Type.KIND_STRUCT:
    //case Type.KIND_UNION:
    //case Type.KIND_SUBP:
    }
    error(t1,e2.getType()); // Inappropriate cast to integer.
    return e2;
  }
  //-------------------------------------------------------------------
  /**
   * Cast to floatage decimal.
   *
   * @param   t1 Target type.
   * @param   e2 Expression.
   * @return  Casted expression.
  **/
  private Exp castToFloat(Type t1,Type t2,Exp e2)
  {
    switch( t2.getTypeKind() )
    {
    case Type.KIND_ENUM:
    case Type.KIND_BOOL:
    case Type.KIND_SHORT:
    case Type.KIND_INT:
    case Type.KIND_LONG:
    case Type.KIND_LONG_LONG:
    case Type.KIND_CHAR:
    case Type.KIND_ADDRESS:
    case Type.KIND_OFFSET:
      if( t2.getSizeValue()<toHir.symRoot.typeInt.getSizeValue() )
        return hir.convExp( t1, hir.convExp(toHir.symRoot.typeInt,e2) );
      else
        return hir.convExp(t1,e2);

    case Type.KIND_U_CHAR:
    case Type.KIND_U_SHORT:
    case Type.KIND_U_INT:
    case Type.KIND_U_LONG:
    case Type.KIND_U_LONG_LONG:
      if( t2.getSizeValue()<toHir.symRoot.typeInt.getSizeValue() )
        return hir.convExp( t1, hir.convExp(toHir.symRoot.typeU_Int,e2) );
      else
        return hir.convExp(t1,e2);

    case Type.KIND_FLOAT:
    case Type.KIND_DOUBLE:
    case Type.KIND_LONG_DOUBLE:
      return hir.convExp(t1,e2);

    //case Type.KIND_VOID:
    //case Type.KIND_POINTER:
    //case Type.KIND_VECTOR:
    //case Type.KIND_STRUCT:
    //case Type.KIND_UNION:
    //case Type.KIND_SUBP:
    }
    // Invalid implicit cast to floating point type.
    error(t1,t2);
    return e2;
  }
  //-------------------------------------------------------------------
  /**
   * Cast to pointer.
   *
   * @param   t1 Target type.
   * @param   e2 Expression.
   * @return  Casted expression.
  **/
  private Exp castToPointer(Type t1,Type t2,Exp e2)
  {
    switch( t2.getTypeKind() )
    {
    case Type.KIND_ENUM:
    case Type.KIND_BOOL:
    case Type.KIND_SHORT:
    case Type.KIND_INT:
    case Type.KIND_LONG:
    case Type.KIND_LONG_LONG:
    case Type.KIND_CHAR:
    case Type.KIND_U_CHAR:
    case Type.KIND_U_SHORT:
    case Type.KIND_U_INT:
    case Type.KIND_U_LONG:
    case Type.KIND_U_LONG_LONG:
      //if( e2.getOperator()!=HIR.OP_CONST
      //|| !((ConstNode)e2).isIntConst0() )
      //  warning(t1,t2); // Inappropriate implicit cast (integer to pointer)
      return hir.convExp(t1,e2);

    //case Type.KIND_ADDRESS:

    case Type.KIND_OFFSET: //pointer +or- offset
      return e2; // Unnecessary cast

    //case Type.KIND_FLOAT:
    //case Type.KIND_DOUBLE:
    //case Type.KIND_LONG_DOUBLE:

    case Type.KIND_POINTER:
      //if( t1!=t2
      //&&  t1!=toHir.typeVoidPtr
      //&&  t2!=toHir.typeVoidPtr  )
      //  warning(t1,t2);
        // Inappropriate implicit cast (pointer to incompatible pointer)
      return hir.convExp(t1,e2);

    case Type.KIND_VECTOR: //SF030620
      e2 = toHir.decayExp(e2);
      if( t1==e2.getType() )
        return e2;
      //warning(t1,t2); // Inappropriate implicit cast (array to pointer)
      return hir.convExp(t1,e2);

    //case Type.KIND_STRUCT:
    //case Type.KIND_UNION:
    //case Type.KIND_SUBP:
    }
    error(t1,t2); // Invalid implicit cast to pointer.
    return e2;
  }
  //-------------------------------------------------------------------
  // implicit cast
  //-------------------------------------------------------------------
  /**
   * Integral promotion.
   *
   * @param   e Expression.
   * @return  Integral type expression.
  **/
  Exp iPromotion(Exp e)
  {
    Type t = e.getType();
    if( t.getTypeRank() < toHir.symRoot.typeInt.getTypeRank() )
    {
      // If rank is less than int, do integral promotion.
      if( t.getSizeValue() >= toHir.symRoot.typeInt.getSizeValue()
      &&  t.isUnsigned() ) // If precision >= int and unsigned, then
        return hir.convExp(toHir.symRoot.typeU_Int,e); // result is unsigned int.
      else // otherwise
        return hir.convExp(toHir.symRoot.typeInt,e); // result is int.
    }
    return e; // Do not convert.

  }
  //-------------------------------------------------------------------
  /**
   * Do signed integral promotion (used at unary -).
   *
   * @param   e Expression.
   * @return  Integral type expression.
  **/
  Exp siPromotion(Exp e)
  {
    Type t = e.getType();
    int r = t.getTypeRank();
    if( toHir.isIntegral(t) ) // integral type
    {
      if( r<toHir.symRoot.typeInt.getTypeRank() ) // If rank < int, then do
        return hir.convExp(toHir.symRoot.typeInt,e);  // integral promotion.
      if( t.isUnsigned() ) // If rank >= int and unsigned, then convert to signed.
        return hir.convExp( toSigned[t.getTypeKind()], e );
    }
    return e;
  }
  //-------------------------------------------------------------------
  /**
    Default argument promotion
    @param   e Expression.
    @return  Integral type expression.
  **/
  Exp daPromotion(Exp e)
  {
    Type t = e.getType();
    if( toHir.isIntegral(t)                                     // If integer and
    &&  t.getTypeRank() < toHir.symRoot.typeInt.getTypeRank() ) // rank < int then
      return hir.convExp(toHir.symRoot.typeInt,e); // do integral promotion.
    if( t.getTypeKind()==Type.KIND_FLOAT ) // If float
      return hir.convExp(toHir.symRoot.typeDouble,e); // promote to double.
    return e;
  }
  //-------------------------------------------------------------------
  /**
   * Do cast for assignment statement.
   * If assignment is permissible, then cast according to the grammar rule
   * (cast to left hand side if required).
   * <pre>
   * Condition of permissible assignment:
   * *) Both sides are array of toHir.typeChar (initiation by char string for HIR-base)
   * 1) Both sides are arithmetic and their types are compatible
   *    except for type qualifier.
   * 2) Both sides are struct/union and their types are compatible
   *    except for type qualifier.
   * 3) Both sides are pointer, and their pointed types are compatible
   *    except for type qualifier (warning if incompatible), and
   *    left hand side pointed type includes type qualifier of the
   *    pointed type of right hand side (warning if not included).
   * 4) Both sides are pointer, and one of them is the pointer to void,
   *    and left hand side pointed type includes type qualifier of the
   *    pointed type of right hand side (warning if not included).
   * //5) Left hand side is pointer and right hand side is NULL pointer constant.
   * 5-1) Left hand side is pointer and right hand side is integral
   *    (implicitly cast to left hand side issueing warning).
   * 5-2) Left hand side is integral and right hand side is pointer
   *    (implicitly cast to left hand side issueing warning).
   * </pre>
   * @param   t1 Left operand Type.
   * @param   e2 Right operand.
   * @return  Casted right operand.
  **/
  Exp assignCast(Type t1,Exp e2)
  //SF050215[
  //{
  //  if( containsConst(t1) )
  //    toHir.warning("lvalue contains const modifier.");
  //  return assignCastAsInit(t1,e2);
  //}
  ////-------------------------------------------------------------------
  ///**
  // * Return true if the type contains const modifier.
  // * If the type is struct or union, the members are inspected recursively.
  // *
  // * @param   t Type.
  // * @return  Boolean.
  //**/
  //private boolean containsConst(Type t)
  //{
  //  if( t.isConst() )
  //    return true;
  //  switch( t.getTypeKind() )
  //  {
  //  case Type.KIND_STRUCT:
  //  case Type.KIND_UNION:
  //    for( Iterator i=t.getElemList().iterator(); i.hasNext(); )
  //      if( containsConst( ((Elem)i.next()).getSymType() ) )
  //        return true;
  //    break;
  //  }
  //  return false;
  //}
  //-------------------------------------------------------------------
  ///**
  // * Do cast for assignment statement as initializer.
  // * This method is almost the same as assignCast excluding that,
  // * Assignment for the const modificated type is admitted.
  // *
  // * @param   t1: Left operand Type.
  // * @param   e2: Right operand.
  // * @return  Casted right operand.
  //**/
  //Exp assignCastAsInit(Type t1,Exp e2)
  //SF050215]
  {
    ////////SF040908[
    if( t1.getSizeExp()==null )
    {
      toHir.error("lvalue is incomplete type.");
      return null;
    }
    ////////SF040908]
    Type t2 = e2.getType();
    int k1 = t1.getTypeKind();
    int k2 = t2.getTypeKind();

    if( k1==Type.KIND_VECTOR
    &&  k2==Type.KIND_VECTOR
    &&( ((VectorType)t1).getElemType().getTypeKind()==Type.KIND_U_CHAR
    ||  ((VectorType)t1).getElemType().getTypeKind()==Type.KIND_CHAR )
    &&( ((VectorType)t2).getElemType().getTypeKind()==Type.KIND_U_CHAR
    ||  ((VectorType)t2).getElemType().getTypeKind()==Type.KIND_CHAR ) ) // *)
      return e2;
    if( toHir.isArithmetic(t1) && toHir.isArithmetic(t2) ) // 1)
      return cast(t1,e2);
    if( (k1==Type.KIND_STRUCT || k1==Type.KIND_UNION)
    &&  toHir.isCompatible(t1,t2,false) ) // 2)
      return e2;
    if( k1==Type.KIND_POINTER
    &&  k2==Type.KIND_POINTER ) // 3) 4)
    {
      Type ptd1 = ((PointerType)t1).getPointedType();
      Type ptd2 = ((PointerType)t2).getPointedType();

      if( toHir.isCompatible(ptd1,ptd2,false) // 3)
      ||  ptd1.getTypeKind()==Type.KIND_VOID // 4)
      ||  ptd2.getTypeKind()==Type.KIND_VOID )
        if( toHir.isModifierIncluded(ptd1,ptd2) ) // 3) 4)
        //SF041206[
        //{
        //  message(6,"assignCast  "+ptd1+" INCLUDES MODIFIER OF "+ptd2);
        //  return e2;
        //}
          ;
        //SF041206]
        else
          toHir.warning(
            "implicit cast to disregard qualifiers of pointer target type");
      else
        toHir.warning("implicit cast to incompatible pointer types");
      return hir.convExp(t1,e2);
    }
    if( k1==Type.KIND_POINTER && toHir.isIntegral(t2)
    ||  k2==Type.KIND_POINTER && toHir.isIntegral(t1) ) // 5) //SF030514
      return cast(t1,e2);

    return null;
  }
  //-------------------------------------------------------------------
  // get type of the cast result
  //-------------------------------------------------------------------
  /**
   * Get the resultant type of UAC (usual arithmetic converson).
   * <pre>
   * If the rank of left operand is less than the rannk of right operand,
   * exchange operands to make processing easy.
   * If one of operands is not arithmetic, result (resultant type) is null.
   * If ranks are less than the rank of int then {
   *   Do integral promotion.
   *   If one of operands has precision greater than int and it is unsigned,
   *   then the result is unsigned int.
   *   In other cases return int.
   * }
   * If one of operands has rank greater than the rank of int then {
   *   If left operand precision > right operand precision,
   *    return left operand type.
   *   If one of operands is unsigned,
   *   then return the left oprerand type changing it to unsigned if required.
   *   If both operands are signed, then return the left operand type.
   * }
   * </pre>
   * @param   t1 Left operand type.
   * @param   t2 Right operand type.
   * @return  Resultant type which is null if t1 or t2 is not arithmetic type.
  **/
  Type getUacType(Type t1,Type t2)
  {
    int r1 = t1.getTypeRank(); // Rank of left operand.
    int r2 = t2.getTypeRank(); // Rank of right operand.
    if (fDbgLevel > 3) //##67
      message(8, "getUacType  LEFT="+t1+"("+r1+")  RIGHT="+t2+"("+r2+")");
    if( r1<r2 ) // If left operand rank is less than right operand rank,
      { Type t=t1; t1=t2; t2=t; int r=r1; r1=r2; r2=r; } // exchange operands.
    if( r2<=0 ) // Return null if one of operands is not arithmetic.
      return null;
    if( r1<toHir.symRoot.typeInt.getTypeRank() ) // If rank < int then
    {                                            // do integral promotion.
      //##22 int sint = toHir.symRoot.typeInt.getSizeValue(); // Precision of int
      long sint = toHir.symRoot.typeInt.getSizeValue(); //Precision of int //##22
      if( t1.getSizeValue()>=sint && t1.isUnsigned()   // If one of operands has
      ||  t2.getSizeValue()>=sint && t2.isUnsigned() ) // precision greater than int
          // and unsigned, then the resultant type is unsigned int.
        return toHir.symRoot.typeU_Int; // Return unsigned int.
      else // In other cases
        return toHir.symRoot.typeInt; // return int.
    }
    else // Operand rank > rank of int.
    {
      // If left operand precision > right operand precision,
      if( t1.getSizeValue()>t2.getSizeValue() )
        return  t1.getUnqualifiedType(); // return left operand type. //SF041014
      // If one of operands is unsigned,
      else if( t1.isUnsigned() || t2.isUnsigned() )
        // then return the left oprerand type (changed to) unsigned.
        return  toUnsigned[t1.getTypeKind()];
      else // If both operands are signed,
        return t1.getUnqualifiedType(); // then return the left operand type.//SF041014
    }
  }
  //-------------------------------------------------------------------
  /**
   * Get the type for casting operands of comparison operation.
   * <pre>
   * 1) Both operands are arithmetic
   *     toHir.isArithmetic(t1) && toHir.isArithmetic(t2)
   * 2) Both operands are pointer and their pointed types
   *   are compatible with each other disregarding type qualifier.
   *     isPointer(t1) && isPointer(t2) && toHir.isCompatible(ptd1,ptd2,false)
   * // 3) pointer and NULL pointer constant
   * 3) One operand is pointer and the other operand is integer
   *   is made permissible. In such case, 0 is converted to NULL pointer
   *   and other integer values are casted to pointer after issueing
   *   warning message.
   * 4) One operand is pointer and the other operand is pointer to
   *   void with or without type qualifier.
   * </pre>
   * @param   t1 Right operand type.
   * @param   t2 Left operand type.
   * @return  Resultant type of comparison.
  **/
  Type getCompareType(Type t1,Type t2)
  {
    int  r1 = t1.getTypeRank();
    int  r2 = t2.getTypeRank();

    if( r1<0 && (r2&1)==1 ) // 3) pointer CMP integer
    {
      //toHir.warning("comparison between pointer and integer"); //SF050215
      return t1;
    }
    if( (r1&1)==1 && r2<0 ) // 3) integer CMP pointer
    {
      //toHir.warning("comparison between integer and pointer"); //SF050215
      return t2;
    }
    if( r1<0 && r2<0 ) // pointer CMP pointer
    {
      Type ptd1 = ((PointerType)t1).getPointedType();
      Type ptd2 = ((PointerType)t2).getPointedType();
      if( ptd1.getTypeKind()==Type.KIND_VOID ) // 4)
        return t2;
      if( ptd2.getTypeKind()==Type.KIND_VOID ) // 4)
        return t1;
      //SF050215[
      //return sym.pointerType( toHir.compositeType(ptd1,ptd2,false) ); // 2)
      Type ptd = toHir.compositeType(ptd1,ptd2,false);
      if( ptd==null )
      {
        toHir.warning("comparison of distinct pointer types lacks a cast");
        ptd = toHir.symRoot.typeVoid;
      }
      return sym.pointerType(ptd); // 2)
      //SF050215]
    }
    return getUacType(t1,t2); // 1) arithmetic CMP arithmetic
  }
  //-------------------------------------------------------------------
  /**
   * Get the resultant type of selection expression (?: expression).
   * <pre>
   * 1) Both operands are arithmetic --&gt; usual arithmetic conversion
   * 2) Both operands are compatible struct/union --&gt; composite type
   * 3) Both operands are void --&gt; void
   * 4) Both operands are pointer and pointed types are compatible with each other
   *   disregarding type qualifier --&gt; pointer to composite type with
   *   all type qualifiers specified
   * 5) One operator is pointer and the oper is NULL pointer constant
   *   (treating as integer) --&gt; pointer
   * 6) Both operands are pointer and one is the pointer to void
   *   --&gt; pointer to void with all type qualifiers specified
   * </pre>
   * @param   t2 2nd operand type.
   * @param   t3 3rd operand type.
   * @return  Resultant type of ?:.
  **/
  Type getSelectType(Type t2,Type t3)
  {
    Type t  = null; // Type of resultant expression.
    int  k2 = t2.getTypeKind();
    int  k3 = t3.getTypeKind();

    // decide type of retrun value
    if( (t=getUacType(t2,t3))!=null ) // 1)
      return t;
    if( (k2==Type.KIND_STRUCT || k2==Type.KIND_UNION) // 2)
    &&  (t=toHir.compositeType(t2,t3,false/*true//SF041014*/))!=null )
      return t;
    if( k2==Type.KIND_VOID && k3==Type.KIND_VOID ) // 3)
      return toHir.symRoot.typeVoid;
    if( k2==Type.KIND_POINTER && k3==Type.KIND_POINTER )
    {
      Type ptd2 = ((PointerType)t2).getPointedType();
      Type ptd3 = ((PointerType)t3).getPointedType();

      if( ptd2.getTypeKind()==Type.KIND_VOID
      ||  ptd3.getTypeKind()==Type.KIND_VOID ) // 6)
        //return sym.pointerType(
        //  setCompositeModifier( toHir.symRoot.typeVoid, ptd2, ptd3 ) );
        return toHir.typeVoidPtr; //SF041014
      if( (t=toHir.compositeType(ptd2,ptd3,false))!=null ) // 4)
        //return sym.pointerType( setCompositeModifier(t,ptd2,ptd3) );
        return sym.pointerType(t); //SF0471014
    }
    if( k2==Type.KIND_POINTER && toHir.isIntegral(t3) ) // 5)
      return t2.getUnqualifiedType(); //SF041014
    if( toHir.isIntegral(t2) && k3==Type.KIND_POINTER ) // 5)
      return t3.getUnqualifiedType(); //SF041014
    return null;
  }
  //-------------------------------------------------------------------
  /**
   * Attach type qualifiers of st1 and st2 to t.
   *
   * @param t Resultant type.
   * @param st1 Type
   * @param st2 Type
   * @return resultant type with all type qualifiers specified.
  **/
  //private Type setCompositeModifier(Type t,Type st1,Type st2)
  //{
  //  if( st1.isConst() || st2.isConst() )
  //    t = t.makeConstType();
  //  if( st1.isVolatile() || st2.isVolatile() )
  //    t = t.makeVolatileType();
  //  return t;
  //}
  //-------------------------------------------------------------------
  /**
   * Get the rresultant type of address expression.
   * <pre>
   *   address expression +/- integer
   *   (pointer - pointer is treated in atOffset)
   * </pre>
   * @param   t1 Type
   * @param   t2 Type
   * @return  Rresultant type of address expression.
  **/
  Type getPointerOpType(Type t1,Type t2)
  {
    if( t1.getTypeKind()==Type.KIND_POINTER && toHir.isIntegral(t2) ) // 2)
    {
      Type ptd = ((PointerType)t1).getPointedType();
      if( ptd.getSizeValue()<=0 )
        toHir.error("pointed type is incomplete type or its size is 0: "+t1);
      return t1.getUnqualifiedType(); //SF041014
    }
    return null;
  }
  //-------------------------------------------------------------------
}
