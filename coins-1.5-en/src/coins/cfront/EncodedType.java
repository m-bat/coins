/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.cfront;

import coins.ast.Declarator;
import coins.ast.TokenId;
import coins.ast.TypeId;

/*
  'c' char
  'i' int (signed, unsigned)
  's' short (short int)
  'l' long (long int)
  'j' long long
  'f' float
  'd' double
  'r' long double
  'v' void

  'S' singned
  'U' unsigned
  'C' const
  'V' volatile

  'e' ...

  'P' pointer
  'A<size>' array (e.g. int k[10] ==> A10i, char c[3][4] ==> A3A4c)
  'A?'      array without a size specification (e.g. int k[] ==> A?i)
  'F' function (e.g. char foo(int) ==> Fi_c)

  'S' singned
  'U' unsigned
  'C' const
  'V' volatile
  'R' restrict //##81
*/

class EncodedType implements TokenId, TypeId {
    private static final int SIZE = 8;
    private int pos = SIZE;
    private byte[] buffer = new byte[SIZE];
    //SF030531[
    private long arrayParamSize = 0; // array parameter size
  // ex. func(int p[2][4],int q[][3],int *r)
  // int p[2][4] => int (*q)[4] (arrayParamSize==2)
  // int q[][3]  => int (*p)[3] (arrayParamSize==-1)
  // int *r      => not changed (arrayParamSize==0)
    //SF030531]
    private int storageClass = S_NONE; //SF041212

    void clear() {
        pos = buffer.length;
    }

    char top() {
  return (char)(buffer[pos] & 0xFF);
    }

    void copy(EncodedType etype) {
  clear();
  insert(etype);
    }

    void insert(char c) {
  if (pos == 0)
      increase();

  buffer[--pos] = (byte)c;
    }

    void insert(String s) {
  int len = s.length();
  while (pos - len < 0)
      increase();

  while (len > 0)
      buffer[--pos] = (byte)s.charAt(--len);
    }

    void insert(byte[] b) {
  int len = b.length;
  while (pos - len < 0)
      increase();

  while (len > 0)
      buffer[--pos] = b[--len];
    }

    void insert(EncodedType etype) {
  byte[] src = etype.buffer;
  int srcPos = etype.pos;
  int srcLen = src.length;
  int len = srcLen - srcPos;
  while (pos - len < 0)
      increase();

  while (srcLen > srcPos)
      buffer[--pos] = src[--srcLen];
    }

    void insertCv(int cv) {
  //SF040909[
  //if (cv == CONST)
  //    insert(CONST_T);
  //else if (cv == VOLATILE)
  //    insert(VOLATILE_T);
  //else if (cv == RESTRICT)
  //    insert(RESTRICT_T);
  int cv_t;
  switch( cv )
  {
  case CONST:    cv_t = CONST_T; break;
  case VOLATILE: cv_t = VOLATILE_T; break;
  case RESTRICT: cv_t = RESTRICT_T; break; //##81
  default:       return;
  }
  int i;
  for( i=pos; i<buffer.length; i++ )
    if( buffer[i]==ARRAY_T
    ||  buffer[i]==NO_DIMENSION_T
    ||  '0'<=buffer[i] && buffer[i]<='9' )
      continue;
    else
      break;

  if( buffer[i]==CONST_T )
  {
    if( cv_t==CONST_T || buffer[i+1]==VOLATILE_T
        || buffer[i+1]==RESTRICT_T )  //##81
      return;
  }
  else if( buffer[i]==VOLATILE_T )
  {
    if( cv_t==VOLATILE_T || buffer[i+1]==CONST_T
        || buffer[i+1]==RESTRICT_T )  //##81
      return;
  }
  //##81 BEGIN
  else if( buffer[i]==RESTRICT_T )
  {
    if( cv_t==RESTRICT_T || buffer[i+1]==CONST_T
        || buffer[i+1]==VOLATILE_T )  //##81
      return;
  }
  //##81 END
  insertCv(i,cv_t);
  //SF040909]
    }

