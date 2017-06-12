/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.util;

/**
 * ......................(..............).
 * ......................................................
 * ..................................................
 * ..................................................................................
 * ................0........................................................
 * ..................0................................................
 * ........................0............................................
 */
public final class IntLive {

// field

// ..(..................1....................0)
  private final IntConst val;

// member type

// ..............................
  private abstract static class Shift {
    abstract IntConst eval(IntConst c,int n);
  }
  private static Shift SHIFT_LSH=new Shift() {
    IntConst eval(IntConst c,int n) { return c.lsh(n); }
  };
  private static Shift SHIFT_RSHU=new Shift() {
    IntConst eval(IntConst c,int n) { return c.rshu(n); }
  };
  private static Shift SHIFT_RSHS=new Shift() {
    IntConst eval(IntConst c,int n) { return c.rshs(n); }
  };

// constructor

  private IntLive(IntConst val) { this.val=val; }

// factory method

/** ....................................IntLive.......
 * @exception IllegalArgumentException ...................... */
  public static IntLive valueOf(int size) { return new IntLive(IntConst.valueOf(size,0).bnot()); }
/** ....................................IntLive.......
 * @exception IllegalArgumentException ...................... */
  public static IntLive empty(int size) { return new IntLive(IntConst.valueOf(size,0)); }
/** ................................................IntLive....... */
  public static IntLive valueOf(IntConst val) { return new IntLive(val); }

// method

/** ..........IntLive...............
 * @exception IllegalArgumentException ....IntLive............IntLive...................... */
  public IntLive union(IntLive l) { return valueOf(val.bor(l.val)); }
/** ..........IntLive...................
 * @exception IllegalArgumentException ....IntLive............IntLive...................... */
  public IntLive intersection(IntLive l) { return valueOf(val.band(l.val)); }
/** ......................IntLive....... */
  public IntLive inheritAdd() {
    // ..........................................
    return valueOf(propagateRight(val));
  }
/** ......................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritAdd(IntConst c) {
    // c..........1................................................
    // c..........1................................................
    // ..:
    // 01000010 ....................
    // 00101000 ........
    // 01111010 ..........................
    return valueOf(propagateRight(val).band(rightmostOne(c).neg()).bor(val));
  }
/** ......................IntLive....... */
  public IntLive inheritSub0() { return inheritAdd(); }
/** ......................IntLive....... */
  public IntLive inheritSub1() { return inheritAdd(); }
/** ......................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritSub0(IntConst c1) { return inheritAdd(c1.neg()); }
/** ......................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritSub1(IntConst c0) {
    // x-y..~(x-y)..............................................
    // ~(x-y) = y-x-1 = (-x-1)+y = ~x+y
    return inheritAdd(c0.bnot());
  }
/** ......................IntLive....... */
  public IntLive inheritMul() { return inheritAdd(); }
/** ......................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritMul(IntConst c) {
    if(c.size()!=val.size()) throw new IllegalArgumentException(this+" "+c);
    if(c.signum()==0) return empty(val.size());
    IntConst a=rightmostOne(c);
    IntConst v0=val.divu(a);
    if(a.equals(c)) return valueOf(v0); // c..2......................
    // c........1........i....2......1........i+j................
    //  ......0....i-1..........0
    //  ......i....i+j-1....................0....j-1....................
    //  ......i+j+k(k>=0)....................0....k............j....j+k..................
    // ....IntLive........i+j..................................
    // ................................IntLive......i........................
    // ....IntLive........i+j..................................................i+j+K........
    // ..........................
    //  ......j....j+K....1
    //  ......0....K....1
    //  ....IntLive......i..........
    // ..................
    // ..:
    //    hgfedcba
    //  x 01010010
    // -----------
    //     fedcba
    //    dcba
    //    ba
    // -----------
    //    ****cba0
    //     |||
    //     ||a,d..............
    //     |a,b,d,e..............
    //     a,b,c,d,e,f..............(......3......1........2......1................)
    IntConst b=rightmostOne(c.sub(a).divu(a)); // c..........2......1....
    IntConst v=propagateRight(v0.divu(b));
    return valueOf(v.mul(b).bor(v).bor(v0));
  }
/** ..............................IntLive....... */
  public IntLive inheritDivu0() {
    // ..........................................
    return valueOf(rightmostOne(val).neg());
  }
/** ..............................IntLive....... */
  public IntLive inheritDivu1() { return valueOf(val.size()); }
/** ..............................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritDivu0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    IntConst a=rightmostOne(c1);
    if(a.equals(c1)) return valueOf(val.mul(a)); // c1..2...................... c1..0......
    IntConst b=IntConst.valueOf(c1.size(),0).bnot().divu(c1); // ........
    // ........0..............................
    if(val.band(propagateRight(b)).signum()==0) return empty(val.size());
    // c1..........1........i......IntLive..........................j........
    // ......0....i+j-1..........................i+j..........................
    // ..:
    //       ..............0
    //       ||a,b..............(c............)
    //       |||
    //       |||   a,b,c,d,e,f..............
    //       |||   |a,b,c,d,e,f,g..............
    //       |||   ||
    //     __________ h................
    // 110 ) abcdefgh
    return valueOf(a.mul(rightmostOne(val)).neg());
  }
/** ..............................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritDivu1(IntConst c0) {
    if(c0.size()!=val.size()) throw new IllegalArgumentException(this+" "+c0);
    return valueOf(val.size());
  }
/** ..............................IntLive....... */
  public IntLive inheritDivs0() { return allOrEmpty(val,val.size()); }
/** ..............................IntLive....... */
  public IntLive inheritDivs1() { return valueOf(val.size()); }
/** ..............................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritDivs0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    if(c1.signum()==0) return empty(val.size()); // c1..0......
    if(val.signum()==0) return this; // ....IntLive................
    final IntConst zero=IntConst.valueOf(c1.size(),0);
    final IntConst one=IntConst.valueOf(c1.size(),1);
    final IntConst msb=one.lsh(c1.size()-1);
    final IntConst all=zero.bnot();
    if(c1.equals(one)) return this; // c1..1............
    if(c1.equals(all)) return inheritNeg(); // c1..-1....NEG......
    IntConst c=c1.signum()>=0 ? c1 : c1.neg();
    IntConst a=rightmostOne(c);
    if(a.equals(c)) { // c1..2..........................
      // c1....(1<<i)......
      // ..........i..............k..................i+k................msb..............
      // ..........i......................................
      // ..: c1=001000......
      // ......(10..)  -8       -7       -33       -1         0     -128       127       -1
      // ......  11111000 11111001  11011111 11111111  00000000 10000000  01111111 11111111
      // ..      11111111 00000000  11111100 00000000  00000000 11110000  00001111 00000000
      IntConst v=propagateRight(val).mul(a).add(a).sub(one).bor(msb);
      // c1....((-1)<<i)......c1........................................
      // ......s..........................s-i......................i......................
      // ......c1..msb..............0..1......................................
      // ..: c1=111000......
      // ......(10..)  -8       -7        33        1        -1      127      -127        1
      // ......  11111000 11111001  00100001 00000001  11111111 01111111  10000001 00000001
      // ..      00000001 00000000  11111100 00000000  00000000 11110001  00001111 00000000
      // ......(10..)-128     -127
      // ......  10000000 10000001
      // ..      00010000 00001111
      if(c1.signum()<0 && val.band(all.divu(a)).signum()==0) {
        v=a.equals(msb) ? zero : v.band(a.neg());
      }
      return valueOf(v);
    }
    IntConst v=all;
    if(a.equals(one)) { // c1............
      // c1..............................................
      if(c1.signum()>0) {
        // c1..................1........i..2......1........j..
        // ........(msb......c..............)..K..............
        // ......................................................j............................
        // ......K..............k........................k..................
        v=rightmostOne(c.sub(one)).neg();
        IntConst b=msb.divu(c); // ........
        if(val.band(propagateRight(b)).signum()!=0) v=v.bor(rightmostOne(val).neg());
      }
      // ............c............-c+1............lsb..........msb................
      if(val.equals(one) && c.add(msb.mods(c)).equals(one)) v=v.band(msb.bnot());
      return valueOf(v);
    }
    // c1......................................
    if(c1.signum()<0) { // c1................
      IntConst b=msb.divu(c); // ........
      // c..........1........i..........(msb......c..............)..K..............
      // ..........K......................i......................
      if(val.band(propagateRight(b)).signum()==0) v=v.band(a.neg());
    }
    return valueOf(v);
  }
/** ..............................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritDivs1(IntConst c0) { return inheritDivu1(c0); }
/** ..............................IntLive....... */
  public IntLive inheritModu0() { return allOrEmpty(val,val.size()); }
/** ..............................IntLive....... */
  public IntLive inheritModu1() { return valueOf(val.size()); }
/** ..............................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritModu0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    if(c1.signum()==0) return empty(val.size()); // c1..0......
    IntConst a=rightmostOne(c1);
    // c1..2......................
    if(a.equals(c1)) return valueOf(val.band(a.sub(IntConst.valueOf(a.size(),1))));
    // c1..........1........i..........1........j........
    // ............i................................
    // ............i....j......................i............................
    // ............j..............0
    // ..:
    //     __________ 
    // 110 ) abcdefgh
    //
    //           ----
    //           ***h
    //            **0
    //           ----
    //            **h
    //           |||
    //           ||a,b,c,d,e,f,g..............
    //           |a,b,c,d,e,f,g..............
    //           ..............0
    IntConst b=propagateRight(c1).band(a.neg());
    if(val.band(b).signum()==0) return valueOf(a.sub(IntConst.valueOf(a.size(),1)).band(val));
    return valueOf(a.neg().bor(val));
  }
/** ..............................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritModu1(IntConst c0) { return inheritDivu1(c0); }
/** ..............................IntLive....... */
  public IntLive inheritMods0() { return inheritModu0(); }
/** ..............................IntLive....... */
  public IntLive inheritMods1() { return valueOf(val.size()); }
/** ..............................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritMods0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    if(c1.signum()==0) return empty(val.size()); // c1..0......
    final IntConst one=IntConst.valueOf(c1.size(),1);
    final IntConst msb=one.lsh(c1.size()-1);
    if(c1.signum()<0) c1=c1.neg(); // ..............c1....................
    if(c1.equals(one)) return empty(c1.size()); // c1..1..............0
    IntConst a=rightmostOne(c1);
    if(a.equals(c1)) { // c1..2......(1<<i)......
      IntConst v=val.band(a.sub(one)); // ......i......................
      // ......i..............................i..................msb..............
      // ..: c1=00010000......
      // ......(10..) -16      -14         4     -124
      // ......  11110000 11110010  00000100 10000100
      // ....    00000000 11110010  00000100 11110100
      if(val.band(a.neg()).signum()!=0) v=v.bor(a.sub(one)).bor(msb);
      return valueOf(v);
    }
    // c1..........1........i........
    // ............i................................
    // ............i........................................i............................
    // ..: c1=00010100(10....20)......
    // ......(10..) -20      -84
    // ......  11101100 10101100
    // ....    00000000 11111100
    IntConst v=val;
    if(val.band(a.neg()).signum()!=0) v=v.bor(a.neg());
    // c1......(i=0)..................i......lsb..........msb..........................
    // ..........................-c1+1............
    // ..: c1=000011......
    // ......  000000 000001 000010 000011 100000 100001 100010 100011
    // ....    000000 000001 000010 000000 111110 111111 000000 111110
    if(val.equals(one) && a.equals(one) && c1.add(msb.mods(c1)).equals(one)) v=v.band(msb.bnot());
    // ..........-c1....................................i....................
    // ....................0......................
    // ..: c1=00010100(10....20)......
    // ......(10..) -20      -19       -16      -15
    // ......  11101100 11101101  11111000 11111001
    // ....    00000000 11101101  11111000 11111001
    if(val.band(c1.neg()).signum()!=0) v=v.bor(a.sub(one));
    return valueOf(v);
  }
/** ..............................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritMods1(IntConst c0) { return inheritDivu1(c0); }
/** ........................IntLive....... */
  public IntLive inheritBand() { return this; }
/** ........................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritBand(IntConst c) { return valueOf(val.band(c)); }
/** ........................IntLive....... */
  public IntLive inheritBor() { return this; }
/** ........................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritBor(IntConst c) { return valueOf(val.band(c.bnot())); }
/** ..............................IntLive....... */
  public IntLive inheritBxor() { return this; }
/** ..............................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritBxor(IntConst c) {
    if(c.size()!=val.size()) throw new IllegalArgumentException(this+" "+c);
    return this;
  }
/** ..........................IntLive....... */
  public IntLive inheritLsh0() { return inheritAdd(); }
/** ..............(..................)............IntLive.......
 * @exception IllegalArgumentException ...................... */
  public IntLive inheritLsh1(int t) { return allOrEmpty(val,t); }
/** ..........................................................IntLive....... */
  public IntLive inheritLsh0(IntConst c1) { return valueOf(val.rshu(c1)); }
/** ..........................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritLsh1(IntConst c0,int t) { return inheritShift1(SHIFT_LSH,c0,t); }
/** ..................................IntLive....... */
  public IntLive inheritRshu0() {
    return valueOf(rightmostOne(val).neg());
  }
/** ......................(..................)............IntLive.......
 * @exception IllegalArgumentException ...................... */
  public IntLive inheritRshu1(int t) { return allOrEmpty(val,t); }
/** ..................................................................IntLive....... */
  public IntLive inheritRshu0(IntConst c1) { return valueOf(val.lsh(c1)); }
/** ..................................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritRshu1(IntConst c0,int t) { return inheritShift1(SHIFT_RSHU,c0,t); }
/** ..................................IntLive....... */
  public IntLive inheritRshs0() { return inheritRshu0(); }
/** ......................(..................)............IntLive.......
 * @exception IllegalArgumentException ...................... */
  public IntLive inheritRshs1(int t) { return allOrEmpty(val.band(msbOnly(t).bnot()),t); }
/** ..................................................................IntLive....... */
  public IntLive inheritRshs0(IntConst c1) {
    IntConst v=val.lsh(c1);
    // msb....................................msb............
    if(!v.rshu(c1).equals(val)) v=v.bor(msbOnly(val.size()));
    return valueOf(v);
  }
/** ..................................................................IntLive.......
 * @exception IllegalArgumentException ....IntLive.......................................... */
  public IntLive inheritRshs1(IntConst c0,int t) { return inheritShift1(SHIFT_RSHS,c0,t); }
// ..................................................................IntLive......
  private IntLive inheritShift1(Shift op,IntConst c0,int t) {
    final int size=val.size(),size2=size<<1;
    if(c0.size()!=size) throw new IllegalArgumentException(this+" "+c0);
    int v=0,s=0;
    // ......2^(r-1)............2^r............................r+1=s......................
    // r+1>t....t................
    for(int m=1;s<t && m!=0 && m-0x80000000<size2-0x80000000;m<<=1,s++) {
      IntConst a=IntConst.valueOf(size,0);
      for(int n=0;n<size;n=((n|m)+1)&~m) a=a.bor(op.eval(c0,n).bxor(op.eval(c0,n+m)));
      if(a.band(val).signum()!=0) v|=m;
    }
    // ......s......................................
    return valueOf(IntConst.valueOf(s,v).convsx(t));
  }
/** ................................IntLive....... */
  public IntLive inheritNeg() { return inheritAdd(); }
/** ................................IntLive....... */
  public IntLive inheritBnot() { return this; }
/** ==......(..................)............IntLive....... */
  public IntLive inheritTsteq(int t) {
    return allOrEmpty(val.band(IntConst.valueOf(val.size(),1)),t);
  }
/** ==..................................................IntLive....... */
  public IntLive inheritTsteq(IntConst c) {
    return allOrEmpty(val.band(IntConst.valueOf(val.size(),1)),c.size());
  }
/** !=......(..................)............IntLive....... */
  public IntLive inheritTstne(int t) { return inheritTsteq(t); }
/** !=..................................................IntLive....... */
  public IntLive inheritTstne(IntConst c) { return inheritTsteq(c); }
/** &lt;(........)......(..................)............IntLive....... */
  public IntLive inheritTstltu0(int t) { return inheritTsteq(t); }
/** &lt;(........)......(..................)............IntLive....... */
  public IntLive inheritTstltu1(int t) { return inheritTsteq(t); }
/** &lt;(........)..................................................IntLive....... */
  public IntLive inheritTstltu0(IntConst c1) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c1.size());
    // ..............1..................
    return valueOf(rightmostOne(c1).neg());
  }
