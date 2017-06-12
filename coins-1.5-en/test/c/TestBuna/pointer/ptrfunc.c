int printf(char* f, ...);

int add(int x, int y) {
  return x+y;
}

int main(int argc, char **argv) {
  int a;
  int b;
  int (*func)(int,int);
  a=2;
  b=3;
  func = add;

  printf("anser=%d\n", func(a,b));
}
