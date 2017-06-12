/* tpoptCompound1.c */

int printf(char *, ...);
int m, n;
int aa[100];
int i, a, b, c; 

int main()
{
  n = 1 + 2 * 3;
  m = n + 1;
  n = m * 3;
  a = m + n;
  for (i = 0; i < n; i++) {
    aa[i] = i + m + i + m + n; 
    b = m + n;
    c = m + n;
    printf(" %d", aa[i]);
  }
}
