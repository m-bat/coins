#include <stdio.h>

void strcpy(char *s, char *t)
{
	while (*s++ = *t++)
		;
}

int strcmp(char *s, char *t)
{
	for ( ; *s == *t; s++, t++)
		if (*s == '\0')
			return 0;
	return *s - *t;
}

main()
{
	char buf[64];
	
	strcpy(buf, "hello, world");
	printf("%d\n", strcmp("hello, worldA", buf));
}
