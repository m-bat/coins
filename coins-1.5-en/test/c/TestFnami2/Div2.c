int printf(char *s, ...);

char s10[]="%u %u %u %u %u %u %u %u %u %u\n";
char s11[]="%u %u %u %u %u %u %u %u %u %u %u\n";

unsigned int a1=9U/1U,a2=9U/2U,a3=9U/3U,a4=9U/4U,a5=9U/5U,a6=9U/6U,a7=9U/0x56U,a8=9U/0xABU,a9=9U/214748364U,aa=9U/2147483649U;
unsigned int b1=5U/1U,b2=5U/2U,b3=5U/3U,b4=5U/4U,b5=5U/5U,b6=5U/6U,b7=5U/0x56U,b8=5U/0xbBU,b9=5U/214748364U,ba=5U/2147483649U;
unsigned int c1=4000000000U/1U,c2=4000000000U/2U,c3=4000000000U/3U,c4=4000000000U/4U,c5=4000000000U/5U,c6=4000000000U/6U,c7=4000000000U/0x56U,c8=4000000000U/0xcBU,c9=4000000000U/214748364U,ca=4000000000U/2147483649U;

void f0(unsigned int x) {
  printf(s10,x/1U,x/2U,x/3U,x/4U,x/5U,x/6U,x/0x56U,x/0xABU,x/214748364U,x/2147483649U);
}

void f1(unsigned int x) {
  printf(s11,0U/x,1U/x,2U/x,3U/x,4U/x,5U/x,6U/x,0x56U/x,0xABU/x,214748364U/x,2147483649U/x);
}

unsigned int op(unsigned int x,unsigned int y) { return x/y; }

void f2(unsigned int x) {
  printf(s10,op(x,1U),op(x,2U),op(x,3U),op(x,4U),op(x,5U),op(x,6U),op(x,0x56U),op(x,0xABU),op(x,214748364U),op(x,2147483649U));
}

int main() {
  unsigned int x;

  printf(s10,a1,a2,a3,a4,a5,a6,a7,a8,a9,aa);
  printf(s10,b1,b2,b3,b4,b5,b6,b7,b8,b9,ba);
  printf(s10,c1,c2,c3,c4,c5,c6,c7,c8,c9,ca);

  printf(s10,9U/1U,9U/2U,9U/3U,9U/4U,9U/5U,9U/6U,9U/0x56U,9U/0xABU,9U/214748364U,9U/2147483649U);
  printf(s10,5U/1U,5U/2U,5U/3U,5U/4U,5U/5U,5U/6U,5U/0x56U,5U/0xABU,5U/214748364U,5U/2147483649U);
  printf(s10,4000000000U/1U,4000000000U/2U,4000000000U/3U,4000000000U/4U,4000000000U/5U,4000000000U/6U,4000000000U/0x56U,4000000000U/0xABU,4000000000U/214748364U,4000000000U/2147483649U);

  x=9U;
  printf(s10,x/1U,x/2U,x/3U,x/4U,x/5U,x/6U,x/0x56U,x/0xABU,x/214748364U,x/2147483649U);
  printf(s10,x/1U,x/2U,x/3U,x/4U,x/5U,x/6U,x/0x56U,x/0xABU,x/214748364U,x/2147483649U);
  x=9U;
  printf(s11,0U/x,1U/x,2U/x,3U/x,4U/x,5U/x,6U/x,0x56U/x,0xABU/x,214748364U/x,2147483649U/x);
  printf(s11,0U/x,1U/x,2U/x,3U/x,4U/x,5U/x,6U/x,0x56U/x,0xABU/x,214748364U/x,2147483649U/x);
  x=5U;
  printf(s10,x/1U,x/2U,x/3U,x/4U,x/5U,x/6U,x/0x56U,x/0xABU,x/214748364U,x/2147483649U);
  printf(s10,x/1U,x/2U,x/3U,x/4U,x/5U,x/6U,x/0x56U,x/0xABU,x/214748364U,x/2147483649U);
  x=5U;
  printf(s11,0U/x,1U/x,2U/x,3U/x,4U/x,5U/x,6U/x,0x56U/x,0xABU/x,214748364U/x,2147483649U/x);
  printf(s11,0U/x,1U/x,2U/x,3U/x,4U/x,5U/x,6U/x,0x56U/x,0xABU/x,214748364U/x,2147483649U/x);
  x=4000000000U;
  printf(s10,x/1U,x/2U,x/3U,x/4U,x/5U,x/6U,x/0x56U,x/0xABU,x/214748364U,x/2147483649U);
  printf(s10,x/1U,x/2U,x/3U,x/4U,x/5U,x/6U,x/0x56U,x/0xABU,x/214748364U,x/2147483649U);
  x=4000000000U;
  printf(s11,0U/x,1U/x,2U/x,3U/x,4U/x,5U/x,6U/x,0x56U/x,0xABU/x,214748364U/x,2147483649U/x);
  printf(s11,0U/x,1U/x,2U/x,3U/x,4U/x,5U/x,6U/x,0x56U/x,0xABU/x,214748364U/x,2147483649U/x);

  f0(9U); f0(5U); f0(4000000000U);
  f1(9U); f1(5U); f1(4000000000U);
  f2(9U); f2(5U); f2(4000000000U);

  return 0;
}
