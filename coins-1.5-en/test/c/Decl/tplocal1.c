/* tplocal1.c  Local variable declaration */
/* (Decl) */

int g1, g2, g3;

int main()
{
  int i, j, k;
  int a[10];

  g1 = 1;
  i  = 2;
  a[i] = g1;
  j    = i + a[2];
  printf("i=%d j=%d g1=%d a[i]=%d a[2]=%d \n",i,j,g1,i,a[i],a[2]); /* SF030620 */
  return 0;
}

