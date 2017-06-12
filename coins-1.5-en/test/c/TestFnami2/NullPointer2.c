int printf(char *s, ...);

void f(void *p,char *q) {
  printf("%d\n",p==q);
}

int main() {
  int *p0=0;
  int *p1=0L;
  int *p2=0LL;
  int *p3='\0';
  int *p4=0x0;
  int *p5=3-2-1;
  int *p6=16<<30;
  int *p7=(void *)(16<<30);
  void *p=0;
  printf("%d %d %d %d %d %d %d %d\n",p0==0,p1==0,p2==0,p3==0,p4==0,p5==0,p6==0,p7==0);
  printf("%d %d %d %d %d %d %d %d\n",p0==p,p1==p,p2==p,p3==p,p4==p,p5==p,p6==p,p7==p);
  printf("%d\n",(void *)p0==(char *)p);
  f(p0,(char *)p);
  return 0;
}
