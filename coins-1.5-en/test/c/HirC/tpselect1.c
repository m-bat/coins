/* tpselect1.c:  ?: expression */

int main()
{
  int a, b, c;

  a = 1;
  b = 2;
  c = (a > b ? a : b);
  printf("c=%d\n",c);
  return c - c;
} 

