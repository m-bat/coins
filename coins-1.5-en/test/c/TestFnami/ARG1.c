int printf(char *s, ...);

int main(int argc,char *argv[]) {
  argc--; argv++;
  while(argc--) printf("arg: %s\n",*argv++);
  return 0;
}
