/* tpsum4.c -- Simple arithmetic (summation) */

  int printf(char*, ...);

  int a[100];
  int sum1, sum2, i;

int main()
{
  for (i = 0; i < 100; i++)
    a[i] = i;
  sum1 = 0;
  sum2 = 0;
  for (i = 0; i < 100; i++) {
    sum1 = sum1 + i;
    sum2 = sum2 + a[i];
  }
  printf("sum1=%d  sum2 = %d \n",sum1, sum2);
  return 0;
}

