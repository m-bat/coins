/* tpprototype1.c  -- Prototype declaration */

int   n = 100;
int printf(char*, ...);
void matvect(float pxx[100][100], float pyy[100], float pzz[100]);

int 
main() 
{
  float z[100], x[100][100], y[100];
  int   i, j;

  /* initialize array x and vector y */
  for (i = 0; i < n; i++) {
    for (j = 0; j < n; j++) {
       x[i][j] = i + j;
    }
    y[i] = i;
  }

  /* call matmul */
  matvect(x, y, z);

  /* print z */
  for (i = 0; i < n; i++)
    printf("%g ",z[i]);
  printf("\n");

  return 0;
}

/* multiply x and y and store to z */
void 
matvect(float px[100][100], float py[100], float pz[100]) 
{
  int   i, j;
  float sum;
  for (i = 0; i < n; i++) {
    sum = 0.0;
    for (j = 0; j < n; j++)
      sum = sum + px[i][j] * py[j];
    pz[i] = sum;
  }
}


