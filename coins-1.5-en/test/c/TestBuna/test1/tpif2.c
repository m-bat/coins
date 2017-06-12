/* tpif2.c:  if-statement with block */
int printf(char *s, ...);

int a, b, c;
int x;
int main()
{
  a = 1;
  b = 2;
  if (a > b+1) {
    x = 0;
    c = a;
  }else {
    x = 1;
    c = a + 2;
  }
  printf("x = %d, c = %d\n", x, c);
  return c;
} 

