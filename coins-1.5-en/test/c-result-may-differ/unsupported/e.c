/***********************************************************
	e.c -- �����п�����
***********************************************************/

long double ee(void)
{
	int n;
	long double e, a, prev;

	e = 0;  a = 1;  n = 1;
	do {
		prev = e;  e += a;  a /= n;  n++;
	} while (e != prev);
	return e;
}

#include <stdio.h>
#include <stdlib.h>
#include <float.h>

#define LIMIT 10

int main()
{
  int k;

  for(k = 0; k < LIMIT; k++){
    printf("e = %.*Lg\n", LDBL_DIG, ee());
  }

  return EXIT_SUCCESS;
}
