/* General heap sort program with random number generation */
/* Its algorithm is based on a Pascal program
   in Kiyoshi Ishihata: Algorithms and Data Structures, Iwanami, 1989. 
   Thanks to his advice.
*/

/*--------- File common.h ----------*/
#define TRUE   1
#define FALSE  0

#define min(x, y)  ((x)<(y) ? (x) : (y))
#define max(x, y)  ((x)>(y) ? (x) : (y))
#define swap(x, y) { item_type tmp; \
          tmp = (x); (x) = (y); (y) = tmp; }

/*------------- File random.c ---------*/
/* random number generator */

int randomseed;

initrandom(v)
int v;
{
  randomseed = v;
}

/* random number */

int random()
{
  randomseed = 19157*randomseed+509;
  return(randomseed);
}

/* random number in the range [0, modulo-1] */

int rrandom(int modulo)
{  int x;

  x = random();
  if (x < 0) x = -x;
  if (x < 0) x = 0;
  x = x/(32767/modulo);
  x = x%modulo;
  return(x);
}

/*------------ File sort.c ----------*/
/* Sorting algorithms */

#include <stdio.h>
/* #include "common.h"*/
/* #include "random.c"*/

#define MAX_TEST 4
#define MAX_ALGO 3

/** #define SIZE     2550
**/
#define SIZE     100
#define debug    TRUE

typedef int item_type;

void init_data(int test);
void heap_sort(int a[], int pn);
void print_data(item_type x[]);

item_type original[SIZE], a[SIZE], sorted[SIZE];
int n;

int main()
{
  int test, algo;
  int i;  /*m*/

  n = SIZE;
  /** for (test = 0; test < MAX_TEST; test++)
  **/
  for (test = 3; test < MAX_TEST; test++) {
    init_data(test);
    /** for (algo = 0; algo < MAX_ALGO; algo++)
    **/
    for (algo = 1; algo < 2; algo++) {
      for (i = 0; i < n; i++)
        a[i] = original[i];
      switch (algo) {
      case 0 :
        printf("quick sort\n");
        /* quick_sort(); */ /*m*/
        break;
      case 1 :
        printf("heap sort\n");
        heap_sort(a, n);  /*m*/
        break;
      case 2 :
        printf("merge sort\n");
        /* merge_sort(); */  /*m*/
        break;
      }
    }
    print_data(a);
  }
  return 0;
}

void init_data(int test)
{
  int i;

  printf("test number %1d ", test);
  switch (test) {
  case 0 :
    printf("(constant)");
    for (i = 0; i < n; i++)
      original[i] = 0;
    break;
  case 1 :
    printf("(sorted)");
    for (i = 0; i < n; i++)
      original[i] = i;
    break;
  case 2 :
    printf("(reverse sorted)");
    for (i = 0; i < n; i++)
      original[i] = n-i;
    break;
  case 3 :
    printf("(random)");
    for (i = 0; i < n; i++)
      original[i] = rrandom(4095);
    break;
  }
  printf("\n");
}

void print_data(item_type x[])
{
  int i, col, lim;

  printf("print_data %d %d \n", n, debug);
  for (i = 0; i < n-1; i++) {
    if (x[i] > x[i+1])
      printf(" Bad[%d] %d %d \n", i, x[i], x[i+1]);
  }
  if (debug) {
    lim = (n > 1000 ? 1000 : n);
    col = 0;
    for (i = 0; i < lim; i++) {
      if (col >= 12) {
        printf("\n");
        col = 0;
      }
      printf(" %d", x[i]);
      col++;
    }
    if (col > 0)
      printf("\n");
  }
}

void heap_sort(int a[], int pn)
{
  int i, j, k;
  int x, n;

  n = pn;
  for (k = n / 2; k >= 1; k--) {
    i = k;
    x = a[i-1];
    while ((j = 2 * i) <= n) {
      if (j < n && a[j-1] < a[j])
        j++;
      if (x >= a[j-1])
        break;
      a[i-1] = a[j-1];
      i = j;
    }
    a[i-1] = x;
  }
  while (n > 1) {
    x = a[n-1];
    a[n-1] = a[0];
    a[0] = x;
    i = 1;
    while ((j = 2 * i) < n) {
      if ((j < n-1) && a[j-1] < a[j])
        j++;
      if (x >= a[j-1])
        break;
      a[i-1] = a[j-1];
      i = j;
    }
    a[i-1] = x;
    n--; 
  }
}

