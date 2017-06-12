/* tpSizeofFunc.c  -- Test for sizeof function. */

int a,   b,   c;
int x,   y,   z;

int(*fp)(int, int);
int (*v[2])(int);
int f1(int p1) { return p1; }
int f2(int p1) { return p1+1; }

struct foo {int x, y;} *f();

struct foo foo1[2] = { {2, 2}, {1, 1}};

int main()
{
  v[0] = f1;
  v[1] = f2;
  z = v[1](2);

  fp = (int(*)(int, int))10;  /* OK */
  x = sizeof f1;
  y = sizeof(fp);
  z = z + sizeof f;   /** OK   incl _z */

  printf("%d %d %d \n", x, y, z);

  return 0;
}

struct foo *f()
{
  return &foo1[0];
}

