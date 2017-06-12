#include<stdio.h>
/* by FUKUOKA Takeaki
int for_test_3 ()
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

  for (i = 0; i < 100; i = i + 1) {
    a = a + b;
    if (a == 0) {
      continue;
    }
  }

  a = b + c;

  printf("%d %d %d\n",a,b,c);
  return a;
}
