/* KandR/7-1.c  Give KandR/7-1.in as input file */

#include <stdio.h>
#include <ctype.h>

main()
{
	int c;

	while ((c = getchar()) != EOF)
		putchar(tolower(c));
	return 0;
}