    //SF040909[
    void insertCv(int insertpos,int cv_t)
    {
  if( pos==0 )
  {
    insertpos += buffer.length;
    increase();
  }
  for( int i=pos; i<insertpos; i++ )
    buffer[i-1] = buffer[i];
  buffer[insertpos-1] = (byte)cv_t;
  pos--;
    }
    //SF040909]

    //##22 void insertDim(int d) {
   void insertDim(long d) {  //##22
  if (d < 0)
      insert(NO_DIMENSION_T);
  else if (d == 0)
      insert('0');
  else
      while (d > 0) {
    insert((char)('0' + d % 10));
    d = d / 10;
      }

  insert(ARRAY_T);
    }

    /* If the type is struct, this method do nothing.
     */
    void setArraySize(int n, Lex lex) throws ParseError {
  char c, c1 = 0, c2 = 0;
  byte[] type = buffer;
  int len = type.length;
  if (lex.fDbgLevel > 3) //##67
    lex.debug.print(5, " setArraySize "+n+" len "+len+" "+type);
  for (int i = pos; i < len; ++i) {
      c = (char)(type[i] & 0xFF);
      if (c == CONST_T || c == VOLATILE_T
          ||c == RESTRICT_T) //##81
    c1 = c;
      else if (c == SIGNED_T || c == UNSIGNED_T)
    c2 = c;
      else if (c == STRUCT_BEGIN)
    return;		// do nothing
      else if (c == ARRAY_T) {
    if ((type[i + 1] & 0xFF) == NO_DIMENSION_T) {
        pos = i + 2;
        insertDim(n);
        if (c1 != 0)
      insert(c1);
        if (c2 != 0)
      insert(c2);
        return;
    }
    else if (getArraySize(type, i + 1) >= n)
        return;
    else
        throw new ParseError(
      lex, "excess elements in array initializer "
      + n + " size " + getArraySize(type, i+1));
      }
      else if ((c == POINTER_T)&&((type[i+1] & 0xFF) == CHAR_T)) {
    setArraySizeIfCharArray(n, lex);
    return;
      }
      throw new ParseError(lex, "incompatible initial value");
  }
    }

    private static int getArraySize(byte[] type, int i) {
  int s = 0;
  for (;;) {
      int c = type[i++] & 0xFF;
      if ('0' <= c && c <= '9')
    s = s * 10 + c - '0';
      else
    break;
  }

  return s;
    }

    int getArraySize() {
  return getArraySize(buffer, pos + 1);
    }

    void setArraySizeIfCharArray(int n, Lex lex) throws ParseError {
  char c, c1 = 0, c2 = 0;
  byte[] type = buffer;
  int len = type.length;
  if (lex.fDbgLevel > 3) //##67
    lex.debug.print(5," setArraySizeIfCharArray "+n+" len "+len+" "+type);
  for (int i = pos; i < len; ++i) {
            c = (char)(type[i] & 0xFF);
            if (c == CONST_T || c == VOLATILE_T
                || c== RESTRICT_T) //##81
                c1 = c;
      else if (c == SIGNED_T || c == UNSIGNED_T)
    c2 = c;
      else if (c == POINTER_T && getTypeChar(type, i + 1) == CHAR_T)
    return;
      else {
    if (c == ARRAY_T) {
        if ((type[i + 1] & 0xFF) == NO_DIMENSION_T) {
      if (getTypeChar(type, i + 2) == CHAR_T) {
          pos = i + 2;
          insertDim(n);
          if (c1 != 0)
        insert(c1);

          if (c2 != 0)
        insert(c2);

          return;
      }
        }
        else if (getArraySizeForChar(type, i + 1, lex) >= n)
      return;
        else if (getArraySizeForChar(type, i + 1, lex) == n-1) { //##18
          lex.warning.put("excess 1 char in array initiation " + lex.getString()); // JIS C p. 77 //##18
          return; //##18
        } //##18
        else
      throw new ParseError(lex,
        "excess elements in array initializer");
    }

    throw new ParseError(lex, "incompatible initial value");
      }
  }
    }

