/* tpparenthesis.c  -- Test parenthesis  */

#include <stdio.h>

int  a[10], b[10];
char c, d;
int (*i1);

/* int (*f1)() */
int *f1()
{
 return &a[0];
}

main()
{
  int* x;
  /*printf("&a %x \n", &a); /* SF030620 */
  i1 = f1();
  /* SF030620[
  printf("f1 %x \n", i1); */
  if( a==i1 )
    printf("OK\n");
  printf("parenthesis test.\n");
  /* SF030620] */
  return 0;
}

