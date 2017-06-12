/* tpprimeL.c: Get prime numbers (using local variables) */
/* Compile with print.c */


#include<unistd.h>
#include<time.h>
#include <assert.h>
#include <stdio.h>
#include <sys/time.h>
#include <unistd.h>
#include<sys/resource.h>

#include "print.h" /* SF030609 */

#define SIZE 10000000

/*
int candidat, quotient, remaindr, index, nth, primenum, loopend ;
*/
int primeNumbers[SIZE] ;
void print(int pvar); /* SF030609 */

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
/*  printf("%f sec.\n", timer->diff.tv_sec + timer->diff.tv_usec / 1.0e6);
*/
  printf("%f sec.\n", sum.tv_sec + sum.tv_usec / 1.0e6);
}

void getPrime(int primevec[SIZE], int count)
{
 int candidat, quotient, remaindr, index, nth, primenum, loopend ; /**/
 primevec[0] = 2 ;
 nth = 1 ;
 candidat = 3 ;
/** starttimer(&timer); */
 while (nth<count) {
   remaindr = 1 ;
   index = 0 ;
   loopend = 0 ;
   while(loopend==0) {
     primenum = primevec[index] ;
     quotient = candidat / primenum ;
     remaindr = candidat - quotient*primenum ;
     if (remaindr==0)
       loopend = 1 ;
     if (quotient*quotient<candidat)
       loopend = 1 ;
     index = index + 1 ;
     if (index>=nth)
       loopend = 1 ;
   }
   if (remaindr != 0) {
     primevec[nth] = candidat ;
     nth = nth + 1 ;
   }
   candidat = candidat + 2 ;
 }
/**  stoptimer(&timer); */
 nth   = 0 ;
 while (nth<count) {
/*   print(primevec[nth]) ;
*/
   nth   = nth + 1 ;
 }
 return;
}

int main()
{
  int i;  /**/
  int loopCount;
  starttimer(&timer);
  for (loopCount = 0; loopCount < 5; loopCount++) {
    init();
    /* starttimer(&timer); */
    for (i=0; i<10; i++) { /**/
      primeNumbers[0] = 2 ;
      getPrime(primeNumbers, SIZE);
    } /**/
  }
  stoptimer(&timer); /**/
  showtimer(&timer);
  return 0;
}
