/* tpfuncptr3.c  Function pointer pointer. */

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
  if (p1 < p2)
    return -1;
  else if (p1 > p2)
    return 1;
  else
    return 0;
}

int strcmp(const char *, const char *);
/**
int (&fptr1)(void *, void *);
int (&fptr2)(int *pi1, int *pi2)
**/
int (*fptr1)(void *, void *);
/* SF030609[
int (*fptr2)(int *pi1, int *pi2)
{
  return (*fptr2)(pi1, pi2);
} */
int (*fptr2)(int *pi1, int *pi2);
/* SF030609] */

void
error(char *msg)
{
  printf("Error %s \n", msg);
}

main()
{
  int i1, i2, i3;

  fptr1 = &numcmp;
  fptr2 = &numcmp;
  i1 = strcmp("ab", "cd");
  i2 = fptr1(data[1], data[2]);
  i3 = (*fptr2)(data[1], data[2]);
  qsort((void **)data, 0, 100, selectFunc(numcmp, strcmp, 1));
  printf("function pointer pointer test.\n"); /* SF030609 */
  return 0;
}
