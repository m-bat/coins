/* tpstatic2: static declaration */

int printf(char*, ...);

static int a, b, c[10];

static int f()
{
  static int count = 0;
  count++;
  return count;
}

int main()
{
  static int d = 1;
  a = f() + d;
  b = f();
  d = f();
  printf("%d %d %d \n", a, b, d);
  return 0;
}

