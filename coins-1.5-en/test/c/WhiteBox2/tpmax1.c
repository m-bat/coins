/* tpmax1.c: maximum value of array elements */

int printf(char*, ...);

int main()
{
  int i, n, max;
  int a[100];

  for (i = 0; i < 100; i=i+1) {
    a[i] = i*(100-i);
  }
    
  n = 100;
  max = a[0];
  for (i = 1; i < n; i=i+1) {
    if (a[i] > max) {
        max = a[i];
    }
  }
  printf(" %d", max);
}


