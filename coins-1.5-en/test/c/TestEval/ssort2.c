/* selection sort
   input:  n and array a[0]..a[n-1]
   output: sorted array a[0]..a[n-1] */
/* modified for coins test suite, July 13, 2002 by M. Sassa */

#include <stdio.h>

/* array upper bound */
#define UPPER 1000
/* used size of array */
#define N 10

/* ssort: sort a[0]...a[n-1] in ascending order */
void ssort(double a[], int n) {
    int min;
    int index;
    int i, j;

    for (i = 0; i < n-1; i++) {
      min = a[i];
      index = i;
      for (j = i+1; j < n; j++) {
        if (a[j] < min) {
          min = a[j];
          index = j;
        }
      }
      a[index] = a[i];
      a[i] = min;
    }
  }

/* read n (number of array elements) and array a[0]..a[n-1],
   and return n */
/* modified. just initialize data here */
int readdata(double a[]) {
   int n, i;
   n = N;   /* used size of array */
   for (i = 0; i < n; i++) {
      a[i] = n - i;
   }
   return n;
}

/* write array a[0]..a[n-1] */
void writedata(double a[], int n) {
   int i;
   for (i = 0; i < n; i++) {
      printf("%15g\n", a[i]);
   }
}

main ( ) {
   int n; /* number of data */
   double a[UPPER];
   n = readdata(a);
   printf("input =\n");
   writedata(a, n);
   ssort(a, n);
   printf("result =\n");
   writedata(a, n);
   return 0;
}

