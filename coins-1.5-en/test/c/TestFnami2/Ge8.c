int printf(char *s, ...);

void f0(double x) {
  if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  if(x>=                       -2.7) printf("T"); else printf("-");
  if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  if(x>=                       -0.0) printf("T"); else printf("-");
  if(x>=                        0.0) printf("T"); else printf("-");
  if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  if(x>=                        2.7) printf("T"); else printf("-");
  if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
}

void f1(double x) {
  if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  if(                       -2.7>=x) printf("T"); else printf("-");
  if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  if(                       -0.0>=x) printf("T"); else printf("-");
  if(                        0.0>=x) printf("T"); else printf("-");
  if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  if(                        2.7>=x) printf("T"); else printf("-");
  if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  printf("\n");
}

void g0(double x) {
  if(x>=     2.71828182845904) printf("T"); else printf("-");
  if(x>= 2.718281828459045235) printf("T"); else printf("-");
  if(x>=2.7182818284590452354) printf("T"); else printf("-");
  printf("\n");
}

void g1(double x) {
  if(     2.71828182845904>=x) printf("T"); else printf("-");
  if( 2.718281828459045235>=x) printf("T"); else printf("-");
  if(2.7182818284590452354>=x) printf("T"); else printf("-");
  printf("\n");
}

void op(double x,double y) {
  if(x>=y) printf("T"); else printf("-");
}

void f2(double x) {
  op(x,-1.7976931348623157081e+308); op(x,-2.7); op(x,-2.2250738585072013831e-308); op(x,-0.0);
  op(x,0.0); op(x,2.2250738585072013831e-308); op(x,2.7); op(x,1.7976931348623157081e+308);
  printf("\n");
}

void g2(double x) {
  op(x,2.71828182845904); op(x,2.718281828459045235); op(x,2.7182818284590452354);
  printf("\n");
}

void main1() {
  double x;
  x=-1.7976931348623157081e+308; if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(x>=                       -2.7) printf("T"); else printf("-"); if(x>=                       -2.7) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(x>=                       -0.0) printf("T"); else printf("-"); if(x>=                       -0.0) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(x>=                        0.0) printf("T"); else printf("-"); if(x>=                        0.0) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(x>=                        2.7) printf("T"); else printf("-"); if(x>=                        2.7) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  x=                       -2.7; if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  x=                       -2.7; if(x>=                       -2.7) printf("T"); else printf("-"); if(x>=                       -2.7) printf("T"); else printf("-");
  x=                       -2.7; if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  x=                       -2.7; if(x>=                       -0.0) printf("T"); else printf("-"); if(x>=                       -0.0) printf("T"); else printf("-");
  x=                       -2.7; if(x>=                        0.0) printf("T"); else printf("-"); if(x>=                        0.0) printf("T"); else printf("-");
  x=                       -2.7; if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  x=                       -2.7; if(x>=                        2.7) printf("T"); else printf("-"); if(x>=                        2.7) printf("T"); else printf("-");
  x=                       -2.7; if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  x=-2.2250738585072013831e-308; if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(x>=                       -2.7) printf("T"); else printf("-"); if(x>=                       -2.7) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(x>=                       -0.0) printf("T"); else printf("-"); if(x>=                       -0.0) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(x>=                        0.0) printf("T"); else printf("-"); if(x>=                        0.0) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(x>=                        2.7) printf("T"); else printf("-"); if(x>=                        2.7) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  x=                       -0.0; if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  x=                       -0.0; if(x>=                       -2.7) printf("T"); else printf("-"); if(x>=                       -2.7) printf("T"); else printf("-");
  x=                       -0.0; if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  x=                       -0.0; if(x>=                       -0.0) printf("T"); else printf("-"); if(x>=                       -0.0) printf("T"); else printf("-");
  x=                       -0.0; if(x>=                        0.0) printf("T"); else printf("-"); if(x>=                        0.0) printf("T"); else printf("-");
  x=                       -0.0; if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  x=                       -0.0; if(x>=                        2.7) printf("T"); else printf("-"); if(x>=                        2.7) printf("T"); else printf("-");
  x=                       -0.0; if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  x=                        0.0; if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  x=                        0.0; if(x>=                       -2.7) printf("T"); else printf("-"); if(x>=                       -2.7) printf("T"); else printf("-");
  x=                        0.0; if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  x=                        0.0; if(x>=                       -0.0) printf("T"); else printf("-"); if(x>=                       -0.0) printf("T"); else printf("-");
  x=                        0.0; if(x>=                        0.0) printf("T"); else printf("-"); if(x>=                        0.0) printf("T"); else printf("-");
  x=                        0.0; if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  x=                        0.0; if(x>=                        2.7) printf("T"); else printf("-"); if(x>=                        2.7) printf("T"); else printf("-");
  x=                        0.0; if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  x= 2.2250738585072013831e-308; if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(x>=                       -2.7) printf("T"); else printf("-"); if(x>=                       -2.7) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(x>=                       -0.0) printf("T"); else printf("-"); if(x>=                       -0.0) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(x>=                        0.0) printf("T"); else printf("-"); if(x>=                        0.0) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(x>=                        2.7) printf("T"); else printf("-"); if(x>=                        2.7) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  x=                        2.7; if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  x=                        2.7; if(x>=                       -2.7) printf("T"); else printf("-"); if(x>=                       -2.7) printf("T"); else printf("-");
  x=                        2.7; if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  x=                        2.7; if(x>=                       -0.0) printf("T"); else printf("-"); if(x>=                       -0.0) printf("T"); else printf("-");
  x=                        2.7; if(x>=                        0.0) printf("T"); else printf("-"); if(x>=                        0.0) printf("T"); else printf("-");
  x=                        2.7; if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  x=                        2.7; if(x>=                        2.7) printf("T"); else printf("-"); if(x>=                        2.7) printf("T"); else printf("-");
  x=                        2.7; if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  x= 1.7976931348623157081e+308; if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(x>=                       -2.7) printf("T"); else printf("-"); if(x>=                       -2.7) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(x>=                       -0.0) printf("T"); else printf("-"); if(x>=                       -0.0) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(x>=                        0.0) printf("T"); else printf("-"); if(x>=                        0.0) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-"); if(x>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(x>=                        2.7) printf("T"); else printf("-"); if(x>=                        2.7) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-"); if(x>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
}

