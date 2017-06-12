/* tpscope5.c:  Test scope of symbols with duplicated names and
                multiple subprogram */

int a, b, c;
int fn(int p)
{
  int d;
  d = 1;
  d = p + d;
  return d;
}
int func(int a, int b) {
  int c = 3;
  int d = 4;

  return a+b+c+d;
}
int func1(){
  int a = 1;
  int b = 1;
  return func(a,b);
}

int func5(){
  int e;
  int f;
}

int main()
{
  int e, f, g;
  a = 0;
  b = 1;
  c = b;
  { int e, f;
    e = fn(c);
    { int e;
      f = e + 1;
    }
  }
  f = fn(c) * 16;
  { int h, i;
    h = f + 1;
    { int k;
      k = h + 1;
      i = k;
    }
    { int m, n, i;
      m = 0;
      for ( i = 0; i < 10; i = i + 1) {
        int n;
        n = i;
        m = m + n;
      }
    }
  }
  printf("a,b,c,f = %d,%d,%d,%d\n",a,b,c,f); /* SF030609 */
  return 0;
}

