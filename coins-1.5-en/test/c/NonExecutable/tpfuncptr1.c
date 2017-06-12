/* tpfuncptr1.c  Function pointer qsort (K & R. p.145) */

#include <stdio.h>

/*int printf(char*, ...); /* SF030620 */

char *lineptr[5000];
int  readlines(char *lineptr[], int nlines);
void writelines(char *lineptr[], int nlines);
void qsort(void *lineptr[], int left, int right,
           int (*comp)(void *, void *));
int numcmp(const char *, const char *);
int strcmp(char *, char *);

main(int argc, char *argv[])
{
  /* SF030620[
  int nlines;
  int numeric = 0;

  if ((argc > 1 && strcmp(argv[1], "-n") == 0) == 0)
    numeric = 1;
  if ((nlines = readlines(lineptr, 5000)) >= 0) {
    qsort((void **) lineptr, 0, nlines - 1,
          (int (*)(void *, void *))(numeric ? numcmp : strcmp));
    writelines(lineptr, nlines);
    return 0;
  }else {
    printf("input too big to sort\n");
    return 1;
  }
  printf("not yet\n"); */
  printf("function declaration test.");
  /* SF030620] */
}
