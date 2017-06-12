#include<stdio.h>
/* by FUKUOKA Takeaki
int rename_test ()
*/
int main()
{
  int a;
  int b;
  int c;

  a = 0;
  b = 10;
  c = 100;
  
  if (a == 0) {
    int a;
    a = 1;
    b = a;
  } else {
    int a;
    a = 2;
    c = a;
  }  

  a = b + c;

  /* by FUKUOKA Takeaki */
  printf("%d\n",a);

  return a;
}
