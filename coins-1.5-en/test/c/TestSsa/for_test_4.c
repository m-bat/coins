#include<stdio.h>
/* by FUKUOKA Takeaki
int for_test_4 ()
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
      break;
    } else {
      continue;
    }
  }

  a = b + c;

  printf("%d %d %d\n",a,b,c);
  return a;
}
