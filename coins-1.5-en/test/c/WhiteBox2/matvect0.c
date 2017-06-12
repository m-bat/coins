/* matvect0.c  -- Matrix and vector multiplication (float global) */

#include <stdio.h>
#define SIZE 100
#define N 3

float z[100], x[100][100], y[100];
float sum;
int   n = 100;
int   i, j;

/* multiply x and y and store to z */
void matvect() {

  for (i = 0; i < n; i++) {
    sum = 0.0;
    for (j = 0; j < n; j++)
      sum = sum + x[i][j] * y[j];
    z[i] = sum;
  }
}

int main() {

  /* initialize array x and vector y */
  for (i = 0; i < n; i++) {
    for (j = 0; j < n; j++) {
       x[i][j] = i + j;
    }
    y[i] = i;
  }

  /* call matmul */
  matvect();

  /* print z */
  for (i = 0; i < n; i++)
    printf("%g ",z[i]);
  printf("\n");

  return 0;
}


