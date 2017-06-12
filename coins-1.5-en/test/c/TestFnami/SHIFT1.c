int printf(char *s, ...);

int main() {
  int a=(-1U)>>31;
  printf("%d\n",a);
  return 0;
}
