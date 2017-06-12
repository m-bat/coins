/* lparaInd1.c test loop parallel (require induction variable normalization) */
#pragma parallel doAllFuncAll

#ifndef MAIN
#define MAIN
#endif

#include "coinsParallelFramework.h"

int printf(char*, ...);
int a[50];
int k;

int main(){
  int i,j,n;
  int ii, jj;
#pragma parallel init
  n=40;
  i = 0;
#pragma parallel doAll
  for (ii=2; ii <= 2*n; ii = ii + 2) {  
    a[i] = ii;
    i = i + 1;
  }
#pragma parallel doAll
  for (j = 1; j <= n; j++) {
    a[j-1] = a[j-1] * 2;
  }
#pragma parallel doAll
  for (k = 0; k < n; k++) {
    a[k] = a[k] + 1;
  }
  for (i = 0; i <= n; i++)
    printf(" %d", a[i]);
#pragma parallel end
}
