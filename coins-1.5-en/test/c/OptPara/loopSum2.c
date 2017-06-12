/* loopSum2.c: Parallel (summation with "if", fixed range. by Iwasawa */

  int   printf(char*, ...);
  void  getData(int x[100]);
  int s, q, a[100];

main( ) {
  int i;
  getData(a); 
  q = 0;
  for (i=0 ; i<100 ; i++) {
     s=a[i]+1;
     if (s>0) a[i]=a[i]*s;
     q=q+a[i];
  }
  printf("q=%d\n",q);     
}

/* SF030620[ */
void getData(int x[100])
{
  int i;
  for(i=0 ; i<100 ; i++) x[i]=1;
}
/* SF030620] */
