/* tparrayParam1.c */

int printf(char*, ...);

int func1(int pa[], int pb[10], int pc[][5], int pd[][8][5], int k) 
{
  pa[k+1] = k;
  pb[k+1] = k;
  pc[k][2] = k;
  pd[k][2][1] = k;
  return pa[k] + pb[k] + pc[k][2] + pd[k][2][1];
}

int main()
{
  int x, y, z;
  int a[10], b[10], c[10][5], d[10][8][5];
  a[1] = 0;
  b[1] = 1;
  c[2][3] = 3;
  d[1][2][3] = 30;
  x = func1(a, b, c, d, 1);
  y = func1(b, a, c, d, 2);
  z = func1(b, a, c, d, 3);
  printf("%d %d %d\n", x, y, z);
  return 0;
}
