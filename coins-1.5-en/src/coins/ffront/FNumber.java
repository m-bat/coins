/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

/**
  Constant evaluator
 */


public abstract class FNumber{
  abstract public int     intValue();
  abstract public float   floatValue();
  abstract public double  doubleValue();
  abstract public Complex complexValue();

  static public FNumber make(int     i){return new FInteger(i);}
  static public FNumber make(float   i){return new FFloat(i);}
  static public FNumber make(double  i){return new FDouble(i);}
  static public FNumber make(double i,double j){
    return new FComplex(i,j);
  }
  static public FNumber make(Complex c){
    return new FComplex(c.real,c.img);
  }
  private FNumber invoke(String op, FNumber n){
    FNumber type = higherType(n);
    
    FNumber a = type.to(this);
    FNumber b = type.to(n);

    Object o[] = {b};
    Class  c[] = {a.getClass()};
    try{
      return (FNumber)a.getClass().getMethod(op,c).invoke(a,o);
    }
    catch(Exception e){
      return null;
    }
  }
  
  public FNumber add(FNumber n){ return invoke("add",n); }
  public FNumber sub(FNumber n){ return invoke("sub",n); }
  public FNumber mul(FNumber n){ return invoke("mul",n); }
  public FNumber div(FNumber n){ return invoke("div",n); }
  public FNumber pow(FNumber n){ return invoke("pow",n); }
  public abstract FNumber to(FNumber n);
  public abstract FNumber neg();
  
  protected FNumber higherType(FNumber n){
    if(cmpType(n) > 0){
      return n;
    }
    else return this;
  }

  /**
    -1 : low  type
     0 : same type
     1 : over type
   */
  protected int cmpType(FNumber n){
    int a = checkType(this);
    int b = checkType(n);
    if(a == b){ return 0; }
    if(a <  b){ return 1; }
    else      { return -1;}
  }
  protected int checkType(FNumber n){
    if(n instanceof FInteger){     return 1; }
    else if(n instanceof FFloat){  return 2; }
    else if(n instanceof FDouble){ return 3; }
    else if(n instanceof FComplex){return 4; }
    return -1;
  }

  void p(String str){
    System.out.println(str);
  }
}

class FInteger extends FNumber{
  int n_;
  FInteger(int i){
    n_ = i;
  }

  public FNumber add(FInteger n){ return make(n_ + n.n_);  }
  public FNumber mul(FInteger n){ return make(n_ * n.n_);  }
  public FNumber sub(FInteger n){ return make(n_ - n.n_);  }
  public FNumber div(FInteger n){ return make(n_ / n.n_);  }
  
  public FNumber neg()         {return make(-1 * n_);}
  public int     intValue()    {return n_;}
  public float   floatValue()  {return n_;}
  public double  doubleValue() {return n_;}
  public Complex complexValue(){return new Complex(n_,0);}
  public FNumber to(FNumber n){
    return make(n.intValue());
  }
  public String toString(){
    return Integer.toString(n_,10);
  }
}

class FFloat extends FNumber{
  float n_;
  FFloat(float f){
    n_ = f;
  }
  public FNumber neg()         {return make(-1 * n_);}
  public int     intValue()    {return (int)n_;}
  public float   floatValue()  {return (float)n_;}
  public double  doubleValue() {return (double)n_;}
  public Complex complexValue(){return new Complex((double)n_,0);}
  public FNumber to(FNumber n){
    return make(n.floatValue());
  }

  public FNumber add(FFloat  n){ return make(n_ + n.n_); }
  public FNumber mul(FFloat  n){ return make(n_ * n.n_); }
  public FNumber sub(FFloat  n){ return make(n_ - n.n_); }
  public FNumber div(FFloat  n){ return make(n_ / n.n_); }

  public String toString(){
    return Float.toString(n_);
  }

}
class FDouble extends FNumber{
  double n_;
  FDouble(double d){
    n_ = d;
  }
  public FNumber neg()         {return make(-1 * n_);}
  public int     intValue()    {return (int)n_;}
  public float   floatValue()  {return (float)n_;}
  public double  doubleValue() {return (double)n_;}
  public Complex complexValue(){return new Complex((double)n_,0);}
  public FNumber to(FNumber n){
    return make(n.doubleValue());
  }

