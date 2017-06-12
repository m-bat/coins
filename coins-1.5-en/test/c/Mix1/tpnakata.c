/* tpnakata.c  -- Partial redundancy elimination (by I. Nakata) */

int a, b, c, i, j, s, t;
 int main() 
{
  a = 1;
  b = a + 1;
  s = 0;
  i = 0;
  while (i < 10) {
    t = a + b;
    s = s + t;
    i++;
  }
  printf("s=%d\n", s);
  s = 0;
  for (j = 0; j < 5; j++) {
    i = 0;
    while (i < 10) {
      i++;
      t = a + b;
      s = s + t;
    }
  }
  printf("s=%d\n", s);
  return 0;
}

