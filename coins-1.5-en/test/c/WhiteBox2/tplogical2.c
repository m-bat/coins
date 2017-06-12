/* tplogical2.c:   */

int printf(char*, ...);

int main()
{
  int a, b, c, d;
  int i, j, k;

  a = 1;
  b = 2;
  c = 3;
  if (a != b) {
    if (b >= c)
      d = a != b;
    else
      d = b < c;
  }else {
    if (! (b > c))
      d = b <= c;
    else
      d = b > c;
  }
  printf("d=%d\n",d);
  return 0;
} 

