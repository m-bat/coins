/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.util;

import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * ..............(..............).
 *
 * <P>......................................................................................................................................
 * ..........................................................2......................
 *
 * <P>..............IEEE 754..............(....................)............
 * ....................................................................................................................
 * ..............................................
 *
 * <P>....(........................)..................................................................................................
 *
 * <P>......................................................(*....................)
 * <TABLE BORDER>
 * <TR><TD><TH>......<TH>......
 * <TR><TD>16..................<TD>11*<TD>5
 * <TR><TD>......<TD>24*<TD>8
 * <TR><TD>......<TD>53*<TD>11
 * <TR><TD>x86..........<TD>64<TD>15
 * <TR><TD>4......<TD>113*<TD>15
 * </TABLE>
 */
public abstract class FloatConst {

// static field

/** ........0.0 */
  public static final FloatConst F32_0=F.F32_0;
/** ........1.0 */
  public static final FloatConst F32_1=F.F32_1;
/** ........0.0 */
  public static final FloatConst F64_0=D.F64_0;
/** ........1.0 */
  public static final FloatConst F64_1=D.F64_1;
// ............................
  private static final int M_MIN=3,M_MAX=0x3FFFFFFF;
// ............................
  private static final int E_MIN=2,E_MAX=30;

// member type

/** ......................................... */
  public abstract static class RoundingMode {
    RoundingMode() {}
    abstract long toLong(double d);
    abstract BigInteger round(BigInteger b,boolean s,int rs);
  }
/** ...................... */
  public static final RoundingMode ROUND_NEAREST=new RoundingMode() {
    long toLong(double d) { return (long)StrictMath.rint(d); }
    BigInteger round(BigInteger b,boolean s,int rs) {
      if(rs>2 || rs==2 && b.testBit(0)) b=b.add(BigInteger.ONE);
      return b;
    }
  };
/** .......................... */
  public static final RoundingMode ROUND_MINUS=new RoundingMode() {
    long toLong(double d) { return (long)StrictMath.floor(d); }
    BigInteger round(BigInteger b,boolean s,int rs) {
      if(s && rs!=0) b=b.add(BigInteger.ONE);
      return b;
    }
  };
/** .......................... */
  public static final RoundingMode ROUND_PLUS=new RoundingMode() {
    long toLong(double d) { return (long)StrictMath.ceil(d); }
    BigInteger round(BigInteger b,boolean s,int rs) {
      if(!s && rs!=0) b=b.add(BigInteger.ONE);
      return b;
    }
  };
/** .......................... */
  public static final RoundingMode ROUND_ZERO=new RoundingMode() {
    long toLong(double d) { return (long)d; }
    BigInteger round(BigInteger b,boolean s,int rs) { return b; }
  };

// float............
  private static final strictfp class F extends FloatConst {
    private final float f;
    static final int msize=24;
    static final int esize=8;
    static final F F32_0=new F(0.0f);
    static final F F32_1=new F(1.0f);
    private static final long MIN_LONG=1<<63; // long....................
    private static final long MAX_LONG=((1L<<(msize-1))-1)<<(64-msize); // long....................
    private F(float val) { f=val; }
    static F valueOf(float val) {
      if(Float.floatToIntBits(val)==0) return F32_0; // ....0....
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

// double............
  private static final strictfp class D extends FloatConst {
    private final double d;
    static final int msize=53;
    static final int esize=11;
    static final D F64_0=new D(0.0);
    static final D F64_1=new D(1.0);
    private static final long MIN_LONG=1<<63; // long....................
    private static final long MAX_LONG=((1L<<(msize-1))-1)<<(64-msize); // long....................
    private D(double val) { d=val; }
    static D valueOf(double val) {
      if(Double.doubleToLongBits(val)==0L) return F64_0; // ....0....
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

// ..........
  private static final class G extends FloatConst {
    private final int msize;
    private final int esize;
    private final BigInteger m; // ......(........1......)(..............................0......)
    private final int e; // ......(bias......)
    private final boolean s; // ....(........)
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
  // ................................round bit..sticky bit............
  // ................................................................
  // rs........0..sticky bit........1..round bit........
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
  // ....................................................................
    static G zero(int msize,int esize,boolean s) {
      return valueOf(msize,esize,BigInteger.ZERO,0,s);
    }
  // ......................................................................
    static G infinity(int msize,int esize,boolean s) {
      return valueOf(msize,esize,BigInteger.ONE.shiftLeft(msize-1),emax(esize)+1,s);
    }
  // ..................................................NaN........
    static G nan(int msize,int esize) {
      return valueOf(msize,esize,BigInteger.valueOf(3).shiftLeft(msize-2),emax(esize)+1,false);
    }
  // ........................................................................NaN........
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
      // ....................NaN....e0:m0..NaN..........................e0:m0................
      if(e0>emax(esize)) return valueOf(msize,esize,m0,e0,s);
      // ....................0....e1:m1..0............
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
      // ....................NaN....e0:m0..NaN..........................e0:m0................
      if(e0>emax(esize)) {
        if(e1>emax(esize) && m0.and(mask(msize-1)).signum()==0) return nan(msize,esize); // ..-..
        else return valueOf(msize,esize,m0,e0,s);
      }
      // ....................0....e1:m1..0............
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
      // ....................NaN....e0:m0..NaN..........................e0:m0................
      if(e0>emax(esize)) {
        if(m0.and(mask(msize-1)).signum()!=0) return valueOf(msize,esize,m0,e0,s0);
        else if(e1==0 && m1.signum()==0) return nan(msize,esize); // ..*0
        else return infinity(msize,esize,s2);
      }
      // ....................0....e1:m1..0............
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
        if(e1>emax(esize)) return nan(msize,esize); // ../..
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
  // ..........-1............0............1..............2
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
  // ................................
    public String toString() {
      return "(FloatConst F"+msize+'+'+esize+' '+toDecimalString()+')';
    }
  // ................................
    public String toDecimalString() {
      if(e<=emax(esize)) return toBigDecimal().toString();
      if(m.and(mask(msize-1)).signum()==0) return s ? "-Infinity" : "Infinity";
      return "NaN";
    }
  }

// constructor

  private FloatConst() {}

// factory method

/** ................................................(double)...........................
 * ....................................................................
 * @exception IllegalArgumentException ..................3..1073741823..............................2..30.............. */
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
/** ................................................(BigDecimal)...........................
 * ............................................................................................................
 * @exception IllegalArgumentException ..................3..1073741823..............................2..30.............. */
// ........................
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
/** ................................................(long)...........................
 * ....................................................................
 * @exception IllegalArgumentException ..................3..1073741823..............................2..30.............. */
  public static FloatConst valueOf(int msize,int esize,long val) {
    if(msize==24 && esize==8) return F.valueOf(val);
    if(msize==53 && esize==11) return D.valueOf(val);
    return valueOf(msize,esize,BigInteger.valueOf(val));
  }
/** ................................................(bigInteger)...........................
 * ....................................................................
 * @exception IllegalArgumentException ..................3..1073741823..............................2..30.............. */
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
/** ........................................................................................................(..................).................................................
 * ..............NaN..........................................................
 * @exception IllegalArgumentException ..................3..1073741823..............................2..30.............. */
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
/** ........................................................................................................(..................).................................................
 * ......................0..............0............................
 * ......................1..............0..................1........
 * ..............NaN..........................................................
 * @exception IllegalArgumentException ..................3..1073741823..............................2..30.............. */
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

/** .......................
 * ...................................................................... ...................... */
  public abstract int msize();
/** .......................
 * ...................................................................... ...................... */
  public abstract int esize();
/** ....this+c.......................
 * @exception IllegalArgumentException .................................................................. */
  public abstract FloatConst add(FloatConst c);
/** ....this-c.......................
 * @exception IllegalArgumentException .................................................................. */
  public abstract FloatConst sub(FloatConst c);
/** ....this*c.......................
 * @exception IllegalArgumentException .................................................................. */
  public abstract FloatConst mul(FloatConst c);
/** ....this/c.......................
 * @exception IllegalArgumentException .................................................................. */
  public abstract FloatConst div(FloatConst c);
//  public abstract FloatConst mod(FloatConst c);
/** ....-this....................... */
  public abstract FloatConst neg();
//  public abstract FloatConst sqrt(FloatConst c);
/** this==c......1................0.......................................
 * @exception IllegalArgumentException .................................................................. */
  public abstract IntConst tsteq(FloatConst c,int size);
/** this!=c......1................0....................................... 
 * @exception IllegalArgumentException .................................................................. */
  public abstract IntConst tstne(FloatConst c,int size);
/** this&lt;c......1................0.......................................
 * @exception IllegalArgumentException .................................................................. */
  public abstract IntConst tstlts(FloatConst c,int size);
/** this&gt;c......1................0.......................................
 * @exception IllegalArgumentException .................................................................. */
  public abstract IntConst tstgts(FloatConst c,int size);
/** this&lt;=c......1................0.......................................
 * @exception IllegalArgumentException .................................................................. */
  public abstract IntConst tstles(FloatConst c,int size);
/** this&gt;=c......1................0.......................................
 * @exception IllegalArgumentException .................................................................. */
  public abstract IntConst tstges(FloatConst c,int size);
/** .................................................................................
 * @exception IllegalArgumentException ..............................1073741823..............................................30.................. */
  public abstract FloatConst convfx(int msize,int esize);
/** .................................................................................
 * ....................................................................
 * @exception IllegalArgumentException ..................3..............................................2.............................. */
  public abstract FloatConst convft(int msize,int esize);
/** .....................................................................................
 * ..............................................................................
 * @exception IllegalArgumentException ................................NaN...... */
  public abstract IntConst convfi(int size,RoundingMode r);
/** ....................float........... */
  public abstract float floatValue();
/** ....................double........... */
  public abstract double doubleValue();
/** ....................BigDecimal...............
 * BigDecimal..............(10<SUP>scale</SUP>*this)............................ */
  public abstract BigDecimal toBigDecimal();
/** ............................................BigInteger...............
 * @exception IllegalArgumentException ................................NaN...... */
  public abstract BigInteger toBigInteger(RoundingMode r);
/** ..................................................(..................)..............BigInteger............... */
  public abstract BigInteger toBigIntegerBits();
/** ..................................................(..................)..............BigInteger...............
 ........msb....................................0........ */
  public abstract BigInteger toExplicitBigIntegerBits();
/** ................................................................. */
  public abstract boolean equals(Object o);
/** ......................................... */
  public abstract int hashCode();
/** .....................................
 * ................"(FloatConst F24+8 1.25)".............. */
  public abstract String toString();
/** ....................10...................
 * ................"1.25".............. */
  public abstract String toDecimalString();
// bias
  private static int bias(int esize) { return (1<<(esize-1))-1; }
// bias....................
// ..............1..........................
  private static int emax(int esize) { return (1<<esize)-2; }
// ....................BigInteger........
  private static BigInteger mask(int i) {
    return BigInteger.ONE.shiftLeft(i).subtract(BigInteger.ONE);
  }

}
