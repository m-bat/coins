int printf(char *s, ...);

int main() {
  unsigned int i;
  for(i=-8;i!=8;i++) {
    printf("%u\n",i);
    switch(i) {
    case 4294967295U: printf("2^32-1\n"); break;
    case 4294967296U: printf("2^32\n"); break;
    case 0: printf("0\n"); break;
    case 1: printf("1\n"); break;
    }
  }
  printf("This test will cause 'duplicate case value' error.\n");
  return 0;
}
