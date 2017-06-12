/* tpPtrLoop1.c -- Loop by using array pointer */
 
int 
main()
{
  int i, j, s;
  int a[20], b[20];
  int *pa, *pb;
 
  a[0]=b[0]= 1;
  pa = a;
  pb = b;
  for (i = 0, j = 0; i < 10; i++, j++) {
    pa[i+1] = pa[i]+1;
    pb[j+1] = pb[i]+pb[j];
  }
  /* SF030620[ */
  for (i = 0, j = 0; i <= 10; i++, j++) 
    printf("pa[%d]=%d pb[%d]=%d\n",i,pa[i],i,pb[i]);
  /* SF030620] */
  return 0;
}
