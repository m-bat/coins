int printf(char *s, ...);

void f(char x1,short x2,int x3,long x4,float x5,double x6,char x7,short x8,int x9,long x10,float x11,double x12,char x13,short x14,int x15,long x16,float x17,double x18,char x19,short x20,int x21,long x22,float x23,double x24,char x25,short x26,int x27,long x28,float x29,double x30,int x31) {
  printf("%d %d %d %ld %g  %g %d %d %d %ld  %g %g %d %d %d  %ld %g %g %d %d  %d %ld %g %g %d  %d %d %ld %g %g  %d\n",x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19,x20,x21,x22,x23,x24,x25,x26,x27,x28,x29,x30,x31);
}

int main() {
  f(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31);
  return 0;
}
