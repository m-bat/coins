/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.util;

/**
 * 整数の生きているビット(不変な値クラス).
 * 整数演算で各部分式の値のそれぞれのビットの生死を表す。
 * 演算子の結果の生きているビットが指定されたときに、
 * 各オペランドに生きているビットがどのように相続されるかを求めるメソッドを提供する。
 * 除算と剰余では、0除算例外が起きるかどうかも結果の一部とみなしているため、
 * 除数の全ビットが、0かどうかに影響があるので、生きていることになる。
 * 除数が定数の場合、定数が0なら被除数の全ビットは死んでいることになる。
 */
public final class IntLive {

// field

// 値(生きているビットは1、死んでいるビットは0)
  private final IntConst val;

// member type

// シフト演算子の関数オブジェクト
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

/** 指定されたビット数すべてが生きているIntLiveを返す.
 * @exception IllegalArgumentException ビット数が正でない場合 */
  public static IntLive valueOf(int size) { return new IntLive(IntConst.valueOf(size,0).bnot()); }
/** 指定されたビット数すべてが死んでいるIntLiveを返す.
 * @exception IllegalArgumentException ビット数が正でない場合 */
  public static IntLive empty(int size) { return new IntLive(IntConst.valueOf(size,0)); }
/** 指定された整数定数の立っているビットが生きているIntLiveを返す. */
  public static IntLive valueOf(IntConst val) { return new IntLive(val); }

// method

/** 指定されたIntLiveとの合併を返す.
 * @exception IllegalArgumentException このIntLiveと指定されたIntLiveのビット数が異なる場合 */
  public IntLive union(IntLive l) { return valueOf(val.bor(l.val)); }
/** 指定されたIntLiveとの共通部分を返す.
 * @exception IllegalArgumentException このIntLiveと指定されたIntLiveのビット数が異なる場合 */
  public IntLive intersection(IntLive l) { return valueOf(val.band(l.val)); }
/** 加算の両辺に相続されるIntLiveを返す. */
  public IntLive inheritAdd() {
    // 生きているビットより下位はすべて生きている
    return valueOf(propagateRight(val));
  }
/** 加算の一辺が指定された整数定数のとき、他辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritAdd(IntConst c) {
    // cの最下位の1より下では、生きているビットがそのまま相続される
    // cの最下位の1以上では、生きているビット以下はすべて生きている
    // 例:
    // 01000010 和の生きているビット
    // 00101000 整数定数
    // 01111010 相続される生きているビット
    return valueOf(propagateRight(val).band(rightmostOne(c).neg()).bor(val));
  }
/** 減算の左辺に相続されるIntLiveを返す. */
  public IntLive inheritSub0() { return inheritAdd(); }
/** 減算の右辺に相続されるIntLiveを返す. */
  public IntLive inheritSub1() { return inheritAdd(); }
/** 減算の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritSub0(IntConst c1) { return inheritAdd(c1.neg()); }
/** 減算の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritSub1(IntConst c0) {
    // x-yと~(x-y)は生きているビットに関して同じであることを利用
    // ~(x-y) = y-x-1 = (-x-1)+y = ~x+y
    return inheritAdd(c0.bnot());
  }
/** 乗算の両辺に相続されるIntLiveを返す. */
  public IntLive inheritMul() { return inheritAdd(); }
/** 乗算の一辺が指定された整数定数のとき、他辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritMul(IntConst c) {
    if(c.size()!=val.size()) throw new IllegalArgumentException(this+" "+c);
    if(c.signum()==0) return empty(val.size());
    IntConst a=rightmostOne(c);
    IntConst v0=val.divu(a);
    if(a.equals(c)) return valueOf(v0); // cが2のべきならシフトと同じ
    // c最下位の1がビットiで、2番目の1がビットi+jとすると、積は、
    //  ビット0からi-1までは常に0
    //  ビットiからi+j-1までは被乗数のビット0からj-1までがそのまま伝わる
    //  ビットi+j+k(k>=0)では、被乗数のビット0からkまでとビットjからj+kまでの影響を受ける
    // このIntLiveのビットi+j以上で生きているビットがなければ、
    // 被乗数の生きているビットは、このIntLiveを右にiだけシフトしたものになる
    // このIntLiveのビットi+j以上で生きているビットがあれば、その最上位をビットi+j+Kとすると
    // 被乗数の生きているビットは
    //  ビットjからj+Kまで1
    //  ビット0からKまで1
    //  このIntLiveを右にiだけシフト
    // の三つの合併になる
    // 例:
    //    hgfedcba
    //  x 01010010
    // -----------
    //     fedcba
    //    dcba
    //    ba
    // -----------
    //    ****cba0
    //     |||
    //     ||a,dの影響を受ける
    //     |a,b,d,eの影響を受ける
    //     a,b,c,d,e,fの影響を受ける(乗数の3番目の1の影響は2番目の1の影響に含まれる)
    IntConst b=rightmostOne(c.sub(a).divu(a)); // cの最下位と2番目の1の比
    IntConst v=propagateRight(v0.divu(b));
    return valueOf(v.mul(b).bor(v).bor(v0));
  }
/** 符号なし除算の左辺に相続されるIntLiveを返す. */
  public IntLive inheritDivu0() {
    // 生きているビットより上位はすべて生きている
    return valueOf(rightmostOne(val).neg());
  }
/** 符号なし除算の右辺に相続されるIntLiveを返す. */
  public IntLive inheritDivu1() { return valueOf(val.size()); }
/** 符号なし除算の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritDivu0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    IntConst a=rightmostOne(c1);
    if(a.equals(c1)) return valueOf(val.mul(a)); // c1が2のべきならシフトと同じ c1が0なら空
    IntConst b=IntConst.valueOf(c1.size(),0).bnot().divu(c1); // 商の最大
    // 商の常に0のビットしか見ていない場合は空
    if(val.band(propagateRight(b)).signum()==0) return empty(val.size());
    // c1の最下位の1をビットi、このIntLiveの生きている最下位をビットjとすると
    // ビット0からi+j-1までは死んでいるが、ビットi+j以上の全ビットは生きている
    // 例:
    //       この二桁は常に0
    //       ||a,bの影響を受ける(cの影響はない)
    //       |||
    //       |||   a,b,c,d,e,fの影響を受ける
    //       |||   |a,b,c,d,e,f,gの影響を受ける
    //       |||   ||
    //     __________ hは商に影響しない
    // 110 ) abcdefgh
    return valueOf(a.mul(rightmostOne(val)).neg());
  }
/** 符号なし除算の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritDivu1(IntConst c0) {
    if(c0.size()!=val.size()) throw new IllegalArgumentException(this+" "+c0);
    return valueOf(val.size());
  }
/** 符号つき除算の左辺に相続されるIntLiveを返す. */
  public IntLive inheritDivs0() { return allOrEmpty(val,val.size()); }
/** 符号つき除算の右辺に相続されるIntLiveを返す. */
  public IntLive inheritDivs1() { return valueOf(val.size()); }
/** 符号つき除算の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritDivs0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    if(c1.signum()==0) return empty(val.size()); // c1が0なら空
    if(val.signum()==0) return this; // このIntLiveが空ならそのまま
    final IntConst zero=IntConst.valueOf(c1.size(),0);
    final IntConst one=IntConst.valueOf(c1.size(),1);
    final IntConst msb=one.lsh(c1.size()-1);
    final IntConst all=zero.bnot();
    if(c1.equals(one)) return this; // c1が1ならそのまま
    if(c1.equals(all)) return inheritNeg(); // c1が-1ならNEGと同じ
    IntConst c=c1.signum()>=0 ? c1 : c1.neg();
    IntConst a=rightmostOne(c);
    if(a.equals(c)) { // c1が2のべきかその符号反転のとき
      // c1が正(1<<i)なら、
      // 商のビットiより下のビットkは、被除数のビットi+k以下の全ビットとmsbの影響を受ける
      // 商のビットi以上は、被除数の全ビットの影響を受ける
      // 例: c1=001000のとき
      // 被除数(10進)  -8       -7       -33       -1         0     -128       127       -1
      // 被除数  11111000 11111001  11011111 11111111  00000000 10000000  01111111 11111111
      // 商      11111111 00000000  11111100 00000000  00000000 11110000  00001111 00000000
      IntConst v=propagateRight(val).mul(a).add(a).sub(one).bor(msb);
      // c1が負((-1)<<i)なら、c1が正のときと似ているが、以下の点が異なる
      // 全体がsビットとすると、商のビットs-i以上は、被除数のビットiより下の影響を受けない
      // なお、c1がmsbのみなら、商は0か1で負になることはないので、特別扱いする
      // 例: c1=111000のとき
      // 被除数(10進)  -8       -7        33        1        -1      127      -127        1
      // 被除数  11111000 11111001  00100001 00000001  11111111 01111111  10000001 00000001
      // 商      00000001 00000000  11111100 00000000  00000000 11110001  00001111 00000000
      // 被除数(10進)-128     -127
      // 被除数  10000000 10000001
      // 商      00010000 00001111
      if(c1.signum()<0 && val.band(all.divu(a)).signum()==0) {
        v=a.equals(msb) ? zero : v.band(a.neg());
      }
      return valueOf(v);
    }
    IntConst v=all;
    if(a.equals(one)) { // c1が奇数のとき
      // c1が負なら、一部例外を除いて、全ビットが影響する
      if(c1.signum()>0) {
        // c1が正なら、最下位の1をビットi、2番目の1をビットj、
        // 商の最大(msbのみをcで符号なし除算)がKビットとすると
        // 商のすべてのビットは、一部例外を除いて、被除数のビットj以上の全ビットの影響を受ける
        // ビットKより下のビットkはさらに、被除数のビットk以上の影響を受ける
        v=rightmostOne(c.sub(one)).neg();
        IntConst b=msb.divu(c); // 商の最大
        if(val.band(propagateRight(b)).signum()!=0) v=v.bor(rightmostOne(val).neg());
      }
      // 負の最大数のcによる剰余が-c+1のとき、商のlsbは被除数のmsbの影響を受けない
      if(val.equals(one) && c.add(msb.mods(c)).equals(one)) v=v.band(msb.bnot());
      return valueOf(v);
    }
    // c1が偶数で正のときは、全ビットが影響する
    if(c1.signum()<0) { // c1が偶数で負のとき
      IntConst b=msb.divu(c); // 商の最大
      // cの最下位の1をビットi、商の最大(msbのみをcで符号なし除算)がKビットとすると
      // 商のビットK以上は、被除数のビットiより下の影響を受けない
      if(val.band(propagateRight(b)).signum()==0) v=v.band(a.neg());
    }
    return valueOf(v);
  }
/** 符号つき除算の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritDivs1(IntConst c0) { return inheritDivu1(c0); }
/** 符号なし剰余の左辺に相続されるIntLiveを返す. */
  public IntLive inheritModu0() { return allOrEmpty(val,val.size()); }
/** 符号なし剰余の右辺に相続されるIntLiveを返す. */
  public IntLive inheritModu1() { return valueOf(val.size()); }
/** 符号なし剰余の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritModu0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    if(c1.signum()==0) return empty(val.size()); // c1が0なら空
    IntConst a=rightmostOne(c1);
    // c1が2のべきなら論理積と同じ
    if(a.equals(c1)) return valueOf(val.band(a.sub(IntConst.valueOf(a.size(),1))));
    // c1の最下位の1をビットi、最上位の1をビットjとすると
    // 剰余のビットiより下は、被除数がそのまま伝わる
    // 剰余のビットiからjまでは、被除数のビットi以上の全ビットの影響を受ける
    // 剰余のビットjより上位は常に0
    // 例:
    //     __________ 
    // 110 ) abcdefgh
    //
    //           ----
    //           ***h
    //            **0
    //           ----
    //            **h
    //           |||
    //           ||a,b,c,d,e,f,gの影響を受ける
    //           |a,b,c,d,e,f,gの影響を受ける
    //           これより上位は0
    IntConst b=propagateRight(c1).band(a.neg());
    if(val.band(b).signum()==0) return valueOf(a.sub(IntConst.valueOf(a.size(),1)).band(val));
    return valueOf(a.neg().bor(val));
  }
/** 符号なし剰余の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritModu1(IntConst c0) { return inheritDivu1(c0); }
/** 符号つき剰余の左辺に相続されるIntLiveを返す. */
  public IntLive inheritMods0() { return inheritModu0(); }
/** 符号つき剰余の右辺に相続されるIntLiveを返す. */
  public IntLive inheritMods1() { return valueOf(val.size()); }
/** 符号つき剰余の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritMods0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    if(c1.signum()==0) return empty(val.size()); // c1が0なら空
    final IntConst one=IntConst.valueOf(c1.size(),1);
    final IntConst msb=one.lsh(c1.size()-1);
    if(c1.signum()<0) c1=c1.neg(); // 符号つき剰余はc1の符号に影響されない
    if(c1.equals(one)) return empty(c1.size()); // c1が1なら剰余は常に0
    IntConst a=rightmostOne(c1);
    if(a.equals(c1)) { // c1が2のべき(1<<i)のとき
      IntConst v=val.band(a.sub(one)); // ビットiより下はそのまま伝わる
      // ビットi以上のすべてのビットは、ビットiより下の全ビットとmsbの影響を受ける
      // 例: c1=00010000のとき
      // 被除数(10進) -16      -14         4     -124
      // 被除数  11110000 11110010  00000100 10000100
      // 剰余    00000000 11110010  00000100 11110100
      if(val.band(a.neg()).signum()!=0) v=v.bor(a.sub(one)).bor(msb);
      return valueOf(v);
    }
    // c1の最下位の1をビットiとすると
    // 剰余のビットiより下は、被除数がそのまま伝わる
    // 剰余のビットi以上は、一部例外を除いて、被除数のビットi以上の全ビットの影響を受ける
    // 例: c1=00010100(10進で20)のとき
    // 被除数(10進) -20      -84
    // 被除数  11101100 10101100
    // 剰余    00000000 11111100
    IntConst v=val;
    if(val.band(a.neg()).signum()!=0) v=v.bor(a.neg());
    // c1が奇数(i=0)なら、剰余のビットiつまりlsbは被除数のmsbの影響を受けない場合がある
    // それは、負の最大数の剰余が-c1+1のときである
    // 例: c1=000011のとき
    // 被除数  000000 000001 000010 000011 100000 100001 100010 100011
    // 剰余    000000 000001 000010 000000 111110 111111 000000 111110
    if(val.equals(one) && a.equals(one) && c1.add(msb.mods(c1)).equals(one)) v=v.band(msb.bnot());
    // 剰余のうち-c1の立っているビットは、被除数のビットiより下の影響を受ける
    // この種の影響は剰余が0から変化するときに限る
    // 例: c1=00010100(10進で20)のとき
    // 被除数(10進) -20      -19       -16      -15
    // 被除数  11101100 11101101  11111000 11111001
    // 剰余    00000000 11101101  11111000 11111001
    if(val.band(c1.neg()).signum()!=0) v=v.bor(a.sub(one));
    return valueOf(v);
  }
/** 符号つき剰余の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritMods1(IntConst c0) { return inheritDivu1(c0); }
/** 論理積の両辺に相続されるIntLiveを返す. */
  public IntLive inheritBand() { return this; }
/** 論理積の一辺が指定された整数定数のとき、他辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritBand(IntConst c) { return valueOf(val.band(c)); }
/** 論理和の両辺に相続されるIntLiveを返す. */
  public IntLive inheritBor() { return this; }
/** 論理和の一辺が指定された整数定数のとき、他辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritBor(IntConst c) { return valueOf(val.band(c.bnot())); }
/** 排他的論理和の両辺に相続されるIntLiveを返す. */
  public IntLive inheritBxor() { return this; }
/** 排他的論理和の一辺が指定された整数定数のとき、他辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritBxor(IntConst c) {
    if(c.size()!=val.size()) throw new IllegalArgumentException(this+" "+c);
    return this;
  }
/** 左シフトの左辺に相続されるIntLiveを返す. */
  public IntLive inheritLsh0() { return inheritAdd(); }
/** 左シフトの右辺(指定されたビット数)に相続されるIntLiveを返す.
 * @exception IllegalArgumentException ビット数が正でない場合 */
  public IntLive inheritLsh1(int t) { return allOrEmpty(val,t); }
/** 左シフトの右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す. */
  public IntLive inheritLsh0(IntConst c1) { return valueOf(val.rshu(c1)); }
/** 左シフトの左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritLsh1(IntConst c0,int t) { return inheritShift1(SHIFT_LSH,c0,t); }
/** 符号なし右シフトの左辺に相続されるIntLiveを返す. */
  public IntLive inheritRshu0() {
    return valueOf(rightmostOne(val).neg());
  }
/** 符号なし右シフトの右辺(指定されたビット数)に相続されるIntLiveを返す.
 * @exception IllegalArgumentException ビット数が正でない場合 */
  public IntLive inheritRshu1(int t) { return allOrEmpty(val,t); }
/** 符号なし右シフトの右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す. */
  public IntLive inheritRshu0(IntConst c1) { return valueOf(val.lsh(c1)); }
/** 符号なし右シフトの左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritRshu1(IntConst c0,int t) { return inheritShift1(SHIFT_RSHU,c0,t); }
/** 符号つき右シフトの左辺に相続されるIntLiveを返す. */
  public IntLive inheritRshs0() { return inheritRshu0(); }
/** 符号つき右シフトの右辺(指定されたビット数)に相続されるIntLiveを返す.
 * @exception IllegalArgumentException ビット数が正でない場合 */
  public IntLive inheritRshs1(int t) { return allOrEmpty(val.band(msbOnly(t).bnot()),t); }
/** 符号つき右シフトの右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す. */
  public IntLive inheritRshs0(IntConst c1) {
    IntConst v=val.lsh(c1);
    // msbの影響のあるビットが生きているなら、msbは生きている
    if(!v.rshu(c1).equals(val)) v=v.bor(msbOnly(val.size()));
    return valueOf(v);
  }
/** 符号つき右シフトの左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す.
 * @exception IllegalArgumentException このIntLiveと指定された整数定数のビット数が異なる場合 */
  public IntLive inheritRshs1(IntConst c0,int t) { return inheritShift1(SHIFT_RSHS,c0,t); }
// 指定されたシフトの左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す
  private IntLive inheritShift1(Shift op,IntConst c0,int t) {
    final int size=val.size(),size2=size<<1;
    if(c0.size()!=size) throw new IllegalArgumentException(this+" "+c0);
    int v=0,s=0;
    // 左辺が2^(r-1)ビットを超え2^rビット以下なら、右辺を下からr+1=sビットぶん素朴に求める
    // r+1>tならtビットぶんでよい
    for(int m=1;s<t && m!=0 && m-0x80000000<size2-0x80000000;m<<=1,s++) {
      IntConst a=IntConst.valueOf(size,0);
      for(int n=0;n<size;n=((n|m)+1)&~m) a=a.bor(op.eval(c0,n).bxor(op.eval(c0,n+m)));
      if(a.band(val).signum()!=0) v|=m;
    }
    // ビットs以上は、今まで求めた最上位ビットと同じ
    return valueOf(IntConst.valueOf(s,v).convsx(t));
  }
/** 符号反転のオペランドに相続されるIntLiveを返す. */
  public IntLive inheritNeg() { return inheritAdd(); }
/** 論理否定のオペランドに相続されるIntLiveを返す. */
  public IntLive inheritBnot() { return this; }
/** ==の両辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTsteq(int t) {
    return allOrEmpty(val.band(IntConst.valueOf(val.size(),1)),t);
  }
/** ==の一辺が指定された整数定数のとき、他辺に相続されるIntLiveを返す. */
  public IntLive inheritTsteq(IntConst c) {
    return allOrEmpty(val.band(IntConst.valueOf(val.size(),1)),c.size());
  }
/** !=の両辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstne(int t) { return inheritTsteq(t); }
/** !=の一辺が指定された整数定数のとき、他辺に相続されるIntLiveを返す. */
  public IntLive inheritTstne(IntConst c) { return inheritTsteq(c); }
/** &lt;(符号なし)の右辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstltu0(int t) { return inheritTsteq(t); }
/** &lt;(符号なし)の左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstltu1(int t) { return inheritTsteq(t); }
/** &lt;(符号なし)の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す. */
  public IntLive inheritTstltu0(IntConst c1) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c1.size());
    // 右辺の最下位の1より下は死んでいる
    return valueOf(rightmostOne(c1).neg());
  }
