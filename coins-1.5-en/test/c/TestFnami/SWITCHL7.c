int printf(char *s, ...);

unsigned int a=0x7ffffff8;
unsigned int b=0x80000008;

int main() {
  unsigned int i;
  for(i=a;i!=b;i++) {
    printf("%u\n",i);
    switch(i) {
    case 2147483647: printf("2^31-1\n"); break;
    case 2147483648U: printf("2^31\n"); break;
    case 2147483649U: printf("2^31+1\n"); break;
    }
  }
  return 0;
}
