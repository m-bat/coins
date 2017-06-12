/* Find all solutions to 8 queens prblem.               */
/* Same as that of queen.c in benchTime5 except for 
   time measurement statements are eliminated. */

/* 
#define SIZE 14
*/
#define SIZE 8
void printInt(int p);   
void printStr(char* p);
void printEol();
int i;
int a[SIZE];
int b[SIZE*2];                   /* b[i + j] */
int c[SIZE*2];                   /* c[i - j + 8] <-- { c[-7..7] }  */
int x[SIZE];                    /* x[i] */
int callCount;

void PRINT()
{
  int k;

  for (k = 0; k < SIZE; k= k+1)	
    printInt(x[k] + 1);
  printStr(" Call count ");
  printInt(callCount);
  printEol();
}

void TRY(int i)
{
  int j;
 
  for (j = 0; j < SIZE; j= j+1) {
    if ((a[j] == 1) && (b[i+j] == 1) && (c[i-j+SIZE] == 1)) {
      x[i] = j;
      a[j]   = 0;
      b[i+j] = 0;
      c[i-j+SIZE] = 0;
      if (i < SIZE-1) {
        callCount = callCount+1;
        TRY(i+1);
      }
      a[j]   = 1;
      b[i+j] = 1;
      c[i-j+SIZE] = 1;
    }
  }
}

main()
{
int loopCount;

  for (i = 0; i < SIZE; i= i+1)      
    a[i] = 1;
  for (i = 0; i < SIZE*2; i= i+1)
    b[i] = 1;
  for (i = 0; i < SIZE*2; i= i+1)     
    c[i] = 1;
  callCount = 1;
  for (loopCount = 0; loopCount < 10; loopCount++) {
    TRY(0);
  }
  PRINT(); 
}

#include <stdio.h>
void print(int pi)
{
  printf("%d ", pi);
}
void printInt(int pi)
{
  printf("%d ", pi);
}
void printStr(char* ps)
{
  printf("%s", ps);
}
void printEol()
{
  printf("\n");
}

