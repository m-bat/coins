int printf(char *s, ...);

short a[3]={ 123,456,789 };

void f(char *p,short *q) {
  printf("%c %d\n",p[2],q[2]);
}

int main() {
  char *cp;
  short *p;
  printf("%d\n",(int)sizeof *a);
  cp="ABCD"+1; p=a+1;
  printf("%c %d\n",*cp,*p);
  f("ABCD",a);
  return 0;
}
