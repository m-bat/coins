/* tppredefined3.c 
  Use function that is specified to be side-effect-less by pragma. 
*/
#pragma optControl functionsWithoutSideEffect display
#pragma optControl functionsWithSideEffect cosf

#pragma parallel doAllFunc predef 
#ifndef MAIN
#define MAIN
#endif
#include "coinsParallelFramework.h"
int printf(char*, ...);

void display(int pNumber )
{
  printf(" %d", pNumber);
}

extern float sinf(float); /****/

float aa[100];

float predef()
{
  int i;
  float sum = 0.0;
  float x   = 0.0;
#pragma parallel doAll
  for (i = 0; i < 100; i++) {
    x = ((float)i)/10.0;
    aa[i] = sinf(x);
    display(i);
    sum = sum + aa[i];
  } 
  return sum;
}

int main()
{
  float ans;
#pragma parallel init
  ans = predef();
  printf("\n ans = %f \n", ans);
#pragma parallel end
  return 0;
}
