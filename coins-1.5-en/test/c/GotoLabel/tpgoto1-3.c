/* tpgoto3.c:  goto statement test */

int main()
{
  int a; 
  int b; 
  int c; 
  int d; 
  int e; 
  a = 1;
  b = 2;
  a = 11;
  b = 22;
  goto L1;
L1:
  c = 3;
  d = 4;

  printf("a=%d b=%d c=%d d=%d\n",a,b,c,d); /* SF030620 */
  return(0);
}
