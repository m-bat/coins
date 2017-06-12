int printf(char *s, ...);

int main() {
  long a=2147483647;
  a=a+1;
  printf("%ld\n",a);
  return 0;
}
