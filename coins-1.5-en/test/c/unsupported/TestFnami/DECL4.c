int printf(char *s, ...);

int f1(void) { return 123; }
int (*f2(void))(void) { return f1; }
int (*(*f3(void))(void))(void) { return f2; }
int (*(*(*f4(void))(void))(void))(void) { return f3; }
int (*(*(*(*f5(void))(void))(void))(void))(void) { return f4; }
int (*(*(*(*(*f6(void))(void))(void))(void))(void))(void) { return f5; }

int main() {
  int (*(*(*(*(*(*p)(void))(void))(void))(void))(void))(void)=f6;
  printf("%d\n",p()()()()()());
  return 0;
}
