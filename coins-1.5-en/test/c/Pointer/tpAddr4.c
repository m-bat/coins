/* tpAddr4.c -- address of variables and sscanf */


int k;

int
main()
{
  int i, j;
  int *p;
  p = &j;

  sscanf("10 20 30", "%d %d %d", &i, p, &k);
  i = i + 1;
  printf("%d %d %d\n", i, j, k);
  return 0;
}

