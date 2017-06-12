/* merge sort
   input:  n and array a[0]..a[n-1]
   output: sorted array a[0]..a[n-1] */
/* modified for coins test suite on July 13, 2002 by M. Sassa */

#include <stdio.h>

/* array upper bound */
#define NMAX 10000
/* used size of array */
#define N 10

/* global variables */
double a[NMAX];
double b[NMAX];
int steps;
int noofcalls /* no. of function calls */;

/* sort a[left]..a[right] */
void msort(double a[], int left, int right) {
   int i, j, k, m;

   noofcalls++;
   if (left < right) {
      m = (right + left) / 2;
      msort(a, left, m);
      msort(a, m+1, right);
      /* merge */
      for (i = m+1; i > left;  i--) b[i-1] = a[i-1];
      for (j = m;   j < right; j++) b[right+m-j] = a[j+1];
      /* here, i == left, j == right */
      for (k = left; k <= right; k++)
         a[k] = (b[i] < b[j]) ? b[i++] : b[j--];
   }
   steps = steps + (right - left + 1) * 4 + 2;
}

main () {
   int i, n;

   n = N; /* used size of the array */
   steps = 0;
   noofcalls = 0;

   /* initialize data */
   for (i = 0; i < n; i++) {
      a[i] = n - i;
   }

   /* write array a[0]..a[n-1] */
   printf("input =\n");
   for (i = 0; i < n; i++) {
      printf("%15g\n", a[i]);
   }

   /* sort */
   msort(a, 0, n-1);

   /* write array a[0]..a[n-1] */
   printf("result =\n");
   for (i = 0; i < n; i++) {
      printf("%15g\n", a[i]);
   }

   /* write no of steps ... */
   printf("no. of steps  = %d,  no. of procedure calls = %d\n", steps, noofcalls);

   return 0;
}

