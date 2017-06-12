#pragma parallel doAllFunc predef 
#ifndef MAIN
#define MAIN
#endif
#include "coinsParallelFramework.h"
#include <stdio.h>

extern double sin(double); /****/

float aa[100];

float predef()
{
  int i;
  float sum = 0.0;
  float x   = 0.0;
#pragma parallel doAll
  for (i = 0; i < 100; i++) {
    x = ((float)i)/10.0;
    aa[i] = sin(x);
    sum = sum + aa[i];
  } 
  return sum;
}

int main()
{
  float ans;
#pragma parallel init
  ans = predef();
  printf("ans = %f \n", ans);
#pragma parallel end
  return 0;
}
