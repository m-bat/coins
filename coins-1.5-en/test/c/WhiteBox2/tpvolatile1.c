/* tpvolatile1.c:  volatile test */

int a, b, c;
unsigned int aa[10] = {1, 2, 3, 4 };

static GC_test_and_set(volatile unsigned int *addr) 
{
  return *addr+2;
}

int main()
{
  a = GC_test_and_set(aa);
  printf(" %d \n", a);
  return 0;
}
