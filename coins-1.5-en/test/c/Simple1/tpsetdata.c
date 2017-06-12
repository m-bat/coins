/* tpsetdata.c -- Set data to parameter */

void getData(int x[100])
{
  int i;
  i = 0;
  while (i < 100) {
    x[i] = i;
    i++;
  }
}

int main()
{
  int a[100];
  int i;
  getData(a);
  for (i = 0; i < 10; i++)
    printf("%d ", a[i]);
  return 0;
}


