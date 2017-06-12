/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.util;

/**
 * �����������Ƥ���ӥå�(���Ѥ��ͥ��饹).
 * �����黻�ǳ���ʬ�����ͤΤ��줾��ΥӥåȤ������ɽ����
 * �黻�Ҥη�̤������Ƥ���ӥåȤ����ꤵ�줿�Ȥ��ˡ�
 * �ƥ��ڥ��ɤ������Ƥ���ӥåȤ��ɤΤ褦����³����뤫�����᥽�åɤ��󶡤��롣
 * �����Ⱦ�;�Ǥϡ�0�����㳰�������뤫�ɤ������̤ΰ����Ȥߤʤ��Ƥ��뤿�ᡢ
 * ���������ӥåȤ���0���ɤ����˱ƶ�������Τǡ������Ƥ��뤳�Ȥˤʤ롣
 * ����������ξ�硢�����0�ʤ�����������ӥåȤϻ��Ǥ��뤳�Ȥˤʤ롣
 */
public final class IntLive {

// field

// ��(�����Ƥ���ӥåȤ�1�����Ǥ���ӥåȤ�0)
  private final IntConst val;

// member type

// ���եȱ黻�Ҥδؿ����֥�������
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

/** ���ꤵ�줿�ӥåȿ����٤Ƥ������Ƥ���IntLive���֤�.
 * @exception IllegalArgumentException �ӥåȿ������Ǥʤ���� */
  public static IntLive valueOf(int size) { return new IntLive(IntConst.valueOf(size,0).bnot()); }
/** ���ꤵ�줿�ӥåȿ����٤Ƥ����Ǥ���IntLive���֤�.
 * @exception IllegalArgumentException �ӥåȿ������Ǥʤ���� */
  public static IntLive empty(int size) { return new IntLive(IntConst.valueOf(size,0)); }
/** ���ꤵ�줿���������Ω�äƤ���ӥåȤ������Ƥ���IntLive���֤�. */
  public static IntLive valueOf(IntConst val) { return new IntLive(val); }

// method

/** ���ꤵ�줿IntLive�Ȥι�ʻ���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿IntLive�Υӥåȿ����ۤʤ��� */
  public IntLive union(IntLive l) { return valueOf(val.bor(l.val)); }
/** ���ꤵ�줿IntLive�Ȥζ�����ʬ���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿IntLive�Υӥåȿ����ۤʤ��� */
  public IntLive intersection(IntLive l) { return valueOf(val.band(l.val)); }
/** �û���ξ�դ���³�����IntLive���֤�. */
  public IntLive inheritAdd() {
    // �����Ƥ���ӥåȤ�겼�̤Ϥ��٤������Ƥ���
    return valueOf(propagateRight(val));
  }
/** �û��ΰ��դ����ꤵ�줿��������ΤȤ���¾�դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritAdd(IntConst c) {
    // c�κǲ��̤�1��겼�Ǥϡ������Ƥ���ӥåȤ����Τޤ���³�����
    // c�κǲ��̤�1�ʾ�Ǥϡ������Ƥ���ӥåȰʲ��Ϥ��٤������Ƥ���
    // ��:
    // 01000010 �¤������Ƥ���ӥå�
    // 00101000 �������
    // 01111010 ��³����������Ƥ���ӥå�
    return valueOf(propagateRight(val).band(rightmostOne(c).neg()).bor(val));
  }
/** �����κ��դ���³�����IntLive���֤�. */
  public IntLive inheritSub0() { return inheritAdd(); }
/** �����α��դ���³�����IntLive���֤�. */
  public IntLive inheritSub1() { return inheritAdd(); }
/** �����α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritSub0(IntConst c1) { return inheritAdd(c1.neg()); }
/** �����κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritSub1(IntConst c0) {
    // x-y��~(x-y)�������Ƥ���ӥåȤ˴ؤ���Ʊ���Ǥ��뤳�Ȥ�����
    // ~(x-y) = y-x-1 = (-x-1)+y = ~x+y
    return inheritAdd(c0.bnot());
  }
/** �軻��ξ�դ���³�����IntLive���֤�. */
  public IntLive inheritMul() { return inheritAdd(); }
/** �軻�ΰ��դ����ꤵ�줿��������ΤȤ���¾�դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritMul(IntConst c) {
    if(c.size()!=val.size()) throw new IllegalArgumentException(this+" "+c);
    if(c.signum()==0) return empty(val.size());
    IntConst a=rightmostOne(c);
    IntConst v0=val.divu(a);
    if(a.equals(c)) return valueOf(v0); // c��2�Τ٤��ʤ饷�եȤ�Ʊ��
    // c�ǲ��̤�1���ӥå�i�ǡ�2���ܤ�1���ӥå�i+j�Ȥ���ȡ��Ѥϡ�
    //  �ӥå�0����i-1�ޤǤϾ��0
    //  �ӥå�i����i+j-1�ޤǤ������Υӥå�0����j-1�ޤǤ����Τޤ������
    //  �ӥå�i+j+k(k>=0)�Ǥϡ������Υӥå�0����k�ޤǤȥӥå�j����j+k�ޤǤαƶ��������
    // ����IntLive�Υӥå�i+j�ʾ�������Ƥ���ӥåȤ��ʤ���С�
    // �����������Ƥ���ӥåȤϡ�����IntLive�򱦤�i�������եȤ�����Τˤʤ�
    // ����IntLive�Υӥå�i+j�ʾ�������Ƥ���ӥåȤ�����С����κǾ�̤�ӥå�i+j+K�Ȥ����
    // �����������Ƥ���ӥåȤ�
    //  �ӥå�j����j+K�ޤ�1
    //  �ӥå�0����K�ޤ�1
    //  ����IntLive�򱦤�i�������ե�
    // �λ��Ĥι�ʻ�ˤʤ�
    // ��:
    //    hgfedcba
    //  x 01010010
    // -----------
    //     fedcba
    //    dcba
    //    ba
    // -----------
    //    ****cba0
    //     |||
    //     ||a,d�αƶ��������
    //     |a,b,d,e�αƶ��������
    //     a,b,c,d,e,f�αƶ��������(�����3���ܤ�1�αƶ���2���ܤ�1�αƶ��˴ޤޤ��)
    IntConst b=rightmostOne(c.sub(a).divu(a)); // c�κǲ��̤�2���ܤ�1����
    IntConst v=propagateRight(v0.divu(b));
    return valueOf(v.mul(b).bor(v).bor(v0));
  }
/** ���ʤ������κ��դ���³�����IntLive���֤�. */
  public IntLive inheritDivu0() {
    // �����Ƥ���ӥåȤ���̤Ϥ��٤������Ƥ���
    return valueOf(rightmostOne(val).neg());
  }
/** ���ʤ������α��դ���³�����IntLive���֤�. */
  public IntLive inheritDivu1() { return valueOf(val.size()); }
/** ���ʤ������α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritDivu0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    IntConst a=rightmostOne(c1);
    if(a.equals(c1)) return valueOf(val.mul(a)); // c1��2�Τ٤��ʤ饷�եȤ�Ʊ�� c1��0�ʤ��
    IntConst b=IntConst.valueOf(c1.size(),0).bnot().divu(c1); // ���κ���
    // ���ξ��0�ΥӥåȤ������Ƥ��ʤ����϶�
    if(val.band(propagateRight(b)).signum()==0) return empty(val.size());
    // c1�κǲ��̤�1��ӥå�i������IntLive�������Ƥ���ǲ��̤�ӥå�j�Ȥ����
    // �ӥå�0����i+j-1�ޤǤϻ��Ǥ��뤬���ӥå�i+j�ʾ�����ӥåȤ������Ƥ���
    // ��:
    //       �������Ͼ��0
    //       ||a,b�αƶ��������(c�αƶ��Ϥʤ�)
    //       |||
    //       |||   a,b,c,d,e,f�αƶ��������
    //       |||   |a,b,c,d,e,f,g�αƶ��������
    //       |||   ||
    //     __________ h�Ͼ��˱ƶ����ʤ�
    // 110 ) abcdefgh
    return valueOf(a.mul(rightmostOne(val)).neg());
  }
/** ���ʤ������κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritDivu1(IntConst c0) {
    if(c0.size()!=val.size()) throw new IllegalArgumentException(this+" "+c0);
    return valueOf(val.size());
  }
/** ���Ĥ������κ��դ���³�����IntLive���֤�. */
  public IntLive inheritDivs0() { return allOrEmpty(val,val.size()); }
/** ���Ĥ������α��դ���³�����IntLive���֤�. */
  public IntLive inheritDivs1() { return valueOf(val.size()); }
/** ���Ĥ������α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritDivs0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    if(c1.signum()==0) return empty(val.size()); // c1��0�ʤ��
    if(val.signum()==0) return this; // ����IntLive�����ʤ餽�Τޤ�
    final IntConst zero=IntConst.valueOf(c1.size(),0);
    final IntConst one=IntConst.valueOf(c1.size(),1);
    final IntConst msb=one.lsh(c1.size()-1);
    final IntConst all=zero.bnot();
    if(c1.equals(one)) return this; // c1��1�ʤ餽�Τޤ�
    if(c1.equals(all)) return inheritNeg(); // c1��-1�ʤ�NEG��Ʊ��
    IntConst c=c1.signum()>=0 ? c1 : c1.neg();
    IntConst a=rightmostOne(c);
    if(a.equals(c)) { // c1��2�Τ٤����������ȿž�ΤȤ�
      // c1����(1<<i)�ʤ顢
      // ���Υӥå�i��겼�Υӥå�k�ϡ�������Υӥå�i+k�ʲ������ӥåȤ�msb�αƶ��������
      // ���Υӥå�i�ʾ�ϡ�����������ӥåȤαƶ��������
      // ��: c1=001000�ΤȤ�
      // �����(10��)  -8       -7       -33       -1         0     -128       127       -1
      // �����  11111000 11111001  11011111 11111111  00000000 10000000  01111111 11111111
      // ��      11111111 00000000  11111100 00000000  00000000 11110000  00001111 00000000
      IntConst v=propagateRight(val).mul(a).add(a).sub(one).bor(msb);
      // c1����((-1)<<i)�ʤ顢c1�����ΤȤ��Ȼ��Ƥ��뤬���ʲ��������ۤʤ�
      // ���Τ�s�ӥåȤȤ���ȡ����Υӥå�s-i�ʾ�ϡ�������Υӥå�i��겼�αƶ�������ʤ�
      // �ʤ���c1��msb�Τߤʤ顢����0��1����ˤʤ뤳�ȤϤʤ��Τǡ����̰�������
      // ��: c1=111000�ΤȤ�
      // �����(10��)  -8       -7        33        1        -1      127      -127        1
      // �����  11111000 11111001  00100001 00000001  11111111 01111111  10000001 00000001
      // ��      00000001 00000000  11111100 00000000  00000000 11110001  00001111 00000000
      // �����(10��)-128     -127
      // �����  10000000 10000001
      // ��      00010000 00001111
      if(c1.signum()<0 && val.band(all.divu(a)).signum()==0) {
        v=a.equals(msb) ? zero : v.band(a.neg());
      }
      return valueOf(v);
    }
    IntConst v=all;
    if(a.equals(one)) { // c1������ΤȤ�
      // c1����ʤ顢�����㳰������ơ����ӥåȤ��ƶ�����
      if(c1.signum()>0) {
        // c1�����ʤ顢�ǲ��̤�1��ӥå�i��2���ܤ�1��ӥå�j��
        // ���κ���(msb�Τߤ�c�����ʤ�����)��K�ӥåȤȤ����
        // ���Τ��٤ƤΥӥåȤϡ������㳰������ơ�������Υӥå�j�ʾ�����ӥåȤαƶ��������
        // �ӥå�K��겼�Υӥå�k�Ϥ���ˡ�������Υӥå�k�ʾ�αƶ��������
        v=rightmostOne(c.sub(one)).neg();
        IntConst b=msb.divu(c); // ���κ���
        if(val.band(propagateRight(b)).signum()!=0) v=v.bor(rightmostOne(val).neg());
      }
      // ��κ������c�ˤ���;��-c+1�ΤȤ�������lsb���������msb�αƶ�������ʤ�
      if(val.equals(one) && c.add(msb.mods(c)).equals(one)) v=v.band(msb.bnot());
      return valueOf(v);
    }
    // c1�����������ΤȤ��ϡ����ӥåȤ��ƶ�����
    if(c1.signum()<0) { // c1����������ΤȤ�
      IntConst b=msb.divu(c); // ���κ���
      // c�κǲ��̤�1��ӥå�i�����κ���(msb�Τߤ�c�����ʤ�����)��K�ӥåȤȤ����
      // ���Υӥå�K�ʾ�ϡ�������Υӥå�i��겼�αƶ�������ʤ�
      if(val.band(propagateRight(b)).signum()==0) v=v.band(a.neg());
    }
    return valueOf(v);
  }
/** ���Ĥ������κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritDivs1(IntConst c0) { return inheritDivu1(c0); }
/** ���ʤ���;�κ��դ���³�����IntLive���֤�. */
  public IntLive inheritModu0() { return allOrEmpty(val,val.size()); }
/** ���ʤ���;�α��դ���³�����IntLive���֤�. */
  public IntLive inheritModu1() { return valueOf(val.size()); }
/** ���ʤ���;�α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritModu0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    if(c1.signum()==0) return empty(val.size()); // c1��0�ʤ��
    IntConst a=rightmostOne(c1);
    // c1��2�Τ٤��ʤ������Ѥ�Ʊ��
    if(a.equals(c1)) return valueOf(val.band(a.sub(IntConst.valueOf(a.size(),1))));
    // c1�κǲ��̤�1��ӥå�i���Ǿ�̤�1��ӥå�j�Ȥ����
    // ��;�Υӥå�i��겼�ϡ�����������Τޤ������
    // ��;�Υӥå�i����j�ޤǤϡ�������Υӥå�i�ʾ�����ӥåȤαƶ��������
    // ��;�Υӥå�j����̤Ͼ��0
    // ��:
    //     __________ 
    // 110 ) abcdefgh
    //
    //           ----
    //           ***h
    //            **0
    //           ----
    //            **h
    //           |||
    //           ||a,b,c,d,e,f,g�αƶ��������
    //           |a,b,c,d,e,f,g�αƶ��������
    //           �������̤�0
    IntConst b=propagateRight(c1).band(a.neg());
    if(val.band(b).signum()==0) return valueOf(a.sub(IntConst.valueOf(a.size(),1)).band(val));
    return valueOf(a.neg().bor(val));
  }
/** ���ʤ���;�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritModu1(IntConst c0) { return inheritDivu1(c0); }
/** ���Ĥ���;�κ��դ���³�����IntLive���֤�. */
  public IntLive inheritMods0() { return inheritModu0(); }
/** ���Ĥ���;�α��դ���³�����IntLive���֤�. */
  public IntLive inheritMods1() { return valueOf(val.size()); }
/** ���Ĥ���;�α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritMods0(IntConst c1) {
    if(c1.size()!=val.size()) throw new IllegalArgumentException(this+" "+c1);
    if(c1.signum()==0) return empty(val.size()); // c1��0�ʤ��
    final IntConst one=IntConst.valueOf(c1.size(),1);
    final IntConst msb=one.lsh(c1.size()-1);
    if(c1.signum()<0) c1=c1.neg(); // ���Ĥ���;��c1�����˱ƶ�����ʤ�
    if(c1.equals(one)) return empty(c1.size()); // c1��1�ʤ��;�Ͼ��0
    IntConst a=rightmostOne(c1);
    if(a.equals(c1)) { // c1��2�Τ٤�(1<<i)�ΤȤ�
      IntConst v=val.band(a.sub(one)); // �ӥå�i��겼�Ϥ��Τޤ������
      // �ӥå�i�ʾ�Τ��٤ƤΥӥåȤϡ��ӥå�i��겼�����ӥåȤ�msb�αƶ��������
      // ��: c1=00010000�ΤȤ�
      // �����(10��) -16      -14         4     -124
      // �����  11110000 11110010  00000100 10000100
      // ��;    00000000 11110010  00000100 11110100
      if(val.band(a.neg()).signum()!=0) v=v.bor(a.sub(one)).bor(msb);
      return valueOf(v);
    }
    // c1�κǲ��̤�1��ӥå�i�Ȥ����
    // ��;�Υӥå�i��겼�ϡ�����������Τޤ������
    // ��;�Υӥå�i�ʾ�ϡ������㳰������ơ�������Υӥå�i�ʾ�����ӥåȤαƶ��������
    // ��: c1=00010100(10�ʤ�20)�ΤȤ�
    // �����(10��) -20      -84
    // �����  11101100 10101100
    // ��;    00000000 11111100
    IntConst v=val;
    if(val.band(a.neg()).signum()!=0) v=v.bor(a.neg());
    // c1�����(i=0)�ʤ顢��;�Υӥå�i�Ĥޤ�lsb���������msb�αƶ�������ʤ���礬����
    // ����ϡ���κ�����ξ�;��-c1+1�ΤȤ��Ǥ���
    // ��: c1=000011�ΤȤ�
    // �����  000000 000001 000010 000011 100000 100001 100010 100011
    // ��;    000000 000001 000010 000000 111110 111111 000000 111110
    if(val.equals(one) && a.equals(one) && c1.add(msb.mods(c1)).equals(one)) v=v.band(msb.bnot());
    // ��;�Τ���-c1��Ω�äƤ���ӥåȤϡ�������Υӥå�i��겼�αƶ��������
    // ���μ�αƶ��Ͼ�;��0�����Ѳ�����Ȥ��˸¤�
    // ��: c1=00010100(10�ʤ�20)�ΤȤ�
    // �����(10��) -20      -19       -16      -15
    // �����  11101100 11101101  11111000 11111001
    // ��;    00000000 11101101  11111000 11111001
    if(val.band(c1.neg()).signum()!=0) v=v.bor(a.sub(one));
    return valueOf(v);
  }
/** ���Ĥ���;�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritMods1(IntConst c0) { return inheritDivu1(c0); }
/** �����Ѥ�ξ�դ���³�����IntLive���֤�. */
  public IntLive inheritBand() { return this; }
/** �����Ѥΰ��դ����ꤵ�줿��������ΤȤ���¾�դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritBand(IntConst c) { return valueOf(val.band(c)); }
/** �����¤�ξ�դ���³�����IntLive���֤�. */
  public IntLive inheritBor() { return this; }
/** �����¤ΰ��դ����ꤵ�줿��������ΤȤ���¾�դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritBor(IntConst c) { return valueOf(val.band(c.bnot())); }
/** ��¾Ū�����¤�ξ�դ���³�����IntLive���֤�. */
  public IntLive inheritBxor() { return this; }
/** ��¾Ū�����¤ΰ��դ����ꤵ�줿��������ΤȤ���¾�դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritBxor(IntConst c) {
    if(c.size()!=val.size()) throw new IllegalArgumentException(this+" "+c);
    return this;
  }
/** �����եȤκ��դ���³�����IntLive���֤�. */
  public IntLive inheritLsh0() { return inheritAdd(); }
/** �����եȤα���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�.
 * @exception IllegalArgumentException �ӥåȿ������Ǥʤ���� */
  public IntLive inheritLsh1(int t) { return allOrEmpty(val,t); }
/** �����եȤα��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritLsh0(IntConst c1) { return valueOf(val.rshu(c1)); }
/** �����եȤκ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritLsh1(IntConst c0,int t) { return inheritShift1(SHIFT_LSH,c0,t); }
/** ���ʤ������եȤκ��դ���³�����IntLive���֤�. */
  public IntLive inheritRshu0() {
    return valueOf(rightmostOne(val).neg());
  }
/** ���ʤ������եȤα���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�.
 * @exception IllegalArgumentException �ӥåȿ������Ǥʤ���� */
  public IntLive inheritRshu1(int t) { return allOrEmpty(val,t); }
/** ���ʤ������եȤα��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritRshu0(IntConst c1) { return valueOf(val.lsh(c1)); }
/** ���ʤ������եȤκ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritRshu1(IntConst c0,int t) { return inheritShift1(SHIFT_RSHU,c0,t); }
/** ���Ĥ������եȤκ��դ���³�����IntLive���֤�. */
  public IntLive inheritRshs0() { return inheritRshu0(); }
/** ���Ĥ������եȤα���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�.
 * @exception IllegalArgumentException �ӥåȿ������Ǥʤ���� */
  public IntLive inheritRshs1(int t) { return allOrEmpty(val.band(msbOnly(t).bnot()),t); }
/** ���Ĥ������եȤα��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritRshs0(IntConst c1) {
    IntConst v=val.lsh(c1);
    // msb�αƶ��Τ���ӥåȤ������Ƥ���ʤ顢msb�������Ƥ���
    if(!v.rshu(c1).equals(val)) v=v.bor(msbOnly(val.size()));
    return valueOf(v);
  }
/** ���Ĥ������եȤκ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�.
 * @exception IllegalArgumentException ����IntLive�Ȼ��ꤵ�줿��������Υӥåȿ����ۤʤ��� */
  public IntLive inheritRshs1(IntConst c0,int t) { return inheritShift1(SHIFT_RSHS,c0,t); }
// ���ꤵ�줿���եȤκ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�
  private IntLive inheritShift1(Shift op,IntConst c0,int t) {
    final int size=val.size(),size2=size<<1;
    if(c0.size()!=size) throw new IllegalArgumentException(this+" "+c0);
    int v=0,s=0;
    // ���դ�2^(r-1)�ӥåȤ�Ķ��2^r�ӥåȰʲ��ʤ顢���դ򲼤���r+1=s�ӥåȤ֤����Ѥ˵���
    // r+1>t�ʤ�t�ӥåȤ֤�Ǥ褤
    for(int m=1;s<t && m!=0 && m-0x80000000<size2-0x80000000;m<<=1,s++) {
      IntConst a=IntConst.valueOf(size,0);
      for(int n=0;n<size;n=((n|m)+1)&~m) a=a.bor(op.eval(c0,n).bxor(op.eval(c0,n+m)));
      if(a.band(val).signum()!=0) v|=m;
    }
    // �ӥå�s�ʾ�ϡ����ޤǵ�᤿�Ǿ�̥ӥåȤ�Ʊ��
    return valueOf(IntConst.valueOf(s,v).convsx(t));
  }
/** ���ȿž�Υ��ڥ��ɤ���³�����IntLive���֤�. */
  public IntLive inheritNeg() { return inheritAdd(); }
/** ��������Υ��ڥ��ɤ���³�����IntLive���֤�. */
  public IntLive inheritBnot() { return this; }
/** ==��ξ��(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTsteq(int t) {
    return allOrEmpty(val.band(IntConst.valueOf(val.size(),1)),t);
  }
/** ==�ΰ��դ����ꤵ�줿��������ΤȤ���¾�դ���³�����IntLive���֤�. */
  public IntLive inheritTsteq(IntConst c) {
    return allOrEmpty(val.band(IntConst.valueOf(val.size(),1)),c.size());
  }
/** !=��ξ��(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstne(int t) { return inheritTsteq(t); }
/** !=�ΰ��դ����ꤵ�줿��������ΤȤ���¾�դ���³�����IntLive���֤�. */
  public IntLive inheritTstne(IntConst c) { return inheritTsteq(c); }
/** &lt;(���ʤ�)�α���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstltu0(int t) { return inheritTsteq(t); }
/** &lt;(���ʤ�)�κ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstltu1(int t) { return inheritTsteq(t); }
/** &lt;(���ʤ�)�α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstltu0(IntConst c1) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c1.size());
    // ���դκǲ��̤�1��겼�ϻ��Ǥ���
    return valueOf(rightmostOne(c1).neg());
  }
/** &lt;(���ʤ�)�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstltu1(IntConst c0) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c0.size());
    // ����+1�κǲ��̤�1��겼�ϻ��Ǥ���
    // ���դ����ӥå�1�ʤ��åץ��饦��ɤ��뤬����ʤ�
    return valueOf(rightmostOne(c0.add(IntConst.valueOf(c0.size(),1))).neg());
  }
/** &gt;(���ʤ�)�α���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstgtu0(int t) { return inheritTsteq(t); }
/** &gt;(���ʤ�)�κ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstgtu1(int t) { return inheritTsteq(t); }
/** &gt;(���ʤ�)�α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstgtu0(IntConst c1) { return inheritTstltu1(c1); }
/** &gt;(���ʤ�)�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstgtu1(IntConst c0) { return inheritTstltu0(c0); }
/** &lt;=(���ʤ�)�α���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstleu0(int t) { return inheritTsteq(t); }
/** &lt;=(���ʤ�)�κ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstleu1(int t) { return inheritTsteq(t); }
/** &lt;=(���ʤ�)�α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstleu0(IntConst c1) { return inheritTstltu1(c1); }
/** &lt;=(���ʤ�)�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstleu1(IntConst c0) { return inheritTstltu0(c0); }
/** &gt;=(���ʤ�)�α���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstgeu0(int t) { return inheritTsteq(t); }
/** &gt;=(���ʤ�)�κ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstgeu1(int t) { return inheritTsteq(t); }
/** &gt;=(���ʤ�)�α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstgeu0(IntConst c1) { return inheritTstltu0(c1); }
/** &gt;=(���ʤ�)�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstgeu1(IntConst c0) { return inheritTstltu1(c0); }
/** &lt;(���Ĥ�)�α���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstlts0(int t) { return inheritTsteq(t); }
/** &lt;(���Ĥ�)�κ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstlts1(int t) { return inheritTsteq(t); }
/** &lt;(���Ĥ�)�α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstlts0(IntConst c1) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c1.size());
    // ����+msb�κǲ��̤�1��겼�ϻ��Ǥ���
    return valueOf(rightmostOne(c1.add(msbOnly(c1.size()))).neg());
  }
/** &lt;(���Ĥ�)�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstlts1(IntConst c0) {
    if(val.band(IntConst.valueOf(val.size(),1)).signum()==0) return empty(c0.size());
    // ����+msb+1�κǲ��̤�1��겼�ϻ��Ǥ���
    return valueOf(rightmostOne(c0.sub(msbOnly(c0.size()).bnot())).neg());
  }
/** &gt;(���Ĥ�)�α���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstgts0(int t) { return inheritTsteq(t); }
/** &gt;(���Ĥ�)�κ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstgts1(int t) { return inheritTsteq(t); }
/** &gt;(���Ĥ�)�α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstgts0(IntConst c1) { return inheritTstlts1(c1); }
/** &gt;(���Ĥ�)�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstgts1(IntConst c0) { return inheritTstlts0(c0); }
/** &lt;=(���Ĥ�)�α���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstles0(int t) { return inheritTsteq(t); }
/** &lt;=(���Ĥ�)�κ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstles1(int t) { return inheritTsteq(t); }
/** &lt;=(���Ĥ�)�α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstles0(IntConst c1) { return inheritTstlts1(c1); }
/** &lt;=(���Ĥ�)�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstles1(IntConst c0) { return inheritTstlts0(c0); }
/** &gt;=(���Ĥ�)�α���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstges0(int t) { return inheritTsteq(t); }
/** &gt;=(���Ĥ�)�κ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritTstges1(int t) { return inheritTsteq(t); }
/** &gt;=(���Ĥ�)�α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstges0(IntConst c1) { return inheritTstlts0(c1); }
/** &gt;=(���Ĥ�)�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritTstges1(IntConst c0) { return inheritTstlts1(c0); }
/** �����ĥ�Υ��ڥ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�.
 * @exception IllegalArgumentException ���ꤵ�줿�ӥåȿ��Τۤ����礭�����ޤ������Ǥʤ���� */
  public IntLive inheritConvzx(int t) { return valueOf(val.convit(t)); }
/** ����ĥ�Υ��ڥ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�.
 * @exception IllegalArgumentException ���ꤵ�줿�ӥåȿ��Τۤ����礭�����ޤ������Ǥʤ���� */
  public IntLive inheritConvsx(int t) {
    IntConst v=val.convit(t);
    // ����ĥ����������ʬ�������Ƥ���ʤ顢msb�������Ƥ���
    IntConst a=IntConst.valueOf(val.size(),1).lsh(t).neg();
    if(val.band(a).signum()!=0) v=v.bor(msbOnly(t));
    return valueOf(v);
  }
/** �̾��Υ��ڥ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�.
 * @exception IllegalArgumentException ���ꤵ�줿�ӥåȿ��Τۤ������������ */
  public IntLive inheritConvit(int t) { return valueOf(val.convzx(t)); }
/** IFTHENELSE�κ���(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritIfthenelse0(int t) { return allOrEmpty(val,t); }
/** IFTHENELSE�����դ���³�����IntLive���֤�. */
  public IntLive inheritIfthenelse1() { return this; }
/** IFTHENELSE�α��դ���³�����IntLive���֤�. */
  public IntLive inheritIfthenelse2() { return this; }
/** IFTHENELSE�����դ����ꤵ�줿��������ΤȤ�������(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritIfthenelse0_1(int t,IntConst c1) { return inheritIfthenelse0(t); }
/** IFTHENELSE�α��դ����ꤵ�줿��������ΤȤ�������(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritIfthenelse0_2(int t,IntConst c2) { return inheritIfthenelse0(t); }
/** IFTHENELSE�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritIfthenelse1_0(IntConst c0) { return c0.signum()!=0 ? this : empty(val.size()); }
/** IFTHENELSE�α��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritIfthenelse1_2(IntConst c2) { return inheritIfthenelse1(); }
/** IFTHENELSE�κ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritIfthenelse2_0(IntConst c0) { return c0.signum()==0 ? this : empty(val.size()); }
/** IFTHENELSE�����դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritIfthenelse2_1(IntConst c1) { return inheritIfthenelse2(); }
/** IFTHENELSE�����դȱ��դ����ꤵ�줿��������ΤȤ�������(���ꤵ�줿�ӥåȿ�)����³�����IntLive���֤�. */
  public IntLive inheritIfthenelse0(int t,IntConst c1,IntConst c2) {
    return allOrEmpty(c1.bxor(c2).band(val),t);
  }
/** IFTHENELSE�κ��դȱ��դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritIfthenelse1(IntConst c0,IntConst c2) { return inheritIfthenelse1_0(c0); }
/** IFTHENELSE�κ��դ����դ����ꤵ�줿��������ΤȤ������դ���³�����IntLive���֤�. */
  public IntLive inheritIfthenelse2(IntConst c0,IntConst c1) { return inheritIfthenelse2_0(c0); }
/** ����IntLive�������Ƥ���ӥåȤ�Ω�äƤ�������������֤�. */
  public IntConst intConstValue() { return val; }
/** ����IntLive�Ȼ��ꤵ�줿���֥������Ȥ����������ɤ������֤�. */
  public boolean equals(Object o) {
    return o==this || o instanceof IntLive && val.equals((((IntLive)o).val));
  }
/** ����IntLive�Υϥå��女���ɤ��֤�. */
  public int hashCode() { return val.hashCode(); }
/** ����IntLive��ʸ����ɽ�����֤�. */
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
// �ǲ��̤�1
  private static IntConst rightmostOne(IntConst c) { return c.neg().band(c); }
// �Ǿ�̤�1��겼�̤򤹤٤�1��
  private static IntConst propagateRight(IntConst c) {
    for(int i=1;i>=0 && i<c.size();i<<=1) c=c.bor(c.rshu(i));
    return c;
  }
// msb����1
  private static IntConst msbOnly(int t) { return IntConst.valueOf(t,1).lsh(t-1); }
// ���������0�ʤ餹�٤Ƥ����Ǥ���IntLive��0�ʳ��ʤ餹�٤Ƥ������Ƥ���IntLive
  private static IntLive allOrEmpty(IntConst c,int t) {
    return c.signum()==0 ? empty(t) : valueOf(t);
  }

}
