/* tpdfaArray2.c:  Data flow analysis for global variables */

int printf(char*, ...);

  int a = 1, b = 2, c, d;
  int i = 0, j = 1, k = 1;
  int sum;
  int x[20][10];
  int y[20];
  int z[10];

int main()
{
  x[i][j] = a;
  c = x[i][j] + 1;
  x[i][j] = x[i][j] + c;
  c = x[i][j] + x[i][j]; 
  d = x[0][k] + x[k-1][1]; 
  sum = c;
  for (i = 0; i < 20; i++) {
    x[i][0] = i;
    for (j = 1; j < 10; j++) {
      x[i][j] = x[i][j-1] + 1;
      c = c + x[i][j] + x[i][j-1];
    }
  }
  d = c;
  for (i = 0; i < 20; i++) {
    y[i] = 0;
    for (j = 0; j < 10; j++) {
      y[i] = y[i] + x[i][j];
      sum = sum + y[i];
    }
    d = d + y[i];
  }
  printf("%d %d %d \n", sum, c, d);
  return 0; 
} 

