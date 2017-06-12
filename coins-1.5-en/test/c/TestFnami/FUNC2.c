int printf(char *s, ...);

int f(void) {
  return 12345;
}

int main() {
  printf("%d\n",f());
  return 0;
}
