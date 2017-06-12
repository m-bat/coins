/* tpforIf1.c: For-statement including if-statement. */

main( )
{
  int i;
  int s, q, a[100];

  /* SF030620[ */
  for(i=0 ; i<100 ; i++) a[i]=i;
  q=1;
  /* SF030620] */

  for (i=0 ; i<100 ; i++) {
     s=a[i]+1;
     if (s>0) a[i]=a[i]*s;
     q=q+a[i];
  }
  printf("s=%d q=%d a[99]=%d\n",s,q,a[99]); /* SF030620 */
}

