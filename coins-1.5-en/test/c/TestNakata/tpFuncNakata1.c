/* tpFuncNakata1 020327 error report */

int func(int x) { /* x become STATIC */
  return x + 1;
}

int main() {
  int b;
  b = func(3);
  printf("%d \n", b);
}

