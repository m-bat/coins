/* tpinline7.c:  Inline expansion test (static variable) */

int printf(char*, ...);
int f2(int);

static int count = 1000;
static int x;

int a, b, c;
int f1(int p)
{
  static int count = 100;
  count = count + 1;
  return count + f2(p);
}

int f2(int p)
{
  static int count = 0;
  count = count + x;
  return count;
}

int main()
{
  int i;
  x = 3;
  for (i = 0; i < 5; i++) {
    a = f1(2);
    count = count + 10;
    b = f2(3) + count;
    x = x + 1;
    c = f1(4);
    printf("a,b,c,f1(4),f2(5) = %d,%d,%d,%d,%d\n",a,b,c,f1(4),f2(5));
    /* Above statement depends on the order of evaluation of f1(4) and f2(5) */
  }
  return 0;
}

