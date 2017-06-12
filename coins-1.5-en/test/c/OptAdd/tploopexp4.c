/* tploopexp4.c */
/*   Loop expansion test (multiple step variables) */

int printf(char*, ...);

int main()
{
  int i, j, n;
  float x, y;
  int sum1 = 0, sum2 = 0;
  int aa[100];
  n = 100;
  for (i = 0; i < n; i++) {
    aa[i] = 2 * i + 1;
  }
  for (i = 2, j = 0; i < 100; i = i + 2, j = j + 3) {
    sum1 = sum1 + i;
  } 
  for (i = 0; i < 100; i = i + 1, j = j + 3) {
    sum2 = sum2 + aa[i];
  } 
  printf(" j=%d sum2=%d\n", j, sum2);
  x = 2.0;
  for (i = 10; i < n; i = i + 4) {
    sum1 = sum1 + i;
    sum2 = sum2 + aa[i];
    x = x + 2.5;
  } 
  printf(" sum1=%d sum2=%d x=%f\n", sum1, sum2, x);
  return 0;
}
