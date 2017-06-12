/* tprecur1.c  Recursive function test */

int printf(char*, ...);

int p(int x,int n)
{
  int a;
  if (n == 0)
    a = 1;
  else {
    n = n -1;
    a = x * p(x , n);
  }
  return(a);
}

int main()
{
  int i;
  i = p(2,10);
  printf("%d \n", i);
}
