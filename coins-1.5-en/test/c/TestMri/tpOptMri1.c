/* tpOptMri1.c  (Fujise 2002/8/19) */

int
main()
{
  int i;
  int j;

  i = 1; /* SF030509 */

  j = (i+1) + (i+1); /* may add extraneous load */

  printf("i,j = %d,%d\n",i,j); /* SF030509 */

  return 0;
}
