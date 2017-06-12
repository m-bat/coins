#include<stdio.h>
int main(){
  int a;
  int b;
  int c;
  int d;
  int e;
  int f;
  int g;
  int h;
  int add;
  int zz;
  int count;

  a=1;
  d=2;
  b=3;
  g=4;
  e=5;
  c=6;
  f=7;
  h=8;
  count=0;

  do{
    if(count>3)break;
    count=count+1;
    zz=d;
    f=b;
    e=a;
    g=h;
    /*c=d; */
    d=a;
    a=b;
    b=c;
    c=zz;
    /*d=a; */
  }while(1);
  add=a+b+c+d+e+f+g;
  printf("%d %d %d %d %d %d %d\n",a,b,c,d,e,f,g);
  printf("%d\n",add);
  return(add);
}

 

