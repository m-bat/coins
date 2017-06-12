/* tpoptG4.c -- Common subexpression in compound stmt */

#include <stdio.h> /* SF030620 */

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
  /* SF030620[ */
  printf("a=%d b=%d c=%d d=%d i=%d j=%d k=%d x=%d y=%d",
	 a,b,c,d,i,j,k,x,y);
  /* SF030620] */
}
 
