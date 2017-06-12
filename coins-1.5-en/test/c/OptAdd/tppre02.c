/*---- tppre02.c: PRE with nested subexpression */ 

/*
#include <stdio.h> 
*/

int data[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

int main()
{
  int a, b, c, d, e, f;
  int x, y, z;
  a = data[1]; 
  b = data[2];
  c = data[1];
  d = data[2];
  if (a != 0) {
    x = a + b + c + d;
  }else {
    x = a + c;
  } 
  y = a + b + c + d;
  z = a + c;
  printf("%d %d %d \n", x, y, z); 
  return 0;
}
