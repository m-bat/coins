/* lparaInduction1.c test induction variable */ 
#pragma parallel doAllFunc main 

#ifndef MAIN
#define MAIN
#endif

#include "coinsParallelFramework.h"

int printf(char*, ...);
int k, c[50];
int main()
{
  int x[50];
  int i,j,n;
  int sum;
#pragma parallel init
  n=50;
  k=100;  
#pragma parallel doAll
  sum = 0;
  for (i=0; i<n ; i++) {  
    x[i]= k + i; 
    c[i]= i * i;
    k=k-2;
    sum = sum + x[i];
  }
  for (j = 0; j < n; j++)
    printf(" x[%d]=%d c[%d]=%d", j, x[j], j, c[j]);
  printf("\nk = %d sum = %d \n", k, sum);  
#pragma parallel end
  return 0;
}
