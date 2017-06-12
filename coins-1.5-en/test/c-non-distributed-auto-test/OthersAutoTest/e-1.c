/***********************************************************
	e-1.c -- Computation of e 
***********************************************************/

/* long double ee(void) */
double ee(void)
{
	int n;
	double e, a, prev;

	e = 0;  a = 1;  n = 1;
	do {
		prev = e;  e += a;  a /= n;  n++;
	} while (e != prev);
	return e;
}

#include <stdio.h>
#include <stdlib.h>
#include <float.h>


int main()
{
  int k;

  printf("e = %g\n", ee());

  return 0;
}
