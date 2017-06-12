/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.util;

import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * ��ư���������(���Ѥ��ͥ��饹).
 *
 * <P>���Υ��饹���Ȥ���ݥ��饹�Ǥ��뤬����ݥ��饹������ҥ��饹�Ȥ��ƻ��äƤ��ꡢ�ե����ȥ�᥽�åɤǥ��󥹥��󥹤��������뤳�Ȥ��Ǥ��롣
 * �ƥ��󥹥��󥹤ϡ����ꤵ�줿�ӥåȿ��β������Ȼؿ��������2����ư����������ɽ����
 *
 * <P>���Υ��饹�ϡ�IEEE 754�˽�򤷤��黻(�㳰���ȥ�åפ����)���󶡤��롣
 * ����������§�黻����ư�������������Ѵ�������������Ѵ��Ǥϡ��ǥե���Ȥδݤ�⡼�ɤǤ���ľ��ؤδݤ�Τ߻��ѤǤ��롣
 * �����ؤ��Ѵ��Ǥϡ��ݤ�⡼�ɤ�����ǽ�Ǥ��롣
 *
 * <P>����(�������Ȼؿ����Υӥåȿ�)�ΰۤʤ륤�󥹥���Ʊ�Τλ�§�黻���뤤����Ӥ�Ԥ��������ϡ�����Ū�˷������Ѵ�����ɬ�פ����롣
 *
 * <P>�ʲ��ˡ��褯���Ѥ���벾�����Ȼؿ����Υӥåȿ��򼨤���(*�ϱ��줿�ӥåȤ�ޤ�)
 * <TABLE BORDER>
 * <TR><TD><TH>������<TH>�ؿ���
 * <TR><TD>16�ӥå���ư��������<TD>11*<TD>5
 * <TR><TD>ñ����<TD>24*<TD>8
 * <TR><TD>������<TD>53*<TD>11
 * <TR><TD>x86��ĥ������<TD>64<TD>15
 * <TR><TD>4������<TD>113*<TD>15
 * </TABLE>
 */
