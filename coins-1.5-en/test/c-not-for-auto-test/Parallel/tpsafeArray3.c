/* tpsafeArray3.c Use #pragma optControl safeArray 
*/
#pragma doAll subpParallel summation

#ifndef MAIN
#define MAIN
#endif

#include "emcoins_thread_framework.h"

int printf(char*, ...);
int c[50], z[50][10];
int k;

int summation(int* pArray1, int pArray2[][10], int pSize)
{
#pragma optControl safeArray pArray1 pArray2
  int i, lResult = 0;
#pragma doAll parallel
  for (i = 0; i < pSize; i++) {
    lResult = lResult + pArray1[i];
  }
#pragma doAll parallel
  for (i = 0; i < pSize; i++) {
    lResult = lResult + pArray2[i][5];
  }
  return lResult;
}

int main()
{
  int x[50];
  int i,j,n;
  int sum;
  int max, min, maxInx, minInx;
#pragma emcoins_thread init
  n=50;
  sum = 0;
  for (i=0; i<n ; i++) {  
    x[i]= i;
    z[i][5] = i*i;
  }
  sum = summation(x, z, n);
  printf(" sum = %d \n", sum);  
#pragma emcoins_thread end
  return 0;
}
