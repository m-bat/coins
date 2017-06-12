/* addvect.c -- add two vectors */
/* M. Sassa.  July 13, 2002. */

#include <stdio.h>
#define SIZE 1000
#define N      10

double a[SIZE], b[SIZE], c[SIZE];

void addvector(double a[], double b[], double c[], int n) {
  /* add b and c and store to a */
  int i;
  for (i = 0; i < n; i++) {
    a[i] = b[i] + c[i];
  }
}

int main() {
  int i;
  int n = N;
  
  /* initialize b and c */
  for (i = 0; i < n; i++) {
    b[i] = i;
    c[i] = i;
  }

  /* call addvector */
  addvector(a, b, c, n);

  /* write a */
  for (i = 0; i < n; i++) 
    printf("%g ",a[i]);
  printf("\n");

  return 0;
}

