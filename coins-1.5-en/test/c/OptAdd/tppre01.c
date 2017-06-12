/*---- tppre01.c: PRE (Partial Redundancy Elimination) 1 */ 
/*
#include <stdio.h> 
*/

int data[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

int main()
{
  int a, b, c, d, e, f;
  a = data[1]; 
  b = data[2];
  if (a != 0) {
    c = a + b;
  }else {
    c = a + 1;
  } 
  d = a + b;
  printf("%d %d \n",c,d); 
  return 0;
}
