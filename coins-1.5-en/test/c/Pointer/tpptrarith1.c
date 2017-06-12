/* tpptrarith1.c:  Pointer arithmetic * */

char *s1, c1, *s2, c2;
int  i, x;
int  a[10];
int *p1, *p2;
int main()
{
  c1 = 'a';
  s1 = "abc";
  s2 = s1 + 1; 
  c2 = s2[1];
  i  = 1;
  p1 = &a[i];
  p2 = p1 + i;
  /* p2 = p1*2; */ /* Error */
  /* p2 = p1/2; */ /* Error */
  /* x = p2 - p1; */ /* Cparser error */
  /* SF030620[ */
  printf("c1=%c s1=%c s2=%d c2=%d  *p1=%d *p2=%d\n",
	 *s1, c1, *s2, c2,*p1, *p2);
  /* SF030620] */
  return 1;
} 

