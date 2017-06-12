int printf(char *s, ...);

void f0(int x) {
  if(x==-9) printf("T"); else printf("-");
  if(x==-8) printf("T"); else printf("-");
  if(x==-1) printf("T"); else printf("-");
  if(x== 0) printf("T"); else printf("-");
  if(x== 1) printf("T"); else printf("-");
  if(x==20) printf("T"); else printf("-");
  if(x==21) printf("T"); else printf("-");
  printf("\n");
}

void f1(int x) {
  if(-9==x) printf("T"); else printf("-");
  if(-8==x) printf("T"); else printf("-");
  if(-1==x) printf("T"); else printf("-");
  if( 0==x) printf("T"); else printf("-");
  if( 1==x) printf("T"); else printf("-");
  if(20==x) printf("T"); else printf("-");
  if(21==x) printf("T"); else printf("-");
  printf("\n");
}

void g0(int x) {
  if(x==-2147483647-1) printf("T"); else printf("-");
  if(x==-2147483647) printf("T"); else printf("-");
  if(x==          0) printf("T"); else printf("-");
  if(x== 2147483646) printf("T"); else printf("-");
  if(x== 2147483647) printf("T"); else printf("-");
  printf("\n");
}

void g1(int x) {
  if(-2147483647-1==x) printf("T"); else printf("-");
  if(-2147483647==x) printf("T"); else printf("-");
  if(          0==x) printf("T"); else printf("-");
  if( 2147483646==x) printf("T"); else printf("-");
  if( 2147483647==x) printf("T"); else printf("-");
  printf("\n");
}

void op(int x,int y) {
  if(x==y) printf("T"); else printf("-");
}

void f2(int x) {
  op(x,-9); op(x,-8); op(x,-1); op(x,0); op(x,1); op(x,20); op(x,21);
  printf("\n");
}

void g2(int x) {
  op(x,-2147483647-1); op(x,-2147483647); op(x,0); op(x,2147483646); op(x,2147483647);
  printf("\n");
}

