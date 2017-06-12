/* tplocal1.c  Local variable declaration */
int printf(char *s, ...);

int g1, g2, g3;

int main()
{
  int i, j, k;
  int a[10];

  g1 = 1;
  i  = 2;
  a[i] = g1;
  j    = i + a[2];
  
  printf("j = %d\n", j);
  return 0;
}

