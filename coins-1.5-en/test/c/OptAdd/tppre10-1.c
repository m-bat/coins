/*---- tppde10-1.c: PDE with loop */ 
/*     i=i+1; is changed to ti=i+1; i=ti;
/*
#include <stdio.h> 
*/

int data[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

int main()
{
  int a, b, c, d, e, f;
  int i;
  int tc, ti;
  a = data[1]; 
  b = data[2];
  c = 0;
  for (i = 0; i < 10; /*i++*/
       ti=i+1,i=ti) {
    /* c = c + (a + b); */
    tc = c + (a + b); 
    c = tc;
  }
  d = a + b;
  printf("%d %d \n",c,d); 
  return 0;
}
