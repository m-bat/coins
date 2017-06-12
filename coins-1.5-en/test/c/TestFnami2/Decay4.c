int printf(char *s, ...);

int f(int x) { return x+4; }
int g(int (*fp)(int)) { return (*fp)(3); }

int main() {
  int (*fp)(int)=f;
  printf("%d\n",(*fp)(3));
  printf("%d\n",g(f));
  return 0;
}
