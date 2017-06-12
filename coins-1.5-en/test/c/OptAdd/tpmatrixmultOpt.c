/* tpmatrixmultOpt.c -- Matrix multiply (Nakata test prog) */
int main()
{
  float x[10][10], y[10][10],z[10][10];
  float sum;
  int   i, j, k;

  /* SF030609[ */
  for (i = 0; i < 10; i++)
    for (j = 0; j < 10; j ++){
      x[i][j] = 10*i+j;
      y[i][j] = (10*i+j)%3;
    }
  /* SF030609] */
  for (i = 0; i < 10; i++) {
    for (j = 0; j < 10; j ++) {
      sum = 0.0;
      for (k = 0; k < 10; k++)
        sum = sum + x[i][k] * y[k][j];
      z[i][j] = sum;
    }
  }
  /* SF030609[ */
  for (i = 0; i < 10; i++){
    for (j = 0; j < 10; j ++)
      printf("%9.4f ",z[i][j]);
    printf("\n");
  }
  /* SF030609] */
  return 0;
}
