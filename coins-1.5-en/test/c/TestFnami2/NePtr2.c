int printf(char *s, ...);

char s3[]="%d %d %d\n";

struct S { int i0; int i1; } s;
union U { int i0; int i1; } u;
int a[]={ 0,10,20,30 };

void f0(int *p) {
  if(p!=&s.i0) printf("T"); else printf("-");
  if(p!=&s.i1) printf("T"); else printf("-");
  if(p!=0) printf("T"); else printf("-");
  printf("\n");
}

void g0(int *p) {
  if(p!=&u.i0) printf("T"); else printf("-");
  if(p!=&u.i1) printf("T"); else printf("-");
  if(p!=0) printf("T"); else printf("-");
  printf("\n");
}

void h0(int *p) {
  if(p!=&a[2]) printf("T"); else printf("-");
  if(p!=&a[4]) printf("T"); else printf("-");
  if(p!=0) printf("T"); else printf("-");
  printf("\n");
}

void f1(int *p) {
  if(&s.i0!=p) printf("T"); else printf("-");
  if(&s.i1!=p) printf("T"); else printf("-");
  if(0!=p) printf("T"); else printf("-");
  printf("\n");
}

void g1(int *p) {
  if(&u.i0!=p) printf("T"); else printf("-");
  if(&u.i1!=p) printf("T"); else printf("-");
  if(0!=p) printf("T"); else printf("-");
  printf("\n");
}

void h1(int *p) {
  if(&a[2]!=p) printf("T"); else printf("-");
  if(&a[4]!=p) printf("T"); else printf("-");
  if(0!=p) printf("T"); else printf("-");
  printf("\n");
}

void op(int *p,int *q) {
  if(p!=q) printf("T"); else printf("-");
}

void f2(int *p) {
  op(p,&s.i0); op(p,&s.i1); op(p,0);
  printf("\n");
}

void g2(int *p) {
  op(p,&u.i0); op(p,&u.i1); op(p,0);
  printf("\n");
}

void h2(int *p) {
  op(p,&a[2]); op(p,&a[4]); op(p,0);
  printf("\n");
}

void main0g() {
  if(&s.i0!=&s.i0) printf("T"); else printf("-");
  if(&s.i0!=&s.i1) printf("T"); else printf("-");
  if(&s.i0!=0) printf("T"); else printf("-");
  printf("\n");
  if(&s.i1!=&s.i0) printf("T"); else printf("-");
  if(&s.i1!=&s.i1) printf("T"); else printf("-");
  if(&s.i1!=0) printf("T"); else printf("-");
  printf("\n");
  if(0!=&s.i0) printf("T"); else printf("-");
  if(0!=&s.i1) printf("T"); else printf("-");
  if((void *)0!=(void *)0) printf("T"); else printf("-");
  printf("\n");
  if(&u.i0!=&s.i0) printf("T"); else printf("-");
  if(&u.i0!=&s.i1) printf("T"); else printf("-");
  if(&u.i0!=0) printf("T"); else printf("-");
  printf("\n");
  if(&u.i0!=&u.i0) printf("T"); else printf("-");
  if(&u.i0!=&u.i1) printf("T"); else printf("-");
  if(&u.i0!=0) printf("T"); else printf("-");
  printf("\n");
  if(&u.i1!=&u.i0) printf("T"); else printf("-");
  if(&u.i1!=&u.i1) printf("T"); else printf("-");
  if(&u.i1!=0) printf("T"); else printf("-");
  printf("\n");
  if(0!=&u.i0) printf("T"); else printf("-");
  if(0!=&u.i1) printf("T"); else printf("-");
  if((void *)0!=(void *)0) printf("T"); else printf("-");
  printf("\n");
  if(&a[2]!=&a[2]) printf("T"); else printf("-");
  if(&a[2]!=&a[4]) printf("T"); else printf("-");
  if(&a[2]!=0) printf("T"); else printf("-");
  printf("\n");
  if(&a[4]!=&a[2]) printf("T"); else printf("-");
  if(&a[4]!=&a[4]) printf("T"); else printf("-");
  if(&a[4]!=0) printf("T"); else printf("-");
  printf("\n");
  if(0!=&a[2]) printf("T"); else printf("-");
  if(0!=&a[4]) printf("T"); else printf("-");
  if((void *)0!=(void *)0) printf("T"); else printf("-");
  printf("\n");
}

