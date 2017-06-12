int printf(char *s, ...);

int a=0x7ffffff8;
int b=0x80000008;

int main() {
  int i;
  for(i=a;i!=b;i++) {
    printf("%d\n",i);
    switch(i) {
    case 2147483647: printf("2^31-1\n"); break;
    case -2147483647-1: printf("-2^31\n"); break;
    case -2147483647: printf("-2^31+1\n"); break;
    }
  }
  return 0;
}
