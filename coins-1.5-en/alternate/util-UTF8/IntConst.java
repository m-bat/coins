/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.util;

import java.math.BigInteger;

/**
 * 整数定数(不変な値クラス).
 * <P>このクラス自身は抽象クラスであるが、具象クラスを入れ子クラスとして持っており、ファクトリメソッドでインスタンスを生成することができる。
 * 各インスタンスは、指定された有限ビット数の2進数を表す。
 * 符号なしおよび2の補数による符号つきの演算を提供する。
 */
public abstract class IntConst implements Comparable {

// field

// ビット数
// privateにすると、入れ子クラスであってもサブクラスでは使えなくなってしまう
// (キャストすれば使えるがめんどう)
  /*private*/ final int size;

// static field

/** 8ビットの-1 */
  public static final IntConst I8_M1=L.I8_M1;
/** 8ビットの0 */
  public static final IntConst I8_0=L.I8_0;
/** 8ビットの1 */
  public static final IntConst I8_1=L.I8_1;
/** 8ビットの2 */
  public static final IntConst I8_2=L.I8_2;
/** 16ビットの-1 */
  public static final IntConst I16_M1=L.I16_M1;
/** 16ビットの0 */
  public static final IntConst I16_0=L.I16_0;
/** 16ビットの1 */
  public static final IntConst I16_1=L.I16_1;
/** 16ビットの2 */
  public static final IntConst I16_2=L.I16_2;
/** 32ビットの-1 */
  public static final IntConst I32_M1=L.I32_M1;
/** 32ビットの0 */
  public static final IntConst I32_0=L.I32_0;
/** 32ビットの1 */
  public static final IntConst I32_1=L.I32_1;
/** 32ビットの2 */
  public static final IntConst I32_2=L.I32_2;
// 演算用定数
  private static final BigInteger BIG_LONG_MASK=bigMask(64);

// member type

// longで表せる場合の実装
  private static class L extends IntConst {
    private long l;
    static final L I8_M1=new L(8,0xFFL);
    static final L I8_0=new L(8,0);
    static final L I8_1=new L(8,1);
    static final L I8_2=new L(8,2);
    static final L I16_M1=new L(16,0xFFFFL);
    static final L I16_0=new L(16,0);
    static final L I16_1=new L(16,1);
    static final L I16_2=new L(16,2);
    static final L I32_M1=new L(32,0xFFFFFFFFL);
    static final L I32_0=new L(32,0);
    static final L I32_1=new L(32,1);
    static final L I32_2=new L(32,2);
    private static final L[] I8_CONST={I8_M1,I8_0,I8_1,I8_2};
    private static final L[] I16_CONST={I16_M1,I16_0,I16_1,I16_2};
    private static final L[] I32_CONST={I32_M1,I32_0,I32_1,I32_2};
    private static final long MSB=0x8000000000000000L;
    private L(int size,long val) { super(size); l=val; }
    static L valueOfL(int size,long val) {
      if(val>=-1 && val<=2) {
        if(size==8) return I8_CONST[(int)(val+1)];
        if(size==16) return I16_CONST[(int)(val+1)];
        if(size==32) return I32_CONST[(int)(val+1)];
      }
      return new L(size,val&longMask(size));
    }
    public IntConst add(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(size,l+((L)c).l);
    }
    public IntConst sub(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(size,l-((L)c).l);
    }
    public IntConst mul(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(size,l*((L)c).l);
    }
    public IntConst divu(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      long x=l,y=((L)c).l;
      if(x>=0 && y>=0) return valueOfL(size,x/y);
      BigInteger x1=BigInteger.valueOf(x).and(BIG_LONG_MASK);
      BigInteger y1=BigInteger.valueOf(y).and(BIG_LONG_MASK);
      return valueOfL(size,x1.divide(y1).longValue());
    }
    public IntConst divs(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(size,signedLongValue()/c.signedLongValue());
    }
    public IntConst modu(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      long x=l,y=((L)c).l;
      if(x>=0 && y>=0) return valueOfL(size,x%y);
      BigInteger x1=BigInteger.valueOf(x).and(BIG_LONG_MASK);
      BigInteger y1=BigInteger.valueOf(y).and(BIG_LONG_MASK);
      return valueOfL(size,x1.remainder(y1).longValue());
    }
    public IntConst mods(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(size,signedLongValue()%c.signedLongValue());
    }
    public IntConst band(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(size,l&((L)c).l);
    }
    public IntConst bor(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(size,l|((L)c).l);
    }
    public IntConst bxor(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(size,l^((L)c).l);
    }
    public IntConst lsh(IntConst c) { return lsh(c.count(size)); }
    public IntConst rshu(IntConst c) { return rshu(c.count(size)); }
    public IntConst rshs(IntConst c) { return rshs(c.count(size)); }
    public IntConst lsh(int n) {
      if(n<0) throw new IllegalArgumentException(String.valueOf(n));
      return valueOfL(size,n<size ? l<<n : 0);
    }
    public IntConst rshu(int n) {
      if(n<0) throw new IllegalArgumentException(String.valueOf(n));
      return valueOfL(size,n<size ? l>>>n : 0);
    }
    public IntConst rshs(int n) {
      if(n<0) throw new IllegalArgumentException(String.valueOf(n));
      if(n>=size) n=size-1;
      return valueOfL(size,l<<(64-size)>>(64-size+n));
    }
    public IntConst neg() { return valueOfL(size,-l); }
    public IntConst bnot() { return valueOfL(size,~l); }
    public IntConst tsteq(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(s,l==((L)c).l ? 1 : 0);
    }
    public IntConst tstne(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(s,l!=((L)c).l ? 1 : 0);
    }
    public IntConst tstltu(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(s,(l^MSB)<(((L)c).l^MSB) ? 1 : 0);
    }
    public IntConst tstgtu(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(s,(l^MSB)>(((L)c).l^MSB) ? 1 : 0);
    }
    public IntConst tstleu(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(s,(l^MSB)<=(((L)c).l^MSB) ? 1 : 0);
    }
    public IntConst tstgeu(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(s,(l^MSB)>=(((L)c).l^MSB) ? 1 : 0);
    }
    public IntConst tstlts(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(s,signedLongValue()<c.signedLongValue() ? 1 : 0);
    }
    public IntConst tstgts(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(s,signedLongValue()>c.signedLongValue() ? 1 : 0);
    }
    public IntConst tstles(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(s,signedLongValue()<=c.signedLongValue() ? 1 : 0);
    }
    public IntConst tstges(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfL(s,signedLongValue()>=c.signedLongValue() ? 1 : 0);
    }
    public IntConst convzx(int s) {
      if(s<size) throw new IllegalArgumentException(this+" "+s);
      return valueOf(s,l);
    }
    public IntConst convsx(int s) {
      if(s<size) throw new IllegalArgumentException(this+" "+s);
      if(s<=64) return valueOfL(s,signedLongValue());
      return valueOf(s,signedBigValue());
    }
    public IntConst convit(int s) {
      if(s>size || s<=0) throw new IllegalArgumentException(this+" "+s);
      return valueOfL(s,l);
    }
    public FloatConst convuf(int msize,int esize) {
      if(l>=0) return FloatConst.valueOf(msize,esize,l);
      return FloatConst.valueOf(msize,esize,BigInteger.valueOf(l).and(BIG_LONG_MASK));
    }
    public FloatConst convsf(int msize,int esize) {
      return FloatConst.valueOf(msize,esize,signedLongValue());
    }
    public IntConst ifthenelse(IntConst t,IntConst f) {
      if(t.size!=f.size) throw new IllegalArgumentException(this+" "+t+' '+f);
      return l!=0 ? t : f;
    }
    public FloatConst ifthenelse(FloatConst t,FloatConst f) {
      if(t.msize()!=f.msize() || t.esize()!=f.esize()) throw new IllegalArgumentException(this+" "+t+' '+f);
      return l!=0 ? t : f;
    }
    public long longValue() { return l; }
    public long signedLongValue() { return l<<(64-size)>>(64-size); }
    public BigInteger bigValue() { return BigInteger.valueOf(l).and(BIG_LONG_MASK); }
    public BigInteger signedBigValue() { return BigInteger.valueOf(signedLongValue()); }
    public int compareTo(Object o) {
      IntConst c=(IntConst)o;
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return l==((L)c).l ? 0 : (l^MSB)<(((L)c).l^MSB) ? -1 : 1;
    }
    public int signedCompareTo(Object o) {
      IntConst c=(IntConst)o;
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return l==((L)c).l ? 0 : signedLongValue()<c.signedLongValue() ? -1 : 1;
    }
    public int signum() { return l==0 ? 0 : (l<<(64-size))<0 ? -1 : 1; }
    public boolean equals(Object o) {
      return o==this || o instanceof L && size==((L)o).size && l==((L)o).l;
    }
    public int hashCode() { return size*37+(int)((l>>>32)^l); }
    public String toString(int radix) {
      if(radix==10 && size<64) return String.valueOf(l);
      return bigValue().toString(radix);
    }
    public String toSignedString(int radix) {
      if(radix==10) return String.valueOf(l);
      return signedBigValue().toString(radix);
    }
  // private abstractがエラーになる影響で、これもprivateにできない
  // sizeはintなので結果はintに収まる
    /*private*/ int count(int size) { return l>=0 || l<size ? (int)l : size; }
  }

// BigIntegerを使った実装
  private static class B extends IntConst {
    private BigInteger b;
    private B(int size,BigInteger val) { super(size); b=val; }
    static B valueOfB(int size,BigInteger val) { return new B(size,val.and(bigMask(size))); }
    public IntConst add(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfB(size,b.add(((B)c).b));
    }
    public IntConst sub(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfB(size,b.subtract(((B)c).b));
    }
    public IntConst mul(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfB(size,b.multiply(((B)c).b));
    }
    public IntConst divu(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfB(size,b.divide(((B)c).b));
    }
    public IntConst divs(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfB(size,signedBigValue().divide(c.signedBigValue()));
    }
    public IntConst modu(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfB(size,b.remainder(((B)c).b));
    }
    public IntConst mods(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfB(size,signedBigValue().remainder(c.signedBigValue()));
    }
    public IntConst band(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfB(size,b.and(((B)c).b));
    }
    public IntConst bor(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfB(size,b.or(((B)c).b));
    }
    public IntConst bxor(IntConst c) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOfB(size,b.xor(((B)c).b));
    }
    public IntConst lsh(IntConst c) { return lsh(c.count(size)); }
    public IntConst rshu(IntConst c) { return rshu(c.count(size)); }
    public IntConst rshs(IntConst c) { return rshs(c.count(size)); }
    public IntConst lsh(int n) {
      if(n<0) throw new IllegalArgumentException(String.valueOf(n));
      return valueOfB(size,n<size ? b.shiftLeft(n) : BigInteger.ZERO);
    }
    public IntConst rshu(int n) {
      if(n<0) throw new IllegalArgumentException(String.valueOf(n));
      return valueOfB(size,b.shiftRight(n));
    }
    public IntConst rshs(int n) {
      if(n<0) throw new IllegalArgumentException(String.valueOf(n));
      return valueOfB(size,signedBigValue().shiftRight(n));
    }
    public IntConst neg() { return valueOfB(size,b.negate()); }
    public IntConst bnot() { return valueOfB(size,b.not()); }
    public IntConst tsteq(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOf(s,b.equals(((B)c).b) ? 1 : 0);
    }
    public IntConst tstne(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOf(s,!b.equals(((B)c).b) ? 1 : 0);
    }
    public IntConst tstltu(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOf(s,b.compareTo(((B)c).b)<0 ? 1 : 0);
    }
    public IntConst tstgtu(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOf(s,b.compareTo(((B)c).b)>0 ? 1 : 0);
    }
    public IntConst tstleu(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOf(s,b.compareTo(((B)c).b)<=0 ? 1 : 0);
    }
    public IntConst tstgeu(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOf(s,b.compareTo(((B)c).b)>=0 ? 1 : 0);
    }
    public IntConst tstlts(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOf(s,signedBigValue().compareTo(c.signedBigValue())<0 ? 1 : 0);
    }
    public IntConst tstgts(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOf(s,signedBigValue().compareTo(c.signedBigValue())>0 ? 1 : 0);
    }
    public IntConst tstles(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOf(s,signedBigValue().compareTo(c.signedBigValue())<=0 ? 1 : 0);
    }
    public IntConst tstges(IntConst c,int s) {
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return valueOf(s,signedBigValue().compareTo(c.signedBigValue())>=0 ? 1 : 0);
    }
    public IntConst convzx(int s) {
      if(s<size) throw new IllegalArgumentException(this+" "+s);
      return valueOfB(s,b);
    }
    public IntConst convsx(int s) {
      if(s<size) throw new IllegalArgumentException(this+" "+s);
      return valueOfB(s,signedBigValue());
    }
    public IntConst convit(int s) {
      if(s>size || s<=0) throw new IllegalArgumentException(this+" "+s);
      return valueOf(s,b);
    }
    public FloatConst convuf(int msize,int esize) {
      return FloatConst.valueOf(msize,esize,b);
    }
    public FloatConst convsf(int msize,int esize) {
      return FloatConst.valueOf(msize,esize,signedBigValue());
    }
    public IntConst ifthenelse(IntConst t,IntConst f) {
      if(t.size!=f.size) throw new IllegalArgumentException(this+" "+t+' '+f);
      return b.signum()!=0 ? t : f;
    }
    public FloatConst ifthenelse(FloatConst t,FloatConst f) {
      if(t.msize()!=f.msize() || t.esize()!=f.esize()) throw new IllegalArgumentException(this+" "+t+' '+f);
      return b.signum()!=0 ? t : f;
    }
    public long longValue() { return b.longValue(); }
    public long signedLongValue() { return b.longValue(); }
    public BigInteger bigValue() { return b; }
    public BigInteger signedBigValue() {
      return b.testBit(size-1) ? b.subtract(BigInteger.ONE.shiftLeft(size)) : b;
    }
    public int compareTo(Object o) {
      IntConst c=(IntConst)o;
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return b.compareTo(((B)c).b);
    }
    public int signedCompareTo(Object o) {
      IntConst c=(IntConst)o;
      if(c.size!=size) throw new IllegalArgumentException(this+" "+c);
      return signedBigValue().compareTo(c.signedBigValue());
    }
    public int signum() { return b.signum()==0 ? 0 : b.testBit(size-1) ? -1 : 1; }
    public boolean equals(Object o) {
      return o==this || o instanceof B && size==((B)o).size && b.equals(((B)o).b);
    }
    public int hashCode() { return size*37+b.hashCode(); }
    public String toString(int radix) { return b.toString(radix); }
    public String toSignedString(int radix) { return signedBigValue().toString(radix); }
  // private abstractがエラーになる影響で、これもprivateにできない
  // sizeはintなので結果はintに収まる
    /*private*/ int count(int size) {
      return b.compareTo(BigInteger.valueOf(size))<0 ? (int)b.longValue() : size;
    }
  }

