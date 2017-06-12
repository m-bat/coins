/* tparray4.c:  3 and 4 dim array. */
int i, j, k, l, m, n,
      x[5][10][20];
int   y[10][15];
int   z[10][5][8][30];
extern void printFloat(float);
int printf(char *s, ...);

int main()
{
  int s[20];

  i = 1;
  j = 2;
  k = 3;
  l = 4;
  m = 5;
  x[i][j][k] = 3;
  y[2][3] = 4;
  z[i][1][j][k] = 5;
  z[i][j][k][l] = 10;
  printf("x[i,j,k] %d z[i,1,j,k] %d z[i,j,k,l] %d \n",
          x[i][j][k], z[i][1][j][k], z[i][j][k][l]);
 /* printFloat(y[k-1][j+1]); */
  return 0;
} 

