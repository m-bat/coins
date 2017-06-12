int printf(char *s, ...);

struct S { int i,j; };
struct S s0={30,40};
struct S const s1={50,60};

void f() { printf("f()\n"); }
void g() { printf("g()\n"); }

void f0(double x) {
  struct S *p;
  void *q;
  printf("%d\n",x?10:20);
  printf("%d\n",(x?s0:s1).j);
  x?f():g();
  printf("%d\n",(x?&s0:&s1)->j);
  p=x?&s0:0;
  if(p!=0) printf("%d\n",p->j); else printf("null\n");
  q=x?&s0:(void *)&s1;
  printf("%d\n",((struct S *)q)->j);
}

int main() {
  double x;
  struct S *p;
  void *q;

  printf("%d\n",1.?10:20);
  printf("%d\n",(1.?s0:s1).j);
  1.?f():g();
  printf("%d\n",(1.?&s0:&s1)->j);
  p=1.?&s0:0;
  if(p!=0) printf("%d\n",p->j); else printf("null\n");
  q=1.?&s0:(void *)&s1;
  printf("%d\n",((struct S *)q)->j);
  printf("%d\n",0.?10:20);
  printf("%d\n",(0.?s0:s1).j);
  0.?f():g();
  printf("%d\n",(0.?&s0:&s1)->j);
  p=0.?&s0:0;
  if(p!=0) printf("%d\n",p->j); else printf("null\n");
  q=0.?&s0:(void *)&s1;
  printf("%d\n",((struct S *)q)->j);

  x=1.;
  printf("%d\n",x?10:20);
  printf("%d\n",(x?s0:s1).j);
  x?f():g();
  printf("%d\n",(x?&s0:&s1)->j);
  p=x?&s0:0;
  if(p!=0) printf("%d\n",p->j); else printf("null\n");
  q=x?&s0:(void *)&s1;
  printf("%d\n",((struct S *)q)->j);
  x=-0.;
  printf("%d\n",x?10:20);
  printf("%d\n",(x?s0:s1).j);
  x?f():g();
  printf("%d\n",(x?&s0:&s1)->j);
  p=x?&s0:0;
  if(p!=0) printf("%d\n",p->j); else printf("null\n");
  q=x?&s0:(void *)&s1;
  printf("%d\n",((struct S *)q)->j);

  f0(1.); f0(-0.);

  return 0;
}
