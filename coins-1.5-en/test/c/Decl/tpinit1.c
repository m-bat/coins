/* tpinit1.c  Initial value (scalar, global and local) (Decl) */
int a, b = 1, c;
int d, e = 2;
main()
{
  int i = 0;
  float x = 1.0;
   
  a = i + e;
  b = x;
  printf("a=%d b=%d c=%d d=%d e=%d i=%d x=%f\n",a,b,c,d,e,i,x); /* SF030620 */
  return a;
}

