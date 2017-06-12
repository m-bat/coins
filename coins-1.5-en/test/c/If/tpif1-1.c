/* tpif1.c:  if-statement (simple one) */

int a, b, c;
int main()
{
  a = 1;
  b = 2;
  if (a == 0)
    c = a;
  else 
    c = a + 2;
  printf("c=%d\n",c); /* SF030620 */
  return c;
} 

