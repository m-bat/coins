/* KandR/7-7.c  Give KandR/7-7.in or any other small text file
   as input. 
*/

#include <stdio.h>

char *fgets(char *s, int n, FILE *iop)
{
	register int c;
	register char *cs;

	cs = s;
	while (--n > 0 && (c = getc(iop)) != EOF)
	if ((*cs++ = c) == '\n')
		break;
	*cs = '\0';
	return (c == EOF && cs == s) ? NULL : s;
}

/* int fputs(char *s, FILE *iop) */
int fputs(const char *s, FILE *iop) /* conforms to stdio.h */
{
	int c;
	while (c = *s++)
		putc(c, iop);
	return ferror(iop) ? EOF : 0;
}

main()
{
	char buf[1000];
	
	while (fgets(buf, 1000, stdin))
		fputs(buf, stdout);
}
