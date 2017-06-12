/* tpincrdecr2.c: Increment, decrement operations and their combination (Error) */

int x, y, z;
int a[10];
int *pa;
int **ppa;

int main()
{
  int i, j, k;
  i = 0;
  j = i++;
  pa = a;
  ppa = &pa;
  *pa = 1;
  k = **ppa;
  **ppa = 2;
  ++pa;
  ++k++;
  ++pa++;
  (*++pa)++;
  (*++ppa)++;
  pa++++;
  (*ppa++)++;
  return 0;
  printf("This test will cause 'invalid lvalue' error.\n"); /* SF030609 */
}