  public FNumber add(FDouble n){ return make(n_ + n.n_); }
  public FNumber mul(FDouble n){ return make(n_ * n.n_); }
  public FNumber sub(FDouble n){ return make(n_ - n.n_); }
  public FNumber div(FDouble n){ return make(n_ / n.n_); }

  public String toString(){
    return Double.toString(n_);
  }
}
class FComplex extends FNumber{
  Complex n_;
  FComplex(double r,double i){
    n_ = new Complex(r,i);
  }
  public FNumber neg()         {return make(-1 * n_.real, -1 * n_.img);}
  public int     intValue()    {return (int)n_.real;}
  public float   floatValue()  {return (float)n_.real;}
  public double  doubleValue() {return (double)n_.real;}
  public Complex complexValue(){return n_;}

  public FNumber to(FNumber n){
    Complex c = n.complexValue();
    return make(c.real,c.img);
  }

  public FNumber add(FComplex n){ return make(n_.add(n.n_)); }
  public FNumber mul(FComplex n){ return make(n_.mul(n.n_)); }
  public FNumber sub(FComplex n){ return make(n_.sub(n.n_)); }
  public FNumber div(FComplex n){ return make(n_.div(n.n_)); }

  public String toString(){
    return n_.toString();
  }
}

class Complex{
  double real,img;
  Complex(double r,double i){
    real = r;
    img  = i;
  }
  Complex add(Complex c){
    return new Complex(real + c.real,img + c.img);
  }
  Complex sub(Complex c){
    return new Complex(real - c.real,img - c.img);
  }
  Complex mul(Complex c){
    return new Complex(real * c.real - (img * c.img),
                       real * c.img  + img  * c.real);
  }
  Complex div(Complex c){
    double d = c.real * c.real + c.img * c.img;
    return new Complex((real * c.real + img * c.img) / d,
                       (img * c.real  - real * c.img)/ d);
  }
  public String toString(){
    return "(" + real + "," + img + ")";
  }
}

class FNumberTest{
  public static void main(String arg[]){
    FNumber n1 = FNumber.make(3);
    FNumber n2 = FNumber.make(1.0);
    FNumber n3 = FNumber.make(3.34);
    FNumber n4 = FNumber.make(3,4);
    FNumber n5 = FNumber.make(5,6);

    System.out.println("value : " + n1);
    System.out.println("value : " + n1.add(n1));
    System.out.println("value : " + n1.sub(n1));
    System.out.println("value : " + n1.mul(n1));
    System.out.println("value : " + n1.div(n1));
    System.out.println("value : " + n1);
    System.out.println("-- ");
    System.out.println("value : " + n1);
    System.out.println("value : " + n1.add(n2));
    System.out.println("value : " + n1.sub(n2));
    System.out.println("value : " + n1.mul(n2));
    System.out.println("value : " + n1.div(n2));
    System.out.println("value : " + n1);

    System.out.println("-- ");
    System.out.println("value : " + n1);
    System.out.println("value : " + n1.add(n3));
    System.out.println("value : " + n1.sub(n3));
    System.out.println("value : " + n1.mul(n3));
    System.out.println("value : " + n1.div(n3));
    System.out.println("value : " + n1);

    System.out.println("-- ");
    System.out.println("value : " + n1);
    System.out.println("value : " + n1.add(n4));
    System.out.println("value : " + n1.sub(n4));
    System.out.println("value : " + n1.mul(n4));
    System.out.println("value : " + n1.div(n4));
    System.out.println("value : " + n1);

    System.out.println("-- ");
    System.out.println("value : " + n4);
    System.out.println("value : " + n4.add(n5));
    System.out.println("value : " + n4.sub(n5));
    System.out.println("value : " + n4.mul(n5));
    System.out.println("value : " + n4.div(n5));
    System.out.println("value : " + n4);
  }
}


