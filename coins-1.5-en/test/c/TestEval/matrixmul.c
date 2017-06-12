/* matrixmul.c                                                     */
/* originally tpmatrixmult.c -- Matrix multiply (Nakata test prog) */
/* modified by M. Sassa. July 13, 2002                             */

#include <stdio.h>
#define SIZE 100
#define N 3

/* multiply x and y and store to z */
void matmul(double z[][SIZE], double x[][SIZE], double y[][SIZE], int n) {
  double sum;
  int i, j, k;

  for (i = 0; i < n; i++) {
    for (j = 0; j < n; j++) {
      sum = 0.0;
      for (k = 0; k < n; k++) 
        sum = sum + x[i][k] * y[k][j];
      z[i][j] = sum;
    }
  }
}

int main() {
  double x[SIZE][SIZE], y[SIZE][SIZE], z[SIZE][SIZE];
  int i, j;
  int n = N;

  /* initialize array x and y */
  for (i = 0; i < n; i++) {
    for (j = 0; j < n; j++) {
       x[i][j] = i + j;
       y[i][j] = i - j;
    }
  }

  /* call matmul */
  matmul(z, x, y, n);

  /* print z */
  for (i = 0; i < n; i++) {
    for (j = 0; j < n; j++) {
      printf("%g ",z[i][j]);
    }
    printf("\n");
  }

  return 0;
}


