/* tploopif4.c -- hirOpt=loopif test (with continue) */

  int printf(char*, ...);

  int a[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

int sum(int pa[10], int pn, int pMode)
{
  int lSum = 0, i;
  for (i = 0; i < pn; i++) {
    if (lSum > 20)
      continue;
    if (pMode > 1)
      lSum = lSum + pa[i] + i;
    else
      lSum = lSum + pa[i] + i * i;
  }
  return lSum; 
}

int main()
{
  int sum1, sum2;
  sum1 = sum(a, 10, 1);
  sum2 = sum(a, 10, 2);
  printf("sum1=%d  sum2 = %d \n",sum1, sum2);
  return 0;
}

