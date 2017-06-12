/* tpcfg1.c: Control flow analysis (critical edge) */

int j, k, m, n, a[10], t;
void f()
{ 
  int i;
  for (i = 0; i < 10; i++)
    a[i] = i + i;
}

int  g(int p)
{
  return p;
}

int main()
{
  int i, s;
  f();
  if (m > 0) {
    s = 0;
    i = 0;
    while(i <= m) {
      s = s + a[i];
      i++;
    }  /* Critical edge */
  }else {
    s = m + n;
  }
  k = m + n + s;
  t = g(k);
  return t;
}
