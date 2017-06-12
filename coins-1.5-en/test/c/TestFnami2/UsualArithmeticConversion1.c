int printf(char *s, ...);

typedef unsigned int u_int;
typedef unsigned long u_long;

char s5[]="%g %g %g %g %g\n";
char s4[]="%g %g %g %g\n";
char s3[]="%lu %lu %lu\n";
char s2[]="%ld %lu\n";
char s1[]="%u\n";

double  d_i=1E38 *-11,d_ui=1E38 *3100000000U,d_l=1E38 *-21L,d_ul=1E38 *3200000000UL,d_f=1E10*1E38F;
float   f_i=1E37F*-11,f_ui=1E29F*3100000000U,f_l=1E37F*-21L,f_ul=1E29F*3200000000UL;
u_long ul_i=  3000000001UL/3,ul_ui=  3000000002UL/3U,ul_l=3000000004UL/3L;
long    l_i=(-1000000000L)/3;
u_long                        l_ui=(-1000000001L)/3U;
u_int  ui_i=  3000000005U /3;

double i_d=(-12)*1E38 ,ui_d=3300000000U*1E38 ,l_d=(-22L)*1E38 ,ul_d=3400000000UL*1E38 ,f_d=1E38F*1E10;
float  i_f=(-12)*1E37F,ui_f=3300000000U*1E29F,l_f=(-22L)*1E37F,ul_f=3400000000UL*1E29F;
u_long i_ul=(-1000000002)/3UL,ui_ul=3000000007U/3UL,l_ul=(-1000000004L)/3UL;
long   i_l =(-1000000006)/3L;
u_long                        ui_l =3000000008U/3L;
u_int  i_ui=(-1000000007)/3U;

void f1d(int i,u_int ui,long l,u_long ul,float f) {
  printf(s5,1E38*i,1E38*ui,1E38*l,1E38*ul,1E10*f);
}

void f1f(int i,u_int ui,long l,u_long ul) {
  printf(s4,1E37F*i,1E29F*ui,1E37F*l,1E29F*ul);
}

void f1ul(int i,u_int ui,long l) {
  printf(s3,3000000001UL/i,3000000002UL/ui,3000000004UL/l);
}

void f1l(int i,u_int ui) {
  printf(s2,(-1000000000L)/i,(-1000000001L)/ui);
}

void f1ui(int i) {
  printf(s1,3000000005U/i);
}

void f2d(int i,u_int ui,long l,u_long ul,float f) {
  printf(s5,i*1E38,ui*1E38,l*1E38,ul*1E38,f*1E10);
}

void f2f(int i,u_int ui,long l,u_long ul) {
  printf(s4,i*1E37F,ui*1E29F,l*1E37F,ul*1E29F);
}

void f2ul(int i,u_int ui,long l) {
  printf(s3,i/3UL,ui/3UL,l/3UL);
}

void f2l(int i,u_int ui) {
  printf(s2,i/3L,ui/3L);
}

void f2ui(int i) {
  printf(s1,i/3U);
}

int main() {
  float f;
  u_long ul;
  long l;
  u_int ui;
  int i;

  printf(s5,d_i,d_ui,d_l,d_ul,d_f);
  i=-11; ui=3100000000U; l=-21L; ul=3200000000UL; f=1E38F;
  printf(s5,1E38*i,1E38*ui,1E38*l,1E38*ul,1E10*f);
  printf(s5,1E38*i,1E38*ui,1E38*l,1E38*ul,1E10*f);
  f1d(-11,3100000000U,-21L,3200000000UL,1E38F);

  printf(s4,f_i,f_ui,f_l,f_ul);
  i=-11; ui=3100000000U; l=-21L; ul=3200000000UL;
  printf(s4,1E37F*i,1E29F*ui,1E37F*l,1E29F*ul);
  printf(s4,1E37F*i,1E29F*ui,1E37F*l,1E29F*ul);
  f1f(-11,3100000000U,-21L,3200000000UL);

  printf(s3,ul_i,ul_ui,ul_l);
  i=3; ui=3U; l=3L;
  printf(s3,3000000001UL/i,3000000002UL/ui,3000000004UL/l);
  printf(s3,3000000001UL/i,3000000002UL/ui,3000000004UL/l);
  f1ul(3,3U,3L);

  printf(s2,l_i,l_ui);
  i=3; ui=3U;
  printf(s2,(-1000000000L)/i,(-1000000001L)/ui);
  printf(s2,(-1000000000L)/i,(-1000000001L)/ui);
  f1l(3,3U);

  printf(s1,ui_i);
  i=3;
  printf(s1,3000000005U/i);
  printf(s1,3000000005U/i);
  f1ui(3);

  printf(s5,i_d,ui_d,l_d,ul_d,f_d);
  i=-12; ui=3300000000U; l=-22L; ul=3400000000UL; f=1E38F;
  printf(s5,i*1E38,ui*1E38,l*1E38,ul*1E38,f*1E10);
  printf(s5,i*1E38,ui*1E38,l*1E38,ul*1E38,f*1E10);
  f2d(-12,3300000000U,-22L,3400000000UL,1E38F);

  printf(s4,i_f,ui_f,l_f,ul_f);
  i=-12; ui=3300000000U; l=-22L; ul=3400000000UL;
  printf(s4,i*1E37F,ui*1E29F,l*1E37F,ul*1E29F);
  printf(s4,i*1E37F,ui*1E29F,l*1E37F,ul*1E29F);
  f2f(-12,3300000000U,-22L,3400000000UL);

  printf(s3,i_ul,ui_ul,l_ul);
  i=-1000000002; ui=3000000007U; l=-1000000004L;
  printf(s3,i/3UL,ui/3UL,l/3UL);
  printf(s3,i/3UL,ui/3UL,l/3UL);
  f2ul(-1000000002,3000000007U,-1000000004L);

  printf(s2,i_l,ui_l);
  i=-1000000006; ui=3000000008U;
  printf(s2,i/3L,ui/3L);
  printf(s2,i/3L,ui/3L);
  f2l(-1000000006,3000000008U);

  printf(s1,i_ui);
  i=-1000000007;
  printf(s1,i/3U);
  printf(s1,i/3U);
  f2ui(-1000000007);

  return 0;
}
