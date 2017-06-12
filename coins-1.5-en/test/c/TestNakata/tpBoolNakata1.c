/* tpBoolNakata1 020327 error report */

int main() {
  int x, b;
  b=1;
  x = b==5;
  if (x) b=8;
  printf("%d \n", b);
  return 0;
}

