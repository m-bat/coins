/* tpassert2.c   */

#include <assert.h>

int i = 1, j = 2;

int main()
{
  double r[10] = {1.0, 2.0 };
  assert(i - r[i]*r[i] < 1e-6);
  printf("assert() is TRUE\n");
  return 0;
}

