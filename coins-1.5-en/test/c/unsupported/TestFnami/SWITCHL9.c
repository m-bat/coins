int printf(char *s, ...);

int main() {
  long long i;
  for(i=1;i<0x1000000000000000;i*=2) {
    switch(i) {
    case 4294967296: printf("2^32\n"); break;
    case 2147483648: printf("2^31\n"); break;
    }
  }
  return 0;
}
