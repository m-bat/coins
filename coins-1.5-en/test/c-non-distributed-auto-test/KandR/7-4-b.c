/* KandR/7-4-b.c  Give KandR/7-4-b.in as input file */

#include <stdio.h>

/* from 1-9.c begin */
int getline(char s[], int lim)
{
	int c, i;

	for (i=0; i<lim-1 && (c=getchar())!=EOF && c!='\n'; ++i)
		s[i] = c;
	if (c == '\n') {
		s[i] = c;
		++i;
	}
	s[i] = '\0';
	return i;
}
/* from 1-9.c end */

void scandate()
{
	char line[1000];
	int getline(char *, int linesize);
	int year, month, day;
	char monthname[7];

	while (getline(line, sizeof(line)) > 0) {
		if (sscanf(line, "%d %s %d", &day, monthname, &year) == 3)
			printf("valid: %s\n", line); /* 25 Dec 1988 */
		else if (sscanf(line, "%d/%d/%d", &month, &day, &year) == 3)
			printf("valid: %s\n", line); /* mm/dd/yy */
		else
			printf("invalid: %s\n", line); /* invalid */
	}
}

main()
{
	scandate();
}
