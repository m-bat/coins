int printf(char *s, ...);

int i0=-(char)1,i1=-(signed char)1,i2=-(unsigned char)1;
int i3=-(short)1,i4=-(unsigned short)1;
int i5=-(char)-1,i6=-(signed char)-1,i7=-(unsigned char)-1;
int i8=-(short)-1,i9=-(unsigned short)-1;

void f(char c,signed char sc,unsigned char uc) {
  printf("%08X %08X %08X\n",-c,-sc,-uc);
}

void g(short s,unsigned short us) {
  printf("%08X %08X\n",-s,-us);
}

int main() {
  char c;
  signed char sc;
  unsigned char uc;
  short s;
  unsigned short us;

  printf("%08X %08X %08X\n",i0,i1,i2);
  c=2; sc=2; uc=2;
  printf("%08X %08X %08X\n",-c,-sc,-uc);
  printf("%08X %08X %08X\n",-c,-sc,-uc);
  f(3,3,3);

  printf("%08X %08X\n",i3,i4);
  s=2; us=2;
  printf("%08X %08X\n",-s,-us);
  printf("%08X %08X\n",-s,-us);
  g(3,3);

  printf("%08X %08X %08X\n",i5,i6,i7);
  c=-2; sc=-2; uc=-2;
  printf("%08X %08X %08X\n",-c,-sc,-uc);
  printf("%08X %08X %08X\n",-c,-sc,-uc);
  f(-3,-3,-3);

  printf("%08X %08X\n",i8,i9);
  s=-2; us=-2;
  printf("%08X %08X\n",-s,-us);
  printf("%08X %08X\n",-s,-us);
  g(-3,-3);

  return 0;
}