public abstract class FloatConst {

// static field

/** ñ���٤�0.0 */
  public static final FloatConst F32_0=F.F32_0;
/** ñ���٤�1.0 */
  public static final FloatConst F32_1=F.F32_1;
/** �����٤�0.0 */
  public static final FloatConst F64_0=D.F64_0;
/** �����٤�1.0 */
  public static final FloatConst F64_1=D.F64_1;
// �������Υӥåȿ��κǾ��Ⱥ���
  private static final int M_MIN=3,M_MAX=0x3FFFFFFF;
// �ؿ����Υӥåȿ��κǾ��Ⱥ���
  private static final int E_MIN=2,E_MAX=30;

// member type

/** ���������Ѵ�����Ȥ��δݤ�⡼�ɤ�ɽ����. */
  public abstract static class RoundingMode {
    RoundingMode() {}
    abstract long toLong(double d);
    abstract BigInteger round(BigInteger b,boolean s,int rs);
  }
/** ľ��ؤδݤ��ɽ����� */
  public static final RoundingMode ROUND_NEAREST=new RoundingMode() {
    long toLong(double d) { return (long)StrictMath.rint(d); }
    BigInteger round(BigInteger b,boolean s,int rs) {
      if(rs>2 || rs==2 && b.testBit(0)) b=b.add(BigInteger.ONE);
      return b;
    }
  };
/** ��������ؤδݤ��ɽ����� */
  public static final RoundingMode ROUND_MINUS=new RoundingMode() {
    long toLong(double d) { return (long)StrictMath.floor(d); }
    BigInteger round(BigInteger b,boolean s,int rs) {
      if(s && rs!=0) b=b.add(BigInteger.ONE);
      return b;
    }
  };
/** ���������ؤδݤ��ɽ����� */
  public static final RoundingMode ROUND_PLUS=new RoundingMode() {
    long toLong(double d) { return (long)StrictMath.ceil(d); }
    BigInteger round(BigInteger b,boolean s,int rs) {
      if(!s && rs!=0) b=b.add(BigInteger.ONE);
      return b;
    }
  };
/** ���������ؤδݤ��ɽ����� */
  public static final RoundingMode ROUND_ZERO=new RoundingMode() {
    long toLong(double d) { return (long)d; }
    BigInteger round(BigInteger b,boolean s,int rs) { return b; }
  };

// float�ξ��μ���
  private static final strictfp class F extends FloatConst {
    private final float f;
    static final int msize=24;
    static final int esize=8;
    static final F F32_0=new F(0.0f);
    static final F F32_1=new F(1.0f);
    private static final long MIN_LONG=1<<63; // long�����Τ�ɽ����Ǿ���
    private static final long MAX_LONG=((1L<<(msize-1))-1)<<(64-msize); // long�����Τ�ɽ���������
    private F(float val) { f=val; }
    static F valueOf(float val) {
      if(Float.floatToIntBits(val)==0) return F32_0; // ����0�Τ�
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

// double�ξ��μ���
  private static final strictfp class D extends FloatConst {
    private final double d;
    static final int msize=53;
    static final int esize=11;
    static final D F64_0=new D(0.0);
    static final D F64_1=new D(1.0);
    private static final long MIN_LONG=1<<63; // long�����Τ�ɽ����Ǿ���
    private static final long MAX_LONG=((1L<<(msize-1))-1)<<(64-msize); // long�����Τ�ɽ���������
    private D(double val) { d=val; }
    static D valueOf(double val) {
      if(Double.doubleToLongBits(val)==0L) return F64_0; // ����0�Τ�
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

// ���̤μ���
  private static final class G extends FloatConst {
    private final int msize;
    private final int esize;
    private final BigInteger m; // ������(��������1��ޤ�)(����ȥǥΡ��ޥ���Τ���������0�ˤʤ�)
    private final int e; // �ؿ���(bias�û���)
    private final boolean s; // ���(��ʤ鿿)
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
  // ���ꤵ�줿���������ؿ�������桢round bit��sticky bit��ɽ������
  // ���ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ����ϰϤ˴ݤ���֤���
  // rs�Υӥå�0��sticky bit���ӥå�1��round bit��ɽ����
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
  // ���ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ���������ĥ�����֤���
    static G zero(int msize,int esize,boolean s) {
      return valueOf(msize,esize,BigInteger.ZERO,0,s);
    }
  // ���ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ����������̵������֤���
    static G infinity(int msize,int esize,boolean s) {
      return valueOf(msize,esize,BigInteger.ONE.shiftLeft(msize-1),emax(esize)+1,s);
    }
  // ���ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ������NaN���֤���
    static G nan(int msize,int esize) {
      return valueOf(msize,esize,BigInteger.valueOf(3).shiftLeft(msize-2),emax(esize)+1,false);
    }
  // ���ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ��ȥӥåȥѥ�������������NaN���֤���
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
      // �򴹤η�̡���ĤǤ�NaN�ʤ�e0:m0��NaN�������Ǥʤ���ĤǤ��ʤ�e0:m0����ˤʤäƤ���
      if(e0>emax(esize)) return valueOf(msize,esize,m0,e0,s);
      // �򴹤η�̡���ĤǤ�0�ʤ�e1:m1��0�ˤʤäƤ���
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
      // �򴹤η�̡���ĤǤ�NaN�ʤ�e0:m0��NaN�������Ǥʤ���ĤǤ��ʤ�e0:m0����ˤʤäƤ���
      if(e0>emax(esize)) {
        if(e1>emax(esize) && m0.and(mask(msize-1)).signum()==0) return nan(msize,esize); // ��-��
        else return valueOf(msize,esize,m0,e0,s);
      }
      // �򴹤η�̡���ĤǤ�0�ʤ�e1:m1��0�ˤʤäƤ���
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
      // �򴹤η�̡���ĤǤ�NaN�ʤ�e0:m0��NaN�������Ǥʤ���ĤǤ��ʤ�e0:m0����ˤʤäƤ���
      if(e0>emax(esize)) {
        if(m0.and(mask(msize-1)).signum()!=0) return valueOf(msize,esize,m0,e0,s0);
        else if(e1==0 && m1.signum()==0) return nan(msize,esize); // ��*0
        else return infinity(msize,esize,s2);
      }
      // �򴹤η�̡���ĤǤ�0�ʤ�e1:m1��0�ˤʤäƤ���
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
        if(e1>emax(esize)) return nan(msize,esize); // ��/��
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
  // �������ʤ�-1���������ʤ�0���礭���ʤ�1�������ǽ�ʤ�2
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
  // ���ߤμ����Ǥ�ɽ�������¿������
    public String toString() {
      return "(FloatConst F"+msize+'+'+esize+' '+toDecimalString()+')';
    }
  // ���ߤμ����Ǥ�ɽ�������¿������
    public String toDecimalString() {
      if(e<=emax(esize)) return toBigDecimal().toString();
      if(m.and(mask(msize-1)).signum()==0) return s ? "-Infinity" : "Infinity";
      return "NaN";
    }
  }

// constructor

  private FloatConst() {}

// factory method

/** ���ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ�����(double)�������ư������������֤�.
 * �ͤ����ꤵ�줿�ӥåȿ������Τ�ɽ���ʤ����ˤϡ�ľ��ؤδݤ��Ԥ���
 * @exception IllegalArgumentException �������Υӥåȿ���3��1073741823���ϰϳ������ؿ����Υӥåȿ���2��30���ϰϳ��ξ�� */
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
/** ���ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ�����(BigDecimal)�������ư������������֤�.
 * �ͤ�ǰ���̵�¤����ΤʥХ��ʥ��ͤ��Ѵ����Ƥ��顢���ꤵ�줿�ӥåȿ���ɽ���Ǥ���褦�ˡ�ľ��ؤδݤ��Ԥ���
 * @exception IllegalArgumentException �������Υӥåȿ���3��1073741823���ϰϳ������ؿ����Υӥåȿ���2��30���ϰϳ��ξ�� */
// ���ߤμ��������Τ����٤�
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
/** ���ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ�����(long)�������ư������������֤�.
 * �ͤ����ꤵ�줿�ӥåȿ������Τ�ɽ���ʤ����ˤϡ�ľ��ؤδݤ��Ԥ���
 * @exception IllegalArgumentException �������Υӥåȿ���3��1073741823���ϰϳ������ؿ����Υӥåȿ���2��30���ϰϳ��ξ�� */
  public static FloatConst valueOf(int msize,int esize,long val) {
    if(msize==24 && esize==8) return F.valueOf(val);
    if(msize==53 && esize==11) return D.valueOf(val);
    return valueOf(msize,esize,BigInteger.valueOf(val));
  }
/** ���ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ�����(bigInteger)�������ư������������֤�.
 * �ͤ����ꤵ�줿�ӥåȿ������Τ�ɽ���ʤ����ˤϡ�ľ��ؤδݤ��Ԥ���
 * @exception IllegalArgumentException �������Υӥåȿ���3��1073741823���ϰϳ������ؿ����Υӥåȿ���2��30���ϰϳ��ξ�� */
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
/** ���ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ�����������ꤵ�줿�ӥå�ɽ���򡢾�̤�����桢�ؿ�����������(���줿�ӥåȤ����)�Υӥå����֤Ȥ��Ʋ�ᤷ������ư������������֤�.
 * ��ᤷ����̤�NaN�ξ��ϡ��������α��줿�ӥåȤ�����Ǿ�̥ӥåȤ�Ω�Ƥ롣
 * @exception IllegalArgumentException �������Υӥåȿ���3��1073741823���ϰϳ������ؿ����Υӥåȿ���2��30���ϰϳ��ξ�� */
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
/** ���ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ�����������ꤵ�줿�ӥå�ɽ���򡢾�̤�����桢�ؿ�����������(���줿�ӥåȤ�ޤ�)�Υӥå����֤Ȥ��Ʋ�ᤷ������ư������������֤�.
 * �������α��줿�ӥåȤ�0�ʤΤ˻ؿ�����0�ʳ��ξ��ϡ���������Ԥ���
 * �������α��줿�ӥåȤ�1�ʤΤ˻ؿ�����0�ξ��ϡ��ؿ�����1�ˤ��롣
 * ��ᤷ����̤�NaN�ξ��ϡ��������α��줿�ӥåȤ�����Ǿ�̥ӥåȤ�Ω�Ƥ롣
 * @exception IllegalArgumentException �������Υӥåȿ���3��1073741823���ϰϳ������ؿ����Υӥåȿ���2��30���ϰϳ��ξ�� */
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

/** �������Υӥåȿ����֤�.
 * �ӥåȿ��򥤥󥹥��󥹤��������ʤ��褦�ʡ�����ѥ��Ȥʼ�����������ᡢ �᥽�åɤˤʤäƤ��롣 */
  public abstract int msize();
/** �ؿ����Υӥåȿ����֤�.
 * �ӥåȿ��򥤥󥹥��󥹤��������ʤ��褦�ʡ�����ѥ��Ȥʼ�����������ᡢ �᥽�åɤˤʤäƤ��롣 */
  public abstract int esize();
/** �ͤ�this+c����ư������������֤�.
 * @exception IllegalArgumentException ������ư����������Ȼ��ꤵ�줿��ư����������Υӥåȿ����ۤʤ��� */
  public abstract FloatConst add(FloatConst c);
/** �ͤ�this-c����ư������������֤�.
 * @exception IllegalArgumentException ������ư����������Ȼ��ꤵ�줿��ư����������Υӥåȿ����ۤʤ��� */
  public abstract FloatConst sub(FloatConst c);
/** �ͤ�this*c����ư������������֤�.
 * @exception IllegalArgumentException ������ư����������Ȼ��ꤵ�줿��ư����������Υӥåȿ����ۤʤ��� */
  public abstract FloatConst mul(FloatConst c);
/** �ͤ�this/c����ư������������֤�.
 * @exception IllegalArgumentException ������ư����������Ȼ��ꤵ�줿��ư����������Υӥåȿ����ۤʤ��� */
  public abstract FloatConst div(FloatConst c);
//  public abstract FloatConst mod(FloatConst c);
/** �ͤ�-this����ư������������֤�. */
  public abstract FloatConst neg();
//  public abstract FloatConst sqrt(FloatConst c);
/** this==c�ΤȤ�1�������Ǥʤ����0�Ρ����ꤵ�줿�ӥåȿ�������������֤�.
 * @exception IllegalArgumentException ������ư����������Ȼ��ꤵ�줿��ư����������Υӥåȿ����ۤʤ��� */
  public abstract IntConst tsteq(FloatConst c,int size);
/** this!=c�ΤȤ�1�������Ǥʤ����0�Ρ����ꤵ�줿�ӥåȿ�������������֤�. 
 * @exception IllegalArgumentException ������ư����������Ȼ��ꤵ�줿��ư����������Υӥåȿ����ۤʤ��� */
  public abstract IntConst tstne(FloatConst c,int size);
/** this&lt;c�ΤȤ�1�������Ǥʤ����0�Ρ����ꤵ�줿�ӥåȿ�������������֤�.
 * @exception IllegalArgumentException ������ư����������Ȼ��ꤵ�줿��ư����������Υӥåȿ����ۤʤ��� */
  public abstract IntConst tstlts(FloatConst c,int size);
/** this&gt;c�ΤȤ�1�������Ǥʤ����0�Ρ����ꤵ�줿�ӥåȿ�������������֤�.
 * @exception IllegalArgumentException ������ư����������Ȼ��ꤵ�줿��ư����������Υӥåȿ����ۤʤ��� */
  public abstract IntConst tstgts(FloatConst c,int size);
/** this&lt;=c�ΤȤ�1�������Ǥʤ����0�Ρ����ꤵ�줿�ӥåȿ�������������֤�.
 * @exception IllegalArgumentException ������ư����������Ȼ��ꤵ�줿��ư����������Υӥåȿ����ۤʤ��� */
  public abstract IntConst tstles(FloatConst c,int size);
/** this&gt;=c�ΤȤ�1�������Ǥʤ����0�Ρ����ꤵ�줿�ӥåȿ�������������֤�.
 * @exception IllegalArgumentException ������ư����������Ȼ��ꤵ�줿��ư����������Υӥåȿ����ۤʤ��� */
  public abstract IntConst tstges(FloatConst c,int size);
/** ������ư����������򡢻��ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ��˳�ĥ�����֤�.
 * @exception IllegalArgumentException �������Υӥåȿ������ߤ��ͤ���1073741823�ޤǤ��ϰϳ������ؿ����Υӥåȿ������ߤ��ͤ���30�ޤǤ��ϰϳ��ξ�� */
  public abstract FloatConst convfx(int msize,int esize);
/** ������ư����������򡢻��ꤵ�줿�������Υӥåȿ��Ȼؿ����Υӥåȿ��˽̾������֤�.
 * �ͤ����ꤵ�줿�ӥåȿ������Τ�ɽ���ʤ����ˤϡ�ľ��ؤδݤ��Ԥ���
 * @exception IllegalArgumentException �������Υӥåȿ���3���鸽�ߤ��ͤޤǤ��ϰϳ������ؿ����Υӥåȿ���2���鸽�ߤ��ͤޤǤ��ϰϳ��ξ�� */
  public abstract FloatConst convft(int msize,int esize);
/** ������ư���������򡢻��ꤵ�줿�ݤ�⡼�ɤǻ��ꤵ�줿�ӥåȿ�������������Ѵ������֤�.
 * �ͤ����ꤵ�줿�ӥåȿ���ɽ�����ϰϤ�Ķ���Ƥ�����ˤϡ���̤ΥӥåȤ�ΤƤ롣
 * @exception IllegalArgumentException ������ư�����������̵����ޤ���NaN�ξ�� */
  public abstract IntConst convfi(int size,RoundingMode r);
/** ������ư�����������float���ͤ��֤�. */
  public abstract float floatValue();
/** ������ư�����������double���ͤ��֤�. */
  public abstract double doubleValue();
/** ������ư�����������BigDecimal���Ѵ������֤�.
 * BigDecimal�Υ�������ϡ�(10<SUP>scale</SUP>*this)�������Ǥ���Ǿ����ͤȤʤ롣 */
  public abstract BigDecimal toBigDecimal();
/** ������ư����������򡢻��ꤵ�줿�ݤ�⡼�ɤ�BigInteger���Ѵ������֤�.
 * @exception IllegalArgumentException ������ư�����������̵����ޤ���NaN�ξ�� */
  public abstract BigInteger toBigInteger(RoundingMode r);
/** ������ư����������򡢾�̤�����桢�ؿ�����������(���줿�ӥåȤ����)�Υӥå����֤�BigInteger���Ѵ������֤�. */
  public abstract BigInteger toBigIntegerBits();
/** ������ư����������򡢾�̤�����桢�ؿ�����������(���줿�ӥåȤ�ޤ�)�Υӥå����֤�BigInteger���Ѵ������֤�.
 ��������msb�ϡ�������ӥǥΡ��ޥ���ΤȤ��Τ�0�Ȥʤ롣 */
  public abstract BigInteger toExplicitBigIntegerBits();
/** ������ư����������Ȼ��ꤵ�줿���֥������Ȥ����������ɤ������֤�. */
  public abstract boolean equals(Object o);
/** ������ư����������Υϥå��女���ɤ��֤�. */
  public abstract int hashCode();
/** ������ư�����������ʸ����ɽ�����֤�.
 * ʸ����ϡ��㤨��"(FloatConst F24+8 1.25)"�Τ褦�ˤʤ롣 */
  public abstract String toString();
/** ������ư�����������10��ʸ����ɽ�����֤�.
 * ʸ����ϡ��㤨��"1.25"�Τ褦�ˤʤ롣 */
  public abstract String toDecimalString();
// bias
  private static int bias(int esize) { return (1<<(esize-1))-1; }
// bias�û���λؿ����κ���
// �ؿ����κǾ���1�ʤΤǥ᥽�åɤ��Ѱդ��ʤ�
  private static int emax(int esize) { return (1<<esize)-2; }
// ���ꤵ�줿�ӥåȿ���BigInteger�Υޥ���
  private static BigInteger mask(int i) {
    return BigInteger.ONE.shiftLeft(i).subtract(BigInteger.ONE);
  }

}
