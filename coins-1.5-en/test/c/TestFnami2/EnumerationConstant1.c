int printf(char *s, ...);

enum e { A=127,B=255 };

void f(int c) {
  printf("%d %d\n",(-A)>>c,(-B)>>c);
}

int main() {
  int c;
  printf("%d %d %d\n",A,B,(int)sizeof A);
  printf("%d %d\n",(-A)>>2,(-B)>>2);
  c=3;
  printf("%d %d\n",(-A)>>c,(-B)>>c);
  printf("%d %d\n",(-A)>>c,(-B)>>c);
  f(4);
  return 0;
}
