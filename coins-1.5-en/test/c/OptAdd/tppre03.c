/*---- tppde03.c: PDE with multiple nested subexpressions */ 

/*
#include <stdio.h> 
*/

int data[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

int main()
{
  int a, b, c, d, e, f;
  int x, y, z, x1, y1, z1, z2;
  a = data[1]; 
  b = data[2];
  c = data[1];
  d = data[2];
  if (a != 0) {
    x1 = a + b;
    x = a + b + c + d;
  }else {
    x1 = a + c;
    x = a + c + d;
  } 
  y = a + b + c + d;
  z = a + c;
  z1 = a + b;
  z = z + z1;
  printf("%d %d %d \n", x, y, z); 
  return 0;
}