void main0l() {
  struct S { int i0; int i1; } s;
  union U { int i0; int i1; } u;
  int a[]={ 0,10,20,30 };

  if(&s.i0!=&s.i0) printf("T"); else printf("-");
  if(&s.i0!=&s.i1) printf("T"); else printf("-");
  if(&s.i0!=0) printf("T"); else printf("-");
  printf("\n");
  if(&s.i1!=&s.i0) printf("T"); else printf("-");
  if(&s.i1!=&s.i1) printf("T"); else printf("-");
  if(&s.i1!=0) printf("T"); else printf("-");
  printf("\n");
  if(0!=&s.i0) printf("T"); else printf("-");
  if(0!=&s.i1) printf("T"); else printf("-");
  if((void *)0!=(void *)0) printf("T"); else printf("-");
  printf("\n");
  if(&u.i0!=&s.i0) printf("T"); else printf("-");
  if(&u.i0!=&s.i1) printf("T"); else printf("-");
  if(&u.i0!=0) printf("T"); else printf("-");
  printf("\n");
  if(&u.i0!=&u.i0) printf("T"); else printf("-");
  if(&u.i0!=&u.i1) printf("T"); else printf("-");
  if(&u.i0!=0) printf("T"); else printf("-");
  printf("\n");
  if(&u.i1!=&u.i0) printf("T"); else printf("-");
  if(&u.i1!=&u.i1) printf("T"); else printf("-");
  if(&u.i1!=0) printf("T"); else printf("-");
  printf("\n");
  if(0!=&u.i0) printf("T"); else printf("-");
  if(0!=&u.i1) printf("T"); else printf("-");
  if((void *)0!=(void *)0) printf("T"); else printf("-");
  printf("\n");
  if(&a[2]!=&a[2]) printf("T"); else printf("-");
  if(&a[2]!=&a[4]) printf("T"); else printf("-");
  if(&a[2]!=0) printf("T"); else printf("-");
  printf("\n");
  if(&a[4]!=&a[2]) printf("T"); else printf("-");
  if(&a[4]!=&a[4]) printf("T"); else printf("-");
  if(&a[4]!=0) printf("T"); else printf("-");
  printf("\n");
  if(0!=&a[2]) printf("T"); else printf("-");
  if(0!=&a[4]) printf("T"); else printf("-");
  if((void *)0!=(void *)0) printf("T"); else printf("-");
  printf("\n");
}

void main1() {
  int *p;
  p=&s.i0; if(p!=&s.i0) printf("T"); else printf("-"); if(p!=&s.i0) printf("T"); else printf("-");
  p=&s.i0; if(p!=&s.i1) printf("T"); else printf("-"); if(p!=&s.i1) printf("T"); else printf("-");
  p=&s.i0; if(p!=0) printf("T"); else printf("-"); if(p!=0) printf("T"); else printf("-");
  printf("\n");
  p=&s.i1; if(p!=&s.i0) printf("T"); else printf("-"); if(p!=&s.i0) printf("T"); else printf("-");
  p=&s.i1; if(p!=&s.i1) printf("T"); else printf("-"); if(p!=&s.i1) printf("T"); else printf("-");
  p=&s.i1; if(p!=0) printf("T"); else printf("-"); if(p!=0) printf("T"); else printf("-");
  printf("\n");
  p=0; if(p!=&s.i0) printf("T"); else printf("-"); if(p!=&s.i0) printf("T"); else printf("-");
  p=0; if(p!=&s.i1) printf("T"); else printf("-"); if(p!=&s.i1) printf("T"); else printf("-");
  p=0; if(p!=0) printf("T"); else printf("-"); if(p!=0) printf("T"); else printf("-");
  printf("\n");
  p=&u.i0; if(p!=&s.i0) printf("T"); else printf("-"); if(p!=&s.i0) printf("T"); else printf("-");
  p=&u.i0; if(p!=&s.i1) printf("T"); else printf("-"); if(p!=&s.i1) printf("T"); else printf("-");
  p=&u.i0; if(p!=0) printf("T"); else printf("-"); if(p!=0) printf("T"); else printf("-");
  printf("\n");
}

