#include<stdio.h>
/* by FUKUOKA Takeaki
int for_test_5 ()
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
	i = 0; // FUKUOKA Takeaki
  
  if (a == 0) {
    goto LABEL;
  }

  for (i = 0; i < 100; i = i + 1) {
    a = a + b; 
    if (b == 0) {
      break;
    } else {
      continue;
    }

  LABEL:
    if (c == 0) {
      b = b + c;
      continue;
    }
  }

  a = b + c;

  printf("%d %d %d\n",a,b,c);
  return a;
}


