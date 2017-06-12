/* tpSmp1.c */
/*   Fukuoka mail 050128 */

int printf(char*, ...);

int i, j;

void f(int a)
{
 L1:
  i = 2;
 L2:
  j = 3;
}

int main()
{
  f(1);
  printf(" %d %d \n", i, j);
  return 0;
}
