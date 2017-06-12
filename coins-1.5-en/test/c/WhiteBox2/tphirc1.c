/* tphirc1.c:  HIR-C to HIR-base test  */

int a=11, b=22, c=33, d=44;
int xx[10];
int printf(char*, ...);

int main()
{
  int i, j, k;
  j = a++, c;
  k = a==b ? c : d;
  for (i = 0; i < 3; i++)
    a += 1;
   
  while (i-- > 0)
    xx[i] += j;
  printf("%d %d %d \n", i, j, k);
}

