/** fpbench1.c by Nishioka (gettimeofday)
    Get square root
 */

#include <assert.h>
#include <stdio.h>
#include <sys/time.h>
#include <unistd.h>

struct timer {
  struct timeval start;
  struct timeval end;
  struct timeval diff;
};

static void starttimer(struct timer* timer) {
  gettimeofday(&timer->start, NULL);
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

int main(int argc, char** argv)
{ 
  double x = 0.0, r[10];
  int i, j;
  struct timer timer;
  starttimer(&timer);
  /* Newton Method ¤Ç 2 ¡Á 9 ¤Î¼«¾èº¬¤òµá¤á¤ë */
  for (i = 2; i < 9; i ++) {
    /* for (j = 0; j < 10000000; j ++) */
    for (j = 0; j < 100000; j ++)   /***/
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
% gcc -O4 -Wall -static -g -o fpbench fpbench.c
% ./fpbench 
root 2 = 1.414214
root 3 = 1.732051
root 4 = 2.000000
root 5 = 2.236068
root 6 = 2.449490
root 7 = 2.645751
root 8 = 2.828427
6.343667 sec.
%
**/

