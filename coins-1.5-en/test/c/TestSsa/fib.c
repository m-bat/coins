#include<stdio.h>
/* fib.c */
int main(){
  int n;
  int fib;
  int i;
   
  n = 7;
  fib = 1;
  i = 1;
   
  do{
    if(i < n){
      i = i + 1;
      fib = fib * i;
    }else{
      break;
    }
  }while(1);
	 
  /* uncomment by FUKUOKA Takeaki */
  printf("fib[%d] = %d\n", n, fib);
  
  return 0;
}
