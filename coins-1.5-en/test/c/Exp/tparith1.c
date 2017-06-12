/* tparith1.c  -- Test simple arithmetic exp  */

int i, j;
int  a[10];
char c, d;
extern void f(){};

main()
{
  int x;
  i=j=1; /* SF030620 */   /* initialize i != 0 */

  f();
  i = i + j * 10;
  j = a[i-1]/i + c;
  /* i = -1; */
  i = +1;
  j = -i + a[-1+i];
  /* SF030620[ */
  printf("extern void f(): with empty block is called by main()\n");
  /* printf("-1+i=%d a[-2]=%d\n",-1+i,a[-2]); */ /* SF030620 */
  printf("-1+i=%d a[0]=%d\n",-1+i,a[0]); 
  printf("i=%d j=%d\n",i,j); /* SF030620 */
  /* SF030620] */
  return 0;
}

