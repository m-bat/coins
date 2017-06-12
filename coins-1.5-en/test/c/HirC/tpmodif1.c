/* tpmodif1.c: Increment, decrement and modifier operation */
int x, y, z;
int a[10];
struct Rec {
  int e1;
  int e2;
} s1[10];
int *pa;
struct Rec *ps;

int main()
{
  int i, j, k;

  i = 0;
  /* SF040720[
  j = i++;
  k = --j; */
  printf("i=%d\n",i);
  j = ++i;
  printf("i=%d j=%d\n",i,j);
  k = j--;
  printf("i=%d j=%d k=%d\n",i,j,k);
  /* SF040720] */
  x = a[k++];
  y = s1[k-- + i].e2;
  printf("x=%d y=%d ",x,y); /* SF030620 */
  pa = &a[0];
  ps = &s1[0];
  pa++;
  ps++;
  ps = ps + 2;
  pa = pa - 1;
  z = ps->e1; /* missed ';' */ /* SF030620 */
  printf("z=%d\n",z); /* SF030620 */
  printf("do those prints fit??\n");
  return 0;
}
