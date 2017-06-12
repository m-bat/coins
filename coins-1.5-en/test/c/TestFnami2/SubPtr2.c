int printf(char *s, ...);

char s4[]="%d %d %d %d\n";

struct S { short s0,s1; } sa[]={{0,1},{2,3},{4,5},{6,7}};
short aa[][3]={{0,1,2},{3,4,5},{6,7,8},{9,10,11}};

typedef struct S *P;
typedef short (*Q)[3];

void f0(P p) {
  printf(s4,p-sa,p-(sa+2),sa-p,(sa+2)-p);
}

void g0(Q q) {
  printf(s4,q-aa,q-(aa+2),aa-q,(aa+2)-q);
}

int op0(P px,P py) { return px-py; }
int op1(Q qx,Q qy) { return qx-qy; }

void f2(P p) {
  printf(s4,op0(p,sa),op0(p,sa+2),op0(sa,p),op0(sa+2,p));
}

void g2(Q q) {
  printf(s4,op1(q,aa),op1(q,aa+2),op1(aa,q),op1(aa+2,q));
}

void main0g() {
  int a0=(sa+4)-sa,a1=(sa+4)-(sa+2),a2=sa-(sa+4),a3=(sa+2)-(sa+4);
  int b0=(aa+4)-aa,b1=(aa+4)-(aa+2),b2=aa-(aa+4),b3=(aa+2)-(aa+4);
  printf(s4,a0,a1,a2,a3);
  printf(s4,b0,b1,b2,b3);
}

void main0l() {
  struct S sa[]={{0,1},{2,3},{4,5},{6,7}};
  short aa[][3]={{0,1,2},{3,4,5},{6,7,8},{9,10,11}};
  int a0=(sa+4)-sa,a1=(sa+4)-(sa+2),a2=sa-(sa+4),a3=(sa+2)-(sa+4);
  int b0=(aa+4)-aa,b1=(aa+4)-(aa+2),b2=aa-(aa+4),b3=(aa+2)-(aa+4);
  printf(s4,a0,a1,a2,a3);
  printf(s4,b0,b1,b2,b3);
}

int main() {
  P p;
  Q q;

  main0g();
  main0l();

  p=sa+4;
  printf(s4,p-sa,p-(sa+2),sa-p,(sa+2)-p);
  printf(s4,p-sa,p-(sa+2),sa-p,(sa+2)-p);
  q=aa+4;
  printf(s4,q-aa,q-(aa+2),aa-q,(aa+2)-q);
  printf(s4,q-aa,q-(aa+2),aa-q,(aa+2)-q);

  f0(sa+4); g0(aa+4);
  f2(sa+4); g2(aa+4);

  return 0;
}
