/* tpfor3.c  -- Simple nested loop */
int printf(char *s, ...);

main()
{
  int a, b, i, k;
 
  a = 1;
  for (i = 1; i < 10; i++) 
    for (k = 1 ; k < 10; k++) 
      b = a + i * k;
  
  printf("b = %d\n", b);
}
