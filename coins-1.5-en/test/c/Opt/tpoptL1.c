/*---- tpoptL1.c: Local optimization test 1 ----*/

#include <stdio.h> /* SF030620 */

int a, b = 2, c = 3, d = 4;  /* See Sassa: Language processors, p. 455. */
int main()
{
  a = b + c;
  b = d;
  b = b + c;
  d = c + d;
  printf("a=%d b=%d c=%d d=%d\n",a,b,c,d); /* SF030620 */
  return d;
}
