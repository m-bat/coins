/* tpoptpre1.c : test partial redundancy elimination */

#include <stdio.h> /* SF030620 */

int a, b, c, d;
int i, j, k;
int aa[10], bb[10];

int main()
{
  a = 1;
  b = 2;
  c = 3;
  for (i = 0; i < 10; i++) {
    aa[i] = a + b;
    for (j = 0; j < 10; j++) {
      bb[j] = a + b + c;
    }
  }
  /* SF030620[ */
  printf("a=%d b=%d c=%d d=%d i=%d j=%d k=%d aa[9]=%d bb[9]=%d\n",
	 a,b,c,d,i,j,k,aa[9],bb[9]);
  /* SF030620] */
  return 0;
}
