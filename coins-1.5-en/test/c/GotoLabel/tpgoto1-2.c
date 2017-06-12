/* tpgoto2.c:  goto statement test */

int main()
{
  int a; 
  int b; 
  int c; 
  int d; 
  a = 1;
  b = 2;
  goto L1;
L1:
  c = 3;
  d = 4;

  printf("a=%d b=%d c=%d d=%d\n",a,b,c,d); /* SF030620 */
  return 0;
}
