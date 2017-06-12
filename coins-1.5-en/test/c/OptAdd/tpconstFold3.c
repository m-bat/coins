/* tpconstFold3.c  -- constant folding  */

int i, j, k, x, y;
int  a[10];
int f(int p);

main()
{
  x = f(1);
  i = 100 + x + 10;
  y = f(2);
  y = y + 50 / 5;
  k = x + j*2*3;
  a[0] = 3;
  a[1] = 4;
  i = -1;
  j = -i + a[-1+2];
  printf("y=%d k=%d j=%d \n",y, k,j);
  return 0;
}

int f( int p )
{
  return p;
}

