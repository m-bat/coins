/* loopPara1.c: loop parallelization test (by Iwasawa) */

int 
main()
{
  int i,j,k,n;
  float s,q,a[100],b[100];

/*  scanf("%d",&n);
*/
  n = 100; /* */
  j=1;
  s = 10.0;
  for (i=0; i<n ; i++) {
    q=b[j]+a[i];
    a[i]=(b[j-1]+b[j+1])/2;
    j=j+1;
    s=q+s+j;
  }
}

