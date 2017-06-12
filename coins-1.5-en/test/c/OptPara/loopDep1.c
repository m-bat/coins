/* loopDep1.c: Parallel (Variable range, multiple subscript expression,
      loop-carried dependency. by Iwasawa */

#include <stdio.h> 

main( ) {
  int i, j, n;
  /** float s, a[100], b[100]; 
  **/
  int s, a[100], b[100];

  scanf("%d",&n); 
  
  /* SF030620[ */   
  s=1;
  for(i=0 ; i<100 ; i++)
  { a[i]=i; b[i]=i+1; }
  /* SF030620] */

  for (i=1 ; i<n ; i++) {
     a[i] = b[i-1]*s;
     s    = a[i+1]+1;
     b[i] = s/i;
  }
  printf("s=%d\n",s); 
}
