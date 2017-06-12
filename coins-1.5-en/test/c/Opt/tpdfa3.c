/* tpdfa3.c  Loop with branch */

#include <stdio.h> /* SF030620 */

int i = 0, j = 1, k, m, n;
int a, b, c, d;

int main() 
{
  m = 5;
  b = 1;
  c = m + b;
  k = j + 1;
  a = b + c;
  printf("a=%d b=%d c=%d d=%d\n",a,b,c,d); /* SF030620 */
  while (i < 10) {
    if (j > 5) {
      n = 6;
      k = j + 1;
    }else {
      j = j + 1;
      m = m - n;
      d = b + c;
    }
    i++;
  } 
  a = b + c;
  d = d + a;
  printf("a=%d b=%d c=%d d=%d\n",a,b,c,d); /* SF030620 */
  return d;
}

