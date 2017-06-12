/* tpstructscope1.c: structure scope */

struct ST { int a; int b; } st0;
struct ST { int a; int b; } st1;

int main()
{
  struct ST { int a; int b; } st2; /* Parse Error "ST was redefined" */
  int x;

  x = st0.a + st0.b + st1.a + st1.b + st2.a + st2.b;
  prinitf("This test will cause 'redefinition of struct' error\n"); /* SF030609 */
  return 0;
}

