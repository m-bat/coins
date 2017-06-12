/* Switch statement test (nested one) */
/* Switch with/without default, case with/without break. */
int printf(char *s, ...);

int main()
{
  int i, j, a, b;

  i = 0;
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
  printf("a = %d, b = %d\n", a, b);
  return a;
}
