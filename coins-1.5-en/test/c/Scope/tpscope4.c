/* tpscope4.c:  Test of duplicated declaration */

int a, b, c;

int fn(int p)
{
  int d;
  d = 1;
  d = p + d;
  return d;
}

int a, b, c;

int main()
{
  int e, f, g;
  a = 0;
  b = 1;
  c = b;

/*   int e; */ /* Redeclared variable */

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

