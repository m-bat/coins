/* coinsParallelFrameworkDbg.c : Implementation of coinsParallelFramework.h */
/*   Debug mode version */

int printf(char*, ... );

#include "coinsParallelFrameWork.h"

int coinsThreadInit( int numberOfMaximumThreads)
{
  coinsNumberOfThreads = numberOfMaximumThreads;
  if (coinsNumberOfThreads <= 0) {
    printf(" Illegal value for coinsNumberOfThreads=%d assume 2\n");
  coinsNumberOfThreads = 2;
  }
  printf("coinsThreadInit = %d\n", coinsNumberOfThreads);
  return coinsNumberOfThreads;
}

int coinsThreadFork(coinsThread_t *thread,
			void *(*startRoutine)(void *), void *argv)
{
  return 0;
}

int coinsThreadJoin(coinsThread_t *cth)
{
  return 0;
}

int coinsThreadEqual(coinsThread_t *cth1, coinsThread_t *cth2)
{
  return 0;
}

int coinsThreadSelfId()
{
  return 0;
}

coinsThread_t * coinsThreadSelf()
{
  return 0;
}

/* Added */
int coinsThreadEnd()
{
  return 0;
}

void 
coinsThreadPreprocessForDoAllLoop(
    int numberOfThreads, 
    long loopLength, long *indexFrom, long *indexTo)
{
  long range, fromTemp, toTemp, i;
  printf(" coinsThreadPreprocessForDoAllLoop numberOfThreads=%d loop length=%d\n", 
          numberOfThreads, loopLength);
  if (coinsNumberOfThreads <= 0) {
    printf(" Illegal value for coinsNumberOfThreads=%d assume 2\n");
    numberOfThreads = 2;
  }
  range = loopLength / numberOfThreads;
  fromTemp = 0;
  for (i = 0; i < numberOfThreads; i++) {
    toTemp = fromTemp + range;
    if (i == numberOfThreads - 1)
      toTemp = loopLength;
    *indexFrom = fromTemp;
    *indexTo   = toTemp;
    indexFrom++;
    indexTo++;
    printf(" index from %d  to %d \n", fromTemp, toTemp);
    fromTemp = fromTemp + range;
  } 
}

void coinsThreadPostprocess(int numberOfThreads,
						long loopLength)
{
  printf("coinsThreadPostprocess numberOfThread %d loopLength %d \n",
          numberOfThreads, loopLength);
}

int coinsThreadForkForDoAll(
      coinsThread_t *thread,
      void *(*startRoutine)(int, long, long, void*, void *), 
      int threadId,
      long indexFrom, long indexTo, 
      void *red1, void *red2 )
{
  printf("coinsThreadForkForDoAll id %d index from %d  to %d \n",
          threadId, indexFrom, indexTo);
  startRoutine(threadId, indexFrom, indexTo, red1, red2);
  return 0;
}
 
void coinsThreadPreprocessForDoAllThread()
{
  printf("coinsThreadPreprocessForDoAllThread  \n");
}

void coinsThreadPostprocessForDoAllThread()
{
  printf("coinsThreadPostprocessForDoAllThread  \n");
}

