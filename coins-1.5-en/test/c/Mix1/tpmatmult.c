#include <stdio.h>
/*#include <stdlib.h>  */
/*#include <limits.h>  */

/*#define N 4 */
/*#define	INT_MAX			 0x7FFFFFFF */
int nrow[4 +1] = {4, 2, 6, 3, 5},
    mincost[4][4], split[4][4];

void main()
{
  int i, left, right, length, cost, min, choice;

  for (i = 0; i < 4; i++)
    mincost[i][i] = 0;
  for (length = 1; length < 4; length++) {
    for (left = 0; left < 4 - length; left++) {
      right = left + length;
/*      min = INT_MAX; */
      min =  0x7FFFFFFF;
      for (i = left; i < right; i++) {
        cost = mincost[left][i]
             + mincost[i+1][right]
             + nrow[left]*nrow[i+1]*nrow[right+1];
        if (cost < min) {
          min = cost;
          choice = i;
        }
      }
      mincost[left][right] = min;
      split[left][right] = choice;
      printf("miscost[%d][%d]=%3d split[%d][%d]=%d\n",  /* SF030620 */
	     left,right,mincost[left][right], /* SF030620 */
	     left,right,split[left][right]); /* SF030620 */
    }
  }
  printf("min=%d choice=%d\n",min,choice); /* SF030620 */
}
