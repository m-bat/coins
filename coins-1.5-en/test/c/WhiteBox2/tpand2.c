/* tpand2.c  Logical-and test */

int printf( char *, ... );
int g1 = 0;

int f1( int pi )
{
  return pi;
}

int f2( int pi)
{
  g1 = g1 + pi;
  return g1; 
}

int main()
{
  int i, j, n;
  i = f1(1);
  j = f1(5);
  n = 1;
  if ((i++ > 5)&(j++ < f2(6)))
    n = 0;
  printf("%d %d \n", n, g1);
  return 0;
}
