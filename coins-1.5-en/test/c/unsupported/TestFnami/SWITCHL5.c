int printf(char *s, ...);

int a=0x7ffffff8;
int b=0x80000008;

int main() {
  int i;
  for(i=a;i!=b;i++) {
    printf("%d\n",i);
    switch(i) {
    case 2147483647: printf("2^31-1\n"); break;
    case 2147483648: printf("2^31\n"); break;
    case -2147483648: printf("-2^31\n"); break;
    case -2147483647: printf("-2^31+1\n"); break;
    }
  }
  printf("This test will cause 'duplicate case value' error.\n");
  return 0;
}
