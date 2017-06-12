int printf(char *s, ...);

int main() {
  int a=2147483647;
  int b=-2147483647-1;
  printf("%d\n",a);
  printf("%d\n",b);
  return 0;
}