void main2() {
  double x;
  x=     2.71828182845904; if(x>=     2.71828182845904) printf("T"); else printf("-"); if(x>=     2.71828182845904) printf("T"); else printf("-");
  x=     2.71828182845904; if(x>= 2.718281828459045235) printf("T"); else printf("-"); if(x>= 2.718281828459045235) printf("T"); else printf("-");
  x=     2.71828182845904; if(x>=2.7182818284590452354) printf("T"); else printf("-"); if(x>=2.7182818284590452354) printf("T"); else printf("-");
  printf("\n");
  x= 2.718281828459045235; if(x>=     2.71828182845904) printf("T"); else printf("-"); if(x>=     2.71828182845904) printf("T"); else printf("-");
  x= 2.718281828459045235; if(x>= 2.718281828459045235) printf("T"); else printf("-"); if(x>= 2.718281828459045235) printf("T"); else printf("-");
  x= 2.718281828459045235; if(x>=2.7182818284590452354) printf("T"); else printf("-"); if(x>=2.7182818284590452354) printf("T"); else printf("-");
  printf("\n");
  x=2.7182818284590452354; if(x>=     2.71828182845904) printf("T"); else printf("-"); if(x>=     2.71828182845904) printf("T"); else printf("-");
  x=2.7182818284590452354; if(x>= 2.718281828459045235) printf("T"); else printf("-"); if(x>= 2.718281828459045235) printf("T"); else printf("-");
  x=2.7182818284590452354; if(x>=2.7182818284590452354) printf("T"); else printf("-"); if(x>=2.7182818284590452354) printf("T"); else printf("-");
  printf("\n");
}

