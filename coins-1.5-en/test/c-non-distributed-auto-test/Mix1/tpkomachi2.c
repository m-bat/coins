/* tpkomachi2.c:  Komachi calculation.
   Insert operators (+ and -) between numbers 1 2 3 4 5 6 7 8 9 
   so that the value of the resultant expression is 100.
   Haruhiko Okumura: Algorithm dictionary written in C, 
     Gijutsu hyoronsha, 1991. 
*/   
#include <stdio.h>

main() {
  int i,s,sign[10];
  long n,x;

  for(i=1;i<=9;i++)
    sign[i]=-1;
  do {
    x=n=0;
    s=1;
    for(i=1;i<=9;i++) {
      if(sign[i]==0)
	n=10*n+i;
      else {
	x+=s*n;
	s=sign[i];
	n=i;
      }
    }
    x+=s*n;
    /**/
    if(x==100)
      printexp(sign);
    /**/
    i=9;
    s=sign[i]+1;
    while(s>1) {
      sign[i]=-1;
      i--;
      s=sign[i]+1;
    }
    sign[i]=s;
  }while(sign[1]<1);
}

/**/
printexp(int sign[]) {
  int i;

  for(i=1;i<=9;i++) {
    if(sign[i]==1) printf("+");
    if(sign[i]==-1) printf("-");
    printf("%d",i);
  }
  printf("=100\n");
}
/**/
