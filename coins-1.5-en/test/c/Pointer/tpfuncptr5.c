/* tpfuncptr5.c  Function with function parameter (Pointer) */

int  printf(char*, ...);
int  i;
int f0(int(*)(int));
int f(int(int));
int f1(int);

int  f1(int p1) 
{
  return p1;
}

int f(int (*fp)(int) ) 
{
  return fp(2);
}

int f0(int (*fp)(int) ) 
{
  return fp(2);
}

int
main()
{
  i    = f(f1);
  printf("%d %d \n", i, f0(f1));
  return 0;
}



