/* tpFuncNakata2 020327 error report */

int g(int y) {
  return y;
}

int func(int x) {
  return x + 1;
}

int main()
{
  printf("%d \n", g(3)+func(4));
}
