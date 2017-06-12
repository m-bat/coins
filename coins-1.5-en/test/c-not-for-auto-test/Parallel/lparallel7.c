/* lparallel7.c test loop parallel with last private and reduction */
/*   Variable x used in a loop is declared as local and other arrays are declared as global */
#pragma parallel doAllFunc main 

#ifndef MAIN
#define MAIN
#endif

#include "coinsParallelFramework.h"

int printf(char*, ...);
float c[50],z[50][100];
int k;

int main(){
  float x[50];
  int i,j,n;
  float sum;
#pragma parallel init
  n=50;k=0; 
#pragma parallel doAll
  for (i=0; i<n ; i++) { 
    x[0]=0 ;   
    x[1]=10 ;
    for (j=2; j<50 ; j++) {  
      x[j] = (x[j-1] + x[j-2])/2 ; 
      z[i][k] = x[j] ;
    }
    k=k+2;                        
  }
  printf("k = %d \n", k);        
#pragma parallel doAll
  sum = 0.0;
  for (i = 0; i < n; i++) {     
    sum = sum + x[i];          
  }
#pragma parallel end
  printf("sum = %f \n", sum); 
}
