/* tpconst6.c:  CharConst, IntConst, StringConst (Exp) */

int printf(char*, ...);

int main()
{
  int  i, j, k, l;
  char c1, c2;
  char  *s1, *s2;
  c1 = '9';
  c2 = 'A';
  i  = 9;
  j  = 'A';
  k  = 123;
  s1 = "123";
  printf("%d %c %d %c %d %s \n", i, c1, j, c2, k, s1);
  return 0;
}

