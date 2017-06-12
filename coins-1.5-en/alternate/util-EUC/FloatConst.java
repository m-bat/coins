/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.util;

import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * 浮動小数点定数(不変な値クラス).
 *
 * <P>このクラス自身は抽象クラスであるが、具象クラスを入れ子クラスとして持っており、ファクトリメソッドでインスタンスを生成することができる。
 * 各インスタンスは、指定されたビット数の仮数部と指数部を持つ2進浮動小数点数を表す。
 *
 * <P>このクラスは、IEEE 754に準拠した演算(例外、トラップを除く)を提供する。
 * ただし、四則演算、浮動小数点形式の変換、整数からの変換では、デフォルトの丸めモードである直近への丸めのみ使用できる。
 * 整数への変換では、丸めモードを指定可能である。
 *
 * <P>形式(仮数部と指数部のビット数)の異なるインスタンス同士の四則演算あるいは比較を行いたい場合は、明示的に形式を変換する必要がある。
 *
 * <P>以下に、よく使用される仮数部と指数部のビット数を示す。(*は隠れたビットを含む)
 * <TABLE BORDER>
 * <TR><TD><TH>仮数部<TH>指数部
 * <TR><TD>16ビット浮動小数点数<TD>11*<TD>5
 * <TR><TD>単精度<TD>24*<TD>8
 * <TR><TD>倍精度<TD>53*<TD>11
 * <TR><TD>x86拡張倍精度<TD>64<TD>15
 * <TR><TD>4倍精度<TD>113*<TD>15
 * </TABLE>
 */
