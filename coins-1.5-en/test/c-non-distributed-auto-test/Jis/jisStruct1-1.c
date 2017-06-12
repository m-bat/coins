/* jisStruct1-1.c : Simplification of jisStruct1.c  */

/*
#include <stdio.h>
#include <math.h>
*/
int printf(char *, ...);

struct pair { int x; int y; };

union {
  struct {
    int alltypes;
  } n;
  struct {
    int type;
    int intnode;
  } ni;
  struct {
    int type;
    double doublenode;
  } nf;
} u;

struct pair f(int px, int py)
{
  struct pair s;
  s.x = px;
  s.y = py;
  return s;
}

int 
main()
{
  struct pair vs;
  double vd;
  int    vi;

  vs = f(3, 5);
  printf("struct f %d %d \n", vs.x, vs.y);
  printf("struct f().x f().y %d %d \n", f(1,2).x, f(1,2).y);

  u.nf.type = 1;
  u.nf.doublenode = 3.14;
  if (u.n.alltypes == 1)
    /* vd = sin(u.nf.doublenode); */
    vd = 1.4142/2.0; 
  printf("sin %e \n", vd);
  u.ni.type = 0;
  u.ni.intnode = 99;
  if (u.n.alltypes == 0)
    vi = u.ni.intnode;
  printf("int %d \n", vi);
  return 0;
}

