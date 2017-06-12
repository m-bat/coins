int printf(char *s, ...);

char s3[]="%d %d %d\n";

struct S { int i0; int i1; } s;
union U { int i0; int i1; } u;
int a[]={ 0,10,20,30 };

void f0(int *p) {
  printf(s3,p==&s.i0,p==&s.i1,p==0);
}

void g0(int *p) {
  printf(s3,p==&u.i0,p==&u.i1,p==0);
}

void h0(int *p) {
  printf(s3,p==&a[2],p==&a[4],p==0);
}

void f1(int *p) {
  printf(s3,&s.i0==p,&s.i1==p,0==p);
}

void g1(int *p) {
  printf(s3,&u.i0==p,&u.i1==p,0==p);
}

void h1(int *p) {
  printf(s3,&a[2]==p,&a[4]==p,0==p);
}

int op(int *p,int *q) { return p==q; }

void f2(int *p) {
  printf(s3,op(p,&s.i0),op(p,&s.i1),op(p,0));
}

void g2(int *p) {
  printf(s3,op(p,&u.i0),op(p,&u.i1),op(p,0));
}

void h2(int *p) {
  printf(s3,op(p,&a[2]),op(p,&a[4]),op(p,0));
}

void main0g() {
  int a00=&s.i0==&s.i0,a01=&s.i0==&s.i1,a02=&s.i0==0;
  int a10=&s.i1==&s.i0,a11=&s.i1==&s.i1,a12=&s.i1==0;
  int a20=    0==&s.i0,a21=    0==&s.i1,a22=(void *)0==(void *)0;
  int a30=&u.i0==&s.i0,a31=&u.i0==&s.i1,a32=&u.i0==0;

  int b00=&u.i0==&u.i0,b01=&u.i0==&u.i1,b02=&u.i0==0;
  int b10=&u.i1==&u.i0,b11=&u.i1==&u.i1,b12=&u.i1==0;
  int b20=    0==&u.i0,b21=    0==&u.i1,b22=(void *)0==(void *)0;

  int c00=&a[2]==&a[2],c01=&a[2]==&a[4],c02=&a[2]==0;
  int c10=&a[4]==&a[2],c11=&a[4]==&a[4],c12=&a[4]==0;
  int c20=    0==&a[2],c21=    0==&a[4],c22=(void *)0==(void *)0;

  printf(s3,a00,a01,a02);
  printf(s3,a10,a11,a12);
  printf(s3,a20,a21,a22);
  printf(s3,a30,a31,a32);

  printf(s3,b00,b01,b02);
  printf(s3,b10,b11,b12);
  printf(s3,b20,b21,b22);

  printf(s3,c00,c01,c02);
  printf(s3,c10,c11,c12);
  printf(s3,c20,c21,c22);
}

void main0l() {
  struct S { int i0; int i1; } s;
  union U { int i0; int i1; } u;
  int a[]={ 0,10,20,30 };

  int a00=&s.i0==&s.i0,a01=&s.i0==&s.i1,a02=&s.i0==0;
  int a10=&s.i1==&s.i0,a11=&s.i1==&s.i1,a12=&s.i1==0;
  int a20=    0==&s.i0,a21=    0==&s.i1,a22=(void *)0==(void *)0;
  int a30=&u.i0==&s.i0,a31=&u.i0==&s.i1,a32=&u.i0==0;

  int b00=&u.i0==&u.i0,b01=&u.i0==&u.i1,b02=&u.i0==0;
  int b10=&u.i1==&u.i0,b11=&u.i1==&u.i1,b12=&u.i1==0;
  int b20=    0==&u.i0,b21=    0==&u.i1,b22=(void *)0==(void *)0;

  int c00=&a[2]==&a[2],c01=&a[2]==&a[4],c02=&a[2]==0;
  int c10=&a[4]==&a[2],c11=&a[4]==&a[4],c12=&a[4]==0;
  int c20=    0==&a[2],c21=    0==&a[4],c22=(void *)0==(void *)0;

  printf(s3,a00,a01,a02);
  printf(s3,a10,a11,a12);
  printf(s3,a20,a21,a22);
  printf(s3,a30,a31,a32);

  printf(s3,b00,b01,b02);
  printf(s3,b10,b11,b12);
  printf(s3,b20,b21,b22);

  printf(s3,c00,c01,c02);
  printf(s3,c10,c11,c12);
  printf(s3,c20,c21,c22);
}

