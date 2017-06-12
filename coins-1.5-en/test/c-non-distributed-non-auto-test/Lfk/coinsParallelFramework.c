/* coinsParallelFramework.c : Dummy implementation of coinsParallelFramework.h */

#include "coinsParallelFrameWork.h"

int coinsThreadInit( int numberOfMaximumThreads)
{
  coinsNumberOfThreads = numberOfMaximumThreads;
  if (coinsNumberOfThreads <= 0) {
    printf(" Illegal value for coinsNumberOfThreads=%d assume 2\n");
  coinsNumberOfThreads = 2;
  }
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
    fromTemp = fromTemp + range;
  } 
}

void coinsThreadPostprocess(int numberOfThreads,
						long loopLength)
{
}

int coinsThreadForkForDoAll(
      coinsThread_t *thread,
      void *(*startRoutine)(int, long, long, void*, void *), 
      int threadId,
      long indexFrom, long indexTo, 
      void *red1, void *red2 )
{
  startRoutine(threadId, indexFrom, indexTo, red1, red2);
  return 0;
}
 
void coinsThreadPreprocessForDoAllThread()
{
}

void coinsThreadPostprocessForDoAllThread()
{
}

