/* tpmax0.c: maximum value of array elements */
int printf(char*, ...);
int a[10] = {0, 2, 1, 4, 3, 6, 5, 8, 7, 9 };
int main()
{
  int i, n, max;
  n = 10;
  max = a[0];
  for (i = 1; i < n; i=i+1) {
    if (a[i] > max) {
        max = a[i];
    }
  }
  printf(" %d", max);
}


