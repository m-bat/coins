/* tpconstFold7.c  -- inline expression (zero divide)  */

#pragma optControl inline ff

int printf(char*, ...);

int ff( int pValue )
{
  return 100/pValue;
}

int main()
{
  int a, b, c, d, e, f, x, y, z;
  a = ff(1);
  b = ff(0);
  printf("%d %d \n", a, b);
  return 0;
}

