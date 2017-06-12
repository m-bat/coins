/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.util;

/**
 * 整数の上下界(不変な値クラス).
 * 有限ビット数の2進数の値のとり得る範囲を、上界と下界で表す。
 * その範囲の値全部をとるとは限らない。例えば3,4,7をとり得る場合には3から7までと表す。
 * 上界と下界は、そのビット数の整数定数である。
 * 符号なしおよび符号つきの両方の解釈に対応するため、下界&gt;上界であってもよいとする。
 * つまり、下界&lt;=上界なら、下界&lt;=値&lt;=上界であることを表し、
 * 下界&gt;上界なら、下界&lt;=値または値&lt;=上界であることを表す。
 * 範囲が演算によってどのように伝播するかを求めるメソッドを提供する。
 */
public final class IntBound {

// field

/** 下界 */
  public final IntConst lower;
/** 上界 */
  public final IntConst upper;

// constructor

/** 下界と上界が等しいIntBoundを作成する. */
  public IntBound(IntConst val) {
    this.lower=val; this.upper=val;
  }
/** 指定された下界と上界のIntBoundを作成する.
 * @exception IllegalArgumentException 下界と上界のビット数が異なる場合 */
  public IntBound(IntConst lower,IntConst upper) {
    if(lower.size()!=upper.size()) throw new IllegalArgumentException(lower+" "+upper);
    this.lower=lower; this.upper=upper;
  }

// method

/** このIntBoundのビット数を返す. */
  public int size() {
    return lower.size();
  }
/** 指定された整数定数がこのIntBoundに入っているかどうかを返す.
 * @exception IllegalArgumentException このIntBoundと指定された整数定数のビット数が異なる場合 */
  public boolean contains(IntConst c) {
    if(size()!=c.size()) throw new IllegalArgumentException(this+" "+c);
    return c.sub(lower).compareTo(upper.sub(lower))<=0;
  }
/** 指定されたIntBoundとの合併を返す.
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合 */
  public IntBound union(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // このIntBoundの下界で正規化
    IntConst u0=upper.sub(lower);
    IntConst l1=b.lower.sub(lower);
    IntConst u1=b.upper.sub(lower);
    if(l1.compareTo(u1)<=0) { // l0=0,l1,u1の順
      if(u1.compareTo(u0)<=0) return this;
      if(l1.compareTo(u0)<=0) return new IntBound(lower,b.upper);
      // l0=0,u0,l1,u1の順のときは、u1-l0=u1とu0-l1を比べる
      if(u1.compareTo(u0.sub(l1))<=0) return new IntBound(lower,b.upper);
      else return new IntBound(b.lower,upper);
    } else { // l0=0,u1,l1の順
      if(u1.compareTo(u0)>=0) return b;
      if(l1.compareTo(u0)>0) return new IntBound(b.lower,upper);
      // l0=0,u1,l1,u0のときは、全域にわたる
      IntConst l=IntConst.valueOf(size(),0);
      return new IntBound(l,l.bnot());
    }
  }
/** 指定されたIntBoundとの共通部分を返す.
 * 共通部分が空のときはnullを返す。
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合 */
  public IntBound intersection(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // このIntBoundの下界で正規化
    IntConst u0=upper.sub(lower);
    IntConst l1=b.lower.sub(lower);
    IntConst u1=b.upper.sub(lower);
    if(l1.compareTo(u1)<=0) { // l0=0,l1,u1の順
      if(u1.compareTo(u0)<=0) return b;
      if(l1.compareTo(u0)<=0) return new IntBound(b.lower,upper);
      // l0=0,u0,l1,u1の順のときは、空集合
      return null;
    } else { // l0=0,u1,l1の順
      if(u1.compareTo(u0)>=0) return this;
      if(l1.compareTo(u0)>0) return new IntBound(lower,b.upper);
      // l0=0,u1,l1,u0の順のときは、u0-l0=u0とu1-l1を比べる
      if(u0.compareTo(u1.sub(l1))<=0) return this;
      else return b;
    }
  }
/** this+bのIntBoundを返す.
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合 */
  public IntBound add(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    IntConst r0=upper.sub(lower); // thisの上下界の差
    IntConst r1=b.upper.sub(b.lower); // bの上下界の差
    IntConst l,u;
    // r0とr1の和がラップアラウンドしたら、this+bは全域にわたる
    if(r0.add(r1).compareTo(r0)<0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    // そうでなければ、普通に計算
    else { l=lower.add(b.lower); u=upper.add(b.upper); }
    return new IntBound(l,u);
  }
/** this-bのIntBoundを返す.
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合 */
  public IntBound sub(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    IntConst r0=upper.sub(lower); // thisの上下界の差
    IntConst r1=b.upper.sub(b.lower); // bの上下界の差
    IntConst l,u;
    // r0とr1の和がラップアラウンドしたら、this-bは全域にわたる
    if(r0.add(r1).compareTo(r0)<0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    // そうでなければ、普通に計算
    else { l=lower.sub(b.upper); u=upper.sub(b.lower); }
    return new IntBound(l,u);
  }
/** this*bのIntBoundを返す.
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合 */
  public IntBound mul(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    final IntConst zero=IntConst.valueOf(size(),0);
    final IntConst msb=IntConst.valueOf(size(),1).lsh(size()-1);
    final IntBound plus=new IntBound(zero,msb.bnot()); // 0または正の範囲
    final IntBound minus=new IntBound(msb,zero.bnot()); // 負の範囲
    IntBound result=null;
    IntBound b0,b1,b2;
    // 乗算はオペランドの絶対値が大きい部分では有益な結果が出ないので、符号つきとして考え、
    // オペランドの符号で場合分けしたものの合併を求める。
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
  // 符号が一定の場合の、積の上下界を返す
  private static IntBound mul1(IntBound b0,IntBound b1) {
    //assert b0.size()==b1.size();
    final int s=b0.size(),s2=s*2;
    final IntConst m=IntConst.valueOf(s2,1).lsh(s);
    IntConst l,u;
    // 倍長で計算して、積の範囲がラップアラウンドしていたら、全域にわたる
    if(b0.upper.convzx(s2).mul(b1.upper.convzx(s2))
      .sub(b0.lower.convzx(s2).mul(b1.lower.convzx(s2)))
      .compareTo(m)>=0) { l=IntConst.valueOf(s,0); u=l.bnot(); }
    // そうでなければ、普通に計算
    else { l=b0.lower.mul(b1.lower); u=b0.upper.mul(b1.upper); }
    return new IntBound(l,u);
  }
/** this/b(符号なし除算)のIntBoundを返す.
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合
 * @exception ArithmeticException bが0のみの場合 */
  public IntBound divu(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    IntConst l0=lower,u0=upper;
    IntConst l1=b.lower,u1=b.upper;
    // l0>u0,l1>u1のときは全域にわたるとする
    if(l0.compareTo(u0)>0) { l0=IntConst.valueOf(size(),0); u0=l0.bnot(); }
    if(l1.compareTo(u1)>0) { l1=IntConst.valueOf(size(),0); u1=l1.bnot(); }
    // l1が0のときは1にする
    if(l1.signum()==0) l1=IntConst.valueOf(size(),1);
    // 普通に計算(u1が0のときはArithmeticException)
    return new IntBound(l0.divu(u1),u0.divu(l1));
  }
/** this/b(符号つき除算)のIntBoundを返す.
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合
 * @exception ArithmeticException bが0のみの場合 */
  public IntBound divs(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    if(b.lower.signum()==0 && b.upper.signum()==0) throw new ArithmeticException(this+" "+b);
    final IntConst zero=IntConst.valueOf(size(),0);
    final IntConst msb=IntConst.valueOf(size(),1).lsh(size()-1);
    final IntBound plus=new IntBound(zero,msb.bnot()); // 0または正の範囲
    final IntBound minus=new IntBound(msb,zero.bnot()); // 負の範囲
    IntBound result=null;
    IntBound b0,b1,b2;
    // オペランドの符号で場合分けしたものの合併を求める。
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
/** this%b(符号なし剰余)のIntBoundを返す.
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合
 * @exception ArithmeticException bが0のみの場合 */
  public IntBound modu(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // bが単一の値で、商が一定のときは、普通に計算
    // そうでなく、b.lower<=b.upperなら、0以上b.upper未満
    // これら以外なら、全範囲にわたる
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
/** this%b(符号つき剰余)のIntBoundを返す.
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合
 * @exception ArithmeticException bが0のみの場合 */
  public IntBound mods(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    if(b.lower.signum()==0 && b.upper.signum()==0) throw new ArithmeticException(this+" "+b);
    final IntConst zero=IntConst.valueOf(size(),0);
    final IntConst msb=IntConst.valueOf(size(),1).lsh(size()-1);
    final IntBound plus=new IntBound(zero,msb.bnot()); // 0または正の範囲
    final IntBound minus=new IntBound(msb,zero.bnot()); // 負の範囲
    IntBound result=null;
    IntBound b0,b1,b2;
    // オペランドの符号で場合分けしたものの合併を求める。
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
/** this&amp;bのIntBoundを返す.
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合 */
  public IntBound band(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // 0をまたいでいるときは分割して、それぞれの上下界の合併を求める
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
// a<=x<=b, c<=y<=dのときの、x&yのIntBoundを返す
  private static IntBound minMaxAnd(IntConst a,IntConst b,IntConst c,IntConst d) {
    return new IntBound(minAnd(a,b,c,d),maxAnd(a,b,c,d));
  }
// a<=x<=b, c<=y<=dのときの、x&yの最小値を返す
// 参照: Hacker's Delight 4-3
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
// a<=x<=b, c<=y<=dのときの、x&yの最大値を返す
// 参照: Hacker's Delight 4-3
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
/** this|bのIntBoundを返す.
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合 */
  public IntBound bor(IntBound b) {
    return this.bnot().band(b.bnot()).bnot();
  }
/** this^bのIntBoundを返す.
 * @exception IllegalArgumentException このIntBoundと指定されたIntBoundのビット数が異なる場合 */
  public IntBound bxor(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // 0をまたいでいるときは分割して、それぞれの上下界の合併を求める
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
// a<=x<=b, c<=y<=dのときの、x^yのIntBoundを返す
  private static IntBound minMaxXor(IntConst a,IntConst b,IntConst c,IntConst d) {
    return new IntBound(minXor(a,b,c,d),maxXor(a,b,c,d));
  }
// a<=x<=b, c<=y<=dのときの、x^yの下界の一つを返す
// 参照: Hacker's Delight 4-3
  private static IntConst minXor(IntConst a,IntConst b,IntConst c,IntConst d) {
    return minAnd(a,b,d.bnot(),c.bnot()).bor(minAnd(b.bnot(),a.bnot(),c,d));
  }
// a<=x<=b, c<=y<=dのときの、x^yの上界の一つを返す
// 参照: Hacker's Delight 4-3
  private static IntConst maxXor(IntConst a,IntConst b,IntConst c,IntConst d) {
    final IntConst m=IntConst.valueOf(a.size(),0).bnot();
    return minAnd(maxAnd(a,b,d.bnot(),c.bnot()).bnot(),m,
                  maxAnd(b.bnot(),a.bnot(),c,d).bnot(),m).bnot();
  }
/** this&lt;&lt;bのIntBoundを返す. */
  public IntBound lsh(IntBound b) {
    b=saturate(b,size());
    IntBound result=null;
    // 0からビット数までの各シフトカウントで範囲を求め、合併をとる
    for(int i=0;i<=size();i++) if(b.contains(IntConst.valueOf(b.size(),i))) {
      IntBound b1=this.lsh(i);
      result=result==null ? b1 : result.union(b1);
    }
    return result;
  }
/** this&gt;&gt;b(符号なし右シフト)のIntBoundを返す. */
  public IntBound rshu(IntBound b) {
    b=saturate(b,size());
    IntBound result=null;
    // 0からビット数までの各シフトカウントで範囲を求め、合併をとる
    for(int i=0;i<=size();i++) if(b.contains(IntConst.valueOf(b.size(),i))) {
      IntBound b1=this.rshu(i);
      result=result==null ? b1 : result.union(b1);
    }
    return result;
  }
/** this&gt;&gt;b(符号つき右シフト)のIntBoundを返す. */
  public IntBound rshs(IntBound b) {
    b=saturate(b,size());
    IntBound result=null;
    // 0からビット数までの各シフトカウントで範囲を求め、合併をとる
    for(int i=0;i<=size();i++) if(b.contains(IntConst.valueOf(b.size(),i))) {
      IntBound b1=this.rshs(i);
      result=result==null ? b1 : result.union(b1);
    }
    return result;
  }
// 指定されたIntBoundを0からiまでに飽和させて返す
  private static IntBound saturate(IntBound b,int i) {
    IntConst i1=IntConst.valueOf(b.size(),i);
    IntConst l=b.lower,u=b.upper;
    int cmp=l.compareTo(u);
    // 上下界をiまでに飽和させる
    if(l.compareTo(i1)>=0) l=i1;
    if(u.compareTo(i1)>=0) {
      u=i1;
      if(cmp>0) l=IntConst.valueOf(b.size(),0); // 飽和前にi,u,lの順だった場合、下界を0に
    }
    return new IntBound(l,u);
  }
/** this&lt;&lt;nのIntBoundを返す.
 * @exception IllegalArgumentException nが負の場合 */
  public IntBound lsh(int n) {
    if(n<0) throw new IllegalArgumentException(String.valueOf(n));
    if(n==0) return this;
    if(n>=size()) return new IntBound(IntConst.valueOf(size(),0));
    IntConst r=upper.sub(lower);
    IntConst l,u;
    // r(thisの上下界の差)がシフト後にはみ出すなら、全範囲にわたる
    // そうでなければ、普通に計算
    if(r.compareTo(IntConst.valueOf(size(),1).lsh(size()-n))>=0) {
      l=IntConst.valueOf(size(),0); u=l.bnot();
    } else {
      l=lower.lsh(n); u=upper.lsh(n);
    }
    return new IntBound(l,u);
  }
/** this&gt;&gt;n(符号なし右シフト)のIntBoundを返す.
 * @exception IllegalArgumentException nが負の場合 */
  public IntBound rshu(int n) {
    if(n<0) throw new IllegalArgumentException(String.valueOf(n));
    if(n==0) return this;
    IntConst l=lower,u=upper;
    // 上下界をそれぞれシフトするだけだと、下界が上界より(符号なしで)大きいとき、
    // (1)上位ビットが0でない値を全部含んでしまう。
    // (2)上界が偶数で下界が上界より1大きいときなどで、上下界が同じになる。
    // という問題があるので、その場合は0と~0をシフトしたものを使う。
    if(l.compareTo(u)>0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    // そうでなければ、普通に計算
    return new IntBound(l.rshu(n),u.rshu(n));
  }
/** this&gt;&gt;n(符号つき右シフト)のIntBoundを返す.
 * @exception IllegalArgumentException nが負の場合 */
  public IntBound rshs(int n) {
    if(n<0) throw new IllegalArgumentException(String.valueOf(n));
    if(n==0) return this;
    IntConst l=lower,u=upper;
    // 上下界をそれぞれシフトするだけだと、下界が上界より(符号つきで)大きいとき、
    // (1)上位ビットが符号拡張でない値を全部含んでしまう。
    // (2)上界が偶数で下界が上界より1大きいときなどで、上下界が同じになる。
    // という問題があるので、その場合は符号つきの最小と最大をシフトしたものを使う。
    if(l.signedCompareTo(u)>0) { l=IntConst.valueOf(size(),1).lsh(size()-1); u=l.bnot(); }
    // そうでなければ、普通に計算
    return new IntBound(l.rshs(n),u.rshs(n));
  }
/** -thisのIntBoundを返す. */
  public IntBound neg() {
    return new IntBound(upper.neg(),lower.neg());
  }
/** ~thisのIntBoundを返す. */
  public IntBound bnot() {
    return new IntBound(upper.bnot(),lower.bnot());
  }
/** this==bのIntBoundを返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public IntBound tsteq(IntBound b,int s) { return cmpeq(this,b,s,1); }
/** this!=bのIntBoundを返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public IntBound tstne(IntBound b,int s) { return cmpeq(this,b,s,0); }
/** this&lt;b(符号なし比較)のIntBoundを返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public IntBound tstltu(IntBound b,int s) { return cmpu(this,b,s,1); }
/** this&gt;b(符号なし比較)のIntBoundを返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public IntBound tstgtu(IntBound b,int s) { return cmpu(b,this,s,1); }
/** this&lt;=b(符号なし比較)のIntBoundを返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public IntBound tstleu(IntBound b,int s) { return cmpu(b,this,s,0); }
/** this&gt;=b(符号なし比較)のIntBoundを返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public IntBound tstgeu(IntBound b,int s) { return cmpu(this,b,s,0); }
/** this&lt;b(符号つき比較)のIntBoundを返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public IntBound tstlts(IntBound b,int s) { return cmps(this,b,s,1); }
/** this&gt;b(符号つき比較)のIntBoundを返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public IntBound tstgts(IntBound b,int s) { return cmps(b,this,s,1); }
/** this&lt;=b(符号つき比較)のIntBoundを返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public IntBound tstles(IntBound b,int s) { return cmps(b,this,s,0); }
/** this&gt;=b(符号つき比較)のIntBoundを返す.
 * @exception IllegalArgumentException この整数定数と指定された整数定数のビット数が異なる場合 */
  public IntBound tstges(IntBound b,int s) { return cmps(this,b,s,0); }
// b0とb1が必ず等しいならsビットのeqを、必ず異なるならsビットの1-eqを、
// それ以外ならsビットの0と1を含むIntBoundを返す
  private static IntBound cmpeq(IntBound b0,IntBound b1,int s,int eq) {
    if(b0.size()!=b1.size()) throw new IllegalArgumentException(b0+" "+b1);
    if(b0.lower.equals(b0.upper) && b1.lower.equals(b1.upper) &&
       b0.lower.equals(b1.lower)) return new IntBound(IntConst.valueOf(s,eq));
    if(b0.intersection(b1)==null) return new IntBound(IntConst.valueOf(s,1-eq));
    return new IntBound(IntConst.valueOf(s,0),IntConst.valueOf(s,1));
  }
// b0がb1より必ず小さければsビットのltを、b0が必ずb1以上ならsビットの1-ltを、
// それ以外ならsビットの0と1を含むIntBoundを返す(符号なし比較)
  private static IntBound cmpu(IntBound b0,IntBound b1,int s,int lt) {
    if(b0.size()!=b1.size()) throw new IllegalArgumentException(b0+" "+b1);
    if(b0.lower.compareTo(b0.upper)<=0 && b1.lower.compareTo(b1.upper)<=0) {
      if(b0.upper.compareTo(b1.lower)<0) return new IntBound(IntConst.valueOf(s,lt));
      if(b0.lower.compareTo(b1.upper)>=0) return new IntBound(IntConst.valueOf(s,1-lt));
    }
    return new IntBound(IntConst.valueOf(s,0),IntConst.valueOf(s,1));
  }
// b0がb1より必ず小さければsビットのltを、b0が必ずb1以上ならsビットの1-ltを、
// それ以外ならsビットの0と1を含むIntBoundを返す(符号つき比較)
  private static IntBound cmps(IntBound b0,IntBound b1,int s,int lt) {
    if(b0.size()!=b1.size()) throw new IllegalArgumentException(b0+" "+b1);
    if(b0.lower.signedCompareTo(b0.upper)<=0 && b1.lower.signedCompareTo(b1.upper)<=0) {
      if(b0.upper.signedCompareTo(b1.lower)<0) return new IntBound(IntConst.valueOf(s,lt));
      if(b0.lower.signedCompareTo(b1.upper)>=0) return new IntBound(IntConst.valueOf(s,1-lt));
    }
    return new IntBound(IntConst.valueOf(s,0),IntConst.valueOf(s,1));
  }
/** このIntBoundを指定されたビット数にゼロ拡張したときのIntBoundを返す.
 * @exception IllegalArgumentException 指定されたビット数のほうが小さい場合 */
  public IntBound convzx(int s) {
    if(s==size()) return this;
    if(s<size()) throw new IllegalArgumentException(this+" "+s);
    IntConst l=lower,u=upper;
    // 上下界をそれぞれゼロ拡張するだけでもよいが、
    // 下界が上界より(符号なしで)大きいときは、上位ビットが0でない値を全部含んでしまう。
    // この場合は、元のビット数の0と~0のゼロ拡張のほうがよい。
    if(l.compareTo(u)>0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    return new IntBound(l.convzx(s),u.convzx(s));
  }
/** このIntBoundを指定されたビット数に符号拡張したときのIntBoundを返す.
 * @exception IllegalArgumentException 指定されたビット数のほうが小さい場合 */
  public IntBound convsx(int s) {
    if(s==size()) return this;
    if(s<size()) throw new IllegalArgumentException(this+" "+s);
    IntConst l=lower,u=upper;
    // 上下界をそれぞれ符号拡張するだけでもよいが、
    // 下界が上界より(符号つきで)大きいときは、上位ビットが符号拡張でない値を全部含んでしまう。
    // この場合は、元のビット数の符号つきの最小と最大の符号拡張のほうがよい。
    if(l.signedCompareTo(u)>0) { l=IntConst.valueOf(size(),1).lsh(size()-1); u=l.bnot(); }
    return new IntBound(l.convsx(s),u.convsx(s));
  }
/** このIntBoundを指定されたビット数に縮小したときのIntBoundを返す.
 * @exception IllegalArgumentException 指定されたビット数のほうが大きい、または正でない場合 */
  public IntBound convit(int s) {
    if(s==size()) return this;
    if(s>size() || s<=0) throw new IllegalArgumentException(this+" "+s);
    IntConst r=upper.sub(lower);
    IntConst l,u;
    // r(thisの上下界の差)が縮小ではみ出すなら、全範囲にわたる
    // そうでなければ普通に計算
    if(r.compareTo(IntConst.valueOf(size(),1).lsh(s))>=0) {
      l=IntConst.valueOf(s,0); u=l.bnot();
    } else {
      l=lower.convit(s); u=upper.convit(s);
    }
    return new IntBound(l,u);
  }
/** 指定された整数定数tとfのうち、この整数定数が0以外ならt、0ならfを値とするときのIntBoundを返す.
 * @exception IllegalArgumentException tとfのビット数が異なる場合 */
  public IntBound ifthenelse(IntBound t,IntBound f) {
    if(t.size()!=f.size()) throw new IllegalArgumentException(this+" "+t+' '+f);
    if(lower.signum()==0 && lower.equals(upper)) return t;
    if(!this.contains(IntConst.valueOf(size(),0))) return f;
    return t.union(f);
  }
/** このIntBoundと指定されたオブジェクトが等しいかどうかを返す. */
  public boolean equals(Object o) {
    return o==this ||
           o instanceof IntBound &&
           lower.equals(((IntBound)o).lower) && upper.equals(((IntBound)o).upper);
  }
/** このIntBoundのハッシュコードを返す. */
  public int hashCode() {
    return lower.hashCode()*37+upper.hashCode();
  }
/** このIntBoundの文字列表現を返す. */
  public String toString() {
    return "("+lower.bigValue()+".."+upper.bigValue()+"):"+size();
  }

}
