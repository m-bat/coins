/* tpptrError1.c NullPtr in code const
*/
#include <stdio.h> /* SF030620 */

int a[1];
int main() {
  /* SF030620[ */
  printf(" *(*&a+0)=%d \n", *(*&a+0) );
  /* SF030620] */
  return *(*&a+0);
}

