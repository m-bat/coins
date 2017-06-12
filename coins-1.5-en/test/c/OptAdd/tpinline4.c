/* tpinline4.c:  Inline expansion test (mean value) */

int printf(char*, ...);

int a, b, c;
int mean(float a[], int count)
{
  int i;
  float sum = 0.0;
  for (i = 0; i < count; i++) {
    sum = sum + a[i];
  }
  return sum/count;
}

int sub1(int p)
{
  printf("sub1 ");
}

int main()
{
  int i, n;
  float aa[100], average;
  n = 100;
  for (i = 0; i < n; i++) {
    aa[i] = i;
  }
  average = mean(aa, n/2);
  sub1(3);
  printf("mean = %f %d\n", average, n/2);
  return 0;
}
