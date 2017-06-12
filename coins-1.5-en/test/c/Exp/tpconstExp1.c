/* tpconstExp1.c  -- constant exp  */

int i, j, k;
int  a[10];
int c = 3, d = 4;

main()
{
  int x = 1;
  i = 100 + 10;
  j = 50 / 5;
  k = x + 2*3;
  a[0] = 3;
  a[1] = 4;
  i = -1;
  j = -i + a[-1+2];
  printf("i=%d j=%d\n",i,j); /* SF030620 */
  return 0;
}

