/* tpif1.c:  if-statement (simple one) */

int main()
{
  int a, b, c;

  a = 1;
  b = 2;
  if (a == 0)
    c = a;
  else 
    c = a + 2;
  printf("c=%d\n",c); /* SF030620 */
  return c;
} 

