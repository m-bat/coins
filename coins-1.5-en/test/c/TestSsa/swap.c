#include<stdio.h>
int main(void){
  int a;
  int b;
  int z;
  int temp;

  temp=0;
  a=1;
  b=2;
  z=0;

  do{
    temp=a;
    a=b;
    b=temp;
    z=z+1;
  }while(z<10);

  printf("%d %d %d %d\n",a,b,temp,z);

  return(0);
}

