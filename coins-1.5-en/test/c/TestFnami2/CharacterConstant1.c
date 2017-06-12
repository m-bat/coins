int printf(char *s, ...);

void f(int c) {
  printf("%d %d\n",'0'>>c,'\xD0'>>c);
}

int main() {
  int c;
  printf("%d %d %d\n",'0','\xD0',(int)sizeof 'A');
  printf("%d %d\n",'0'>>2,'\xD0'>>2);
  c=3;
  printf("%d %d\n",'0'>>c,'\xD0'>>c);
  printf("%d %d\n",'0'>>c,'\xD0'>>c);
  f(4);
  return 0;
}
