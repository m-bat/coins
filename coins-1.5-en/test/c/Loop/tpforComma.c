/* tpforComma.c -- Comma expression in loop control */
 
int 
main()
{
  int i, j, s;
  int a[20], b[20];
 
  a[0]=b[0]= (0, 1);
  for (i = 0, j = 0; i < 10; i++, j++) {
    a[i+1] = a[i]+1;
    b[j+1] = a[i]+b[j];
  }
  s = 0;
  i = j,11;
  for (; i<20; i++) {
    s = s + a[i-10];
  }
  /* SF030620[ */
  printf("i=%d j=%d s=%d\n",i,j,s);
  printf("a=[%d %d %d %d %d %d %d %d %d %d %d]\n",
	 a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9],a[10]);
  printf("b=[%d %d %d %d %d %d %d %d %d %d %d]\n",
	 b[0],b[1],b[2],b[3],b[4],b[5],b[6],b[7],b[8],b[9],b[10]);
  /* SF030620] */
  return 0;
}
