/* jisCall1.c : JIS C 6.3.2.2 Call with func param  p.41 */
/*   This depends on the order of parameter evaluation,
     that is, the result may change by implementation.
*/

#include <stdio.h>

int val;

int f1() 
{ 
  val++;
  printf("f1 %d \n", val);
  return val;
}

int f2() 
{ 
  val++;
  printf("f2 %d \n", val);
  return val;
}

int f3() 
{ 
  val++;
  printf("f3 %d \n", val);
  return val;
}

int f4() 
{ 
  val++;
  printf("f4 %d \n", val);
  return val;
}

int g1(int p1, int p2)
{
  printf("g1 %d \n", p1+p2);
  return p1 + p2;
}

int g2(int p1, int p2)
{
  printf("g2 %d \n", p1+p2);
  return p1 + p2;
}

int g3(int p1, int p2)
{
  printf("g3 %d \n", p1+p2);
  return p1 + p2;
}

int 
main()
{
  int gv;
  int (*(pf)[6])(int, int);
  val = 0;
  pf[0] = &g1;
  pf[1] = g2;
  pf[2] = g3;
  pf[3] = &g1;
  pf[4] = g2;
  pf[5] = g3;
  gv = (*pf[f1()])(f2(), f3() + f4());
  printf("gv %d \n", gv);
  val = 1;
  gv = (*pf[f1()])(f2(), f3() + f4());
  printf("gv %d \n", gv);
  return 0;
}

