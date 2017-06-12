/* tpsimple1.c -- Simple arithmetic  */

  int y[10],z[10];
  int   i, j, k;

int main()
{
  i = 1;
  j = 2;
  k = i + j * 3;
  if (i > 0)
    k = k + i;
  else
    k = k - i;
    y[0] = k;
    printf("i=%d j=%d k=%d\n",i,j,k); /* SF030620 */
  return 0;
}

