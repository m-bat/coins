/* matMultTime.c Nakata mail 051021 */

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

#define N 500
#define L 500
#define M 500

typedef float matNL[N][L];
typedef float matLM[L][M];
typedef float matNM[N][M];
void multiply(matNM c, matNL a, matLM b)
{
        int i, j, k;
        float s;

        for (i = 0; i < N; i++)
                for (j = 0; j < M; j++) {
                        s = 0;
                        for (k = 0; k < L; k++) s += a[i][k] * b[k][j];
                        c[i][j] = s;
                }
}

#include <stdio.h>
#include <stdlib.h>

int main()
{
        int i, j;
        static matNL a;
        static matLM b;
        static matNM c;

        for (i = 0; i < N; i++)
                for (j = 0; j < L; j++)
                        a[i][j] = rand() / (RAND_MAX / 10 + 1);
        for (i = 0; i < L; i++)
                for (j = 0; j < M; j++)
                        b[i][j] = rand() / (RAND_MAX / 10 + 1);
  starttimer(&timer);
        multiply(c, a, b);

  stoptimer(&timer);
  showtimer(&timer);
  return 0;
}

