/* Switch statement test (3) */
/*              Consecutive case, no case body, no default. */
int printf(char *s, ...);

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
  printf("a = %d, b = %d\n", a, b);
  return a;
}
