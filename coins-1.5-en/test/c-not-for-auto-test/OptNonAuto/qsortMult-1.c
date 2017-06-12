/* qsortMult-1.c  Multi-input file test */
/*   Compile with qsortMult-2.c and qsortMult-3.c */

int printf(char*, ...);

#define UPPER 1000
#define N 10

void quicksort(double a[], int left, int right);
int readdata(double a[]);
void writedata(double a[], int n);

main ( ) {
   int n; /* number of data */
   double a[UPPER];
   n = readdata(a);
   printf("\ninput =\n");
   writedata(a, n);
   quicksort(a, 0, n-1);
   printf("\nresult =\n");
   writedata(a, n);
   return 0;
}

