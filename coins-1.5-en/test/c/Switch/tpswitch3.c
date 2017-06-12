/* tpswitch3.c: Switch statement test (3) */
/*              Consecutive case, no case body, no default. */
int main()
{
  int i, j, a, b;

  i = 1;
  j = 2;
  b = 0;
  switch (i) {
  case 0:
  case 1:
  case 2:
    a = 0;
    break;
  case 3:
    b = 1;
    a = 2;
    switch (j) {
    case 1:
      break;
    case 2:
      a = 3;
    case 3:
    case 4: ;
    };
  default:
    a = 4;
  };

  printf("i,j,a,b = %d,%d,%d,%d\n",i,j,a,b); /* SF030609 */

  return a;
}
