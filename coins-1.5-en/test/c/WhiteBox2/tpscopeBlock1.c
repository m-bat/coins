/* tpscopeBlock1.c:  variables in block (Yamano mail 030530) */ 

int printf(char*, ...);

long Cero = 0, Uno = 1, Dos = 2, Tres = 3;
int  statusFlag = 0;

int main()
{
  long     i = 0;
  if (i = Uno) {
    long     i = 9;
    printf("i %d \n", i);
    if (i != Cero)
      statusFlag++;
    if (i = Dos) {
      long     i = 8;
      if (i != Cero)
        statusFlag++;
      printf("i %d \n", i);
      if (i = Tres) {
        long     i = 7;
        printf("i %d \n", i);
      }
      printf("i %d \n", i);
    }
    printf("i %d \n", i);
  }
  printf("flag %d \n", statusFlag);
  return 0;
}


