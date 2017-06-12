/* appel_19_4.c */
/* Appel, A.: Modern compiler implementation in Java, 2nd ed., 2002
   Fig. 19.4 */

#include <stdio.h>

int main ()
{
  int i;
  int j;
  int k;

  i = 1;
  j = 1;
  k = 0;

  while (k < 100) {
    if (j < 20) {
      j = i;
      k = k + 1;
    } else {
      j = k;
      k = k + 2;
    }
  }

  printf("%d\n",j);
}
