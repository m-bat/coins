/* tpconv2.c:  exp type conversion 2 (Conv) */

int main()
{
  signed char a, b;
  int c;
  a = -11;
  b = 23;
  c = a - b;
  printf("a=%d b=%d c=%d \n",a,b,c); /* SF030620 */
  return 0;
} 

