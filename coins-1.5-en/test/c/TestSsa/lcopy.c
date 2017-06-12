#include<stdio.h>
main(){
  int z;
  int y;

  z=1;
  y=0;
  do{
    y=z;
    z=z+1;
  }while(z<2);

  printf("%d\n",y);
}
