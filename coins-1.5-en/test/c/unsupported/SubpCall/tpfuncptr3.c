/* tpfuncptr3.c  Function pointer pointer. */

int *data[50];
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
  /* printf("L %d R %d vl %d vr %d \n", left, right, v[left], v[right]); */
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

int numcmp2(int *p1, int *p2)
{
  if (*p1 < *p2)
    return -1;
  else if (*p1 > *p2)
    return 1;
  else
    return 0;
}

int strcmp(const char *, const char *);
int (*fptr1)(void *, void *);
int (*fptr2)(int *, int *);
int (*fptr3)(int *, int *);

void
error(char *msg)
{
  printf("Error %s \n", msg);
}

main()
{
  int cmpResult[4];
  int j;

  int value[2] = {1, 2}; /* SF030609 */
  data[1] = &value[0]; /* SF030609 */
  data[2] = &value[1]; /* SF030609 */

  fptr1 = &numcmp;
  fptr2 = &numcmp;
  fptr3 = &numcmp2;
  int (*fptrmsg)(char *); 
  fptrmsg = &error;      
  cmpResult[0] = strcmp("ab", "cd");
  cmpResult[1] = fptr1(data[1], data[2]);
  cmpResult[2] = (*fptr2)(data[1], data[2]);
  cmpResult[3] = (*fptr3)(data[1], data[2]);
  for (j = 0; j < 4; j++) {
    if (cmpResult[j] < 0) cmpResult[j] = -1;
    else if (cmpResult[j] > 0) cmpResult[j] = 1;
  }
  printf("strcmp %d fptr1 %d fptr2 %d fptr3 %d\n",
         cmpResult[0],cmpResult[1],cmpResult[2],cmpResult[3]);
  int value2[10] = {8, 2, 9, 1, 7, 3, 6, 4, 5, 0};
  char *str[10] = {"abc", "bcaa", "cab", "abdd", "dba", "dab", "abea", 
                   "bea", "eab", "efg"}; 
  int *data2[10];
  int i;
  for (i = 0; i < 10; i++) {
    data2[i] = &value2[i];
  }
  qsort1((void *)data2, 10, 9, selectFunc(numcmp2, strcmp, 1), &error, &fptrmsg);
  qsort1((void *)data2, -1, 9, selectFunc(numcmp2, strcmp, 1), &error, &fptrmsg);
  qsort((void *)data2, 0, 9, selectFunc(numcmp2, strcmp, 1));
  printf(" %d %d %d %d %d %d \n", *data2[0], *data2[1], *data2[2],
         *data2[7], *data2[8], *data2[9]);
  printf(" %d %d %d %d %d %d \n", value2[0], value2[1], value2[2],
         value2[7], value2[8], value2[9]);
  qsort((void **)str, 0, 9, selectFunc(numcmp, strcmp, 2));
  printf(" %s %s %s %s %s %s \n", str[0], str[1], str[2],
         str[7], str[8], str[9]);
  return 0;
}
