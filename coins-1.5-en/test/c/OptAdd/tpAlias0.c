/* tpAlias0.c  Alias test */

int ga[10], gb[10];

int *func1( int pa[10], int pb[10], int *pi)
{
  int i;
  for (i = 1; i < 10; i++)
    pa[i] = pb[i] + *pi;
  return &pa[0];
}

int main()
{
  int x, y, xx, yy, zz;
  int *px, *py;
  xx = 3;
  zz = 5;
  px = &xx;
  *px = 1;
  px = &yy;
  *px = 2;
  x = *px;
  y = *px + *px;
  px = func1(ga, gb, px);
  printf("%d %d %d %d %d \n", x, y, xx, yy, *(px+1));
  return 0;
}
