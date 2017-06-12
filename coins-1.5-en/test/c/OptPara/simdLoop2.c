/* simdloop.c by Fujinami */

static int sa,da;
static int sr,dr;
static int sg,dg;
static int sb,db;
static int k;

static void
hLineRight(unsigned char *p,int n,int a,int ea,int r,int er,int g,int eg,int b,int eb) {

  /* SF030620[ */
  sa=10,da=10;
  sr=20,dr=20;
  sg=30,dg=30;
  sb=40,db=40;
  k=1;
  /* SF030620] */

  while(n!=0) {
    *p++=a; *p++=r; *p++=g; *p++=b;
    a+=sa; r+=sr; g+=sg; b+=sb;
    if((ea+=da)>=0) { a++; ea-=k; }
    if((er+=dr)>=0) { r++; er-=k; }
    if((eg+=dg)>=0) { g++; eg-=k; }
    if((eb+=db)>=0) { b++; eb-=k; }
    --n;
  }
}

/* SF030620[ */
main()
{
  unsigned char p[12];
  
  hLineRight(p,3,1,1,2,2,3,3,4,4);
  
  printf("p = [%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d]\n",
	 p[0],p[1],p[2],p[3],
	 p[4],p[5],p[6],p[7],
	 p[8],p[9],p[10],p[11]);
  return 0;
}
/* SF030620] */
