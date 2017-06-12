/* tpassert1.c   */

#include <assert.h>

int i = 1, j = 2;

int main()
{
  assert(i < j);
  printf("assert() is TRUE\n");
  return 0;
}

