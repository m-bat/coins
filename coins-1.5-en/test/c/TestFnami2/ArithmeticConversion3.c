int printf(char *s, ...);

typedef signed char s_char;
typedef unsigned char u_char;
typedef unsigned short u_short;
typedef unsigned int u_int;
typedef unsigned long u_long;

char s1[]="%08X %08X %08X\n";
char s2[]="%08lX %08lX %08lX\n";

s_char  scf0=      (s_char)127.9F,scd0=       (s_char)127.9,scld0=       (s_char)127.9L;
u_char  ucf0=      (u_char)255.9F,ucd0=       (u_char)255.9,ucld0=       (u_char)255.9L;
short    sf0=     (short)32767.9F, sd0=      (short)32767.9, sld0=      (short)32767.9L;
u_short usf0=   (u_short)65535.9F,usd0=    (u_short)65535.9,usld0=    (u_short)65535.9L;
int      if0=   (int)2147483520.F, id0=   (int)2147483647.9, ild0=   (int)2147483647.9L;
u_int   uif0= (u_int)4294967040.F,uid0= (u_int)4294967295.9,uild0= (u_int)4294967295.9L;
long     lf0=  (long)2147483520.F, ld0=  (long)2147483647.9, lld0=  (long)2147483647.9L;
u_long  ulf0=(u_long)4294967040.F,uld0=(u_long)4294967295.9,ulld0=(u_long)4294967295.9L;
s_char  scf1=      (s_char)-128.9F,scd1=       (s_char)-128.9,scld1=       (s_char)-128.9L;
u_char  ucf1=      (u_char)  -0.9F,ucd1=       (u_char)  -0.9,ucld1=       (u_char)  -0.9L;
short    sf1=     (short)-32768.9F, sd1=      (short)-32768.9, sld1=      (short)-32768.9L;
u_short usf1=   (u_short)    -0.9F,usd1=    (u_short)    -0.9,usld1=    (u_short)    -0.9L;
int      if1=   (int)-2147483520.F, id1=   (int)-2147483648.9, ild1=   (int)-2147483648.9L;
u_int   uif1= (u_int)        -0.9F,uid1= (u_int)         -0.9,uild1= (u_int)         -0.9L;
long     lf1=  (long)-2147483520.F, ld1=  (long)-2147483648.9, lld1=  (long)-2147483648.9L;
u_long  ulf1=(u_long)        -0.9F,uld1=(u_long)         -0.9,ulld1=(u_long)         -0.9L;

void fsc(float f,double d,long double ld) { printf(s1, (s_char)f, (s_char)d, (s_char)ld); }
void fuc(float f,double d,long double ld) { printf(s1, (u_char)f, (u_char)d, (u_char)ld); }
void fs (float f,double d,long double ld) { printf(s1,  (short)f,  (short)d,  (short)ld); }
void fus(float f,double d,long double ld) { printf(s1,(u_short)f,(u_short)d,(u_short)ld); }
void fi (float f,double d,long double ld) { printf(s1,    (int)f,    (int)d,    (int)ld); }
void fui(float f,double d,long double ld) { printf(s1,  (u_int)f,  (u_int)d,  (u_int)ld); }
void fl (float f,double d,long double ld) { printf(s2,   (long)f,   (long)d,   (long)ld); }
void ful(float f,double d,long double ld) { printf(s2, (u_long)f, (u_long)d, (u_long)ld); }