/** &lt;(........)..................................................IntLive....... */
  public IntLive inheritTstltu1(IntConst c0) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c0.size());
    // ....+1..........1..................
    // ..............1..................................
    return valueOf(rightmostOne(c0.add(IntConst.valueOf(c0.size(),1))).neg());
  }
/** &gt;(........)......(..................)............IntLive....... */
  public IntLive inheritTstgtu0(int t) { return inheritTsteq(t); }
/** &gt;(........)......(..................)............IntLive....... */
  public IntLive inheritTstgtu1(int t) { return inheritTsteq(t); }
/** &gt;(........)..................................................IntLive....... */
  public IntLive inheritTstgtu0(IntConst c1) { return inheritTstltu1(c1); }
/** &gt;(........)..................................................IntLive....... */
  public IntLive inheritTstgtu1(IntConst c0) { return inheritTstltu0(c0); }
/** &lt;=(........)......(..................)............IntLive....... */
  public IntLive inheritTstleu0(int t) { return inheritTsteq(t); }
/** &lt;=(........)......(..................)............IntLive....... */
  public IntLive inheritTstleu1(int t) { return inheritTsteq(t); }
/** &lt;=(........)..................................................IntLive....... */
  public IntLive inheritTstleu0(IntConst c1) { return inheritTstltu1(c1); }
