/* tpheap.c  -- Heap sort for small array (Saze) */
void heapsort(int pn, int a[]);

int main()
{
  int a[1000];
  int i;

  a[1] = 7;
  a[2] = 5;
  a[3] = 1;
  a[4] = 9;
  a[5] = 2;
  a[6] = 6;
  a[7] = 8;
  a[8] = 4;
  a[9] = 3;
  a[10] = 3;
  heapsort(10, a);
  for (i = 1; i <= 10; i++)
    printf("\n[%d] %d", i, a[i]);
  return 0; 
}

void 
heapsort(int pn, int a[])
{
  int i, j, k;
  int x, n;
 
  n = pn;
  for (k = n / 2; k >= 1; k--) {
    i = k;
    x = a[i];
    while ((j = 2 * i) <= n) {
      if (j < n && a[j] < a[j + 1])
        j++;
      if (x >= a[j]) 
        break;
      a[i] = a[j];
      i = j;
    }
    a[i] = x;
  }
  while (n > 1) {
    x = a[n];
    a[n] = a[1];
    n--;
    i = 1;
    while ((j = 2 * i) <= n) {
      if (j < n &&a[j] < a[j + 1])
        j++;
      if (x >= a[j])
        break;
      a[i] = a[j];
      i = j; 
    } 
    a[i] = x;
  }
} 

