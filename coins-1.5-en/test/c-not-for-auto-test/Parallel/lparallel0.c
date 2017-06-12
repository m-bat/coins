/* lparallel0.c test loop parallel (local variables) */
#pragma parallel doAllFunc main 

#ifndef MAIN
#define MAIN
#endif

#include "coinsParallelFramework.h"

int main(){
  float x[50],c[50],z[50][100];
  int i,j,n,k;
#pragma parallel init
  n=50;k=0;               /* B1 */
#pragma parallel doAll
  for (i=0; i<n ; i++) {  /* B2 */ /* B8 */
    x[0]=0 ;              /* B3 */
    x[1]=10 ;
    for (j=2; j<50 ; j++) {        /* B4 */ /* B6 */
      x[j] = (x[j-1] + x[j-2])/2 ; /* B5 */
      z[j][k] = x[j] ;
    }
    k=k+2;                         /* B7 */
  }
#pragma parallel end
}                                  /* B8 */