int main() {
  float f;
  double d;
  long double ld;

  printf(s1,scf0,scd0,scld0);
  f=127.9F; d=127.9; ld=127.9L;
  printf(s1, (s_char)f, (s_char)d, (s_char)ld);
  printf(s1, (s_char)f, (s_char)d, (s_char)ld);
  fsc(127.9F,127.9,127.9L);

  printf(s1,ucf0,ucd0,ucld0);
  f=255.9F; d=255.9; ld=255.9L;
  printf(s1, (u_char)f, (u_char)d, (u_char)ld);
  printf(s1, (u_char)f, (u_char)d, (u_char)ld);
  fuc(255.9F,255.9,255.9L);

  printf(s1,sf0,sd0,sld0);
  f=32767.9F; d=32767.9; ld=32767.9L;
  printf(s1,  (short)f,  (short)d,  (short)ld);
  printf(s1,  (short)f,  (short)d,  (short)ld);
  fs(32767.9F,32767.9,32767.9L);

  printf(s1,usf0,usd0,usld0);
  f=65535.9F; d=65535.9; ld=65535.9L;
  printf(s1,(u_short)f,(u_short)d,(u_short)ld);
  printf(s1,(u_short)f,(u_short)d,(u_short)ld);
  fus(65535.9F,65535.9,65535.9L);

  printf(s1,if0,id0,ild0);
  f=2147483520.F; d=2147483647.9; ld=2147483647.9L;
  printf(s1,    (int)f,    (int)d,    (int)ld);
  printf(s1,    (int)f,    (int)d,    (int)ld);
  fi(2147483520.F,2147483647.9,2147483647.9L);

  printf(s1,uif0,uid0,uild0);
  f=4294967040.F; d=4294967295.9; ld=4294967295.9L;
  printf(s1,  (u_int)f,  (u_int)d,  (u_int)ld);
  printf(s1,  (u_int)f,  (u_int)d,  (u_int)ld);
  fui(4294967040.F,4294967295.9,4294967295.9L);

  printf(s1,lf0,ld0,lld0);
  f=2147483520.F; d=2147483647.9; ld=2147483647.9L;
  printf(s2,   (long)f,   (long)d,   (long)ld);
  printf(s2,   (long)f,   (long)d,   (long)ld);
  fl(2147483520.F,2147483647.9,2147483647.9L);

  printf(s1,ulf0,uld0,ulld0);
  f=4294967040.F; d=4294967295.9; ld=4294967295.9L;
  printf(s2, (u_long)f, (u_long)d, (u_long)ld);
  printf(s2, (u_long)f, (u_long)d, (u_long)ld);
  ful(4294967040.F,4294967295.9,4294967295.9L);

  printf(s1,scf1,scd1,scld1);
  f=-128.9F; d=-128.9; ld=-128.9L;
  printf(s1, (s_char)f, (s_char)d, (s_char)ld);
  printf(s1, (s_char)f, (s_char)d, (s_char)ld);
  fsc(-128.9F,-128.9,-128.9L);

  printf(s1,ucf1,ucd1,ucld1);
  f=-0.9F; d=-0.9; ld=-0.9L;
  printf(s1, (u_char)f, (u_char)d, (u_char)ld);
  printf(s1, (u_char)f, (u_char)d, (u_char)ld);
  fuc(-0.9F,-0.9,-0.9L);

  printf(s1,sf1,sd1,sld1);
  f=-32768.9F; d=-32768.9; ld=-32768.9L;
  printf(s1,  (short)f,  (short)d,  (short)ld);
  printf(s1,  (short)f,  (short)d,  (short)ld);
  fs(-32768.9F,-32768.9,-32768.9L);

  printf(s1,usf1,usd1,usld1);
  f=-0.9F; d=-0.9; ld=-0.9L;
  printf(s1,(u_short)f,(u_short)d,(u_short)ld);
  printf(s1,(u_short)f,(u_short)d,(u_short)ld);
  fus(-0.9F,-0.9,-0.9L);

  printf(s1,if1,id1,ild1);
  f=-2147483520.F; d=-2147483648.9; ld=-2147483648.9L;
  printf(s1,    (int)f,    (int)d,    (int)ld);
  printf(s1,    (int)f,    (int)d,    (int)ld);
  fi(-2147483520.F,-2147483648.9,-2147483648.9L);

  printf(s1,uif1,uid1,uild1);
  f=-0.9F; d=-0.9; ld=-0.9L;
  printf(s1,  (u_int)f,  (u_int)d,  (u_int)ld);
  printf(s1,  (u_int)f,  (u_int)d,  (u_int)ld);
  fui(-0.9F,-0.9,-0.9L);

  printf(s1,lf1,ld1,lld1);
  f=-2147483520.F; d=-2147483648.9; ld=-2147483648.9L;
  printf(s2,   (long)f,   (long)d,   (long)ld);
  printf(s2,   (long)f,   (long)d,   (long)ld);
  fl(-2147483520.F,-2147483648.9,-2147483648.9L);

  printf(s1,ulf1,uld1,ulld1);
  f=-0.9F; d=-0.9; ld=-0.9L;
  printf(s2, (u_long)f, (u_long)d, (u_long)ld);
  printf(s2, (u_long)f, (u_long)d, (u_long)ld);
  ful(-0.9F,-0.9,-0.9L);

  return 0;
}
