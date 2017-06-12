/* tpswitch2.c: Switch statement test (nested one) */
/*              Switch with/without default, case with/without break. */
int main()
{
  int i, j, a, b;

  i = 1;
  j = 2;
  b = 0;
  switch (i+1) {
  case 0:
    a = 0;
    break;
  case 1:
    b = 1;
    switch (j) {
    case 1:
      a = 2;
      break;
    case 2:
      a = 3;
    case 3:
      b = 5;
    };
  default:
    a = 3;
    break;
  };

  printf("i,j,a,b = %d,%d,%d,%d\n",i,j,a,b); /* SF030609 */

  return a;
}
