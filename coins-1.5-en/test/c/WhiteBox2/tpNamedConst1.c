/* tpNamedConst1.c: Named constant */

int printf(char*, ...);

const int m = 7;

enum {no, yes } ;

int main()
{
  int x, y;
  x = m;
  y = yes;
  printf(" %d %d \n", x, y);
  return 0;
}

