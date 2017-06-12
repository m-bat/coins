/* tpblockscope2.c: block scope test 2 */

int main()
{
  int a = 1;
  int b = 1;
  int c = 2; 
  {
    int a = 2;
    int b = 3;
    a = a + 1;
    b = 10;
  }
  printf("%d \n", a+b);
}

