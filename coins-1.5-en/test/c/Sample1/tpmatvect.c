/* tpmatvect.c -- Matrix and vector multiplication */

  float x[10][10], y[10],z[10];
  float sum;
  int   i, j, k;

int main()
{
  /* SF030609[ */
  for (i = 0; i < 10; i++){
    for (j = 0; j < 10; j ++)
      x[i][j] = 10*i+j;
    y[i] = i%3;
  }
  /* SF030609] */
  for (i = 0; i < 10; i++) {
    sum = 0.0;
    for (j = 0; j < 10; j ++)
      sum = sum + x[i][j] * y[j];
    z[i] = sum;
  }
  /* SF030609[ */
  for (i = 0; i < 10; i++)
    printf("%9.4f ",z[i]);
  printf("\n");
  /* SF030609] */
  return 0;
}
