/* tpstructinit4.c : Structure initiation in declaration */ 

int printf(char*, ...);

  typedef struct { double re, im; } complex;
  complex x;
  complex z, *zp;

  struct tag {
    int    s;
    int    t;
    int    r;
  };

int 
main()
{
  struct tag str1 = { 15, -15, 12 };  /* excess elements in struct initializer */
  x.re = 1.0;
  x.im = 2.0;
  z.re = 1.5;
  z.im = 2.5;
  zp = &z;
  printf("x   %e %e \n", x.re, x.im);
  printf("*zp %e %e \n", zp->re, zp->im);
  printf("str1 %d %d %d \n", str1.s, str1.t, str1.r);
  return 0;
}

