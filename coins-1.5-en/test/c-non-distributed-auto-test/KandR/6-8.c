#include <stdio.h>

#define INT 1
#define FLOAT 2
#define STRING 3
#define NSYM 100

union u_tag {
	int ival;
	float fval;
	char *sval;
} u;

struct {
	char *name;
	int flags;
	int utype;
	union {
		int ival;
		float fval;
		char *sval;
	} u;
} symtab [ NSYM ];

void printval(int i)
{
	if (symtab[i].utype == INT)
		printf("%d\n", symtab[i].u.ival);
	else if (symtab[i].utype == FLOAT)
		printf("%f\n", symtab[i].u.fval);
	else if (symtab[i].utype == STRING)
		printf("%s\n", symtab[i].u.sval);
	else
		printf("bad type %d in utype\n", symtab[i].utype);
}

main()
{
	symtab[0].utype = INT;
	symtab[0].u.ival = 1;
	symtab[1].utype = FLOAT;
	symtab[1].u.fval = 1.1;
	symtab[2].utype = STRING;
	symtab[2].u.sval = "ABC";
	printval(0);
	printval(1);
	printval(2);
}
