/* lparallel6.c test loop parallel for CoinsEmb with last private and reduction */
/*   Arrays are declared as global */
#pragma parallel doAllFunc main 

#ifndef MAIN
#define MAIN
#endif

#include "coinsParallelFramework.h"

int printf(char*, ...);
float x[50],c[50],z[50][100];
int k;

int main(){
  int i,j,n;
  float sum;
#pragma parallel init
  n=50;k=0;               /* B1 */
#pragma parallel doAll
  for (i=0; i<n ; i++) {  /* B2 */ /* B8 */
    x[0]=0 ;              /* B3 */
    x[1]=10 ;
    for (j=2; j<50 ; j++) {        /* B4 */ /* B6 */
      x[j] = (x[j-1] + x[j-2])/2 ; /* B5 */
      z[i][k] = x[j] ;
    }
    k=k+2;                         /* B7 */
  }
  printf("k = %d \n", k);          /* B9 */
#pragma parallel doAll
  sum = 0.0;
  for (i = 0; i < n; i++) {        /* B10 */ /* B12 */
    sum = sum + x[i];              /* B11 */
  }
#pragma parallel end
  printf("sum = %f \n", sum);      /* B13 */
}
