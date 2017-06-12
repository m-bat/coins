/*---- tppre07.c: PRE for nested if-statements */ 

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
    x = a + b;
    if (b != 0) {
      y = a + c;
    }else {
      y = a + d;
    }
    z = z + (a + c);
    z = z + (a + d);
  }else {
    x = b + c;
    if (b != 0) {
      y = c + b;
    }else {
      y = d + b;
    }
    z = z + (c + b);
    z = z + (d + b);
  } 
  z = a + b;
  z = z + (b + c);
  printf("%d %d %d \n", x, y, z); 
  return 0;
}
