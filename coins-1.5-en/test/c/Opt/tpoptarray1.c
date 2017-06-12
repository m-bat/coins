/* tpoptarray1.c:  Array subscript and range-check optimization */

#include <stdio.h> /* SF030620 */

int i, x[10], j, k, l;
int y[20], z[30];
int main()
{
  i = 1;
  j = i + 2;
  k = 0;
  for (l = 1; l < 3; l++)
    k = k + l;
  if (j < 10)
    x[j] = 1;
  if (k < 10) {
    x[k] = k;
    if (k < 10)
      y[k] = k+1;
  }
  /* SF030620[ */
  printf("i=%d j=%d k=%d l=%d x[%d]=%d x[%d]=%d y[%d]=%d\n",
	 i,j,k,l,j,x[j],k,x[k],k,y[k]);
  /* SF030620] */
  if ((i >= 0)&&(i < 20)) 
    y[i] = x[j]*4;
  if ((k >= 0)&&(k < 20)) {
    y[k] = 4;
    if (k >= 0)
      y[k] = y[i] + 1;
    if ((k >= 0)&&(y[i] > 100)) {
      l = i;
      y[i] = 100;
    }
  }
  /* SF030620[ */
  printf("i=%d j=%d k=%d l=%d x[%d]=%d y[%d]=%d y[%d]=%d\n",
	 i,j,k,l,j,x[j],i,y[i],k,y[k]);
  /* SF030620] */
  return 0;
} 

