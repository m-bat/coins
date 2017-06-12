/* tpsum0.c -- Simple arithmetic (summation) */

  int a[100];
  int sum, i;

int main()
{
  sum = 0;
  for (i = 0; i < 100; i++) {
    sum = sum + a[i];
  }
  printf("sum=%d\n",sum); /* SF030620 */
  return 0;
}

