/* tpsum2.c -- Summation with subprogram */

  int  printf(char*, ...);

  int a[100];
  int sum, i;

int main()
{
  for (i = 0; i < 100; i++)
    a[i] = i;
  sum = 0;
  for (i = 0; i < 100; i++) {
    sum = sum + a[i];
  }
  printf("sum = %d\n", sum);
  return 0;
}


