int printf(char *s, ...);

int main(int argc,char *argv[]) {
  switch(argc) {
  case 2: printf("two\n"); break;
  default: printf("unknown\n"); break;
  case 1: printf("one\n"); break;
  case 3: printf("many\n"); break;
  }
  return 0;
}
