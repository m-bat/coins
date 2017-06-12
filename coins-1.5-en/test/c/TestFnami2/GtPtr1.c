int printf(char *s, ...);

char s2[]="%d %d\n";

struct S { int i0; int i1; } s;
union U { int i0; int i1; } u;
int a[]={ 0,10,20,30 };

void f0(int *p) {
  printf(s2,p>&s.i0,p>&s.i1);
}

void g0(int *p) {
  printf(s2,p>&u.i0,p>&u.i1);
}

void h0(int *p) {
  printf(s2,p>&a[2],p>&a[4]);
}

void f1(int *p) {
  printf(s2,&s.i0>p,&s.i1>p);
}

void g1(int *p) {
  printf(s2,&u.i0>p,&u.i1>p);
}

void h1(int *p) {
  printf(s2,&a[2]>p,&a[4]>p);
}

int op(int *p,int *q) { return p>q; }

void f2(int *p) {
  printf(s2,op(p,&s.i0),op(p,&s.i1));
}

void g2(int *p) {
  printf(s2,op(p,&u.i0),op(p,&u.i1));
}

void h2(int *p) {
  printf(s2,op(p,&a[2]),op(p,&a[4]));
}

void main0g() {
  int a00=&s.i0>&s.i0,a01=&s.i0>&s.i1;
  int a10=&s.i1>&s.i0,a11=&s.i1>&s.i1;

  int b00=&u.i0>&u.i0,b01=&u.i0>&u.i1;
  int b10=&u.i1>&u.i0,b11=&u.i1>&u.i1;

  int c00=&a[2]>&a[2],c01=&a[2]>&a[4];
  int c10=&a[4]>&a[2],c11=&a[4]>&a[4];

  printf(s2,a00,a01);
  printf(s2,a10,a11);

  printf(s2,b00,b01);
  printf(s2,b10,b11);

  printf(s2,c00,c01);
  printf(s2,c10,c11);
}

void main0l() {
  struct S { int i0; int i1; } s;
  union U { int i0; int i1; } u;
  int a[]={ 0,10,20,30 };

  int a00=&s.i0>&s.i0,a01=&s.i0>&s.i1;
  int a10=&s.i1>&s.i0,a11=&s.i1>&s.i1;

  int b00=&u.i0>&u.i0,b01=&u.i0>&u.i1;
  int b10=&u.i1>&u.i0,b11=&u.i1>&u.i1;

  int c00=&a[2]>&a[2],c01=&a[2]>&a[4];
  int c10=&a[4]>&a[2],c11=&a[4]>&a[4];

  printf(s2,a00,a01);
  printf(s2,a10,a11);

  printf(s2,b00,b01);
  printf(s2,b10,b11);

  printf(s2,c00,c01);
  printf(s2,c10,c11);
}

void main1() {
  int *p;
  p=&s.i0;
  printf(s2,p>&s.i0,p>&s.i1);
  printf(s2,p>&s.i0,p>&s.i1);
  p=&s.i1;
  printf(s2,p>&s.i0,p>&s.i1);
  printf(s2,p>&s.i0,p>&s.i1);
}

void main2() {
  int *p;
  p=&u.i0;
  printf(s2,p>&u.i0,p>&u.i1);
  printf(s2,p>&u.i0,p>&u.i1);
  p=&u.i1;
  printf(s2,p>&u.i0,p>&u.i1);
  printf(s2,p>&u.i0,p>&u.i1);
}

void main3() {
  int *p;
  p=&a[2];
  printf(s2,p>&a[2],p>&a[4]);
  printf(s2,p>&a[2],p>&a[4]);
  p=&a[4];
  printf(s2,p>&a[2],p>&a[4]);
  printf(s2,p>&a[2],p>&a[4]);
}

void main4() {
  int *p;
  p=&s.i0;
  printf(s2,&s.i0>p,&s.i1>p);
  printf(s2,&s.i0>p,&s.i1>p);
  p=&s.i1;
  printf(s2,&s.i0>p,&s.i1>p);
  printf(s2,&s.i0>p,&s.i1>p);
}

void main5() {
  int *p;
  p=&u.i0;
  printf(s2,&u.i0>p,&u.i1>p);
  printf(s2,&u.i0>p,&u.i1>p);
  p=&u.i1;
  printf(s2,&u.i0>p,&u.i1>p);
  printf(s2,&u.i0>p,&u.i1>p);
}

void main6() {
  int *p;
  p=&a[2];
  printf(s2,&a[2]>p,&a[4]>p);
  printf(s2,&a[2]>p,&a[4]>p);
  p=&a[4];
  printf(s2,&a[2]>p,&a[4]>p);
  printf(s2,&a[2]>p,&a[4]>p);
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

  f0(&s.i0); f0(&s.i1);
  g0(&u.i0); g0(&u.i1);
  h0(&a[2]); h0(&a[4]);

  f1(&s.i0); f1(&s.i1);
  g1(&u.i0); g1(&u.i1);
  h1(&a[2]); h1(&a[4]);

  f2(&s.i0); f2(&s.i1);
  g2(&u.i0); g2(&u.i1);
  h2(&a[2]); h2(&a[4]);

  return 0;
}
