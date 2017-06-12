/* tpstructscope2.c: structure scope */

struct ST { int a; int b; } st0;

int main()
{
  struct ST { int a; int b; } st1;
  int x;

  st0.a = 10; /* SF030609 */
  st0.b = 20; /* SF030609 */
  st1.a = 30; /* SF030609 */
  st1.b = 40; /* SF030609 */

  x = st0.a + st0.b + st1.a + st1.b;

  printf("st0 = [%d,%d]\n",st0.a,st0.b); /* SF030609 */
  printf("st1 = [%d,%d]\n",st1.a,st1.b); /* SF030609 */
  printf("x = %d\n",x); /* SF030609 */

  return 0;
}
