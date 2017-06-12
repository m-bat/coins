/*---- tppre14.c: PRE for nested repeat-loop with outside redundant expressions */ 
/*
#include <stdio.h> 
*/

int data[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

int main()
{
  int a, b, c, d, e, f;
  int i, j;
  a = data[1]; 
  b = data[2];
  c = data[3];
  d = 0;
  i = 0;
  do {
    d = d + (a + b);
    j = 0;
    do {
      d = d + (b + c);
      j = j + 1;
    } while (j < 3);
    i = i + 1;
  } while (i < 10);
  d = d + (a + b); 
  d = d + (b + c); /* Redundancy appears at outside */
  printf("%d %d \n",c,d); 
  return 0;
}
