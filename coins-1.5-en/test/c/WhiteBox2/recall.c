/* recall.c  Recursive call */

int func(int x)
{
  if (x==0) return 0;
  return 1 + func(x-1);
}

int main()
{
  int i , ret = 0;
  for (i = 0 ; i < 50 ; i++) ret += func(10000);
  printf("%d\n", ret);
  return 0;
}
