/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.util;

/**
 * �����ξ岼��(���Ѥ��ͥ��饹).
 * ͭ�¥ӥåȿ���2�ʿ����ͤΤȤ������ϰϤ򡢾峦�Ȳ�����ɽ����
 * �����ϰϤ���������Ȥ�Ȥϸ¤�ʤ����㤨��3,4,7��Ȥ�������ˤ�3����7�ޤǤ�ɽ����
 * �峦�Ȳ����ϡ����Υӥåȿ�����������Ǥ��롣
 * ���ʤ���������Ĥ���ξ���β����б����뤿�ᡢ����&gt;�峦�Ǥ��äƤ�褤�Ȥ��롣
 * �Ĥޤꡢ����&lt;=�峦�ʤ顢����&lt;=��&lt;=�峦�Ǥ��뤳�Ȥ�ɽ����
 * ����&gt;�峦�ʤ顢����&lt;=�ͤޤ�����&lt;=�峦�Ǥ��뤳�Ȥ�ɽ����
 * �ϰϤ��黻�ˤ�äƤɤΤ褦�����Ť��뤫�����᥽�åɤ��󶡤��롣
 */
public final class IntBound {

// field

/** ���� */
  public final IntConst lower;
/** �峦 */
  public final IntConst upper;

// constructor

/** �����Ⱦ峦��������IntBound���������. */
  public IntBound(IntConst val) {
    this.lower=val; this.upper=val;
  }
/** ���ꤵ�줿�����Ⱦ峦��IntBound���������.
 * @exception IllegalArgumentException �����Ⱦ峦�Υӥåȿ����ۤʤ��� */
  public IntBound(IntConst lower,IntConst upper) {
    if(lower.size()!=upper.size()) throw new IllegalArgumentException(lower+" "+upper);
    this.lower=lower; this.upper=upper;
  }

// method

/** ����IntBound�Υӥåȿ����֤�. */
  public int size() {
    return lower.size();
  }
/** ���ꤵ�줿�������������IntBound�����äƤ��뤫�ɤ������֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public boolean contains(IntConst c) {
    if(size()!=c.size()) throw new IllegalArgumentException(this+" "+c);
    return c.sub(lower).compareTo(upper.sub(lower))<=0;
  }
/** ���ꤵ�줿IntBound�Ȥι�ʻ���֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ��� */
  public IntBound union(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // ����IntBound�β�����������
    IntConst u0=upper.sub(lower);
    IntConst l1=b.lower.sub(lower);
    IntConst u1=b.upper.sub(lower);
    if(l1.compareTo(u1)<=0) { // l0=0,l1,u1�ν�
      if(u1.compareTo(u0)<=0) return this;
      if(l1.compareTo(u0)<=0) return new IntBound(lower,b.upper);
      // l0=0,u0,l1,u1�ν�ΤȤ��ϡ�u1-l0=u1��u0-l1����٤�
      if(u1.compareTo(u0.sub(l1))<=0) return new IntBound(lower,b.upper);
      else return new IntBound(b.lower,upper);
    } else { // l0=0,u1,l1�ν�
      if(u1.compareTo(u0)>=0) return b;
      if(l1.compareTo(u0)>0) return new IntBound(b.lower,upper);
      // l0=0,u1,l1,u0�ΤȤ��ϡ�����ˤ錄��
      IntConst l=IntConst.valueOf(size(),0);
      return new IntBound(l,l.bnot());
    }
  }
/** ���ꤵ�줿IntBound�Ȥζ�����ʬ���֤�.
 * ������ʬ�����ΤȤ���null���֤���
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ��� */
  public IntBound intersection(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // ����IntBound�β�����������
    IntConst u0=upper.sub(lower);
    IntConst l1=b.lower.sub(lower);
    IntConst u1=b.upper.sub(lower);
    if(l1.compareTo(u1)<=0) { // l0=0,l1,u1�ν�
      if(u1.compareTo(u0)<=0) return b;
      if(l1.compareTo(u0)<=0) return new IntBound(b.lower,upper);
      // l0=0,u0,l1,u1�ν�ΤȤ��ϡ�������
      return null;
    } else { // l0=0,u1,l1�ν�
      if(u1.compareTo(u0)>=0) return this;
      if(l1.compareTo(u0)>0) return new IntBound(lower,b.upper);
      // l0=0,u1,l1,u0�ν�ΤȤ��ϡ�u0-l0=u0��u1-l1����٤�
      if(u0.compareTo(u1.sub(l1))<=0) return this;
      else return b;
    }
  }
/** this+b��IntBound���֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ��� */
  public IntBound add(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    IntConst r0=upper.sub(lower); // this�ξ岼���κ�
    IntConst r1=b.upper.sub(b.lower); // b�ξ岼���κ�
    IntConst l,u;
    // r0��r1���¤���åץ��饦��ɤ����顢this+b������ˤ錄��
    if(r0.add(r1).compareTo(r0)<0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    // �����Ǥʤ���С����̤˷׻�
    else { l=lower.add(b.lower); u=upper.add(b.upper); }
    return new IntBound(l,u);
  }
/** this-b��IntBound���֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ��� */
  public IntBound sub(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    IntConst r0=upper.sub(lower); // this�ξ岼���κ�
    IntConst r1=b.upper.sub(b.lower); // b�ξ岼���κ�
    IntConst l,u;
    // r0��r1���¤���åץ��饦��ɤ����顢this-b������ˤ錄��
    if(r0.add(r1).compareTo(r0)<0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    // �����Ǥʤ���С����̤˷׻�
    else { l=lower.sub(b.upper); u=upper.sub(b.lower); }
    return new IntBound(l,u);
  }
/** this*b��IntBound���֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ��� */
  public IntBound mul(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    final IntConst zero=IntConst.valueOf(size(),0);
    final IntConst msb=IntConst.valueOf(size(),1).lsh(size()-1);
    final IntBound plus=new IntBound(zero,msb.bnot()); // 0�ޤ��������ϰ�
    final IntBound minus=new IntBound(msb,zero.bnot()); // ����ϰ�
    IntBound result=null;
    IntBound b0,b1,b2;
    // �軻�ϥ��ڥ��ɤ������ͤ��礭����ʬ�Ǥ�ͭ�פʷ�̤��Фʤ��Τǡ����Ĥ��Ȥ��ƹͤ���
    // ���ڥ��ɤ����Ǿ��ʬ��������Τι�ʻ����롣
    if((b0=this.intersection(plus))!=null) {
      if((b1=b.intersection(plus))!=null) {
        result=mul1(b0,b1);
      }
      if((b1=b.intersection(minus))!=null) {
        b2=mul1(b0,b1.neg()).neg();
        result=result==null ? b2 : result.union(b2);
      }
    }
    if((b0=this.intersection(minus))!=null) {
      if((b1=b.intersection(plus))!=null) {
        b2=mul1(b0.neg(),b1).neg();
        result=result==null ? b2 : result.union(b2);
      }
      if((b1=b.intersection(minus))!=null) {
        b2=mul1(b0.neg(),b1.neg());
        result=result==null ? b2 : result.union(b2);
      }
    }
    //assert result!=null;
    return result;
  }
  // ��椬����ξ��Ρ��Ѥξ岼�����֤�
  private static IntBound mul1(IntBound b0,IntBound b1) {
    //assert b0.size()==b1.size();
    final int s=b0.size(),s2=s*2;
    final IntConst m=IntConst.valueOf(s2,1).lsh(s);
    IntConst l,u;
    // ��Ĺ�Ƿ׻����ơ��Ѥ��ϰϤ���åץ��饦��ɤ��Ƥ����顢����ˤ錄��
    if(b0.upper.convzx(s2).mul(b1.upper.convzx(s2))
      .sub(b0.lower.convzx(s2).mul(b1.lower.convzx(s2)))
      .compareTo(m)>=0) { l=IntConst.valueOf(s,0); u=l.bnot(); }
    // �����Ǥʤ���С����̤˷׻�
    else { l=b0.lower.mul(b1.lower); u=b0.upper.mul(b1.upper); }
    return new IntBound(l,u);
  }
/** this/b(���ʤ�����)��IntBound���֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ���
 * @exception ArithmeticException b��0�Τߤξ�� */
  public IntBound divu(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    IntConst l0=lower,u0=upper;
    IntConst l1=b.lower,u1=b.upper;
    // l0>u0,l1>u1�ΤȤ�������ˤ錄��Ȥ���
    if(l0.compareTo(u0)>0) { l0=IntConst.valueOf(size(),0); u0=l0.bnot(); }
    if(l1.compareTo(u1)>0) { l1=IntConst.valueOf(size(),0); u1=l1.bnot(); }
    // l1��0�ΤȤ���1�ˤ���
    if(l1.signum()==0) l1=IntConst.valueOf(size(),1);
    // ���̤˷׻�(u1��0�ΤȤ���ArithmeticException)
    return new IntBound(l0.divu(u1),u0.divu(l1));
  }
/** this/b(���Ĥ�����)��IntBound���֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ���
 * @exception ArithmeticException b��0�Τߤξ�� */
  public IntBound divs(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    if(b.lower.signum()==0 && b.upper.signum()==0) throw new ArithmeticException(this+" "+b);
    final IntConst zero=IntConst.valueOf(size(),0);
    final IntConst msb=IntConst.valueOf(size(),1).lsh(size()-1);
    final IntBound plus=new IntBound(zero,msb.bnot()); // 0�ޤ��������ϰ�
    final IntBound minus=new IntBound(msb,zero.bnot()); // ����ϰ�
    IntBound result=null;
    IntBound b0,b1,b2;
    // ���ڥ��ɤ����Ǿ��ʬ��������Τι�ʻ����롣
    if((b0=this.intersection(plus))!=null) {
      if((b1=b.intersection(plus))!=null) {
        result=b0.divu(b1);
      }
      if((b1=b.intersection(minus))!=null) {
        b2=b0.divu(b1.neg()).neg();
        result=result==null ? b2 : result.union(b2);
      }
    }
    if((b0=this.intersection(minus))!=null) {
      if((b1=b.intersection(plus))!=null) {
        b2=b0.neg().divu(b1).neg();
        result=result==null ? b2 : result.union(b2);
      }
      if((b1=b.intersection(minus))!=null) {
        b2=b0.neg().divu(b1.neg());
        result=result==null ? b2 : result.union(b2);
      }
    }
    //assert result!=null;
    return result;
  }
/** this%b(���ʤ���;)��IntBound���֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ���
 * @exception ArithmeticException b��0�Τߤξ�� */
  public IntBound modu(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // b��ñ����ͤǡ���������ΤȤ��ϡ����̤˷׻�
    // �����Ǥʤ���b.lower<=b.upper�ʤ顢0�ʾ�b.upper̤��
    // �����ʳ��ʤ顢���ϰϤˤ錄��
    if(b.lower.equals(b.upper)) {
      if(b.lower.signum()==0) throw new ArithmeticException(this+" "+b);
      if(lower.compareTo(upper)<=0 && lower.divu(b.lower).equals(upper.divu(b.lower))) {
        return new IntBound(lower.modu(b.lower),upper.modu(b.lower));
      }
    }
    IntConst l=IntConst.valueOf(size(),0);
    IntConst u=b.lower.compareTo(b.upper)<=0 ? b.upper.sub(IntConst.valueOf(size(),1)) : l.bnot();
    return new IntBound(l,u);
  }
/** this%b(���Ĥ���;)��IntBound���֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ���
 * @exception ArithmeticException b��0�Τߤξ�� */
  public IntBound mods(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    if(b.lower.signum()==0 && b.upper.signum()==0) throw new ArithmeticException(this+" "+b);
    final IntConst zero=IntConst.valueOf(size(),0);
    final IntConst msb=IntConst.valueOf(size(),1).lsh(size()-1);
    final IntBound plus=new IntBound(zero,msb.bnot()); // 0�ޤ��������ϰ�
    final IntBound minus=new IntBound(msb,zero.bnot()); // ����ϰ�
    IntBound result=null;
    IntBound b0,b1,b2;
    // ���ڥ��ɤ����Ǿ��ʬ��������Τι�ʻ����롣
    if((b0=this.intersection(plus))!=null) {
      if((b1=b.intersection(plus))!=null) {
        result=b0.modu(b1);
      }
      if((b1=b.intersection(minus))!=null) {
        b2=b0.modu(b1.neg());
        result=result==null ? b2 : result.union(b2);
      }
    }
    if((b0=this.intersection(minus))!=null) {
      if((b1=b.intersection(plus))!=null) {
        b2=b0.neg().modu(b1).neg();
        result=result==null ? b2 : result.union(b2);
      }
      if((b1=b.intersection(minus))!=null) {
        b2=b0.neg().modu(b1.neg()).neg();
        result=result==null ? b2 : result.union(b2);
      }
    }
    //assert result!=null;
    return result;
  }
/** this&amp;b��IntBound���֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ��� */
  public IntBound band(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // 0��ޤ����Ǥ���Ȥ���ʬ�䤷�ơ����줾��ξ岼���ι�ʻ�����
    if(lower.compareTo(upper)<=0) {
      if(b.lower.compareTo(b.upper)<=0) {
        return minMaxAnd(lower,upper,b.lower,b.upper);
      } else {
        IntConst c0=IntConst.valueOf(size(),0),c1=c0.bnot();
        return minMaxAnd(lower,upper,c0,b.upper).union(minMaxAnd(lower,upper,b.lower,c1));
      }
    } else {
      if(b.lower.compareTo(b.upper)<=0) {
        IntConst c0=IntConst.valueOf(size(),0),c1=c0.bnot();
        return minMaxAnd(c0,upper,b.lower,b.upper).union(minMaxAnd(lower,c1,b.lower,b.upper));
      } else {
        IntConst c0=IntConst.valueOf(size(),0),c1=c0.bnot();
        return minMaxAnd(c0,upper,c0,b.upper).union(minMaxAnd(c0,upper,b.lower,c1))
               .union(minMaxAnd(lower,c1,c0,b.upper)).union(minMaxAnd(lower,c1,b.lower,c1));
      }
    }
  }
// a<=x<=b, c<=y<=d�ΤȤ��Ρ�x&y��IntBound���֤�
  private static IntBound minMaxAnd(IntConst a,IntConst b,IntConst c,IntConst d) {
    return new IntBound(minAnd(a,b,c,d),maxAnd(a,b,c,d));
  }
// a<=x<=b, c<=y<=d�ΤȤ��Ρ�x&y�κǾ��ͤ��֤�
// ����: Hacker's Delight 4-3
  private static IntConst minAnd(IntConst a,IntConst b,IntConst c,IntConst d) {
    final int size=a.size();
    IntConst m=IntConst.valueOf(size,1).lsh(size-1);
    while(m.signum()!=0) {
      if(a.bor(c).bnot().band(m).signum()!=0) {
        IntConst t;
        t=a.bor(m).band(m.neg());
        if(t.compareTo(b)<=0) { a=t; break; }
        t=c.bor(m).band(m.neg());
        if(t.compareTo(d)<=0) { c=t; break; }
      }
      m=m.rshu(1);
    }
    return a.band(c);
  }
// a<=x<=b, c<=y<=d�ΤȤ��Ρ�x&y�κ����ͤ��֤�
// ����: Hacker's Delight 4-3
  private static IntConst maxAnd(IntConst a,IntConst b,IntConst c,IntConst d) {
    final int size=a.size();
    IntConst m=IntConst.valueOf(size,1).lsh(size-1);
    final IntConst one=IntConst.valueOf(size,1);
    while(m.signum()!=0) {
      if(b.band(d.bnot()).band(m).signum()!=0) {
        IntConst t=b.band(m.bnot()).bor(m.sub(one));
        if(t.compareTo(a)>=0) { b=t; break; }
      } else if(b.bnot().band(d).band(m).signum()!=0) {
        IntConst t=d.band(m.bnot()).bor(m.sub(one));
        if(t.compareTo(c)<=0) { d=t; break; }
      }
      m=m.rshu(1);
    }
    return b.band(d);
  }
/** this|b��IntBound���֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ��� */
  public IntBound bor(IntBound b) {
    return this.bnot().band(b.bnot()).bnot();
  }
/** this^b��IntBound���֤�.
 * @exception IllegalArgumentException ����IntBound�Ȼ��ꤵ�줿IntBound�Υӥåȿ����ۤʤ��� */
  public IntBound bxor(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // 0��ޤ����Ǥ���Ȥ���ʬ�䤷�ơ����줾��ξ岼���ι�ʻ�����
    if(lower.compareTo(upper)<=0) {
      if(b.lower.compareTo(b.upper)<=0) {
        return minMaxXor(lower,upper,b.lower,b.upper);
      } else {
        IntConst c0=IntConst.valueOf(size(),0),c1=c0.bnot();
        return minMaxXor(lower,upper,c0,b.upper).union(minMaxXor(lower,upper,b.lower,c1));
      }
    } else {
      if(b.lower.compareTo(b.upper)<=0) {
        IntConst c0=IntConst.valueOf(size(),0),c1=c0.bnot();
        return minMaxXor(c0,upper,b.lower,b.upper).union(minMaxXor(lower,c1,b.lower,b.upper));
      } else {
        IntConst c0=IntConst.valueOf(size(),0),c1=c0.bnot();
        return minMaxXor(c0,upper,c0,b.upper).union(minMaxXor(c0,upper,b.lower,c1))
               .union(minMaxXor(upper,c1,c0,b.upper)).union(minMaxXor(upper,c1,b.lower,c1));
      }
    }
  }
// a<=x<=b, c<=y<=d�ΤȤ��Ρ�x^y��IntBound���֤�
  private static IntBound minMaxXor(IntConst a,IntConst b,IntConst c,IntConst d) {
    return new IntBound(minXor(a,b,c,d),maxXor(a,b,c,d));
  }
// a<=x<=b, c<=y<=d�ΤȤ��Ρ�x^y�β����ΰ�Ĥ��֤�
// ����: Hacker's Delight 4-3
  private static IntConst minXor(IntConst a,IntConst b,IntConst c,IntConst d) {
    return minAnd(a,b,d.bnot(),c.bnot()).bor(minAnd(b.bnot(),a.bnot(),c,d));
  }
// a<=x<=b, c<=y<=d�ΤȤ��Ρ�x^y�ξ峦�ΰ�Ĥ��֤�
// ����: Hacker's Delight 4-3
  private static IntConst maxXor(IntConst a,IntConst b,IntConst c,IntConst d) {
    final IntConst m=IntConst.valueOf(a.size(),0).bnot();
    return minAnd(maxAnd(a,b,d.bnot(),c.bnot()).bnot(),m,
                  maxAnd(b.bnot(),a.bnot(),c,d).bnot(),m).bnot();
  }
/** this&lt;&lt;b��IntBound���֤�. */
  public IntBound lsh(IntBound b) {
    b=saturate(b,size());
    IntBound result=null;
    // 0����ӥåȿ��ޤǤγƥ��եȥ�����Ȥ��ϰϤ��ᡢ��ʻ��Ȥ�
    for(int i=0;i<=size();i++) if(b.contains(IntConst.valueOf(b.size(),i))) {
      IntBound b1=this.lsh(i);
      result=result==null ? b1 : result.union(b1);
    }
    return result;
  }
/** this&gt;&gt;b(���ʤ������ե�)��IntBound���֤�. */
  public IntBound rshu(IntBound b) {
    b=saturate(b,size());
    IntBound result=null;
    // 0����ӥåȿ��ޤǤγƥ��եȥ�����Ȥ��ϰϤ��ᡢ��ʻ��Ȥ�
    for(int i=0;i<=size();i++) if(b.contains(IntConst.valueOf(b.size(),i))) {
      IntBound b1=this.rshu(i);
      result=result==null ? b1 : result.union(b1);
    }
    return result;
  }
/** this&gt;&gt;b(���Ĥ������ե�)��IntBound���֤�. */
  public IntBound rshs(IntBound b) {
    b=saturate(b,size());
    IntBound result=null;
    // 0����ӥåȿ��ޤǤγƥ��եȥ�����Ȥ��ϰϤ��ᡢ��ʻ��Ȥ�
    for(int i=0;i<=size();i++) if(b.contains(IntConst.valueOf(b.size(),i))) {
      IntBound b1=this.rshs(i);
      result=result==null ? b1 : result.union(b1);
    }
    return result;
  }
// ���ꤵ�줿IntBound��0����i�ޤǤ�˰�¤������֤�
  private static IntBound saturate(IntBound b,int i) {
    IntConst i1=IntConst.valueOf(b.size(),i);
    IntConst l=b.lower,u=b.upper;
    int cmp=l.compareTo(u);
    // �岼����i�ޤǤ�˰�¤�����
    if(l.compareTo(i1)>=0) l=i1;
    if(u.compareTo(i1)>=0) {
      u=i1;
      if(cmp>0) l=IntConst.valueOf(b.size(),0); // ˰������i,u,l�ν���ä���硢������0��
    }
    return new IntBound(l,u);
  }
/** this&lt;&lt;n��IntBound���֤�.
 * @exception IllegalArgumentException n����ξ�� */
  public IntBound lsh(int n) {
    if(n<0) throw new IllegalArgumentException(String.valueOf(n));
    if(n==0) return this;
    if(n>=size()) return new IntBound(IntConst.valueOf(size(),0));
    IntConst r=upper.sub(lower);
    IntConst l,u;
    // r(this�ξ岼���κ�)�����եȸ�ˤϤ߽Ф��ʤ顢���ϰϤˤ錄��
    // �����Ǥʤ���С����̤˷׻�
    if(r.compareTo(IntConst.valueOf(size(),1).lsh(size()-n))>=0) {
      l=IntConst.valueOf(size(),0); u=l.bnot();
    } else {
      l=lower.lsh(n); u=upper.lsh(n);
    }
    return new IntBound(l,u);
  }
/** this&gt;&gt;n(���ʤ������ե�)��IntBound���֤�.
 * @exception IllegalArgumentException n����ξ�� */
  public IntBound rshu(int n) {
    if(n<0) throw new IllegalArgumentException(String.valueOf(n));
    if(n==0) return this;
    IntConst l=lower,u=upper;
    // �岼���򤽤줾�쥷�եȤ���������ȡ��������峦���(���ʤ���)�礭���Ȥ���
    // (1)��̥ӥåȤ�0�Ǥʤ��ͤ������ޤ�Ǥ��ޤ���
    // (2)�峦�������ǲ������峦���1�礭���Ȥ��ʤɤǡ��岼����Ʊ���ˤʤ롣
    // �Ȥ������꤬����Τǡ����ξ���0��~0�򥷥եȤ�����Τ�Ȥ���
    if(l.compareTo(u)>0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    // �����Ǥʤ���С����̤˷׻�
    return new IntBound(l.rshu(n),u.rshu(n));
  }
/** this&gt;&gt;n(���Ĥ������ե�)��IntBound���֤�.
 * @exception IllegalArgumentException n����ξ�� */
  public IntBound rshs(int n) {
    if(n<0) throw new IllegalArgumentException(String.valueOf(n));
    if(n==0) return this;
    IntConst l=lower,u=upper;
    // �岼���򤽤줾�쥷�եȤ���������ȡ��������峦���(���Ĥ���)�礭���Ȥ���
    // (1)��̥ӥåȤ�����ĥ�Ǥʤ��ͤ������ޤ�Ǥ��ޤ���
    // (2)�峦�������ǲ������峦���1�礭���Ȥ��ʤɤǡ��岼����Ʊ���ˤʤ롣
    // �Ȥ������꤬����Τǡ����ξ������Ĥ��κǾ��Ⱥ���򥷥եȤ�����Τ�Ȥ���
    if(l.signedCompareTo(u)>0) { l=IntConst.valueOf(size(),1).lsh(size()-1); u=l.bnot(); }
    // �����Ǥʤ���С����̤˷׻�
    return new IntBound(l.rshs(n),u.rshs(n));
  }
/** -this��IntBound���֤�. */
  public IntBound neg() {
    return new IntBound(upper.neg(),lower.neg());
  }
/** ~this��IntBound���֤�. */
  public IntBound bnot() {
    return new IntBound(upper.bnot(),lower.bnot());
  }
/** this==b��IntBound���֤�.
 * @exception IllegalArgumentException ������������Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntBound tsteq(IntBound b,int s) { return cmpeq(this,b,s,1); }
/** this!=b��IntBound���֤�.
 * @exception IllegalArgumentException ������������Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntBound tstne(IntBound b,int s) { return cmpeq(this,b,s,0); }
/** this&lt;b(���ʤ����)��IntBound���֤�.
 * @exception IllegalArgumentException ������������Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntBound tstltu(IntBound b,int s) { return cmpu(this,b,s,1); }
/** this&gt;b(���ʤ����)��IntBound���֤�.
 * @exception IllegalArgumentException ������������Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntBound tstgtu(IntBound b,int s) { return cmpu(b,this,s,1); }
/** this&lt;=b(���ʤ����)��IntBound���֤�.
 * @exception IllegalArgumentException ������������Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntBound tstleu(IntBound b,int s) { return cmpu(b,this,s,0); }
/** this&gt;=b(���ʤ����)��IntBound���֤�.
 * @exception IllegalArgumentException ������������Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntBound tstgeu(IntBound b,int s) { return cmpu(this,b,s,0); }
/** this&lt;b(���Ĥ����)��IntBound���֤�.
 * @exception IllegalArgumentException ������������Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntBound tstlts(IntBound b,int s) { return cmps(this,b,s,1); }
/** this&gt;b(���Ĥ����)��IntBound���֤�.
 * @exception IllegalArgumentException ������������Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntBound tstgts(IntBound b,int s) { return cmps(b,this,s,1); }
/** this&lt;=b(���Ĥ����)��IntBound���֤�.
 * @exception IllegalArgumentException ������������Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntBound tstles(IntBound b,int s) { return cmps(b,this,s,0); }
/** this&gt;=b(���Ĥ����)��IntBound���֤�.
 * @exception IllegalArgumentException ������������Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntBound tstges(IntBound b,int s) { return cmps(this,b,s,0); }
// b0��b1��ɬ���������ʤ�s�ӥåȤ�eq��ɬ���ۤʤ�ʤ�s�ӥåȤ�1-eq��
// ����ʳ��ʤ�s�ӥåȤ�0��1��ޤ�IntBound���֤�
  private static IntBound cmpeq(IntBound b0,IntBound b1,int s,int eq) {
    if(b0.size()!=b1.size()) throw new IllegalArgumentException(b0+" "+b1);
    if(b0.lower.equals(b0.upper) && b1.lower.equals(b1.upper) &&
       b0.lower.equals(b1.lower)) return new IntBound(IntConst.valueOf(s,eq));
    if(b0.intersection(b1)==null) return new IntBound(IntConst.valueOf(s,1-eq));
    return new IntBound(IntConst.valueOf(s,0),IntConst.valueOf(s,1));
  }
// b0��b1���ɬ�����������s�ӥåȤ�lt��b0��ɬ��b1�ʾ�ʤ�s�ӥåȤ�1-lt��
// ����ʳ��ʤ�s�ӥåȤ�0��1��ޤ�IntBound���֤�(���ʤ����)
  private static IntBound cmpu(IntBound b0,IntBound b1,int s,int lt) {
    if(b0.size()!=b1.size()) throw new IllegalArgumentException(b0+" "+b1);
    if(b0.lower.compareTo(b0.upper)<=0 && b1.lower.compareTo(b1.upper)<=0) {
      if(b0.upper.compareTo(b1.lower)<0) return new IntBound(IntConst.valueOf(s,lt));
      if(b0.lower.compareTo(b1.upper)>=0) return new IntBound(IntConst.valueOf(s,1-lt));
    }
    return new IntBound(IntConst.valueOf(s,0),IntConst.valueOf(s,1));
  }
// b0��b1���ɬ�����������s�ӥåȤ�lt��b0��ɬ��b1�ʾ�ʤ�s�ӥåȤ�1-lt��
// ����ʳ��ʤ�s�ӥåȤ�0��1��ޤ�IntBound���֤�(���Ĥ����)
  private static IntBound cmps(IntBound b0,IntBound b1,int s,int lt) {
    if(b0.size()!=b1.size()) throw new IllegalArgumentException(b0+" "+b1);
    if(b0.lower.signedCompareTo(b0.upper)<=0 && b1.lower.signedCompareTo(b1.upper)<=0) {
      if(b0.upper.signedCompareTo(b1.lower)<0) return new IntBound(IntConst.valueOf(s,lt));
      if(b0.lower.signedCompareTo(b1.upper)>=0) return new IntBound(IntConst.valueOf(s,1-lt));
    }
    return new IntBound(IntConst.valueOf(s,0),IntConst.valueOf(s,1));
  }
/** ����IntBound����ꤵ�줿�ӥåȿ��˥����ĥ�����Ȥ���IntBound���֤�.
 * @exception IllegalArgumentException ���ꤵ�줿�ӥåȿ��Τۤ������������ */
  public IntBound convzx(int s) {
    if(s==size()) return this;
    if(s<size()) throw new IllegalArgumentException(this+" "+s);
    IntConst l=lower,u=upper;
    // �岼���򤽤줾�쥼���ĥ��������Ǥ�褤����
    // �������峦���(���ʤ���)�礭���Ȥ��ϡ���̥ӥåȤ�0�Ǥʤ��ͤ������ޤ�Ǥ��ޤ���
    // ���ξ��ϡ����Υӥåȿ���0��~0�Υ����ĥ�Τۤ����褤��
    if(l.compareTo(u)>0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    return new IntBound(l.convzx(s),u.convzx(s));
  }
/** ����IntBound����ꤵ�줿�ӥåȿ�������ĥ�����Ȥ���IntBound���֤�.
 * @exception IllegalArgumentException ���ꤵ�줿�ӥåȿ��Τۤ������������ */
  public IntBound convsx(int s) {
    if(s==size()) return this;
    if(s<size()) throw new IllegalArgumentException(this+" "+s);
    IntConst l=lower,u=upper;
    // �岼���򤽤줾������ĥ��������Ǥ�褤����
    // �������峦���(���Ĥ���)�礭���Ȥ��ϡ���̥ӥåȤ�����ĥ�Ǥʤ��ͤ������ޤ�Ǥ��ޤ���
    // ���ξ��ϡ����Υӥåȿ������Ĥ��κǾ��Ⱥ��������ĥ�Τۤ����褤��
    if(l.signedCompareTo(u)>0) { l=IntConst.valueOf(size(),1).lsh(size()-1); u=l.bnot(); }
    return new IntBound(l.convsx(s),u.convsx(s));
  }
/** ����IntBound����ꤵ�줿�ӥåȿ��˽̾������Ȥ���IntBound���֤�.
 * @exception IllegalArgumentException ���ꤵ�줿�ӥåȿ��Τۤ����礭�����ޤ������Ǥʤ���� */
  public IntBound convit(int s) {
    if(s==size()) return this;
    if(s>size() || s<=0) throw new IllegalArgumentException(this+" "+s);
    IntConst r=upper.sub(lower);
    IntConst l,u;
    // r(this�ξ岼���κ�)���̾��ǤϤ߽Ф��ʤ顢���ϰϤˤ錄��
    // �����Ǥʤ�������̤˷׻�
    if(r.compareTo(IntConst.valueOf(size(),1).lsh(s))>=0) {
      l=IntConst.valueOf(s,0); u=l.bnot();
    } else {
      l=lower.convit(s); u=upper.convit(s);
    }
    return new IntBound(l,u);
  }
/** ���ꤵ�줿�������t��f�Τ������������������0�ʳ��ʤ�t��0�ʤ�f���ͤȤ���Ȥ���IntBound���֤�.
 * @exception IllegalArgumentException t��f�Υӥåȿ����ۤʤ��� */
  public IntBound ifthenelse(IntBound t,IntBound f) {
    if(t.size()!=f.size()) throw new IllegalArgumentException(this+" "+t+' '+f);
    if(lower.signum()==0 && lower.equals(upper)) return t;
    if(!this.contains(IntConst.valueOf(size(),0))) return f;
    return t.union(f);
  }
/** ����IntBound�Ȼ��ꤵ�줿���֥������Ȥ����������ɤ������֤�. */
  public boolean equals(Object o) {
    return o==this ||
           o instanceof IntBound &&
           lower.equals(((IntBound)o).lower) && upper.equals(((IntBound)o).upper);
  }
/** ����IntBound�Υϥå��女���ɤ��֤�. */
  public int hashCode() {
    return lower.hashCode()*37+upper.hashCode();
  }
/** ����IntBound��ʸ����ɽ�����֤�. */
  public String toString() {
    return "("+lower.bigValue()+".."+upper.bigValue()+"):"+size();
  }

}