void main1() {
  int x;
  x=-9; if(x==-9) printf("T"); else printf("-"); if(x==-9) printf("T"); else printf("-");
  x=-9; if(x==-8) printf("T"); else printf("-"); if(x==-8) printf("T"); else printf("-");
  x=-9; if(x==-1) printf("T"); else printf("-"); if(x==-1) printf("T"); else printf("-");
  x=-9; if(x== 0) printf("T"); else printf("-"); if(x== 0) printf("T"); else printf("-");
  x=-9; if(x== 1) printf("T"); else printf("-"); if(x== 1) printf("T"); else printf("-");
  x=-9; if(x==20) printf("T"); else printf("-"); if(x==20) printf("T"); else printf("-");
  x=-9; if(x==21) printf("T"); else printf("-"); if(x==21) printf("T"); else printf("-");
  printf("\n");
  x=-8; if(x==-9) printf("T"); else printf("-"); if(x==-9) printf("T"); else printf("-");
  x=-8; if(x==-8) printf("T"); else printf("-"); if(x==-8) printf("T"); else printf("-");
  x=-8; if(x==-1) printf("T"); else printf("-"); if(x==-1) printf("T"); else printf("-");
  x=-8; if(x== 0) printf("T"); else printf("-"); if(x== 0) printf("T"); else printf("-");
  x=-8; if(x== 1) printf("T"); else printf("-"); if(x== 1) printf("T"); else printf("-");
  x=-8; if(x==20) printf("T"); else printf("-"); if(x==20) printf("T"); else printf("-");
  x=-8; if(x==21) printf("T"); else printf("-"); if(x==21) printf("T"); else printf("-");
  printf("\n");
  x=-1; if(x==-9) printf("T"); else printf("-"); if(x==-9) printf("T"); else printf("-");
  x=-1; if(x==-8) printf("T"); else printf("-"); if(x==-8) printf("T"); else printf("-");
  x=-1; if(x==-1) printf("T"); else printf("-"); if(x==-1) printf("T"); else printf("-");
  x=-1; if(x== 0) printf("T"); else printf("-"); if(x== 0) printf("T"); else printf("-");
  x=-1; if(x== 1) printf("T"); else printf("-"); if(x== 1) printf("T"); else printf("-");
  x=-1; if(x==20) printf("T"); else printf("-"); if(x==20) printf("T"); else printf("-");
  x=-1; if(x==21) printf("T"); else printf("-"); if(x==21) printf("T"); else printf("-");
  printf("\n");
  x= 0; if(x==-9) printf("T"); else printf("-"); if(x==-9) printf("T"); else printf("-");
  x= 0; if(x==-8) printf("T"); else printf("-"); if(x==-8) printf("T"); else printf("-");
  x= 0; if(x==-1) printf("T"); else printf("-"); if(x==-1) printf("T"); else printf("-");
  x= 0; if(x== 0) printf("T"); else printf("-"); if(x== 0) printf("T"); else printf("-");
  x= 0; if(x== 1) printf("T"); else printf("-"); if(x== 1) printf("T"); else printf("-");
  x= 0; if(x==20) printf("T"); else printf("-"); if(x==20) printf("T"); else printf("-");
  x= 0; if(x==21) printf("T"); else printf("-"); if(x==21) printf("T"); else printf("-");
  printf("\n");
  x= 1; if(x==-9) printf("T"); else printf("-"); if(x==-9) printf("T"); else printf("-");
  x= 1; if(x==-8) printf("T"); else printf("-"); if(x==-8) printf("T"); else printf("-");
  x= 1; if(x==-1) printf("T"); else printf("-"); if(x==-1) printf("T"); else printf("-");
  x= 1; if(x== 0) printf("T"); else printf("-"); if(x== 0) printf("T"); else printf("-");
  x= 1; if(x== 1) printf("T"); else printf("-"); if(x== 1) printf("T"); else printf("-");
  x= 1; if(x==20) printf("T"); else printf("-"); if(x==20) printf("T"); else printf("-");
  x= 1; if(x==21) printf("T"); else printf("-"); if(x==21) printf("T"); else printf("-");
  printf("\n");
  x=20; if(x==-9) printf("T"); else printf("-"); if(x==-9) printf("T"); else printf("-");
  x=20; if(x==-8) printf("T"); else printf("-"); if(x==-8) printf("T"); else printf("-");
  x=20; if(x==-1) printf("T"); else printf("-"); if(x==-1) printf("T"); else printf("-");
  x=20; if(x== 0) printf("T"); else printf("-"); if(x== 0) printf("T"); else printf("-");
  x=20; if(x== 1) printf("T"); else printf("-"); if(x== 1) printf("T"); else printf("-");
  x=20; if(x==20) printf("T"); else printf("-"); if(x==20) printf("T"); else printf("-");
  x=20; if(x==21) printf("T"); else printf("-"); if(x==21) printf("T"); else printf("-");
  printf("\n");
  x=21; if(x==-9) printf("T"); else printf("-"); if(x==-9) printf("T"); else printf("-");
  x=21; if(x==-8) printf("T"); else printf("-"); if(x==-8) printf("T"); else printf("-");
  x=21; if(x==-1) printf("T"); else printf("-"); if(x==-1) printf("T"); else printf("-");
  x=21; if(x== 0) printf("T"); else printf("-"); if(x== 0) printf("T"); else printf("-");
  x=21; if(x== 1) printf("T"); else printf("-"); if(x== 1) printf("T"); else printf("-");
  x=21; if(x==20) printf("T"); else printf("-"); if(x==20) printf("T"); else printf("-");
  x=21; if(x==21) printf("T"); else printf("-"); if(x==21) printf("T"); else printf("-");
  printf("\n");
}

void main2() {
  int x;
  x=-2147483647-1; if(x==-2147483647-1) printf("T"); else printf("-"); if(x==-2147483647-1) printf("T"); else printf("-");
  x=-2147483647-1; if(x==-2147483647) printf("T"); else printf("-"); if(x==-2147483647) printf("T"); else printf("-");
  x=-2147483647-1; if(x== 2147483646) printf("T"); else printf("-"); if(x== 2147483646) printf("T"); else printf("-");
  x=-2147483647-1; if(x== 2147483647) printf("T"); else printf("-"); if(x== 2147483647) printf("T"); else printf("-");
  printf("\n");
  x=-2147483647; if(x==-2147483647-1) printf("T"); else printf("-"); if(x==-2147483647-1) printf("T"); else printf("-");
  x=-2147483647; if(x==-2147483647) printf("T"); else printf("-"); if(x==-2147483647) printf("T"); else printf("-");
  x=-2147483647; if(x== 2147483646) printf("T"); else printf("-"); if(x== 2147483646) printf("T"); else printf("-");
  x=-2147483647; if(x== 2147483647) printf("T"); else printf("-"); if(x== 2147483647) printf("T"); else printf("-");
  printf("\n");
  x= 2147483646; if(x==-2147483647-1) printf("T"); else printf("-"); if(x==-2147483647-1) printf("T"); else printf("-");
  x= 2147483646; if(x==-2147483647) printf("T"); else printf("-"); if(x==-2147483647) printf("T"); else printf("-");
  x= 2147483646; if(x== 2147483646) printf("T"); else printf("-"); if(x== 2147483646) printf("T"); else printf("-");
  x= 2147483646; if(x== 2147483647) printf("T"); else printf("-"); if(x== 2147483647) printf("T"); else printf("-");
  printf("\n");
  x= 2147483647; if(x==-2147483647-1) printf("T"); else printf("-"); if(x==-2147483647-1) printf("T"); else printf("-");
  x= 2147483647; if(x==-2147483647) printf("T"); else printf("-"); if(x==-2147483647) printf("T"); else printf("-");
  x= 2147483647; if(x== 2147483646) printf("T"); else printf("-"); if(x== 2147483646) printf("T"); else printf("-");
  x= 2147483647; if(x== 2147483647) printf("T"); else printf("-"); if(x== 2147483647) printf("T"); else printf("-");
  printf("\n");
}

