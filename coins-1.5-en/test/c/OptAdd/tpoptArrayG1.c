/*---- tpoptArrayG1.c: Global optimization (integer expressions) ----*/
/*       with global variables, subscripted variable, with call, without <stdio> */

int printf(char*, ...);

int a, b, c, d, e, f, g, h;
int m = 10, n = 5;
int aa[10] = {1, 2, 3}, bb[5] = {4, 5, 6};

int main()
{
  int cc[10], dd[5];
  int i, j;
  i = 0;
  j = 1;
  a = aa[i];
  cc[i] = aa[i] + 1;
  cc[i+1] = cc[i] + aa[i+1];
  dd[i+1] = cc[i] + aa[i+1];
  printf("%d %d %d %d\n", aa[i], cc[i], cc[i+1], dd[i+1]);
  dd[i+1] = cc[i] + aa[i+1];
  return 0;
}
