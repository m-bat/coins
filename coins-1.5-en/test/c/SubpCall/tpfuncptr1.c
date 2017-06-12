/* tpfuncptr1.c  Function pointer qsort (K & R. p.145) simplified. */
char *lineptr[5000];
void qsort(void *lineptr[], int left, int right,
           int (*comp)(void *, void *));
void qsort1(void *lineptr[], int left, int right, int (*)(void *, void *));

/* SF030609[
int numcmp(char *, char *); */
int numcmp(char **a, char **b)
{
  return atoi(*a) - atoi(*b);
}
/* SF030609] */
int strcmp(char *, char *);
char c;

main()
{
  int numeric = 0;

  c = (char)numeric;
  qsort((void **) lineptr, 0, 100,
        (int (*)(void*, void*))(numcmp));
  /*      (int (*)(void *, void *))(numeric ? numcmp : strcmp)); */
  printf("Function pointer qsort simplified.\n"); /* SF030609 */
  return 0;
}
