/* tpswitch5.c: Switch statement test (non numeric case constant) */
int main()
{
  int i, j, a, b;
  char c;
  enum color { red, yellow, green } signal;

  i = 1;
  b = 0;
  c = 'a';
  switch (c) {
  case 'a':
    a = 0;
    break;
  case 'b':
    b = 1;
  case 'c':
    a = b+2;
    break;
  default:
    a = 3;
    break;
  };
  signal = red;
  switch (signal) {
  case red:
    a = 0;
    break;
  case yellow:
    b = 1;
  case green:
    a = b+2;
    break;
  default:
    a = 3;
    break;
  };

  printf("red,yellow,green = %d,%d,%d\n",red,yellow,green); /* SF030609 */
  printf("i,a,b,c = %d,%d,%d,%d\n",i,a,b,c); /* SF030609 */

  return a;
}
