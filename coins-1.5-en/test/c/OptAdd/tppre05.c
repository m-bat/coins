/*---- tppre05.c: PRE for compound variables (structure elements). */ 

/*
#include <stdio.h> 
*/

struct point { int xx; int yy; } p1, p2;
int d0=0, d1=1, d2=2, d3=3;

int main()
{
  int a, b, c, d, e, f;
  int x, y, z, x1, y1, z1, z2;
  p1.xx = d0;
  p1.yy = d1;
  a = d0;
  if (a != 0) {
    x = p1.xx;
  }else {
    x = p1.yy + a; 
  } 
  y = p1.xx;
  z = p1.yy + a;
  printf("%d %d %d \n", x, y, z); 
  return 0;
}