void main2() {
  int *p;
  p=&u.i0; if(p!=&u.i0) printf("T"); else printf("-"); if(p!=&u.i0) printf("T"); else printf("-");
  p=&u.i0; if(p!=&u.i1) printf("T"); else printf("-"); if(p!=&u.i1) printf("T"); else printf("-");
  p=&u.i0; if(p!=0) printf("T"); else printf("-"); if(p!=0) printf("T"); else printf("-");
  printf("\n");
  p=&u.i1; if(p!=&u.i0) printf("T"); else printf("-"); if(p!=&u.i0) printf("T"); else printf("-");
  p=&u.i1; if(p!=&u.i1) printf("T"); else printf("-"); if(p!=&u.i1) printf("T"); else printf("-");
  p=&u.i1; if(p!=0) printf("T"); else printf("-"); if(p!=0) printf("T"); else printf("-");
  printf("\n");
  p=0; if(p!=&u.i0) printf("T"); else printf("-"); if(p!=&u.i0) printf("T"); else printf("-");
  p=0; if(p!=&u.i1) printf("T"); else printf("-"); if(p!=&u.i1) printf("T"); else printf("-");
  p=0; if(p!=0) printf("T"); else printf("-"); if(p!=0) printf("T"); else printf("-");
  printf("\n");
}

void main3() {
  int *p;
  p=&a[2]; if(p!=&a[2]) printf("T"); else printf("-"); if(p!=&a[2]) printf("T"); else printf("-");
  p=&a[2]; if(p!=&a[4]) printf("T"); else printf("-"); if(p!=&a[4]) printf("T"); else printf("-");
  p=&a[2]; if(p!=0) printf("T"); else printf("-"); if(p!=0) printf("T"); else printf("-");
  printf("\n");
  p=&a[4]; if(p!=&a[2]) printf("T"); else printf("-"); if(p!=&a[2]) printf("T"); else printf("-");
  p=&a[4]; if(p!=&a[4]) printf("T"); else printf("-"); if(p!=&a[4]) printf("T"); else printf("-");
  p=&a[4]; if(p!=0) printf("T"); else printf("-"); if(p!=0) printf("T"); else printf("-");
  printf("\n");
  p=0; if(p!=&a[2]) printf("T"); else printf("-"); if(p!=&a[2]) printf("T"); else printf("-");
  p=0; if(p!=&a[4]) printf("T"); else printf("-"); if(p!=&a[4]) printf("T"); else printf("-");
  p=0; if(p!=0) printf("T"); else printf("-"); if(p!=0) printf("T"); else printf("-");
  printf("\n");
}

void main4() {
  int *p;
  p=&s.i0; if(&s.i0!=p) printf("T"); else printf("-"); if(&s.i0!=p) printf("T"); else printf("-");
  p=&s.i0; if(&s.i1!=p) printf("T"); else printf("-"); if(&s.i1!=p) printf("T"); else printf("-");
  p=&s.i0; if(0!=p) printf("T"); else printf("-"); if(0!=p) printf("T"); else printf("-");
  printf("\n");
  p=&s.i1; if(&s.i0!=p) printf("T"); else printf("-"); if(&s.i0!=p) printf("T"); else printf("-");
  p=&s.i1; if(&s.i1!=p) printf("T"); else printf("-"); if(&s.i1!=p) printf("T"); else printf("-");
  p=&s.i1; if(0!=p) printf("T"); else printf("-"); if(0!=p) printf("T"); else printf("-");
  printf("\n");
  p=0; if(&s.i0!=p) printf("T"); else printf("-"); if(&s.i0!=p) printf("T"); else printf("-");
  p=0; if(&s.i1!=p) printf("T"); else printf("-"); if(&s.i1!=p) printf("T"); else printf("-");
  p=0; if(0!=p) printf("T"); else printf("-"); if(0!=p) printf("T"); else printf("-");
  printf("\n");
  p=&u.i0; if(&s.i0!=p) printf("T"); else printf("-"); if(&s.i0!=p) printf("T"); else printf("-");
  p=&u.i0; if(&s.i1!=p) printf("T"); else printf("-"); if(&s.i1!=p) printf("T"); else printf("-");
  p=&u.i0; if(0!=p) printf("T"); else printf("-"); if(0!=p) printf("T"); else printf("-");
  printf("\n");
}

