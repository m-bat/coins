int printf(char *s, ...);

int i0=(-(char)1)>>5,i1=(-(signed char)1)>>5,i2=(-(unsigned char)1)>>5;
int i3=(-(short)1)>>5,i4=(-(unsigned short)1)>>5;

void f(char c,signed char sc,unsigned char uc) {
  printf("%08X %08X %08X\n",(-c)>>7,(-sc)>>7,(-uc)>>7);
}

void g(short s,unsigned short us) {
  printf("%08X %08X\n",(-s)>>7,(-us)>>7);
}

int main() {
  char c;
  signed char sc;
  unsigned char uc;
  short s;
  unsigned short us;

  printf("%08X %08X %08X\n",i0,i1,i2);
  c=1; sc=1; uc=1;
  printf("%08X %08X %08X\n",(-c)>>6,(-sc)>>6,(-uc)>>6);
  printf("%08X %08X %08X\n",(-c)>>6,(-sc)>>6,(-uc)>>6);
  f(1,1,1);

  printf("%08X %08X\n",i3,i4);
  s=1; us=1;
  printf("%08X %08X\n",(-s)>>6,(-us)>>6);
  printf("%08X %08X\n",(-s)>>6,(-us)>>6);
  g(1,1);

  return 0;
}
