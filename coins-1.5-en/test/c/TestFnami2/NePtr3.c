int printf(char *s, ...);

struct S;
int a[10];

int main() {
  struct S *p=0;
  struct S const *pc=0;
  struct S volatile *pv=0;
  struct S const volatile *pcv=0;
  int (*q)[]=&a;
  typedef int A[];
  A const *qc=&a;
  A volatile *qv=&a;
  A const volatile *qcv=&a;
  printf("%d %d %d\n",p!=0,q!=&a,q!=0);
  printf("%d %d %d\n",pc!=0,qc!=&a,qc!=0);
  printf("%d %d %d\n",pv!=0,qv!=&a,qv!=0);
  printf("%d %d %d\n",pcv!=0,qcv!=&a,qcv!=0);
  printf("%d %d %d\n",0!=p,&a!=q,0!=q);
  printf("%d %d %d\n",0!=pc,&a!=qc,0!=qc);
  printf("%d %d %d\n",0!=pv,&a!=qv,0!=qv);
  printf("%d %d %d\n",0!=pcv,&a!=qcv,0!=qcv);
  return 0;
}
