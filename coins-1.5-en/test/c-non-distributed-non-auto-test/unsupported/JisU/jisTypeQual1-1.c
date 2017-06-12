/* jisTypeQual1.c : JIS C 6.5.3 Type qualifier p.66 */

#include <stdio.h>

int 
main()
{
  const struct s {int mem; } cs = {1};
  struct s ncs; /* ncs can be modified */
  typedef int A[2][3];
  const A a = {{4,5,6},{7,8,9}}; /* array of array of const int */
  int *pi;
  const int *pci;
  
  ncs = cs;        /* OK */
  cs  = ncs;       /* const struct can not be modified */
  printf("ncs=%d cs=%d\n",ncs.mem,cs.mem); /* SF030620 */
  pi  = &ncs.mem;  /* OK */
  printf("*pi=%d\n",*pi);
  pi  = &cs.mem;   /* Illegal. pi is not const. */
  pci = &cs.mem;   /* OK */
  printf("*pi=%d *pci=%d\n",*pi,*pci);
  pi  = a[0];      /* Illegal. a[0] is const int *. */
  printf("pi=[%d,%d,%d]\n",pi[0],pi[1],pi[2]);
  return 0;
}

