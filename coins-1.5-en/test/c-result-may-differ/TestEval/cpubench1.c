/** cpubench1.c by Nishioka (gettimeofday)
    Decompose integral number into sum of up to 4 square numbers
**/
#include <assert.h>
#include <stdio.h>
#include <sys/time.h>
#include <unistd.h>

#define bool int
#define true (0==0)
#define false (!true)

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

static bool sub(int x, int *a, int t, int n) {
  if (n == 4) {
    return false;
  } else {
    for (a[n] = 0; a[n] * a[n] < x - t; a[n] ++) ;
    if (a[n] * a[n] == x - t) {
      return true;
    } else {
      while (a[n] > 0) {
	-- a[n];
	if (sub(x, a, t + a[n] * a[n], n +1)) return true;
      }
      return false;
    }
  }
}

/* #define SIZE 100000 */
#define SIZE 1000

int main(int argc, char** argv) {
  static int a[SIZE][4];
  int i;
  struct timer timer;
  starttimer(&timer);
  /* 0 〜 SIZE - 1 の整数を, 4 つ以下の平方数の和に分解する */
  for (i = 0; i < SIZE; i ++) {
    if (! sub(i, a[i], 0, 0)) {
      printf("%d cannot be expressed as a sum of 4 or less square numbers.\n", i);
    }
  }
  stoptimer(&timer);
  for (i = 0; i < SIZE; i ++) {
    assert(i == a[i][0] * a[i][0] + a[i][1] * a[i][1] + a[i][2] * a[i][2] + a[i][3] * a[i][3]);
  }
  showtimer(&timer);
  return 0;
}
/**
% gcc -O4 -Wall -static -g -o cpubench cpubench.c
% ./cpubench 
51.176321 sec.
**/

