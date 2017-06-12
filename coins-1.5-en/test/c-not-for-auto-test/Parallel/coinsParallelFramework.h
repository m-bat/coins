/* coinsParallelFramework.h  pthread version */
/* java -cp classes coins.driver.Driver -S -coins:parallelDoAll=4,hir2c,trace=Para1.3/Flow.3/HIR.1 test/c-not-for-auto-test/Parallel/lparallel6.c */

#ifndef COINS_PARALLEL_FRAMEWORK_H
#define COINS_PARALLEL_FRAMEWORK_H

#ifndef COINS_MAX_NUMBER_OF_THREADS
#define COINS_MAX_NUMBER_OF_THREADS 4
#endif

#ifdef MAIN
#define Extern
#else
#define Extern extern
#endif

#include <sys/types.h>

/** typedef struct __pthread_t {char __dummy;} *pthread_t; /* defined in type.h */

typedef struct {
  int id;
  pthread_t th;
} coinsThread_t;

typedef struct {
  int id, from, to, step;
} coinsThreadLoopParam;

Extern coinsThread_t coinsThread[COINS_MAX_NUMBER_OF_THREADS];

Extern int coinsThreadInit( int numberOfMaximumThreads);

Extern int coinsThreadFork(coinsThread_t *thread,
                     void *(*startRoutine)(void *), void *argv);
Extern int coinsThreadJoin(coinsThread_t *cth);

Extern int coinsThreadEqual(coinsThread_t *cth1, coinsThread_t *cth2);

Extern int coinsThreadSelfId();

Extern coinsThread_t * coinsThreadSelf();

Extern int coinsNumberOfThreads;
Extern int coinsThreadEnd();
Extern int coinsThreadIdArray[COINS_MAX_NUMBER_OF_THREADS];
Extern int coinsThreadSelfIdOfMaster;
Extern void 
coinsThreadPreprocessForDoAllLoop(int numberOfThreads, 
                long loopLength, long *indexFrom, long *indexTo);
Extern void coinsThreadPostprocess(int totalNumberOfThreads,
                long loopLength);
Extern int coinsThreadForkForDoAll(coinsThread_t *thread,
                void *(*startRoutine)(int, long, long, void*, void *), 
                int threadId, long indexFrom, long indexTo, void*, void* ); 
Extern void coinsThreadPreprocessForDoAllThread();

Extern void coinsThreadPostprocessForDoAllThread();

#endif 

