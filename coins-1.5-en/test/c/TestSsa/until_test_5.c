#include<stdio.h>
/*
	by FUKUOKA Takeaki
int until_test_5 ()
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
  i=0; /* FUKUOKA Takeaki */
  if (a == 0) {
    goto LABEL;
  }

  /*i = 0; */ /* Comment out by FUKUOKA Takeaki */
  do {
    a = a + b; 
    if (b == 0) {
      i = i + 1;
      break;
    } else {
      i = i + 1;
      continue;
    }

  LABEL:
    if (c == 0) {
      b = b + c;
      continue;
    }
  } while (i < 100);

  a = b + c;
  printf("%d %d %d\n",a,b,c);
  return a;
}
