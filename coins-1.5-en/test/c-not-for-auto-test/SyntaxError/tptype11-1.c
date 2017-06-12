/* tptype11-1.c:  type check  */

int a[10];

int f1(int p1[10]);
int f2(int p2[]);

int f1(int p1[9])
{
  return p1[1];
}

int f2(int p2[5])
{
  return p2[1];
}

int main()
{
  int x1, x2;
  char  c1[5];
  a[0] = 1;
  a[1] = 2;
  c1[1] = 'a';
  x1 = f1(a);
  x2 = f2(c1);
  printf("%d %d \n", x1, x2);
  return 0;
}
