int printf(char *s, ...);

typedef signed char s_char;
typedef unsigned char u_char;
typedef unsigned short u_short;
typedef unsigned int u_int;
typedef unsigned long u_long;
typedef long double l_double;

char s1[]="%g %g %g %g %.1f %.1f %.1f %.1f\n";
char s2[]="%Lg %Lg %Lg %Lg %.1Lf %.1Lf %.1Lf %.1Lf\n";

float     fsc=(s_char)-123, fuc=(u_char)234, fs=(short)-12345, fus=(u_short)34567,
          fi=-1234567890, fui=3456789012U, fl=-1234567890L, ful=3456789012UL;
double    dsc=(s_char)-123, duc=(u_char)234, ds=(short)-12345, dus=(u_short)34567,
          di=-1234567890, dui=3456789012U, dl=-1234567890L, dul=3456789012UL;
l_double ldsc=(s_char)-123,lduc=(u_char)234,lds=(short)-12345,ldus=(u_short)34567,
         ldi=-1234567890,ldui=3456789012U,ldl=-1234567890L,ldul=3456789012UL;

void ff(s_char sc,u_char uc,short s,u_short us,int i,u_int ui,long l,u_long ul) {
  printf(s1,(float)sc,(float)uc,(float)s,(float)us,(float)i,(float)ui,(float)l,(float)ul);
}

void fd(s_char sc,u_char uc,short s,u_short us,int i,u_int ui,long l,u_long ul) {
  printf(s1,(double)sc,(double)uc,(double)s,(double)us,(double)i,(double)ui,(double)l,(double)ul);
}

void fld(s_char sc,u_char uc,short s,u_short us,int i,u_int ui,long l,u_long ul) {
  printf(s2,(l_double)sc,(l_double)uc,(l_double)s,(l_double)us,(l_double)i,(l_double)ui,(l_double)l,(l_double)ul);
}

int main() {
  s_char sc;
  u_char uc;
  short s;
  u_short us;
  int i;
  u_int ui;
  long l;
  u_long ul;

  printf(s1,fsc,fuc,fs,fus,fi,fui,fl,ful);
  sc=-123; uc=234; s=-12345; us=34567; i=-1234567890; ui=3456789012U; l=-1234567890L; ul=3456789012UL;
  printf(s1,(float)sc,(float)uc,(float)s,(float)us,(float)i,(float)ui,(float)l,(float)ul);
  printf(s1,(float)sc,(float)uc,(float)s,(float)us,(float)i,(float)ui,(float)l,(float)ul);
  ff(-123,234,-12345,34567,-1234567890,3456789012U,-1234567890L,3456789012UL);

  printf(s1,dsc,duc,ds,dus,di,dui,dl,dul);
  sc=-123; uc=234; s=-12345; us=34567; i=-1234567890; ui=3456789012U; l=-1234567890L; ul=3456789012UL;
  printf(s1,(double)sc,(double)uc,(double)s,(double)us,(double)i,(double)ui,(double)l,(double)ul);
  printf(s1,(double)sc,(double)uc,(double)s,(double)us,(double)i,(double)ui,(double)l,(double)ul);
  fd(-123,234,-12345,34567,-1234567890,3456789012U,-1234567890L,3456789012UL);

  printf(s2,ldsc,lduc,lds,ldus,ldi,ldui,ldl,ldul);
  sc=-123; uc=234; s=-12345; us=34567; i=-1234567890; ui=3456789012U; l=-1234567890L; ul=3456789012UL;
  printf(s2,(l_double)sc,(l_double)uc,(l_double)s,(l_double)us,(l_double)i,(l_double)ui,(l_double)l,(l_double)ul);
  printf(s2,(l_double)sc,(l_double)uc,(l_double)s,(l_double)us,(l_double)i,(l_double)ui,(l_double)l,(l_double)ul);
  fld(-123,234,-12345,34567,-1234567890,3456789012U,-1234567890L,3456789012UL);

  return 0;
}
