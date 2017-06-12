/* lparaForce1.c test forceDoAll */ 
#pragma parallel doAllFunc main 

#ifndef MAIN
#define MAIN
#endif

#include "coinsParallelFramework.h"

int printf(char*, ...);
int c[50],z[50][100];
int k;

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
    x[i]= k + i; 
    c[i]= i * i;
    k=k-2;
    sum = sum + x[i];
  }
  printf("k = %d sum = %d \n", k, sum);  
#pragma parallel forceDoAll (private ) (lastPrivate ) (reduction ("max" max) ("min" min))
  max = x[0];
  min = x[0];
  for (i = 0; i < n; i++) { 
   if (x[i] > max)
     max = x[i];
   if (x[i] < min)
     min = x[i];
  }
  printf("max = %d min = %d \n", max, min);
  maxInx = 2;
  minInx = 2;
  max = x[2];
  min = x[2];
#pragma parallel forceDoAll (private ) (lastPrivate ) (reduction ("maxIndex" maxInx x) ("minIndex" minInx x))
  for (i = 0; i < n; i++) { 
   if (x[i] > max) {
     max = x[i];
     maxInx = i;
   }
   if (x[i] < min) {
     min = x[i];
     minInx = i;
   }
  }
  sum = c[0];
  for (i = 1; i < n; i++) {   /* Loop with no pragma */
    sum = sum + c[i];
  }
  printf("maxInx = %d minInx = %d sum = %d\n", maxInx, minInx, sum);
#pragma parallel end
  return 0;
}
