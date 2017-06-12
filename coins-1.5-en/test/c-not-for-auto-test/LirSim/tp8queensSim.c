/* Find all solutions to 8 queens prblem. */
/* Compile with print.c */
/* With simulation pragma */

#pragma simulate profileOff PRINT MAIN
void printInt(int p);   
void printStr(char* p);
void printEol();
int i;
int a[8];
int b[16];                   /* b[i + j] */
int c[16];                   /* c[i - j + 8] <-- { c[-7..7] }  */
int x[8];                    /* x[i] */
int callCount;

void PRINT()
{
  int k;

  for (k = 0; k < 8; k= k+1)	
    printInt(x[k] + 1);
  printStr(" Call count ");
  printInt(callCount);
  printEol();
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
  printStr("Call count ");
  printInt(callCount);
  printEol();
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

