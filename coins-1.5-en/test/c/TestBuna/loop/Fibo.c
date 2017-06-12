/* Fibonacci_num   */
   /* print number */
int scanf(char *s, ...);
int printf(char *s, ...);
/*int exit( int a); */

int  MAX_VAL= 99;
unsigned  result[99];
unsigned  Fibo(int n)
{
  int i;
    result[1] = 1;
    result[2] = 1;

  for (i = 3; i <= n; i++)
      result[i] = result[i - 1] + result[i - 2];

  return (result[n]);
}

main()
{
  int n;
  scanf("%d", &n);

/*  if (n > MAX_VAL -1) */
/*    exit(0); */

   printf("\nFibonacci_num = %u\n", Fibo(n));
}
