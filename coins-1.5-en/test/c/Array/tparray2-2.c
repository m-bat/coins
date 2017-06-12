/* tparray2-2.c:  2 dim array. */
int i, j, x[10];
int z[30][5];
int main()
{
  int y[20];

  i = 1;
  j = 2;
  x[i] = 3;
  y[2] = 4;
  z[i][1] = 5;
  z[i][j] = x[1];

  printf("x[i] %d y[2] %d z[i][1] %d ", x[i], y[2], z[i][1]);
  printf("z[i][j] %d \n", z[i][j]);

  return z[2][1];
} 

