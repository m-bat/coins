/* tpdefuse1.c: Def/use test 1 */ 

#include <stdio.h> /* SF030620 */

int i, j, k, m, n; 
int main()
{
  i = 1;
  j = i + 1;
  k = j + i;
  i = k + 1;
  j = i + k;
  k = k + j; 
  printf("i=%d j=%d k=%d ",i,j,k); /* SF030620 */
  while (k < 3) {
    printf("check\n"); /* SF030620 */
    i = i +1;
    j = j -1;
    k = k + j;
    if (k) {
      i = 3;
      j = i + j;
    }else 
      k = 1;
  }
  return 0;
}
