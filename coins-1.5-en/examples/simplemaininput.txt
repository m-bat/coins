/* tpif2.c:  if-statement with block */

int a, b, c;
int x;
int main()
{
  a = 1;
  b = 2;
  if (a > b+1) {
    x = 0;
    c = a;
  }else {
    x = 1;
    a = b;
    c = a + 2;
  }
  printf("c=%d\n",c); /* SF030620 */
  return c;
} 

