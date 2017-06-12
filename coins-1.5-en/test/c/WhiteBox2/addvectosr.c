/* addvectosr.c -- add vector for osr example */
/* Sassa. Feb. 28, 2004 */
#include <stdio.h>
#define N 20

void addvect(int z[], int x[], int y[], int n) {
  int i;

  for (i = 0; i < n; i++) {
     z[i] = x[i] + y[i];
  }
}

int main()
{
  int x[N], y[N], z[N];
  int i;

  for (i = 0; i < N; i++) {
     x[i] = i;
     y[i] = i;
  }

  addvect(z,x,y,N);

  for (i = 0 ; i < N; i++) printf("%d ", z[i]);
  printf("\n");

  return 0;
}






