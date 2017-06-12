int printf(char *s, ...);

typedef unsigned int u_int;
typedef unsigned long u_long;

char s5[]="%g %g %g %g %11.8f\n";
char s4[]="%g %g %g %g\n";
char s3[]="%lu %lu %lu\n";
char s2[]="%ld %lu\n";
char s1[]="%u\n";

double d_i=1?10. :-50,d_ui=1?11. :3000000001U,d_l=1?12. :-52L,d_ul=1?13. :3000000003UL,d_f=1?14.00000001:14.F;
float  f_i=1?15.F:-55,f_ui=1?16.F:3000000006U,f_l=1?17.F:-57L,f_ul=1?18.F:3000000008UL;
u_long ul_i=1?19UL:-59,ul_ui=1?20UL:3000000010U,ul_l=1?21UL:-61L;
long    l_i=1?22L :-62;
u_long                  l_ui=1?23L :3000000013U;
u_int  ui_i=1?24U :-64;

double i_d=1?-70:30. ,ui_d=1?3000000021U:31. ,l_d=1?-72L:32. ,ul_d=1?3000000023UL:33. ,f_d=1?34.F:34.00000001;
float  i_f=1?-75:35.F,ui_f=1?3000000026U:36.F,l_f=1?-77L:37.F,ul_f=1?3000000028UL:38.F;
u_long i_ul=1?-79:39UL,ui_ul=1?3000000030U:40UL,l_ul=1?-81L:41UL;
long   i_l =1?-82:42L ;
u_long                 ui_l =1?3000000033U:43L;
u_int  i_ui=1?-84:44U;

void f1d(int i,u_int ui,long l,u_long ul,float f) {
  printf(s5,1?10.:i,1?11.:ui,1?12.:l,1?13.:ul,1?14.00000001:f);
}

void f1f(int i,u_int ui,long l,u_long ul) {
  printf(s4,1?15.F:i,1?16.F:ui,1?17.F:l,1?18.F:ul);
}

void f1ul(int i,u_int ui,long l) {
  printf(s3,1?19UL:i,1?20UL:ui,1?21UL:l);
}

void f1l(int i,u_int ui) {
  printf(s2,1?22L:i,1?23L:ui);
}

void f1ui(int i) {
  printf(s1,1?24U:i);
}

void f2d(int i,u_int ui,long l,u_long ul,float f) {
  printf(s5,1?i:30.,1?ui:31.,1?l:32.,1?ul:33.,1?f:34.00000001);
}

void f2f(int i,u_int ui,long l,u_long ul) {
  printf(s4,1?i:35.F,1?ui:36.F,1?l:37.F,1?ul:38.F);
}

void f2ul(int i,u_int ui,long l) {
  printf(s3,1?i:39UL,1?ui:40UL,1?l:41UL);
}

void f2l(int i,u_int ui) {
  printf(s2,1?i:42L,1?ui:43L);
}

void f2ui(int i) {
  printf(s1,1?i:44U);
}

int main() {
  float f;
  u_long ul;
  long l;
  u_int ui;
  int i;

  printf(s5,d_i,d_ui,d_l,d_ul,d_f);
  i=-50; ui=3000000001U; l=-52L; ul=3000000003UL; f=14.F;
  printf(s5,1?10.:i,1?11.:ui,1?12.:l,1?13.:ul,1?14.00000001:f);
  printf(s5,1?10.:i,1?11.:ui,1?12.:l,1?13.:ul,1?14.00000001:f);
  f1d(-50,3000000001U,-52L,3000000003UL,14.F);

  printf(s4,f_i,f_ui,f_l,f_ul);
  i=-55; ui=3000000006U; l=-57L; ul=3000000008UL;
  printf(s4,1?15.F:i,1?16.F:ui,1?17.F:l,1?18.F:ul);
  printf(s4,1?15.F:i,1?16.F:ui,1?17.F:l,1?18.F:ul);
  f1f(-55,3000000006U,-57L,3000000008UL);

  printf(s3,ul_i,ul_ui,ul_l);
  i=-59; ui=3000000010U; l=-61L;
  printf(s3,1?19UL:i,1?20UL:ui,1?21UL:l);
  printf(s3,1?19UL:i,1?20UL:ui,1?21UL:l);
  f1ul(-59,3000000010U,-61L);

  printf(s2,l_i,l_ui);
  i=-62; ui=3000000013U;
  printf(s2,1?22L:i,1?23L:ui);
  printf(s2,1?22L:i,1?23L:ui);
  f1l(-62,3000000013U);

  printf(s1,ui_i);
  i=-64;
  printf(s1,1?24U:i);
  printf(s1,1?24U:i);
  f1ui(-64);

  printf(s5,i_d,ui_d,l_d,ul_d,f_d);
  i=-70; ui=3000000021U; l=-72L; ul=3000000023UL; f=34.F;
  printf(s5,1?i:30.,1?ui:31.,1?l:32.,1?ul:33.,1?f:34.00000001);
  printf(s5,1?i:30.,1?ui:31.,1?l:32.,1?ul:33.,1?f:34.00000001);
  f2d(-70,3000000021U,-72L,3000000023UL,34.F);

  printf(s4,i_f,ui_f,l_f,ul_f);
  i=-75; ui=3000000026U; l=-77L; ul=3000000028UL;
  printf(s4,1?i:35.F,1?ui:36.F,1?l:37.F,1?ul:38.F);
  printf(s4,1?i:35.F,1?ui:36.F,1?l:37.F,1?ul:38.F);
  f2f(-75,3000000026U,-77L,3000000028UL);

  printf(s3,i_ul,ui_ul,l_ul);
  i=-79; ui=3000000030U; l=-81L;
  printf(s3,1?i:39UL,1?ui:40UL,1?l:41UL);
  printf(s3,1?i:39UL,1?ui:40UL,1?l:41UL);
  f2ul(-79,3000000030U,-81L);

  printf(s2,i_l,ui_l);
  i=-82; ui=3000000033U;
  printf(s2,1?i:42L,1?ui:43L);
  printf(s2,1?i:42L,1?ui:43L);
  f2l(-82,3000000033U);

  printf(s1,i_ui);
  i=-84;
  printf(s1,1?i:44U);
  printf(s1,1?i:44U);
  f2ui(-84);

  return 0;
}
