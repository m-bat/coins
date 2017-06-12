/* tpscopeStatic2.c:  static variables in block (Yamano mail 030530) */ 

int printf(char*, ...);

long Cero = 0, Uno = 1, Dos = 2, Tres = 3;
int  statusFlag = 0;
static int jj;
static int jj;

int main()
{
  static long     i = 0;
  jj = 3;
  if (i = Uno) {
    static long     i = 2;
    printf("i %d \n", i);
    if (i != Cero)
      statusFlag++;
    if (i = Dos) {
      static long     i = 3;
      if (i != Cero)
        statusFlag++;
      printf("i %d \n", i);
      if (i = Tres) {
        static long     i;
        printf("i %d \n", i);
      }
      printf("i %d \n", i);
    }
    printf("i %d \n", i);
  }
  printf("flag %d %d \n", statusFlag, jj);
  return 0;
}


