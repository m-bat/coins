/* tpolddecl1.c -- Old declaration form */

char f(v,x)
  char  v;
  short x;
{
  short s = 99;  /*##*/
  if(--x)
    s = f(v*2/2+1,x); 
  return(s);
}

int foo(char p)
{
  return ++p;
}

int main()
{
  char c, d;
  short s, t;
  int a;
  c = 'A';
  t = 2;
  d = 'X';
  s = f(c, t);
  a = foo(d);
  printf("%d %d \n", s, a);
  return  0;
}

