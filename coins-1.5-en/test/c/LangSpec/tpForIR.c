/* tpForIR.c  -- Test for IR representation. */

int a,   b,   c;
int x,   y,   z;
int i,   j,   k;
int *pa, *pb, *pc;
int t1,  t2,  t3;
int aa[10], bb[20], cc[30];
int (*p1)[10];
int (*p2)[2][3];
int ab[2][3];

int(*fp)(int, int);
int (*v[2])(int);
int f1(int p1) { return p1; }
int f2(int p1) { return p1+1; }

struct foo {int x, y;} *f();

struct foo foo1[2] = { {2, 2}, {1, 1}};

int main()
{
  a = 0;
  b = 1;
  i = j = k = 1;
  if (a < b) a + 1; else b - 1; /* OK. No object for a+1 & b-1 */
  /** c = (if (a < b) a); -- Parse error */
  /** c = {if (a < b) a; else b;}; -- Parse error */


  /** ((struct {int x; int y;})0x10).x = 1; 
      -- conversion to non scalar type requested */

  if (a < 0)
    ((int*)16)[1] = 2;   /* OK.  movl $2,20 */
  /** (int*)10[1] = 2; -- subscripted value is neither array nor pointer */
  printf("%d %d %d %d %d %d \n", a, b, c, i, j, k);

  p1 = &aa;
  (*p1)[0] = 3;
  a = (*p1)[0];
  p2 = &ab;
  (*p2)[1][2] = (*p2)[j][k];

  printf("%d %d %d %d %d \n", a, aa[0], aa[1], aa[3], ab[1][2]);
  x = (pa = &aa[a], *pa = *pa + 1);
  y = (pa = &aa[a], t1 = *pa, *pa = *pa + 1, t1); 

  v[0] = f1;
  v[1] = f2;
  z = v[1](2);

  printf("%d %d %d \n", x, y, z);

  fp = (int(*)(int, int))10;  /* OK */
  if (a < 0)
    (*fp)(1, 2);  /* OK */
  /** (a)(1,2);  -- called object is not a function */
  /** 10(1,2);   -- called object is not a function */
  /** (int(*)(int, int))10(1, 2); -- called object is not a function */

  (f()+1)->x = 3;
  printf("%d %d \n", foo1[0].x, foo1[1].x);

  return 0;
}

struct foo *f()
{
  return &foo1[0];
}

