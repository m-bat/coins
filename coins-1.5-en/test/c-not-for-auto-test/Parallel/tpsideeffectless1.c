/* tpsideeffectless1.c Use function declared as side-effect-less by pragma. */
#pragma optControl functionsWithoutSideEffect distance
#pragma parallel doAllFunc main 
#ifndef MAIN
#define MAIN
#endif
#include "coinsParallelFramework.h"
#include <math.h>
double distance(double px[], double py[], int pi );
double setData(double px[], double py[], int pn );
int printf(char*, ...);
double x[10000], y[10000];

int main()
{
  int i, n = 720;
  double t, sum = 0.0;
#pragma parallel init
  setData(x, y, n);
#pragma parallel doAll
  for (i = 0; i < n; i++) {
    sum = sum + distance(x, y, i);
  } 
  printf("\n ans = %f \n", sum);
#pragma parallel end
  return 0;
}

double setData(double px[], double py[], int pn )
{
  int i;
  double t;
  for (i = 0; i <= pn; i++) {
    t = ((double)(i))*3.1415926535898/((double)pn);
    px[i] = sin(t);
    py[i] = cos(t);
  } 
}

extern double sqrt(double);
double distance(double px[], double py[], int pi )
{
  double deltax = px[pi+1] - px[pi];
  double deltay = py[pi+1] - py[pi];
  return sqrt(deltax * deltax + deltay * deltay);
}
