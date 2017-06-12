#include<stdio.h>
int main () 
{
  int a;
  int b;
  int c;
  int i;

  a = 1;
  b = 5;
  c = 0;

  for (i = 0; i < 10; i = i + 1) {
    if (a != b) 
      a = a + 1;
    else 
      a = a + b;
    if (c != b)
      c = a + 1;
    else 
      c = c + b;
  }

  printf("%d\n",a);
  return a;
}
