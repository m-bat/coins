/* tpcpp3.c -- C preprocessor */
#include <ctype.h>
int main() 
{
#if defined __OPTIMIZE__
  printf("Yes, __OPTIMIZE__ is defined \n");
#endif
  printf("return \n");
  return 0;
}

