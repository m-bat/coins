#define VTAB1 '\013'
#define BELL1 '\007'

#define VTAB2 '\xb'
#define BELL2 '\x7'

#define MAXLINE 1000
char line[MAXLINE+1];

#define LEAP 1
int days[31+28+LEAP+31+30+31+30+31+31+30+31+30+31];

char string[] = "I am a string";
char hello[] = "hello," " world";

int strlen(char s[])
{
	int i;

	i = 0;
	while (s[i] != '\0')
		++i;
	return i;
}

enum boolean { NO, YES };

enum escapes {
	BELL = '\a',
	BACKSPACE = '\b',
	TAB = '\t',
	NEWLINE = '\n',
	VTAB = '\v',
	RETURN = '\r'
};

enum months {
	JAN = 1,
	FEB,
	MAR,
	APR,
	MAY,
	JUN,
	JUL,
	AUG,
	SEP,
	OCT,
	NOV,
	DEC
};

#include <stdio.h>

main()
{
	printf("%s %d%c%s %d\n", string, strlen(string), TAB, hello, strlen(hello));
	printf("Remember %d 11,2001.\n",SEP); /* SF030620 */
}