/** &lt;=(........)..................................................IntLive....... */
  public IntLive inheritTstleu1(IntConst c0) { return inheritTstltu0(c0); }
/** &gt;=(........)......(..................)............IntLive....... */
  public IntLive inheritTstgeu0(int t) { return inheritTsteq(t); }
/** &gt;=(........)......(..................)............IntLive....... */
  public IntLive inheritTstgeu1(int t) { return inheritTsteq(t); }
/** &gt;=(........)..................................................IntLive....... */
  public IntLive inheritTstgeu0(IntConst c1) { return inheritTstltu0(c1); }
/** &gt;=(........)..................................................IntLive....... */
  public IntLive inheritTstgeu1(IntConst c0) { return inheritTstltu1(c0); }
/** &lt;(........)......(..................)............IntLive....... */
  public IntLive inheritTstlts0(int t) { return inheritTsteq(t); }
/** &lt;(........)......(..................)............IntLive....... */
  public IntLive inheritTstlts1(int t) { return inheritTsteq(t); }
/** &lt;(........)..................................................IntLive....... */
  public IntLive inheritTstlts0(IntConst c1) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c1.size());
    // ....+msb..........1..................
    return valueOf(rightmostOne(c1.add(msbOnly(c1.size()))).neg());
  }
/** &lt;(........)..................................................IntLive....... */
  public IntLive inheritTstlts1(IntConst c0) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c0.size());
    // ....+msb+1..........1..................
    return valueOf(rightmostOne(c0.sub(msbOnly(c0.size()).bnot())).neg());
  }
