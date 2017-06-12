/* tpenum2.c:  Enumeration 2 */

int a, b, c;
enum level {low = 5, medium = 10, high, limit};
enum level c1, c2, c3, c4;

int main()
{
  c1 = low;
  c2 = medium;
  c3 = high;
  c4 = limit;

  printf("c1,c2,c3,c4 = %d,%d,%d,%d\n", c1,c2,c3,c4); /* SF030509 */

  return 0;
}

