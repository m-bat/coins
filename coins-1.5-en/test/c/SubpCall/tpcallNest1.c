/* tpcallNest1.c: Nested call */

int a, b, i, j, k;
char c;

int f1(int p)
{
  return p + 1;
}
int f2(int p, char q)
{
  return p + q;
}
int f3(int p, int q, int r)
{
  return p + q + r;
}
char fc(char p)
{
  return p;
}
int main()
{
  c = 'x';
  a = f1(1);
  b = f2(a, 2) * 16;
  i = f2(b, fc(c));
  j = f3(a + 1, f2(b + 1, fc(fc(c) + 5)), f1(3) + 2); 
  printf("a=%d, b=%d, c= %d, fc(c)= %d, i=%d, j=%d \n", a, b, (int)c, 
         (int)fc(c), i, j);
  return 0;
} 

