/* tpsizeof12.c:  sizeof bug */
/*     Hasegawa mail 040206 */

int printf(char *, ...);

int main()
{
  char c;
  printf("sizeof +c is %d, sizeof(c+1) is %d sizeof int is %d\n", 
         sizeof(+c), sizeof(c+1), sizeof(int));
}
