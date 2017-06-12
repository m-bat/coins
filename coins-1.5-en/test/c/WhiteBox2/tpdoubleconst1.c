/* tpdoubleconst1.c  float const / double const test */
/*   Mori mail 040307 coins-bug 2004/8 */

int printf(char*, ...);

int main()
{
  float f = 1.0;
  double d = 1.0;
  printf("%f %f \n", f, d);
}
