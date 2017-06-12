/* tpoptB2.c:  Branch optimization 2 (NOT YET. COPIED FROM tpoptB1.c) */

#include <stdio.h>

int a, b, c;
int x;
int main()
{
  a = 1;
  b = 2;
 start:
  if (a)
    ;
  else 
    b = 0;
  x = 2;
  if (a == 1) {
   first:
    if (b < 1)
      goto next;
    else if (b == 2) {
      b = 1;
      goto first;
    }else {
      b = 0;
      goto start;
    }
    c = 0;
  next:
    a = 0;
  }else {
    if (b > 0)
      goto last;    
  }
  switch (a) {
  case 0:
    a = 101;
    break;
  case 1:
    a = 102;
  case 3:
    a = a + 1;
    break;
  case 4:
    x = b;
    goto last;
  default:
    a = 100;
  };
  c = x + 1;
last:
  printf("a=%d b=%d c=%d x=%d\n",a,b,c,x); /* SF030620 */
  return c;
} 

