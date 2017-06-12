/* tpfuncptr7.c  Function pointer Hasegawa mail 031104 */

#include <stdio.h>

int h()
{
  printf(" bbb");
  return 0;
}
int (*g())()
{
  printf(" aaa");
  return &h;
}

int main()
{
  int (*(*f)(void))(void);
  int (*(*f2)(void))(void);
  f = &g;
  (*(*f)())();
  f2 = f;
  (*(*f2)())();
  return 0;
}
