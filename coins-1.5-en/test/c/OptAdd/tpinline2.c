/* tpinline2.c:  Inline expansion test 2 */

int a, b, c;
int fn1(int p)
{
  return p+2;
}

int fn2(int p, int q)
{
  int a;
  a = fn1(p+q);
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