    private static int getArraySizeForChar(byte[] type, int i, Lex lex)
  throws ParseError
    {
  int s = 0;
  int c;
  for (;;) {
      c = type[i++] & 0xFF;
      if ('0' <= c && c <= '9')
    s = s * 10 + c - '0';
      else
    break;
  }

  while (c == CONST_T || c == VOLATILE_T || c == UNSIGNED_T || c == SIGNED_T
         || c == RESTRICT_T ) //##81
      c = type[i++] & 0xFF;

  if (c == CHAR_T)
      return s;
  else
      throw new ParseError(lex, "incompatible initial value");
    }

    boolean hasIncompleteArray() {
        for (int i = pos; i < buffer.length; ++i)
            if ((buffer[i] & 0xFF) == NO_DIMENSION_T)
                return true;
        return false;
    }

    void insertStruct(String name) {
  insert(STRUCT_END);
  insert(name);
  insert(STRUCT_BEGIN);
    }

    void insertUnion(String name) {
  insert(UNION_END);
  insert(name);
  insert(UNION_BEGIN);
    }

    void insertEnum(String name) {
  insert(ENUM_END);
  insert(name);
  insert(ENUM_BEGIN);
    }

    void insertArgTypes(OldFuncArgs args) {
  for (; args != null; args = args.next)
      insert(((Declarator)args.arg).getType());
    }

    boolean dereference() {
  int i = getTypeChar0(buffer, pos);
  int c = buffer[i] & 0xFF;
  if (c == POINTER_T) {
      pos = i + 1;
      //bePointer(); //SF030726
      return true;
  }
  if (c == ARRAY_T) {
      do {
    c = buffer[++i] & 0xFF;
      } while (('0' <= c && c <= '9') || c == '?');
      pos = i;
      //bePointer(); //SF030726
      return true;
  }
  else
      return false;
    }

    boolean toReturnType() {
  int i = getTypeChar0(buffer, pos);
  int c = buffer[i] & 0xFF;
  if (c == FUNCTION_T) {
      int len = buffer.length;
      while ((buffer[i++] & 0xFF) != RETURN_T)
    if (i >= len)
        return false;

      pos = i;
      return true;
  }
  else
      return false;
    }

    int getTypeChar() {
  return getTypeChar(buffer, pos);
    }

    /**
     * Returns a struct/union/enum name.
     */
    String getTagName() {
  return getTagName(buffer, pos);
    }

  static String getTagName(byte[] type, int pos) {
  int end;
  int i = getTypeChar0(type, pos);
  switch (type[i] & 0xFF) {
  case STRUCT_BEGIN :
      end = STRUCT_END;
      break;
  case UNION_BEGIN :
      end = UNION_END;
      break;
  case ENUM_BEGIN :
      end = ENUM_END;
      break;
  default :
      return null;
  }

  StringBuffer sbuf = new StringBuffer();
  int len = type.length;
  while (++i < len) {
      int c = type[i] & 0xFF;
      if (c == end)
    return sbuf.toString();
      else
    sbuf.append((char)c);
  }

  return null;
    }

    int save() { return buffer.length - pos; }

    void restore(int p) { pos = buffer.length - p; }

    private void increase() {
  int length = buffer.length;
  int oldPos = pos;
  byte[] buf2 = new byte[length * 2];
  System.arraycopy(buffer, oldPos, buf2, oldPos + length,
       length - oldPos);
  buffer = buf2;
  pos = oldPos + length;
    }

    byte[] get() {
  int len = buffer.length - pos;
  byte[] buf = new byte[len];
  System.arraycopy(buffer, pos, buf, 0, len);
  return buf;
    }

    /**
     * Returns the first character except const, volatile, signed,
     * or unsigned.
     *
     * @param type	encoded type
     */
  public static int getTypeChar(byte[] type, int start) {
  int i = getTypeChar0(type, start);
  if (i >= 0)
      return type[i] & 0xFF;
  else
      return VOID_T;		// error
    }

