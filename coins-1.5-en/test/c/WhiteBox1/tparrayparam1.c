/*---- tparrayparam1.c: Array parameter test */ 

int printf(char*, ...);

int 
func( int a[10], char b[2][3], int *p )
{
  int i, j, s = 0, t = 0;
  a[0] = 0;
  for (i = 1; i < 10; i++) {
    a[i] = a[i-1] + 1;
    s = s + a[i];
  }
  printf("s= %d \n", s);
  for (i = 0; i < 2; i++)
    for (j = 0; j < 3; j ++) {
      printf(" %c", b[i][j]);
      t = t + b[i][j];
    }
  return  s + t + *p;
}

int main()
{
  int  aa[10];
  char bb[2][3] = { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };
  int  r = 3;
  int *pp= &r;
 
  r = func(aa, bb, pp);
  printf("\n r = %d aa[9]=%d \n", r, aa[9]);
  return 0;
}