// constructor

  private IntConst(int size) {
    this.size=size;
  }

// factory method

/** 指定されたビット数と値(long)を持つ整数定数を返す.
 * ビット数が64より大きいとき、上位ビットはゼロ拡張する。
 * なお、符号拡張したいときはvalueOf(size,BigInteger.valueOf(val))を使えばよい。
 * 値がintで指定された場合は64ビットまでは符号拡張されることに注意せよ。
 * @exception IllegalArgumentException ビット数が正でない場合 */
  public static IntConst valueOf(int size,long val) {
    if(size<=0) throw new IllegalArgumentException(String.valueOf(size));
    if(size<=64) return L.valueOfL(size,val);
    return B.valueOfB(size,BigInteger.valueOf(val).and(BIG_LONG_MASK));
  }
/** 指定されたビット数と値(BigInteger)を持つ整数定数を返す.
 * @exception IllegalArgumentException ビット数が正でない場合 */
  public static IntConst valueOf(int size,BigInteger val) {
    if(size<=0) throw new IllegalArgumentException(String.valueOf(size));
    if(size<=64) return L.valueOfL(size,val.longValue());
    // BigIntegerのサブクラスが使われていたら、コピーして改変を防ぐ
    if(val.getClass()!=BigInteger.class) val=new BigInteger(val.toByteArray());
    return B.valueOfB(size,val);
  }

