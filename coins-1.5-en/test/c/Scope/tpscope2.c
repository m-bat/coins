/* tpscope2.c:  Test scope of symbols in blocks */

int a, b, c;
int fn(int p)
{
  int d;
  d = 1;
  d = p + d;
  return d;
}

int printf(char*, ...);

int main()
{
  int e, f, g;
  a = 0;
  b = 1;
  c = b;
  { int e2, f2; 
    e2 = fn(c);
    { int e3;
      e3 = e2;
      f2 = e3 + 1;
    }
    f = f2;
  } 
  g = f + fn(c) * 16;
  { int h, i; 
    h = f + 1;
    { int k;
      k = h + 1;
      i = k;
    }
    f = f + i;
    { int m, n, i2;
      m = 0; 
      for ( i2 = 0; i2 < 10; i2 = i2 + 1) {
        int n2;
        n2 = i2;
        m = m + n2;
      }
      h = m;
    }
    e = h + i;
  }
  printf("%d %d %d \n", f, g, e);
  return 0;
}

