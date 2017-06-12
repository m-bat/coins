/* Decl/tpincrdecr2-1.c: Increment, decrement (Error) */

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
  ++k++;  /* Invalid lvalue */
  ++pa++;
  (*++pa)++;
  (*++ppa)++;
  pa++++;
  (*ppa++)++;
  return 0;
  printf("This test will cause 'Invalid lvalue' error."); /* SF030609 */
}
