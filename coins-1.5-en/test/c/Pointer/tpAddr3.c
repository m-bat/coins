/* tpAddr3.c -- pointer to array and addr operator */

  int *p0;
  int **p2;
  int (*p3)[10];
  int d0;
  int aa[10];
  int    i;

void readdata(int a[])
{
  p0 = &a[2];
  d0 = a[2];
  scanf("%1g", &a[1]);
  p0 = aa;
  p0 = a;
  p3 = &aa;
  p2 = &a;
  p2 = &aa; /* assignment from incompatible pointer type */
  p0 = &aa; /* assignment from incompatible pointer type */
  p0 = &a;  /* assignment from incompatible pointer type */
  p3 = &a;  /* assignment from incompatible pointer type */
  d0 = p0[i];
}

void readdata2(int *pa)
{
  p0 = &pa[2];
  d0 = pa[2];
  scanf("%1g", &pa[1]);
  p0 = (pa+2);
  p0 = &(*(pa+2));  /* *(pa+2) is l-value */
  d0 = *(pa+2);
  *p0 = 1;
}

main()
{
  int a[10];
  int *p1;
  a[0] = 10; /* SF030620 */
  a[1] = 11; /* SF030620 */
  a[2] = 12; /* SF030620 */
  i  = 1;
  p1 = a;
  d0 = p1[i];
  printf("d0=%d *p1=%d\n",d0,*p1); /* SF030620 */
  d0 = a[i];
  p1 = aa;
  printf("d0=%d *p1=%d\n",d0,*p1); /* SF030620 */
  d0 = aa[i];
  p1 = &a[2];
  printf("d0=%d *p1=%d\n",d0,*p1); /* SF030620 */
  p1 = &d0;
  d0 = *p1;
  printf("d0=%d *p1=%d\n",d0,*p1); /* SF030620 */
  p1 = &*p1;
  /* SF030620[
  readdata(a); */
  printf("*p1=%d\n",*p1);
  readdata(aa);
  printf("aa=[%d %d %d %d %d %d %d %d %d %d]\n",
	 aa[0],aa[1],aa[2],aa[3],aa[4],aa[5],aa[6],aa[7],aa[8],aa[9]);
  readdata2(aa);
  printf("aa=[%d %d %d %d %d %d %d %d %d %d]\n",
	 aa[0],aa[1],aa[2],aa[3],aa[4],aa[5],aa[6],aa[7],aa[8],aa[9]);
  /* SF030620] */
}
