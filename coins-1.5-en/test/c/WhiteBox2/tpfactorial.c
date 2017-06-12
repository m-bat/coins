/* tpfactorial.c:  Inline expansion test 3 */

int printf(char*, ...);

int a, b, c;
int fact(int p)
{
  if (p > 0)
    return p * fact(p - 1);
  else
    return 1;
}

int main()
{
  a = fact(3);
  b = fact(4);
  printf("a,b,fact(5),fact(6) = %d,%d,%d,%d\n",a,b,fact(5),fact(6));
  return 0;
}

