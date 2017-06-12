int printf(char *s, ...);

int x;
int a[];
struct s *sp0;

void f(int *ip,int (*ap)[],struct s *sp) {
  void *p=ip,*q=ap,*r=sp;
  printf("%d %d %d\n",p==&x,q==&a,r==sp0);
  printf("%d %d %d\n",&x==p,&a==q,sp0==r);
}

void g(void *p,void *q,void *r) {
  printf("%d %d %d\n",p==&x,q==&a,r==sp0);
  printf("%d %d %d\n",&x==p,&a==q,sp0==r);
}

void init(void);

int main() {
  void *p,*q,*r;
  int *ip;
  int (*ap)[];
  struct s *sp;
  init();
  p=&x;
  q=&a;
  r=sp0;
  ip=p;
  ap=q;
  sp=r;
  printf("%d %d %d\n",ip==&x,ap==&a,sp==sp0);
  printf("%d %d %d\n",&x==ip,&a==ap,sp0==sp);
  f(&x,&a,sp0);
  g(&x,&a,sp0);
  return 0;
}

int a[3];
struct s { int x; } s;

void init(void) {
  sp0=&s;
}
