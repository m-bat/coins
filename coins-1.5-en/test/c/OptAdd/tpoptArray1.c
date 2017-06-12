/* tpoptArray1.c: Optimize array element expressions */
int printf(char*, ...);
  int m = 1, n = 2;
  int aa[10]; 
int main()
{
  int a, b, c, d, i, j;
  i = 1;
  j = 2;
  aa[i] = i;
  aa[j] = j;
  c = aa[i] + aa[j];
  printf(" %d %d ", c, aa[i]+aa[j]);
}


