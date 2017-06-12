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

/** #define SIZE  30000 */
#define SIZE  100000  /**/
/* #define SIZE 10 */
int A[SIZE];

void initdata(){
  int i;
  srand(200);
  for(i = 0; i< SIZE; i++){
    A[i] = rand();
  }
}

/* InsertionSort.c */
int main(){
    int j;
    int i;
    int key;
int loopCount;

    starttimer(&timer);
  for (loopCount = 0; loopCount < 5; loopCount++) {
    init();

    initdata();

    j = 1;

    key = 0;
    i = 0;
    /* starttimer(&timer); */
    do{
      if(j < SIZE){
        key = A[j];
        i = j - 1;
        
        do{
          if(i >= 0 && key < A[i]){
            A[i + 1] = A[i];
            i = i - 1;
          }else{
            break;
          }
        }while(1);
        
        A[i + 1] = key;
        j = j + 1;
      }else{
        break;
      }
    }while(1);
  }  
    stoptimer(&timer);

    /* uncomment by FUKUOKA Takeaki */
/*
    for(i = 0; i < SIZE; i++){
      printf("A[%d] = %d\n ",i,A[i]);
    }
    printf("\n");
*/
    showtimer(&timer);

    return(0);
}