void main3() {
  int x;
  x=-9; if(-9==x) printf("T"); else printf("-"); if(-9==x) printf("T"); else printf("-");
  x=-9; if(-8==x) printf("T"); else printf("-"); if(-8==x) printf("T"); else printf("-");
  x=-9; if(-1==x) printf("T"); else printf("-"); if(-1==x) printf("T"); else printf("-");
  x=-9; if( 0==x) printf("T"); else printf("-"); if( 0==x) printf("T"); else printf("-");
  x=-9; if( 1==x) printf("T"); else printf("-"); if( 1==x) printf("T"); else printf("-");
  x=-9; if(20==x) printf("T"); else printf("-"); if(20==x) printf("T"); else printf("-");
  x=-9; if(21==x) printf("T"); else printf("-"); if(21==x) printf("T"); else printf("-");
  printf("\n");
  x=-8; if(-9==x) printf("T"); else printf("-"); if(-9==x) printf("T"); else printf("-");
  x=-8; if(-8==x) printf("T"); else printf("-"); if(-8==x) printf("T"); else printf("-");
  x=-8; if(-1==x) printf("T"); else printf("-"); if(-1==x) printf("T"); else printf("-");
  x=-8; if( 0==x) printf("T"); else printf("-"); if( 0==x) printf("T"); else printf("-");
  x=-8; if( 1==x) printf("T"); else printf("-"); if( 1==x) printf("T"); else printf("-");
  x=-8; if(20==x) printf("T"); else printf("-"); if(20==x) printf("T"); else printf("-");
  x=-8; if(21==x) printf("T"); else printf("-"); if(21==x) printf("T"); else printf("-");
  printf("\n");
  x=-1; if(-9==x) printf("T"); else printf("-"); if(-9==x) printf("T"); else printf("-");
  x=-1; if(-8==x) printf("T"); else printf("-"); if(-8==x) printf("T"); else printf("-");
  x=-1; if(-1==x) printf("T"); else printf("-"); if(-1==x) printf("T"); else printf("-");
  x=-1; if( 0==x) printf("T"); else printf("-"); if( 0==x) printf("T"); else printf("-");
  x=-1; if( 1==x) printf("T"); else printf("-"); if( 1==x) printf("T"); else printf("-");
  x=-1; if(20==x) printf("T"); else printf("-"); if(20==x) printf("T"); else printf("-");
  x=-1; if(21==x) printf("T"); else printf("-"); if(21==x) printf("T"); else printf("-");
  printf("\n");
  x= 0; if(-9==x) printf("T"); else printf("-"); if(-9==x) printf("T"); else printf("-");
  x= 0; if(-8==x) printf("T"); else printf("-"); if(-8==x) printf("T"); else printf("-");
  x= 0; if(-1==x) printf("T"); else printf("-"); if(-1==x) printf("T"); else printf("-");
  x= 0; if( 0==x) printf("T"); else printf("-"); if( 0==x) printf("T"); else printf("-");
  x= 0; if( 1==x) printf("T"); else printf("-"); if( 1==x) printf("T"); else printf("-");
  x= 0; if(20==x) printf("T"); else printf("-"); if(20==x) printf("T"); else printf("-");
  x= 0; if(21==x) printf("T"); else printf("-"); if(21==x) printf("T"); else printf("-");
  printf("\n");
  x= 1; if(-9==x) printf("T"); else printf("-"); if(-9==x) printf("T"); else printf("-");
  x= 1; if(-8==x) printf("T"); else printf("-"); if(-8==x) printf("T"); else printf("-");
  x= 1; if(-1==x) printf("T"); else printf("-"); if(-1==x) printf("T"); else printf("-");
  x= 1; if( 0==x) printf("T"); else printf("-"); if( 0==x) printf("T"); else printf("-");
  x= 1; if( 1==x) printf("T"); else printf("-"); if( 1==x) printf("T"); else printf("-");
  x= 1; if(20==x) printf("T"); else printf("-"); if(20==x) printf("T"); else printf("-");
  x= 1; if(21==x) printf("T"); else printf("-"); if(21==x) printf("T"); else printf("-");
  printf("\n");
  x=20; if(-9==x) printf("T"); else printf("-"); if(-9==x) printf("T"); else printf("-");
  x=20; if(-8==x) printf("T"); else printf("-"); if(-8==x) printf("T"); else printf("-");
  x=20; if(-1==x) printf("T"); else printf("-"); if(-1==x) printf("T"); else printf("-");
  x=20; if( 0==x) printf("T"); else printf("-"); if( 0==x) printf("T"); else printf("-");
  x=20; if( 1==x) printf("T"); else printf("-"); if( 1==x) printf("T"); else printf("-");
  x=20; if(20==x) printf("T"); else printf("-"); if(20==x) printf("T"); else printf("-");
  x=20; if(21==x) printf("T"); else printf("-"); if(21==x) printf("T"); else printf("-");
  printf("\n");
  x=21; if(-9==x) printf("T"); else printf("-"); if(-9==x) printf("T"); else printf("-");
  x=21; if(-8==x) printf("T"); else printf("-"); if(-8==x) printf("T"); else printf("-");
  x=21; if(-1==x) printf("T"); else printf("-"); if(-1==x) printf("T"); else printf("-");
  x=21; if( 0==x) printf("T"); else printf("-"); if( 0==x) printf("T"); else printf("-");
  x=21; if( 1==x) printf("T"); else printf("-"); if( 1==x) printf("T"); else printf("-");
  x=21; if(20==x) printf("T"); else printf("-"); if(20==x) printf("T"); else printf("-");
  x=21; if(21==x) printf("T"); else printf("-"); if(21==x) printf("T"); else printf("-");
  printf("\n");
}

