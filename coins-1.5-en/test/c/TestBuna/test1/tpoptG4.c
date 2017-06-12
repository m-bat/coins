/* tpoptG4.c -- Common subexpression in compound stmt */
int printf(char *s, ...);

int i, j, k;
int a, b, c, d;
int x, y;

int main()
{
  a = 1;
  b = 2;
  c = a + y;
  if (a + x > 0)
    y = a + y;
  else
    x = a + x;
  
  printf("y = %d, x = %d\n", y, x);
}
 
