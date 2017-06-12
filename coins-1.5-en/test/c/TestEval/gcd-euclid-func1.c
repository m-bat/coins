/* greatest common divisor - Euclidean algorithm
   input:  x and y.
   output: greatest common divisor of x and y,
           and the number of iteration.
*/
/* modified by M. Sassa for coins test suite on July 13, 2002 */
#include <stdio.h>

int noofiteration;  /* number of iteration */

int gcd(int x, int y) {
   while (y != 0) {
      int r = x % y;
      x = y;
      y = r;
      noofiteration = noofiteration + 1; /* count no. of iteration */
   }
   /* here, x is the GCD */
   return x;
}

int main()
{
   int x, y;   /* input */
/*
   printf("type two numbers: ");
   scanf("%d%d", &x, &y);
*/
   x = 1346269;   /* fibonacci(30) */
   y = 2178309;   /* fibonacci(31) */
   printf("GCD(%d, %d) = ", x, y);
   noofiteration = 0;
   printf(" %d\n", gcd(x,y));
   printf("no. of iteration is %d\n", noofiteration);
   return 0;
}




