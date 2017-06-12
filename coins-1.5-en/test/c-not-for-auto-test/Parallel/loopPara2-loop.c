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
hir_s_extern hir_t_int  scanf ( ) ;
hir_t_int  main(  )
{
    hir_s_private hir_v_auto hir_t_int  i;
    hir_s_private hir_v_auto hir_t_int  j;
    hir_s_private hir_v_auto hir_t_int  k;
    hir_s_private hir_v_auto hir_t_int  n;
    hir_s_private hir_v_auto hir_t_float  s;
    hir_s_private hir_v_auto hir_t_float  q;
    hir_s_private hir_v_auto hir_t_float  a[100];
    hir_s_private hir_v_auto hir_t_float  b[100];
    (hir___ADDR(scanf))( (hir___DECAY("%d")),(hir___ADDR(n)));
    j = (hir_t_int  )1;
    s = (hir_t_float  )((hir_t_double  )10.0);
#pragma omp  parallel for  private(q,j) reduction(+:s)
    for ( i = (hir_t_int  )0;hir___LT(i,n); i = (hir___ADD(i,(hir_t_int  )1)))
    {
        j = (hir___ADD(hir___MULT((hir_t_int  )1,i),(hir_t_int  )1));
        q = (hir___ADD(b[j],a[i]));
        a[i] = hir___DIV((hir___ADD(b[(hir___SUB(j,(hir_t_int  )1))],b[(hir___ADD(j,(hir_t_int  )1))])),(hir_t_float  )((hir_t_int  )2));
        s = (hir___ADD((hir___ADD(q,s)),(hir_t_float  )((hir___ADD(j,(hir_t_int  )1)))));
    }
    _lab4:;
    j = (hir_t_int  )1;
    s = (hir_t_float  )((hir_t_double  )10.0);
//VAR:true-dependence:( s)
    for ( i = (hir_t_int  )0;hir___LT(i,n); i = (hir___ADD(i,(hir_t_int  )1)))
    {
        a[i] = hir___DIV((hir___ADD(b[(hir___SUB(j,(hir_t_int  )1))],s)),(hir_t_float  )((hir_t_int  )2));
        j = (hir___ADD(j,(hir_t_int  )1));
        s = (hir___ADD((hir___ADD(a[(hir___SUB(i,(hir_t_int  )1))],s)),(hir_t_float  )(j)));
    }
    _lab8:;
    return  (hir_t_int  )0;
}
