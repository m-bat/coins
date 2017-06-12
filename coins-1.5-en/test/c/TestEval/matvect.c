/* matvect.c                                                  */
/* originally tpmatvect.c -- Matrix and vector multiplication */
/* modified by M. Sassa. July 13, 2002                        */

#include <stdio.h>
#define SIZE 100
#define N 3

/* multiply x and y and store to z */
void matvect(double z[], double x[][SIZE], double y[], int n) {
  double sum;
  int i, j;

  for (i = 0; i < n; i++) {
    sum = 0.0;
    for (j = 0; j < n; j++)
      sum = sum + x[i][j] * y[j];
    z[i] = sum;
  }
}

int main() {
  double x[SIZE][SIZE], y[SIZE], z[SIZE];
  int i, j;
  int n = N;

  /* initialize array x and vector y */
  for (i = 0; i < n; i++) {
    for (j = 0; j < n; j++) {
       x[i][j] = i + j;
    }
    y[i] = i;
  }

  /* call matmul */
  matvect(z, x, y, n);

  /* print z */
  for (i = 0; i < n; i++)
    printf("%g ",z[i]);
  printf("\n");

  return 0;
}


