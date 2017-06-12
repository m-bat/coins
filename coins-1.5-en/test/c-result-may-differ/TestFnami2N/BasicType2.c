int printf(char *s, ...);

signed char sc;
short s;
int i;
long l;
long long ll;
unsigned char uc;
unsigned short us;
unsigned int ui;
unsigned long ul;
unsigned long long ull;
char c;
float f;
double d;
long double ld;

int main() {
  printf("%d %d %d %d %d\n",(int)sizeof sc,(int)sizeof  s,(int)sizeof  i,(int)sizeof  l,(int)sizeof  ll);
  printf("%d %d %d %d %d\n",(int)sizeof uc,(int)sizeof us,(int)sizeof ui,(int)sizeof ul,(int)sizeof ull);
  printf("%d %d %d %d\n",   (int)sizeof  c,(int)sizeof f,(int)sizeof d,(int)sizeof ld);
  return 0;
}
