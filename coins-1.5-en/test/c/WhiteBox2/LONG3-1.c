/* LONG3-1.c */

int printf(char *s, ...);

long a;
long b;

int main() {
  a=2147483648L;
  b = a+a;
  printf("%ld %ld %d\n",a, b, sizeof(b));
  return 0;
}
