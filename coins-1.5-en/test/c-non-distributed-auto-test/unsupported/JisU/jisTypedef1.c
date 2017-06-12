/* jisTypedef1.c : JIS C 6.5.6 Type definition pp.72-73 */

#include <stdio.h>

  typedef int MILES, KLICKSP();
  typedef struct { double re, im; } complex;
  MILES distance;
  extern KLICKSP *metriccp;
  complex x;
  complex z, *zp;

  typedef struct s1 { int x; } t1, *tp1;
  typedef struct s2 { int x; } t2, *tp2;
  t1 t11, t12;
  t2 t21, t22;
  tp1 t1p;
  tp2 t2p;
  
  typedef signed int t;
  typedef int plain;
  struct tag {
    unsigned t:4;
    const    t:5;
    plain    r:5;
  };

  KLICKSP *metriccp;

  typedef void fv(int), (*pfv)(int);
  void (*signal(int, void (*)(int)))(int);
  fv *signal(int, fv *);
  pfv signal(int, pfv);
  fv fv1, fv2;

int 
sub()
{
  t f(t (t));
  t sif(t);
  t si1;
  long t = 3;
  printf("sub \n");
  si1 = f(sif);
  return si1 + t;
}

t sif(t psi)
{
  printf("sif %d \n", psi);
  return psi;
}

t f(t pg(t))
{
  t si = 10;
  printf("f %d \n", si);
  return pg(si);
}

int
clicksp()
{
  printf("clicksp \n");
  return 11;
}

int 
main()
{
  struct tag str1 = { 15, -15, 12 };  /* excess elements in struct initializer */
  int    v1;
  pfv    pfunc;
  distance = 100;
  x.re = 1.0;
  x.im = 2.0;
  z.re = 1.5;
  z.im = 2.5;
  zp = &z;
  t11.x = 3;
  t21.x = 5;
  t1p   = &t12;
  t2p   = &t22;
  t1p->x = 6;
  t2p->x = 7;
  printf("*zp %e %e \n", zp->re, zp->im);
  printf("*t1p %d *t2p %d \n", t1p->x, t2p->x);
  printf("str1 %d %d \n", str1.t, str1.r);
  metriccp = &clicksp;
  (*metriccp)();
  v1 = sub();
  printf("v1 %d \n", v1);
  pfunc = signal(11, fv1);
  pfunc(13);
  return 0;
}

void
fv1(int p1)
{
  printf("fv1 %d \n", p1);
}

void
fv2(int p1)
{
  printf("fv2 %d \n", p1);
}

pfv
signal(int p2, pfv pfv2)
{
  printf("signal %d \n", p2);
  pfv2(10);
  return &fv2;
}

