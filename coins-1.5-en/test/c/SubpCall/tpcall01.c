/* tpcall01.c:  Basic call 01 */

int a, b, c;
int fn(int p)
{
  int d;
  d = 1;
  d = p + d;
  return d;
}
int main()
{
  int e, f, g;
  a = 0;
  b = 1;
  c = b;
  e = fn(c);
  f = fn(c) * 16;
  printf("a,b,c,e,f = %d,%d,%d,%d,%d\n",a,b,c,e,f); /* SF030609 */
  return 0;
}
