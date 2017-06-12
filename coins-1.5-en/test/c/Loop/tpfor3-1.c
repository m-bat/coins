/* tpfor3-1.c  -- Simple nested loop (global variable) */

  int a, b, i, k;

main()
{
  a = 1;
  for (i = 1; i < 10; i++) 
    for (k = 1 ; k < 10; k++) 
      b = a + i * k;
  printf("a=%d b=%d i=%d k=%d\n",a,b,i,k); /* SF030620 */
}
