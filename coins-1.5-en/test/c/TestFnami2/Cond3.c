int printf(char *s, ...);

int i;

struct S { int i,j; };
struct S s0={30,40};
struct S const s1={50,60};

void f() { printf("f()\n"); }
void g() { printf("g()\n"); }

void f0(int *x) {
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
  int *x;
  struct S *p;
  void *q;

  printf("%d\n",&i?10:20);
  printf("%d\n",(&i?s0:s1).j);
  &i?f():g();
  printf("%d\n",(&i?&s0:&s1)->j);
  p=&i?&s0:0;
  if(p!=0) printf("%d\n",p->j); else printf("null\n");
  q=&i?&s0:(void *)&s1;
  printf("%d\n",((struct S *)q)->j);
  printf("%d\n",(void *)0?10:20);
  printf("%d\n",((void *)0?s0:s1).j);
  (void *)0?f():g();
  printf("%d\n",((void *)0?&s0:&s1)->j);
  p=(void *)0?&s0:0;
  if(p!=0) printf("%d\n",p->j); else printf("null\n");
  q=(void *)0?&s0:(void *)&s1;
  printf("%d\n",((struct S *)q)->j);

  x=&i;
  printf("%d\n",x?10:20);
  printf("%d\n",(x?s0:s1).j);
  x?f():g();
  printf("%d\n",(x?&s0:&s1)->j);
  p=x?&s0:0;
  if(p!=0) printf("%d\n",p->j); else printf("null\n");
  q=x?&s0:(void *)&s1;
  printf("%d\n",((struct S *)q)->j);
  x=0;
  printf("%d\n",x?10:20);
  printf("%d\n",(x?s0:s1).j);
  x?f():g();
  printf("%d\n",(x?&s0:&s1)->j);
  p=x?&s0:0;
  if(p!=0) printf("%d\n",p->j); else printf("null\n");
  q=x?&s0:(void *)&s1;
  printf("%d\n",((struct S *)q)->j);

  f0(&i); f0(0);

  return 0;
}
