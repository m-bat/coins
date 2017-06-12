/* tpifnest1.c:  Nested if-statement */

int main()
{
  int a, b, c, d;
  int i, j, k;

  a = 1;
  b = 2;
  if (a == 0)
    if (b == 0)
      c = a;
    else
      c = 1;
  else 
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
  printf("c=%d\n",c); /* SF030620 */
  return d;
} 