void main4() {
  int x;
  x=-2147483647-1; if(-2147483647-1==x) printf("T"); else printf("-"); if(-2147483647-1==x) printf("T"); else printf("-");
  x=-2147483647-1; if(-2147483647==x) printf("T"); else printf("-"); if(-2147483647==x) printf("T"); else printf("-");
  x=-2147483647-1; if( 2147483646==x) printf("T"); else printf("-"); if( 2147483646==x) printf("T"); else printf("-");
  x=-2147483647-1; if( 2147483647==x) printf("T"); else printf("-"); if( 2147483647==x) printf("T"); else printf("-");
  printf("\n");
  x=-2147483647; if(-2147483647-1==x) printf("T"); else printf("-"); if(-2147483647-1==x) printf("T"); else printf("-");
  x=-2147483647; if(-2147483647==x) printf("T"); else printf("-"); if(-2147483647==x) printf("T"); else printf("-");
  x=-2147483647; if( 2147483646==x) printf("T"); else printf("-"); if( 2147483646==x) printf("T"); else printf("-");
  x=-2147483647; if( 2147483647==x) printf("T"); else printf("-"); if( 2147483647==x) printf("T"); else printf("-");
  printf("\n");
  x= 2147483646; if(-2147483647-1==x) printf("T"); else printf("-"); if(-2147483647-1==x) printf("T"); else printf("-");
  x= 2147483646; if(-2147483647==x) printf("T"); else printf("-"); if(-2147483647==x) printf("T"); else printf("-");
  x= 2147483646; if( 2147483646==x) printf("T"); else printf("-"); if( 2147483646==x) printf("T"); else printf("-");
  x= 2147483646; if( 2147483647==x) printf("T"); else printf("-"); if( 2147483647==x) printf("T"); else printf("-");
  printf("\n");
  x= 2147483647; if(-2147483647-1==x) printf("T"); else printf("-"); if(-2147483647-1==x) printf("T"); else printf("-");
  x= 2147483647; if(-2147483647==x) printf("T"); else printf("-"); if(-2147483647==x) printf("T"); else printf("-");
  x= 2147483647; if( 2147483646==x) printf("T"); else printf("-"); if( 2147483646==x) printf("T"); else printf("-");
  x= 2147483647; if( 2147483647==x) printf("T"); else printf("-"); if( 2147483647==x) printf("T"); else printf("-");
  printf("\n");
}

