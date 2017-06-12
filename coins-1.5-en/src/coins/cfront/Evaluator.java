/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.cfront;

import coins.ast.TokenId;
import coins.ast.TypeId;
import coins.ast.expr.ConstantExpr;
import coins.ast.expr.FloatConstantExpr;
import coins.ast.expr.IntConstantExpr;
import coins.MachineParam;
import coins.sym.Type;

import java.math.BigInteger;

/**
 * Evaluator computes constant expressions.
 * All integer constants are processed as long int values.
**/
////////SF030212[
class Evaluator implements TokenId, TypeId
{
  final int[] toSize = new int[128]; // Type-to-precision conversion table

  private final int[] toRank  = new int[128]; // Type-to-rank conversion table
  private final BigInteger[] toBigIntMask = new BigInteger[128];

  private static byte[]   signedIntType = { (byte)  SIGNED_T, (byte)INT_T };
  private static byte[] unsignedIntType = { (byte)UNSIGNED_T, (byte)INT_T };

  public Evaluator(MachineParam mp) //SF041014
  {
    // Initialize the conversion table.
    toSize[CHAR_T       ] = mp.evaluateSize(Type.KIND_CHAR);
    toSize[SHORT_T      ] = mp.evaluateSize(Type.KIND_SHORT);
    toSize[INT_T        ] = mp.evaluateSize(Type.KIND_INT);
    toSize[LONG_T       ] = mp.evaluateSize(Type.KIND_LONG);
    toSize[LONG_LONG_T  ] = mp.evaluateSize(Type.KIND_LONG_LONG);
    toSize[FLOAT_T      ] = mp.evaluateSize(Type.KIND_FLOAT);
    toSize[DOUBLE_T     ] = mp.evaluateSize(Type.KIND_DOUBLE);
    toSize[LONG_DOUBLE_T] = mp.evaluateSize(Type.KIND_LONG_DOUBLE);
    toSize[POINTER_T    ] = mp.evaluateSize(Type.KIND_POINTER);

    toRank[CHAR_T       ] = Type.KIND_RANKS[Type.KIND_CHAR];
    toRank[SHORT_T      ] = Type.KIND_RANKS[Type.KIND_SHORT];
    toRank[INT_T        ] = Type.KIND_RANKS[Type.KIND_INT];
    toRank[LONG_T       ] = Type.KIND_RANKS[Type.KIND_LONG];
    toRank[LONG_LONG_T  ] = Type.KIND_RANKS[Type.KIND_LONG_LONG];
    toRank[FLOAT_T      ] = Type.KIND_RANKS[Type.KIND_FLOAT];
    toRank[DOUBLE_T     ] = Type.KIND_RANKS[Type.KIND_DOUBLE];
    toRank[LONG_DOUBLE_T] = Type.KIND_RANKS[Type.KIND_LONG_DOUBLE];

    toBigIntMask[CHAR_T     ] =
      BigInteger.valueOf( ~((-1L)<<(8*mp.evaluateSize(Type.KIND_CHAR))) );
    toBigIntMask[SHORT_T    ] =
      BigInteger.valueOf( ~((-1L)<<(8*mp.evaluateSize(Type.KIND_SHORT))) );
    toBigIntMask[INT_T      ] =
      BigInteger.valueOf( ~((-1L)<<(8*mp.evaluateSize(Type.KIND_INT))) );
    toBigIntMask[LONG_T     ] =
      BigInteger.valueOf( ~((-1L)<<(8*mp.evaluateSize(Type.KIND_LONG))) );
    toBigIntMask[LONG_LONG_T] =
      BigInteger.valueOf( ~((-1L)<<(8*mp.evaluateSize(Type.KIND_LONG_LONG))) );
  }
  //------------------------------------------------------
  public ConstantExpr make(long value)
  {
    return make(value,SIGNED_T,INT_T);
  }
  //------------------------------------------------------
  public ConstantExpr make(long value,byte[] type)
  {
    return make( value, (char)type[0], (char)type[1] );
  }
  //------------------------------------------------------
  public ConstantExpr make(long value,EncodedType type)
  {
    return make(
      value, type.isSigned()?SIGNED_T:UNSIGNED_T, (char)type.getTypeChar() );
  }
  //------------------------------------------------------
  /**
   * Make integer constant expression.
   * If the precision of "type" is less than 64, upper bits are filled
   * by sign extension (if "sign" is S) or 0-extension (if "sign" is U).
   *
   * @param   value: Value of the constant.
   * @param   sign: S if signed, U if unsigned.
   * @param   type: Type of the constant to be generated.
   * @return  Constant expression.
  **/
  public ConstantExpr make(long value,char sign,char type)
  {
    if( type==ENUM_BEGIN ) //SF041010
      type = INT_T; //SF041010
    int bits = 8*toSize[type]; // Precision (number of bits) of "type".
    if( bits==0 )
    {
      System.err.println("TYPEID ERROR: "+type);
      bits = 8*toSize[INT_T];
    }
    int restbits = 64-bits; // Unused upper bits.
    value = sign==SIGNED_T                // If signed
          ? (value<<restbits)>> restbits  // then do sign extension;
          : (value<<restbits)>>>restbits; // else if unsigned then do 0-extension.
    return new IntConstantExpr(value,sign,type);
  }
  //------------------------------------------------------
  public ConstantExpr make(double value)
  {
    return make(value,DOUBLE_T);
  }
  //------------------------------------------------------
  public ConstantExpr make(double value,byte[] type)
  {
    return make( value, (char)type[0] );
  }
  //------------------------------------------------------
  public ConstantExpr make(double value,EncodedType type)
  {
    return make( value, (char)type.getTypeChar() );
  }
  //------------------------------------------------------
  /**
   * Make floating constant expression.
   * (How to adjust precision according to "type" ?)
   *
   * @param   value: Floating constant value.
   * @param   type: Type of the constant to be generated.
   * @return  Constant expression.
  **/
  public ConstantExpr make(double value,char type)
  {
    switch(type) // How to adjust precision according to "type" ?
    {
    case FLOAT_T:       value = (float)value; // REFINE !
    case DOUBLE_T:      break;
    case LONG_DOUBLE_T: break;
    default: System.err.println("TYPEID ERROR: "+type); break;
    }
    return new FloatConstantExpr(value,type);
  }
  //------------------------------------------------------
  /**
   * Do constant folding by casting the constant expression expr
   * to the type shown by etype.
   *
   * @param   lex: Lex
   * @param   expr: Constant expression to be casted.
   * @param   etype: Type of resultant expression.
   * @return  Constant expression.
  **/
  public ConstantExpr cast( Lex lex,ConstantExpr expr,EncodedType etype)
  throws ParseError
  {
    if( etype.isValue() )
      return etype.isDouble()
           ? make(expr.doubleValue(),etype)
           : make(expr.longValue(),etype);
    throw new ParseError(lex, "bad cast operation: ("+etype+")");
  }
  //------------------------------------------------------
  /**
   * Constant folding for arithmetic unary expression.
   *
   * @param   lex: Lex
   * @param   op: Character which show operation.
   * @param   expr: Constant expression to be fold.
   * @return  Constant expression.
  **/
  public ConstantExpr applyUnaryOp(Lex lex,int op,ConstantExpr expr)
  throws ParseError
  {
    if( expr instanceof IntConstantExpr )
      switch (op)
      {
      case '+': return make(  expr.longValue(), getIpType(expr.getType()) );
      case '-': return make( -expr.longValue(), getIpType(expr.getType()) );
      case '!': return make(  expr.longValue()==0?1:0, signedIntType  );
      case '~': return make( ~expr.longValue(), getIpType(expr.getType()) );
      }
    else //FloatConstantExpr
      switch (op)
      {
      case '+': return expr;
      case '-': return make( -expr.doubleValue(), expr.getTypeChar() );
      case '!': return make(  expr.doubleValue()==0?1:0, signedIntType ); //SF040420
      case '~': throw new ParseError(lex, "bad unary operation");
      }
    throw new RuntimeException("unknonw unary operator: "+op);
  }
  //------------------------------------------------------
  /**
   * Constant folding for arithmetic binary expression.
   *
   * @param   lex: Lex
   * @param   expr1: Left-hand-side constant expression to be fold.
   * @param   op: Character which show operation.
   * @param   expr2: Right-hand-side constant expression to be fold.
   * @return  Constant expression.
  **/
  public ConstantExpr applyBinaryOp(
    Lex lex,ConstantExpr expr1,int op,ConstantExpr expr2)
  throws ParseError
  {
    byte[] uactype = getUacType( expr1.getType(), expr2.getType() );
    if( uactype.length==1 ) // Result of expression is floating.
    {
      double d1 = expr1.doubleValue();
      double d2 = expr2.doubleValue();
      switch( op )
      {
      case '+':    return make(d1+d2,uactype);
      case '-':    return make(d1-d2,uactype);
      case '*':    return make(d1*d2,uactype);
      case '/':    if( d2==0 ) //SF040816
                     throw new ParseError(lex, "division by zero"); //SF040816
                   return make(d1/d2,uactype);
      case '%':
      case LSHIFT:
      case RSHIFT:
      case '&':
      case '^':
      case '|':    throw new ParseError(lex, "bad binary operation of float");
      case ANDAND: return make( (d1!=0)&&(d2!=0)?1:0, signedIntType );
      case OROR:   return make( (d1!=0)||(d2!=0)?1:0, signedIntType );
      case LE:     return make( d1<=d2?1:0, signedIntType );
      case GE:     return make( d1>=d2?1:0, signedIntType );
      case '<':    return make( d1< d2?1:0, signedIntType );
      case '>':    return make( d1> d2?1:0, signedIntType );
      case EQ:     return make( d1==d2?1:0, signedIntType );
      case NEQ:    return make( d1!=d2?1:0, signedIntType );
      }
    }
    else // Result of expression is integer.
    {
      long l1 = expr1.longValue();
      long l2 = expr2.longValue();
      switch (op)
      {
      //case '+':    return make( l1+l2, uactype );
      //case '-':    return make( l1-l2, uactype );
      //case '*':    return make( l1*l2, uactype );
      //case '/':    return make( l1/l2, uactype );
      //case '%':    return make( l1%l2, uactype );
      case LSHIFT: return make( l1<<l2, getIpType(expr1.getType()) );
      case RSHIFT: return make( expr1.getSignChar()==SIGNED_T?l1>>l2:l1>>>l2,
                                getIpType(expr1.getType()) );
      case '&':    return make( l1&l2, uactype );
      case '^':    return make( l1^l2, uactype );
      case '|':    return make( l1|l2, uactype );
      case ANDAND: return make( (l1!=0)&&(l2!=0)?1:0, signedIntType );
      case OROR:   return make( (l1!=0)||(l2!=0)?1:0, signedIntType );
      //case LE:     return make( l1<=l2?1:0, signedIntType );
      //case GE:     return make( l1>=l2?1:0, signedIntType );
      //case '<':    return make( l1< l2?1:0, signedIntType );
      //case '>':    return make( l1> l2?1:0, signedIntType );
      //case EQ:     return make( l1==l2?1:0, signedIntType );
      //case NEQ:    return make( l1!=l2?1:0, signedIntType );
      }

      //BigInteger i1 = make(l1,uactype).toBigInteger();
      //BigInteger i2 = make(l2,uactype).toBigInteger();

      ConstantExpr c1 = make(l1,uactype);
      BigInteger b1 = BigInteger.valueOf(c1.longValue());
      if( c1.getSignChar()!=SIGNED_T )
        b1 = b1.and( toBigIntMask[c1.getTypeChar()] );

      ConstantExpr c2 = make(l2,uactype);
      BigInteger b2 = BigInteger.valueOf(c2.longValue());
      if( c2.getSignChar()!=SIGNED_T )
        b2 = b2.and( toBigIntMask[c2.getTypeChar()] );

      switch (op)
      {
      case '+':    return make( b1.add      (b2).longValue(), uactype );
      case '-':    return make( b1.subtract (b2).longValue(), uactype );
      case '*':    return make( b1.multiply (b2).longValue(), uactype );
      case '/':    if( b2.equals(BigInteger.ZERO) ) //SF040816
                     throw new ParseError(lex, "division by zero"); //SF040816
                   return make( b1.divide   (b2).longValue(), uactype );
      case '%':    return make( b1.remainder(b2).longValue(), uactype );
      case LE:     return make( b1.compareTo(b2)<=0?1:0, signedIntType );
      case GE:     return make( b1.compareTo(b2)>=0?1:0, signedIntType );
      case '<':    return make( b1.compareTo(b2)< 0?1:0, signedIntType );
      case '>':    return make( b1.compareTo(b2)> 0?1:0, signedIntType );
      case EQ:     return make( b1.compareTo(b2)==0?1:0, signedIntType );
      case NEQ:    return make( b1.compareTo(b2)!=0?1:0, signedIntType );
      }
    }
    throw new RuntimeException("unknonw binary operator: " + op);
  }
  //------------------------------------------------------
  /**
   * Get the resultant type of arithmetic type conversion.
   *
   * @param   t1: Type of left-hand-side operand.
   * @param   t2: Type of reft-hand-side operand.
   * @return  Resultant type.
  **/
  private byte[] getUacType(byte[] t1,byte[] t2)
  {
    // Floating number.
    int r1 = toRank[t1[0]]; // Rank
    int r2 = toRank[t2[0]];
    if( r1!=0 || r2!=0 )
      return r1>r2? t1: t2;
    // Integer
    r1 = toRank[t1[1]]; // Rank
    r2 = toRank[t2[1]];
    int a1 = toSize[t1[1]]; // Precision
    int a2 = toSize[t2[1]];
    if( r1>=toRank[INT_T] || r2>=toRank[INT_T] ) // Either one has rank greater or equal to int
    {
      if( a1!=a2 ) // If precisions differ
        return a1>a2? t1: t2; // then return higher one.
      else // If precisions are the same then
        return new byte[]{
          t1[0]==SIGNED_T&&t2[0]==SIGNED_T? (byte)SIGNED_T: (byte)UNSIGNED_T,
          r1>r2? t1[1]: t2[1] }; // return the higher one.
                                 // (unsigned is higher than signed.)
    }
    else // Both ranks are less than int.
    {
      if( a1>=toSize[INT_T] && t1[0]==UNSIGNED_T   // If ((either precision >= precision of int)
      ||  a2>=toSize[INT_T] && t2[0]==UNSIGNED_T ) // && (unsigend)) then
        return unsignedIntType; // unsigned int
      else // otherwise
        return signedIntType; // signed int
    }
  }
  //------------------------------------------------------
  /**
   * Get the resultant type of integral promotion.
   *
   * @param   t1: Type of operand.
   * @return  Resultant type.
  **/
  private byte[] getIpType(byte[] t1)
  {
    if( toRank[t1[0]]!=0 )
      return null; // Not integral type.
    if( toRank[t1[1]]>=toRank[INT_T] ) // If (rank >= (rank of int))
      return t1; // then return it unchanged.
    else // If ((rank < (rank of int)&&
      if( toSize[t1[1]]>=toSize[INT_T] // (precision >= (precision of int))
      &&  t1[0]==UNSIGNED_T )      // &&(unsigned)) then
        return unsignedIntType; // unsigned int
      else // otherwise
        return signedIntType; // signed int
  }
  //------------------------------------------------------
}
////////SF030212]
