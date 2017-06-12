/* tpconstFold1.c */

int printf(char*, ...);

int main ()
{
  int a, b, c, d, x, y, z;

  a = 1;
  b = 2;
  x = 3;

  y = a + 0;
  y = y * 1;
  y = y + 2 + 3;
  c = 1 + 2 + 3 + 4 + 5;
  c = c * 2 * 4;

  if (1 < 10)
    z = a + b;
  else
    z = a - b;

  printf("%d %d %d \n",c, y, z);
}
