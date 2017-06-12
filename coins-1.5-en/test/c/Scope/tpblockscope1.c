/* tpblockscope1.c: block scope */

int main()
{
  int a = 1;
  {
    int a = 2;  /* Parse Error "redeclaration" */
    a = a + 1;
  }
  printf("a = %d\n",a); /* SF030609 */
  return a;
}

