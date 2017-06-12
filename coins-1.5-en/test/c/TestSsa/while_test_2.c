#include<stdio.h>
/*
	by FUKUOKA Takeaki
int while_test_2 ()
*/
int main()
{
  int a;
  int b;
  int c;
  int i;
  
  a = 0;
  b = 10;
  c = 100;

  i = 0;
  while (i < 100) {
    a = a + b;
    if (a == 0) {
      i = i + 1;
      break;
    }
    i = i + 1;
  }

  a = b + c;
  printf("%d %d %d\n",a,b,c);
  return a;
}
