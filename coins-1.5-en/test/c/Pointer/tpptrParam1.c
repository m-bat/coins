/* tpptrParam1.c -- difference between pointer parameter */

  int a[10], b, c;

int f(int *p, int *q) 
{
  return (p - q)/2;
}

int printf(char *, ...);

int main()
{
  /* b = f(&c, a); */ /* Implementation dependent. */
  b = f(&a[5], &a[1]);
  printf("%d \n", b);
}

