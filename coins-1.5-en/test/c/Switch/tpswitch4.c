/* tpswitch4.c: Switch statement test (simple one) */
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
  case 3:
    a = 0;
    break;
  case 4:
    b = 1;
  case 5:
    a = b+2;
    break;
  case 6:
    a = 0;
    break;
  case 7:
    b = 1;
  case 8:
    a = b+2;
    break;
  case 9:
    a = 0;
    break;
  case 10:
    b = 1;
  case 11:
    a = b+2;
    break;
  default:
    a = 3;
    break;
  };

  printf("i,a,b = %d,%d,%d\n",i,a,b); /* SF030609 */

  return a;
}

