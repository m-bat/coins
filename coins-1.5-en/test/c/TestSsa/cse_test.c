#include<stdio.h>
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
    z = a + b;
  else 
    z = a + b;

  d = x + z;

  printf("%d\n",d);
  return d;
}
