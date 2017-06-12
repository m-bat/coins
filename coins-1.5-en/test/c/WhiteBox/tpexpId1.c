/** tpexpId1.c:  Expression ID test */
int main()
{
  int i, x[10], j;
  int y[20];

  i = 0;
  j = i + 1;
  x[i] = i + 1;
  y[i + 1] = x[i] + 1;
  for (i = 1; i < 10; i = i + 1) {
    x[i] = x[i-1] + i;
    y[i] = x[i] + x[i-1];
  }

  printf("0 = %d\n",i+1-(i+1)); /* SF030509 */

  return i + 1 - (i + 1);
}
