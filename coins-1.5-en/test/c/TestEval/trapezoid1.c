/* integral - trapezoidal rule */
#include <stdio.h>

double f(double x) {
   return 1.0 - x*x*x*x;
}

main( ) {
   int n, i;
   double a, b, h, sum, s;
   a = 0.0;
   b = 1.0;
   printf("a = %10g,  b = %10g\n", a, b);
   n = 10000;
   h = (b - a) / n;
   sum = f(a) * 0.5;
   for (i = 1; i <= n-1; i++) {
      sum = sum + f(a + i * h);
   }
   s = h * (sum + f(b) * 0.5);
   printf("n = %10d      h = %-8g    s = %22.16f\n", n, h, s);
}

