/* tpstatic1: static declaration */

static int a, b, c[10];

static int f()
{
  static int count = 0;
  count++;
  return count;
}

/*void main() /* SF030620 */
main() /* SF030620 */
{
  static d; /* syntax error ? */
  a = f();
  b = f();
  d = f();
  printf("%d %d %d \n", a, b, d);
}
