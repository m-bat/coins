/* tpinline1.c:  Inline expansion test 1 */

int a, b, c;
int fn1(int p)
{
  int d;
  d = 1;
  d = p + d;
  return d;
}

int fn2(int p, int q)
{
  int a;
  a = p + q;
  return a + b;
}
int main()
{
  int e, f, g;
  a = 0;
  b = 1;
  c = b;
  e = fn1(c);
  f = fn2(c, e) * 16;
  printf("a,b,c,e,f = %d,%d,%d,%d,%d\n",a,b,c,e,f);
  return 0;
}
