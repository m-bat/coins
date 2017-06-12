/* tpifnest2.c:  Nested if-statement with empty else part */

int printf(char*, ...);

int main()
{
  int a, b, c, d;
  int i, j, k;

  a = 1;
  b = 2;
  if (a == 0) {
    if (b == 0)
      c = a;
  }else 
    if (b > 0)
      c = a + 2;
  if (a == 0) {
    if (b == 0) {
      c = a;
      a = 3;
    }else {
      c = 1;
      b = 4;
    }
    d = a + b + c;
  }else {
    if (b > 0)
      c = a + 2;
    d = 5;
  }
  c = d + 1 - d - 1; 
  printf("a=%d b=%d c=%d d=%d\n",a, b, c, d); /* SF030620 */
  return 0;
} 

