/* tpptrarith2.c:  Pointer arithmetic and common subexp */

int printf(char*, ...);

char *s1, c1, *s2, *s3, c2;
int  i, x;
int  a[3] = {10, 20, 30}, b[3] = {100, 200, 300};
int  *pa[3] = {&a[0], b };
int *p1, *p2;
int f1(int p1);
int *f2(int pa1[10]);
int main()
{
  int i, j, k, y, z;
  int pb[2] = {3, 4};
  i = 0;
  j = 1;
  k = 2;
  c1 = 'a';
  s1 = "abc";
  s2 = s1 + i; 
  s3 = s1 + i; 
  c2 = s2[1];
  p1 = &a[i];
  p2 = p1 + i;
  /* SF030620[ */
  printf("c1=%c s1=%c s2=%d c2=%d  *p1=%d *p2=%d\n",
	 *s1, c1, *s2, c2,*p1, *p2);
  /* SF030620] */
  p1 = pa[i];
  p2 = pa[i];
  x = *p1 + *p1;
  y = x + *p2 + *p2+ *pa[i] + *pa[i];
  y = f1(x*x) + f1(x*x);
  y = x + *p2 + *p2+ *pa[i] + *pa[i];
  z = f1(y+y) + f1(y+y);
  y = z + y*2 + y*2 + *p2 + *p2+ *pa[i] + *pa[i];
  p1 = f2( pb);
  z  = y + z + *p1 + *p2 + f1(k+k) + f1(k+k);
  p2 = f2( pb);
  z  = y + z + *p1 + *p2 + f1(k+k) + f1(k+k);
  printf("x y z %d %d %d \n", x, y, z);
  return 0;
} 

int 
f1(int p1)
{
  return p1;
}
int *f2(int pa1[10])
{
  return pa1;
}


