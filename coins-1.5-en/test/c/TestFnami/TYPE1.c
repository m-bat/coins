int printf(char *s, ...);

typedef int A[3];
typedef A *AP;
typedef AP B[5];
typedef B *BP;
typedef BP C[7];
typedef C *CP;

B b;
C c;

int main() {
  A a={123,456,789};
  CP cp=&c;
  c[3]=&b;
  b[2]=&a;
  printf("%d\n",cp[0][3][0][2][0][1]);
  return 0;
}
