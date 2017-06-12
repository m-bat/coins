/*=======================================================================*/
/*     Optimizing Compiler Benchmark Program                             */
/* Original: (in Fortran)                                                */
/*  "A new benchmark test to estimate optimization quality of compilers" */
/*  by M. Klerer, H. Liu, SIGPLAN Notices, Vol. 23, no. 3, pp. 63-72,    */
/*  (Mar. 1988).                                                         */
/*=======================================================================*/

#include <stdio.h>
#include <math.h>
#include <time.h>

#define loop     2
#define iw1   4160
#define iw2   4110
#define iw3    531
#define iw4    150
#define iw5    331
#define iw6     19
#define iw7      6
#define iw8    343
#define iw9    250
#define iw10    44
#define iw11    56

int   k, m, n;
float a, b, c, d, x;
/** float arr[100], arr1[40][40], arr2[40][40], arr3[40][40];
**/
float arr[101], arr1[41][41], arr2[41][41], arr3[41][41];

void init();
void struc1();
void struc2();
void struc3();
void struc4();
void struc5();
void struc6();
void struc7();
void struc8();
void struc9();
void struca();
void strucb();

main()
{
  int i, j;

  init();
  for (i = 1; i <= loop; i++) {
    for (j = 1; j <= iw1; j++)
      struc1();
    for (j = 1; j <= iw2; j++)
      struc2();
    for (j = 1; j <= iw3; j++)
      struc3();
    for (j = 1; j <= iw4; j++)
      struc4();
    for (j = 1; j <= iw5; j++)
      struc5();
    for (j = 1; j <= iw6; j++)
      struc6();
    for (j = 1; j <= iw7; j++)
      struc7();
    for (j = 1; j <= iw8; j++)
      struc8();
    for (j = 1; j <= iw9; j++)
      struc9();
    for (j = 1; j <= iw10; j++)
      struca();
    for (j = 1; j <= iw11; j++)
      strucb();
  }
  /*printf(" a %e b %e c %e d %e x %e %\n"); /* SF030609 */
  printf(" a %e b %e c %e d %e x %e\n",a,b,c,d,x); /* SF030609 */
  return 0;
}
void init()
{
  a = 1;
  b = 1;
  c = 1;
  d = 1;
  x = 1;
  for (k = 1; k <= 40; k++) {
    arr[k] = 1;
    for (m = 1; m <= 40; m++) {
      arr1[m][k] = 1;
      arr2[m][k] = 1;
    }
  }
}
void proc1()
{
  x = x+b;
}
void struc1()
{
#define is 100
  double pi;

  for (k = 0; k < is; k++)
  ;
  b = 100;
  a = a+b;
  pi= atan(1.0)*4;
  a = pi*b*b*k;
  c = 2*pi*b;
  d = (2*pi*a+b)/b;
  proc1();
}
void proc2()
{
  x = x+1;
}
void struc2()
{
  c = (-b+sqrt(x))/(2.0*a);
  d = (-b-sqrt(x))/(2.0*a);
  a = (arr[1]+arr[2])/(arr[1]*arr[1]+arr[2]*arr[2]);
  b = (arr[1]-arr[2])/(arr[1]*arr[1]+arr[2]*arr[2]);
  k = 1;
  n = 100;
  a = arr[k];
  arr[n] = b;
  b = arr[k];
  c = arr[k];
  a = b/c;
  proc2();
  d = b/c;
}
void proc3()
{
  d = b/(c+1);
}
void struc3()
{
  if (k == n)
    x = (k-a)/(b+100);
  else
    x = (k-b+c)/(b+100);
  if ((a+b+c) > 9) {
    x = a+b+c-10;
    c = 1;
  }
  else {
    x = a+b+c;
    c = 0;
  }
  n = 40;
  for (k = 1; k <= 100; k++) {
    n = n-1;
    if (n <= 0)
      n = 40;
    if (arr2[n][1] == arr[k])
      goto L100;
  }
 L100:
  if (arr2[n][1] == arr[6])
    a = a+1;
  a = b/(c+1);
  proc3();
}
void struc4()
{
  a = x;
  b = a+c;
  a = a+b;
  d = a+b;
}
void struc5()
{
  for (k = 1; k <= 50; k++)
    arr[k] = arr[k]+sqrt(fabs(2*a+1));
  for (k = 50; k >= 1; k--) {
    if (k < n) {
      a = b*c;
      arr[k] = arr[k]+a;
    }
  }
}
void struc6()
{
  for (k = 1; k <= 25; k++) {
    n = 4*k;
    arr[n] = a;
  }
  k = 0;
  for (n = 2; n <= 40; n++) {
    k = k+1;
    a = arr[n];
    arr[k] = k+a;
    arr2[k][1] = k;
  }
}
void struc7()
{
  a = 3*b;
  a = b/2;
  a = pow(a,6);
  a = a*b;
  for (k = 1; k <= 10; k++) {
    for (m = 1; m <= 10; m++)
      if (arr1[m][k] < 0.0)
        arr1[m][k] = -arr1[m][k];
  }
}
void struc8()
{
  b = 100;
  a = a+100;
}
void struc9()
{
  float arr4[100];

  for (k = 1; k <= 5; k++) {
    for (m = 1; m <= 5; m++)
      arr3[m][k] = 0;
  }
  for (k = 1; k <= 5; k++) {
    for (m = 1; m <= 5; m++) {
      for (n = 1; n <= 5; n++)
        arr3[m][k] = arr3[m][k]+arr1[n][k]*arr2[m][n];
    }
  }
  for (k = 1; k <= 5; k++) {
    arr[k] = a;
    arr2[k][1] = b;
  }
  for (k = 1; k <= 5; k++) {
    if (arr[k] == arr2[k][1])
      arr1[k][1] = k+1;
    for (m = 1; m <= 5; m++) {
      if (arr[k] == arr2[m][1])
        arr1[k][1] = m;
    }
  }
  for (k = 1; k <= 5; k++) {
    arr3[k][1] = 0;
    for (m = 1; m <= 5; m++) {
      if (arr2[k][1] == arr[m])
        arr3[k][1] = arr3[k][1]+1;
    }
  }
  for (k = 1; k <= 5; k++) {
    if (arr3[k][k] != 0)
      n = 0;
    for (m = 1; m <= 5; m++) {
      if (arr2[k][1] == arr[m]) {
        n = n+1;
        arr4[n] = m;
      }
    }
  }
}
void pause1()
{
  for (k = 1; k <= 10; k++)
  ;
}
void pause2()
{
  for (k = 1; k <= 10; k++)
  ;
}
void struca()
{
  a = a+0;
  a = a-0;
  b = b*1;
  b = b/1;
  goto L100;
 L100:
  a = a+1;
  pause1();
  pause2();
}
void strucb()
{
  a = 5;
  if (a != 5) {
    b = a+b;
    c = b+c;
    d = d+b;
  }
}
