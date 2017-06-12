/* tpoptspace1.c  -- optspace test */

int printf(char*, ...);

int f(int p)
{
  return p;
}

int main()
{
  int a, b;
  a = f(10);
  b = a*5;
  printf("%d %d", a, b);
  return 0; 
}

