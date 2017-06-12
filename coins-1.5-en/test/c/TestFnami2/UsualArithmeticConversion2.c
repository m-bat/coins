int printf(char *s, ...);

typedef unsigned int u_int;
typedef unsigned long u_long;

char s1[]="%d\n";
char s2[]="%d %d\n";
char s3[]="%d %d %d\n";
char s4[]="%d %d %d %d\n";
char s5[]="%d %d %d %d %d\n";

int d_i=10. <-50,d_ui=11. <3000000001U,d_l=12. <-52L,d_ul=13. <3000000003UL,d_f=14.00000001<=14.F;
int f_i=15.F<-55,f_ui=16.F<3000000006U,f_l=17.F<-57L,f_ul=18.F<3000000008UL;
int ul_i=19UL<-59,ul_ui=20UL<3000000010U,ul_l=21UL<-61L;
int  l_i=22L <-62, l_ui=23L <3000000013U;
int ui_i=24U <-64;

int i_d=-70<30. ,ui_d=3000000021U<31. ,l_d=-72L<32. ,ul_d=3000000023UL<33. ,f_d=34.F<34.00000001;
int i_f=-75<35.F,ui_f=3000000026U<36.F,l_f=-77L<37.F,ul_f=3000000028UL<38.F;
int i_ul=-79<39UL,ui_ul=3000000030U<40UL,l_ul=-81L<41UL;
int i_l =-82<42L ,ui_l =3000000033U<43L;
int i_ui=-84<44U;

void f1d(int i,u_int ui,long l,u_long ul,float f) {
  printf(s5,10.<i,11.<ui,12.<l,13.<ul,14.00000001<=f);
}

void f1f(int i,u_int ui,long l,u_long ul) {
  printf(s4,15.F<i,16.F<ui,17.F<l,18.F<ul);
}

void f1ul(int i,u_int ui,long l) {
  printf(s3,19UL<i,20UL<ui,21UL<l);
}

void f1l(int i,u_int ui) {
  printf(s2,22L<i,23L<ui);
}

void f1ui(int i) {
  printf(s1,24U<i);
}

void f2d(int i,u_int ui,long l,u_long ul,float f) {
  printf(s5,i<30.,ui<31.,l<32.,ul<33.,f<34.00000001);
}

void f2f(int i,u_int ui,long l,u_long ul) {
  printf(s4,i<35.F,ui<36.F,l<37.F,ul<38.F);
}

void f2ul(int i,u_int ui,long l) {
  printf(s3,i<39UL,ui<40UL,l<41UL);
}

void f2l(int i,u_int ui) {
  printf(s2,i<42L,ui<43L);
}

void f2ui(int i) {
  printf(s1,i<44U);
}

int main() {
  float f;
  u_long ul;
  long l;
  u_int ui;
  int i;

  printf(s5,d_i,d_ui,d_l,d_ul,d_f);
  i=-50; ui=3000000001U; l=-52L; ul=3000000003UL; f=14.F;
  printf(s5,10.<i,11.<ui,12.<l,13.<ul,14.00000001<=f);
  printf(s5,10.<i,11.<ui,12.<l,13.<ul,14.00000001<=f);
  f1d(-50,3000000001U,-52L,3000000003UL,14.F);

  printf(s4,f_i,f_ui,f_l,f_ul);
  i=-55; ui=3000000006U; l=-57L; ul=3000000008UL;
  printf(s4,15.F<i,16.F<ui,17.F<l,18.F<ul);
  printf(s4,15.F<i,16.F<ui,17.F<l,18.F<ul);
  f1f(-55,3000000006U,-57L,3000000008UL);

  printf(s3,ul_i,ul_ui,ul_l);
  i=-59; ui=3000000010U; l=-61L;
  printf(s3,19UL<i,20UL<ui,21UL<l);
  printf(s3,19UL<i,20UL<ui,21UL<l);
  f1ul(-59,3000000010U,-61L);

  printf(s2,l_i,l_ui);
  i=-62; ui=3000000013U;
  printf(s2,22L<i,23L<ui);
  printf(s2,22L<i,23L<ui);
  f1l(-62,3000000013U);

  printf(s1,ui_i);
  i=-64;
  printf(s1,24U<i);
  printf(s1,24U<i);
  f1ui(-64);

  printf(s5,i_d,ui_d,l_d,ul_d,f_d);
  i=-70; ui=3000000021U; l=-72L; ul=3000000023UL; f=34.F;
  printf(s5,i<30.,ui<31.,l<32.,ul<33.,f<34.00000001);
  printf(s5,i<30.,ui<31.,l<32.,ul<33.,f<34.00000001);
  f2d(-70,3000000021U,-72L,3000000023UL,34.F);

  printf(s4,i_f,ui_f,l_f,ul_f);
  i=-75; ui=3000000026U; l=-77L; ul=3000000028UL;
  printf(s4,i<35.F,ui<36.F,l<37.F,ul<38.F);
  printf(s4,i<35.F,ui<36.F,l<37.F,ul<38.F);
  f2f(-75,3000000026U,-77L,3000000028UL);

  printf(s3,i_ul,ui_ul,l_ul);
  i=-79; ui=3000000030U; l=-81L;
  printf(s3,i<39UL,ui<40UL,l<41UL);
  printf(s3,i<39UL,ui<40UL,l<41UL);
  f2ul(-79,3000000030U,-81L);

  printf(s2,i_l,ui_l);
  i=-82; ui=3000000033U;
  printf(s2,i<42L,ui<43L);
  printf(s2,i<42L,ui<43L);
  f2l(-82,3000000033U);

  printf(s1,i_ui);
  i=-84;
  printf(s1,i<44U);
  printf(s1,i<44U);
  f2ui(-84);

  return 0;
}
