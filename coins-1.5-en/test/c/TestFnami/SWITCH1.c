int printf(char *s, ...);

int main(int argc,char *argv[]) {
  switch(argc) {
  case 1: printf("one\n"); break;
  case 2: printf("two\n"); break;
  case 3: printf("many\n"); break;
  default: printf("unknown\n"); break;
  }
  return 0;
}
