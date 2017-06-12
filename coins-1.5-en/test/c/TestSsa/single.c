#include<stdio.h>
/* single.c */
int main(){
  
  int x;
  int y;
  int z;
  /* by FUKUOKA Takeaki */
  int count;
  
  x = 0;
  y = 0;
  z = 0;
  count=0;
   
  do{
    z = y + 1;
    y = x;
    x = 5;
    count++;
    /*
      by FUKUOKA Takeaki
      }while(1);
    */
  }while(count<10);
   
  /*
    by FUKUOKA Takeaki
    return 0;
  */
  printf("%d\n",z); /* Added by M.Takahashi */
  return(z);
}
