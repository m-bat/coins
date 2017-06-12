/* tpsignedchar1.c  signed char test */
/*   Mori mail 040307 coins-bug 2004/10 */

int printf(char*, ...);

int main()
{
  signed char c = -1;
  signed short s = -1;
  printf("%d %d \n", c, s);
}
