/* tpmatvect1.c -- Matrix and vector multiplication with null statement*/

  float x[10][10], y[10],z[10];
  float sum;
  int   i, j, k;
  int printf(char*, ...);

int main()
{
  /* Initialize */  /* ##82 */
  for (i = 0; i < 10; i++) {
    for (j = 0; j < 10; j++) {
      x[i][j] = i+j; 
    }
    y[i] = 10 - i;
  }
  /* Maltiply matrix x and vector y */
  for (i = 0; i < 10; i++) {
    sum = 0.0;
    for (j = 0; j < 10; j ++) 
      sum = sum + x[i][j] * y[j]; ;
    /** sum = sum + x[i][j] * y[j]; */
    z[i] = sum;
  }

  /* SF030620[ */
  printf("z=[");
  for(i=0;i<10;i++)
    printf("%f ",z[i]);  /* ##82 */
  printf("]\n");
  /* SF030620] */

  return 0;
}

