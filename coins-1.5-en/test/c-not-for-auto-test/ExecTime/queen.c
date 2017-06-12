/* Find all solutions to 8 queens prblem.               */
/* Compile with print.c */
#include<unistd.h>
#include<time.h>
#include <assert.h>
#include <stdio.h>
#include <sys/time.h>
#include <unistd.h>
#include<sys/resource.h>
#include<stdlib.h>

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




#define SIZE 14
void printInt(int p);   
void printStr(char* p);
void printEol();
int i;
int a[SIZE];
int b[SIZE*2];                   /* b[i + j] */
int c[SIZE*2];                   /* c[i - j + 8] <-- { c[-7..7] }  */
int x[SIZE];                    /* x[i] */
int callCount;

void PRINT()
{
  int k;

  for (k = 0; k < SIZE; k= k+1)	
    printInt(x[k] + 1);
  printStr(" Call count ");
  printInt(callCount);
  printEol();
}

void TRY(int i)
{
  int j;
 
  for (j = 0; j < SIZE; j= j+1) {
    if ((a[j] == 1) && (b[i+j] == 1) && (c[i-j+SIZE] == 1)) {
      x[i] = j;
      a[j]   = 0;
      b[i+j] = 0;
      c[i-j+SIZE] = 0;
      if (i < SIZE-1) {
        callCount = callCount+1;
        TRY(i+1);
      }
      a[j]   = 1;
      b[i+j] = 1;
      c[i-j+SIZE] = 1;
    }
  }
}

main()
{
int loopCount;

  for (i = 0; i < SIZE; i= i+1)      
    a[i] = 1;
  for (i = 0; i < SIZE*2; i= i+1)
    b[i] = 1;
  for (i = 0; i < SIZE*2; i= i+1)     
    c[i] = 1;
  callCount = 1;
  starttimer(&timer);
for (loopCount = 0; loopCount < 10; loopCount++) {
  TRY(0);
}
  stoptimer(&timer);
  showtimer(&timer);
  /* PRINT(); */
}

#include <stdio.h>
void print(int pi)
{
  printf("%d ", pi);
}
void printInt(int pi)
{
  printf("%d ", pi);
}
void printStr(char* ps)
{
  printf("%s", ps);
}
void printEol()
{
  printf("\n");
}

