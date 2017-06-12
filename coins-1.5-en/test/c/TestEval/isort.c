/* insertion sort
   input:  n and array a[1]..a[n]
   output: sorted array a[1]..a[n] */
/* originally by Teruo Hikita, C de kaku algorithm, Science-sha
   note that a[0] is used as a sentinel */
/* modified for coins test suite, July 13, 2002 by M. Sassa */

#include <stdio.h>

/* array upper bound */
#define UPPER 1000
/* used size of array */
#define N 10
/* sentinel */
#define SENTINEL -1.0e10

/* isort: insertion sort a[1]...a[n] in ascending order */
void isort(double a[], int n) {
  int i, j, q;
  for (i = 2; i <= n; i++) {
    q = a[i]; 
    j = i;
    while (a[j-1] > q) {
      a[j] = a[j-1];
      j--;
    }
    a[j] = q;
  }
}

/* write array a[1]..a[n] */
void writedata(double a[], int n) {
   int i;
   for (i = 1; i <= n; i++) {
      printf("%15g\n", a[i]);
   }
}

main ( ) {
   double a[UPPER+1];
   int n, i;
   n = N;   /* used size of array */
   for (i = 1; i <= n; i++) {
      a[i] = n - i;
   }
   a[0] = SENTINEL;
   printf("input =\n");
   writedata(a, n);
   isort(a, n);
   printf("result =\n");
   writedata(a, n);
   return 0;
}

