/* tpcall0.c:  Basic call */

int a, b;
char c, c2, c3;
int fn(char p)
{
  int d;
  d = 1;
  return (int)p+d;
}
int main()
{
  a = 0;
  b = 1;
  c = 'x';
  printf("a %d b %d c %x \n", a, b, c);
  a = fn(c);
  b = fn(c) * 16;
  printf("a %d b %d c %x \n", a, b, c);
  return 0;
}

