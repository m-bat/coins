/* tpblockscope3.c: block scope test for switch */

int main()
{
  int a = 1;
  int b = 1;
  int c = 2; 
  switch (a) {
  case 0: {
    int a = 20;
    int b = 30;
    a = a + 1;
    b = 10;
    c = a + b;
    }
    break;
  case 1: {
    int a = 50;
    int b = 60;
    a = a + 1;
    b = 20;
    c = a + b;
    }
    break;
  default:
    break;
  }
  c = c + a;
  printf("%d \n", c);
}

