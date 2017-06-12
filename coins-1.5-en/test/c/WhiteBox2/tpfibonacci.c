/* tpfibonacci.c:  Fibonacci seq */

int printf(char*, ...);

int a, b, c;
int fib(int p)
{
  if (p <= 0)
    return 0;
  else if (p <= 2)
    return 1;
  else 
    return fib(p - 1) + fib(p - 2);
}

int main()
{
  a = fib(5);
  b = fib(2) + fib(3);
  c = fib(fib(4) + fib(5));
  printf("a,b,c = %d,%d,%d\n",a,b,c);
  return 0;
}
