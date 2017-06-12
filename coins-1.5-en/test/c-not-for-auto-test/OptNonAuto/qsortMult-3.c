/* qsortMult-3.c  Multi-input file test */
/*   Compile with qsortMult-1.c and qsortMult-2.c */

int printf(char*, ...);

#define N 10

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
      printf(" %5g", a[i]);
   }
}

