/* lparaInduction1-lastprivate.c test induction variable */ 
#pragma doAll subpParallel main 

#ifndef MAIN
#define MAIN
#endif

#include "emcoins_thread_framework.h"

int printf(char*, ...);
int k, c[50];
int main()
{
  int x[50];
  int i,j,n;
  int sum;
#pragma emcoins_thread init
  n=50;
  k=100;  
#pragma doAll parallel 
  sum = 0;
  for (i=0; i<n ; i++) {  
    x[i]= k + i; 
    c[i]= i * i;
    k=k-2;
    sum = sum + x[i];
  }
  for (j = 0; j < n; j++)
    printf(" x[%d]=%d c[%d]=%d", j, x[j], j, c[j]);
  printf("\nk = %d sum = %d i=%d\n", k, sum, i);  
#pragma emcoins_thread end
  return 0;
}
