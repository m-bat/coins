/* tpfuncptr2.c  Function pointer pointer. */

int *data[5000];
int numeric = 0;
char c;

int
printf(char*, ...);

void
swap(void *v[], int i, int j)
{
  void *temp;
  temp = v[i];
  v[i] = v[j];
  v[j] = temp;
}

typedef int cmpFunc(void *, void *);

cmpFunc
*selectFunc(int (*pf1)(void *, void *),
            int (*pf2)(void *, void *), int pType)
{
   return pType == 1 ? pf1 : pf2;
}

void
qsort(void *v[], int left, int right,
      int (*comp)(void *, void *))
{
  int i, last;

  if (left >= right)
    return;
  swap(v, left, (left+right)/2);
  last = left;
  for (i = left+1; i <= right; i++)
    if ((*comp)(v[i], v[left]) < 0)
      swap(v, ++last, i);
  swap(v, left, last);
  qsort(v, left, last-1, comp);
  qsort(v, last+1, right, comp);
}

void
qsort1(void *lineptr[], int left, int right,
       int (*comp)(void *, void *), int (*error1)(char *),
       int (**error2)(char *))
{
  if (left > right)
    error1("null range");
  else if (left < 0)
    (*error2)("minus range");
  else
    qsort(lineptr, left, right, comp);
}

int numcmp(int *p1, int *p2)
{
  /*if (p1 < p2) /* SF030620 */
  if (*p1 < *p2) /* SF030620 */
    return -1;
  /*else if (p1 > p2) /* SF030620 */
  else if (*p1 > *p2) /* SF030620 */
    return 1;
  else
    return 0;
}

int strcmp(const char *, const char *);

int (*fptr1)(void *, void *);
int (*fptr2)(int *pi1, int *pi2);

void
error(char *msg)
{
  printf("Error %s \n", msg);
}

main()
{
  int i1, i2, i3;

  int j, sample[10]={9, 5, 1, 3, 7, 6, 2, 4, 8, 0 }; /* SF030620 */
  for(j=0;j<10;j++) data[j]=&sample[j]; /* SF030620 */

  fptr1 = (int (*)(void*, void*))&numcmp;
  fptr2 = &numcmp;
  i1 = strcmp("ab", "cd");
  i2 = fptr1(data[1], data[2]);
  i3 = (*fptr2)(data[1], data[2]);
  /*qsort((void **)data, 0, 100, /* SF030620 */
  qsort((void **)data, 0, 9, /* SF030620 */
        selectFunc((int (*)(void*, void*))numcmp,
		   (int (*)(void*, void*))strcmp, 1));

  printf("QuickSort\n"); /* SF030620 */
  printf("%d %d %d %d %d %d %d %d %d %d\n", /* SF030620 */
	 *data[0],*data[1],*data[2],*data[3],*data[4], /* SF030620 */
	 *data[5],*data[6],*data[7],*data[8],*data[9]); /* SF030620 */
  return 0;
}