void main5() {
  int *p;
  p=&u.i0; if(&u.i0!=p) printf("T"); else printf("-"); if(&u.i0!=p) printf("T"); else printf("-");
  p=&u.i0; if(&u.i1!=p) printf("T"); else printf("-"); if(&u.i1!=p) printf("T"); else printf("-");
  p=&u.i0; if(0!=p) printf("T"); else printf("-"); if(0!=p) printf("T"); else printf("-");
  printf("\n");
  p=&u.i1; if(&u.i0!=p) printf("T"); else printf("-"); if(&u.i0!=p) printf("T"); else printf("-");
  p=&u.i1; if(&u.i1!=p) printf("T"); else printf("-"); if(&u.i1!=p) printf("T"); else printf("-");
  p=&u.i1; if(0!=p) printf("T"); else printf("-"); if(0!=p) printf("T"); else printf("-");
  printf("\n");
  p=0; if(&u.i0!=p) printf("T"); else printf("-"); if(&u.i0!=p) printf("T"); else printf("-");
  p=0; if(&u.i1!=p) printf("T"); else printf("-"); if(&u.i1!=p) printf("T"); else printf("-");
  p=0; if(0!=p) printf("T"); else printf("-"); if(0!=p) printf("T"); else printf("-");
  printf("\n");
}

void main6() {
  int *p;
  p=&a[2]; if(&a[2]!=p) printf("T"); else printf("-"); if(&a[2]!=p) printf("T"); else printf("-");
  p=&a[2]; if(&a[4]!=p) printf("T"); else printf("-"); if(&a[4]!=p) printf("T"); else printf("-");
  p=&a[2]; if(0!=p) printf("T"); else printf("-"); if(0!=p) printf("T"); else printf("-");
  printf("\n");
  p=&a[4]; if(&a[2]!=p) printf("T"); else printf("-"); if(&a[2]!=p) printf("T"); else printf("-");
  p=&a[4]; if(&a[4]!=p) printf("T"); else printf("-"); if(&a[4]!=p) printf("T"); else printf("-");
  p=&a[4]; if(0!=p) printf("T"); else printf("-"); if(0!=p) printf("T"); else printf("-");
  printf("\n");
  p=0; if(&a[2]!=p) printf("T"); else printf("-"); if(&a[2]!=p) printf("T"); else printf("-");
  p=0; if(&a[4]!=p) printf("T"); else printf("-"); if(&a[4]!=p) printf("T"); else printf("-");
  p=0; if(0!=p) printf("T"); else printf("-"); if(0!=p) printf("T"); else printf("-");
  printf("\n");
}

int main() {
  main0g();
  main0l();
  main1();
  main2();
  main3();
  main4();
  main5();
  main6();

  f0(&s.i0); f0(&s.i1); f0(0); f0(&u.i0);
  g0(&u.i0); g0(&u.i1); g0(0);
  h0(&a[2]); h0(&a[4]); h0(0);

  f1(&s.i0); f1(&s.i1); f1(0); f1(&u.i0);
  g1(&u.i0); g1(&u.i1); g1(0);
  h1(&a[2]); h1(&a[4]); h1(0);

  f2(&s.i0); f2(&s.i1); f2(0); f2(&u.i0);
  g2(&u.i0); g2(&u.i1); g2(0);
  h2(&a[2]); h2(&a[4]); h2(0);

  return 0;
}
