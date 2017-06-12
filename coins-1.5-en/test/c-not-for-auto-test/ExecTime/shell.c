#include <ctype.h>
#include <string.h>
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
//  printf("%f sec.\n", timer->diff.tv_sec + timer->diff.tv_usec / 1.0e6);
  printf("%f sec.\n", sum.tv_sec + sum.tv_usec / 1.0e6);
}

void shellsort(int v[], int n)
{
	int gap, i, j, temp;
	for (gap = n/2; gap > 0; gap /= 2)
		for (i = gap; i < n; i++)
			for (j=i-gap; j>=0 && v[j]>v[j+gap]; j-=gap) {
				temp = v[j];
				v[j] = v[j+gap];
				v[j+gap] = temp;
			}
}

#define SIZE 2000000

main()
{
	int data[SIZE];
	int i;
	init();
	srand(500);
        for(i=0; i<SIZE; i++){
		data[i] = rand();
	}
        starttimer(&timer);
	shellsort(data, SIZE);
	stoptimer(&timer);    
/**
  	for (i = 0; i < SIZE; i++)
  		printf("%d\n",data[i]);
**/
	showtimer(&timer);
}
