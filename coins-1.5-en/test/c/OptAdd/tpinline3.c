/* tpinline3.c:  Inline expansion test 3 */

int printf(char*, ...);
int fact2(int);

int a, b, c;
int fact1(int p)
{
  if (p > 0)
    return p * fact1(p - 1);
  else
    return 1;
}

int main()
{
  a = fact1(2);
  b = fact1(3);
  c = fact2(4);
  printf("a,b,c,fact1(5),fact2(6) = %d,%d,%d,%d,%d\n",a,b,c,fact1(5),fact2(6));
  return 0;
}

int fact2(int p)
{
  if (p > 0)
    return p * fact2(p - 1);
  else
    return 1;
}

