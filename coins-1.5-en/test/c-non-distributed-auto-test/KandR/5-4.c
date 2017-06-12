#include <stdio.h>

#define ALLOCSIZE 10000

static char allocbuf[ALLOCSIZE];
static char *allocp = allocbuf;

char *alloc(int n)
{
	if (allocbuf + ALLOCSIZE - allocp >= n) {
		allocp += n;
		return allocp - n;
	} else
		return 0;
}

void afree(char *p)
{
	if (p >= allocbuf && p < allocbuf + ALLOCSIZE)
		allocp = p;
}
int strlen(char *s)
{
	char *p = s;

	while (*p != '\0')
		p++;
	return p - s;
}

#define SIZE 64

main()
{
	char *p = alloc(SIZE);
	int i;
	
	for (i = 0; i < SIZE; i++)
		p[i] = '-';
	p[SIZE-1] = 0;
	printf("%d\n", strlen(p));
}
