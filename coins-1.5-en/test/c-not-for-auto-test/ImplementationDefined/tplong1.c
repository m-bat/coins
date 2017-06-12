/* tplong1.c  long and unsigned long  (Decl) */

int printf(char*, ...);

int main()
{
  long          l1, l2, l3;
  unsigned long ul1, ul2, ul3;
  l1  = 1;
  l2  = l1<<31; 
  l3  = l1<<32; 
  ul1 = 1;
  ul2 = ul1<<31; 
  ul3 = ul1<<32; 
  printf("%ld %ld %u %u \n", l2, l3, ul2, ul3);
  return 0;
}

