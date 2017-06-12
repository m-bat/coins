/* tpoptB3.c: Optimization for if-stmt and switch-stmt */

#include <stdio.h> /* SF030620 */

int x, y, z;

int func1(int p, int q)
{
  int i, j, a, b;

  a = p + q;
  {  }       /* null block */
  i = a;
  j = a * 3;
  b = a + 1;
  switch (p) {
  case 0: 
  case 1: 
  case 2: 
    a = 10 + a * 3;
    if (p == 2)
      break;
  case 3: 
    b = 11;
    a = 20 + a * 3;
    switch (q) {
    case 1: 
      break;
    case 2: 
      a = p + q;
    case 3: 
    case 4: ;
    case 5: ;
    };
  default:
    a = 40;
  };
  printf("func1: return %d\n",a); /* SF030620 */
  return a;
}
 
int main()
{
  int d, e, f;

  d = 1;
  e = d + 1;
  x = d;
  y = d + 1;
  printf("d=%d e=%d      x=%d y=%d z=%d\n",d,e,x,y,z); /* SF030620 */
  f = func1(d, e);
  z = d + 1 + f;
  printf("d=%d e=%d f=%d x=%d y=%d z=%d\n",d,e,f,x,y,z); /* SF030620 */
  return z;
}
  
