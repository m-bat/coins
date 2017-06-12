/* tpfuncptr8.c  Function pointer assignment */

int printf(char*, ...);

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
  int (*fp)();
  f = &g;
  (*(*f)())();
  fp = h;
  /* *fp = h; */  /* invalid lvalue */
  fp();
  return 0;
}
