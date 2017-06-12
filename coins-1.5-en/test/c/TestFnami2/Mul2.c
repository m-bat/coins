int printf(char *s, ...);

char s11[]="%u %u %u %u %u %u %u %u %u %u %u\n";

unsigned int a0=10U*0U,a1=10U*1U,a2=10U*2U,a3=10U*3U,a4=10U*4U,a5=10U*5U,a6=10U*6U,a7=10U*0x56U,a8=10U*0xABU,a9=10U*214748364U,aa=10U*2147483649U;

void f0(unsigned int x) {
  printf(s11,x*0U,x*1U,x*2U,x*3U,x*4U,x*5U,x*6U,x*0x56U,x*0xABU,x*214748364U,x*2147483649U);
}

void f1(unsigned int x) {
  printf(s11,0U*x,1U*x,2U*x,3U*x,4U*x,5U*x,6U*x,0x56U*x,0xABU*x,214748364U*x,2147483649U*x);
}

unsigned int op(unsigned int x,unsigned int y) { return x*y; }

void f2(unsigned int x) {
  printf(s11,op(x,0U),op(x,1U),op(x,2U),op(x,3U),op(x,4U),op(x,5U),op(x,6U),op(x,0x56U),op(x,0xABU),op(x,214748364U),op(x,2147483649U));
}

int main() {
  unsigned int x;

  printf(s11,a0,a1,a2,a3,a4,a5,a6,a7,a8,a9,aa);

  printf(s11,10U*0U,10U*1U,10U*2U,10U*3U,10U*4U,10U*5U,10U*6U,10U*0x56U,10U*0xABU,10U*214748364U,10U*2147483649U);

  x=10U;
  printf(s11,x*0U,x*1U,x*2U,x*3U,x*4U,x*5U,x*6U,x*0x56U,x*0xABU,x*214748364U,x*2147483649U);
  printf(s11,x*0U,x*1U,x*2U,x*3U,x*4U,x*5U,x*6U,x*0x56U,x*0xABU,x*214748364U,x*2147483649U);
  x=10U;
  printf(s11,0U*x,1U*x,2U*x,3U*x,4U*x,5U*x,6U*x,0x56U*x,0xABU*x,214748364U*x,2147483649U*x);
  printf(s11,0U*x,1U*x,2U*x,3U*x,4U*x,5U*x,6U*x,0x56U*x,0xABU*x,214748364U*x,2147483649U*x);

  f0(10U);
  f1(10U);
  f2(10U);

  return 0;
}
