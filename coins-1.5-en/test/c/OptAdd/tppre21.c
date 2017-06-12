/*---- tppre21.c: PRE for array parameter */ 
/*
#include <stdio.h> 
*/

int data[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

int func(int pi, int pn, int pa[])
{
  int i, s = 0;
  int aa[3] = {10, 20, 30};
  for (i = 0; i < pn-1; i++) {
    pa[i] = pa[i] + aa[pi] + s;
    s = s + pa[i];
  }
  return pa[pn-2] + s;
}

int main()
{
  printf("%d\n", func(2, 5, data));
  return 0;
}
