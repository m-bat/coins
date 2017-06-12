/* tpinline8.c:  Inline expansion test (subprogram call in loop) */

int printf(char*, ...);

static int x = 0;

int a, b, c;
int f1(int p)
{
  int i;
  if (p < 1)
    return 1;
  for (i = 0; i < 3; i++) {
    x =  x + f1(p - 1);
  }
  return (p - 1) + x;
}

int main()
{
  int i;
  for (i = 0; i < 3; i++) {
    a = f1(i);
    printf("a = %d\n",a);
  }
  return 0;
}

