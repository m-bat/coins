/* loopSum2.c: Parallel (summation with "if", fixed range. by Iwasawa */

  int   printf(char*, ...);
  void  getData2(float x[100]);
  float s, q, a[100];

main( ) {
  int i;
  getData2(a); 
  q=0; /* SF030620 */
  for (i=0 ; i<100 ; i++) {
     s=a[i]+1;
     if (s>0) a[i]=a[i]*s;
     q=q+a[i];
  }
  printf("q=%f\n",q);     
}

/* SF030620[ */
void getData2(float x[100])
{
  int i;
  for(i=0 ; i<100 ; i++) x[i]=1.0;
}
/* SF030620] */
