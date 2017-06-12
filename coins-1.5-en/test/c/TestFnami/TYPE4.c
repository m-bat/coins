int printf(char *s, ...);

int main() {
  int a[]={123,456,789};
  int (*ap)[3]=&a;
  printf("%d\n",ap[0][1]);
  return 0;
}
