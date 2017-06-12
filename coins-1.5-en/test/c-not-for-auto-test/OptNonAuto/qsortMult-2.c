/* qsortMult-2.c  Multi-input file test */
/*   Compile with qsortMult-1.c and qsortMult-3.c */

int printf(char*, ...);

void swap(double a[], int i, int j);

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

