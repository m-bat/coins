/* tpnonproto1.c:  function with out prototype declaration */
/*   Mori, Kitamura mail 041007 */

int printf(char*, ...);

int f();
int h(int p);
int main()
{
  double d;
  int    a, b, c;
  a = g(1);
  b = g(2, 3);
  c = h(4);
  d = f(12.0);
  printf("%d %d %d %f \n", a, b, c, d);
}

int f(x)
  double x;
{
  return x*x;
}

int g(p1, p2)
  int p1, p2;
{
  return p1;
}

int h(int p)
{
  return p+1;
}
