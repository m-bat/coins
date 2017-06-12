/* rotate.c 
**   Rotate figures in affine space  (Watanabe)
*/
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
/*
#include <stddef.h>
#include <sys/time.h>
#include <sys/resource.h>
*/

/*
  x2 =  x1 * cos(a) + y1 * sin(a);
  y2 = -x1 * sin(a) + y1 * cos(a);
*/
#define COUNT 1000
#define LOOP  1000

double px[COUNT], py[COUNT];
double qx[COUNT], qy[COUNT];
double angle, cos_a, sin_a;

/*--------*/
void rotate1(
    double px1[], double py1[], 
    double px2[], double py2[],
    int    pn,    double pa)
{
  int i;

  for (i = 0; i < pn; i++) {
    px2[i] =  px1[i]*cos(pa) + py1[i]*sin(pa);
    py2[i] = -px1[i]*sin(pa) + py1[i]*cos(pa);
  }
}

/*--------*/
void rotate2(
    double px1[], double py1[], 
    double px2[], double py2[],
    int   pn,    
    double cosa, double sina)
{
  int i;

  for (i = 0; i < pn; i++) {
    px2[i] =  px1[i]*cosa + py1[i]*sina;
    py2[i] = -px1[i]*sina + py1[i]*cosa;
  }
}

/*--------*/
void rotate3(
    double px1[], double py1[], 
    double px2[], double py2[])
{
  int i;

  for (i = 0; i < COUNT; i++) {
    px2[i] =  px1[i]*0.9808047 + py1[i]*0.1949927;
    py2[i] = -px1[i]*0.1949927 + py1[i]*0.9808047;
  }
}

/*--------*/
void rotate4(
    double px1[], double py1[], 
    double px2[], double py2[])
{
  int i;

  for (i = 0; i < COUNT; i = i + 4) {
    px2[i+0] =  px1[i+0]*0.9808047 + py1[i+0]*0.1949927;
    py2[i+0] = -px1[i+0]*0.1949927 + py1[i+0]*0.9808047;
    px2[i+1] =  px1[i+1]*0.9808047 + py1[i+1]*0.1949927;
    py2[i+1] = -px1[i+1]*0.1949927 + py1[i+1]*0.9808047;
    px2[i+2] =  px1[i+2]*0.9808047 + py1[i+2]*0.1949927;
    py2[i+2] = -px1[i+2]*0.1949927 + py1[i+2]*0.9808047;
    px2[i+3] =  px1[i+3]*0.9808047 + py1[i+3]*0.1949927;
    py2[i+3] = -px1[i+3]*0.1949927 + py1[i+3]*0.9808047;
  }
}

/*--------*/
int main()
{
  int i, j, k;

  for (i = 0; i < COUNT; i++) {
    px[i] = i + 1;
    py[i] = 2 * i + 1;
  }
  angle = 3.14/16.0;
  cos_a = cos(angle);
  sin_a = sin(angle);

  printf("rotate1: No optimization angle %e cos %e sin %e\n", 
          angle, cos_a, sin_a);
  for (j = 0; j < LOOP; j++)
    rotate1(px, py, qx, qy, COUNT, angle);
  printf("    qx[0] %e  qy[0] %e \n", qx[0], qy[0]);
  printf("rotate2: Change cos/sin to constant \n");
  for (j = 0; j < LOOP; j++)
    rotate2(px, py, qx, qy, COUNT, cos_a, sin_a);
  printf("    qx[1] %e  qy[1] %e \n", qx[1], qy[1]);

  printf("rotate3: Embed cos/sin and count as constants \n");
  for (j = 0; j < LOOP; j++)
    rotate3(px, py, qx, qy);
  printf("    qx[2] %e  qy[2] %e \n", qx[2], qy[2]);

  printf("rotate4: Expand loop 4 times and embed constants \n");
  for (j = 0; j < LOOP; j++)
    rotate4(px, py, qx, qy);
  printf("    qx[3] %e  qy[3] %e \n", qx[3], qy[3]);
  return 0;
}

