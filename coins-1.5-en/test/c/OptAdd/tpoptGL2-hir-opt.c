#define hir_t_short   short 
#define hir___XOR(a,b) (a) ^ (b)
#define hir__StringCopy(a,b,c) memcpy(a,b,c)
#define hir_v_auto   
#define hir_s_compile_unit   
#define hir_v_static  static 
#define hir___NULL 
#define hir_s_protected  
#define hir_t_offset  int 
#define hir___NOT(a) ~(a)
#define hir___AND(a,b) (a) & (b)
#define hir___CMP_GE(a,b) (a) >= (b)
#define hir___UNDECAY(a,b) (a)
#define hir___ENCLOSE(a)  (a)
#define hir___DIV(a,b) (a) / (b)
#define hir__LowerBound(a,b) (a) - (b)
#define hir___CONTENTS *
#define hir___ADDR(a) &(a)
#define hir___DECAY(a) (a)
#define hir_t_long_double   long double 
#define hir_t_char  char 
#define hir_t_u_short  unsigned short 
#define hir_v_register  register 
#define hir___SHIFT_R(a,b) (a) >> (b)
#define hir___ADD(a,b) (a) + (b)
#define hir___MOD(a,b) (a) % (b)
#define hir_t_u_long  unsigned long 
#define hir_t_int  int 
#define hir___LT(a,b) (a) < (b)
#define hir___NEG(a) -(a)
#define hir_t_float  float 
#define hir___OR(a,b) (a) | (b)
#define hir___MULT(a,b) (a) * (b)
#define hir___SHIFT_LL(a,b) (a) << (b)
#define hir_t_u_long_long  unsigned long long 
#define hir___SHIFT_RL(a,b) (a) >> (b)
#define hir_t_void  void 
#define hir___CMP_EQ(a,b) (a) == (b)
#define hir___LE(a,b) (a) <= (b)
#define hir_s_private  
#define hir__Pointer(a) ((char*)a)
#define hir_t_double  double 
#define hir_s_public  
#define hir___SUB(a,b) (a) - (b)
#define hir_t_long_long  long long 
#define hir_t_bool  int 
#define hir_t_long  long 
#define hir_s_extern  extern 
#define hir___CMP_NE(a,b) (a) != (b)
#define hir_t_addr  
#define hir_t_u_int  unsigned int 
#define hir_t_u_char  unsigned char 
#define hir___CMP_GT(a,b) (a) > (b)

hir_s_extern hir_t_int  printf ( hir_t_char  *  , ... ) ;
hir_s_public hir_t_int  ff ( hir_t_int   ) ;
hir_s_public hir_t_int  ga;
hir_s_public hir_t_int  gb;
hir_s_public hir_t_int  gc;
hir_s_public hir_t_int  gd;
hir_s_public hir_t_int  ge;
hir_s_public hir_t_int  gf;
hir_s_public hir_t_int  gh;
hir_s_public hir_t_int  main ( ) ;
hir_t_int  main(  )
{
    hir_s_private hir_v_auto hir_t_int  a;
    hir_s_private hir_v_auto hir_t_int  b;
    hir_s_private hir_v_auto hir_t_int  c;
    hir_s_private hir_v_auto hir_t_int  d;
    hir_s_private hir_v_auto hir_t_int  e;
    hir_s_private hir_v_auto hir_t_int  f;
    hir_s_private hir_v_auto hir_t_int  h;
    hir_s_private hir_v_auto hir_t_int  _var1;
    hir_s_private hir_v_auto hir_t_int  _var3;
    hir_s_private hir_v_auto hir_t_int  _var5;
    hir_s_private hir_v_auto hir_t_int  _var7;
    hir_s_private hir_v_auto hir_t_int  _var9;
    hir_s_private hir_v_auto hir_t_int  _var11;
    hir_s_private hir_v_auto hir_t_int  _var13;
    hir_s_private hir_v_auto hir_t_int  _var15;
    hir_s_private hir_v_auto hir_t_int  _var17;
    hir_s_private hir_v_auto hir_t_int  _var19;
    hir_s_private hir_v_auto hir_t_int  _var21;
    a = (hir___ADDR(ff))( (hir_t_int  )1);
    b = (hir___ADDR(ff))( (hir_t_int  )5);
    ga = (hir___ADDR(ff))( (hir_t_int  )1);
    gb = (hir___ADDR(ff))( (hir_t_int  )5);
    c = (hir___ADD(a,b));
    d = (hir___ADD(a,(hir_t_int  )1));
    _var1 = ga;
    _var3 = gb;
    gc = (hir___ADD(_var1,_var3));
    gd = (hir___ADD(_var3,_var1));
    if (hir___CMP_NE(a,(hir_t_int  )0))
    {
        _var5 = (hir___ADD(a,b));
        c = _var5;
        _var7 = (hir___ADD(_var5,c));
        d = _var7;
        e = (hir___ADD(a,(hir_t_int  )1));
        f = _var7;
        _var1 = ga;
        _var3 = gb;
        _var9 = (hir___ADD(_var1,_var3));
        gc = _var9;
        a = (hir___ADD(a,_var1));
        _var11 = gc;
        gd = (hir___ADD(_var9,_var11));
        _var13 = gd;
        d = (hir___ADD(d,_var13));
        ge = (hir___ADD(_var1,(hir___ADDR(ff))( (hir_t_int  )1)));
        gf = (hir___ADD((hir___ADD(_var1,_var3)),_var11));
    }
    else
    {
        c = (hir___ADD(a,(hir_t_int  )1));
        d = hir___MULT((hir_t_int  )3,(hir___ADD(a,(hir_t_int  )1)));
        e = a;
        f = (hir___ADD(e,(hir_t_int  )1));
        _var1 = ga;
        gc = (hir___ADD(_var1,(hir___ADDR(ff))( (hir_t_int  )1)));
        _var11 = gc;
        c = (hir___ADD(c,_var11));
        gd = hir___MULT((hir_t_int  )3,(hir___ADD(_var1,(hir_t_int  )1)));
        ge = _var1;
        _var15 = ge;
        gf = (hir___ADD(_var15,(hir_t_int  )1));
    }
    d = (hir___ADD(d,(hir_t_int  )2));
    h = (hir___ADD((hir___ADD(a,b)),c));
    gh = hir___MULT((hir_t_int  )3,(hir___ADD(ga,(hir_t_int  )1)));
    (hir___ADDR(printf))( (hir_t_char  *  )((hir___DECAY("a=%d b=%d c=%d d=%d e=%d f=%d h=%d return %d\n"))),a,b,c,d,e,f,h,(hir___ADD(d,(hir_t_int  )2)));
    (hir___ADDR(printf))( (hir_t_char  *  )((hir___DECAY("ga=%d gb=%d gc=%d gd=%d ge=%d gf=%d gh=%d \n"))),ga,gb,gc,gd,ge,gf,gh);
    return  (hir_t_int  )0;
}
hir_t_int  ff( hir_t_int  p )
{
    hir_s_private hir_v_auto hir_t_int  _var23;
    hir_s_private hir_v_auto hir_t_int  _var25;
    hir_s_private hir_v_auto hir_t_int  _var27;
    hir_s_private hir_v_auto hir_t_int  _var29;
    hir_s_private hir_v_auto hir_t_int  _var31;
    gf = (hir_t_int  )10;
    _var23 = gf;
    ge = _var23;
    _var25 = ge;
    gd = _var25;
    _var27 = gd;
    gc = _var27;
    _var29 = gc;
    gb = _var29;
    _var31 = gb;
    ga = _var31;
    return  p;
}
