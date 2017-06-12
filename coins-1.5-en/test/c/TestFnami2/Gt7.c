int printf(char *s, ...);

void f0(float x) {
  if(x>-3.402823466e+38F) printf("T"); else printf("-");
  if(x>            -2.7F) printf("T"); else printf("-");
  if(x>-1.175494351e-38F) printf("T"); else printf("-");
  if(x>            -0.0F) printf("T"); else printf("-");
  if(x>             0.0F) printf("T"); else printf("-");
  if(x> 1.175494351e-38F) printf("T"); else printf("-");
  if(x>             2.7F) printf("T"); else printf("-");
  if(x> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
}

void f1(float x) {
  if(-3.402823466e+38F>x) printf("T"); else printf("-");
  if(            -2.7F>x) printf("T"); else printf("-");
  if(-1.175494351e-38F>x) printf("T"); else printf("-");
  if(            -0.0F>x) printf("T"); else printf("-");
  if(             0.0F>x) printf("T"); else printf("-");
  if( 1.175494351e-38F>x) printf("T"); else printf("-");
  if(             2.7F>x) printf("T"); else printf("-");
  if( 3.402823466e+38F>x) printf("T"); else printf("-");
  printf("\n");
}

void g0(float x) {
  if(x>    2.71828F) printf("T"); else printf("-");
  if(x> 2.71828182F) printf("T"); else printf("-");
  if(x>2.718281828F) printf("T"); else printf("-");
  printf("\n");
}

void g1(float x) {
  if(    2.71828F>x) printf("T"); else printf("-");
  if( 2.71828182F>x) printf("T"); else printf("-");
  if(2.718281828F>x) printf("T"); else printf("-");
  printf("\n");
}

void op(float x,float y) {
  if(x>y) printf("T"); else printf("-");
}

void f2(float x) {
  op(x,-3.402823466e+38F); op(x,-2.7F); op(x,-1.175494351e-38F); op(x,-0.0F);
  op(x,0.0F); op(x,1.175494351e-38F); op(x,2.7F); op(x,3.402823466e+38F);
  printf("\n");
}

void g2(float x) {
  op(x,2.71828F); op(x,2.71828182F); op(x,2.718281828F);
  printf("\n");
}

void main1() {
  float x;
  x=-3.402823466e+38F; if(x>-3.402823466e+38F) printf("T"); else printf("-"); if(x>-3.402823466e+38F) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(x>            -2.7F) printf("T"); else printf("-"); if(x>            -2.7F) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(x>-1.175494351e-38F) printf("T"); else printf("-"); if(x>-1.175494351e-38F) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(x>            -0.0F) printf("T"); else printf("-"); if(x>            -0.0F) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(x>             0.0F) printf("T"); else printf("-"); if(x>             0.0F) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(x> 1.175494351e-38F) printf("T"); else printf("-"); if(x> 1.175494351e-38F) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(x>             2.7F) printf("T"); else printf("-"); if(x>             2.7F) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(x> 3.402823466e+38F) printf("T"); else printf("-"); if(x> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  x=            -2.7F; if(x>-3.402823466e+38F) printf("T"); else printf("-"); if(x>-3.402823466e+38F) printf("T"); else printf("-");
  x=            -2.7F; if(x>            -2.7F) printf("T"); else printf("-"); if(x>            -2.7F) printf("T"); else printf("-");
  x=            -2.7F; if(x>-1.175494351e-38F) printf("T"); else printf("-"); if(x>-1.175494351e-38F) printf("T"); else printf("-");
  x=            -2.7F; if(x>            -0.0F) printf("T"); else printf("-"); if(x>            -0.0F) printf("T"); else printf("-");
  x=            -2.7F; if(x>             0.0F) printf("T"); else printf("-"); if(x>             0.0F) printf("T"); else printf("-");
  x=            -2.7F; if(x> 1.175494351e-38F) printf("T"); else printf("-"); if(x> 1.175494351e-38F) printf("T"); else printf("-");
  x=            -2.7F; if(x>             2.7F) printf("T"); else printf("-"); if(x>             2.7F) printf("T"); else printf("-");
  x=            -2.7F; if(x> 3.402823466e+38F) printf("T"); else printf("-"); if(x> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  x=-1.175494351e-38F; if(x>-3.402823466e+38F) printf("T"); else printf("-"); if(x>-3.402823466e+38F) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(x>            -2.7F) printf("T"); else printf("-"); if(x>            -2.7F) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(x>-1.175494351e-38F) printf("T"); else printf("-"); if(x>-1.175494351e-38F) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(x>            -0.0F) printf("T"); else printf("-"); if(x>            -0.0F) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(x>             0.0F) printf("T"); else printf("-"); if(x>             0.0F) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(x> 1.175494351e-38F) printf("T"); else printf("-"); if(x> 1.175494351e-38F) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(x>             2.7F) printf("T"); else printf("-"); if(x>             2.7F) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(x> 3.402823466e+38F) printf("T"); else printf("-"); if(x> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  x=            -0.0F; if(x>-3.402823466e+38F) printf("T"); else printf("-"); if(x>-3.402823466e+38F) printf("T"); else printf("-");
  x=            -0.0F; if(x>            -2.7F) printf("T"); else printf("-"); if(x>            -2.7F) printf("T"); else printf("-");
  x=            -0.0F; if(x>-1.175494351e-38F) printf("T"); else printf("-"); if(x>-1.175494351e-38F) printf("T"); else printf("-");
  x=            -0.0F; if(x>            -0.0F) printf("T"); else printf("-"); if(x>            -0.0F) printf("T"); else printf("-");
  x=            -0.0F; if(x>             0.0F) printf("T"); else printf("-"); if(x>             0.0F) printf("T"); else printf("-");
  x=            -0.0F; if(x> 1.175494351e-38F) printf("T"); else printf("-"); if(x> 1.175494351e-38F) printf("T"); else printf("-");
  x=            -0.0F; if(x>             2.7F) printf("T"); else printf("-"); if(x>             2.7F) printf("T"); else printf("-");
  x=            -0.0F; if(x> 3.402823466e+38F) printf("T"); else printf("-"); if(x> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  x=             0.0F; if(x>-3.402823466e+38F) printf("T"); else printf("-"); if(x>-3.402823466e+38F) printf("T"); else printf("-");
  x=             0.0F; if(x>            -2.7F) printf("T"); else printf("-"); if(x>            -2.7F) printf("T"); else printf("-");
  x=             0.0F; if(x>-1.175494351e-38F) printf("T"); else printf("-"); if(x>-1.175494351e-38F) printf("T"); else printf("-");
  x=             0.0F; if(x>            -0.0F) printf("T"); else printf("-"); if(x>            -0.0F) printf("T"); else printf("-");
  x=             0.0F; if(x>             0.0F) printf("T"); else printf("-"); if(x>             0.0F) printf("T"); else printf("-");
  x=             0.0F; if(x> 1.175494351e-38F) printf("T"); else printf("-"); if(x> 1.175494351e-38F) printf("T"); else printf("-");
  x=             0.0F; if(x>             2.7F) printf("T"); else printf("-"); if(x>             2.7F) printf("T"); else printf("-");
  x=             0.0F; if(x> 3.402823466e+38F) printf("T"); else printf("-"); if(x> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  x= 1.175494351e-38F; if(x>-3.402823466e+38F) printf("T"); else printf("-"); if(x>-3.402823466e+38F) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(x>            -2.7F) printf("T"); else printf("-"); if(x>            -2.7F) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(x>-1.175494351e-38F) printf("T"); else printf("-"); if(x>-1.175494351e-38F) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(x>            -0.0F) printf("T"); else printf("-"); if(x>            -0.0F) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(x>             0.0F) printf("T"); else printf("-"); if(x>             0.0F) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(x> 1.175494351e-38F) printf("T"); else printf("-"); if(x> 1.175494351e-38F) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(x>             2.7F) printf("T"); else printf("-"); if(x>             2.7F) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(x> 3.402823466e+38F) printf("T"); else printf("-"); if(x> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  x=             2.7F; if(x>-3.402823466e+38F) printf("T"); else printf("-"); if(x>-3.402823466e+38F) printf("T"); else printf("-");
  x=             2.7F; if(x>            -2.7F) printf("T"); else printf("-"); if(x>            -2.7F) printf("T"); else printf("-");
  x=             2.7F; if(x>-1.175494351e-38F) printf("T"); else printf("-"); if(x>-1.175494351e-38F) printf("T"); else printf("-");
  x=             2.7F; if(x>            -0.0F) printf("T"); else printf("-"); if(x>            -0.0F) printf("T"); else printf("-");
  x=             2.7F; if(x>             0.0F) printf("T"); else printf("-"); if(x>             0.0F) printf("T"); else printf("-");
  x=             2.7F; if(x> 1.175494351e-38F) printf("T"); else printf("-"); if(x> 1.175494351e-38F) printf("T"); else printf("-");
  x=             2.7F; if(x>             2.7F) printf("T"); else printf("-"); if(x>             2.7F) printf("T"); else printf("-");
  x=             2.7F; if(x> 3.402823466e+38F) printf("T"); else printf("-"); if(x> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  x= 3.402823466e+38F; if(x>-3.402823466e+38F) printf("T"); else printf("-"); if(x>-3.402823466e+38F) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(x>            -2.7F) printf("T"); else printf("-"); if(x>            -2.7F) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(x>-1.175494351e-38F) printf("T"); else printf("-"); if(x>-1.175494351e-38F) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(x>            -0.0F) printf("T"); else printf("-"); if(x>            -0.0F) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(x>             0.0F) printf("T"); else printf("-"); if(x>             0.0F) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(x> 1.175494351e-38F) printf("T"); else printf("-"); if(x> 1.175494351e-38F) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(x>             2.7F) printf("T"); else printf("-"); if(x>             2.7F) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(x> 3.402823466e+38F) printf("T"); else printf("-"); if(x> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
}

void main2() {
  float x;
  x=    2.71828F; if(x>    2.71828F) printf("T"); else printf("-"); if(x>    2.71828F) printf("T"); else printf("-");
  x=    2.71828F; if(x> 2.71828182F) printf("T"); else printf("-"); if(x> 2.71828182F) printf("T"); else printf("-");
  x=    2.71828F; if(x>2.718281828F) printf("T"); else printf("-"); if(x>2.718281828F) printf("T"); else printf("-");
  printf("\n");
  x= 2.71828182F; if(x>    2.71828F) printf("T"); else printf("-"); if(x>    2.71828F) printf("T"); else printf("-");
  x= 2.71828182F; if(x> 2.71828182F) printf("T"); else printf("-"); if(x> 2.71828182F) printf("T"); else printf("-");
  x= 2.71828182F; if(x>2.718281828F) printf("T"); else printf("-"); if(x>2.718281828F) printf("T"); else printf("-");
  printf("\n");
  x=2.718281828F; if(x>    2.71828F) printf("T"); else printf("-"); if(x>    2.71828F) printf("T"); else printf("-");
  x=2.718281828F; if(x> 2.71828182F) printf("T"); else printf("-"); if(x> 2.71828182F) printf("T"); else printf("-");
  x=2.718281828F; if(x>2.718281828F) printf("T"); else printf("-"); if(x>2.718281828F) printf("T"); else printf("-");
  printf("\n");
}

void main3() {
  float x;
  x=-3.402823466e+38F; if(-3.402823466e+38F>x) printf("T"); else printf("-"); if(-3.402823466e+38F>x) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(            -2.7F>x) printf("T"); else printf("-"); if(            -2.7F>x) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(-1.175494351e-38F>x) printf("T"); else printf("-"); if(-1.175494351e-38F>x) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(            -0.0F>x) printf("T"); else printf("-"); if(            -0.0F>x) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(             0.0F>x) printf("T"); else printf("-"); if(             0.0F>x) printf("T"); else printf("-");
  x=-3.402823466e+38F; if( 1.175494351e-38F>x) printf("T"); else printf("-"); if( 1.175494351e-38F>x) printf("T"); else printf("-");
  x=-3.402823466e+38F; if(             2.7F>x) printf("T"); else printf("-"); if(             2.7F>x) printf("T"); else printf("-");
  x=-3.402823466e+38F; if( 3.402823466e+38F>x) printf("T"); else printf("-"); if( 3.402823466e+38F>x) printf("T"); else printf("-");
  printf("\n");
  x=            -2.7F; if(-3.402823466e+38F>x) printf("T"); else printf("-"); if(-3.402823466e+38F>x) printf("T"); else printf("-");
  x=            -2.7F; if(            -2.7F>x) printf("T"); else printf("-"); if(            -2.7F>x) printf("T"); else printf("-");
  x=            -2.7F; if(-1.175494351e-38F>x) printf("T"); else printf("-"); if(-1.175494351e-38F>x) printf("T"); else printf("-");
  x=            -2.7F; if(            -0.0F>x) printf("T"); else printf("-"); if(            -0.0F>x) printf("T"); else printf("-");
  x=            -2.7F; if(             0.0F>x) printf("T"); else printf("-"); if(             0.0F>x) printf("T"); else printf("-");
  x=            -2.7F; if( 1.175494351e-38F>x) printf("T"); else printf("-"); if( 1.175494351e-38F>x) printf("T"); else printf("-");
  x=            -2.7F; if(             2.7F>x) printf("T"); else printf("-"); if(             2.7F>x) printf("T"); else printf("-");
  x=            -2.7F; if( 3.402823466e+38F>x) printf("T"); else printf("-"); if( 3.402823466e+38F>x) printf("T"); else printf("-");
  printf("\n");
  x=-1.175494351e-38F; if(-3.402823466e+38F>x) printf("T"); else printf("-"); if(-3.402823466e+38F>x) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(            -2.7F>x) printf("T"); else printf("-"); if(            -2.7F>x) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(-1.175494351e-38F>x) printf("T"); else printf("-"); if(-1.175494351e-38F>x) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(            -0.0F>x) printf("T"); else printf("-"); if(            -0.0F>x) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(             0.0F>x) printf("T"); else printf("-"); if(             0.0F>x) printf("T"); else printf("-");
  x=-1.175494351e-38F; if( 1.175494351e-38F>x) printf("T"); else printf("-"); if( 1.175494351e-38F>x) printf("T"); else printf("-");
  x=-1.175494351e-38F; if(             2.7F>x) printf("T"); else printf("-"); if(             2.7F>x) printf("T"); else printf("-");
  x=-1.175494351e-38F; if( 3.402823466e+38F>x) printf("T"); else printf("-"); if( 3.402823466e+38F>x) printf("T"); else printf("-");
  printf("\n");
  x=            -0.0F; if(-3.402823466e+38F>x) printf("T"); else printf("-"); if(-3.402823466e+38F>x) printf("T"); else printf("-");
  x=            -0.0F; if(            -2.7F>x) printf("T"); else printf("-"); if(            -2.7F>x) printf("T"); else printf("-");
  x=            -0.0F; if(-1.175494351e-38F>x) printf("T"); else printf("-"); if(-1.175494351e-38F>x) printf("T"); else printf("-");
  x=            -0.0F; if(            -0.0F>x) printf("T"); else printf("-"); if(            -0.0F>x) printf("T"); else printf("-");
  x=            -0.0F; if(             0.0F>x) printf("T"); else printf("-"); if(             0.0F>x) printf("T"); else printf("-");
  x=            -0.0F; if( 1.175494351e-38F>x) printf("T"); else printf("-"); if( 1.175494351e-38F>x) printf("T"); else printf("-");
  x=            -0.0F; if(             2.7F>x) printf("T"); else printf("-"); if(             2.7F>x) printf("T"); else printf("-");
  x=            -0.0F; if( 3.402823466e+38F>x) printf("T"); else printf("-"); if( 3.402823466e+38F>x) printf("T"); else printf("-");
  printf("\n");
  x=             0.0F; if(-3.402823466e+38F>x) printf("T"); else printf("-"); if(-3.402823466e+38F>x) printf("T"); else printf("-");
  x=             0.0F; if(            -2.7F>x) printf("T"); else printf("-"); if(            -2.7F>x) printf("T"); else printf("-");
  x=             0.0F; if(-1.175494351e-38F>x) printf("T"); else printf("-"); if(-1.175494351e-38F>x) printf("T"); else printf("-");
  x=             0.0F; if(            -0.0F>x) printf("T"); else printf("-"); if(            -0.0F>x) printf("T"); else printf("-");
  x=             0.0F; if(             0.0F>x) printf("T"); else printf("-"); if(             0.0F>x) printf("T"); else printf("-");
  x=             0.0F; if( 1.175494351e-38F>x) printf("T"); else printf("-"); if( 1.175494351e-38F>x) printf("T"); else printf("-");
  x=             0.0F; if(             2.7F>x) printf("T"); else printf("-"); if(             2.7F>x) printf("T"); else printf("-");
  x=             0.0F; if( 3.402823466e+38F>x) printf("T"); else printf("-"); if( 3.402823466e+38F>x) printf("T"); else printf("-");
  printf("\n");
  x= 1.175494351e-38F; if(-3.402823466e+38F>x) printf("T"); else printf("-"); if(-3.402823466e+38F>x) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(            -2.7F>x) printf("T"); else printf("-"); if(            -2.7F>x) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(-1.175494351e-38F>x) printf("T"); else printf("-"); if(-1.175494351e-38F>x) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(            -0.0F>x) printf("T"); else printf("-"); if(            -0.0F>x) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(             0.0F>x) printf("T"); else printf("-"); if(             0.0F>x) printf("T"); else printf("-");
  x= 1.175494351e-38F; if( 1.175494351e-38F>x) printf("T"); else printf("-"); if( 1.175494351e-38F>x) printf("T"); else printf("-");
  x= 1.175494351e-38F; if(             2.7F>x) printf("T"); else printf("-"); if(             2.7F>x) printf("T"); else printf("-");
  x= 1.175494351e-38F; if( 3.402823466e+38F>x) printf("T"); else printf("-"); if( 3.402823466e+38F>x) printf("T"); else printf("-");
  printf("\n");
  x=             2.7F; if(-3.402823466e+38F>x) printf("T"); else printf("-"); if(-3.402823466e+38F>x) printf("T"); else printf("-");
  x=             2.7F; if(            -2.7F>x) printf("T"); else printf("-"); if(            -2.7F>x) printf("T"); else printf("-");
  x=             2.7F; if(-1.175494351e-38F>x) printf("T"); else printf("-"); if(-1.175494351e-38F>x) printf("T"); else printf("-");
  x=             2.7F; if(            -0.0F>x) printf("T"); else printf("-"); if(            -0.0F>x) printf("T"); else printf("-");
  x=             2.7F; if(             0.0F>x) printf("T"); else printf("-"); if(             0.0F>x) printf("T"); else printf("-");
  x=             2.7F; if( 1.175494351e-38F>x) printf("T"); else printf("-"); if( 1.175494351e-38F>x) printf("T"); else printf("-");
  x=             2.7F; if(             2.7F>x) printf("T"); else printf("-"); if(             2.7F>x) printf("T"); else printf("-");
  x=             2.7F; if( 3.402823466e+38F>x) printf("T"); else printf("-"); if( 3.402823466e+38F>x) printf("T"); else printf("-");
  printf("\n");
  x= 3.402823466e+38F; if(-3.402823466e+38F>x) printf("T"); else printf("-"); if(-3.402823466e+38F>x) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(            -2.7F>x) printf("T"); else printf("-"); if(            -2.7F>x) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(-1.175494351e-38F>x) printf("T"); else printf("-"); if(-1.175494351e-38F>x) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(            -0.0F>x) printf("T"); else printf("-"); if(            -0.0F>x) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(             0.0F>x) printf("T"); else printf("-"); if(             0.0F>x) printf("T"); else printf("-");
  x= 3.402823466e+38F; if( 1.175494351e-38F>x) printf("T"); else printf("-"); if( 1.175494351e-38F>x) printf("T"); else printf("-");
  x= 3.402823466e+38F; if(             2.7F>x) printf("T"); else printf("-"); if(             2.7F>x) printf("T"); else printf("-");
  x= 3.402823466e+38F; if( 3.402823466e+38F>x) printf("T"); else printf("-"); if( 3.402823466e+38F>x) printf("T"); else printf("-");
  printf("\n");
}

void main4() {
  float x;
  x=    2.71828F; if(    2.71828F>x) printf("T"); else printf("-"); if(    2.71828F>x) printf("T"); else printf("-");
  x=    2.71828F; if( 2.71828182F>x) printf("T"); else printf("-"); if( 2.71828182F>x) printf("T"); else printf("-");
  x=    2.71828F; if(2.718281828F>x) printf("T"); else printf("-"); if(2.718281828F>x) printf("T"); else printf("-");
  printf("\n");
  x= 2.71828182F; if(    2.71828F>x) printf("T"); else printf("-"); if(    2.71828F>x) printf("T"); else printf("-");
  x= 2.71828182F; if( 2.71828182F>x) printf("T"); else printf("-"); if( 2.71828182F>x) printf("T"); else printf("-");
  x= 2.71828182F; if(2.718281828F>x) printf("T"); else printf("-"); if(2.718281828F>x) printf("T"); else printf("-");
  printf("\n");
  x=2.718281828F; if(    2.71828F>x) printf("T"); else printf("-"); if(    2.71828F>x) printf("T"); else printf("-");
  x=2.718281828F; if( 2.71828182F>x) printf("T"); else printf("-"); if( 2.71828182F>x) printf("T"); else printf("-");
  x=2.718281828F; if(2.718281828F>x) printf("T"); else printf("-"); if(2.718281828F>x) printf("T"); else printf("-");
  printf("\n");
}

int main() {
  if(-3.402823466e+38F>-3.402823466e+38F) printf("T"); else printf("-");
  if(-3.402823466e+38F>            -2.7F) printf("T"); else printf("-");
  if(-3.402823466e+38F>-1.175494351e-38F) printf("T"); else printf("-");
  if(-3.402823466e+38F>            -0.0F) printf("T"); else printf("-");
  if(-3.402823466e+38F>             0.0F) printf("T"); else printf("-");
  if(-3.402823466e+38F> 1.175494351e-38F) printf("T"); else printf("-");
  if(-3.402823466e+38F>             2.7F) printf("T"); else printf("-");
  if(-3.402823466e+38F> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  if(            -2.7F>-3.402823466e+38F) printf("T"); else printf("-");
  if(            -2.7F>            -2.7F) printf("T"); else printf("-");
  if(            -2.7F>-1.175494351e-38F) printf("T"); else printf("-");
  if(            -2.7F>            -0.0F) printf("T"); else printf("-");
  if(            -2.7F>             0.0F) printf("T"); else printf("-");
  if(            -2.7F> 1.175494351e-38F) printf("T"); else printf("-");
  if(            -2.7F>             2.7F) printf("T"); else printf("-");
  if(            -2.7F> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  if(-1.175494351e-38F>-3.402823466e+38F) printf("T"); else printf("-");
  if(-1.175494351e-38F>            -2.7F) printf("T"); else printf("-");
  if(-1.175494351e-38F>-1.175494351e-38F) printf("T"); else printf("-");
  if(-1.175494351e-38F>            -0.0F) printf("T"); else printf("-");
  if(-1.175494351e-38F>             0.0F) printf("T"); else printf("-");
  if(-1.175494351e-38F> 1.175494351e-38F) printf("T"); else printf("-");
  if(-1.175494351e-38F>             2.7F) printf("T"); else printf("-");
  if(-1.175494351e-38F> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  if(            -0.0F>-3.402823466e+38F) printf("T"); else printf("-");
  if(            -0.0F>            -2.7F) printf("T"); else printf("-");
  if(            -0.0F>-1.175494351e-38F) printf("T"); else printf("-");
  if(            -0.0F>            -0.0F) printf("T"); else printf("-");
  if(            -0.0F>             0.0F) printf("T"); else printf("-");
  if(            -0.0F> 1.175494351e-38F) printf("T"); else printf("-");
  if(            -0.0F>             2.7F) printf("T"); else printf("-");
  if(            -0.0F> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  if(             0.0F>-3.402823466e+38F) printf("T"); else printf("-");
  if(             0.0F>            -2.7F) printf("T"); else printf("-");
  if(             0.0F>-1.175494351e-38F) printf("T"); else printf("-");
  if(             0.0F>            -0.0F) printf("T"); else printf("-");
  if(             0.0F>             0.0F) printf("T"); else printf("-");
  if(             0.0F> 1.175494351e-38F) printf("T"); else printf("-");
  if(             0.0F>             2.7F) printf("T"); else printf("-");
  if(             0.0F> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  if( 1.175494351e-38F>-3.402823466e+38F) printf("T"); else printf("-");
  if( 1.175494351e-38F>            -2.7F) printf("T"); else printf("-");
  if( 1.175494351e-38F>-1.175494351e-38F) printf("T"); else printf("-");
  if( 1.175494351e-38F>            -0.0F) printf("T"); else printf("-");
  if( 1.175494351e-38F>             0.0F) printf("T"); else printf("-");
  if( 1.175494351e-38F> 1.175494351e-38F) printf("T"); else printf("-");
  if( 1.175494351e-38F>             2.7F) printf("T"); else printf("-");
  if( 1.175494351e-38F> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  if(             2.7F>-3.402823466e+38F) printf("T"); else printf("-");
  if(             2.7F>            -2.7F) printf("T"); else printf("-");
  if(             2.7F>-1.175494351e-38F) printf("T"); else printf("-");
  if(             2.7F>            -0.0F) printf("T"); else printf("-");
  if(             2.7F>             0.0F) printf("T"); else printf("-");
  if(             2.7F> 1.175494351e-38F) printf("T"); else printf("-");
  if(             2.7F>             2.7F) printf("T"); else printf("-");
  if(             2.7F> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  if( 3.402823466e+38F>-3.402823466e+38F) printf("T"); else printf("-");
  if( 3.402823466e+38F>            -2.7F) printf("T"); else printf("-");
  if( 3.402823466e+38F>-1.175494351e-38F) printf("T"); else printf("-");
  if( 3.402823466e+38F>            -0.0F) printf("T"); else printf("-");
  if( 3.402823466e+38F>             0.0F) printf("T"); else printf("-");
  if( 3.402823466e+38F> 1.175494351e-38F) printf("T"); else printf("-");
  if( 3.402823466e+38F>             2.7F) printf("T"); else printf("-");
  if( 3.402823466e+38F> 3.402823466e+38F) printf("T"); else printf("-");
  printf("\n");
  if(    2.71828F>    2.71828F) printf("T"); else printf("-");
  if(    2.71828F> 2.71828182F) printf("T"); else printf("-");
  if(    2.71828F>2.718281828F) printf("T"); else printf("-");
  printf("\n");
  if( 2.71828182F>    2.71828F) printf("T"); else printf("-");
  if( 2.71828182F> 2.71828182F) printf("T"); else printf("-");
  if( 2.71828182F>2.718281828F) printf("T"); else printf("-");
  printf("\n");
  if(2.718281828F>    2.71828F) printf("T"); else printf("-");
  if(2.718281828F> 2.71828182F) printf("T"); else printf("-");
  if(2.718281828F>2.718281828F) printf("T"); else printf("-");
  printf("\n");

  main1();
  main2();
  main3();
  main4();

  f0(-3.402823466e+38F); f0(-2.7F); f0(-1.175494351e-38F); f0(-0.0F);
  f0(0.0F); f0(1.175494351e-38F); f0(2.7F); f0(3.402823466e+38F);
  g0(2.71828F); g0(2.71828182F); g0(2.718281828F);

  f1(-3.402823466e+38F); f1(-2.7F); f1(-1.175494351e-38F); f1(-0.0F);
  f1(0.0F); f1(1.175494351e-38F); f1(2.7F); f1(3.402823466e+38F);
  g1(2.71828F); g1(2.71828182F); g1(2.718281828F);

  f2(-3.402823466e+38F); f2(-2.7F); f2(-1.175494351e-38F); f2(-0.0F);
  f2(0.0F); f2(1.175494351e-38F); f2(2.7F); f2(3.402823466e+38F);
  g2(2.71828F); g2(2.71828182F); g2(2.718281828F);

  return 0;
}