int main() {
  if(-9==-9) printf("T"); else printf("-");
  if(-9==-8) printf("T"); else printf("-");
  if(-9==-1) printf("T"); else printf("-");
  if(-9== 0) printf("T"); else printf("-");
  if(-9== 1) printf("T"); else printf("-");
  if(-9==20) printf("T"); else printf("-");
  if(-9==21) printf("T"); else printf("-");
  printf("\n");
  if(-8==-9) printf("T"); else printf("-");
  if(-8==-8) printf("T"); else printf("-");
  if(-8==-1) printf("T"); else printf("-");
  if(-8== 0) printf("T"); else printf("-");
  if(-8== 1) printf("T"); else printf("-");
  if(-8==20) printf("T"); else printf("-");
  if(-8==21) printf("T"); else printf("-");
  printf("\n");
  if(-1==-9) printf("T"); else printf("-");
  if(-1==-8) printf("T"); else printf("-");
  if(-1==-1) printf("T"); else printf("-");
  if(-1== 0) printf("T"); else printf("-");
  if(-1== 1) printf("T"); else printf("-");
  if(-1==20) printf("T"); else printf("-");
  if(-1==21) printf("T"); else printf("-");
  printf("\n");
  if( 0==-9) printf("T"); else printf("-");
  if( 0==-8) printf("T"); else printf("-");
  if( 0==-1) printf("T"); else printf("-");
  if( 0== 0) printf("T"); else printf("-");
  if( 0== 1) printf("T"); else printf("-");
  if( 0==20) printf("T"); else printf("-");
  if( 0==21) printf("T"); else printf("-");
  printf("\n");
  if( 1==-9) printf("T"); else printf("-");
  if( 1==-8) printf("T"); else printf("-");
  if( 1==-1) printf("T"); else printf("-");
  if( 1== 0) printf("T"); else printf("-");
  if( 1== 1) printf("T"); else printf("-");
  if( 1==20) printf("T"); else printf("-");
  if( 1==21) printf("T"); else printf("-");
  printf("\n");
  if(20==-9) printf("T"); else printf("-");
  if(20==-8) printf("T"); else printf("-");
  if(20==-1) printf("T"); else printf("-");
  if(20== 0) printf("T"); else printf("-");
  if(20== 1) printf("T"); else printf("-");
  if(20==20) printf("T"); else printf("-");
  if(20==21) printf("T"); else printf("-");
  printf("\n");
  if(21==-9) printf("T"); else printf("-");
  if(21==-8) printf("T"); else printf("-");
  if(21==-1) printf("T"); else printf("-");
  if(21== 0) printf("T"); else printf("-");
  if(21== 1) printf("T"); else printf("-");
  if(21==20) printf("T"); else printf("-");
  if(21==21) printf("T"); else printf("-");
  printf("\n");
  if(-2147483647-1==-2147483647-1) printf("T"); else printf("-");
  if(-2147483647-1==-2147483647) printf("T"); else printf("-");
  if(-2147483647-1== 2147483646) printf("T"); else printf("-");
  if(-2147483647-1== 2147483647) printf("T"); else printf("-");
  printf("\n");
  if(-2147483647==-2147483647-1) printf("T"); else printf("-");
  if(-2147483647==-2147483647) printf("T"); else printf("-");
  if(-2147483647== 2147483646) printf("T"); else printf("-");
  if(-2147483647== 2147483647) printf("T"); else printf("-");
  printf("\n");
  if( 2147483646==-2147483647-1) printf("T"); else printf("-");
  if( 2147483646==-2147483647) printf("T"); else printf("-");
  if( 2147483646== 2147483646) printf("T"); else printf("-");
  if( 2147483646== 2147483647) printf("T"); else printf("-");
  printf("\n");
  if( 2147483647==-2147483647-1) printf("T"); else printf("-");
  if( 2147483647==-2147483647) printf("T"); else printf("-");
  if( 2147483647== 2147483646) printf("T"); else printf("-");
  if( 2147483647== 2147483647) printf("T"); else printf("-");
  printf("\n");

  main1();
  main2();
  main3();
  main4();

  f0(-9); f0(-8); f0(-1); f0(0); f0(1); f0(20); f0(21);
  g0(-2147483647-1); g0(-2147483647); g0(0); g0(2147483646); g0(2147483647);

  f1(-9); f1(-8); f1(-1); f1(0); f1(1); f1(20); f1(21);
  g1(-2147483647-1); g1(-2147483647); g1(0); g1(2147483646); g1(2147483647);

  f2(-9); f2(-8); f2(-1); f2(0); f2(1); f2(20); f2(21);
  g2(-2147483647-1); g2(-2147483647); g2(0); g2(2147483646); g2(2147483647);

  return 0;
}
