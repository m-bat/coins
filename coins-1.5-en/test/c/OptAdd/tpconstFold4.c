/* tpconstFold4.c  -- constant expression with interleaved variables.  */

int ff( int pValue )
{
  if (pValue > 0)
    return pValue;
  else
    return -pValue;
}

int main()
{
  int a, b, c, d, e, f, x, y, z;
  int i, j, aa[10];
  i = 1;
  a = 1 + 2 + i;
  b = a + 2 * 3;
  c = 3 + 4 + 5 + a;
  aa[i] = 1;
  j = 1 + 2 + aa[i]; 
  d = ff(1);
  d = d + 2;
  e = d + 2 * 3;
  f = 3 + 4 + 5 + e;
  x = ff(1) + 1 + 2;
  y = x * 2 * 3;
  z = 3 + y + 4 + 5;
  printf("%d %d %d %d %d %d %d %d %d %d \n", 
         a, b, c, d, e, f, j, x, y, z);
  return 0;
}

