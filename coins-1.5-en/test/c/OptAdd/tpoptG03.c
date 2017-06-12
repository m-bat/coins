/*---- tpoptG03.c: Global optimization (integer expressions) ----*/
/* See tpoptG02 */
/*
#include <stdio.h> 
*/

int ff(int p1);

int main()
{
  int a, b, c, d, e, f;
  a = ff(1);
  b = ff(2 + 3);
  c = a + b;
  d = a + 1;
  if (a) {
    c = a + b;
    d = (a + b) + c;
    e = a + 1;
    f = a + b + c;
  }else {
    c = a + 1;
    d = 3 * (a + 1);
    e = a;
    f = e + 1;
  } 
  d = d + 2;
  printf("a=%d b=%d c=%d d=%d e=%d f=%d return %d\n",a,b,c,d,e,f,d+2); 
  return 0;
}

int ff(int p)
{
  return p;
}

