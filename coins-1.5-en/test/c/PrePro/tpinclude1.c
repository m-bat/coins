
/* tpinclude1.c:  include test. This is 2nd line. 1st line is blank. */

/** Multi-line comment
***
***
**/

#include "tpincludeheader1.h"
int /* embedded com */ i /**/, j/***/, k;


   /* Multiple blank lines */
int main()  /* Trailing comment */
{           /* The next line contains 2 blanks. */

  i = j = 0;
  k = i + j + 2; 
  k = a + k - a;
  printf("Pre-processor test.\n"); /* SF030609 */
  return k;
}
/* Comment before EOF */




