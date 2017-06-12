int printf(char *s, ...);

char s13[]="%d %d %d %d %d %d %d %d %d %d %d %d %d\n";
char s14[]="%d %d %d %d %d %d %d %d %d %d %d %d %d %d\n";

int a0=10-0,a1=10-1,a2=10-2,a3=10-3,a4=10-127,a5=10-128,a6=10-129,a7=10-4095,a8=10-4096,a9=10-4097,
    aa=10-32767,ab=10-32768,ac=10-32769,ad=10-2147483638;
int b1=10-(-1),b2=10-(-2),b3=10-(-3),b4=10-(-127),b5=10-(-128),b6=10-(-129),b7=10-(-4095),b8=10-(-4096),b9=10-(-4097),
    ba=10-(-32767),bb=10-(-32768),bc=10-(-32769),bd=10-(-2147483637);

void f0(int x) {
  printf(s14,x-0,x-1,x-2,x-3,x-127,x-128,x-129,x-4095,x-4096,x-4097,
         x-32767,x-32768,x-32769,x-2147483638);
  printf(s13,x-(-1),x-(-2),x-(-3),x-(-127),x-(-128),x-(-129),x-(-4095),x-(-4096),x-(-4097),
         x-(-32767),x-(-32768),x-(-32769),x-(-2147483637));
}

void f1(int x) {
  printf(s14,0-x,1-x,2-x,3-x,127-x,128-x,129-x,4095-x,4096-x,4097-x,
         32767-x,32768-x,32769-x,2147483637-x);
  printf(s13,(-1)-x,(-2)-x,(-3)-x,(-127)-x,(-128)-x,(-129)-x,(-4095)-x,(-4096)-x,(-4097)-x,
         (-32767)-x,(-32768)-x,(-32769)-x,(-2147483638)-x);
}

int op(int x,int y) { return x-y; }

void f2(int x) {
  printf(s14,op(x,0),op(x,1),op(x,2),op(x,3),op(x,127),op(x,128),op(x,129),op(x,4095),op(x,4096),op(x,4097),
         op(x,32767),op(x,32768),op(x,32769),op(x,2147483638));
  printf(s13,op(x,-1),op(x,-2),op(x,-3),op(x,-127),op(x,-128),op(x,-129),op(x,-4095),op(x,-4096),op(x,-4097),
         op(x,-32767),op(x,-32768),op(x,-32769),op(x,-2147483637));
}

int main() {
  int x;

  printf(s14,a0,a1,a2,a3,a4,a5,a6,a7,a8,a9,aa,ab,ac,ad);
  printf(s13,b1,b2,b3,b4,b5,b6,b7,b8,b9,ba,bb,bc,bd);

  printf(s14,10-0,10-1,10-2,10-3,10-127,10-128,10-129,10-4095,10-4096,10-4097,
         10-32767,10-32768,10-32769,10-2147483638);
  printf(s13,10-(-1),10-(-2),10-(-3),10-(-127),10-(-128),10-(-129),10-(-4095),10-(-4096),10-(-4097),
         10-(-32767),10-(-32768),10-(-32769),10-(-2147483637));

  x=10;
  printf(s14,x-0,x-1,x-2,x-3,x-127,x-128,x-129,x-4095,x-4096,x-4097,
         x-32767,x-32768,x-32769,x-2147483638);
  printf(s14,x-0,x-1,x-2,x-3,x-127,x-128,x-129,x-4095,x-4096,x-4097,
         x-32767,x-32768,x-32769,x-2147483638);
  x=10;
  printf(s13,x-(-1),x-(-2),x-(-3),x-(-127),x-(-128),x-(-129),x-(-4095),x-(-4096),x-(-4097),
         x-(-32767),x-(-32768),x-(-32769),x-(-2147483637));
  printf(s13,x-(-1),x-(-2),x-(-3),x-(-127),x-(-128),x-(-129),x-(-4095),x-(-4096),x-(-4097),
         x-(-32767),x-(-32768),x-(-32769),x-(-2147483637));
  x=10;
  printf(s14,0-x,1-x,2-x,3-x,127-x,128-x,129-x,4095-x,4096-x,4097-x,
         32767-x,32768-x,32769-x,2147483637-x);
  printf(s14,0-x,1-x,2-x,3-x,127-x,128-x,129-x,4095-x,4096-x,4097-x,
         32767-x,32768-x,32769-x,2147483637-x);
  x=10;
  printf(s13,(-1)-x,(-2)-x,(-3)-x,(-127)-x,(-128)-x,(-129)-x,(-4095)-x,(-4096)-x,(-4097)-x,
         (-32767)-x,(-32768)-x,(-32769)-x,(-2147483638)-x);
  printf(s13,(-1)-x,(-2)-x,(-3)-x,(-127)-x,(-128)-x,(-129)-x,(-4095)-x,(-4096)-x,(-4097)-x,
         (-32767)-x,(-32768)-x,(-32769)-x,(-2147483638)-x);

  f0(10); f0(-10);
  f1(10); f1(-10);
  f2(10); f2(-10);

  return 0;
}