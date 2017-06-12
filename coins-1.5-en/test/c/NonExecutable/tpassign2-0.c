/* Assign/tpassign2.c:  Basic assignment */

#include <stdio.h> /* SF030620 */

int aaaa,b,c;
int x,y;
int maaaain()
{
aaaa=1+  b;
b=aaaa+2;
c=b+aaaa*c;
x=aaaa+(b+c)*aaaa;
return x;
}

/* SF030620[ */
int main()
{
  maaaain();
  printf("aaaa=%d b=%d c=%d x=%d\n",aaaa,b,c,x);
  return 0;
}
/* SF030620] */
