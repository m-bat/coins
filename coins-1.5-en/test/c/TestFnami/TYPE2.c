int printf(char *s, ...);

typedef int A[3];
typedef A *AP;
typedef AP B[5];
typedef B *BP;

B b;

int main() {
  A a={123,456,789};
  BP bp=&b;
  b[2]=&a;
  printf("%d\n",bp[0][2][0][1]);
  return 0;
}
