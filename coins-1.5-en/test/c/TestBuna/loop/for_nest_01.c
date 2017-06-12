/* for_nest_1  simple nested for statement */
int printf(char *s, ...);

main()
{
  int a, b, i, k;
 
  a = 0;
  for (i = 1; i < 10; i++) 
    for (k = 1 ; k < 10; k++) 
      a = a + i * k;
  printf("a = %d\n", a);
}
