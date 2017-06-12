int printf(char *s, ...);

char s2[]="%d %d\n";

struct S { short s0,s1; } sa[]={{0,1},{2,3},{4,5},{6,7}};
short aa[][3]={{0,1,2},{3,4,5},{6,7,8},{9,10,11}};

typedef struct S *P;
typedef short (*Q)[3];

P p0=sa+4-2,p1=sa+4-4;
Q q0=aa+4-2,q1=aa+4-4;

void f0(P p) {
  printf(s2,(p-2)->s0,(p-4)->s0);
}

void g0(Q q) {
  printf(s2,(*(q-2))[0],(*(q-4))[0]);
}

P op0(P p,int y) { return p-y; }
Q op1(Q q,int y) { return q-y; }

void f2(P p) {
  printf(s2,op0(p,2)->s0,op0(p,4)->s0);
}

void g2(Q q) {
  printf(s2,(*op1(q,2))[0],(*op1(q,4))[0]);
}

void main0g() {
  P p0=sa+4-2,p1=sa+4-4;
  Q q0=aa+4-2,q1=aa+4-4;
  printf(s2,p0->s0,p1->s0);
  printf(s2,(*q0)[0],(*q1)[0]);
}

void main0l() {
  struct S sa[]={{0,1},{2,3},{4,5},{6,7}};
  short aa[][3]={{0,1,2},{3,4,5},{6,7,8},{9,10,11}};
  P p0=sa+4-2,p1=sa+4-4;
  Q q0=aa+4-2,q1=aa+4-4;
  printf(s2,p0->s0,p1->s0);
  printf(s2,(*q0)[0],(*q1)[0]);
}

int main() {
  P p;
  Q q;

  printf(s2,p0->s0,p1->s0);
  printf(s2,(*q0)[0],(*q1)[0]);

  main0g();
  main0l();

  p=sa+4;
  printf(s2,(p-2)->s0,(p-4)->s0);
  printf(s2,(p-2)->s0,(p-4)->s0);
  q=aa+4;
  printf(s2,(*(q-2))[0],(*(q-4))[0]);
  printf(s2,(*(q-2))[0],(*(q-4))[0]);

  f0(sa+4); g0(aa+4);
  f2(sa+4); g2(aa+4);

  return 0;
}
