/* tpcallFloat2.c:  Floating point arithmetic and calling sequence
                    with double parameters */

/* SF030609[ */
void printStr(char *s)
{
  printf("%s\n",s);
}
void printFloat2(float f1, float f2)
{
  printf("%f,%f\n",f1,f2);
}
void printEol()
{
  printf("\n");
}
/* SF030609] */

float a, b, c, e, f, g;
double d, d1, d2, d3;
int   i, j, k, l, m, n;
float ff(float p) { return p + 1; }
void  printFloat2(float p1, float p2);
int   fint(int p1, float pf1, int p2, float pf2, int p3, int p4, int p5, int p6)
{
  printStr("fint p1, p2, p3, p4, p5, p6: ");
  printf("%d %d %d %d %d %d \n", p1, p2, p3, p4, p5, p6);
  printStr("fint pf1, pf2: ");
  printFloat2(pf1, pf2);
  return p1+p2+p3+p4+p5+p6 + pf1 + pf2;
}
float ffloat(float p1, float p2, float p3, float p4,
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
  k3 = fint(t1, 10.0, t2, 20.0, t3, t4, k1, k2);
  return p1 + p2 + t3 + t4 + k3;
}
double fdouble(float p1, int pi1, double p2, double p3, double p4)
{
  return p1 + p2 + p3 + p4 + pi1;
}

int main()
{
  a = 1.0;
  b = 3.25;
  c = a + b * 4;
  d = 1.0;
  i = c;
  printFloat2(1.0, 3.25);
  printStr("a,b,c,d: ");
  printFloat2(a, b);
  printFloat2(c, d);
  e = ff(i);
  f = i + 2.5;
  printFloat2(e, f);
  j = fint(i, 3.0, 1, 4.0, 2, a, b, 2.0);
  g = ffloat(a, b, c, d, 10, 11, 12, 13, 14);
  d1 = fdouble(a, 1, b, c, d);
  printf("%d %d %d %d %d %d \n", i, j, 1, 2, 3, 4);
  printf("%e %e %e %e \n", a, b, c, d);
  printf("%f %f %f %f \n", d, d1, d, a);
  printFloat2(a, b);
  printFloat2(c, d);
  printEol();
  return c;
}