/** &lt;(符号なし)の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す. */
  public IntLive inheritTstltu1(IntConst c0) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c0.size());
    // 左辺+1の最下位の1より下は死んでいる
    // 左辺が全ビット1ならラップアラウンドするが問題ない
    return valueOf(rightmostOne(c0.add(IntConst.valueOf(c0.size(),1))).neg());
  }
/** &gt;(符号なし)の右辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstgtu0(int t) { return inheritTsteq(t); }
/** &gt;(符号なし)の左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstgtu1(int t) { return inheritTsteq(t); }
/** &gt;(符号なし)の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す. */
  public IntLive inheritTstgtu0(IntConst c1) { return inheritTstltu1(c1); }
/** &gt;(符号なし)の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す. */
  public IntLive inheritTstgtu1(IntConst c0) { return inheritTstltu0(c0); }
/** &lt;=(符号なし)の右辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstleu0(int t) { return inheritTsteq(t); }
/** &lt;=(符号なし)の左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstleu1(int t) { return inheritTsteq(t); }
/** &lt;=(符号なし)の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す. */
  public IntLive inheritTstleu0(IntConst c1) { return inheritTstltu1(c1); }
/** &lt;=(符号なし)の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す. */
  public IntLive inheritTstleu1(IntConst c0) { return inheritTstltu0(c0); }
