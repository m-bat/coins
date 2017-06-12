/* lparaRed1.c test loop parallel (reduction with minus operator) */
#pragma parallel doAllFuncAll
#ifndef MAIN
#define MAIN
#endif

#include "coinsParallelFramework.h"
int printf(char*, ...);
int a[50];

int main(){
  int i,j,n;
  int ii, jj;
  int remainder;
#pragma parallel init
  n=50;
  i = 0;
  for (ii=2; ii <= 2*n; ii = ii + 2) {  
    a[i] = ii;
    i = i + 1;
  }
#pragma parallel doAll
  remainder = 2000;
  for (j = 0; j < n; j++) {
    remainder = remainder - a[j]; 
  }
  for (i = 0; i < n; i++)
    printf(" %d", a[i]);
  printf("\nremainder %d\n", remainder);
#pragma parallel end
  return 0;
}
