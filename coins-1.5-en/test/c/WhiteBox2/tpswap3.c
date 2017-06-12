/* tpswap3.c: Swap array element */
int printf(char*, ...);
int a[10] = {0, 2, 1, 4, 3, 6, 5, 8, 7, 9 };
int x[2] = {2, 3};
int main()
{
  int i, j, temp; 
  i = x[0];
  j = x[1];
  temp = a[i];
  a[i] = a[j];
  a[j] = temp; 
  printf(" %d %d", a[2], a[3]);
}


