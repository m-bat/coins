/* tpptrinit1.c:  Pointer initiation * */
/*       Mori mail 031120 */

int printf(char*, ...);

int a[] = {1, 2, 3};
int *p = a+2;
int *q = &a[2]; /* compile error 4444 */

int main()
{
  int aa[] = {1, 2, 3};
  int *pp = aa+2;
  int *qq = &aa[2];
  printf("%d %d %d %d \n", *p, *q, *pp, *qq);
  return 0;
}