  private static int getTypeChar0(byte[] type, int start) {
  int len = type.length;
  for (int i = start; i < len; ++i) {
      int c = type[i] & 0xFF;
      if (c != CONST_T && c != VOLATILE_T
          && c != RESTRICT_T  //##81
    && c != SIGNED_T && c != UNSIGNED_T)
    return i;
  }

  return -1;	// error
    }

    /**
     * Changes to be a pointer type if the type is an array type.
     */
    public void bePointer() {
  if (!isArray())
      return;

  int i = getTypeChar0(buffer, pos);
  if (i < 0)
      return;	// error?

  int c = buffer[i] & 0xFF;
  if (c == ARRAY_T) {
      do {
    c = buffer[++i] & 0xFF;
      } while (('0' <= c && c <= '9') || c == '?');
      pos = i - 1;
      //##13 buffer[pos] = POINTER_T;
      buffer[pos] = (byte)POINTER_T; //##13
  }
    }

    public boolean isSigned() {
  byte[] type = buffer;
  int len = type.length;
  for (int i = pos; i < len; ++i) {
      int c = type[i] & 0xFF;
      if (c != CONST_T && c != VOLATILE_T
          && c != RESTRICT_T)  //##81
    //return c == SIGNED_T; //SF030212
    return c != UNSIGNED_T; //SF030212
  }

  return false;
    }

    boolean isFunction() {
  return isFunction(buffer, pos);
    }

    public static boolean isFunction(byte[] type, int pos) {
  return getTypeChar(type, pos) == FUNCTION_T;
    }

    boolean isPointer() {
  return isPointer(buffer, pos);
    }

    public static boolean isPointer(byte[] type, int pos) {
  int c = getTypeChar(type, pos);
  return c == POINTER_T || c == ARRAY_T;
    }

    boolean isArray() {
  return isArray(buffer, pos);
    }

    public static boolean isArray(byte[] type, int pos) {
  return getTypeChar(type, pos) == ARRAY_T;
    }

    public boolean isValue() {
  return isValue(buffer, pos);
    }

    public static boolean isValue(byte[] type, int pos) {
  int c = getTypeChar(type, pos);
  return c != FUNCTION_T && c != STRUCT_BEGIN && c != UNION_BEGIN;
    }

    public boolean isValueOrFunction() {
  int c = getTypeChar(buffer, pos);
  return c != STRUCT_BEGIN && c != UNION_BEGIN;
    }
//##83 BEGIN
boolean isLong() {
  return isLong(buffer, pos);
}

public static boolean isLong(byte[] type, int pos) {
  return getTypeChar(type, pos) == LONG_T;
}
//##83 END

    boolean isLongLong() {
  return isLongLong(buffer, pos);
    }

    public static boolean isLongLong(byte[] type, int pos) {
  return getTypeChar(type, pos) == LONG_LONG_T;
    }

    boolean isIndex() {
  return isIndex(buffer, pos);
    }

    /* LONG_LONG_T is not an index type.
     */
    public static boolean isIndex(byte[] type, int pos) {
  int c = getTypeChar(type, pos);
  return c == INT_T || c == CHAR_T || c == SHORT_T || c == LONG_T
      || c == LONG_LONG_T //SF021210
      || c == ENUM_BEGIN;
    }

    boolean isInteger() {
  return isInteger(buffer, pos);
    }

    public static boolean isInteger(byte[] type, int pos) {
  int c = getTypeChar(type, pos);
  return c == INT_T || c == CHAR_T || c == SHORT_T || c == LONG_T
      || c == LONG_LONG_T || c == ENUM_BEGIN;
    }

//##84 BEGIN
boolean toBePromotedToInteger() {
  return toBePromotedToInteger(buffer, pos);
    }
public static boolean toBePromotedToInteger(byte[] type, int pos) {
  int c = getTypeChar(type, pos);
  return  c == CHAR_T || c == SHORT_T || c == ENUM_BEGIN;
}

//##84 END

    boolean isNumber() {
  return isNumber(buffer, pos);
    }

