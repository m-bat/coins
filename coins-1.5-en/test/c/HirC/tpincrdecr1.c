/* tpincrdecr1.c: Increment, decrement operations 
                  and their combination (HirC) */

#include <stdio.h>

int x, y, z;
int a[10];
int *pa;

int main()
{
  int i, j, k;
  double nc;

  /*  for (nc = 0; getchar() != EOF; ++nc) */
  /*    ;                                  */
  /*  printf("%.0f\n", nc);                */
  printf("tpincrdecr1.c: NOT SOLVED YET!!!\n");

  i = 0;
  j = i++;
  pa = a;
  *pa = 1;
  printf("i=%d j=%d\n*pa=1 -> %d\n",i,j,*pa); /* SF030620 */
  *pa+=1;
  printf("*pa+=1 -> %d\n",*pa); /* SF030620 */
  ++*pa;
  printf("++*pa -> %d\n",*pa); /* SF030620 */
  (*pa)++;
  printf("(*pa)++ -> %d\n",*pa); /* SF030620 */
  return 0;
}


