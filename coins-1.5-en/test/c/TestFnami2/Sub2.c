int printf(char *s, ...);

char s16[]="%u %u %u %u %u %u %u %u %u %u %u %u %u %u %u %u\n";

unsigned int a0=10U-0U,a1=10U-1U,a2=10U-2U,a3=10U-3U,a4=10U-127U,a5=10U-128U,a6=10U-129U,
             a7=10U-4095U,a8=10U-4096U,a9=10U-4097U,
             aa=10U-32767U,ab=10U-32768U,ac=10U-32769U,
             ad=10U-2147483647U,ae=10U-4294967285U,af=10U-4294967295U;

void f0(unsigned int x) {
  printf(s16,x-0U,x-1U,x-2U,x-3U,x-127U,x-128U,x-129U,
             x-4095U,x-4096U,x-4097U,
             x-32767U,x-32768U,x-32769U,
             x-2147483647U,x-4294967285U,x-4294967295U);
}

void f1(unsigned int x) {
  printf(s16,0U-x,1U-x,2U-x,3U-x,127U-x,128U-x,129U-x,
             4095U-x,4096U-x,4097U-x,
             32767U-x,32768U-x,32769U-x,
             2147483647U-x,4294967285U-x,4294967295U-x);
}

unsigned int op(unsigned int x,unsigned int y) { return x-y; }

void f2(unsigned int x) {
  printf(s16,op(x,0U),op(x,1U),op(x,2U),op(x,3U),op(x,127U),op(x,128U),op(x,129U),
         op(x,4095U),op(x,4096U),op(x,4097U),
         op(x,32767U),op(x,32768U),op(x,32769U),
         op(x,2147483637U),op(x,4294967285U),op(x,4294967295U));
}

int main() {
  unsigned int x;

  printf(s16,a0,a1,a2,a3,a4,a5,a6,a7,a8,a9,aa,ab,ac,ad,ae,af);

  printf(s16,10U-0U,10U-1U,10U-2U,10U-3U,10U-127U,10U-128U,10U-129U,
             10U-4095U,10U-4096U,10U-4097U,
             10U-32767U,10U-32768U,10U-32769U,
             10U-2147483647U,10U-4294967285U,10U-4294967295U);

  x=10U;
  printf(s16,x-0U,x-1U,x-2U,x-3U,x-127U,x-128U,x-129U,
             x-4095U,x-4096U,x-4097U,
             x-32767U,x-32768U,x-32769U,
             x-2147483647U,x-4294967285U,x-4294967295U);
  printf(s16,x-0U,x-1U,x-2U,x-3U,x-127U,x-128U,x-129U,
             x-4095U,x-4096U,x-4097U,
             x-32767U,x-32768U,x-32769U,
             x-2147483647U,x-4294967285U,x-4294967295U);
  x=10U;
  printf(s16,0U-x,1U-x,2U-x,3U-x,127U-x,128U-x,129U-x,
             4095U-x,4096U-x,4097U-x,
             32767U-x,32768U-x,32769U-x,
             2147483647U-x,4294967285U-x,4294967295U-x);
  printf(s16,0U-x,1U-x,2U-x,3U-x,127U-x,128U-x,129U-x,
             4095U-x,4096U-x,4097U-x,
             32767U-x,32768U-x,32769U-x,
             2147483647U-x,4294967285U-x,4294967295U-x);

  f0(10U);
  f1(10U);
  f2(10U);

  return 0;
}
