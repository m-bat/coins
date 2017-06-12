/* tpasm2.c: ASM test */ 
/*   Kitamura mail 060519 */
int printf(char*, ...);

void vfunc(char *s, ...);
int ifunc(int);

int i;

int foo()
{
  i = (vfunc("rdtsc\n"), ifunc(i));
  return i;
}

int bar()
{
  i = (asm("rdtsc\n"), ifunc(i));	/* エラーになる */
  return i;
}

int ifunc(int i)
{
  return i;
}

#define readport(p) (asm("#param %eax,w%I32\n" \
                         "#clobber %eax\n" \
                         "inl (%1)\n", \
                         (p), val__) \
		     , val__)

int readXYZ0()
{
  return readport(0x10);
}

int main()
{
  int a;
  a = foo();
  printf(" %d ", a);
  return 0;
}

