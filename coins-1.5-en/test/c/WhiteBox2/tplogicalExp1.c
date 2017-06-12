/* tplogicalExp1.c */

int printf(char*, ...);

int main()
{
  int a, b, c;
  int x = 0, y = 0;
  a = 1;
  b = 2;
L1: ;
  if (!(a == 1))
    goto L1;
  c = b == 2;
  if (!c)
    x = 1;
  if (!((a > 0)&&(b > 1)))
    y = 1; 
  printf("%d %d %d\n", c, x, y);
  return 0;
}
