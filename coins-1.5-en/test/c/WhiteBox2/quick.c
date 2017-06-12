/* quick.c
 * Sample C program (quicksort) for Code generator
 *   Give data sequence by command line
 */

#undef __CYGWIN__
#include <stdio.h>
#include <stdlib.h>

void quicksort(int a[], int m, int n)
{
    int i, j;
    int v;
    int x;

    if (n <= m) return;

    i = m - 1; j = n; v = a[n];
    for (;;) {
	while (a[++i] < v)
	    ;
	while (--j > i && a[j] >= v)
	    ;
	if (i >= j)
	    break;
	x = a[i]; a[i] = a[j]; a[j] = x;
    }
    x = a[i]; a[i] = a[n]; a[n] = x;
    quicksort(a, m, i - 1);
    quicksort(a, i + 1, n);
}


int main(int argc, char **argv)
{
    int a[100];
    int i, n;

    n = 0;
/**
    for (i = 1; i < argc; i++)
	a[n++] = atoi(argv[i]);
**/
    n = 100;
    for (i = 0; i < n; i++) {
      a[i] = (i * i) + (n - i)*(n - i);
    } 

    quicksort(a, 0, n - 1);
    for (i = 0; i < n; i++)
	printf("%d ", a[i]);
    printf("\n");
}


