int printf(char *s, ...);

void f(int x) {
  printf("%d\n",x);
}

int main() {
  f(12345);
  return 0;
}
