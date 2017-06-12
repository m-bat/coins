/* tpptrinit2.c:  Pointer initiation * */
/*       Mori mail 031120 */

int printf(char*, ...);

int a[] = {1, 2, 3};
int *p = a+1;   /* a + 1*elemSize */
int *q = &a[1]; /* a + 1*elemSize */

int main()
{
  int aa[] = {1, 2, 3};
  int *pp = aa+1;
  int *qq = &aa[1];
  printf("%d %d %d %d \n", *p, *q, *pp, *qq);
  return 0;
}
