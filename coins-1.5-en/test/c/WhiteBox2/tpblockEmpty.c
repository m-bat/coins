/* tpblockEmpty.c: Test HirIterator for empty block. Hasegawa mail 030724 */

int printf(char*, ...);
int x, y, z;

int func1(int p, int q)
{
  int i, j, a, b;

  a = p + q;
  {  }       /* null block */
  i = a;
  {
    {
      j = a * 3;
    }
  }
  b = a + 1;
  return a;
}
 
int main()
{
  int d, e, f;

  {
    d = 1;
    {
      e = d + 1;
    }
    f = func1(d, e);
  }
  if (d) { 
    x = 1;
  }else { 
    x = 2;
  }
  if (d) { 
  }else { 
  }
  x = f;
  while(0) { 
  }
  printf("%d %d \n", f, x);
  return 0;
}
  
