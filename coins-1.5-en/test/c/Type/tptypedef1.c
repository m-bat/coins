/* tptypedef1.c:  typedef test (basic) */

typedef int integer;
integer a, b, c;
int     x;
integer y[3], z;
int main()
{
  c = 1; /* SF030509 */

  a = 1;
  b = a - 2;
  c = b - a / c;
  x = a + (b + c) * a;

  printf("a,b,c,x = %d,%d,%d,%d\n",a,b,c,x); /* SF030509 */

  return x;
}