/** &gt;(........)......(..................)............IntLive....... */
  public IntLive inheritTstgts0(int t) { return inheritTsteq(t); }
/** &gt;(........)......(..................)............IntLive....... */
  public IntLive inheritTstgts1(int t) { return inheritTsteq(t); }
/** &gt;(........)..................................................IntLive....... */
  public IntLive inheritTstgts0(IntConst c1) { return inheritTstlts1(c1); }
/** &gt;(........)..................................................IntLive....... */
  public IntLive inheritTstgts1(IntConst c0) { return inheritTstlts0(c0); }
/** &lt;=(........)......(..................)............IntLive....... */
  public IntLive inheritTstles0(int t) { return inheritTsteq(t); }
/** &lt;=(........)......(..................)............IntLive....... */
  public IntLive inheritTstles1(int t) { return inheritTsteq(t); }
/** &lt;=(........)..................................................IntLive....... */
  public IntLive inheritTstles0(IntConst c1) { return inheritTstlts1(c1); }
/** &lt;=(........)..................................................IntLive....... */
  public IntLive inheritTstles1(IntConst c0) { return inheritTstlts0(c0); }
/** &gt;=(........)......(..................)............IntLive....... */
  public IntLive inheritTstges0(int t) { return inheritTsteq(t); }
/** &gt;=(........)......(..................)............IntLive....... */
  public IntLive inheritTstges1(int t) { return inheritTsteq(t); }
