/* tpswitch1.c: Switch statement test (simple one) */
int main()
{
  int i, j, a, b;

  i = 1;
  b = 0;
  switch (i) {
  case 0:
    a = 0;
    break;
  case 1:
    b = 1;
  case 2:
    a = b+2;
    break;
  default:
    a = 3;
    break;
  };

  printf("i,a,b = %d,%d,%d\n",i,a,b); /* SF030609 */

  return a;
}