    public static boolean isNumber(byte[] type, int pos) {
  int c = getTypeChar(type, pos);
  return c == INT_T || c == CHAR_T || c == SHORT_T || c == LONG_T
      || c == LONG_LONG_T || c == ENUM_BEGIN
      || c == FLOAT_T || c == DOUBLE_T || c == LONG_DOUBLE_T;
    }

    /** isDouble:
     * @return true if float, double, or long double.
     */
    public boolean isDouble() {
  return isDouble(buffer, pos);
    }

    public static boolean isDouble(byte[] type, int pos) {
  int c = getTypeChar(type, pos);
  return c == FLOAT_T || c == DOUBLE_T || c == LONG_DOUBLE_T;
    }

    // Add ifFloatType(), isDoubleType(), isLongDoubleType(),
    // because isDouble() returns true even for float and long double.
    public boolean isFloatType() {
  return getTypeChar(buffer, pos) == FLOAT_T;
    }
    public boolean isDoubleType() {
  return getTypeChar(buffer, pos) == DOUBLE_T;
    }
    public boolean isLongDoubleType() {
  return getTypeChar(buffer, pos) == LONG_DOUBLE_T;
    }

    //##17 BEGIN

    public EncodedType
    getArrayElemType() {
        int         lElemPos = pos;
        int         c;
        EncodedType etype3 = new EncodedType();
  int len = buffer.length;
  etype3.buffer = new byte[len];
  System.arraycopy(buffer, 0, etype3.buffer, 0, len);

        c = etype3.buffer[lElemPos] & 0xFF;
        if (c == ARRAY_T) {
          do {
            c = buffer[++lElemPos] & 0xFF;
          } while (('0' <= c && c <= '9') || c == '?');
          etype3.pos = lElemPos;
          return etype3;
        }else { // Error
    System.out.println("Non array in EncodedType.getArrayElemType"
            + this.toString());
          return null;
        }
    }

    public EncodedType
    getDeclaratorType( Declarator pDecl ) {
        int         pos;
        int         c;
        EncodedType etype3 = new EncodedType();
        byte[] lType = pDecl.getType();
  int len = lType.length;
  etype3.buffer = new byte[len];
  System.arraycopy(lType, 0, etype3.buffer, 0, len);
        pos = 0;
        c = etype3.buffer[pos] & 0xFF;
        do {
          c = etype3.buffer[pos++] & 0xFF;
        } while (c == 0);
        etype3.pos = pos-1;
        return etype3;
    }

    public boolean isStruct() {
  return getTypeChar(buffer, pos) == STRUCT_BEGIN;
    }

//##18 BEGIN

    boolean isChar() {
  return isChar(buffer, pos);
    }

    public static boolean isChar(byte[] type, int pos) {
  int c = getTypeChar(type, pos);
  return c == CHAR_T;
    }

    boolean isVoid() {
  return isVoid(buffer, pos);
    }

    public static boolean isVoid(byte[] type, int pos) {
  int c = getTypeChar(type, pos);
  return c == VOID_T;
    }

//##18 END

    public String toString() {
  StringBuffer sbuf = new StringBuffer();
  for (int i = pos; i < buffer.length; ++i)
      sbuf.append((char)buffer[i] + "-" + (int)buffer[i] + " ");
  return sbuf.toString();
    }

    //##17 END

    public static int FUNCTION_TYPE_SIZE = -1;

    /* This mehthod returns -1 if an error occurs.
     */
    //##22 public int computeSizeof(Parser parser) throws ParseError {
  public long computeSizeof(Parser parser) throws ParseError {  //##22
  return computeSizeof(parser, buffer, pos);
    }

