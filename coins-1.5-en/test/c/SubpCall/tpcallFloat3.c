/* tpcallFloat3.c:  Floating point arithmetic and calling sequence
                    with mixed type (int, float, doluble) parameters */

/* SF030609[ */
void printStr(char *s)
{
  printf("%s\n",s);
}
void printFloat2(float f1, float f2)
{
  printf("%f,%f\n",f1,f2);
}
void printDouble2(double d1, double d2)
{
  printf("%f,%f\n",d1,d2);
}
void printEol()
{
  printf("\n");
}
/* SF030609] */

float  a,   b,   c,   e,   f,   g;
float  f10, f11, f12, f13, f14, f15;
double d,   d1,  d2,  d3, d4;
double d10, d11, d12, d13;
int    i,   j,   k,   l,   m,   n;
int    i1,  i2,  i3,  i4,  i5;
int    i10, i11, i12, i13, i14, i15, i16, i17, i18, i19, i20;

/* int    printFloat2(float pf1, float pf2); SF030609 */
int    If    (float  pf) { return pf + 1; }
float  Ff    (float  pf) { return pf + 1; }
float  Fd    (double pd) { return pd + 1.0; }
double Df    (float  pf) { return pf + 1; }
double Dd    (double pd) { return pd + 1; }
int    Iffi  (float  pf1, float pf2, int p) { return pf1 + pf2 + p; }
int    Ififi (float  pf1, int p1, float pf2, int p2)
{
  return pf1 + pf2 + p1 + p2;
}
int    Ifiif (float  pf1, int p1, int p2, float pf2)
{
  return pf1 + p1 + pf2 + p2;
}
int    Iiiiif (int p1, int p2, int p3, int p4,
               float  pf1, int p5, float pf2, int p6)
{
  return p1 + p2 + p3 + p4 + pf1 + p5 + pf2 + p6;
}
int    Iiiif  (int p1, int p2, int p3,
               float  pf1, int p5, float pf2, int p6)
{
  return p1 + p2 + p3 + pf1 + p5 + pf2 + p6;
}
int    Iiif   (int p1, int p2,
               float  pf1, int p5, float pf2, int p6)
{
  return p1 + p2 + pf1 + p5 + pf2 + p6;
}
int    Iif    (int p1,
               float  pf1, int p5, float pf2, int p6)
{
  return p1 + pf1 + p5 + pf2 + p6;
}
int    Iddi  (double  pd1, double pd2, int p) { return pd1 + pd2 + p; }
int    Ididi (double  pd1, int p1, double pd2, int p2)
{
  return pd1 + pd2 + p1 + p2;
}
int    Idiid (double  pd1, int p1, int p2, double pd2)
{
  return pd1 + p1 + pd2 + p2;
}
int    Iiiiid (int p1, int p2, int p3, int p4,
               double  pd1, int p5, double pd2, int p6)
{
  return p1 + p2 + p3 + p4 + pd1 + p5 + pd2 + p6;
}
int    Iiiid  (int p1, int p2, int p3,
               double  pd1, int p5, double pd2, int p6)
{
  return p1 + p2 + p3 + pd1 + p5 + pd2 + p6;
}
int    Iiid   (int p1, int p2,
               double  pd1, int p5, double pd2, int p6)
{
  return p1 + p2 + pd1 + p5 + pd2 + p6;
}
int    Iid   (int p1,
               double  pd1, int p5, double pd2, int p6)
{
  return p1 + pd1 + p5 + pd2 + p6;
}
int    Iififii(int p1, float pf1, int p2, float pf2,
               int p3, int p4, int p5, int p6)
{
  printStr("fint p1, p2, p3, p4, p5, p6: ");
  printf("%d %d %d %d %d %d \n", p1, p2, p3, p4, p5, p6);
  printStr("fint pf1, pf2: ");
  printFloat2(pf1, pf2);
  return p1+p2+p3+p4+p5+p6 + pf1 + pf2;
}
float Fffffii(float p1, float p2, float p3, float p4,
             int q1, int q2, int q3, int q4, int q5)
{
  int t1, t2, t3, t4;
  int k1 = 5, k2, k3;
  float z;

  k2 = q1 + q2 + q3 + q4 + q5;
  t1 = p1;
  t2 = p2;
  t3 = p3;
  t4 = p4;
  printStr("ffloat t1,t2,t3,t4,k1,k2,/p1,p2,/p3,p4: ");
  printf("%d %d %d %d %d %d \n", t1, t2, t3, t4, k1, k2);
  printFloat2(p1, p2);
  printFloat2(p3, p4);
  k3 = Iififii(t1, 10.0, t2, 20.0, t3, t4, k1, k2);
  return p1 + p2 + t3 + t4 + k3;
}
double Dfiddd(float p1, int pi1, double p2, double p3, double p4)
{
  return p1 + p2 + p3 + p4 + pi1;
}

