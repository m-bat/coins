/* tpAddr2.c -- address of arrays and array  parameters */

  double *p0;
  double **p2;
  double (*p3)[10];
  double d0;
  double aa[10];
  int    i;

void readdata(double a[])
{
  p0 = &a[2];
  d0 = a[2];
  /*scanf("%1g", &a[1]); /* SF030620 */
  scanf("%lg", &a[1]); /* SF030620 */
  p0 = aa;
  p0 = a;
  p3 = &aa;
  p2 = &a;
  p2 = &aa; /* assignment from incompatible pointer type */
  p0 = &aa; /* assignment from incompatible pointer type */
  p0 = &a;  /* assignment from incompatible pointer type */
  p3 = &a;  /* assignment from incompatible pointer type */
  /*d0 = p0[i]; /* SF030620 Segmentation Fault */
}

void readdata2(double *pa)
{
  p0 = &pa[2];
  d0 = pa[2];
  /*scanf("%1g", &pa[1]); /* SF030620 */
  scanf("%lg", &pa[1]); /* SF030620 */
  p0 = (pa+2);
  p0 = &(*(pa+2));  /* *(pa+2) is l-value */
  d0 = *(pa+2);
  *p0 = 1.0;
}

main()
{
  double a[10];
  double *p1;
  a[0]=0.12; /* SF030620 */
  a[1]=1.23; /* SF030620 */
  a[2]=2.34; /* SF030620 */
  i  = 1;
  p1 = a;
  d0 = p1[i];
  printf("d0=%f *p1=%f\n",d0,*p1); /* SF030620 */
  d0 = a[i];
  p1 = aa;
  printf("d0=%f *p1=%f\n",d0,*p1); /* SF030620 */
  d0 = aa[i];
  p1 = &a[2];
  printf("d0=%f *p1=%f\n",d0,*p1); /* SF030620 */
  p1 = &d0;
  d0 = *p1;
  printf("d0=%f *p1=%f\n",d0,*p1); /* SF030620 */
  p1 = &*p1;
  printf("*p1=%f\n",*p1); /* SF030620 */
  readdata(a);
  printf("a = {%f,%f,%f,...}\n",a[0],a[1],a[2]); /* SF030620 */
  readdata2(a); /* SF030620 */
  printf("a = {%f,%f,%f,...}\n",a[0],a[1],a[2]); /* SF030620 */
}

