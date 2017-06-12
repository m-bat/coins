/* jisTypeName1.c : JIS C 6.5.5 Type name pp.71-72 */

#include <stdio.h>

  typedef int integer;
  typedef int * intPtr;
  integer i;
  intPtr  ip;
  int ia[3] = {1, 2, 3};
  int *ipa[3];
  int (*iap)[3];
  int * 
  ipf() 
  { 
    printf("ipf\n");
    return ip; 
  }
  int 
  ifunc() 
  { 
    printf("ifunc\n");
    return 8; 
  }
  int 
  ifuncuv(unsigned int p1, ...) 
  {  
    printf("ifuncuv\n");
    return p1; 
  }

  int (*const ifuncuva[])(unsigned int, ...) = {ifuncuv};
  int sub(
    integer,
    intPtr,
    int *[3],
    int (*)[3],
    int *(),
    int (*)(void),
    int (*const[])(unsigned int, ...));

  int sub(
    integer pI,
    intPtr  pIp,
    int *pIpa[3],
    int (*pIap)[3],
    int *pIpf(),
    int (*pIfp)(void),
    int (*const pIfa[])(unsigned int, ...)) 
  {
    int x, y, z;
    printf("sub enter\n");
    x = pI + *pIp;
    pIpa[0] = &x;
    pIap    = &ia;
    y = *pIpf();
    y = y + pIfp();
    z = pIfa[0](9, i);
    printf("sub exit x %d y %d z %d\n", x, y, z);
    return x + y + z;
  }
  
/**
  typedef int *[3] intPtrArray;
  typedef int (*)[3] intArrayPtr;
  typedef int *() intPtrFunc;
  typedef int (*)(void) intFuncPtr;
  typedef int (*const[])(unsigned int, ...) intFuncPtrArray;
**/

int 
main()
{
  int i1;
  volatile int v;
  i = 1;
  ip = &i1;
  *ip = 2;
  v = sub(i, ip, ipa, iap, ipf, &ifunc, ifuncuva); 
  printf("v= %d\n", v);
  return v-v;
}

