/*---- tpoptG1.c: Global optimization (integer expressions) ----*/

#include <stdio.h> /* SF030620 */

int a, b, c, d, e, f;

int main()
{
  a = 1; 
  b = 2 + 3;
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
  printf("a=%d b=%d c=%d d=%d e=%d f=%d return %d\n",a,b,c,d,e,f,d+2); /* SF030620 */
  return d + 2;
}
