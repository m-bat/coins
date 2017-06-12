/*---- tppre10-2.c: PRE with loop */ 
/*
#include <stdio.h> 
*/

int data[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

int main()
{
  int a, b, c, n;
  int i;
  a = data[2]; 
  b = data[3];
  n = 10;
  c = 0;
  for (i = 0; i < n; i++) {
    c = (a * b) + i + c;
  }
  printf("%d \n",c); 
  return 0;
}
