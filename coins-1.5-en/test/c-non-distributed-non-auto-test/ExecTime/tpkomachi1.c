/* tpkomachi.c:  Komachi calculation for execution time measurement.
   Insert operators (+ and -) between numbers 1 2 3 4 5 6 7 8 9 
   so that the value of the resultant expression is 100.
   Haruhiko Okumura: Algorithm dictionary written in C, 
     Gijutsu hyoronsha, 1991. 
*/
#include <stdio.h>
#include<unistd.h>
#include<time.h>
#include <assert.h>
#include <sys/time.h>
#include <unistd.h>
#include<sys/resource.h>

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
  printf("%f sec.\n", timer->diff.tv_sec + timer->diff.tv_usec / 1.0e6);
}

int printCount = 0; /***/

main() {
  int i,s,sign[10];
  long n,x;
  int j; /**/

  int loopCount;
  starttimer(&timer); /**/
  for (loopCount = 0; loopCount < 40; loopCount++) {
 for (j=0; j<5000; j++) { /**/ 
  for(i=1;i<=9;i++)
    sign[i]=-1;
  do {
    x=n=0;
    s=1;
    for(i=1;i<=9;i++) {
      if(sign[i]==0)
	n=10*n+i;
      else {
	x+=s*n;
	s=sign[i];
	n=i;
      }
    }
    x+=s*n;
    /***/
    if(x==100)
      printexp(sign);
    /***/
    i=9;
    s=sign[i]+1;
    while(s>1) {
      sign[i]=-1;
      i--;
      s=sign[i]+1;
    }
    sign[i]=s;
  }while(sign[1]<1);
 }  /**/
}
  stoptimer(&timer); /**/
  showtimer(&timer); /**/ 
}

/**/
printexp(int sign[]) {
  int i;
  if (printCount > 20)
    return;
  printCount++;
  for(i=1;i<=9;i++) {
    if(sign[i]==1) printf("+");
    if(sign[i]==-1) printf("-");
    printf("%d",i);
  }
  printf("=100\n");
}
/**/