/** &gt;=(符号なし)の右辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstgeu0(int t) { return inheritTsteq(t); }
/** &gt;=(符号なし)の左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstgeu1(int t) { return inheritTsteq(t); }
/** &gt;=(符号なし)の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す. */
  public IntLive inheritTstgeu0(IntConst c1) { return inheritTstltu0(c1); }
/** &gt;=(符号なし)の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す. */
  public IntLive inheritTstgeu1(IntConst c0) { return inheritTstltu1(c0); }
/** &lt;(符号つき)の右辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstlts0(int t) { return inheritTsteq(t); }
/** &lt;(符号つき)の左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstlts1(int t) { return inheritTsteq(t); }
/** &lt;(符号つき)の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す. */
  public IntLive inheritTstlts0(IntConst c1) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c1.size());
    // 左辺+msbの最下位の1より下は死んでいる
    return valueOf(rightmostOne(c1.add(msbOnly(c1.size()))).neg());
  }
/** &lt;(符号つき)の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す. */
  public IntLive inheritTstlts1(IntConst c0) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c0.size());
    // 左辺+msb+1の最下位の1より下は死んでいる
    return valueOf(rightmostOne(c0.sub(msbOnly(c0.size()).bnot())).neg());
  }
/** &gt;(符号つき)の右辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstgts0(int t) { return inheritTsteq(t); }
/** &gt;(符号つき)の左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstgts1(int t) { return inheritTsteq(t); }
/** &gt;(符号つき)の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す. */
  public IntLive inheritTstgts0(IntConst c1) { return inheritTstlts1(c1); }
