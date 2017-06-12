/* tpTypeDef.c  -- Test typedef. */

typedef int unsigned intU;
intU a[10];
int  b[10];

int main()
{
  a[0] =0;
  b[1] = a[0];
  printf("%d %d \n", a[0], b[1]);
  return 0;
}
