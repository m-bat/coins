/* tpsoukanCore.c  Nakata mail 051120 */
/* This is derived from soukan.c removing time measurement statements and
   repetition for time measurement.
*/

#include <stdio.h>
#include <math.h>

#define NMAX  10 /**/
double x[NMAX], y[NMAX];

void corrcoef1(int n)
{
  int i;
  double sx, sy, sxx, syy, sxy, dx, dy;

  sx = sy = sxx = syy = sxy = 0;
  for (i = 0; i < n; i++) {  /*1*/ /* Stmt:2 HIR:22-nodes LIR:7,7 */
    sx += x[i];  sy += y[i];
  }
  sx /= n;  sy /= n;
  for (i = 0; i < n; i++) {  /*2*/ /* Stmt:5 HIR:42-nodes LIR:11,13 */
    dx = x[i] - sx;  dy = y[i] - sy;
    sxx += dx * dx;  syy += dy * dy;  sxy += dx * dy;
  }
  sxx = sqrt(sxx / (n - 1));
  syy = sqrt(syy / (n - 1));
  sxy /= (n - 1) * sxx * syy;
  printf("standard deviation %g %g  correlation %g\n", sxx, syy, sxy);
}

void corrcoef2(int n)
{
  int i;
  double sx, sy, sxx, syy, sxy;

  sx = sy = sxx = syy = sxy = 0;
  for (i = 0; i < n; i++) {  /*3*/ /* Stmt:5 HIR:54-nodes LIR:11 */
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
  printf("standard deviation %g %g  correlation %g\n", sxx, syy, sxy);
}

void corrcoef3(int n)
{
  int i;
  double sx, sy, sxx, syy, sxy, dx, dy;

  sx = sy = sxx = syy = sxy = 0;
  for (i = 0; i < n; i++) {    /*4*/ /* Stmt:7  HIR:86-nodes LIR:23,25 */
    dx = x[i] - sx;  sx += dx / (i + 1);
    dy = y[i] - sy;  sy += dy / (i + 1);
    sxx += i * dx * dx / (i + 1);
    syy += i * dy * dy / (i + 1);
    sxy += i * dx * dy / (i + 1);
  }
  sxx = sqrt(sxx / (n - 1));
  syy = sqrt(syy / (n - 1));
  sxy /= (n - 1) * sxx * syy;
  printf("standard deviation %g %g  correlation %g\n", sxx, syy, sxy); 
}

int main()
{
  int n;
  double t, u;
  int j;
  int loopCount;
  n = 0;
  srand(300);
  for ( n = 0;n < NMAX ;n++ ) {
      x[n] = rand()/2003.0;
      y[n] = rand()/2003.0;
      printf("n=%d,x[n]=%f,y[n]=%f\n",n,x[n],y[n]);
  }
  printf("Number of data %d\n", n);
  corrcoef1(n);
  corrcoef2(n);
  corrcoef3(n);
  return 0;
}

