/* tpconstFold5.c  -- constant expression (zero divide)  */

int printf(char*, ...);

int main()
{
  int a, b, c, d, e, f, x, y, z;
  a = 10/0;
  b = 100 % 0; 
  printf("%d %d \n", a, b);
  return 0;
}