public abstract class FloatConst {

// static field

/** 単精度の0.0 */
  public static final FloatConst F32_0=F.F32_0;
/** 単精度の1.0 */
  public static final FloatConst F32_1=F.F32_1;
/** 倍精度の0.0 */
  public static final FloatConst F64_0=D.F64_0;
/** 倍精度の1.0 */
  public static final FloatConst F64_1=D.F64_1;
// 仮数部のビット数の最小と最大
  private static final int M_MIN=3,M_MAX=0x3FFFFFFF;
// 指数部のビット数の最小と最大
  private static final int E_MIN=2,E_MAX=30;

// member type

/** 整数型に変換するときの丸めモードを表す型. */
  public abstract static class RoundingMode {
    RoundingMode() {}
    abstract long toLong(double d);
    abstract BigInteger round(BigInteger b,boolean s,int rs);
  }
/** 直近への丸めを表す定数 */
  public static final RoundingMode ROUND_NEAREST=new RoundingMode() {
    long toLong(double d) { return (long)StrictMath.rint(d); }
    BigInteger round(BigInteger b,boolean s,int rs) {
      if(rs>2 || rs==2 && b.testBit(0)) b=b.add(BigInteger.ONE);
      return b;
    }
  };
/** 負の方向への丸めを表す定数 */
  public static final RoundingMode ROUND_MINUS=new RoundingMode() {
    long toLong(double d) { return (long)StrictMath.floor(d); }
    BigInteger round(BigInteger b,boolean s,int rs) {
      if(s && rs!=0) b=b.add(BigInteger.ONE);
      return b;
    }
  };
/** 正の方向への丸めを表す定数 */
  public static final RoundingMode ROUND_PLUS=new RoundingMode() {
    long toLong(double d) { return (long)StrictMath.ceil(d); }
    BigInteger round(BigInteger b,boolean s,int rs) {
      if(!s && rs!=0) b=b.add(BigInteger.ONE);
      return b;
    }
  };
/** ゼロ方向への丸めを表す定数 */
  public static final RoundingMode ROUND_ZERO=new RoundingMode() {
    long toLong(double d) { return (long)d; }
    BigInteger round(BigInteger b,boolean s,int rs) { return b; }
  };

// floatの場合の実装
  private static final strictfp class F extends FloatConst {
    private final float f;
    static final int msize=24;
    static final int esize=8;
    static final F F32_0=new F(0.0f);
    static final F F32_1=new F(1.0f);
    private static final long MIN_LONG=1<<63; // longで正確に表せる最小値
    private static final long MAX_LONG=((1L<<(msize-1))-1)<<(64-msize); // longで正確に表せる最大値
    private F(float val) { f=val; }
    static F valueOf(float val) {
      if(Float.floatToIntBits(val)==0) return F32_0; // 正の0のみ
      if(val==1.0f) return F32_1;
      return new F(val);
    }
    static F valueOf(BigInteger m,int e,boolean s) {
      int i=((s ? -1 : 0)<<esize)+e;
      i=(i<<(msize-1))+(m.intValue()&((1<<(msize-1))-1));
      return F.valueOf(Float.intBitsToFloat(i));
    }
    public int msize() { return msize; }
    public int esize() { return esize; }
    public FloatConst add(FloatConst c) {
      if(!(c instanceof F)) throw new IllegalArgumentException(this+" "+c);
      return valueOf(f+((F)c).f);
    }
    public FloatConst sub(FloatConst c) {
      if(!(c instanceof F)) throw new IllegalArgumentException(this+" "+c);
      return valueOf(f-((F)c).f);
    }
    public FloatConst mul(FloatConst c) {
      if(!(c instanceof F)) throw new IllegalArgumentException(this+" "+c);
      return valueOf(f*((F)c).f);
    }
    public FloatConst div(FloatConst c) {
      if(!(c instanceof F)) throw new IllegalArgumentException(this+" "+c);
      return valueOf(f/((F)c).f);
    }
    public FloatConst neg() {
      return valueOf(-f);
    }
    public IntConst tsteq(FloatConst c,int size) {
      if(!(c instanceof F)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,f==((F)c).f ? 1 : 0);
    }
    public IntConst tstne(FloatConst c,int size) {
      if(!(c instanceof F)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,f!=((F)c).f ? 1 : 0);
    }
    public IntConst tstlts(FloatConst c,int size) {
      if(!(c instanceof F)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,f<((F)c).f ? 1 : 0);
    }
    public IntConst tstgts(FloatConst c,int size) {
      if(!(c instanceof F)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,f>((F)c).f ? 1 : 0);
    }
    public IntConst tstles(FloatConst c,int size) {
      if(!(c instanceof F)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,f<=((F)c).f ? 1 : 0);
    }
    public IntConst tstges(FloatConst c,int size) {
      if(!(c instanceof F)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,f>=((F)c).f ? 1 : 0);
    }
    public FloatConst convfx(int msize,int esize) {
      if(msize<F.msize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      if(esize<F.esize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      return valueOf(msize,esize,f);
    }
    public FloatConst convft(int msize,int esize) {
      if(msize>F.msize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      if(esize>F.esize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      return valueOf(msize,esize,f);
    }
    public IntConst convfi(int size,RoundingMode r) {
      if(size<=64 && f<=MAX_LONG && f>=MIN_LONG) return IntConst.valueOf(size,r.toLong(f));
      return IntConst.valueOf(size,toBigInteger(r));
    }
    public float floatValue() { return f; }
    public double doubleValue() { return f; }
    public BigDecimal toBigDecimal() { return new BigDecimal(f); }
    public BigInteger toBigInteger(RoundingMode r) {
      if(f<=MAX_LONG && f>=MIN_LONG) return BigInteger.valueOf(r.toLong(f));
      int i=Float.floatToIntBits(f);
      int e=(i>>(msize-1))&((1<<esize)-1);
      if(e>emax(esize)) throw new IllegalArgumentException(this.toString());
      int m=i&((1<<(msize-1))-1)|(1<<(msize-1));
      return BigInteger.valueOf(i<0 ? -m : m).shiftLeft(e-bias(esize)-msize+1);
    }
    public BigInteger toBigIntegerBits() {
      return BigInteger.valueOf(Float.floatToIntBits(f));
    }
    public BigInteger toExplicitBigIntegerBits() {
      int i=Float.floatToIntBits(f);
      int e=(i>>(msize-1))&((1<<esize)-1);
      int m=i&((1<<(msize-1))-1);
      if(e!=0) m|=1<<(msize-1);
      return BigInteger.valueOf(m|((long)(i&-(1<<(msize-1)))<<1));
    }
    public boolean equals(Object o) {
      return o==this ||
             o instanceof F && Float.floatToRawIntBits(f)==Float.floatToRawIntBits(((F)o).f);
    }
    public int hashCode() { return msize*37+Float.floatToRawIntBits(f); }
    public String toString() { return "(FloatConst F24+8 "+f+')'; }
    public String toDecimalString() { return String.valueOf(f); }
  }

// doubleの場合の実装
  private static final strictfp class D extends FloatConst {
    private final double d;
    static final int msize=53;
    static final int esize=11;
    static final D F64_0=new D(0.0);
    static final D F64_1=new D(1.0);
    private static final long MIN_LONG=1<<63; // longで正確に表せる最小値
    private static final long MAX_LONG=((1L<<(msize-1))-1)<<(64-msize); // longで正確に表せる最大値
    private D(double val) { d=val; }
    static D valueOf(double val) {
      if(Double.doubleToLongBits(val)==0L) return F64_0; // 正の0のみ
      if(val==1.0) return F64_1;
      return new D(val);
    }
    static D valueOf(BigInteger m,int e,boolean s) {
      long l=((s ? -1L : 0L)<<esize)+e;
      l=(l<<(msize-1))+(m.longValue()&((1L<<(msize-1))-1));
      return D.valueOf(Double.longBitsToDouble(l));
    }
    public int msize() { return msize; }
    public int esize() { return esize; }
    public FloatConst add(FloatConst c) {
      if(!(c instanceof D)) throw new IllegalArgumentException(this+" "+c);
      return valueOf(d+((D)c).d);
    }
    public FloatConst sub(FloatConst c) {
      if(!(c instanceof D)) throw new IllegalArgumentException(this+" "+c);
      return valueOf(d-((D)c).d);
    }
    public FloatConst mul(FloatConst c) {
      if(!(c instanceof D)) throw new IllegalArgumentException(this+" "+c);
      return valueOf(d*((D)c).d);
    }
    public FloatConst div(FloatConst c) {
      if(!(c instanceof D)) throw new IllegalArgumentException(this+" "+c);
      return valueOf(d/((D)c).d);
    }
    public FloatConst neg() {
      return valueOf(-d);
    }
    public IntConst tsteq(FloatConst c,int size) {
      if(!(c instanceof D)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,d==((D)c).d ? 1 : 0);
    }
    public IntConst tstne(FloatConst c,int size) {
      if(!(c instanceof D)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,d!=((D)c).d ? 1 : 0);
    }
    public IntConst tstlts(FloatConst c,int size) {
      if(!(c instanceof D)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,d<((D)c).d ? 1 : 0);
    }
    public IntConst tstgts(FloatConst c,int size) {
      if(!(c instanceof D)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,d>((D)c).d ? 1 : 0);
    }
    public IntConst tstles(FloatConst c,int size) {
      if(!(c instanceof D)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,d<=((D)c).d ? 1 : 0);
    }
    public IntConst tstges(FloatConst c,int size) {
      if(!(c instanceof D)) throw new IllegalArgumentException(this+" "+c);
      return IntConst.valueOf(size,d>=((D)c).d ? 1 : 0);
    }
    public FloatConst convfx(int msize,int esize) {
      if(msize<D.msize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      if(esize<D.esize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      return valueOf(msize,esize,d);
    }
    public FloatConst convft(int msize,int esize) {
      if(msize>D.msize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      if(esize>D.esize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      return valueOf(msize,esize,d);
    }
    public IntConst convfi(int size,RoundingMode r) {
      if(size<=64 && d<=MAX_LONG && d>=MIN_LONG) return IntConst.valueOf(size,r.toLong(d));
      return IntConst.valueOf(size,toBigInteger(r));
    }
    public float floatValue() { return (float)d; }
    public double doubleValue() { return d; }
    public BigDecimal toBigDecimal() { return new BigDecimal(d); }
    public BigInteger toBigInteger(RoundingMode r) {
      if(d<=MAX_LONG && d>=MIN_LONG) return BigInteger.valueOf(r.toLong(d));
      long l=Double.doubleToLongBits(d);
      int e=(int)(l>>(msize-1))&((1<<esize)-1);
      if(e>emax(esize)) throw new IllegalArgumentException(this.toString());
      long m=l&((1L<<(msize-1))-1L)|(1L<<(msize-1));
      return BigInteger.valueOf(l<0 ? -m : m).shiftLeft(e-bias(esize)-msize+1);
    }
    public BigInteger toBigIntegerBits() {
      return BigInteger.valueOf(Double.doubleToLongBits(d));
    }
    public BigInteger toExplicitBigIntegerBits() {
      long l=Double.doubleToLongBits(d);
      int e=(int)(l>>(msize-1))&((1<<esize)-1);
      long m=l&((1L<<(msize-1))-1L);
      if(e!=0) m|=1L<<(msize-1);
      return BigInteger.valueOf(m).or(BigInteger.valueOf(l&-(1L<<(msize-1))).shiftLeft(1));
    }
    public boolean equals(Object o) {
      return o==this ||
             o instanceof D && Double.doubleToRawLongBits(d)==Double.doubleToRawLongBits(((D)o).d);
    }
    public int hashCode() {
      long v=Double.doubleToRawLongBits(d);
      return msize*37+(int)((v>>>32)^v);
    }
    public String toString() { return "(FloatConst F53+11 "+d+')'; }
    public String toDecimalString() { return String.valueOf(d); }
  }

// 一般の実装
  private static final class G extends FloatConst {
    private final int msize;
    private final int esize;
    private final BigInteger m; // 仮数部(整数部の1を含む)(ゼロとデノーマル数のみ整数部が0になる)
    private final int e; // 指数部(bias加算後)
    private final boolean s; // 符号(負なら真)
    private G(int msize,int esize,BigInteger man,int exp,boolean sign) {
      this.msize=msize; this.esize=esize; m=man; e=exp; s=sign;
    }
    static G valueOf(int msize,int esize,BigInteger m,int e,boolean s) {
      //assert m.signum()>=0;
      //assert m.bitLength()<=msize;
      //assert e>=0 && e<=emax(esize)+1;
      //assert (e==0)==(m.bitLength()<msize);
      return new G(msize,esize,m,e,s);
    }
  // 指定された仮数部、指数部、符号、round bitとsticky bitの表す数を、
  // 指定された仮数部のビット数と指数部のビット数の範囲に丸めて返す。
  // rsのビット0がsticky bit、ビット1がround bitを表す。
    static G number(int msize,int esize,BigInteger m,int e,boolean s,int rs) {
      //assert m.signum()>=0;
      //assert m.bitLength()==msize || m.bitLength()==msize+1;
      //assert (rs&~3)==0;
      if(m.testBit(msize)) {
        rs=(rs!=0 ? 1 : 0)+(m.testBit(0) ? 2 : 0); m=m.shiftRight(1); e++;
      }
      if(e<=0) {
        if(e<-msize) e=-msize;
        rs=(rs!=0 || m.and(mask(-e)).signum()!=0 ? 1 : 0)+(m.testBit(-e) ? 2 : 0);
        m=m.shiftRight(1-e);
        e=0;
        if(rs>2 || rs==2 && m.testBit(0)) {
          m=m.add(BigInteger.ONE);
          if(m.testBit(msize-1)) e=1;
        }
        return valueOf(msize,esize,m,e,s);
      }
      if(rs>2 || rs==2 && m.testBit(0)) {
        m=m.add(BigInteger.ONE);
        if(m.testBit(msize)) { m=m.shiftRight(1); e++; }
      }
      if(e>emax(esize)) return infinity(msize,esize,s);
      return valueOf(msize,esize,m,e,s);
    }
  // 指定された仮数部のビット数と指数部のビット数と符号を持つゼロを返す。
    static G zero(int msize,int esize,boolean s) {
      return valueOf(msize,esize,BigInteger.ZERO,0,s);
    }
  // 指定された仮数部のビット数と指数部のビット数と符号を持つ無限大を返す。
    static G infinity(int msize,int esize,boolean s) {
      return valueOf(msize,esize,BigInteger.ONE.shiftLeft(msize-1),emax(esize)+1,s);
    }
  // 指定された仮数部のビット数と指数部のビット数を持つNaNを返す。
    static G nan(int msize,int esize) {
      return valueOf(msize,esize,BigInteger.valueOf(3).shiftLeft(msize-2),emax(esize)+1,false);
    }
  // 指定された仮数部のビット数と指数部のビット数とビットパターンと符号を持つNaNを返す。
    static G nan(int msize,int esize,BigInteger m,boolean s) {
      return valueOf(msize,esize,m.setBit(msize-1).setBit(msize-2),emax(esize)+1,s);
    }
    public int msize() { return msize; }
    public int esize() { return esize; }
    public FloatConst add(FloatConst c) {
      if(c.msize()!=msize || c.esize()!=esize) throw new IllegalArgumentException(this+" "+c);
      G g=(G)c;
      return g.s==s ? add1(msize,esize,m,g.m,e,g.e,s) : sub1(msize,esize,m,g.m,e,g.e,s);
    }
    public FloatConst sub(FloatConst c) {
      if(c.msize()!=msize || c.esize()!=esize) throw new IllegalArgumentException(this+" "+c);
      G g=(G)c;
      return g.s==s ? sub1(msize,esize,m,g.m,e,g.e,s) : add1(msize,esize,m,g.m,e,g.e,s);
    }
    private static G add1(int msize,int esize,BigInteger m0,BigInteger m1,
                                   int e0,int e1,boolean s) {
      if(e0<e1 || e0==e1 && m0.compareTo(m1)<0) {
        BigInteger m=m0; m0=m1; m1=m;
        int e=e0; e0=e1; e1=e;
      }
      // 交換の結果、一つでもNaNならe0:m0がNaN、そうでなく一つでも∞ならe0:m0が∞になっている
      if(e0>emax(esize)) return valueOf(msize,esize,m0,e0,s);
      // 交換の結果、一つでも0ならe1:m1が0になっている
      if(e1==0) {
        if(m1.signum()==0) return valueOf(msize,esize,m0,e0,s);
        if(e0==0) {
          //assert m0.signum()!=0;
          int j=msize-m0.bitLength();
          e0=1-j;
          m0=m0.shiftLeft(j);
        }
        int j=msize-m1.bitLength();
        e1=1-j;
        m1=m1.shiftLeft(j);
      }
      //assert m0.signum()!=0 && m1.signum()!=0;
      int i=e0-e1;
      if(i>msize+1) i=msize+1;
      BigInteger m=m0.add(m1.shiftRight(i));
      int rs=(i>1 && m1.and(mask(i-1)).signum()!=0 ? 1 : 0)+(i>=1 && m1.testBit(i-1) ? 2 : 0);
      return number(msize,esize,m,e0,s,rs);
    }
    private static G sub1(int msize,int esize,BigInteger m0,BigInteger m1,
                                   int e0,int e1,boolean s) {
      if(e0<e1 || e0==e1 && m0.compareTo(m1)<0) {
        BigInteger m=m0; m0=m1; m1=m;
        int e=e0; e0=e1; e1=e;
        s=!s;
      }
      // 交換の結果、一つでもNaNならe0:m0がNaN、そうでなく一つでも∞ならe0:m0が∞になっている
      if(e0>emax(esize)) {
        if(e1>emax(esize) && m0.and(mask(msize-1)).signum()==0) return nan(msize,esize); // ∞-∞
        else return valueOf(msize,esize,m0,e0,s);
      }
      // 交換の結果、一つでも0ならe1:m1が0になっている
      if(e1==0) {
        if(m1.signum()==0) {
          if(e0==0 && m0.signum()==0) return zero(msize,esize,false); // 0-0
          return valueOf(msize,esize,m0,e0,s);
        }
        if(e0==0) {
          //assert m1.signum()!=0;
          int j=msize-m0.bitLength();
          e0=1-j;
          m0=m0.shiftLeft(j);
        }
        int j=msize-m1.bitLength();
        e1=1-j;
        m1=m1.shiftLeft(j);
      }
      //assert m0.signum()!=0 && m1.signum()!=0;
      int i=e0-e1-1;
      BigInteger m=m0.shiftLeft(1).subtract(m1.shiftRight(i));
      if(m.signum()==0) return zero(msize,esize,false); // x-x
      int rs;
      int e=e0-1;
      int j=msize-m.bitLength();
      if(j>0) {
        m=m.shiftLeft(j); rs=0; e-=j;
      } else {
        rs=(i>1 && m1.and(mask(i-1)).signum()!=0 ? 1 : 0)+(i>=1 && m1.testBit(i-1) ? 2 : 0);
        if(rs!=0) m=m.subtract(BigInteger.ONE);
      }
      return number(msize,esize,m,e,s,rs);
    }
    public FloatConst mul(FloatConst c) {
      if(c.msize()!=msize || c.esize()!=esize) throw new IllegalArgumentException(this+" "+c);
      G g=(G)c;
      BigInteger m0=m,m1=g.m;
      int e0=e,e1=g.e;
      boolean s0=s;
      boolean s2=s^g.s;
      if(e0<e1 || e0==e1 && m0.compareTo(m1)<0) {
        BigInteger m=m0; m0=m1; m1=m;
        int e=e0; e0=e1; e1=e;
        s0=g.s;
      }
      // 交換の結果、一つでもNaNならe0:m0がNaN、そうでなく一つでも∞ならe0:m0が∞になっている
      if(e0>emax(esize)) {
        if(m0.and(mask(msize-1)).signum()!=0) return valueOf(msize,esize,m0,e0,s0);
        else if(e1==0 && m1.signum()==0) return nan(msize,esize); // ∞*0
        else return infinity(msize,esize,s2);
      }
      // 交換の結果、一つでも0ならe1:m1が0になっている
      if(e1==0) {
        if(m1.signum()==0) return zero(msize,esize,s2);
        if(e0==0) {
          //assert m0.signum()!=0;
          int j=msize-m0.bitLength();
          e0=1-j;
          m0=m0.shiftLeft(j);
        }
        int j=msize-m1.bitLength();
        e1=1-j;
        m1=m1.shiftLeft(j);
      }
      //assert m0.signum()!=0 && m1.signum()!=0;
      BigInteger m2=m0.multiply(m1);
      int rs=(m2.and(mask(msize-2)).signum()!=0 ? 1 : 0)+(m2.testBit(msize-2) ? 2 : 0);
      return number(msize,esize,m2.shiftRight(msize-1),e0-bias(esize)+e1,s2,rs);
    }
    public FloatConst div(FloatConst c) {
      if(c.msize()!=msize || c.esize()!=esize) throw new IllegalArgumentException(this+" "+c);
      G g=(G)c;
      BigInteger m0=m,m1=g.m;
      int e0=e,e1=g.e;
      boolean s2=s^g.s;
      if(e0>emax(esize) && m0.and(mask(msize-1)).signum()!=0) return this;
      if(e1>emax(esize) && m1.and(mask(msize-1)).signum()!=0) return g;
      if(e0>emax(esize)) {
        if(e1>emax(esize)) return nan(msize,esize); // ∞/∞
        else return infinity(msize,esize,s2);
      }
      if(e1>emax(esize)) return zero(msize,esize,s2);
      if(e0==0) {
        if(m0.signum()==0) {
          if(e1==0 && m1.signum()==0) return nan(msize,esize); // 0/0
          return zero(msize,esize,s2);
        }
        int j=msize-m0.bitLength();
        e0=1-j;
        m0=m0.shiftLeft(j);
      }
      if(e1==0) {
        if(m1.signum()==0) return infinity(msize,esize,s2);
        int j=msize-m1.bitLength();
        e1=1-j;
        m1=m1.shiftLeft(j);
      }
      //assert m0.signum()!=0 && m1.signum()!=0;
      BigInteger[] qr=m0.shiftLeft(msize).divideAndRemainder(m1);
      int rs=0;
      if(qr[1].signum()!=0) rs=qr[1].shiftLeft(1).compareTo(m1)+2;
      return number(msize,esize,qr[0],e0-1-e1+bias(esize),s2,rs);
    }
    public FloatConst neg() {
      return valueOf(msize,esize,m,e,!s);
    }
    public IntConst tsteq(FloatConst c,int size) {
      return IntConst.valueOf(size,tst(c)==0 ? 1 : 0);
    }
    public IntConst tstne(FloatConst c,int size) {
      return IntConst.valueOf(size,tst(c)!=0 ? 1 : 0);
    }
    public IntConst tstlts(FloatConst c,int size) {
      return IntConst.valueOf(size,tst(c)==-1 ? 1 : 0);
    }
    public IntConst tstgts(FloatConst c,int size) {
      return IntConst.valueOf(size,tst(c)==1 ? 1 : 0);
    }
    public IntConst tstles(FloatConst c,int size) {
      return IntConst.valueOf(size,tst(c)<=0 ? 1 : 0);
    }
    public IntConst tstges(FloatConst c,int size) {
      return IntConst.valueOf(size,(tst(c)&~1)==0 ? 1 : 0);
    }
  // 小さいなら-1、等しいなら0、大きいなら1、比較不能なら2
    private int tst(FloatConst c) {
      if(c.msize()!=msize || c.esize()!=esize) throw new IllegalArgumentException(this+" "+c);
      G g=(G)c;
      if(e>emax(esize) && m.and(mask(msize-1)).signum()!=0) return 2;
      if(g.e>emax(esize) && g.m.and(mask(msize-1)).signum()!=0) return 2;
      if(e==0 && g.e==0 && m.signum()==0 && g.m.signum()==0) return 0;
      if(s) {
        if(!g.s) return -1;
        if(g.e==e) return g.m.compareTo(m);
        return g.e<e ? -1 : 1;
      } else {
        if(g.s) return 1;
        if(e==g.e) return m.compareTo(g.m);
        return e<g.e ? -1 : 1;
      }
    }
    public FloatConst convfx(int msize,int esize) {
      if(msize<this.msize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      if(esize<this.esize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      BigInteger m2=m.shiftLeft(msize-this.msize);
      int i=bias(esize)-bias(this.esize);
      int e2=e+i;
      if(e>emax(this.esize)) e2=emax(esize)+1;
      else if(e==0) {
        if(m.signum()==0) { m2=m; e2=0; }
        else {
          int j=msize-m2.bitLength();
          if(j<i+1) { m2=m2.shiftLeft(j); e2=i+1-j; }
          else { m2=m2.shiftLeft(i); e2=0; }
        }
      }
      if(msize==24 && esize==8) return F.valueOf(m2,e2,s);
      if(msize==53 && esize==11) return D.valueOf(m2,e2,s);
      return valueOf(msize,esize,m2,e2,s);
    }
    public FloatConst convft(int msize,int esize) {
      if(msize>this.msize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      if(esize>this.esize) throw new IllegalArgumentException(this+" "+msize+'+'+esize);
      BigInteger m0=m;
      int e0=e;
      int i=this.msize-msize;
      G c2;
      conv: {
        if(e0>emax(this.esize)) {
          if(m0.and(mask(this.msize-1)).signum()!=0) c2=nan(msize,esize,m0.shiftRight(i),s);
          else c2=infinity(msize,esize,s);
          break conv;
        }
        if(e0==0) {
          if(m0.signum()==0) {
            c2=zero(msize,esize,s);
            break conv;
          }
          int j=this.msize-m0.bitLength();
          e0=1-j;
          m0=m0.shiftLeft(j);
        }
        BigInteger m2=m0.shiftRight(i);
        int rs=(i>1 && m0.and(mask(i-1)).signum()!=0 ? 1 : 0)+(i>=1 && m0.testBit(i-1) ? 2 : 0);
        int e2=e0-bias(this.esize)+bias(esize);
        c2=number(msize,esize,m2,e2,s,rs);
      }
      if(msize==24 && esize==8) return F.valueOf(c2.m,c2.e,c2.s);
      if(msize==53 && esize==11) return D.valueOf(c2.m,c2.e,c2.s);
      return c2;
    }
    public IntConst convfi(int size,RoundingMode r) {
      return IntConst.valueOf(size,toBigInteger(r));
    }
    public float floatValue() {
      FloatConst c;
      if(msize>24) {
        if(esize>=8) c=this.convft(24,8);
        else c=this.convfx(msize,8).convft(24,8);
      } else if(msize<24) {
        if(esize<=8) c=this.convfx(24,8);
        else c=this.convfx(24,esize).convft(24,8);
      } else { // msize==24
        if(esize>8) c=this.convft(24,8);
        else c=this.convfx(24,8);
      }
      return c.floatValue();
    }
    public double doubleValue() {
      FloatConst c;
      if(msize>53) {
        if(esize>=11) c=this.convft(53,11);
        else c=this.convfx(msize,11).convft(53,11);
      } else if(msize<53) {
        if(esize<=11) c=this.convfx(53,11);
        else c=this.convfx(53,esize).convft(53,11);
      } else { // msize==53
        if(esize>11) c=this.convft(53,11);
        else c=this.convfx(53,11);
      }
      return c.doubleValue();
    }
    public BigDecimal toBigDecimal() {
      if(e>emax(esize)) throw new IllegalArgumentException(this.toString());
      BigDecimal bd;
      if(e==0 && m.signum()==0) bd=new BigDecimal(0);
      else {
        int i=(e==0 ? 1 : e)-bias(esize)-msize+1;
        if(i>=0) bd=new BigDecimal(m.shiftLeft(i));
        else {
          i=-i;
          int j=m.getLowestSetBit();
          if(j>i) j=i;
          BigDecimal bdm=new BigDecimal(m.shiftRight(j));
          BigDecimal bde=new BigDecimal(BigInteger.ONE.shiftLeft(i-j));
          bd=bdm.divide(bde,i-j,BigDecimal.ROUND_UNNECESSARY);
        }
      }
      return s ? bd.negate() : bd;
    }
    public BigInteger toBigInteger(RoundingMode r) {
      if(e>emax(esize)) throw new IllegalArgumentException(this.toString());
      int i=bias(esize)+msize-1-(e==0 ? 1 : e);
      if(i>msize+1) i=msize+1;
      BigInteger b=m.shiftRight(i);
      int rs=0;
      if(i>0) {
        rs=(m.and(mask(i-1)).signum()!=0 ? 1 : 0)+(m.testBit(i-1) ? 2 : 0);
      }
      b=r.round(b,s,rs);
      return s ? b.negate() : b;
    }
    public BigInteger toBigIntegerBits() {
      int i=((s ? -1 : 0)<<esize)+e;
      return BigInteger.valueOf(i).shiftLeft(msize-1).add(m.and(mask(msize-1)));
    }
    public BigInteger toExplicitBigIntegerBits() {
      int i=((s ? -1 : 0)<<esize)+e;
      return BigInteger.valueOf(i).shiftLeft(msize).add(m);
    }
    public boolean equals(Object o) {
      if(o==this) return true;
      if(!(o instanceof G)) return false;
      G g=(G)o;
      return s==g.s && e==g.e && m.equals(g.m);
    }
    public int hashCode() { return (msize*37+e)*37+m.hashCode(); }
  // 現在の実装では表示桁数が多すぎる
    public String toString() {
      return "(FloatConst F"+msize+'+'+esize+' '+toDecimalString()+')';
    }
  // 現在の実装では表示桁数が多すぎる
    public String toDecimalString() {
      if(e<=emax(esize)) return toBigDecimal().toString();
      if(m.and(mask(msize-1)).signum()==0) return s ? "-Infinity" : "Infinity";
      return "NaN";
    }
  }

// constructor

  private FloatConst() {}

// factory method

/** 指定された仮数部のビット数と指数部のビット数と値(double)を持つ浮動小数点定数を返す.
 * 値が指定されたビット数で正確に表せない場合には、直近への丸めを行う。
 * @exception IllegalArgumentException 仮数部のビット数が3〜1073741823の範囲外か、指数部のビット数が2〜30の範囲外の場合 */
  public static FloatConst valueOf(int msize,int esize,double val) {
    if(msize==24 && esize==8) return F.valueOf((float)val);
    if(msize==53 && esize==11) return D.valueOf(val);
    if(msize<M_MIN || msize>M_MAX) throw new IllegalArgumentException(msize+"+"+esize);
    if(esize<E_MIN || esize>E_MAX) throw new IllegalArgumentException(msize+"+"+esize);
    long l=Double.doubleToLongBits(val);
    int e1=(int)(l>>(53-1))&((1<<11)-1);
    long m1=l&((1L<<(53-1))-1L);
    boolean s=l<0;
    if(e1>emax(11)) {
      if(m1==0) return G.infinity(msize,esize,s);
      else return G.nan(msize,esize,BigInteger.valueOf(m1).shiftLeft(msize-53),s);
    }
    int e=e1-bias(11)+bias(esize);
    if(e1==0) {
      if(m1==0) return G.zero(msize,esize,s);
      e++; do { e--; m1<<=1; } while(m1<(1L<<(53-1)));
    } else {
      m1|=1L<<(53-1);
    }
    BigInteger m;
    int rs;
    if(msize>=53) {
      m=BigInteger.valueOf(m1).shiftLeft(msize-53); rs=0;
    } else {
      int i=53-msize;
      m=BigInteger.valueOf(m1>>i);
      long t=1L<<(i-1);
      rs=((m1&(t-1))!=0 ? 1 : 0)+((m1&t)!=0 ? 2 : 0);
    }
    return G.number(msize,esize,m,e,s,rs);
  }
/** 指定された仮数部のビット数と指数部のビット数と値(BigDecimal)を持つ浮動小数点定数を返す.
 * 値を概念上は無限に正確なバイナリ値に変換してから、指定されたビット数で表現できるように、直近への丸めを行う。
 * @exception IllegalArgumentException 仮数部のビット数が3〜1073741823の範囲外か、指数部のビット数が2〜30の範囲外の場合 */
// 現在の実装は正確だが遅い
  public static FloatConst valueOf(int msize,int esize,BigDecimal val) {
    if(msize==24 && esize==8) return new F(val.floatValue());
    if(msize==53 && esize==11) return new D(val.doubleValue());
    if(msize<M_MIN || msize>M_MAX) throw new IllegalArgumentException(msize+"+"+esize);
    if(esize<E_MIN || esize>E_MAX) throw new IllegalArgumentException(msize+"+"+esize);
    if(val.signum()==0) return G.zero(msize,esize,false);
    if(val.scale()==0) return valueOf(msize,esize,val.toBigInteger());
    boolean s=false;
    if(val.signum()<0) { val=val.negate(); s=!s; }
    BigInteger b=BigInteger.ONE;
    if(val.compareTo(fromBigIntegerBits(msize+1,esize,b).toBigDecimal())<=0) return G.zero(msize,esize,s);
    int k=0;
    for(int i=(msize+1)-1+esize;i>1;) {
      BigInteger b1=b.setBit(--i);
      BigDecimal bd=fromBigIntegerBits(msize+1,esize,b1).toBigDecimal();
      k=val.compareTo(bd);
      if(k>=0) b=b1;
      if(k==0) break;
    }
    b=b.add(BigInteger.ONE).shiftRight(1);
    if(k==0 && b.testBit(0)) b=b.subtract(BigInteger.ONE);
    int e=b.shiftRight(msize-1).intValue()&((1<<esize)-1);
    if(e>emax(esize)) return G.infinity(msize,esize,s);
    FloatConst c=fromBigIntegerBits(msize,esize,b);
    return s ? c.neg() : c;
  }
/** 指定された仮数部のビット数と指数部のビット数と値(long)を持つ浮動小数点定数を返す.
 * 値が指定されたビット数で正確に表せない場合には、直近への丸めを行う。
 * @exception IllegalArgumentException 仮数部のビット数が3〜1073741823の範囲外か、指数部のビット数が2〜30の範囲外の場合 */
  public static FloatConst valueOf(int msize,int esize,long val) {
    if(msize==24 && esize==8) return F.valueOf(val);
    if(msize==53 && esize==11) return D.valueOf(val);
    return valueOf(msize,esize,BigInteger.valueOf(val));
  }
/** 指定された仮数部のビット数と指数部のビット数と値(bigInteger)を持つ浮動小数点定数を返す.
 * 値が指定されたビット数で正確に表せない場合には、直近への丸めを行う。
 * @exception IllegalArgumentException 仮数部のビット数が3〜1073741823の範囲外か、指数部のビット数が2〜30の範囲外の場合 */
  public static FloatConst valueOf(int msize,int esize,BigInteger val) {
    if(msize==24 && esize==8) return F.valueOf(val.floatValue());
    if(msize==53 && esize==11) return D.valueOf(val.doubleValue());
    if(msize<M_MIN || msize>M_MAX) throw new IllegalArgumentException(msize+"+"+esize);
    if(esize<E_MIN || esize>E_MAX) throw new IllegalArgumentException(msize+"+"+esize);
    if(val.signum()==0) return G.zero(msize,esize,false);
    boolean s=false;
    if(val.signum()<0) { val=val.negate(); s=true; }
    int j=val.bitLength();
    int e=j-1+bias(esize);
    j-=msize;
    BigInteger m=val.shiftRight(j);
    int rs=0;
    if(j>0) rs=(val.and(mask(j-1)).signum()!=0 ? 1 : 0)+(val.testBit(j-1) ? 2 : 0);
    return G.number(msize,esize,m,e,s,rs);
  }
/** 指定された仮数部のビット数と指数部のビット数を持ち、指定されたビット表現を、上位から符号、指数部、仮数部(隠れたビットを除く)のビット配置として解釈した、浮動小数点定数を返す.
 * 解釈した結果がNaNの場合は、仮数部の隠れたビットを除く最上位ビットを立てる。
 * @exception IllegalArgumentException 仮数部のビット数が3〜1073741823の範囲外か、指数部のビット数が2〜30の範囲外の場合 */
  public static FloatConst fromBigIntegerBits(int msize,int esize,BigInteger bits) {
    if(msize==24 && esize==8) return F.valueOf(Float.intBitsToFloat(bits.intValue()));
    if(msize==53 && esize==11) return D.valueOf(Double.longBitsToDouble(bits.longValue()));
    BigInteger m=bits.and(mask(msize-1));
    int e=bits.shiftRight(msize-1).intValue()&((1<<esize)-1);
    if(e>emax(esize) && m.signum()!=0) m=m.or(BigInteger.ONE.shiftLeft(msize-2));
    if(e!=0) m=m.or(BigInteger.ONE.shiftLeft(msize-1));
    boolean s=bits.testBit(msize-1+esize);
    return G.valueOf(msize,esize,m,e,s);
  }
/** 指定された仮数部のビット数と指数部のビット数を持ち、指定されたビット表現を、上位から符号、指数部、仮数部(隠れたビットを含む)のビット配置として解釈した、浮動小数点定数を返す.
 * 仮数部の隠れたビットが0なのに指数部が0以外の場合は、正規化を行う。
 * 仮数部の隠れたビットが1なのに指数部が0の場合は、指数部を1にする。
 * 解釈した結果がNaNの場合は、仮数部の隠れたビットを除く最上位ビットを立てる。
 * @exception IllegalArgumentException 仮数部のビット数が3〜1073741823の範囲外か、指数部のビット数が2〜30の範囲外の場合 */
  public static FloatConst fromExplicitBigIntegerBits(int msize,int esize,BigInteger bits) {
    BigInteger m=bits.and(mask(msize));
    int e=bits.shiftRight(msize).intValue()&((1<<esize)-1);
    if(e>emax(esize)) {
      if(m.signum()!=0) m=m.or(BigInteger.ONE.shiftLeft(msize-2));
      m=m.or(BigInteger.ONE.shiftLeft(msize-1));
    } else if(e==0) {
      if(m.testBit(msize-1)) e=1;
    } else {
      if(!m.testBit(msize-1)) {
        int j=msize-m.bitLength();
        if(j==0) e=0;
        else {
          if(j>e-1) { j=e-1; e--; }
          m=m.shiftLeft(j); e-=j;
        }
      }
    }
    boolean s=bits.testBit(msize+esize);
    if(msize==24 && esize==8) return F.valueOf(m,e,s);
    if(msize==53 && esize==11) return D.valueOf(m,e,s);
    return G.valueOf(msize,esize,m,e,s);
  }

// method

/** 仮数部のビット数を返す.
 * ビット数をインスタンスが記憶しないような、コンパクトな実装を許すため、 メソッドになっている。 */
  public abstract int msize();
/** 指数部のビット数を返す.
 * ビット数をインスタンスが記憶しないような、コンパクトな実装を許すため、 メソッドになっている。 */
  public abstract int esize();
/** 値がthis+cの浮動小数点定数を返す.
 * @exception IllegalArgumentException この浮動小数点定数と指定された浮動小数点定数のビット数が異なる場合 */
  public abstract FloatConst add(FloatConst c);
/** 値がthis-cの浮動小数点定数を返す.
 * @exception IllegalArgumentException この浮動小数点定数と指定された浮動小数点定数のビット数が異なる場合 */
  public abstract FloatConst sub(FloatConst c);
/** 値がthis*cの浮動小数点定数を返す.
 * @exception IllegalArgumentException この浮動小数点定数と指定された浮動小数点定数のビット数が異なる場合 */
  public abstract FloatConst mul(FloatConst c);
/** 値がthis/cの浮動小数点定数を返す.
 * @exception IllegalArgumentException この浮動小数点定数と指定された浮動小数点定数のビット数が異なる場合 */
  public abstract FloatConst div(FloatConst c);
//  public abstract FloatConst mod(FloatConst c);
/** 値が-thisの浮動小数点定数を返す. */
  public abstract FloatConst neg();
//  public abstract FloatConst sqrt(FloatConst c);
/** this==cのとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この浮動小数点定数と指定された浮動小数点定数のビット数が異なる場合 */
  public abstract IntConst tsteq(FloatConst c,int size);
/** this!=cのとき1、そうでなければ0の、指定されたビット数の整数定数を返す. 
 * @exception IllegalArgumentException この浮動小数点定数と指定された浮動小数点定数のビット数が異なる場合 */
  public abstract IntConst tstne(FloatConst c,int size);
/** this&lt;cのとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この浮動小数点定数と指定された浮動小数点定数のビット数が異なる場合 */
  public abstract IntConst tstlts(FloatConst c,int size);
/** this&gt;cのとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この浮動小数点定数と指定された浮動小数点定数のビット数が異なる場合 */
  public abstract IntConst tstgts(FloatConst c,int size);
/** this&lt;=cのとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この浮動小数点定数と指定された浮動小数点定数のビット数が異なる場合 */
  public abstract IntConst tstles(FloatConst c,int size);
/** this&gt;=cのとき1、そうでなければ0の、指定されたビット数の整数定数を返す.
 * @exception IllegalArgumentException この浮動小数点定数と指定された浮動小数点定数のビット数が異なる場合 */
  public abstract IntConst tstges(FloatConst c,int size);
/** この浮動小数点定数を、指定された仮数部のビット数と指数部のビット数に拡張して返す.
 * @exception IllegalArgumentException 仮数部のビット数が現在の値から1073741823までの範囲外か、指数部のビット数が現在の値から30までの範囲外の場合 */
  public abstract FloatConst convfx(int msize,int esize);
/** この浮動小数点定数を、指定された仮数部のビット数と指数部のビット数に縮小して返す.
 * 値が指定されたビット数で正確に表せない場合には、直近への丸めを行う。
 * @exception IllegalArgumentException 仮数部のビット数が3から現在の値までの範囲外か、指数部のビット数が2から現在の値までの範囲外の場合 */
  public abstract FloatConst convft(int msize,int esize);
/** この浮動小数点数を、指定された丸めモードで指定されたビット数の整数定数に変換して返す.
 * 値が指定されたビット数で表せる範囲を超えている場合には、上位のビットを捨てる。
 * @exception IllegalArgumentException この浮動小数点定数が無限大またはNaNの場合 */
  public abstract IntConst convfi(int size,RoundingMode r);
/** この浮動小数点定数のfloatの値を返す. */
  public abstract float floatValue();
/** この浮動小数点定数のdoubleの値を返す. */
  public abstract double doubleValue();
/** この浮動小数点定数をBigDecimalに変換して返す.
 * BigDecimalのスケールは、(10<SUP>scale</SUP>*this)が整数である最小の値となる。 */
  public abstract BigDecimal toBigDecimal();
/** この浮動小数点定数を、指定された丸めモードでBigIntegerに変換して返す.
 * @exception IllegalArgumentException この浮動小数点定数が無限大またはNaNの場合 */
  public abstract BigInteger toBigInteger(RoundingMode r);
/** この浮動小数点定数を、上位から符号、指数部、仮数部(隠れたビットを除く)のビット配置でBigIntegerに変換して返す. */
  public abstract BigInteger toBigIntegerBits();
/** この浮動小数点定数を、上位から符号、指数部、仮数部(隠れたビットを含む)のビット配置でBigIntegerに変換して返す.
 仮数部のmsbは、ゼロおよびデノーマル数のときのみ0となる。 */
  public abstract BigInteger toExplicitBigIntegerBits();
/** この浮動小数点定数と指定されたオブジェクトが等しいかどうかを返す. */
  public abstract boolean equals(Object o);
/** この浮動小数点定数のハッシュコードを返す. */
  public abstract int hashCode();
/** この浮動小数点定数の文字列表現を返す.
 * 文字列は、例えば"(FloatConst F24+8 1.25)"のようになる。 */
  public abstract String toString();
/** この浮動小数点定数の10進文字列表現を返す.
 * 文字列は、例えば"1.25"のようになる。 */
  public abstract String toDecimalString();
// bias
  private static int bias(int esize) { return (1<<(esize-1))-1; }
// bias加算後の指数部の最大
// 指数部の最小は1なのでメソッドは用意しない
  private static int emax(int esize) { return (1<<esize)-2; }
// 指定されたビット数のBigIntegerのマスク
  private static BigInteger mask(int i) {
    return BigInteger.ONE.shiftLeft(i).subtract(BigInteger.ONE);
  }

}
