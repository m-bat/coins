/* fibLoop2: Print Fibonacci sequence by loop */

int a[100];
int n, i;
void printf(char *, ...);

int 
main() 
{
  a[0] = 1;
  a[1] = 1;
  n = 50;
  i = 2;
  while (i < n) {
    a[i] = a[i-1] + a[i-2];
    i = i + 1;
  }
  for (i = 2; i < n; i = i + 1)
    printf("fib %d = %d\n", i, a[i-1]);
  return 0;
}
