/* KandR/7-6.c  Error message test.
   Give such command as
     ./a.out KandR/7-6.in
   which will produce message
     can't open KandR/7-6.in
*/

#include <stdio.h>

main(int argc, char *argv[])
{
	FILE *fp;
	void filecopy(FILE *, FILE *);
	char *prog = argv[0];
	
	if (argc == 1)
		filecopy(stdin, stdout);
	else
		while (--argc > 0)
			if ((fp = fopen(*++argv, "r")) == NULL) {
				fprintf(stderr, "%s: can't open %s\n", prog, *argv);
				exit(1);
			} else {
				filecopy(fp, stdout);
				fclose(fp);
			}
	if (ferror(stdout)) {
		fprintf(stderr, "%s: error writing stdout\n", prog);
		exit(2);
	}
	exit(0);
}

/* from 7-5.c begin */
void filecopy(FILE *ifp, FILE *ofp)
{
	int c;

	while ((c = getc(ifp)) != EOF)
		putc(c, ofp);
}
/* from 7-5.c end */
