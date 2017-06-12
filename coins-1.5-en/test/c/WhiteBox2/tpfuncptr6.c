/* tpfuncptr6.c  Function pointer Hasegawa mail 030813 */

#include <stdio.h>

int h()
{
  printf("bbb");
  return 0;
}
int (*g())()
{
  printf("aaa");
  return &h;
}

int main()
{
  int (*(*f)(void))(void);
  f = &g;
  (*(*f)())();
  return 0;
}