// method

/** この整数定数のビット数を返す. */
  public int size() {
    return size;
  }
/** 値がthis+cの整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst add(IntConst c);
/** 値がthis-cの整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst sub(IntConst c);
/** 値がthis*cの整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst mul(IntConst c);
/** 値がthis/c(符号なし除算)の整数定数を返す.
 * 丸めは0方向に行う。
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合
 * @exception ArithmeticException c==0の場合 */
  public abstract IntConst divu(IntConst c);
/** 値がthis/c(符号つき除算)の整数定数を返す.
 * 丸めは0方向に行う。
 * 負の最大数を-1で割ったら、負の最大数となる。
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合
 * @exception ArithmeticException c==0の場合 */
  public abstract IntConst divs(IntConst c);
/** 値がthis%c(符号なし剰余)の整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合
 * @exception ArithmeticException c==0の場合 */
  public abstract IntConst modu(IntConst c);
/** 値がthis%c(符号つき剰余)の整数定数を返す.
 * 剰余の符号は被除数と一致または0となる。
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合
 * @exception ArithmeticException c==0の場合 */
  public abstract IntConst mods(IntConst c);
/** 値がthis&amp;cの整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst band(IntConst c);
/** 値がthis|cの整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst bor(IntConst c);
/** 値がthis^cの整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst bxor(IntConst c);
/** 値がthis&lt;&lt;cの整数定数を返す. */
  public abstract IntConst lsh(IntConst c);
