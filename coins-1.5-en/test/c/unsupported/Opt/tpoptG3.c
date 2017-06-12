/* tpoptG3.c -- Global optimization, multiple functions with arrays */

#include <stdio.h> /* SF030620 */

int i, j, k;
int x[10], y[10], z[10];
float xx[10], yy[10], ax, ay;
int s;

float average(float pa[], int pn)
{
  float sum;

  sum = 0.0;
  for (i = 0; i < pn; i++)
    sum = sum + pa[i];
  return sum / pn;
}

void swapx(int pi, int pj)
{
  int tmp;

  tmp   = x[pi];
  x[pi] = x[pj];
  x[pj] = tmp;
} 

int main()
{
  for (i = 0; i < 10; i++) {
    x[i] = i;
    y[i] = i + i;
    xx[i] = x[i] + 10;
    yy[i] = xx[i] + x[i];
  }
  ax = average(xx, 10);
  ay = average(yy, 10) + ax;
  swapx(3, 4);
  /* SF030620[ */
  printf("x[3]=%d ax=%f ay=%f return %f",x[3],ax,ay,x[3]+ax+ay); 
  /* SF030620] */
  return x[3] + ax + ay; 
}

