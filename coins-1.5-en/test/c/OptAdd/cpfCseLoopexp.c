/* cpfCseLoopexp.c */

/* #include <stdio.h>
*/
int printf(char*, ...);

int main ()
{
  int a, b, c, d, x;
  int aa[10];
  int i;
  a = 1;
  b = 1 + 1;
  c = a + b;
  d = a + b;
  if (a+1 < c-1)
    x = a - b;
  else
    x = a + b;
  printf("%d\n",x); 
  aa[0] = 1;
  for (i = 1; i < 10; i++) {
    if (x > 0) {
      aa[i] = aa[i-1] + i;
    }else {
      aa[i] = aa[i-1] + 1;
    }
  }
  printf("%d\n", aa[9]); 
}
