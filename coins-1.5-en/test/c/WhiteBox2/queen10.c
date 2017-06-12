/* queen10.c */
/* Find all solutions to 10 queens problem.               */
/* Same as that of queen.c in benchTime5 except for time measurement statements 
   and print statement.
 */

/* 
#define SIZE 14
*/
#define SIZE 10
int printf(char*, ...);
int i;
int a[SIZE];
int b[SIZE*2];                   /* b[i + j] */
int c[SIZE*2];                   /* c[i - j + 8] <-- { c[-7..7] }  */
int x[SIZE];                    /* x[i] */
int callCount;
int printCount = 0;

void PRINT()
{
  int k;
  if (printCount > 10)
    return;
  printCount++;
  for (k = 0; k < SIZE; k= k+1)	
    printf(" %d", x[k] + 1);
  printf(" Call count %d\n", callCount);
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
      } else { PRINT(); } 
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
    TRY(0);
  printf(" Total call count %d\n", callCount);
}


