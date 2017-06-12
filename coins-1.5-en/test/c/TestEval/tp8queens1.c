/* Find all solutions to 8 queens problem. */
/* originally tp8queens.c                  */
/* modified by M. Sassa on July 13, 2002   */

#include <stdio.h>

int i;
int a[8];
int b[16];                   /* b[i + j] */
int c[16];                   /* c[i - j + 8] <-- { c[-7..7] }  */
int x[8];                    /* x[i] */
int callCount;
int number = 0;

void PRINT()
{
  int k;

  number++;
  printf("solution no. %2d : ", number);
  for (k = 0; k < 8; k= k+1)	
    printf("%d  ", x[k] + 1);
  printf(": Call count = %d\n", callCount);
}

void TRY(int i)
{
  int j;
 
  for (j = 0; j < 8; j= j+1) {
    if ((a[j] == 1) && (b[i+j] == 1) && (c[i-j+8] == 1)) {
      x[i] = j;
      a[j]   = 0;
      b[i+j] = 0;
      c[i-j+8] = 0;
      if (i < 7) {
        callCount = callCount+1;
        TRY(i+1);
      }else 
        PRINT();
      a[j]   = 1;
      b[i+j] = 1;
      c[i-j+8] = 1;
    }
  }
}

main()
{
  for (i = 0; i < 8; i= i+1)      
    a[i] = 1;
  for (i = 0; i < 8*2; i= i+1)
    b[i] = 1;
  for (i = 0; i < 8*2; i= i+1)     
    c[i] = 1;
  callCount = 1;
  TRY(0);
  printf("Call count = %d\n", callCount);
}


