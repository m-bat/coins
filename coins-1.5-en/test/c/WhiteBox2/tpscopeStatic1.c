/* tpscopeStatic1.c:  static variables in block (Yamano mail 030530) */ 
/*                    See tpcsopeBlock1.c */

int printf(char*, ...);

long Cero = 0, Uno = 1, Dos = 2, Tres = 3;
int  statusFlag = 0;

int main()
{
  static long     i = 0;
  if (i = Uno) {
    static long     i;
    printf("i %d \n", i);
    if (i != Cero)
      statusFlag++;
    if (i = Dos) {
      static long     i;
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
  printf("flag %d \n", statusFlag);
  return 0;
}


