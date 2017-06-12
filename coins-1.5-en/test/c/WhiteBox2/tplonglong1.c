/* tplonglong1.c  long long  Kitamura 041116 */

int printf(char*, ...);

long l;
long long ll;
short s;
int a[10];

int main()
{
  s = ll = 2;
  a[(long)ll] = 3;
  a[3] = a[s];
  a[4] = a[ll];
  printf("%lld %hd %d %d %d \n", ll, s, a[2], a[3], a[4]);
  return 0;
}

