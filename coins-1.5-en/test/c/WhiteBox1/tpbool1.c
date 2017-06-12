/* Exp/tpbool1.c */
/*   Nakata mail 021006, Yamano mail 021216 */

int printf(char*, ...);

int main()
{
  int a, b, c, d;
  a = 7;
  b = 3;
  c = 2;
  b = a & b > c;
  d = a && b > c;
  printf("%d %d \n", b, d);
  return 0;
}
