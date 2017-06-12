/* ptrToArray1.c Pointer to array Hasegawa mail 030702 */

int printf(char*, ...);

int (*a)[3];  /* Pointer to array with 3 int elements */ 
int *b[3];    /* Array with 3 pointers to int */
int x[3] = {1, 2, 3};
int y = 10;
int z = 20;
int main()
{
  a = &x;
  b[0] = &y;
  b[1] = &z;
  b[2] = &x[1];
  printf("%d %d %d %d %d %d \n", 
         (*a)[0], (*a)[1], (*a)[2], *b[0], *b[1], *b[2]);
  return 0;
}

