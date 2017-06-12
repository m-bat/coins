/*---- tppre09.c: PRE for various redundant expression in if-statemant */ 

/*
#include <stdio.h> 
*/

int g = 5;

int main()
{
  int a, b, c, d, e, f;
  int x, y, z;
  b = 2;
  a = b + g;
  if (a != 0) {
    x = a + b;
  }else {
    x = b + g;
  } 
  printf("%d \n", x); 
  y = (a + b) * (b + g);
  printf("%d \n", y); 
  return 0;
}


