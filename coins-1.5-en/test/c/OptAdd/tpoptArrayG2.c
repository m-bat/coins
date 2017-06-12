/*---- tpoptArrayG2.c: Global optimization (integer expressions) ----*/
/*       with global variables, subscripted variable, with call, 
         with functionWithoutSideEffect, without <stdio> */

#pragma functionsWithoutSideEffect sin printf
#pragma functionsWithSideEffect cos

int printf(char*, ...);
double sin(double p);
double cos(double p);

int a, b, c, d, e, f, g, h;
int m = 10, n = 5;
int aa[10] = {1, 2, 3}, bb[5] = {4, 5, 6};
double eee[10] = {1.0, 2.0, 3.0};

int main()
{
  int cc[10], dd[5];
  int i, j;
  int s = 0;
  double x = 0.0;
  i = 0;
  j = 1;
  a = aa[i];
  cc[i] = aa[i] + 1;
  cc[i+1] = cc[i] + aa[i+1];
  dd[i+1] = cc[i] + aa[i+1];
  printf("%d %d %d %d\n", aa[i], cc[i], cc[i+1], dd[i+1]);
  dd[i+1] = cc[i] + aa[i+1];
  s  = s + (int)sin((double)a) + (cc[i] + aa[i+1]);
  s  = s + (int)sin((double)a) + (cc[i] + aa[i+1]);
  x  = x + cos((double)a) + (eee[i] + eee[i+1]);
  x  = x + cos((double)a) + (eee[i] + eee[i+1]);
  printf("s = %d x=%f\n", s, x);
  return 0;
}
