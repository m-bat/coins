/* back2.c global variables */

/* #include <stdio.h>
*/
int printf(char*, ...);

int i, s;
int main ()
{
  s = 0;
  for (i = 1; i < 100; i = i+1) {
    s = s + i;
  }
  printf("s=%d\n", s); 
}
