#include<stdio.h>
int main(void){
  int a;
  int b;
  int z;
  int y;
  int temp;

  a=1;
  b=2;
  z=1;

  do{
    temp=a;
    a=b;
    b=temp;
    y=z;
    z=z+1;
  }while(z<2);

  printf("%d %d %d\n",a,b,y);

  return(0);
}

