/* loopSum1.c: Parallel (summation with "if", fixed range. by Iwasawa */

/****/
#include <stdio.h>

main( ) {
  int i;
  /** float s, q, a[100];
  **/
  int s, q, a[100];

  q=0;
  for (i=0 ; i<100 ; i++) a[i]=1;

  for (i=0 ; i<100 ; i++) {
     s=a[i]+1;
     if (s>0) a[i]=a[i]*s;
     q=q+a[i];
  }
  printf("q=%d\n",q);  /* SF030620 */   

}

