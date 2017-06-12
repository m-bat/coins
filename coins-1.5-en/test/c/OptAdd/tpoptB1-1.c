/* tpoptB1-1.c:  Branch optimization */
/*               Local variables */

#include <stdio.h> /* SF030620 */

int main()
{
  int a, b, c;
  int x;

  a = 1;
  b = 2;
  c = 9;   /*##*/
  x = 10;  /*##*/
  if (a)
    b = 0;
  else 
    ;
  if ((a > b+1)||(a > 2)) {
    x = 0;
    c = a;
  }else {
    goto last;    
  }
last:
  printf("a=%d b=%d c=%d x=%d \n",a,b,c,x); /* SF030620 */
  return c;
} 

