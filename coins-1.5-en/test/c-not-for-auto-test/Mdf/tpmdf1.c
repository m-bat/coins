/* tpmdf1.c: Test mdf parallelization */

int printf(char*, ...);

int main()
{
  int i, n = 1000, sum, max;
  int a[1000];
  for (i = 0; i < 1000; i=i+1) {
    a[i] = i*(1000-i);
  }
  sum = 0;
  for (i = 0; i < 1000; i=i+1) {
    sum = sum + a[i];
  }
  max = a[0];
  for (i = 1; i < n; i=i+1) {
    if (a[i] > max) 
        max = a[i];
  }
  printf(" sum=%d max=%d\n", sum, max);
}


