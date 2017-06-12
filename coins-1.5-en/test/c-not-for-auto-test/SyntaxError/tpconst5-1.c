/* tpconst5-1.c:  Constants (Exp) */

int printf(char*, ...);

int f();
int main()
{
  int i;
  char c;
  short int si;
  unsigned int  ui;
  unsigned char uc;
  unsigned short us;
  i = 1;
  c = 'a';
  si = 32767;
  ui = si + f() + 32767u;
  uc = 'a'U;  /* syntax error */
  us = 65535; 

  printf("%d %d %d %d %d %d \n", i, c, si, ui, uc, us);
  return 0;
}

int f()
{
  return 0;
}

