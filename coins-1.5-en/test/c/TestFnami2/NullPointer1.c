int printf(char *s, ...);

int x;
void f() {}

void g(int *ip,void (*fp)(void)) {
  int *p=0;
  void (*q)(void)=0;
  printf("%d %d %d %d\n",ip==0,ip==p,fp==0,fp==q);
}

void h(int *p,void (*q)(void)) {
  printf("%d %d\n",&x==p,&f==q);
}

int main() {
  int *p=0;
  void (*q)(void)=0;
  printf("%d %d %d %d\n",&x==0,&x==p,&f==0,&f==q);
  g(&x,&f);
  h(p,q);
  return 0;
}
