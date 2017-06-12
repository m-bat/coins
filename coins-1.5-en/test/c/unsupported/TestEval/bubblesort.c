/** bubblesort.c
    Bubble sort random numbers (by Watanabe)
**/
#include <stdio.h> 
#include <stdlib.h>
/*
#include <sys/time.h>
#include <sys/types.h>
#include <sys/resource.h>
#include <unistd.h>
#include <assert.h>
*/

#define N 1000
#define MTIMES 10

int main(void)
{
  int A[N + 1];
  int B[N + 1];
  int i, j, k, lc, w;
/*
  struct rusage before, after;
*/
  fflush(stdout); 
  for (i = 0; i < N; i++) {
    /*
    printf("%d ? ", i);
    scanf("%d", &A[i]);

    B[i] = 1 + (rand() % 10);
    */
    B[i] = 1 + (rand() % N);      

  }

  for (lc = 0; lc < MTIMES; lc++){
    for (k = 0; k < N; k++)
      A[k] = B[k];
    for (i = 0; i < N; i++) {
      for (j = 0; j < N-1; j++) {
	if (A[j] > A[j + 1]) {
	  w = A[j];
	  A[j] = A[j + 1];
	  A[j + 1] = w;
	}
      }
    }

  }

  if (N <= 20) {
    for(k = 0; k < N; k++)
      printf("%d  ", A[k]);
  }else {
    for(k = 0; k < 10; k++)
      printf("%d  ", A[k]);
    printf("\n");
    for(k = N-10; k < N; k++)
      printf("%d  ", A[k]);
  }
  printf("\n");

  exit(0);
}

