/* jisEnum1.c : JIS C 6.5.2.2 Enumeration p.62 */

#include <stdio.h>

int 
main()
{
  enum hue { chartreuse, burgundy, claret=20, windark };
  enum hue col, *cp;
  col = claret;
  cp = &col;
  if (*cp != burgundy)
    *cp = windark;
  printf("hue elem %d %d %d %d  *cp %d \n", 
         chartreuse, burgundy, claret, windark, *cp);
  return 0;
}

