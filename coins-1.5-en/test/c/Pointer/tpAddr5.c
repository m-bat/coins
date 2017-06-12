/* tpAddr5.c -- address of variables and sscanf (by Fukuoka) */

int
main(void)
{
  int *i;
  int a=10;

  i = &a;
  *i = *i + 10;
  a = a + 40;

  printf("%d %d \n", *i, a);
  return 0;
}

