/* tpswitch0.c: Switch statement test 0 (simple one) */

int i;
int main()
{
  int j;
  i = 1;
  switch (i) {
  case 1:  i = 2;
  case 2:  i = 1;
  default: i = 3;
  };
  j = 1;
  switch (j) {
  case 1:  j = 2;
  case 2:  j = 1;
  };

  printf("i,j = %d,%d\n",i,j); /* SF030609 */

  return 0;
}
