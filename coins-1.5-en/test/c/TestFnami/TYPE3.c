int printf(char *s, ...);

typedef int A[3];
typedef A *AP;

int main() {
  A a={123,456,789};
  AP ap=&a;
  printf("%d\n",ap[0][1]);
  return 0;
}