  //##22 public static int computeSizeof(Parser parser, byte[] type, int pos)
  public static long computeSizeof(Parser parser, byte[] type, int pos)
  throws ParseError
{
  int c = getTypeChar(type, pos);
  //##77 BEGIN
  if (parser.lex.fDbgLevel > 3) { //##77
    parser.debug.print(5," computeSizeof " + type[pos] + " pos " +pos +
      " " + c + " " + (char)c + " " + java.lang.Character.forDigit(c, 10) +
      " value " + parser.evaluator.toSize[c]);
  } //##77 END
  if (c == CHAR_T)
      return parser.evaluator.toSize[CHAR_T];
  else if (c == SHORT_T)
      return parser.evaluator.toSize[SHORT_T];
  else if (c == INT_T || c == ENUM_BEGIN)
      return parser.evaluator.toSize[INT_T];
  else if (c == LONG_T)
      return parser.evaluator.toSize[LONG_T];
  else if (c == LONG_LONG_T)
      return parser.evaluator.toSize[LONG_LONG_T];
  else if (c == FLOAT_T)
      return parser.evaluator.toSize[FLOAT_T];
  else if (c == DOUBLE_T)
      return parser.evaluator.toSize[DOUBLE_T];
  else if (c == LONG_DOUBLE_T)
      return parser.evaluator.toSize[LONG_DOUBLE_T];
  else if (c == STRUCT_BEGIN) {
      long lSize = parser.sizeofStruct(getTagName(type, pos)); //##27
      if (parser.fDbgLevel > 3) //##67
        parser.debug.print(5, " computeSizeof " + lSize + " "); //##27
      return lSize;  //##27
  }
  if (c == UNION_BEGIN) {
      long lSize = parser.sizeofUnion(getTagName(type, pos)); //##27
      if (parser.fDbgLevel > 3) //##67
        parser.debug.print(5, " computeSizeof " + lSize + " "); //##27
      return lSize;  //##27
  }
  else if (c == POINTER_T) {
      return parser.evaluator.toSize[POINTER_T];
  }
  else if (c == ARRAY_T) {
      int i = pos + 1;
      c = type[i] & 0xFF;
      if (c == NO_DIMENSION_T) {
    //SF040420 return parser.evaluator.toSize[INT_T];
    return 0; //SF040420
      }
      else {
    //##22 int size = 0;
    long size = 0;  //##22
    while ('0' <= c && c <= '9') {
        size = size * 10 + c - '0';
        c = type[++i] & 0xFF;
    }
    //##22 return size * computeSizeof(parser, type, i);
    size = size * computeSizeof(parser, type, i);  //##22
    return size;  //##22
    }
  }
  else {
    if (parser.fDbgLevel > 3) //##77
      parser.debug.print(5, " computeSizeof return -1 \n"); //##77
    return -1;
  }
 }

    public static String toString(byte[] type) {
  StringBuffer sbuf = new StringBuffer();
  for (int i = 0; i < type.length; ++i)
      sbuf.append((char)type[i]);

  return sbuf.toString();
    }

    //SF030531[
    /**
     * get/set array parameter size.
     */
    public long getArrayParamSize() {
  return arrayParamSize;
    }
    public void setArrayParamSize(long s) {
  arrayParamSize = s;
    }
    //SF030531]

    //##040124 BEGIN
    public EncodedType getReturnValueType()
    {
      EncodedType etype3 = new EncodedType();

      int index, index3 = 0;
      if (this.isFunction()) {
        boolean lFound = false;
        int c = 0;
        int len = this.buffer.length;
        for (index = 0; index < len; index++) {
          c = this.buffer[index] & 0xFF;
          if (!lFound &&(c == RETURN_T)) {
            lFound = true;
            etype3.buffer = new byte[len - index + 1];
            continue;
          }
          if (lFound) {
            etype3.buffer[index3] = this.buffer[index];
            index3++;
          }
        }
        etype3.pos = 0;
      }
      return etype3;
    }
    //##040124 END

    //SF041212[
    void ellipsisToVoid()
    {
      if( buffer[pos  ]==FUNCTION_T
      &&  buffer[pos+1]==ELLIPSIS_T )
        buffer[pos+1] = VOID_T;
    }

    void setStorageClass(int s)
    {
      storageClass = s;
    }

    int getStorageClass()
    {
      return storageClass;
    }
    //SF041212]
}

