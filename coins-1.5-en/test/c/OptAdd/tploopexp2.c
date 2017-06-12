/* tploopexp2.c */
/*   Loop expansion test */

int printf(char*, ...);

int main()
{
  int i, j, n;
  int sum1 = 0, sum2 = 0;
  int aa[100];
  n = 100;
  for (i = 0; i < n; i++) {
    aa[i] = 2 * i + 1;
  }
  for (i = 0; i < 100; i++) {
    sum1 = sum1 + i;
  } 
  for (i = 0; i < 100; i++) {
    sum2 = sum2 + aa[i];
  } 
  for (i = 0; i < n; i++) {
    sum1 = sum1 + i;
    sum2 = sum2 + aa[i];
  } 
  printf(" %d %d\n", sum1, sum2);
  return 0;
}
