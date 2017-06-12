/* KandR/2-6.c getline is a little different from that of 1-9.c */
/* Give any small text file as input. */

#include <stdio.h>

/** #define EOF -1 
**/

int getline(char s[], int lim)
{
	int c, i;

	for (i=0; i<lim-1 && (c=getchar())!='\n' && c != EOF; ++i)
		s[i] = c;
	if (c == '\n') {
		s[i] = c;
		++i;
	}
	s[i] = '\0';
	return i;
}

#define MAXLINE 1000

main()
{
	int len;
	char line[MAXLINE];
	char longest[MAXLINE];
	
	while ((len = getline(line, MAXLINE)) > 0) 
		printf("%d %s", len, line);
	return 0;
}