/** 値がthis&gt;&gt;c(符号なし右シフト)の整数定数を返す. */
  public abstract IntConst rshu(IntConst c);
/** 値がthis&gt;&gt;c(符号つき右シフト)の整数定数を返す. */
  public abstract IntConst rshs(IntConst c);
/** 値がthis&lt;&lt;nの整数定数を返す.
 * @exception IllegalArgumentException nが負の場合 */
  public abstract IntConst lsh(int n);
/** 値がthis&gt;&gt;n(符号なし右シフト)の整数定数を返す.
 * @exception IllegalArgumentException nが負の場合 */
  public abstract IntConst rshu(int n);
/** 値がthis&gt;&gt;n(符号つき右シフト)の整数定数を返す.
 * @exception IllegalArgumentException nが負の場合 */
  public abstract IntConst rshs(int n);
/** 値が-thisの整数定数を返す. */
  public abstract IntConst neg();
/** 値が~thisの整数定数を返す. */
  public abstract IntConst bnot();
/** this==cのとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst tsteq(IntConst c,int s);
/** this!=cのとき1、そうでなければ0の、指定されたビット数の整数定数を返す. 
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst tstne(IntConst c,int s);
/** this&lt;c(符号なし比較)のとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst tstltu(IntConst c,int s);
/** this&gt;c(符号なし比較)のとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst tstgtu(IntConst c,int s);
/** this&lt;=c(符号なし比較)のとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst tstleu(IntConst c,int s);
/** this&gt;=c(符号なし比較)のとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst tstgeu(IntConst c,int s);
/** this&lt;c(符号つき比較)のとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst tstlts(IntConst c,int s);
/** this&gt;c(符号つき比較)のとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst tstgts(IntConst c,int s);
/** this&lt;=c(符号つき比較)のとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst tstles(IntConst c,int s);
/** this&gt;=c(符号つき比較)のとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public abstract IntConst tstges(IntConst c,int s);
/** この整数定数を指定されたビット数にゼロ拡張して返す.
 * @exception IllegalArgumentException 指定されたビット数のほうが小さい場合 */
  public abstract IntConst convzx(int s);
