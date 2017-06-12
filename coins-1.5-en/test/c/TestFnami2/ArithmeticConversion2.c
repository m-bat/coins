int printf(char *s, ...);

typedef signed char s_char;
typedef unsigned char u_char;
typedef unsigned short u_short;
typedef unsigned int u_int;
typedef unsigned long u_long;

char s1[]="%08lX\n";
char s2[]="%08X %08X\n";
char s3[]="%08X %08X %08X\n";
char s4[]="%08X %08X %08X %08X\n";
char s5[]="%08X %08X %08X %08X %08X\n";
char s6[]="%08X %08X %08X %08X %08X %08X\n";
char s7[]="%08X %08X %08X %08X %08X %08X %08X\n";

long     lul0=  (long)0xABCD1UL;
u_int   uiul0= (u_int)0xABCD1UL,uil0= (u_int)0xABCD2L;
int      iul0=   (int)0xABCD1UL, il0=   (int)0xABCD2L, iui0=   (int)0xABCD3U;
u_short usul0=(u_short)0xABCD1UL,usl0=(u_short)0xABCD2L,usui0=(u_short)0xABCD3U,usi0=(u_short)0xABCD4;
short    sul0= (short)0xABCD1UL, sl0= (short)0xABCD2L, sui0= (short)0xABCD3U, si0= (short)0xABCD4,
         sus0= (short)(u_short)0xAB5;
u_char  ucul0=(u_char)0xABCD1UL,ucl0=(u_char)0xABCD2L,ucui0=(u_char)0xABCD3U,uci0=(u_char)0xABCD4,
        ucus0=(u_char)(u_short)0xAB5,ucs0=(u_char)(short)0xAB6;
s_char  scul0=(s_char)0xABCD1UL,scl0=(s_char)0xABCD2L,scui0=(s_char)0xABCD3U,sci0=(s_char)0xABCD4,
        scus0=(s_char)(u_short)0xAB5,scs0=(s_char)(short)0xAB6,scuc0=(s_char)(u_char)0xA7;
long     lul1=  (long)-1UL;
u_int   uiul1= (u_int)-1UL,uil1= (u_int)-2L;
int      iul1=   (int)-1UL, il1=   (int)-2L, iui1=   (int)-3U;
u_short usul1=(u_short)-1UL,usl1=(u_short)-2L,usui1=(u_short)-3U,usi1=(u_short)-4;
short    sul1= (short)-1UL, sl1= (short)-2L, sui1= (short)-3U, si1= (short)-4,
         sus1= (short)(u_short)-5;
u_char  ucul1=(u_char)-1UL,ucl1=(u_char)-2L,ucui1=(u_char)-3U,uci1=(u_char)-4,
        ucus1=(u_char)(u_short)-5,ucs1=(u_char)(short)-6;
s_char  scul1=(s_char)-1UL,scl1=(s_char)-2L,scui1=(s_char)-3U,sci1=(s_char)-4,
        scus1=(s_char)(u_short)-5,scs1=(s_char)(short)-6,scuc1=(s_char)(u_char)-7;

void f(u_long ul,long l,u_int ui,int i,u_short us,short s,u_char uc) {
  printf(s1,  (long)ul);
  printf(s2, (u_int)ul, (u_int)l);
  printf(s3,   (int)ul,   (int)l,   (int)ui);
  printf(s4,(u_short)ul,(u_short)l,(u_short)ui,(u_short)i);
  printf(s5, (short)ul, (short)l, (short)ui, (short)i, (short)us);
  printf(s6,(u_char)ul,(u_char)l,(u_char)ui,(u_char)i,(u_char)us,(u_char)s);
  printf(s7,(s_char)ul,(s_char)l,(s_char)ui,(s_char)i,(s_char)us,(s_char)s,(s_char)uc);
}

int main() {
  u_long ul=0xABCD2;
  long l=0xABCD3;
  u_int ui=0xABCD4;
  int i=0xABCD5;
  u_short us=0xAB6;
  short s=0xAB7;
  u_char uc=0xA8;

  printf(s1, lul0);
  printf(s2,uiul0,uil0);
  printf(s3, iul0, il0, iui0);
  printf(s4,usul0,usl0,usui0,usi0);
  printf(s5, sul0, sl0, sui0, si0, sus0);
  printf(s6,ucul0,ucl0,ucui0,uci0,ucus0,ucs0);
  printf(s7,scul0,scl0,scui0,sci0,scus0,scs0,scuc0);
  printf(s1,  (long)ul);
  printf(s2, (u_int)ul, (u_int)l);
  printf(s3,   (int)ul,   (int)l,   (int)ui);
  printf(s4,(u_short)ul,(u_short)l,(u_short)ui,(u_short)i);
  printf(s5, (short)ul, (short)l, (short)ui, (short)i, (short)us);
  printf(s6,(u_char)ul,(u_char)l,(u_char)ui,(u_char)i,(u_char)us,(u_char)s);
  printf(s7,(s_char)ul,(s_char)l,(s_char)ui,(s_char)i,(s_char)us,(s_char)s,(s_char)uc);
  f(0xABCD3,0xABCD4,0xABCD5,0xABCD6,0xAB7,0xAB8,0xA9);

  ul=-2; l=-3; ui=-4; i=-5; us=-6; s=-7; uc=-8;
  printf(s1, lul1);
  printf(s2,uiul1,uil1);
  printf(s3, iul1, il1, iui1);
  printf(s4,usul1,usl1,usui1,usi1);
  printf(s5, sul1, sl1, sui1, si1, sus1);
  printf(s6,ucul1,ucl1,ucui1,uci1,ucus1,ucs1);
  printf(s7,scul1,scl1,scui1,sci1,scus1,scs1,scuc1);
  printf(s1,  (long)ul);
  printf(s2, (u_int)ul, (u_int)l);
  printf(s3,   (int)ul,   (int)l,   (int)ui);
  printf(s4,(u_short)ul,(u_short)l,(u_short)ui,(u_short)i);
  printf(s5, (short)ul, (short)l, (short)ui, (short)i, (short)us);
  printf(s6,(u_char)ul,(u_char)l,(u_char)ui,(u_char)i,(u_char)us,(u_char)s);
  printf(s7,(s_char)ul,(s_char)l,(s_char)ui,(s_char)i,(s_char)us,(s_char)s,(s_char)uc);
  f(-3,-4,-5,-6,-7,-8,-9);

  return 0;
}
