int printf(char *s, ...);

char c[10];
char c[10];
short s[];
short s[20];
int i[30];
int i[];
long l[40][];
long l[][50];
long long ll[][60];
long long ll[70][];
float f[][];
float f[80][90];
double d[10][];
double d[10][20];
long double ld[][30];
long double ld[40][30];

int main() {
  printf("%d\n",(int)sizeof c);
  printf("%d\n",(int)sizeof s);
  printf("%d\n",(int)sizeof i);
  printf("%d %d\n",(int)sizeof l);
  printf("%d %d\n",(int)sizeof ll);
  printf("%d %d\n",(int)sizeof f);
  printf("%d %d\n",(int)sizeof d);
  printf("%d %d\n",(int)sizeof ld);
  return 0;
}
