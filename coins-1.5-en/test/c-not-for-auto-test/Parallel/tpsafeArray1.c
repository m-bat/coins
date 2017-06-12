/* tpsafeArray1.c 
*/
#pragma parallel doAllFuncAll

#ifndef MAIN
#define MAIN
#endif

#include "coinsParallelFramework.h"

int printf(char*, ...);
int c[50],z[50][100];
int k;

int summation(int pArray[50], int pSize)
{
  int i, lResult = 0;
#pragma parallel doAll
  for (i = 0; i < pSize; i++) {
    lResult = lResult + pArray[i];
  }
  return lResult;
}

int main()
{
  int x[50];
  int i,j,n;
  int sum;
  int max, min, maxInx, minInx;
#pragma parallel init
  n=50;
  k=100;  
#pragma parallel doAll
  sum = 0;
  for (i=0; i<n ; i++) {  
    x[i]= k + i;   /* Initial value of k is not set in main_loop_0 ! */
    c[i]= i * i;
    k=k-2;
  }
  sum = summation(x, 50);
  printf("k = %d sum = %d \n", k, sum);  
#pragma parallel end
  return 0;
}
