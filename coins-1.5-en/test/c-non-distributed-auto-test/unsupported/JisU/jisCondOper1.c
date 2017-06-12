/* jisCondOper1.c : JIS C 6.3.15 Conditional operator p.53 */

const void   *c_vp;
void         *vp;
const int    *c_ip;
volatile int *v_ip;
int          *ip;
const char   *c_cp;

int 
main()
{
  const void   *c_vp1, *c_vp2;
  volatile int *v_ip1;
  const volatile int *cv_ip1;
  const int    *c_ip1;
  void         *vp1;
  int a, b;
  
  c_vp1  = a == b ? c_vp : c_ip;
  v_ip1  = a == b ? v_ip : 0;
  cv_ip1 = a != b ? c_ip : v_ip;
  c_vp2  = a >  b ? vp   : c_cp;
  c_ip1  = a >= b ? ip   : c_ip;
  vp1    = a <  b ? vp   : ip;
  /* SF030620[ */
  printf("c_vp1=%d v_ip1=%d cv_ip1=%d c_vp2=%d c_ip1=%d vp1=%d\n"
	 ,c_vp1,v_ip1,cv_ip1,c_vp2,c_ip1,vp1);
  /* SF030620] */
  return 0;
}

