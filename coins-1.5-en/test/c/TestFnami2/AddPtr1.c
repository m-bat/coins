int printf(char *s, ...);

char s4[]="%d %d %d %d\n";

struct S { short s0,s1; } sa[]={{0,1},{2,3},{4,5},{6,7}};
short aa[][3]={{0,1,2},{3,4,5},{6,7,8},{9,10,11}};

typedef struct S *P;
typedef short (*Q)[3];

P p0=sa+2,p1=sa+4,p2=2+sa,p3=4+sa;
Q q0=aa+2,q1=aa+4,q2=2+aa,q3=4+aa;

void f0(P p) {
  printf(s4,(p+2)[-1].s0,(p+4)[-1].s0,(2+p)[-1].s0,(4+p)[-1].s0);
}

void g0(Q q) {
  printf(s4,(q+2)[-1][0],(q+4)[-1][0],(2+q)[-1][0],(4+q)[-1][0]);
}

P op0(P p,int y) { return p+y; }
Q op1(Q q,int y) { return q+y; }
P op2(P p,int x) { return x+p; }
Q op3(Q q,int x) { return x+q; }

void f2(P p) {
  printf(s4,op0(p,2)[-1].s0,op0(p,4)[-1].s0,op2(p,2)[-1].s0,op2(p,4)[-1].s0);
}

void g2(Q q) {
  printf(s4,op1(q,2)[-1][0],op1(q,4)[-1][0],op3(q,2)[-1][0],op3(q,4)[-1][0]);
}

void main0g() {
  P p0=sa+2,p1=sa+4,p2=2+sa,p3=4+sa;
  Q q0=aa+2,q1=aa+4,q2=2+aa,q3=4+aa;
  printf(s4,p0[-1].s0,p1[-1].s0,p2[-1].s0,p3[-1].s0);
  printf(s4,q0[-1][0],q1[-1][0],q2[-1][0],q3[-1][0]);
}

void main0l() {
  struct S sa[]={{0,1},{2,3},{4,5},{6,7}};
  short aa[][3]={{0,1,2},{3,4,5},{6,7,8},{9,10,11}};
  P p0=sa+2,p1=sa+4,p2=2+sa,p3=4+sa;
  Q q0=aa+2,q1=aa+4,q2=2+aa,q3=4+aa;
  printf(s4,p0[-1].s0,p1[-1].s0,p2[-1].s0,p3[-1].s0);
  printf(s4,q0[-1][0],q1[-1][0],q2[-1][0],q3[-1][0]);
}

int main() {
  P p;
  Q q;

  printf(s4,p0[-1].s0,p1[-1].s0,p2[-1].s0,p3[-1].s0);
  printf(s4,q0[-1][0],q1[-1][0],q2[-1][0],q3[-1][0]);

  main0g();
  main0l();

  p=sa;
  printf(s4,(p+2)[-1].s0,(p+4)[-1].s0,(2+p)[-1].s0,(4+p)[-1].s0);
  printf(s4,(p+2)[-1].s0,(p+4)[-1].s0,(2+p)[-1].s0,(4+p)[-1].s0);
  q=aa;
  printf(s4,(q+2)[-1][0],(q+4)[-1][0],(2+q)[-1][0],(4+q)[-1][0]);
  printf(s4,(q+2)[-1][0],(q+4)[-1][0],(2+q)[-1][0],(4+q)[-1][0]);

  f0(sa); g0(aa);
  f2(sa); g2(aa);

  return 0;
}
