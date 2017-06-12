/* tpifArray3.c: If-statement and subscripted var. */

main( )
{
  int i;
  int s, q, a[100];
  i=0;s=1,a[0]=2,q=3; /* SF030620 */

  s=a[i]+1;
  if (s>0) a[i]=a[i]*s;
  q=q+a[i];
  printf("s=%d a[%d]=%d q=%d\n",s,i,a[i],q); /* SF030620 */
}

