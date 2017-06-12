/* tpinit6-2.c  Initial value without array bound specification */
/*            nest level 3 or greater. */
/* (Decl) */

int printf(char*, ...);

int x[] = { 11, 12, 13 };
int a[][3] = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 10, 11}};
int aa[][2][3] = {{{0, 1, 2}, {3, 4, 5}}, {{6, 7, 8}, {9, 10, 11}}};
main()
{
  int i = 0, b, c, d;

  b = x[2];
  c = aa[0][1][0];
  d = a[2][2];
  printf("b=%d c=%d d=%d\n",b,c, d);
  return 0;
}

