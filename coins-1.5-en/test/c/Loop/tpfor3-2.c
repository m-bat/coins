/* tpfor3-2.c  -- Simple nested loop (summation) */
main()
{
  int a, b, i, k;

  a = 0;
  for (i = 1; i < 10; i++)
    for (k = 1 ; k < 10; k++)
      a = a + i * k;
  printf("a,i,k = %d,%d,%d\n",a,i,k); /* SF030620 */
}
