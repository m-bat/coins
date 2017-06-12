/* tpinitfunc1.c  Initial value by function call  (Decl) */

int a[2] = {1, 2};
int f(int p1) { return p1 + a[0]; }

int main()
{
  int x = f(2);
  int y = a[1]*2;
  printf("%d %d \n", x, y);
  return 0;
}