int main()
{
  a  = 1.0;
  b  = 3.25;
  c  = a + b * 4;
  d  = 1.0;
  d1 = d + 1.0;
  d2 = d1 + 1;
  d3 = d2 + 1.0;
  i  = c;
  j  = i + 2;
  k  = j + 2;
  l  = k + 3;
  m  = l + 1;
  n  = m + 4;
  printDouble2(1.0, 3.25);
  printStr("a,b,c,d: ");
  printFloat2(a, b);
  printFloat2(c, d);
  e = Ff(i);
  f = i + 2.5;
  printFloat2(e, f);
  j  = Iififii(i, 3.0, 1, 4.0, 2, a, b, 2.0);
  g  = Fffffii(a, b, c, d, 10, 11, 12, 13, 14);
  d1 = Dfiddd(a, 1, b, c, d);
  printf("i j 1 2 3 4 = %d %d %d %d %d %d \n", i, j, 1, 2, 3, 4);
  printf("a b c d = %e %e %e %e \n", a, b, c, d);
  printf("d d1 d a = %f %f %f %f \n", d, d1, d, a);
  printFloat2(a, b);
  printFloat2(c, d);
  printEol();

  i10 = If(a);
  i11 = Iffi(a, b, 1);
  i12 = Ififi(2.0, 3, 4.0, 5);
  printf("If Iffi Ififi = %d %d %d \n", i10, i11, i12);
  f10 = Ff(3.0);
  f11 = Fd(d);
  printf("Ff Fd = %f %f \n", f10, f11);
  d10 = Df(1.0);
  d11 = Dd(2.0);
  printf("Df Dd = %f %f \n", d10, d11);
  i14 = Iffi(a, b, i);
  i15 = Ififi(a, i, b, j);
  i16 = Ifiif(a, i, j, b);
  i17 = Iiiiif(i, j, k, l, a, m, b, n);
  i18 = Iiiif (i, j, k,    a, m, b, n);
  i19 = Iiif  (i, j,       a, m, b, n);
  i20 = Iif   (i,          a, m, b, n);
  printf("Iffi Ififi Ifiif Iiiiif Iiiif Iiif Iif = %d %d %d %d %d %d %d \n",
          i14, i15, i16, i17, i18, i19, i20);
  i14 = Iddi(d, d1, i);
  i15 = Ididi(d, i, d1, j);
  i16 = Idiid(d, i, j, d1);
  i17 = Iiiiid(i, j, k, l, d1, d2, d3, d4);
  i18 = Iiiid (i, j, k,    d1, d2, d3, d4);
  i19 = Iiid  (i, j,       d1, d2, d3, d4);
  i20 = Iid   (i,          d1, d2, d3, d4);
  printf("Iddi Ididi Idiid Iiiiid Iiiid Iiid Iid = %d %d %d %d %d %d %d \n",
          i14, i15, i16, i17, i18, i19, i20);
  return c;
}

