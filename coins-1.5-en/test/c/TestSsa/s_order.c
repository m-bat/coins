#include<stdio.h>
int main(void){
  int a;
  int b;
  int c;

  a=1;
  b=2;

  do{
    c=b;
    b=a;
    a=a-1;
  }while(a<0);
  
  printf("%d %d %d\n",a,b,c);
  
  return(0);
}
