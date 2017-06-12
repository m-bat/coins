/* tpincrdecr2.c: Increment, decrement operations and their combination */
/*   Modified 060802 */

int x, y, z;
int a[10];
int *pa;
int **ppa;
int printf(char*, ...);

int main()
{
  int i, j, k;
  i = 0;
  j = i++;
  pa = a;
  ppa = &pa;
  *pa = 1;       /* a[0] = 1 */
  k = **ppa;     /* k = a[0] */
  **ppa = 2;     /* a[0] = 2 */
  ++pa;          /* pa = &a[1] */
  /** ++k++; */  /* invalid lvalue in increment */
  (*++pa)++;     /* a[2]++ */
  pa[1] = 4;     /* a[3] = 4 */
  (*++pa)++;     /* a[3]++ */
  (*pa++)++;     /* a[3]++ */
  **ppa = 7;     /* a[4] = 7; */
  printf("%d %d %d %d %d a = %d %d %d %d %d\n",
         i, j, *pa, **ppa, k, a[0], a[1], a[2], a[3], a[4]);
  return 0;
}


