/* tpoldFunc1.c Old form function */ 
/* Spec2000/gap/system.c */

#include <string.h>
#include <ctype.h>
#include <stdlib.h>

long SyStrcmp ( str1, str2 )
    char *              str1;
    char *              str2;
{
    return strcmp( str1, str2 );
}

char * SyStrncat ( dst, src, len )
    char *              dst;
    char *              src;
    long                len;
{
    return strncat( dst, src, len );
}

void InitSystem ( argc, argv )
    int    argc;
    char * argv [];
{
  while ( argc > 1 && argv[1][0] == '-' ) {
    ++argv;
    --argc;
    break;
  }
}

/*
extern char * calloc(size_t, size_t);
  conflicting types for calloc 
*/
  
main()
{
  char s1[10], s2[10], *s3;
  int  i1, i2;
  strcpy(s1, "abc");
  strcpy(s2, "xyz");
  s3 = "pqrs";
  i1 = SyStrcmp(s1, "abcd");
  printf("%d %s %s \n", i1, s2, s3);
  s3 = SyStrncat(s2, s1, 2);
  printf("%d %s %s \n", i1, s2, s3);
}
