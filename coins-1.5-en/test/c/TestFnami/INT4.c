int printf(char *s, ...);

int main() {
  int a=2147483647;
  int a1=2147483648;
  int b=-2147483647-1;
  int b1=-2147483649;
  printf("%d\n",a);
  printf("%d\n",a1);
  printf("%d\n",b);
  printf("%d\n",b1);
  return 0;
}