/** &gt;=(........)..................................................IntLive....... */
  public IntLive inheritTstges0(IntConst c1) { return inheritTstlts0(c1); }
/** &gt;=(........)..................................................IntLive....... */
  public IntLive inheritTstges1(IntConst c0) { return inheritTstlts1(c0); }
/** ....................(..................)............IntLive.......
 * @exception IllegalArgumentException .................................................... */
  public IntLive inheritConvzx(int t) { return valueOf(val.convit(t)); }
/** ....................(..................)............IntLive.......
 * @exception IllegalArgumentException .................................................... */
  public IntLive inheritConvsx(int t) {
    IntConst v=val.convit(t);
    // ......................................msb............
    IntConst a=IntConst.valueOf(val.size(),1).lsh(t).neg();
    if(val.band(a).signum()!=0) v=v.bor(msbOnly(t));
    return valueOf(v);
  }
/** ................(..................)............IntLive.......
 * @exception IllegalArgumentException .................................... */
  public IntLive inheritConvit(int t) { return valueOf(val.convzx(t)); }
/** IFTHENELSE......(..................)............IntLive....... */
  public IntLive inheritIfthenelse0(int t) { return allOrEmpty(val,t); }
/** IFTHENELSE..................IntLive....... */
  public IntLive inheritIfthenelse1() { return this; }
