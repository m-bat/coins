/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.util;

/**
 * ............(..............).
 * ..............2............................................
 * ..........................................3,4,7..................3....7............
 * ............................................
 * ......................................................&gt;..........................
 * ............&lt;=..............&lt;=..&lt;=......................
 * ....&gt;..............&lt;=..........&lt;=......................
 * ..................................................................
 */
public final class IntBound {

// field

/** .... */
  public final IntConst lower;
/** .... */
  public final IntConst upper;

// constructor

/** ..................IntBound........... */
  public IntBound(IntConst val) {
    this.lower=val; this.upper=val;
  }
/** ......................IntBound...........
 * @exception IllegalArgumentException ................................ */
  public IntBound(IntConst lower,IntConst upper) {
    if(lower.size()!=upper.size()) throw new IllegalArgumentException(lower+" "+upper);
    this.lower=lower; this.upper=upper;
  }

// method

/** ....IntBound................. */
  public int size() {
    return lower.size();
  }
/** ........................IntBound...........................
 * @exception IllegalArgumentException ....IntBound.......................................... */
  public boolean contains(IntConst c) {
    if(size()!=c.size()) throw new IllegalArgumentException(this+" "+c);
    return c.sub(lower).compareTo(upper.sub(lower))<=0;
  }
/** ..........IntBound...............
 * @exception IllegalArgumentException ....IntBound............IntBound...................... */
  public IntBound union(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // ....IntBound..............
    IntConst u0=upper.sub(lower);
    IntConst l1=b.lower.sub(lower);
    IntConst u1=b.upper.sub(lower);
    if(l1.compareTo(u1)<=0) { // l0=0,l1,u1....
      if(u1.compareTo(u0)<=0) return this;
      if(l1.compareTo(u0)<=0) return new IntBound(lower,b.upper);
      // l0=0,u0,l1,u1..............u1-l0=u1..u0-l1........
      if(u1.compareTo(u0.sub(l1))<=0) return new IntBound(lower,b.upper);
      else return new IntBound(b.lower,upper);
    } else { // l0=0,u1,l1....
      if(u1.compareTo(u0)>=0) return b;
      if(l1.compareTo(u0)>0) return new IntBound(b.lower,upper);
      // l0=0,u1,l1,u0......................
      IntConst l=IntConst.valueOf(size(),0);
      return new IntBound(l,l.bnot());
    }
  }
/** ..........IntBound...................
 * ....................null........
 * @exception IllegalArgumentException ....IntBound............IntBound...................... */
  public IntBound intersection(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // ....IntBound..............
    IntConst u0=upper.sub(lower);
    IntConst l1=b.lower.sub(lower);
    IntConst u1=b.upper.sub(lower);
    if(l1.compareTo(u1)<=0) { // l0=0,l1,u1....
      if(u1.compareTo(u0)<=0) return b;
      if(l1.compareTo(u0)<=0) return new IntBound(b.lower,upper);
      // l0=0,u0,l1,u1....................
      return null;
    } else { // l0=0,u1,l1....
      if(u1.compareTo(u0)>=0) return this;
      if(l1.compareTo(u0)>0) return new IntBound(lower,b.upper);
      // l0=0,u1,l1,u0..............u0-l0=u0..u1-l1........
      if(u0.compareTo(u1.sub(l1))<=0) return this;
      else return b;
    }
  }
/** this+b..IntBound.......
 * @exception IllegalArgumentException ....IntBound............IntBound...................... */
  public IntBound add(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    IntConst r0=upper.sub(lower); // this............
    IntConst r1=b.upper.sub(b.lower); // b............
    IntConst l,u;
    // r0..r1..............................this+b..............
    if(r0.add(r1).compareTo(r0)<0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    // ..........................
    else { l=lower.add(b.lower); u=upper.add(b.upper); }
    return new IntBound(l,u);
  }
/** this-b..IntBound.......
 * @exception IllegalArgumentException ....IntBound............IntBound...................... */
  public IntBound sub(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    IntConst r0=upper.sub(lower); // this............
    IntConst r1=b.upper.sub(b.lower); // b............
    IntConst l,u;
    // r0..r1..............................this-b..............
    if(r0.add(r1).compareTo(r0)<0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    // ..........................
    else { l=lower.sub(b.upper); u=upper.sub(b.lower); }
    return new IntBound(l,u);
  }
/** this*b..IntBound.......
 * @exception IllegalArgumentException ....IntBound............IntBound...................... */
  public IntBound mul(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    final IntConst zero=IntConst.valueOf(size(),0);
    final IntConst msb=IntConst.valueOf(size(),1).lsh(size()-1);
    final IntBound plus=new IntBound(zero,msb.bnot()); // 0..............
    final IntBound minus=new IntBound(msb,zero.bnot()); // ........
    IntBound result=null;
    IntBound b0,b1,b2;
    // ....................................................................................
    // ..................................................
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
  // ....................................
  private static IntBound mul1(IntBound b0,IntBound b1) {
    //assert b0.size()==b1.size();
    final int s=b0.size(),s2=s*2;
    final IntConst m=IntConst.valueOf(s2,1).lsh(s);
    IntConst l,u;
    // ..................................................................
    if(b0.upper.convzx(s2).mul(b1.upper.convzx(s2))
      .sub(b0.lower.convzx(s2).mul(b1.lower.convzx(s2)))
      .compareTo(m)>=0) { l=IntConst.valueOf(s,0); u=l.bnot(); }
    // ..........................
    else { l=b0.lower.mul(b1.lower); u=b0.upper.mul(b1.upper); }
    return new IntBound(l,u);
  }
/** this/b(............)..IntBound.......
 * @exception IllegalArgumentException ....IntBound............IntBound......................
 * @exception ArithmeticException b..0.......... */
  public IntBound divu(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    IntConst l0=lower,u0=upper;
    IntConst l1=b.lower,u1=b.upper;
    // l0>u0,l1>u1..........................
    if(l0.compareTo(u0)>0) { l0=IntConst.valueOf(size(),0); u0=l0.bnot(); }
    if(l1.compareTo(u1)>0) { l1=IntConst.valueOf(size(),0); u1=l1.bnot(); }
    // l1..0........1......
    if(l1.signum()==0) l1=IntConst.valueOf(size(),1);
    // ..........(u1..0........ArithmeticException)
    return new IntBound(l0.divu(u1),u0.divu(l1));
  }
/** this/b(............)..IntBound.......
 * @exception IllegalArgumentException ....IntBound............IntBound......................
 * @exception ArithmeticException b..0.......... */
  public IntBound divs(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    if(b.lower.signum()==0 && b.upper.signum()==0) throw new ArithmeticException(this+" "+b);
    final IntConst zero=IntConst.valueOf(size(),0);
    final IntConst msb=IntConst.valueOf(size(),1).lsh(size()-1);
    final IntBound plus=new IntBound(zero,msb.bnot()); // 0..............
    final IntBound minus=new IntBound(msb,zero.bnot()); // ........
    IntBound result=null;
    IntBound b0,b1,b2;
    // ..................................................
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
/** this%b(............)..IntBound.......
 * @exception IllegalArgumentException ....IntBound............IntBound......................
 * @exception ArithmeticException b..0.......... */
  public IntBound modu(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // b..........................................
    // ............b.lower<=b.upper......0....b.upper....
    // ..............................
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
/** this%b(............)..IntBound.......
 * @exception IllegalArgumentException ....IntBound............IntBound......................
 * @exception ArithmeticException b..0.......... */
  public IntBound mods(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    if(b.lower.signum()==0 && b.upper.signum()==0) throw new ArithmeticException(this+" "+b);
    final IntConst zero=IntConst.valueOf(size(),0);
    final IntConst msb=IntConst.valueOf(size(),1).lsh(size()-1);
    final IntBound plus=new IntBound(zero,msb.bnot()); // 0..............
    final IntBound minus=new IntBound(msb,zero.bnot()); // ........
    IntBound result=null;
    IntBound b0,b1,b2;
    // ..................................................
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
/** this&amp;b..IntBound.......
 * @exception IllegalArgumentException ....IntBound............IntBound...................... */
  public IntBound band(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // 0............................................................
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
// a<=x<=b, c<=y<=d..........x&y..IntBound......
  private static IntBound minMaxAnd(IntConst a,IntConst b,IntConst c,IntConst d) {
    return new IntBound(minAnd(a,b,c,d),maxAnd(a,b,c,d));
  }
// a<=x<=b, c<=y<=d..........x&y..............
// ....: Hacker's Delight 4-3
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
// a<=x<=b, c<=y<=d..........x&y..............
// ....: Hacker's Delight 4-3
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
/** this|b..IntBound.......
 * @exception IllegalArgumentException ....IntBound............IntBound...................... */
  public IntBound bor(IntBound b) {
    return this.bnot().band(b.bnot()).bnot();
  }
/** this^b..IntBound.......
 * @exception IllegalArgumentException ....IntBound............IntBound...................... */
  public IntBound bxor(IntBound b) {
    if(size()!=b.size()) throw new IllegalArgumentException(this+" "+b);
    // 0............................................................
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
// a<=x<=b, c<=y<=d..........x^y..IntBound......
  private static IntBound minMaxXor(IntConst a,IntConst b,IntConst c,IntConst d) {
    return new IntBound(minXor(a,b,c,d),maxXor(a,b,c,d));
  }
// a<=x<=b, c<=y<=d..........x^y..................
// ....: Hacker's Delight 4-3
  private static IntConst minXor(IntConst a,IntConst b,IntConst c,IntConst d) {
    return minAnd(a,b,d.bnot(),c.bnot()).bor(minAnd(b.bnot(),a.bnot(),c,d));
  }
// a<=x<=b, c<=y<=d..........x^y..................
// ....: Hacker's Delight 4-3
  private static IntConst maxXor(IntConst a,IntConst b,IntConst c,IntConst d) {
    final IntConst m=IntConst.valueOf(a.size(),0).bnot();
    return minAnd(maxAnd(a,b,d.bnot(),c.bnot()).bnot(),m,
                  maxAnd(b.bnot(),a.bnot(),c,d).bnot(),m).bnot();
  }
/** this&lt;&lt;b..IntBound....... */
  public IntBound lsh(IntBound b) {
    b=saturate(b,size());
    IntBound result=null;
    // 0..........................................................
    for(int i=0;i<=size();i++) if(b.contains(IntConst.valueOf(b.size(),i))) {
      IntBound b1=this.lsh(i);
      result=result==null ? b1 : result.union(b1);
    }
    return result;
  }
/** this&gt;&gt;b(................)..IntBound....... */
  public IntBound rshu(IntBound b) {
    b=saturate(b,size());
    IntBound result=null;
    // 0..........................................................
    for(int i=0;i<=size();i++) if(b.contains(IntConst.valueOf(b.size(),i))) {
      IntBound b1=this.rshu(i);
      result=result==null ? b1 : result.union(b1);
    }
    return result;
  }
/** this&gt;&gt;b(................)..IntBound....... */
  public IntBound rshs(IntBound b) {
    b=saturate(b,size());
    IntBound result=null;
    // 0..........................................................
    for(int i=0;i<=size();i++) if(b.contains(IntConst.valueOf(b.size(),i))) {
      IntBound b1=this.rshs(i);
      result=result==null ? b1 : result.union(b1);
    }
    return result;
  }
// ..........IntBound..0....i....................
  private static IntBound saturate(IntBound b,int i) {
    IntConst i1=IntConst.valueOf(b.size(),i);
    IntConst l=b.lower,u=b.upper;
    int cmp=l.compareTo(u);
    // ........i................
    if(l.compareTo(i1)>=0) l=i1;
    if(u.compareTo(i1)>=0) {
      u=i1;
      if(cmp>0) l=IntConst.valueOf(b.size(),0); // ........i,u,l......................0..
    }
    return new IntBound(l,u);
  }
/** this&lt;&lt;n..IntBound.......
 * @exception IllegalArgumentException n.......... */
  public IntBound lsh(int n) {
    if(n<0) throw new IllegalArgumentException(String.valueOf(n));
    if(n==0) return this;
    if(n>=size()) return new IntBound(IntConst.valueOf(size(),0));
    IntConst r=upper.sub(lower);
    IntConst l,u;
    // r(this............)........................................
    // ..........................
    if(r.compareTo(IntConst.valueOf(size(),1).lsh(size()-n))>=0) {
      l=IntConst.valueOf(size(),0); u=l.bnot();
    } else {
      l=lower.lsh(n); u=upper.lsh(n);
    }
    return new IntBound(l,u);
  }
/** this&gt;&gt;n(................)..IntBound.......
 * @exception IllegalArgumentException n.......... */
  public IntBound rshu(int n) {
    if(n<0) throw new IllegalArgumentException(String.valueOf(n));
    if(n==0) return this;
    IntConst l=lower,u=upper;
    // ..................................................(..........)............
    // (1)............0............................
    // (2)..........................1......................................
    // ................................0..~0........................
    if(l.compareTo(u)>0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    // ..........................
    return new IntBound(l.rshu(n),u.rshu(n));
  }
/** this&gt;&gt;n(................)..IntBound.......
 * @exception IllegalArgumentException n.......... */
  public IntBound rshs(int n) {
    if(n<0) throw new IllegalArgumentException(String.valueOf(n));
    if(n==0) return this;
    IntConst l=lower,u=upper;
    // ..................................................(..........)............
    // (1)................................................
    // (2)..........................1......................................
    // ............................................................................
    if(l.signedCompareTo(u)>0) { l=IntConst.valueOf(size(),1).lsh(size()-1); u=l.bnot(); }
    // ..........................
    return new IntBound(l.rshs(n),u.rshs(n));
  }
/** -this..IntBound....... */
  public IntBound neg() {
    return new IntBound(upper.neg(),lower.neg());
  }
/** ~this..IntBound....... */
  public IntBound bnot() {
    return new IntBound(upper.bnot(),lower.bnot());
  }
/** this==b..IntBound.......
 * @exception IllegalArgumentException ...................................................... */
  public IntBound tsteq(IntBound b,int s) { return cmpeq(this,b,s,1); }
/** this!=b..IntBound.......
 * @exception IllegalArgumentException ...................................................... */
  public IntBound tstne(IntBound b,int s) { return cmpeq(this,b,s,0); }
/** this&lt;b(............)..IntBound.......
 * @exception IllegalArgumentException ...................................................... */
  public IntBound tstltu(IntBound b,int s) { return cmpu(this,b,s,1); }
/** this&gt;b(............)..IntBound.......
 * @exception IllegalArgumentException ...................................................... */
  public IntBound tstgtu(IntBound b,int s) { return cmpu(b,this,s,1); }
/** this&lt;=b(............)..IntBound.......
 * @exception IllegalArgumentException ...................................................... */
  public IntBound tstleu(IntBound b,int s) { return cmpu(b,this,s,0); }
/** this&gt;=b(............)..IntBound.......
 * @exception IllegalArgumentException ...................................................... */
  public IntBound tstgeu(IntBound b,int s) { return cmpu(this,b,s,0); }
/** this&lt;b(............)..IntBound.......
 * @exception IllegalArgumentException ...................................................... */
  public IntBound tstlts(IntBound b,int s) { return cmps(this,b,s,1); }
/** this&gt;b(............)..IntBound.......
 * @exception IllegalArgumentException ...................................................... */
  public IntBound tstgts(IntBound b,int s) { return cmps(b,this,s,1); }
/** this&lt;=b(............)..IntBound.......
 * @exception IllegalArgumentException ...................................................... */
  public IntBound tstles(IntBound b,int s) { return cmps(b,this,s,0); }
/** this&gt;=b(............)..IntBound.......
 * @exception IllegalArgumentException ...................................................... */
  public IntBound tstges(IntBound b,int s) { return cmps(this,b,s,0); }
// b0..b1................s........eq..................s........1-eq....
// ............s........0..1......IntBound......
  private static IntBound cmpeq(IntBound b0,IntBound b1,int s,int eq) {
    if(b0.size()!=b1.size()) throw new IllegalArgumentException(b0+" "+b1);
    if(b0.lower.equals(b0.upper) && b1.lower.equals(b1.upper) &&
       b0.lower.equals(b1.lower)) return new IntBound(IntConst.valueOf(s,eq));
    if(b0.intersection(b1)==null) return new IntBound(IntConst.valueOf(s,1-eq));
    return new IntBound(IntConst.valueOf(s,0),IntConst.valueOf(s,1));
  }
// b0..b1..................s........lt....b0......b1........s........1-lt....
// ............s........0..1......IntBound......(............)
  private static IntBound cmpu(IntBound b0,IntBound b1,int s,int lt) {
    if(b0.size()!=b1.size()) throw new IllegalArgumentException(b0+" "+b1);
    if(b0.lower.compareTo(b0.upper)<=0 && b1.lower.compareTo(b1.upper)<=0) {
      if(b0.upper.compareTo(b1.lower)<0) return new IntBound(IntConst.valueOf(s,lt));
      if(b0.lower.compareTo(b1.upper)>=0) return new IntBound(IntConst.valueOf(s,1-lt));
    }
    return new IntBound(IntConst.valueOf(s,0),IntConst.valueOf(s,1));
  }
// b0..b1..................s........lt....b0......b1........s........1-lt....
// ............s........0..1......IntBound......(............)
  private static IntBound cmps(IntBound b0,IntBound b1,int s,int lt) {
    if(b0.size()!=b1.size()) throw new IllegalArgumentException(b0+" "+b1);
    if(b0.lower.signedCompareTo(b0.upper)<=0 && b1.lower.signedCompareTo(b1.upper)<=0) {
      if(b0.upper.signedCompareTo(b1.lower)<0) return new IntBound(IntConst.valueOf(s,lt));
      if(b0.lower.signedCompareTo(b1.upper)>=0) return new IntBound(IntConst.valueOf(s,1-lt));
    }
    return new IntBound(IntConst.valueOf(s,0),IntConst.valueOf(s,1));
  }
/** ....IntBound........................................IntBound.......
 * @exception IllegalArgumentException .................................... */
  public IntBound convzx(int s) {
    if(s==size()) return this;
    if(s<size()) throw new IllegalArgumentException(this+" "+s);
    IntConst l=lower,u=upper;
    // ............................................
    // ..............(..........)..........................0............................
    // ..........................0..~0........................
    if(l.compareTo(u)>0) { l=IntConst.valueOf(size(),0); u=l.bnot(); }
    return new IntBound(l.convzx(s),u.convzx(s));
  }
/** ....IntBound........................................IntBound.......
 * @exception IllegalArgumentException .................................... */
  public IntBound convsx(int s) {
    if(s==size()) return this;
    if(s<size()) throw new IllegalArgumentException(this+" "+s);
    IntConst l=lower,u=upper;
    // ............................................
    // ..............(..........)..............................................................
    // ......................................................................
    if(l.signedCompareTo(u)>0) { l=IntConst.valueOf(size(),1).lsh(size()-1); u=l.bnot(); }
    return new IntBound(l.convsx(s),u.convsx(s));
  }
/** ....IntBound....................................IntBound.......
 * @exception IllegalArgumentException .................................................... */
  public IntBound convit(int s) {
    if(s==size()) return this;
    if(s>size() || s<=0) throw new IllegalArgumentException(this+" "+s);
    IntConst r=upper.sub(lower);
    IntConst l,u;
    // r(this............)....................................
    // ........................
    if(r.compareTo(IntConst.valueOf(size(),1).lsh(s))>=0) {
      l=IntConst.valueOf(s,0); u=l.bnot();
    } else {
      l=lower.convit(s); u=upper.convit(s);
    }
    return new IntBound(l,u);
  }
/** ..................t..f......................0........t..0....f................IntBound.......
 * @exception IllegalArgumentException t..f...................... */
  public IntBound ifthenelse(IntBound t,IntBound f) {
    if(t.size()!=f.size()) throw new IllegalArgumentException(this+" "+t+' '+f);
    if(lower.signum()==0 && lower.equals(upper)) return t;
    if(!this.contains(IntConst.valueOf(size(),0))) return f;
    return t.union(f);
  }
/** ....IntBound............................................... */
  public boolean equals(Object o) {
    return o==this ||
           o instanceof IntBound &&
           lower.equals(((IntBound)o).lower) && upper.equals(((IntBound)o).upper);
  }
/** ....IntBound....................... */
  public int hashCode() {
    return lower.hashCode()*37+upper.hashCode();
  }
/** ....IntBound................... */
  public String toString() {
    return "("+lower.bigValue()+".."+upper.bigValue()+"):"+size();
  }

}
