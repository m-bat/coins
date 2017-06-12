#include<stdio.h>
main(){
  int a,b,c,d,e,f,g,t;
	
  d=1;
  e=2;
  f=3;
  a=d;
  b=e;
  c=f;
	
  do{
    g=a+b+c;
    if(g>2) goto aaaa;
    a=b;
    t=b;
    b=c;
    c=t;
  }while(1);
	
 aaaa:
  printf("%d %d %d\n",a,b,c);
}
