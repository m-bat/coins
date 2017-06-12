/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.math.BigDecimal;

/**
   This class is for Constant Folding.
   The evaluation of expressions in Constant Folding uses the
   method in this class.
   The method for evaluation is depend on the type of the expression.
   Then 7 types of method is in this class.

   Byte for I8, Short for I16, Integer for I32,
   Long for I64, Float for F32, Double for F64,
   and BigDecimal for F128.
**/
class Evaluation{
/**
   The evaluation method for I32.

   @param int argument 1
   @param int argument 2
   @param String operator
   @return Number The result of evaluation
**/
public static Number calc(int arg1,int arg2,String op){
  Number result=null;
  if(op.equals("==")){
    if(arg1==arg2){
      Integer i=new Integer(1);
      result=(Number)i;
    }
    else{
      Integer i=new Integer(0);
      result=(Number)i;
    }
  }
  else if(op.equals("!=")){
    if(arg1!=arg2){
      Integer i=new Integer(1);
      result=(Number)i;
    }
    else{
      Integer i=new Integer(0);
      result=(Number)i;
    }
  }
  else if(op.equals("<")){
    if(arg1<arg2){
      Integer i=new Integer(1);
      result=(Number)i;
    }
    else{
      Integer i=new Integer(0);
      result=(Number)i;
    }
  }
  else if(op.equals("<=")){
    if(arg1<=arg2){
      Integer i=new Integer(1);
      result=(Number)i;
    }
    else{
      Integer i=new Integer(0);
      result=(Number)i;
    }
  }
  else if(op.equals(">")){
    if(arg1>arg2){
      Integer i=new Integer(1);
      result=(Number)i;
    }
    else{
      Integer i=new Integer(0);
      result=(Number)i;
    }
  }
  else if(op.equals(">=")){
    if(arg1>=arg2){
      Integer i=new Integer(1);
      result=(Number)i;
    }
    else{
      Integer i=new Integer(0);
      result=(Number)i;
    }
  }
  else if(op.equals("+")){
    Integer b=new Integer((int)(arg1+arg2));
    result=(Number)b;
  }
  else if(op.equals("-")){
    Integer b=new Integer((int)(arg1-arg2));
    result=(Number)b;
  }
  else if(op.equals("*")){
    Integer b=new Integer((int)(arg1*arg2));
    result=(Number)b;
  }
  else if(op.equals("/")){
    if(arg2==0){
      System.err.println("divide by 0!");
      System.exit(-1);
    }
    else{
      Integer b=new Integer((int)(arg1/arg2));
      result=(Number)b;
    }
  }
  else if(op.equals("%")){
    if(arg2==0){
      System.err.println("divide by 0!");
      System.exit(-1);
    }
    else{
      Integer b=new Integer((int)(arg1%arg2));
      result=(Number)b;
    }
  }
  else if(op.equals("&")){
    Integer b=new Integer((int)(arg1&arg2));
    result=(Number)b;
  }
  else if(op.equals("^")){
    Integer b=new Integer((int)(arg1^arg2));
    result=(Number)b;
  }
  else if(op.equals("|")){
    Integer b=new Integer((int)(arg1|arg2));
    result=(Number)b;
  }
  else if(op.equals("~")){
    /*
    Integer b=new Integer((int)(arg1~arg2));
    result=(Number)b;
    */
  }
  else if(op.equals("<<")){
    Integer b=new Integer((int)(arg1<<arg2));
    result=(Number)b;
  }
  else if(op.equals(">>")){
    Integer b=new Integer((int)(arg1>>arg2));
    result=(Number)b;
  }

  else if(op.equals("byte")){
    Integer i=new Integer((int)(arg2));
    Byte b=new Byte(i.byteValue());
    result=(Number)b;
  }
  else if(op.equals("short")){
    Integer i=new Integer((int)(arg2));
    Short b=new Short(i.shortValue());
    result=(Number)b;
  }
  else if(op.equals("int")){
    Integer b=new Integer((int)(arg2));
    result=(Number)b;
  }
  else if(op.equals("long")){
    Integer i=new Integer((int)(arg2));
    Long b=new Long(i.longValue());
    result=(Number)b;
  }
  else if(op.equals("float")){
    Integer i=new Integer((int)(arg2));
    Float b=new Float(i.floatValue());
    result=(Number)b;
  }
  else if(op.equals("double")){
    Integer i=new Integer((int)(arg2));
    Double b=new Double(i.doubleValue());
    result=(Number)b;
  }

  //System.err.println("xxx "+result);

  return(result);
}


public static Number calc(long arg1,long arg2,String op){
  Number result=null;
  if(op.equals("==")){
    if(arg1==arg2){
      Long i=new Long(1);
      result=(Number)i;
    }
    else{
      Long i=new Long(0);
      result=(Number)i;
    }
  }
  else if(op.equals("!=")){
    if(arg1!=arg2){
      Long i=new Long(1);
      result=(Number)i;
    }
    else{
      Long i=new Long(0);
      result=(Number)i;
    }
  }
  else if(op.equals("<")){
    if(arg1<arg2){
      Long i=new Long(1);
      result=(Number)i;
    }
    else{
      Long i=new Long(0);
      result=(Number)i;
    }
  }
  else if(op.equals("<=")){
    if(arg1<=arg2){
      Long i=new Long(1);
      result=(Number)i;
    }
    else{
      Long i=new Long(0);
      result=(Number)i;
    }
  }
  else if(op.equals(">")){
    if(arg1>arg2){
      Long i=new Long(1);
      result=(Number)i;
    }
    else{
      Long i=new Long(0);
      result=(Number)i;
    }
  }
  else if(op.equals(">=")){
    if(arg1>=arg2){
      Long i=new Long(1);
      result=(Number)i;
    }
    else{
      Long i=new Long(0);
      result=(Number)i;
    }
  }
  else if(op.equals("+")){
    Long b=new Long((long)(arg1+arg2));
    result=(Number)b;
  }
  else if(op.equals("-")){
    Long b=new Long((long)(arg1-arg2));
    result=(Number)b;
  }
  else if(op.equals("*")){
    Long b=new Long((long)(arg1*arg2));
    result=(Number)b;
  }
  else if(op.equals("/")){
    if(arg2==0){
      System.err.println("divide by 0!");
      System.exit(-1);
    }
    else{
      Long b=new Long((long)(arg1/arg2));
      result=(Number)b;
    }
  }
  else if(op.equals("%")){
    if(arg2==0){
      System.err.println("divide by 0!");
      System.exit(-1);
    }
    else{
      Long b=new Long((long)(arg1%arg2));
      result=(Number)b;
    }
  }
  else if(op.equals("&")){
    Long b=new Long((long)(arg1&arg2));
    result=(Number)b;
  }
  else if(op.equals("^")){
    Long b=new Long((long)(arg1^arg2));
    result=(Number)b;
  }
  else if(op.equals("|")){
    Long b=new Long((long)(arg1|arg2));
    result=(Number)b;
  }
  else if(op.equals("~")){
    /*
    Long b=new Long((long)(arg1~arg2));
    result=(Number)b;
    */
  }
  else if(op.equals("<<")){
    Long b=new Long((long)(arg1<<arg2));
    result=(Number)b;
  }
  else if(op.equals(">>")){
    Long b=new Long((long)(arg1>>arg2));
    result=(Number)b;
  }

  else if(op.equals("byte")){
    Long i=new Long((long)(arg2));
    Byte b=new Byte(i.byteValue());
    result=(Number)b;
  }
  else if(op.equals("short")){
    Long i=new Long((long)(arg2));
    Short b=new Short(i.shortValue());
    result=(Number)b;
  }
  else if(op.equals("int")){
    Long b=new Long((long)(arg2));
    result=(Number)b;
  }
  else if(op.equals("long")){
    Long i=new Long((long)(arg2));
    Long b=new Long(i.longValue());
    result=(Number)b;
  }
  else if(op.equals("float")){
    Long i=new Long((long)(arg2));
    Float b=new Float(i.floatValue());
    result=(Number)b;
  }
  else if(op.equals("double")){
    Long i=new Long((long)(arg2));
    Double b=new Double(i.doubleValue());
    result=(Number)b;
  }
  //System.err.println("xxx "+result);

  return(result);
}


public static Number calc(float arg1,float arg2,String op){
  Number result=null;
  if(op.equals("==")){
    if(arg1==arg2){
      Float i=new Float(1);
      result=(Number)i;
    }
    else{
      Float i=new Float(0);
      result=(Number)i;
    }
  }
  else if(op.equals("!=")){
    if(arg1!=arg2){
      Float i=new Float(1);
      result=(Number)i;
    }
    else{
      Float i=new Float(0);
      result=(Number)i;
    }
  }
  else if(op.equals("<")){
    if(arg1<arg2){
      Float i=new Float(1);
      result=(Number)i;
    }
    else{
      Float i=new Float(0);
      result=(Number)i;
    }
  }
  else if(op.equals("<=")){
    if(arg1<=arg2){
      Float i=new Float(1);
      result=(Number)i;
    }
    else{
      Float i=new Float(0);
      result=(Number)i;
    }
  }
  else if(op.equals(">")){
    if(arg1>arg2){
      Float i=new Float(1);
      result=(Number)i;
    }
    else{
      Float i=new Float(0);
      result=(Number)i;
    }
  }
  else if(op.equals(">=")){
    if(arg1>=arg2){
      Float i=new Float(1);
      result=(Number)i;
    }
    else{
      Float i=new Float(0);
      result=(Number)i;
    }
  }
  else if(op.equals("+")){
    Float b=new Float((float)(arg1+arg2));
    result=(Number)b;
  }
  else if(op.equals("-")){
    Float b=new Float((float)(arg1-arg2));
    result=(Number)b;
  }
  else if(op.equals("*")){
    Float b=new Float((float)(arg1*arg2));
    result=(Number)b;
  }
  else if(op.equals("/")){
    if(arg2==0){
      System.err.println("divide by 0!");
      System.exit(-1);
    }
    else{
      Float b=new Float((float)(arg1/arg2));
      result=(Number)b;
    }
  }
  else if(op.equals("%")){
    if(arg2==0){
      System.err.println("divide by 0!");
      System.exit(-1);
    }
    else{
      Float b=new Float((float)(arg1%arg2));
      result=(Number)b;
    }
  }
  else if(op.equals("byte")){
    Float i=new Float((float)(arg2));
    Byte b=new Byte(i.byteValue());
    result=(Number)b;
  }
  else if(op.equals("short")){
    Float i=new Float((float)(arg2));
    Short b=new Short(i.shortValue());
    result=(Number)b;
  }
  else if(op.equals("int")){
    Float b=new Float((float)(arg2));
    result=(Number)b;
  }
  else if(op.equals("long")){
    Float i=new Float((float)(arg2));
    Long b=new Long(i.longValue());
    result=(Number)b;
  }
  else if(op.equals("float")){
    Float i=new Float((float)(arg2));
    Float b=new Float(i.floatValue());
    result=(Number)b;
  }
  else if(op.equals("double")){
    Float i=new Float((float)(arg2));
    Double b=new Double(i.doubleValue());
    result=(Number)b;
  }

  /*
  else if(op.equals("&")){
    Float b=new Float((float)(arg1&arg2));
    result=(Number)b;
  }
  else if(op.equals("^")){
    Float b=new Float((float)(arg1^arg2));
    result=(Number)b;
  }
  else if(op.equals("|")){
    Float b=new Float((float)(arg1|arg2));
    result=(Number)b;
  }
  else if(op.equals("~")){
    Float b=new Float((float)(arg1~arg2));
    result=(Number)b;
  }
  else if(op.equals("<<")){
    Float b=new Float((float)(arg1<<arg2));
    result=(Number)b;
  }
  else if(op.equals(">>")){
    Float b=new Float((float)(arg1>>arg2));
    result=(Number)b;
  }
  */

  //System.err.println("xxx "+result);

  return(result);
}



public static Number calc(double arg1,double arg2,String op){
  Number result=null;
  if(op.equals("==")){
    if(arg1==arg2){
      Double i=new Double(1);
      result=(Number)i;
    }
    else{
      Double i=new Double(0);
      result=(Number)i;
    }
  }
  else if(op.equals("!=")){
    if(arg1!=arg2){
      Double i=new Double(1);
      result=(Number)i;
    }
    else{
      Double i=new Double(0);
      result=(Number)i;
    }
  }
  else if(op.equals("<")){
    if(arg1<arg2){
      Double i=new Double(1);
      result=(Number)i;
    }
    else{
      Double i=new Double(0);
      result=(Number)i;
    }
  }
  else if(op.equals("<=")){
    if(arg1<=arg2){
      Double i=new Double(1);
      result=(Number)i;
    }
    else{
      Double i=new Double(0);
      result=(Number)i;
    }
  }
  else if(op.equals(">")){
    if(arg1>arg2){
      Double i=new Double(1);
      result=(Number)i;
    }
    else{
      Double i=new Double(0);
      result=(Number)i;
    }
  }
  else if(op.equals(">=")){
    if(arg1>=arg2){
      Double i=new Double(1);
      result=(Number)i;
    }
    else{
      Double i=new Double(0);
      result=(Number)i;
    }
  }
  else if(op.equals("+")){
    Double b=new Double((double)(arg1+arg2));
    result=(Number)b;
  }
  else if(op.equals("-")){
    Double b=new Double((double)(arg1-arg2));
    result=(Number)b;
  }
  else if(op.equals("*")){
    Double b=new Double((double)(arg1*arg2));
    result=(Number)b;
  }
  else if(op.equals("/")){
    if(arg2==0){
      System.err.println("divide by 0!");
      System.exit(-1);
    }
    else{
      Double b=new Double((double)(arg1/arg2));
      result=(Number)b;
    }
  }
  else if(op.equals("%")){
    if(arg2==0){
      System.err.println("divide by 0!");
      System.exit(-1);
    }
    else{
      Double b=new Double((double)(arg1%arg2));
      result=(Number)b;
    }
  }
  else if(op.equals("byte")){
    Double i=new Double((double)(arg2));
    Byte b=new Byte(i.byteValue());
    result=(Number)b;
  }
  else if(op.equals("short")){
    Double i=new Double((double)(arg2));
    Short b=new Short(i.shortValue());
    result=(Number)b;
  }
  else if(op.equals("int")){
    Double b=new Double((double)(arg2));
    result=(Number)b;
  }
  else if(op.equals("long")){
    Double i=new Double((double)(arg2));
    Long b=new Long(i.longValue());
    result=(Number)b;
  }
  else if(op.equals("float")){
    Double i=new Double((double)(arg2));
    Float b=new Float(i.floatValue());
    result=(Number)b;
  }
  else if(op.equals("double")){
    Double i=new Double((double)(arg2));
    Double b=new Double(i.doubleValue());
    result=(Number)b;
  }

  /*
  else if(op.equals("&")){
    Double b=new Double((double)(arg1&arg2));
    result=(Number)b;
  }
  else if(op.equals("^")){
    Double b=new Double((double)(arg1^arg2));
    result=(Number)b;
  }
  else if(op.equals("|")){
    Double b=new Double((double)(arg1|arg2));
    result=(Number)b;
  }
  else if(op.equals("~")){
    Double b=new Double((double)(arg1~arg2));
    result=(Number)b;
  }
  else if(op.equals("<<")){
    Double b=new Double((double)(arg1<<arg2));
    result=(Number)b;
  }
  else if(op.equals(">>")){
    Double b=new Double((double)(arg1>>arg2));
    result=(Number)b;
  }
  */

  //System.err.println("xxx "+result);

  return(result);
}
}
