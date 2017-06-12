/* tpassignWithCall4.c:  Basic assignment with arithmetic operations */
/*   With local variable and expression parameters */

int printf(char*, ...);

int x, y, z;
int main()
{
  int a, b, c, d, e;
  a = 1;
  b = a + 2;
  c = b + a * b; 
  if (c > 0)
    x = a + (b + c) * a;
  else
    x = 0;
  y = x + 2;
  z = y + 1;
  printf("x=%d c=%d a+b=%d y=%d z=%d\n", x, c, a+b, y, z);
  return 0;
} 

