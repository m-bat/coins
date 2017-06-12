/* sorting by quicksort
   input:  n and array a[0]..a[n-1]
   output: sorted array a[0]..a[n-1] */
/* modified for coins test suite on July 13, 2002 by M. Sassa */

#include <stdio.h>
/* array upper bound */
#define UPPER 1000
/* used size of array */
#define N 10

/* quicksort: sort a[left]...a[right] in ascending order */
void quicksort(double a[], int left, int right) {
   int i, last;
   void swap(double a[], int i, int j); /* prototype declaration */

   if (left >= right) { /* if array contains only */
      return;           /* one element do nothing */
   }
   swap(a, left, (left + right)/2); /* move pivot element to a[left] */
   last = left;
   for (i = left+1; i <= right; i++) {
      if (a[i] < a[left]) {
         last = last + 1;
         swap(a, last, i);
      }
   }
   swap(a, left, last); /* recover pivot */
   quicksort(a, left, last-1);
   quicksort(a, last+1, right);
}

/* swap: swap a[i] and a[j] */
void swap(double a[], int i, int j) {
   double temp;
   temp = a[i];
   a[i] = a[j];
   a[j] = temp;
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
   quicksort(a, 0, n-1);
   printf("result =\n");
   writedata(a, n);
   return 0;
}

