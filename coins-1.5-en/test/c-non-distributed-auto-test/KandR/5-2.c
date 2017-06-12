/* KandR/5-2.c  Give 5-2.in as input file */

#include <stdio.h>

void swap(int *px, int *py)
{
	int temp;

	temp = *px;
	*px = *py;
	*py = temp;
}

#define SIZE 10

main()
{
	int n, array[SIZE], getint(int *), i;

	for (n = 0; n < SIZE && getint(&array[n]) != EOF; n++)
		;
	swap(&array[0], &array[1]);
	for (i = 0; i < n; i++)
		printf("%d ", array[i]);
	puts("");
}

#include <ctype.h>

int getch(void);
void ungetch(int);

int getint(int *pn)
{
	int c, sign;

	while (isspace(c = getch()))
		;
	if (!isdigit(c) && c != EOF && c != '+' && c != '-') {
		ungetch(c);
		return 0;
	}
	sign = (c == '-') ? -1 : 1;
	if (c == '+' || c == '-')
		c = getch();
	for (*pn = 0; isdigit(c); c = getch())
		*pn = 10 * *pn + (c - '0');
	*pn *= sign;
	if (c != EOF)
		ungetch(c);
	return c;
}

/* from 4-3.c begin */
#define BUFSIZE 100

char buf[BUFSIZE];
int bufp = 0;
int getch(void)
{
	return (bufp > 0) ? buf[--bufp] : getchar();
}

void ungetch(int c)
{
	if (bufp >= BUFSIZE)
		printf("ungetch: too many characters\n");
	else
		buf[bufp++] = c;
}
/* from 4-3.c end */
