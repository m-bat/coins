/* tpoptG4-1.c -- Common subexpression in compound stmt */
/*                Local variables */

#include <stdio.h> /* SF030620 */

int main()
{
  int i, j, k;
  int a, b, c, d;
  int x, y;

  a = 1;
  b = 2;
  x = 8;  /*##*/
  y = 9;  /*##*/
  c = a + y;
  if (a + x > 0)
    y = a + y;
  else
    x = a + x;
  /* SF030620[ */
  printf("a=%d b=%d c=%d x=%d y=%d",
	 a,b,c,x,y);
  /* SF030620] */
}
 
