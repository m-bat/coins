/* tpassignWithCall2.c:  Basic assignment with arithmetic operations */
/*   With local variable and expression parameters */
int x;
int main()
{
  int a, b, c;
  a = 1;
  b = a + 2;
  c = b + a * b; 
  if (c > 0)
    x = a + (b + c) * a;
  else
    x = 0;
  printf("x=%d c=%d a+b=%d\n", x, c, a+b);
  return 0;
} 

