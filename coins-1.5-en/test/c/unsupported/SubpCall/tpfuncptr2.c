/* tpfuncptr2.c  Function pointer qsort (K & R. p.145) skeleton. */

char *lineptr[5000];
void qsort(void *lineptr[], int left, int right,
           int (*comp)(void *, void *));
void qsort1(void *lineptr[], int left, int right, int (*)(void *, void *));

/* SF030609[
int numcmp(char *, char *);
int strcmp(char *, char *); */
int numcmp(char **a, char **b)
{
  return atoi(*a) - atoi(*b);
}
int strcmp(char **a, char **b)
{
  char *p = *a;
  char *q = *b;
  while( *p==*q )
    p++, q++;
  return *p - *q;
}
/* SF030609] */

char c;

main()
{
  int numeric = 0;

  /* SF030609[
  c = (char)numeric;
  qsort((void **) lineptr, 0, 100,
        (int (*)(void *, void *))(numeric ? numcmp : strcmp)); */
  int i;
  char buf[16];
  char *strdup(char *);

  for( i=0; i<100; lineptr[i++]=strdup(buf) )
    sprintf(buf,"%d",rand());

  numeric = 0;
  printf("numeric = %d\n",numeric);
  qsort( lineptr, 100, sizeof(char*), (int(*)(void*,void*))(numeric?numcmp:strcmp) );
  for( i=0; i<100; i++ )
    printf("%s\n",lineptr[i]);

  numeric = 1;
  printf("numeric = %d\n",numeric);
  qsort( lineptr, 100, sizeof(char*), (int(*)(void*,void*))(numeric?numcmp:strcmp) );
  for( i=0; i<100; i++ )
    printf("%s\n",lineptr[i]);
  /* SF030609] */
  return 0;
}