void main1() {
  int *p;
  p=&s.i0;
  printf(s3,p==&s.i0,p==&s.i1,p==0);
  printf(s3,p==&s.i0,p==&s.i1,p==0);
  p=&s.i1;
  printf(s3,p==&s.i0,p==&s.i1,p==0);
  printf(s3,p==&s.i0,p==&s.i1,p==0);
  p=0;
  printf(s3,p==&s.i0,p==&s.i1,p==0);
  printf(s3,p==&s.i0,p==&s.i1,p==0);
  p=&u.i0;
  printf(s3,p==&s.i0,p==&s.i1,p==0);
  printf(s3,p==&s.i0,p==&s.i1,p==0);
}

void main2() {
  int *p;
  p=&u.i0;
  printf(s3,p==&u.i0,p==&u.i1,p==0);
  printf(s3,p==&u.i0,p==&u.i1,p==0);
  p=&u.i1;
  printf(s3,p==&u.i0,p==&u.i1,p==0);
  printf(s3,p==&u.i0,p==&u.i1,p==0);
  p=0;
  printf(s3,p==&u.i0,p==&u.i1,p==0);
  printf(s3,p==&u.i0,p==&u.i1,p==0);
}

void main3() {
  int *p;
  p=&a[2];
  printf(s3,p==&a[2],p==&a[4],p==0);
  printf(s3,p==&a[2],p==&a[4],p==0);
  p=&a[4];
  printf(s3,p==&a[2],p==&a[4],p==0);
  printf(s3,p==&a[2],p==&a[4],p==0);
  p=0;
  printf(s3,p==&a[2],p==&a[4],p==0);
  printf(s3,p==&a[2],p==&a[4],p==0);
}

void main4() {
  int *p;
  p=&s.i0;
  printf(s3,&s.i0==p,&s.i1==p,0==p);
  printf(s3,&s.i0==p,&s.i1==p,0==p);
  p=&s.i1;
  printf(s3,&s.i0==p,&s.i1==p,0==p);
  printf(s3,&s.i0==p,&s.i1==p,0==p);
  p=0;
  printf(s3,&s.i0==p,&s.i1==p,0==p);
  printf(s3,&s.i0==p,&s.i1==p,0==p);
  p=&u.i0;
  printf(s3,&s.i0==p,&s.i1==p,0==p);
  printf(s3,&s.i0==p,&s.i1==p,0==p);
}

void main5() {
  int *p;
  p=&u.i0;
  printf(s3,&u.i0==p,&u.i1==p,0==p);
  printf(s3,&u.i0==p,&u.i1==p,0==p);
  p=&u.i1;
  printf(s3,&u.i0==p,&u.i1==p,0==p);
  printf(s3,&u.i0==p,&u.i1==p,0==p);
  p=0;
  printf(s3,&u.i0==p,&u.i1==p,0==p);
  printf(s3,&u.i0==p,&u.i1==p,0==p);
}

void main6() {
  int *p;
  p=&a[2];
  printf(s3,&a[2]==p,&a[4]==p,0==p);
  printf(s3,&a[2]==p,&a[4]==p,0==p);
  p=&a[4];
  printf(s3,&a[2]==p,&a[4]==p,0==p);
  printf(s3,&a[2]==p,&a[4]==p,0==p);
  p=0;
  printf(s3,&a[2]==p,&a[4]==p,0==p);
  printf(s3,&a[2]==p,&a[4]==p,0==p);
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
