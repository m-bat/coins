/* tpConv1.c:  Conditional exp conversion 1 (Conv) */

int main()
{
  int a, b;
  char c;
  float f;
  double d;

  a = 1;
  b = 2;
  c = 'x';
  f = 1.0;
  d = 1.0;
  if (a)
    c = a;
  else if (c) 
    c = a + 2;
  if (f - 1.0F)
    f = 2.0;
  if (d - 1.0)
    d = 2.0;
  printf("a=%d b=%d c=%d f=%f d=%f \n",a,b,c,f,d); /* SF030620 */
  return c;
} 

