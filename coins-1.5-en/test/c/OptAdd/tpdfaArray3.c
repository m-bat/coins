/* tpdfaArray3.c:  Data flow analysis for array elements  */

int printf(char*, ...);

  int a = 1, b = 2, c, d;
  int i = 1, j = 2, k = 3;
  int sum;
  int ga[20];

int main()
{
  int x[20];
  int y[20];
  x[0] = 0;
  x[i] = a;
  c = x[i] + 1;
  x[j] = x[i] + c;
  x[k] = x[j] + c;
  c = x[i] + x[j]; 
  ga[0] = 0;
  ga[i] = a;
  d = ga[i] + 1;
  ga[j] = ga[i] + c;
  ga[k] = ga[j] + c;
  d = ga[i] + ga[j]; 
  sum = c;
  for (i = 0; i <= k; i++) {
    sum = sum + x[i] + ga[i];
  }
  printf("%d %d %d \n", sum, c, d);
  return 0; 
} 

