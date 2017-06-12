/* tpSizeofFunc.c  -- Test for sizeof function. */
/* sizeof cannot be applied to function type.
   It can be applied to object or basic type or struct/union/array type 
   K & R p.251.
*/

int a,   b,   c;
int x,   y,   z, z2, z3, z4;

int(*fp)(int, int);
int (*v[2])(int);
int f1(int p1) { return p1; }
int f2(int p1) { return p1+1; }
double fd(double p2) { return p2; }

struct foo {int x, y;} *f();
struct foo fs();

struct foo foo1[2] = { {2, 2}, {1, 1}};

int main()
{
  v[0] = f1;
  v[1] = f2;
  z = v[1](2);

  fp = (int(*)(int, int))10; 
  x = sizeof f1;  /* sizeof cannot be applied to function type 
                     but can be applied to function. */
  y = sizeof(fp);
  z = z + sizeof f; 
  z2= sizeof(fs);
  z3 = sizeof fd;
  z4 = sizeof foo1;

  printf("%d %d %d %d %d %d \n", x, y, z, z2, z3, z4);

  return 0;
}

struct foo *f()
{
  return &foo1[0];
}

struct foo fs()
{
  return foo1[1];
}

