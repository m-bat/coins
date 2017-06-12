/* tptypedef4.c:  typedef test (boolean) */

#ifndef TRUE       /* Some compilers predefine TRUE, FALSE */
typedef enum {FALSE, TRUE} boolean;
#else
typedef int boolean;
#endif

boolean t1;

int main()
{
  boolean t2;
  t1 = TRUE;
  t2 = FALSE;

  printf("t1,t2 = %d,%d\n", t1, t2); /* SF030509 */

  return 0;
}

