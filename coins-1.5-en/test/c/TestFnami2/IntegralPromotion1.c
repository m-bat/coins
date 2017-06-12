int printf(char *s, ...);

enum e { A=128,B=32767 };

int i0=(char)255,i1=(signed char)255,i2=(unsigned char)255;
int i3=(short)0xFFFF,i4=(unsigned short)0xFFFF;
int i5=A,i6=B;

void f(char c,signed char sc,unsigned char uc) {
  printf("%08X %08X %08X\n",c,sc,uc);
}

void g(short s,unsigned short us) {
  printf("%08X %08X\n",s,us);
}

void h(enum e e0,enum e e1) {
  printf("%08X %08X\n",e0,e1);
}

int main() {
  char c;
  signed char sc;
  unsigned char uc;
  short s;
  unsigned short us;
  enum e e0,e1;

  printf("%08X %08X %08X\n",i0,i1,i2);
  c=240; sc=240; uc=240;
  printf("%08X %08X %08X\n",c,sc,uc);
  printf("%08X %08X %08X\n",c,sc,uc);
  f(160,160,160);

  printf("%08X %08X\n",i3,i4);
  s=0xF123; us=0xF123;
  printf("%08X %08X\n",s,us);
  printf("%08X %08X\n",s,us);
  g(0xF456,0xF456);

  printf("%08X %08X\n",i5,i6);
  e0=A; e1=B;
  printf("%08X %08X\n",e0,e1);
  printf("%08X %08X\n",e0,e1);
  h(A,B);

  return 0;
}
