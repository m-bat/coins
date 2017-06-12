/*---- tppre04.c: PRE for compound variables (array elements). */ 

/*
#include <stdio.h> 
*/

int data[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

int main()
{
  int a, b, c, d, e, f;
  int i, j;
  int x, y, z, x1, y1, z1, z2;
  i = 1;
  j = 2;
  a = data[1];
  if (a != 0) {
    x = data[i];
  }else {
    x = data[j] + j;
  } 
  y = data[i] + x; 
  z = data[j] + j; 
  printf("%d %d %d \n", x, y, z); 
  return 0;
}
