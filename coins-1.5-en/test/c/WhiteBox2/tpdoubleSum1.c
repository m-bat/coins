/* tpdoubleSum1.c  Nakata Mail 051103 */

#include <stdio.h>
/* #define N 1000000 */
#define N 10000 /*##*/
double d[N];

int main()
{
    int i, j;
    double sum;
    for (j = 0; j < 500; j++) {
	for (i = 0; i < N; i++) {
	    d[i] = i;
	}

	for (i = 1; i < N; i++) {
	    d[i] += d[i-1];
	}
	sum = 0;
	for (i = 0; i < N; i++) {
	    sum += d[i];
	}
    }
    printf("%f\n", sum);
    return 0;
}

