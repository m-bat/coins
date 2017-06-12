/* tpassign1.c:  Basic assignment with arithmetic operations */
int main()
{
  int a, b, c;
  int x;

  a = 1;
  b = a + 2;
  /*c = b + a * c; /* SF030620 */
  c = b + a; /* SF030620 */
  x = a + (b + c) * a;
  printf("a=%d b=%d c=%d x=%d \n",a,b,c,x); /* SF030620 */
  return 0;
}
