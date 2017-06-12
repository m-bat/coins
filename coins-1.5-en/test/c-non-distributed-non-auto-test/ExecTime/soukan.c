#include <stdio.h>
#include <math.h>
#include<unistd.h>
#include<time.h>
#include <assert.h>
#include <stdio.h>
#include <sys/time.h>
#include <unistd.h>
#include<sys/resource.h>
#include<stdlib.h>
//#define NMAX 10
#define NMAX  20000000
double x[NMAX], y[NMAX];

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

void corrcoef1(int n)
{
	int i;
	double sx, sy, sxx, syy, sxy, dx, dy;

	sx = sy = sxx = syy = sxy = 0;
	for (i = 0; i < n; i++) {
		sx += x[i];  sy += y[i];
	}
	sx /= n;  sy /= n;
	for (i = 0; i < n; i++) {
		dx = x[i] - sx;  dy = y[i] - sy;
		sxx += dx * dx;  syy += dy * dy;  sxy += dx * dy;
	}
	sxx = sqrt(sxx / (n - 1));
	syy = sqrt(syy / (n - 1));
	sxy /= (n - 1) * sxx * syy;
//	printf("…∏Ω‡ –∫π %g %g  ¡Í¥ÿ∑∏øÙ %g\n", sxx, syy, sxy);
}

void corrcoef2(int n)
{
	int i;
	double sx, sy, sxx, syy, sxy;

	sx = sy = sxx = syy = sxy = 0;
	for (i = 0; i < n; i++) {
		sx += x[i];  sy += y[i];
		sxx += x[i] * x[i];
		syy += y[i] * y[i];
		sxy += x[i] * y[i];
	}
	sx /= n;  sxx = (sxx - n * sx * sx) / (n - 1);
	sy /= n;  syy = (syy - n * sy * sy) / (n - 1);
	if (sxx > 0) sxx = sqrt(sxx);  else sxx = 0;
	if (syy > 0) syy = sqrt(syy);  else syy = 0;
	sxy = (sxy - n * sx * sy) / ((n - 1) * sxx * syy);
//	printf("…∏Ω‡ –∫π %g %g  ¡Í¥ÿ∑∏øÙ %g\n", sxx, syy, sxy);
}

void corrcoef3(int n)
{
	int i;
	double sx, sy, sxx, syy, sxy, dx, dy;

	sx = sy = sxx = syy = sxy = 0;
	for (i = 0; i < n; i++) {
		dx = x[i] - sx;  sx += dx / (i + 1);
		dy = y[i] - sy;  sy += dy / (i + 1);
		sxx += i * dx * dx / (i + 1);
		syy += i * dy * dy / (i + 1);
		sxy += i * dx * dy / (i + 1);
	}
	sxx = sqrt(sxx / (n - 1));
	syy = sqrt(syy / (n - 1));
	sxy /= (n - 1) * sxx * syy;
//	printf("…∏Ω‡ –∫π %g %g  ¡Í¥ÿ∑∏øÙ %g\n", sxx, syy, sxy);
}


int main()
{
	int n;
	double t, u;

	n = 0;
        srand(300);
	for ( n = 0;n < NMAX ;n++ ) {
	    x[n] = rand()/2003.0;
	    y[n] = rand()/2003.0;
	    //printf("n=%d,x[n]=%f,y[n]=%f\n",n,x[n],y[n]);
	}
	printf("•«°º•ø§Œ∑ÔøÙ %d\n", n);
	starttimer(&timer);
	corrcoef1(n);
	corrcoef2(n);
	corrcoef3(n);
	stoptimer(&timer);
	showtimer(&timer);
	return EXIT_SUCCESS;
}