/** IFTHENELSE..................IntLive....... */
  public IntLive inheritIfthenelse2() { return this; }
/** IFTHENELSE......................................(..................)............IntLive....... */
  public IntLive inheritIfthenelse0_1(int t,IntConst c1) { return inheritIfthenelse0(t); }
/** IFTHENELSE......................................(..................)............IntLive....... */
  public IntLive inheritIfthenelse0_2(int t,IntConst c2) { return inheritIfthenelse0(t); }
/** IFTHENELSE..................................................IntLive....... */
  public IntLive inheritIfthenelse1_0(IntConst c0) { return c0.signum()!=0 ? this : empty(val.size()); }
/** IFTHENELSE..................................................IntLive....... */
  public IntLive inheritIfthenelse1_2(IntConst c2) { return inheritIfthenelse1(); }
/** IFTHENELSE..................................................IntLive....... */
  public IntLive inheritIfthenelse2_0(IntConst c0) { return c0.signum()==0 ? this : empty(val.size()); }
/** IFTHENELSE..................................................IntLive....... */
  public IntLive inheritIfthenelse2_1(IntConst c1) { return inheritIfthenelse2(); }
/** IFTHENELSE............................................(..................)............IntLive....... */
  public IntLive inheritIfthenelse0(int t,IntConst c1,IntConst c2) {
    return allOrEmpty(c1.bxor(c2).band(val),t);
  }
/** IFTHENELSE........................................................IntLive....... */
  public IntLive inheritIfthenelse1(IntConst c0,IntConst c2) { return inheritIfthenelse1_0(c0); }
/** IFTHENELSE........................................................IntLive....... */
  public IntLive inheritIfthenelse2(IntConst c0,IntConst c1) { return inheritIfthenelse2_0(c0); }
/** ....IntLive............................................. */
  public IntConst intConstValue() { return val; }
/** ....IntLive............................................... */
  public boolean equals(Object o) {
    return o==this || o instanceof IntLive && val.equals((((IntLive)o).val));
  }
/** ....IntLive....................... */
  public int hashCode() { return val.hashCode(); }
/** ....IntLive................... */
  public String toString() {
    StringBuffer sb=new StringBuffer("{");
    final IntConst one=IntConst.valueOf(val.size(),1);
    int j=-2;
    for(int i=0;i<val.size();i++) {
      int s=val.rshu(i).band(one).signum();
      if(j<0 && s>0) {
        if(j==-1) sb.append(',');
        sb.append(i);
        j=i; 
      }
      if(j>=0 && s==0) {
        if(i>j+1) sb.append("..").append(i-1);
        j=-1;
      }
    }
    if(j>=0 && j<val.size()-1) sb.append("..").append(val.size()-1);
    return sb.append("}:").append(val.size()).toString();
  }
// ........1
  private static IntConst rightmostOne(IntConst c) { return c.neg().band(c); }
// ........1................1..
  private static IntConst propagateRight(IntConst c) {
    for(int i=1;i>=0 && i<c.size();i<<=1) c=c.bor(c.rshu(i));
    return c;
  }
// msb....1
  private static IntConst msbOnly(int t) { return IntConst.valueOf(t,1).lsh(t-1); }
// ..........0......................IntLive..0..........................IntLive
  private static IntLive allOrEmpty(IntConst c,int t) {
    return c.signum()==0 ? empty(t) : valueOf(t);
  }

}
