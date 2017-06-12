/* backSample,c: backend example www.lsi-j.co.jp */

int printf(char*, ...);

int foo(int n)
{
  int i, sum;
  sum = 0;
  for (i = 0; ; i++) {
    sum += i * i;
  if (sum >= n)
    break;
  }
  return i;
}

int main()
{
  int x;
  x = foo(100);
  printf("%d \n", x);
  return 0;
}

