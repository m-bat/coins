/* tplabelOpt1.c: Test of deleting useless labels. */

int main()
{
  int i, j, a, b, c;

  i = 1;
  c = 0;
  if (i > 0)
    c = 3;
 for (i = 0; i < 3; i++)
    c = c + i; 
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
  printf("%d %d %d \n", a, b, c);
  return 0;
}
