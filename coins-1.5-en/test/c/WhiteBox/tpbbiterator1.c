/* tpbbiterator1.c:  BasicBlockIterator test 1 */

int main()
{
  int a, b, c, d;
  int i, j, k;

  { a = 1;
    b = 2;
  }
  if (a == 0) {
    if (b == 0) {
      c = a;
      a = 3;
    }else {
      c = 1;
      b = 4;
    }
    d = a + b + c;
  }else {
    {
      if (b > 0) { c = a + 2; }
    }
    d = 5;
  }
  a = d;
  while (a < 0) {
    a = a + 1;
  }
  j = 7;

  k = 11;

  for (i = 3; i < j; i = i + 1)
    k = k + i;
  c = k + 1 - k - 1;

  printf("a,b,c,d = %d,%d,%d,%d\n",a,b,d,c); /* SF030509 */
  printf("i,j,k = %d,%d,%d\n",i,j,k); /* SF030509 */

  return d;
}

