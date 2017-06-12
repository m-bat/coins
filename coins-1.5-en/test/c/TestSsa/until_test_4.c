#include<stdio.h>
/*
	by FUKUOKA Takeaki
int until_test_4 ()
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
  do {
    a = a + b; 
    if (a == 0) {
      i = i + 1;
      break;
    } else {
      i = i + 1;
      continue;
    }
  } while (i < 100);

  a = b + c;
  printf("%d %d %d\n",a,b,c);
  return a;
}
