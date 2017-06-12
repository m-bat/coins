/* looppara2.c Loop parallelization */

int main()
{
  int i,j,k,n;
  float s,q,a[100],b[100];

  scanf("%d",&n);
  j=1;
  s = 10.0;
  for (i=0 ; i<n ; i++) {
   q=b[j]+a[i];
   a[i]=(b[j-1]+b[j+1])/2;
   j=j+1;
   s=q+s+j;
  }
  j=1;
  s = 10.0;
  for (i=0 ; i<n ; i++) {
    a[i]=(b[j-1]+s)/2; /**/
    j=j+1;
    s=a[i-1]+s+j;
  }

  return 0;
}
