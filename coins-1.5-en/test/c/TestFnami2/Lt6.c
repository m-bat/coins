int printf(char *s, ...);

void f0(unsigned int x) {
  if(x< 0U) printf("T"); else printf("-");
  if(x< 1U) printf("T"); else printf("-");
  if(x<19U) printf("T"); else printf("-");
  if(x<20U) printf("T"); else printf("-");
  if(x<2147483648U+10) printf("T"); else printf("-");
  if(x<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
}

void f1(unsigned int x) {
  if( 0U<x) printf("T"); else printf("-");
  if( 1U<x) printf("T"); else printf("-");
  if(19U<x) printf("T"); else printf("-");
  if(20U<x) printf("T"); else printf("-");
  if(2147483648U+10<x) printf("T"); else printf("-");
  if(2147483648U+11<x) printf("T"); else printf("-");
  printf("\n");
}

void g0(unsigned int x) {
  if(x<         0U) printf("T"); else printf("-");
  if(x<4294967294U) printf("T"); else printf("-");
  if(x<4294967295U) printf("T"); else printf("-");
  printf("\n");
}

void g1(unsigned int x) {
  if(         0U<x) printf("T"); else printf("-");
  if(4294967294U<x) printf("T"); else printf("-");
  if(4294967295U<x) printf("T"); else printf("-");
  printf("\n");
}

void op(unsigned int x,unsigned int y) {
  if(x<y) printf("T"); else printf("-");
}

void f2(unsigned int x) {
  op(x,0U); op(x,1U); op(x,19U); op(x,20U); op(x,2147483648U+10); op(x,2147483648U+11);
  printf("\n");
}

void g2(unsigned int x) {
  op(x,0); op(x,4294967294U); op(x,4294967295U);
  printf("\n");
}

void main1() {
  unsigned int x;
  x=0U; if(x< 0U) printf("T"); else printf("-"); if(x< 0U) printf("T"); else printf("-");
  x=0U; if(x< 1U) printf("T"); else printf("-"); if(x< 1U) printf("T"); else printf("-");
  x=0U; if(x<19U) printf("T"); else printf("-"); if(x<19U) printf("T"); else printf("-");
  x=0U; if(x<20U) printf("T"); else printf("-"); if(x<20U) printf("T"); else printf("-");
  x=0U; if(x<2147483648U+10) printf("T"); else printf("-"); if(x<2147483648U+10) printf("T"); else printf("-");
  x=0U; if(x<2147483648U+11) printf("T"); else printf("-"); if(x<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
  x=1U; if(x< 0U) printf("T"); else printf("-"); if(x< 0U) printf("T"); else printf("-");
  x=1U; if(x< 1U) printf("T"); else printf("-"); if(x< 1U) printf("T"); else printf("-");
  x=1U; if(x<19U) printf("T"); else printf("-"); if(x<19U) printf("T"); else printf("-");
  x=1U; if(x<20U) printf("T"); else printf("-"); if(x<20U) printf("T"); else printf("-");
  x=1U; if(x<2147483648U+10) printf("T"); else printf("-"); if(x<2147483648U+10) printf("T"); else printf("-");
  x=1U; if(x<2147483648U+11) printf("T"); else printf("-"); if(x<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
  x=19U; if(x< 0U) printf("T"); else printf("-"); if(x< 0U) printf("T"); else printf("-");
  x=19U; if(x< 1U) printf("T"); else printf("-"); if(x< 1U) printf("T"); else printf("-");
  x=19U; if(x<19U) printf("T"); else printf("-"); if(x<19U) printf("T"); else printf("-");
  x=19U; if(x<20U) printf("T"); else printf("-"); if(x<20U) printf("T"); else printf("-");
  x=19U; if(x<2147483648U+10) printf("T"); else printf("-"); if(x<2147483648U+10) printf("T"); else printf("-");
  x=19U; if(x<2147483648U+11) printf("T"); else printf("-"); if(x<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
  x=20U; if(x< 0U) printf("T"); else printf("-"); if(x< 0U) printf("T"); else printf("-");
  x=20U; if(x< 1U) printf("T"); else printf("-"); if(x< 1U) printf("T"); else printf("-");
  x=20U; if(x<19U) printf("T"); else printf("-"); if(x<19U) printf("T"); else printf("-");
  x=20U; if(x<20U) printf("T"); else printf("-"); if(x<20U) printf("T"); else printf("-");
  x=20U; if(x<2147483648U+10) printf("T"); else printf("-"); if(x<2147483648U+10) printf("T"); else printf("-");
  x=20U; if(x<2147483648U+11) printf("T"); else printf("-"); if(x<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
  x=2147483648U+10; if(x< 0U) printf("T"); else printf("-"); if(x< 0U) printf("T"); else printf("-");
  x=2147483648U+10; if(x< 1U) printf("T"); else printf("-"); if(x< 1U) printf("T"); else printf("-");
  x=2147483648U+10; if(x<19U) printf("T"); else printf("-"); if(x<19U) printf("T"); else printf("-");
  x=2147483648U+10; if(x<20U) printf("T"); else printf("-"); if(x<20U) printf("T"); else printf("-");
  x=2147483648U+10; if(x<2147483648U+10) printf("T"); else printf("-"); if(x<2147483648U+10) printf("T"); else printf("-");
  x=2147483648U+10; if(x<2147483648U+11) printf("T"); else printf("-"); if(x<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
  x=2147483648U+11; if(x< 0U) printf("T"); else printf("-"); if(x< 0U) printf("T"); else printf("-");
  x=2147483648U+11; if(x< 1U) printf("T"); else printf("-"); if(x< 1U) printf("T"); else printf("-");
  x=2147483648U+11; if(x<19U) printf("T"); else printf("-"); if(x<19U) printf("T"); else printf("-");
  x=2147483648U+11; if(x<20U) printf("T"); else printf("-"); if(x<20U) printf("T"); else printf("-");
  x=2147483648U+11; if(x<2147483648U+10) printf("T"); else printf("-"); if(x<2147483648U+10) printf("T"); else printf("-");
  x=2147483648U+11; if(x<2147483648U+11) printf("T"); else printf("-"); if(x<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
}

void main2() {
  unsigned int x;
  x=0U; if(x<0U) printf("T"); else printf("-"); if(x<0U) printf("T"); else printf("-");
  x=0U; if(x<4294967294U) printf("T"); else printf("-"); if(x<4294967294U) printf("T"); else printf("-");
  x=0U; if(x<4294967295U) printf("T"); else printf("-"); if(x<4294967295U) printf("T"); else printf("-");
  printf("\n");
  x=4294967294U; if(x<0U) printf("T"); else printf("-"); if(x<0U) printf("T"); else printf("-");
  x=4294967294U; if(x<4294967294U) printf("T"); else printf("-"); if(x<4294967294U) printf("T"); else printf("-");
  x=4294967294U; if(x<4294967295U) printf("T"); else printf("-"); if(x<4294967295U) printf("T"); else printf("-");
  printf("\n");
  x=4294967295U; if(x<0U) printf("T"); else printf("-"); if(x<0U) printf("T"); else printf("-");
  x=4294967295U; if(x<4294967294U) printf("T"); else printf("-"); if(x<4294967294U) printf("T"); else printf("-");
  x=4294967295U; if(x<4294967295U) printf("T"); else printf("-"); if(x<4294967295U) printf("T"); else printf("-");
  printf("\n");
}

void main3() {
  unsigned int x;
  x=0U; if( 0U<x) printf("T"); else printf("-"); if( 0U<x) printf("T"); else printf("-");
  x=0U; if( 1U<x) printf("T"); else printf("-"); if( 1U<x) printf("T"); else printf("-");
  x=0U; if(19U<x) printf("T"); else printf("-"); if(19U<x) printf("T"); else printf("-");
  x=0U; if(20U<x) printf("T"); else printf("-"); if(20U<x) printf("T"); else printf("-");
  x=0U; if(2147483648U+10<x) printf("T"); else printf("-"); if(2147483648U+10<x) printf("T"); else printf("-");
  x=0U; if(2147483648U+11<x) printf("T"); else printf("-"); if(2147483648U+11<x) printf("T"); else printf("-");
  printf("\n");
  x=1U; if( 0U<x) printf("T"); else printf("-"); if( 0U<x) printf("T"); else printf("-");
  x=1U; if( 1U<x) printf("T"); else printf("-"); if( 1U<x) printf("T"); else printf("-");
  x=1U; if(19U<x) printf("T"); else printf("-"); if(19U<x) printf("T"); else printf("-");
  x=1U; if(20U<x) printf("T"); else printf("-"); if(20U<x) printf("T"); else printf("-");
  x=1U; if(2147483648U+10<x) printf("T"); else printf("-"); if(2147483648U+10<x) printf("T"); else printf("-");
  x=1U; if(2147483648U+11<x) printf("T"); else printf("-"); if(2147483648U+11<x) printf("T"); else printf("-");
  printf("\n");
  x=19U; if( 0U<x) printf("T"); else printf("-"); if( 0U<x) printf("T"); else printf("-");
  x=19U; if( 1U<x) printf("T"); else printf("-"); if( 1U<x) printf("T"); else printf("-");
  x=19U; if(19U<x) printf("T"); else printf("-"); if(19U<x) printf("T"); else printf("-");
  x=19U; if(20U<x) printf("T"); else printf("-"); if(20U<x) printf("T"); else printf("-");
  x=19U; if(2147483648U+10<x) printf("T"); else printf("-"); if(2147483648U+10<x) printf("T"); else printf("-");
  x=19U; if(2147483648U+11<x) printf("T"); else printf("-"); if(2147483648U+11<x) printf("T"); else printf("-");
  printf("\n");
  x=20U; if( 0U<x) printf("T"); else printf("-"); if( 0U<x) printf("T"); else printf("-");
  x=20U; if( 1U<x) printf("T"); else printf("-"); if( 1U<x) printf("T"); else printf("-");
  x=20U; if(19U<x) printf("T"); else printf("-"); if(19U<x) printf("T"); else printf("-");
  x=20U; if(20U<x) printf("T"); else printf("-"); if(20U<x) printf("T"); else printf("-");
  x=20U; if(2147483648U+10<x) printf("T"); else printf("-"); if(2147483648U+10<x) printf("T"); else printf("-");
  x=20U; if(2147483648U+11<x) printf("T"); else printf("-"); if(2147483648U+11<x) printf("T"); else printf("-");
  printf("\n");
  x=2147483648U+10; if( 0U<x) printf("T"); else printf("-"); if( 0U<x) printf("T"); else printf("-");
  x=2147483648U+10; if( 1U<x) printf("T"); else printf("-"); if( 1U<x) printf("T"); else printf("-");
  x=2147483648U+10; if(19U<x) printf("T"); else printf("-"); if(19U<x) printf("T"); else printf("-");
  x=2147483648U+10; if(20U<x) printf("T"); else printf("-"); if(20U<x) printf("T"); else printf("-");
  x=2147483648U+10; if(2147483648U+10<x) printf("T"); else printf("-"); if(2147483648U+10<x) printf("T"); else printf("-");
  x=2147483648U+10; if(2147483648U+11<x) printf("T"); else printf("-"); if(2147483648U+11<x) printf("T"); else printf("-");
  printf("\n");
  x=2147483648U+11; if( 0U<x) printf("T"); else printf("-"); if( 0U<x) printf("T"); else printf("-");
  x=2147483648U+11; if( 1U<x) printf("T"); else printf("-"); if( 1U<x) printf("T"); else printf("-");
  x=2147483648U+11; if(19U<x) printf("T"); else printf("-"); if(19U<x) printf("T"); else printf("-");
  x=2147483648U+11; if(20U<x) printf("T"); else printf("-"); if(20U<x) printf("T"); else printf("-");
  x=2147483648U+11; if(2147483648U+10<x) printf("T"); else printf("-"); if(2147483648U+10<x) printf("T"); else printf("-");
  x=2147483648U+11; if(2147483648U+11<x) printf("T"); else printf("-"); if(2147483648U+11<x) printf("T"); else printf("-");
  printf("\n");
}

void main4() {
  unsigned int x;
  x=0U; if(0U<x) printf("T"); else printf("-"); if(0U<x) printf("T"); else printf("-");
  x=0U; if(4294967294U<x) printf("T"); else printf("-"); if(4294967294U<x) printf("T"); else printf("-");
  x=0U; if(4294967295U<x) printf("T"); else printf("-"); if(4294967295U<x) printf("T"); else printf("-");
  printf("\n");
  x=4294967294U; if(0U<x) printf("T"); else printf("-"); if(0U<x) printf("T"); else printf("-");
  x=4294967294U; if(4294967294U<x) printf("T"); else printf("-"); if(4294967294U<x) printf("T"); else printf("-");
  x=4294967294U; if(4294967295U<x) printf("T"); else printf("-"); if(4294967295U<x) printf("T"); else printf("-");
  printf("\n");
  x=4294967295U; if(0U<x) printf("T"); else printf("-"); if(0U<x) printf("T"); else printf("-");
  x=4294967295U; if(4294967294U<x) printf("T"); else printf("-"); if(4294967294U<x) printf("T"); else printf("-");
  x=4294967295U; if(4294967295U<x) printf("T"); else printf("-"); if(4294967295U<x) printf("T"); else printf("-");
  printf("\n");
}

int main() {
  if( 0U< 0U) printf("T"); else printf("-");
  if( 0U< 1U) printf("T"); else printf("-");
  if( 0U<19U) printf("T"); else printf("-");
  if( 0U<20U) printf("T"); else printf("-");
  if( 0U<2147483648U+10) printf("T"); else printf("-");
  if( 0U<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
  if( 1U< 0U) printf("T"); else printf("-");
  if( 1U< 1U) printf("T"); else printf("-");
  if( 1U<19U) printf("T"); else printf("-");
  if( 1U<20U) printf("T"); else printf("-");
  if( 1U<2147483648U+10) printf("T"); else printf("-");
  if( 1U<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
  if(19U< 0U) printf("T"); else printf("-");
  if(19U< 1U) printf("T"); else printf("-");
  if(19U<19U) printf("T"); else printf("-");
  if(19U<20U) printf("T"); else printf("-");
  if(19U<2147483648U+10) printf("T"); else printf("-");
  if(19U<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
  if(20U< 0U) printf("T"); else printf("-");
  if(20U< 1U) printf("T"); else printf("-");
  if(20U<19U) printf("T"); else printf("-");
  if(20U<20U) printf("T"); else printf("-");
  if(20U<2147483648U+10) printf("T"); else printf("-");
  if(20U<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
  if(2147483648U+10< 0U) printf("T"); else printf("-");
  if(2147483648U+10< 1U) printf("T"); else printf("-");
  if(2147483648U+10<19U) printf("T"); else printf("-");
  if(2147483648U+10<20U) printf("T"); else printf("-");
  if(2147483648U+10<2147483648U+10) printf("T"); else printf("-");
  if(2147483648U+10<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
  if(2147483648U+11< 0U) printf("T"); else printf("-");
  if(2147483648U+11< 1U) printf("T"); else printf("-");
  if(2147483648U+11<19U) printf("T"); else printf("-");
  if(2147483648U+11<20U) printf("T"); else printf("-");
  if(2147483648U+11<2147483648U+10) printf("T"); else printf("-");
  if(2147483648U+11<2147483648U+11) printf("T"); else printf("-");
  printf("\n");
  if(         0U<         0U) printf("T"); else printf("-");
  if(         0U<4294967294U) printf("T"); else printf("-");
  if(         0U<4294967295U) printf("T"); else printf("-");
  printf("\n");
  if(4294967294U<         0U) printf("T"); else printf("-");
  if(4294967294U<4294967294U) printf("T"); else printf("-");
  if(4294967294U<4294967295U) printf("T"); else printf("-");
  printf("\n");
  if(4294967295U<         0U) printf("T"); else printf("-");
  if(4294967295U<4294967294U) printf("T"); else printf("-");
  if(4294967295U<4294967295U) printf("T"); else printf("-");
  printf("\n");

  main1();
  main2();
  main3();
  main4();

  f0(0U); f0(1U); f0(19U); f0(20U); f0(2147483648U+10); f0(2147483648U+11);
  g0(0U); g0(4294967294U); g0(4294967295U);

  f1(0U); f1(1U); f1(19U); f1(20U); f1(2147483648U+10); f1(2147483648U+11);
  g1(0U); g1(4294967294U); g1(4294967295U);

  f2(0U); f2(1U); f2(19U); f2(20U); f2(2147483648U+10); f2(2147483648U+11);
  g2(0U); g2(4294967294U); g2(4294967295U);

  return 0;
}
