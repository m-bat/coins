#include<stdio.h>
/* swap.c */
int main(){
  int x;
  int y;
  int z;
  /* by FUKUOKA Takeaki */
  int count;
  
  x = 1;
  y = 2;
  /*z = 3;*/
  z = 0;
  count=0;
  
  do{
    z = x;
    x = y;
    y = z;
    count++;
    /*
      by FUKUOKA Takeaki
      }while(count<10);
    */
  }while(count<10);
	
  printf("%d %d %d %d\n",x,y,z,count);
  return 0;
}
