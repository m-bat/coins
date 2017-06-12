/*---- tppre13.c: PRE for nested for-loop with outside redundant expressions */ 
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
  for (i = 0; i < 10; i++) {
    d = d + (a + b);
    for (j = 0; j < 3; j++) {/* (b+c) is not computed if j<3 is false,*/
      d = d + (b + c);    /* so (b+c) is not recognized as redundant. */
    }
  }
  d = d + (a + b); /* Redundancy appeaes at outside */
  d = d + (b + c); 
  printf("%d %d \n",c,d); 
  return 0;
}
