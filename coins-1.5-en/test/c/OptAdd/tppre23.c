/*---- tppre23.c: PRE for array parameter and local array */ 
/*   hoisting loop invariant expressions j*j and aa[j]. */

int printf(char*, ...);

int data[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

int func(int pi, int pn, int pa[])
{
  int i, j, s;
  int aa[3] = {10, 20, 30};
  j = pi;
  s = aa[j];
  for (i = 0; i < pn-1; i++) {
    s = s + aa[j] + j*j;
    pa[i] = aa[j] + pa[i] + s;
    s = s + pa[i];
  }
  s = s + j*j;
  return pa[pn-2] + s;
}

int main()
{
  printf("%d\n", func(2, 9, data));
  return 0;
}
