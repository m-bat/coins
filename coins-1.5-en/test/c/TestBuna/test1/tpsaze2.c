/* tpsaze2.c */
int printf(char *s, ...);

main()
{
  int a,b,c,i;

  a = 1;
  b = 2;
  for (i = 1; i < 10; i++) {
    c = a + b;
  }
  printf("c = %d\n", c);
}