/** この整数定数を指定されたビット数に符号拡張して返す.
 * @exception IllegalArgumentException 指定されたビット数のほうが小さい場合 */
  public abstract IntConst convsx(int s);
/** この整数定数を指定されたビット数に縮小して返す.
 * @exception IllegalArgumentException 指定されたビット数のほうが大きい、または正でない場合 */
  public abstract IntConst convit(int s);
/** この整数定数を符号なしで浮動小数点定数に変換する.
 * この整数定数の符号なしの値を近似する、指定された仮数部のビット数と指数部のビット数を持つ浮動小数点定数を返す。 */
  public abstract FloatConst convuf(int msize,int esize);
/** この整数定数を符号つきで浮動小数点定数に変換する.
 * この整数定数の符号つきの値を近似する、指定された仮数部のビット数と指数部のビット数を持つ浮動小数点定数を返す。 */
  public abstract FloatConst convsf(int msize,int esize);
/** 指定された整数定数tとfのうち、この整数定数が0以外ならt、0ならfを返す.
 * @exception IllegalArgumentException tとfのビット数が異なる場合 */
  public abstract IntConst ifthenelse(IntConst t,IntConst f);
/** 指定された浮動小数点定数tとfのうち、この整数定数が0以外ならt、0ならfを返す.
 * @exception IllegalArgumentException tとfのビット数が異なる場合 */
  public abstract FloatConst ifthenelse(FloatConst t,FloatConst f);
