/* tpfloat1.c:  Floating point arithmetic */

float a, b, c, x, y;
double d;
int   i, j, k;
void  printFloat2(float p1, float p2)
{
  printf("(%f,%f)\n",p1,p2); /* SF030509 */
}
void printEol()
{
  printf("\n"); /* SF030509 */
}
float f(float p)
{
  printFloat2(p, 1);
  return p + 1;
}
int main()
{
  a = 1.0;
  b = 3.25;
  c = a + b * 4;
  i = c;
  x = f(i);
  y = i + 2.5;
  d = 1.0L + a;
  printFloat2(a, b);
  printFloat2(c, d);
  printFloat2(x, y);
  printEol();
  return c;
}

