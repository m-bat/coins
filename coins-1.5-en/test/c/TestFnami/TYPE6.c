int printf(char *s, ...);

int main() {
  static int x=123,y=456,z=789;
  int *pa[3];
  pa[0]=&x; pa[1]=&y; pa[2]=&z;
  printf("%d\n",pa[1][0]);
  return 0;
}
