/* simdLoop1.c by Fujinami */

/** static int sa,da;
    static int sr,dr;
    static int sg,dg;
    static int sb,db;
    static int k;
**/

  int sa,da;  
  int sr,dr;  
  int sg,dg;  
  int sb,db;  
  int k;          

/** static void
    hLineRight(unsigned char *p,int n,int a,int ea,
               int r,int er,int g,int eg,int b,int eb) {
**/
void
hLineRight(unsigned char ca[],int n,
	   int a,int ea,int r,int er,int g,int eg,int b,int eb) {
  int i;
  i = 0;

  /* SF030620[ */
  sa=10,da=10;  
  sr=20,dr=20;  
  sg=30,dg=30;  
  sb=40,db=40;  
  k=1;          
  /* SF030620] */

  while(n!=0) {
    /** *p++=a; *p++=r; *p++=g; *p++=b;
    **/
    ca[i  ] = a;
    ca[i+1] = r;
    ca[i+2] = g;
    ca[i+3] = b;

    i = i + 4; 
    /** a+=sa; r+=sr; g+=sg; b+=sb;
    **/
    a = a + sa;
    r = r + sr;
    g = g + sg;
    b = b + sb;
    /** if((ea+=da)>=0) { a++; ea-=k; }
        if((er+=dr)>=0) { r++; er-=k; }
        if((eg+=dg)>=0) { g++; eg-=k; }
        if((eb+=db)>=0) { b++; eb-=k; }
    **/
    ea = ea + da;
    if (ea >= 0) {
      a = a + 1;
      ea = ea - k;
    }
    er = er + dr;
    if (er >= 0) {
      r = r + 1;
      er = er - k;
    }
    eg = eg + dg;
    if (eg >= 0) {
      g = g + 1;
      eg = eg - k;
    }
    eb = eb + db;
    if (eb >= 0) {
      b = b + 1;
      eb = eb - k;
    }
    /** --n
    **/
    n = n - 1;
  }
}

/* SF030620[ */
main()
{
  unsigned char ca[12];

  hLineRight(ca,3,1,1,2,2,3,3,4,4);
  printf("ca=[%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d]\n",
	 ca[0],ca[1],ca[2],ca[3],
	 ca[4],ca[5],ca[6],ca[7],
	 ca[8],ca[9],ca[10],ca[11]);
  return 0;
}
/* SF030620] */
