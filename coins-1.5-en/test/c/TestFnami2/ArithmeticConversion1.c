int printf(char *s, ...);

typedef signed char s_char;
typedef unsigned char u_char;
typedef unsigned short u_short;
typedef unsigned int u_int;
typedef unsigned long u_long;

char s1[]="%08X\n";
char s2[]="%08X %08X\n";
char s3[]="%08X %08X %08X\n";
char s4[]="%08X %08X %08X %08X\n";
char s5[]="%08X %08X %08X %08X %08X\n";
char s6[]="%08lX %08lX %08lX %08lX %08lX %08lX\n";
char s7[]="%08lX %08lX %08lX %08lX %08lX %08lX %08lX\n";

u_char  ucsc0=(s_char)1;
short    ssc0=(s_char)1, suc0=(u_char)2;
u_short ussc0=(s_char)1,usuc0=(u_char)2,uss0=(short)3;
int      isc0=(s_char)1, iuc0=(u_char)2, is0=(short)3, ius0=(u_short)4;
u_int   uisc0=(s_char)1,uiuc0=(u_char)2,uis0=(short)3,uius0=(u_short)4,uii0=5;
long     lsc0=(s_char)1, luc0=(u_char)2, ls0=(short)3, lus0=(u_short)4, li0=5, lui0=6U;
u_long  ulsc0=(s_char)1,uluc0=(u_char)2,uls0=(short)3,ulus0=(u_short)4,uli0=5,ului0=6U,ull0=7L;
u_char  ucsc1=(s_char)-1;
short    ssc1=(s_char)-1, suc1=(u_char)-2;
u_short ussc1=(s_char)-1,usuc1=(u_char)-2,uss1=(short)-3;
int      isc1=(s_char)-1, iuc1=(u_char)-2, is1=(short)-3, ius1=(u_short)-4;
u_int   uisc1=(s_char)-1,uiuc1=(u_char)-2,uis1=(short)-3,uius1=(u_short)-4,uii1=-5;
long     lsc1=(s_char)-1, luc1=(u_char)-2, ls1=(short)-3, lus1=(u_short)-4, li1=-5, lui1=-6U;
u_long  ulsc1=(s_char)-1,uluc1=(u_char)-2,uls1=(short)-3,ulus1=(u_short)-4,uli1=-5,ului1=-6U,ull1=-7L;

void f(s_char sc,u_char uc,short s,u_short us,int i,u_int ui,long l) {
  printf(s1,(u_char)sc);
  printf(s2, (short)sc, (short)uc);
  printf(s3,(u_short)sc,(u_short)uc,(u_short)s);
  printf(s4,   (int)sc,   (int)uc,   (int)s,   (int)us);
  printf(s5, (u_int)sc, (u_int)uc, (u_int)s, (u_int)us, (u_int)i);
  printf(s6,  (long)sc,  (long)uc,  (long)s,  (long)us,  (long)i,  (long)ui);
  printf(s7,(u_long)sc,(u_long)uc,(u_long)s,(u_long)us,(u_long)i,(u_long)ui,(u_long)l);
}

int main() {
  s_char sc=2;
  u_char uc=3;
  short s=4;
  u_short us=5;
  int i=6;
  u_int ui=7;
  long l=8;

  printf(s1,ucsc0);
  printf(s2, ssc0, suc0);
  printf(s3,ussc0,usuc0,uss0);
  printf(s4, isc0, iuc0, is0, ius0);
  printf(s5,uisc0,uiuc0,uis0,uius0,uii0);
  printf(s6, lsc0, luc0, ls0, lus0, li0, lui0);
  printf(s7,ulsc0,uluc0,uls0,ulus0,uli0,ului0,ull0);
  printf(s1,(u_char)sc);
  printf(s2, (short)sc, (short)uc);
  printf(s3,(u_short)sc,(u_short)uc,(u_short)s);
  printf(s4,   (int)sc,   (int)uc,   (int)s,   (int)us);
  printf(s5, (u_int)sc, (u_int)uc, (u_int)s, (u_int)us, (u_int)i);
  printf(s6,  (long)sc,  (long)uc,  (long)s,  (long)us,  (long)i,  (long)ui);
  printf(s7,(u_long)sc,(u_long)uc,(u_long)s,(u_long)us,(u_long)i,(u_long)ui,(u_long)l);
  f(3,4,5,6,7,8,9);

  sc=-2; uc=-3; s=-4; us=-5; i=-6; ui=-7; l=-8;
  printf(s1,ucsc1);
  printf(s2, ssc1, suc1);
  printf(s3,ussc1,usuc1,uss1);
  printf(s4, isc1, iuc1, is1, ius1);
  printf(s5,uisc1,uiuc1,uis1,uius1,uii1);
  printf(s6, lsc1, luc1, ls1, lus1, li1, lui1);
  printf(s7,ulsc1,uluc1,uls1,ulus1,uli1,ului1,ull1);
  printf(s1,(u_char)sc);
  printf(s2, (short)sc, (short)uc);
  printf(s3,(u_short)sc,(u_short)uc,(u_short)s);
  printf(s4,   (int)sc,   (int)uc,   (int)s,   (int)us);
  printf(s5, (u_int)sc, (u_int)uc, (u_int)s, (u_int)us, (u_int)i);
  printf(s6,  (long)sc,  (long)uc,  (long)s,  (long)us,  (long)i,  (long)ui);
  printf(s7,(u_long)sc,(u_long)uc,(u_long)s,(u_long)us,(u_long)i,(u_long)ui,(u_long)l);
  f(-3,-4,-5,-6,-7,-8,-9);

  return 0;
}
