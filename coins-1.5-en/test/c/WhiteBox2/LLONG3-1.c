/* LLONG3-1 */

int printf(char *s, ...);

long long a;
long long b;

int main() {
  a=2147483648LL;
  b = a+a;
  printf("%lld %lld %d\n",a, b, sizeof(b));
  return 0;
}
