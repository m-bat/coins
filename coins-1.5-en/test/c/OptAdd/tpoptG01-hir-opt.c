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

hir_s_public hir_t_int  main ( ) ;
hir_s_extern hir_t_int  printf ( ) ;
hir_t_int  main(  )
{
    hir_s_private hir_v_auto hir_t_int  a;
    hir_s_private hir_v_auto hir_t_int  b;
    hir_s_private hir_v_auto hir_t_int  c;
    hir_s_private hir_v_auto hir_t_int  d;
    hir_s_private hir_v_auto hir_t_int  e;
    hir_s_private hir_v_auto hir_t_int  f;
    hir_s_private hir_v_auto hir_t_int  _var1;
    hir_s_private hir_v_auto hir_t_int  _var3;
    hir_s_private hir_v_auto hir_t_int  _var5;
    a = (hir_t_int  )1;
    b = (hir_t_int  )5;
    _var1 = (hir___ADD(a,b));
    c = _var1;
    _var5 = (hir___ADD(a,(hir_t_int  )1));
    d = _var5;
    if (hir___CMP_GT(a,(hir_t_int  )0))
    {
        c = _var1;
        _var3 = (hir___ADD(_var1,c));
        d = _var3;
        e = _var5;
        f = _var3;
    }
    else
    {
        c = _var5;
        d = hir___MULT((hir_t_int  )3,_var5);
        e = a;
        f = (hir___ADD(e,(hir_t_int  )1));
    }
    d = (hir___ADD(d,(hir_t_int  )2));
    (hir___ADDR(printf))( (hir___DECAY("a=%d b=%d c=%d d=%d e=%d f=%d return %d\n")),a,b,c,d,e,f,(hir___ADD(d,(hir_t_int  )2)));
    return  (hir_t_int  )0;
}
