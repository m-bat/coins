/* tpsum2.c -- Summation with subprogram */

  int  printf(char*, ...);
  void getData(int x[100]);
  int a[100];
  int sum, i;

int main()
{
  getData(a);
  sum = 0;
  for (i = 0; i < 100; i++) {
    sum = sum + a[i];
  }
  printf("sum = %d\n", sum);
  return 0;
}

void getData(int x[100])
{
  int i;
  i = 0;
  while (i < 100) {
    x[i] = i;
    i++;
  }
}


