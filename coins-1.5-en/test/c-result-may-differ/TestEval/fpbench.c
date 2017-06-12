/*  cat fpbench.c  by Nishioka  (getrusage)
    Get square root
**/

#include <assert.h>
#include <stdio.h>
#include <sys/resource.h>
#include <sys/time.h>
#include <unistd.h>

struct timer {
  struct timeval start;
  struct timeval end;
  struct timeval diff;
};

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
#if 0
  gettimeofday(&timer->start, NULL);
#else
  struct rusage r_usage;

  getrusage(RUSAGE_SELF, &r_usage);
  addtimeval(&r_usage.ru_utime, &r_usage.ru_stime, &timer->start);
#endif
}

static void stoptimer(struct timer* timer) {
#if 0
  gettimeofday(&timer->end, NULL);
#else
  struct rusage r_usage;

  getrusage(RUSAGE_SELF, &r_usage);
  addtimeval(&r_usage.ru_utime, &r_usage.ru_stime, &timer->end);
#endif
  subtracttimeval(&timer->end, &timer->start, &timer->diff);
}

static void showtimer(struct timer* timer) {
  printf("%f sec.\n", timer->diff.tv_sec + timer->diff.tv_usec / 1.0e6);
}

int main(int argc, char** argv) {
  double x = 0.0, r[10];
  int i, j;
  struct timer timer;
  starttimer(&timer);
  /* Newton Method ¤Ç 2 ¡Á 9 ¤Î¼«¾èº¬¤òµá¤á¤ë */
  for (i = 2; i < 9; i ++) {
    /* for (j = 0; j < 10000000; j ++)  */
    for (j = 0; j < 100000; j ++)    /***/
    {
      x = x - (x * x - i) / (double)i;
    }
    r[i] = x;
  }
  stoptimer(&timer);
  for (i = 2; i < 9; i ++) {
    assert(i - r[i] * r[i] < 1e-6);
    printf ("root %d = %f\n", i, r[i]);
  }
  showtimer(&timer);
  return 0;
}
/**
% cc -O    fpbench.c   -o fpbench
% fpbench 
root 2 = 1.414214
root 3 = 1.732051
root 4 = 2.000000
root 5 = 2.236068
root 6 = 2.449490
root 7 = 2.645751
root 8 = 2.828427
2.430000 sec.

% gcc -O4 -Wall -g  -static  fpbench.c   -o fpbench
% fpbench 
root 2 = 1.414214
root 3 = 1.732051
root 4 = 2.000000
root 5 = 2.236068
root 6 = 2.449490
root 7 = 2.645751
root 8 = 2.828427
2.420000 sec.

% coinscc -O4 -Wall -g  -static  fpbench.c   -o fpbench

Recovered error 1015: TypeSize is not evaluable <STRUCT __FILE>
fpbench.c:88 bad operand
%

**/

