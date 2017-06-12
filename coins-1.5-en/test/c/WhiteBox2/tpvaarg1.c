/* tpvaarg1.c:  variable number of arguments */
/*       Use gcc extension (Fukuda mail 030705) */

int printf(char*, ...);

#undef __GNUC__
#undef __USE_GNU
#undef __OPTIMIZE__
  
#include <stdarg.h>
/* /usr/local/lib/gcc-lib/sparc-sun-solaris2.9/3.3/include/stdargs.h */
  
int sum(int n,...) /* Compute the sum of n parameters */
{
  int s = 0;
  va_list ap;
  va_start(ap,n);
  while(n--) {
    s = s + va_arg(ap,int);
  }
  va_end(ap);
  return s;
}
  
main()
{
    printf("%d\n",sum(3,123,222,321)); /* Result is 666 */
}
