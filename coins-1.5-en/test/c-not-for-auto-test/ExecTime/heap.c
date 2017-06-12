/* General heap sort with random number generation */
/* Rewritten from a Pascal program
   in Ishihata: Algorithm and Data Structure, Iwanami. 
*/

#include<unistd.h>
#include<time.h>
#include <assert.h>
#include <stdio.h>
#include <sys/time.h>
#include <unistd.h>
#include<sys/resource.h>


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
  /* x = x/(32767/modulo); */
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
/** #define SIZE     4000000 */
#define SIZE     8000000 /**/
/* #define SIZE 10 */
/* #define debug    TRUE */
#define debug 0
typedef int item_type;


struct timer{
  struct timeval start;
  struct timeval end;
  struct timeval diff;
};


struct timer timer;
struct timeval sum;

init(){
  sum.tv_usec = 0;
  sum.tv_sec  = 0;
}

static void addtimeval(struct timeval* a, struct timeval* b,
                       struct timeval* c) {
  c->tv_sec = a->tv_sec + b->tv_sec;
  c->tv_usec = a->tv_usec + b->tv_usec;
  if (c->tv_usec >= 1000000) {
    c->tv_usec -= 1000000;
    c->tv_sec ++;
  }

}

static void subtracttimeval(struct timeval* a, struct timeval* b,
                            struct timeval* c) {
  c->tv_sec = a->tv_sec - b->tv_sec;
  c->tv_usec = a->tv_usec - b->tv_usec;
  if (c->tv_usec < 0) {
    c->tv_usec += 1000000;
    c->tv_sec --;
  }
  sum.tv_usec += c->tv_usec;
  sum.tv_sec  += c->tv_sec;
  if (sum.tv_usec < 0) {
    sum.tv_usec += 1000000;
    sum.tv_sec --;
 }
}



static void starttimer(struct timer* timer) {
  struct rusage r_usage;

  getrusage(RUSAGE_SELF, &r_usage);
  addtimeval(&r_usage.ru_utime, &r_usage.ru_stime, &timer->start);
}

static void stoptimer(struct timer* timer) {
  struct rusage r_usage;

  getrusage(RUSAGE_SELF, &r_usage);
  addtimeval(&r_usage.ru_utime, &r_usage.ru_stime, &timer->end);
  subtracttimeval(&timer->end, &timer->start, &timer->diff);
}

static void showtimer(struct timer* timer) {
/*  printf("%f sec.\n", timer->diff.tv_sec + timer->diff.tv_usec / 1.0e6); */
  printf("%f sec.\n", sum.tv_sec + sum.tv_usec / 1.0e6);
}



/*
static void starttimer(struct timer* timer){
  gettimeofday(&timer->start,NULL);
}

static void stoptimer(struct timer* timer) {
  gettimeofday(&timer->end, NULL);
  timer->diff.tv_sec = timer->end.tv_sec - timer->start.tv_sec;
  timer->diff.tv_usec = timer->end.tv_usec - timer->start.tv_usec;
  if (timer->diff.tv_usec < 0) {
    timer->diff.tv_usec += 1000000;
    timer->diff.tv_sec --;
  }
}

static void showtimer(struct timer* timer) {
  printf("%f sec.\n", timer->diff.tv_sec + timer->diff.tv_usec / 1.0e6);
}
*/

int init_data(int test);
int heap_sort(int a[], int pn);
int print_data(item_type x[]);

item_type original[SIZE], a[SIZE], sorted[SIZE];
int n;

int main()
{
  int test, algo;
  int i;  /*m*/
  int counter;
  int loopCount;

starttimer(&timer);
for (loopCount = 0; loopCount < 5; loopCount++) {
  init();

  n = SIZE;
  /** for (test = 0; test < MAX_TEST; test++)
  **/
  for (test = 3; test < MAX_TEST; test++) {
    /* starttimer(&timer); */
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
            /* printf("heap sort\n"); */

           /* starttimer(&timer); */
            heap_sort(a, n);  /*m*/
           /* stoptimer(&timer); */
            break;
          case 2 :
            printf("merge sort\n");
            /* merge_sort(); */  /*m*/
            break;
          }
      }
    print_data(a);
  }
}
  stoptimer(&timer);
  showtimer(&timer);
  return 0;
}

int init_data(int test)
{
  int i;

  /* printf("test number %1d ", test); */
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
    /* printf("(random)"); */
    for (i = 0; i < n; i++)
      original[i] = rrandom(10000);
    break;
  }
  /* printf("\n"); */
}

int print_data(item_type x[])
{
  int i, col, lim;

  /* printf("print_data %d %d \n", n, debug); */
  for (i = 0; i < n-1; i++) {
    if (x[i] > x[i+1])
      printf(" Bad[%d] %d %d \n", i, x[i], x[i+1]);
  }
  if (debug) {
    /* lim = (n > 1000 ? 1000 : n); */
    lim = SIZE;
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

int heap_sort(int a[], int pn)
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
      if ((j < n-1) &&a[j-1] < a[j])
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

