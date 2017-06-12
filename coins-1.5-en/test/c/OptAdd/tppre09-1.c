/*---- tppre09-1.c: PRE for various redundant expression in if-statemant */ 
/* With #pragma and with global variables */

#pragma functionsWithoutSideEffect printf
/*
#include <stdio.h> 
*/
int printf(char*, ...);

int g = 5;
int a, b, c, d, e, f;

int main()
{
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


