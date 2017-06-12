/*---- tppre10.c: PRE with loop */ 
/*
#include <stdio.h> 
*/

int data[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

int main()
{
  int a, b, c, d, e, f;
  int i;
  a = data[1]; 
  b = data[2];
  c = 0;
  for (i = 0; i < 10; i++) {
    c = c + (a + b);
  }
  d = a + b;
  printf("%d %d \n",c,d); 
  return 0;
}