void main3() {
  double x;
  x=-1.7976931348623157081e+308; if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(                       -2.7>=x) printf("T"); else printf("-"); if(                       -2.7>=x) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(                       -0.0>=x) printf("T"); else printf("-"); if(                       -0.0>=x) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(                        0.0>=x) printf("T"); else printf("-"); if(                        0.0>=x) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if(                        2.7>=x) printf("T"); else printf("-"); if(                        2.7>=x) printf("T"); else printf("-");
  x=-1.7976931348623157081e+308; if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  printf("\n");
  x=                       -2.7; if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  x=                       -2.7; if(                       -2.7>=x) printf("T"); else printf("-"); if(                       -2.7>=x) printf("T"); else printf("-");
  x=                       -2.7; if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=                       -2.7; if(                       -0.0>=x) printf("T"); else printf("-"); if(                       -0.0>=x) printf("T"); else printf("-");
  x=                       -2.7; if(                        0.0>=x) printf("T"); else printf("-"); if(                        0.0>=x) printf("T"); else printf("-");
  x=                       -2.7; if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=                       -2.7; if(                        2.7>=x) printf("T"); else printf("-"); if(                        2.7>=x) printf("T"); else printf("-");
  x=                       -2.7; if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  printf("\n");
  x=-2.2250738585072013831e-308; if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(                       -2.7>=x) printf("T"); else printf("-"); if(                       -2.7>=x) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(                       -0.0>=x) printf("T"); else printf("-"); if(                       -0.0>=x) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(                        0.0>=x) printf("T"); else printf("-"); if(                        0.0>=x) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if(                        2.7>=x) printf("T"); else printf("-"); if(                        2.7>=x) printf("T"); else printf("-");
  x=-2.2250738585072013831e-308; if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  printf("\n");
  x=                       -0.0; if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  x=                       -0.0; if(                       -2.7>=x) printf("T"); else printf("-"); if(                       -2.7>=x) printf("T"); else printf("-");
  x=                       -0.0; if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=                       -0.0; if(                       -0.0>=x) printf("T"); else printf("-"); if(                       -0.0>=x) printf("T"); else printf("-");
  x=                       -0.0; if(                        0.0>=x) printf("T"); else printf("-"); if(                        0.0>=x) printf("T"); else printf("-");
  x=                       -0.0; if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=                       -0.0; if(                        2.7>=x) printf("T"); else printf("-"); if(                        2.7>=x) printf("T"); else printf("-");
  x=                       -0.0; if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  printf("\n");
  x=                        0.0; if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  x=                        0.0; if(                       -2.7>=x) printf("T"); else printf("-"); if(                       -2.7>=x) printf("T"); else printf("-");
  x=                        0.0; if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=                        0.0; if(                       -0.0>=x) printf("T"); else printf("-"); if(                       -0.0>=x) printf("T"); else printf("-");
  x=                        0.0; if(                        0.0>=x) printf("T"); else printf("-"); if(                        0.0>=x) printf("T"); else printf("-");
  x=                        0.0; if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=                        0.0; if(                        2.7>=x) printf("T"); else printf("-"); if(                        2.7>=x) printf("T"); else printf("-");
  x=                        0.0; if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  printf("\n");
  x= 2.2250738585072013831e-308; if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(                       -2.7>=x) printf("T"); else printf("-"); if(                       -2.7>=x) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(                       -0.0>=x) printf("T"); else printf("-"); if(                       -0.0>=x) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(                        0.0>=x) printf("T"); else printf("-"); if(                        0.0>=x) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if(                        2.7>=x) printf("T"); else printf("-"); if(                        2.7>=x) printf("T"); else printf("-");
  x= 2.2250738585072013831e-308; if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  printf("\n");
  x=                        2.7; if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  x=                        2.7; if(                       -2.7>=x) printf("T"); else printf("-"); if(                       -2.7>=x) printf("T"); else printf("-");
  x=                        2.7; if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=                        2.7; if(                       -0.0>=x) printf("T"); else printf("-"); if(                       -0.0>=x) printf("T"); else printf("-");
  x=                        2.7; if(                        0.0>=x) printf("T"); else printf("-"); if(                        0.0>=x) printf("T"); else printf("-");
  x=                        2.7; if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x=                        2.7; if(                        2.7>=x) printf("T"); else printf("-"); if(                        2.7>=x) printf("T"); else printf("-");
  x=                        2.7; if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  printf("\n");
  x= 1.7976931348623157081e+308; if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if(-1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(                       -2.7>=x) printf("T"); else printf("-"); if(                       -2.7>=x) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if(-2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(                       -0.0>=x) printf("T"); else printf("-"); if(                       -0.0>=x) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(                        0.0>=x) printf("T"); else printf("-"); if(                        0.0>=x) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-"); if( 2.2250738585072013831e-308>=x) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if(                        2.7>=x) printf("T"); else printf("-"); if(                        2.7>=x) printf("T"); else printf("-");
  x= 1.7976931348623157081e+308; if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-"); if( 1.7976931348623157081e+308>=x) printf("T"); else printf("-");
  printf("\n");
}

void main4() {
  double x;
  x=     2.71828182845904; if(     2.71828182845904>=x) printf("T"); else printf("-"); if(     2.71828182845904>=x) printf("T"); else printf("-");
  x=     2.71828182845904; if( 2.718281828459045235>=x) printf("T"); else printf("-"); if( 2.718281828459045235>=x) printf("T"); else printf("-");
  x=     2.71828182845904; if(2.7182818284590452354>=x) printf("T"); else printf("-"); if(2.7182818284590452354>=x) printf("T"); else printf("-");
  printf("\n");
  x= 2.718281828459045235; if(     2.71828182845904>=x) printf("T"); else printf("-"); if(     2.71828182845904>=x) printf("T"); else printf("-");
  x= 2.718281828459045235; if( 2.718281828459045235>=x) printf("T"); else printf("-"); if( 2.718281828459045235>=x) printf("T"); else printf("-");
  x= 2.718281828459045235; if(2.7182818284590452354>=x) printf("T"); else printf("-"); if(2.7182818284590452354>=x) printf("T"); else printf("-");
  printf("\n");
  x=2.7182818284590452354; if(     2.71828182845904>=x) printf("T"); else printf("-"); if(     2.71828182845904>=x) printf("T"); else printf("-");
  x=2.7182818284590452354; if( 2.718281828459045235>=x) printf("T"); else printf("-"); if( 2.718281828459045235>=x) printf("T"); else printf("-");
  x=2.7182818284590452354; if(2.7182818284590452354>=x) printf("T"); else printf("-"); if(2.7182818284590452354>=x) printf("T"); else printf("-");
  printf("\n");
}

int main() {
  if(-1.7976931348623157081e+308>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  if(-1.7976931348623157081e+308>=                       -2.7) printf("T"); else printf("-");
  if(-1.7976931348623157081e+308>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  if(-1.7976931348623157081e+308>=                       -0.0) printf("T"); else printf("-");
  if(-1.7976931348623157081e+308>=                        0.0) printf("T"); else printf("-");
  if(-1.7976931348623157081e+308>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  if(-1.7976931348623157081e+308>=                        2.7) printf("T"); else printf("-");
  if(-1.7976931348623157081e+308>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  if(                       -2.7>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  if(                       -2.7>=                       -2.7) printf("T"); else printf("-");
  if(                       -2.7>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  if(                       -2.7>=                       -0.0) printf("T"); else printf("-");
  if(                       -2.7>=                        0.0) printf("T"); else printf("-");
  if(                       -2.7>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  if(                       -2.7>=                        2.7) printf("T"); else printf("-");
  if(                       -2.7>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  if(-2.2250738585072013831e-308>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  if(-2.2250738585072013831e-308>=                       -2.7) printf("T"); else printf("-");
  if(-2.2250738585072013831e-308>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  if(-2.2250738585072013831e-308>=                       -0.0) printf("T"); else printf("-");
  if(-2.2250738585072013831e-308>=                        0.0) printf("T"); else printf("-");
  if(-2.2250738585072013831e-308>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  if(-2.2250738585072013831e-308>=                        2.7) printf("T"); else printf("-");
  if(-2.2250738585072013831e-308>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  if(                       -0.0>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  if(                       -0.0>=                       -2.7) printf("T"); else printf("-");
  if(                       -0.0>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  if(                       -0.0>=                       -0.0) printf("T"); else printf("-");
  if(                       -0.0>=                        0.0) printf("T"); else printf("-");
  if(                       -0.0>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  if(                       -0.0>=                        2.7) printf("T"); else printf("-");
  if(                       -0.0>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  if(                        0.0>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  if(                        0.0>=                       -2.7) printf("T"); else printf("-");
  if(                        0.0>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  if(                        0.0>=                       -0.0) printf("T"); else printf("-");
  if(                        0.0>=                        0.0) printf("T"); else printf("-");
  if(                        0.0>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  if(                        0.0>=                        2.7) printf("T"); else printf("-");
  if(                        0.0>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  if( 2.2250738585072013831e-308>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  if( 2.2250738585072013831e-308>=                       -2.7) printf("T"); else printf("-");
  if( 2.2250738585072013831e-308>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  if( 2.2250738585072013831e-308>=                       -0.0) printf("T"); else printf("-");
  if( 2.2250738585072013831e-308>=                        0.0) printf("T"); else printf("-");
  if( 2.2250738585072013831e-308>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  if( 2.2250738585072013831e-308>=                        2.7) printf("T"); else printf("-");
  if( 2.2250738585072013831e-308>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  if(                        2.7>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  if(                        2.7>=                       -2.7) printf("T"); else printf("-");
  if(                        2.7>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  if(                        2.7>=                       -0.0) printf("T"); else printf("-");
  if(                        2.7>=                        0.0) printf("T"); else printf("-");
  if(                        2.7>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  if(                        2.7>=                        2.7) printf("T"); else printf("-");
  if(                        2.7>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  if( 1.7976931348623157081e+308>=-1.7976931348623157081e+308) printf("T"); else printf("-");
  if( 1.7976931348623157081e+308>=                       -2.7) printf("T"); else printf("-");
  if( 1.7976931348623157081e+308>=-2.2250738585072013831e-308) printf("T"); else printf("-");
  if( 1.7976931348623157081e+308>=                       -0.0) printf("T"); else printf("-");
  if( 1.7976931348623157081e+308>=                        0.0) printf("T"); else printf("-");
  if( 1.7976931348623157081e+308>= 2.2250738585072013831e-308) printf("T"); else printf("-");
  if( 1.7976931348623157081e+308>=                        2.7) printf("T"); else printf("-");
  if( 1.7976931348623157081e+308>= 1.7976931348623157081e+308) printf("T"); else printf("-");
  printf("\n");
  if(     2.71828182845904>=     2.71828182845904) printf("T"); else printf("-");
  if(     2.71828182845904>= 2.718281828459045235) printf("T"); else printf("-");
  if(     2.71828182845904>=2.7182818284590452354) printf("T"); else printf("-");
  printf("\n");
  if( 2.718281828459045235>=     2.71828182845904) printf("T"); else printf("-");
  if( 2.718281828459045235>= 2.718281828459045235) printf("T"); else printf("-");
  if( 2.718281828459045235>=2.7182818284590452354) printf("T"); else printf("-");
  printf("\n");
  if(2.7182818284590452354>=     2.71828182845904) printf("T"); else printf("-");
  if(2.7182818284590452354>= 2.718281828459045235) printf("T"); else printf("-");
  if(2.7182818284590452354>=2.7182818284590452354) printf("T"); else printf("-");
  printf("\n");

  main1();
  main2();
  main3();
  main4();

  f0(-1.7976931348623157081e+308); f0(-2.7); f0(-2.2250738585072013831e-308); f0(-0.0);
  f0(0.0); f0(2.2250738585072013831e-308); f0(2.7); f0(1.7976931348623157081e+308);
  g0(2.71828182845904); g0(2.718281828459045235); g0(2.7182818284590452354);

  f1(-1.7976931348623157081e+308); f1(-2.7); f1(-2.2250738585072013831e-308); f1(-0.0);
  f1(0.0); f1(2.2250738585072013831e-308); f1(2.7); f1(1.7976931348623157081e+308);
  g1(2.71828182845904); g1(2.718281828459045235); g1(2.7182818284590452354);

  f2(-1.7976931348623157081e+308); f2(-2.7); f2(-2.2250738585072013831e-308); f2(-0.0);
  f2(0.0); f2(2.2250738585072013831e-308); f2(2.7); f2(1.7976931348623157081e+308);
  g2(2.71828182845904); g2(2.718281828459045235); g2(2.7182818284590452354);

  return 0;
}
