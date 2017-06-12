/* cse_test.c */

/* #include <stdio.h>
*/
int printf(char*, ...); /**/

int main ()
{
  int a;
  int b;
  int c;
  int d;
  int x;
  int y;
  int z;

  a = 1;
  b = 2;
  x = 3;

  y = a + b;
  c = x + y;

  if (a < 10)
    z = a + b; // a + b is a common subexpr, and is equal to y.
  else
    z = a + b; // a + b is a common subexpr, and is equal to y.
               // therefore, z is equal to y after the if statement.
  d = x + z;   // z is equal to y. thus, x + z is equal to x + y, or c.
               // as a result, d is equal to c.

  printf("%d\n",d); // d is equal to c
}