/** &gt;(符号つき)の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す. */
  public IntLive inheritTstgts1(IntConst c0) { return inheritTstlts0(c0); }
/** &lt;=(符号つき)の右辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstles0(int t) { return inheritTsteq(t); }
/** &lt;=(符号つき)の左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstles1(int t) { return inheritTsteq(t); }
/** &lt;=(符号つき)の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す. */
  public IntLive inheritTstles0(IntConst c1) { return inheritTstlts1(c1); }
/** &lt;=(符号つき)の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す. */
  public IntLive inheritTstles1(IntConst c0) { return inheritTstlts0(c0); }
/** &gt;=(符号つき)の右辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstges0(int t) { return inheritTsteq(t); }
/** &gt;=(符号つき)の左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritTstges1(int t) { return inheritTsteq(t); }
/** &gt;=(符号つき)の右辺が指定された整数定数のとき、左辺に相続されるIntLiveを返す. */
  public IntLive inheritTstges0(IntConst c1) { return inheritTstlts0(c1); }
/** &gt;=(符号つき)の左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す. */
  public IntLive inheritTstges1(IntConst c0) { return inheritTstlts1(c0); }
/** ゼロ拡張のオペランド(指定されたビット数)に相続されるIntLiveを返す.
 * @exception IllegalArgumentException 指定されたビット数のほうが大きい、または正でない場合 */
  public IntLive inheritConvzx(int t) { return valueOf(val.convit(t)); }
