/* tpreplaceGlobal1.c */

int printf(char *, ...);
int m, n;
int a[100];
int i; 

int main()
{
  n = 1;
  n = n * 10;
  m = n + 1;
  for (i = 0; i < n; i++) {
    a[i] = i + m + i + m + n; 
    printf(" %d", a[i]);
  }
}
