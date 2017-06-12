/* back1.c local variables */

/* #include <stdio.h>
*/
int printf(char*, ...);

int main ()
{
  int i, s;
  s = 0;
  for (i = 1; i < 100; i = i+1) {
    s = s + i;
  }
  printf("s=%d\n", s); 
}
