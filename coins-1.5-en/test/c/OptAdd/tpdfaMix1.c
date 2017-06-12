/* tpdfaMix1.c:  Data flow analysis for global/local/pointer and array 
       variables (simple version) */

int printf(char*, ...);
int func(int pa[10], int pn);

int ga1[10] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

int main()
{
  int a = 1, b = 2, c, d;
  int i = 0;
  int *ptrc, *ptry;
  int sum;
  int x[10];
  int y[10] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
  int z[10] = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
  int zz[10] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
  ptrc = &c;
  ptry = y;
  x[i] = a;
  *ptrc = x[i] + 1;
  sum = c + func(z, 10);
  d = zz[2] + zz[3];
  printf(" sum=%d ", sum);
  for (i = 0; i < 10; i++) {
    d = d + (zz[2] + zz[3]);
    d = d + z[i] + z[i];
    d = d + zz[i] + zz[i];
    sum = ga1[i] + ga1[i];
    sum = sum + *ptry;
    printf(" *ptry=%d d=%d ", *ptry, d);
    ptry = ptry + 1;
    sum = sum + z[i] + z[i];
    sum = sum + zz[i] + zz[i];
    d = d + ga1[i] + ga1[i];
  }
  d = d + ga1[2] + ga1[2];
  printf("\n"); 
  d = d + (zz[2] + zz[3]);
  d = d + ga1[2] + ga1[2];
  printf("%d %d %d \n", sum, c, d);
  return 0; 
} 

int func(int pa[10], int pn)
{
  int sum1, i;
  sum1 = ga1[0] + ga1[1];
  for (i = 0; i < pn; i++) {
    sum1 = sum1 + pa[i];
  }
  return sum1;
}

