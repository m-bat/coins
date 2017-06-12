/* tpcontinue1.c:  continue test (basic) */

int i, a, b, c;
int main()
{
  i = 1;
  a = 0;
  b = 1;
  while (i < 10) {
    a = a + b;
    if (a >= 10) {
      i = i + 1;
      continue;
    }
    i = i + 1;
  }
  c = a + b;
  printf("a=%d b=%d c=%d i=%d\n",a,b,c,i); /* SF030620 */
  return 0;
} 