/** 符号拡張のオペランド(指定されたビット数)に相続されるIntLiveを返す.
 * @exception IllegalArgumentException 指定されたビット数のほうが大きい、または正でない場合 */
  public IntLive inheritConvsx(int t) {
    IntConst v=val.convit(t);
    // 符号拡張で増えた部分が生きているなら、msbは生きている
    IntConst a=IntConst.valueOf(val.size(),1).lsh(t).neg();
    if(val.band(a).signum()!=0) v=v.bor(msbOnly(t));
    return valueOf(v);
  }
/** 縮小のオペランド(指定されたビット数)に相続されるIntLiveを返す.
 * @exception IllegalArgumentException 指定されたビット数のほうが小さい場合 */
  public IntLive inheritConvit(int t) { return valueOf(val.convzx(t)); }
/** IFTHENELSEの左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse0(int t) { return allOrEmpty(val,t); }
/** IFTHENELSEの中辺に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse1() { return this; }
/** IFTHENELSEの右辺に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse2() { return this; }
/** IFTHENELSEの中辺が指定された整数定数のとき、左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse0_1(int t,IntConst c1) { return inheritIfthenelse0(t); }
/** IFTHENELSEの右辺が指定された整数定数のとき、左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse0_2(int t,IntConst c2) { return inheritIfthenelse0(t); }
/** IFTHENELSEの左辺が指定された整数定数のとき、中辺に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse1_0(IntConst c0) { return c0.signum()!=0 ? this : empty(val.size()); }
/** IFTHENELSEの右辺が指定された整数定数のとき、中辺に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse1_2(IntConst c2) { return inheritIfthenelse1(); }
/** IFTHENELSEの左辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse2_0(IntConst c0) { return c0.signum()==0 ? this : empty(val.size()); }
/** IFTHENELSEの中辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse2_1(IntConst c1) { return inheritIfthenelse2(); }
/** IFTHENELSEの中辺と右辺が指定された整数定数のとき、左辺(指定されたビット数)に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse0(int t,IntConst c1,IntConst c2) {
    return allOrEmpty(c1.bxor(c2).band(val),t);
  }
/** IFTHENELSEの左辺と右辺が指定された整数定数のとき、中辺に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse1(IntConst c0,IntConst c2) { return inheritIfthenelse1_0(c0); }
/** IFTHENELSEの左辺と中辺が指定された整数定数のとき、右辺に相続されるIntLiveを返す. */
  public IntLive inheritIfthenelse2(IntConst c0,IntConst c1) { return inheritIfthenelse2_0(c0); }
/** このIntLiveの生きているビットが立っている整数定数を返す. */
  public IntConst intConstValue() { return val; }
/** このIntLiveと指定されたオブジェクトが等しいかどうかを返す. */
  public boolean equals(Object o) {
    return o==this || o instanceof IntLive && val.equals((((IntLive)o).val));
  }
/** このIntLiveのハッシュコードを返す. */
  public int hashCode() { return val.hashCode(); }
/** このIntLiveの文字列表現を返す. */
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
// 最下位の1
  private static IntConst rightmostOne(IntConst c) { return c.neg().band(c); }
// 最上位の1より下位をすべて1に
  private static IntConst propagateRight(IntConst c) {
    for(int i=1;i>=0 && i<c.size();i<<=1) c=c.bor(c.rshu(i));
    return c;
  }
// msbだけ1
  private static IntConst msbOnly(int t) { return IntConst.valueOf(t,1).lsh(t-1); }
// 整数定数が0ならすべてが死んでいるIntLive、0以外ならすべてが生きているIntLive
  private static IntLive allOrEmpty(IntConst c,int t) {
    return c.signum()==0 ? empty(t) : valueOf(t);
  }

}
