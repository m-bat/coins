/* KandR/7-4-a.c  Give KandR/7-4-a.in as input file */

#include <stdio.h>

main()
{
	/* double sum, v; */
	double sum;
	float v; /* scanf("%f",...) requires a pointer to float */

	sum = 0;
	while (scanf("%f", &v) == 1)
		printf("\t%.2f\n", sum += v);
	return 0;
}
