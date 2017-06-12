/* tpassignWithCall1.c:  Basic assignment with arithmetic operations */
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
  printf("x=%d \n", x);
  return 0;
} 

