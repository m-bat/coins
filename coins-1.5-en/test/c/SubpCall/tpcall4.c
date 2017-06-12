/* tpcall4.c:  array parameter. */
int a[10];

int fn(int p[10])
{
  p[1] = p[0]+1;
  return p[1];
}
int main()
{
  a[0] = 3; /* SF030609 */
  a[3] = fn(a);
  printf("a[0],a[3] = %d,%d\n",a[0],a[3]); /* SF030609 */
  return 0;
}

