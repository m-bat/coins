#include <stdio.h>

int strlen(char *s)
{
	int n;

	for (n = 0; *s != '\0'; s++)
		n++;
	return n;
}

main()
{
	printf("%d\n", strlen("hello, world"));
}