/** この整数定数をintに変換する.
 * <!>32ビットより小さいときは、上位ビットは0になる。
 * 32ビットより大きいときは、下位32ビットだけを返す。 */
//  public int intValue() {
/** この整数定数を符号つきでintに変換する.
 * <!>32ビットより小さいときは、上位ビットは符号拡張される。
 * 32ビットより大きいときは、下位32ビットだけを返す。 */
//  public int signedIntValue() {
/** この整数定数をlongに変換する.
 * <!>64ビットより小さいときは、上位ビットは0になる。
 * 64ビットより大きいときは、下位64ビットだけを返す。 */
  public abstract long longValue();
/** この整数定数を符号つきでlongに変換する.
 * <!>64ビットより小さいときは、上位ビットは符号拡張される。
 * 64ビットより大きいときは、下位64ビットだけを返す。 */
  public abstract long signedLongValue();
/** この整数定数をBigIntegerに変換する.
 * この整数定数の符号なしの値を表す、負でないBigIntegerを返す。 */
  public abstract BigInteger bigValue();
/** この整数定数を符号つきでBigIntegerに変換する.
 * この整数定数の符号つきの値を表すBigIntegerを返す。 */
  public abstract BigInteger signedBigValue();
/** この整数定数をfloatに変換する. 
 * この整数定数の符号なしの値を近似するfloatの値を返す。 */
  //public float floatValue() {
/** この整数定数を符号つきでfloatに変換する. 
 * この整数定数の符号つきの値を近似するfloatの値を返す。 */
  //public float signedFloatValue() {
/** この整数定数をdoubleに変換する.
 * この整数定数の符号なしの値を近似するdoubleの値を返す。 */
  //public double doubleValue() {
/** この整数定数を符号つきでdoubleに変換する.
 * この整数定数の符号つきの値を近似するdoubleの値を返す。 */
  //public double signedDoubleValue() {
/** この整数定数と指定された整数定数を符号なしで比較する.
 * この整数定数がoより小さい場合は-1、等しい場合は0、大きい場合は1を返す。
 * @exception ClassCastException oが整数定数ではない場合
 * @exception IllegalArgumentException 指定された整数定数のビット数がこの整数定数と異なる場合 */
  public abstract int compareTo(Object o);
/** この整数定数と指定された整数定数を符号つきで比較する.
 * この整数定数がoより小さい場合は-1、等しい場合は0、大きい場合は1を返す。
 * @exception ClassCastException oが整数定数ではない場合
 * @exception IllegalArgumentException 指定された整数定数のビット数がこの整数定数と異なる場合 */
  public abstract int signedCompareTo(Object o);
/** この整数定数の符号要素を返す.
 * msbが立っていたら-1、ゼロなら0、それ以外は1を返す。 */
  public abstract int signum();
/** この整数定数と指定されたオブジェクトが等しいかどうかを返す. */
  public abstract boolean equals(Object o);
/** この整数定数のハッシュコードを返す. */
  public abstract int hashCode();
/** この整数定数の文字列表現を返す. */
  public String toString() {
    return "(IntConst I"+size+' '+toString(10)+')';
  }
/** 指定された基数による、この整数定数の文字列表現を返す.
 * 基数がCharacter.MIN_RADIX(2)〜Character.MAX_RADIX(36)の範囲外の場合は、10を用いる(BigInteger.toString(int)と同じ)。
 * Character.forDigitによる数字から文字へのマッピングが使用される。 */
  public abstract String toString(int radix);
/** 指定された基数による、この整数定数の符号つきの文字列表現を返す.
 * 基数がCharacter.MIN_RADIX(2)〜Character.MAX_RADIX(36)の範囲外の場合は、10を用いる(BigInteger.toString(int)と同じ)。
 * Character.forDigitによる数字から文字へのマッピングが使用される。 */
  public abstract String toSignedString(int radix);
// シフトカウントを0以上size以下に飽和させる
// 入れ子クラスでだけ実装したいが、private abstractはエラーになる
  /*private*/ abstract int count(int size);
// 指定されたビット数のlongのマスク
  private static long longMask(int size) {
    return size==64 ? -1 : (1L<<size)-1;
  }
// 指定されたビット数のBigIntegerのマスク
  private static BigInteger bigMask(int size) {
    return BigInteger.ONE.shiftLeft(size).subtract(BigInteger.ONE);
  }

}
