/* back3.c array elements */

/* #include <stdio.h>
*/
int printf(char*, ...);

int main ()
{
  int i, a[100];
  a[0] = 0;
  for (i = 1; i < 100; i = i+1) {
    a[i] = a[i-1] + i;
  }
  printf("%d\n", a[99]); 
}
