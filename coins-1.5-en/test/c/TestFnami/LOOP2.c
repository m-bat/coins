int printf(char *s, ...);

int main() {
  int i;
  for(i=0;i<32;i++) {
    printf("%x\n",1+i);
  }
  return 0;
}
