/* tpifArray1.c: If-statement and subscripted var. */

main( )
{
  int i;
  int s, q, a[100];
  i=0;s=1,a[0]=2; /* SF030620 */

  if (s>0) a[i]=a[i]*s;
  printf("s=%d a[%d]=%d\n",s,i,a[i]); /* SF030620 */
}

