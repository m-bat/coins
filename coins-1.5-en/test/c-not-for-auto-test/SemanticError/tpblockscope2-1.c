/* tpblockscope2-1.c: block scope test 2 */

int main()
{
  int a = 1;
  int b = 1;
  int b = 2;    /* Redeclaration */
  {
    int a = 2;
    int b = 3;
    a = a + 1;
    b = 10;
  }
  printf("This test will cause 'redeclaration' error.\n");
  return a+b;
}

