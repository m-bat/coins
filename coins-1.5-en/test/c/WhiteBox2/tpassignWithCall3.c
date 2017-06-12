/* tpassignWithCall3.c:  Basic assignment with arithmetic operations
   without string constant and without global variable */

int printf(char*, ...);
void printInt(int p);

int main()
{
  int a, b, c;
  int x;
  a = 1;
  b = a + 2;
  c = b + a * b; 
  printInt(c);
  if (c > 0)
    x = a + (b + c) * a;
  else
    x = 0;
  printInt(x+a);
  return 0;
} 
void printInt(int p)
{
  printf(" %d", p);
}

